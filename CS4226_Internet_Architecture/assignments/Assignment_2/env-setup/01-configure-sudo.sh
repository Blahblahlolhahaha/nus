#!/usr/bin/env bash
# Step 1 - Configure passwordless sudo.
#
# Gives the current user passwordless sudo, which the multi-hour P4 build needs
# (install-p4dev-v8.sh calls sudo many times and must not stall on a prompt).
#
# Makes both edits up front, so no manual visudo round-trip is needed:
#   * every simple "<who> ALL=(...) ALL" rule in /etc/sudoers becomes
#     "<who> ALL=(...) NOPASSWD:ALL"
#   * "<you> ALL=(ALL:ALL) NOPASSWD:ALL" is appended as the LAST line of
#     /etc/sudoers, after @includedir, so no other rule can override it
#
# Safety: the original /etc/sudoers is backed up to /var/backups/cs4226-p4-env/
# (restore with ./99-revert-sudo-nopasswd.sh), the edited copy is validated with
# `visudo -cf`, and it is moved into place atomically.
#
# You will be asked for your password once.

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root

log "Caching sudo credentials (enter your password if prompted)"
sudo -v

if sudo test -e "$SUDOERS_BACKUP"; then
    ok "Original /etc/sudoers already backed up at $SUDOERS_BACKUP (keeping it)"
else
    log "Backing up /etc/sudoers to $SUDOERS_BACKUP"
    sudo install -d -o root -g root -m 0700 "$(dirname "$SUDOERS_BACKUP")"
    sudo cp -p /etc/sudoers "$SUDOERS_BACKUP"
fi

orig=$(mktemp)
tmp=$(mktemp)
trap 'rm -f "$orig" "$tmp"' EXIT
user_rule="$USER ALL=(ALL:ALL) NOPASSWD:ALL"
marker='# Added by CS4226 env-setup/01-configure-sudo.sh'

sudo cat /etc/sudoers >"$orig"
[[ -s "$orig" ]] || die "Could not read /etc/sudoers"

# 1. Make the simple "X ALL=(...) ALL" rules NOPASSWD.
#    Comments, Defaults, aliases, @include lines and rules that already carry a
#    tag do not match and are left untouched.
# 2. Drop any earlier copy of our rule (re-runs) and append it as the very last line.
# 3. cat -s collapses the blank lines that repeated runs would otherwise accumulate.
{
    sed -E 's/^([[:space:]]*%?[^[:space:]#@]+[[:space:]]+ALL[[:space:]]*=[[:space:]]*(\([^)]*\))?)[[:space:]]*ALL[[:space:]]*$/\1 NOPASSWD:ALL/' "$orig" \
        | grep -vxF -e "$user_rule" -e "$marker" || true
    printf '\n%s\n%s\n' "$marker" "$user_rule"
} | cat -s >"$tmp"

# Sanity check: only our two lines may disappear, nothing else.
if (( $(wc -l <"$tmp") < $(wc -l <"$orig") - 3 )); then
    die "Edited sudoers is unexpectedly shorter than the original; /etc/sudoers was NOT changed."
fi

if cmp -s "$tmp" "$orig"; then
    ok "/etc/sudoers is already configured"
else
    log "Changes to /etc/sudoers:"
    diff -u --label /etc/sudoers --label /etc/sudoers.new "$orig" "$tmp" || true

    sudo visudo -cf "$tmp" >/dev/null || die "Edited sudoers failed validation; /etc/sudoers was NOT changed."
    # Stage next to the target, then rename (atomic on the same filesystem).
    sudo install -o root -g root -m 0440 "$tmp" /etc/sudoers.cs4226.new
    sudo mv -f /etc/sudoers.cs4226.new /etc/sudoers
    ok "/etc/sudoers updated"
fi

log "Verifying: clearing the credential cache and running sudo non-interactively"
sudo -k
if sudo -n true 2>/dev/null; then
    ok "Passwordless sudo works for $USER."
    next_steps \
        "Run ./02-update-system.sh"
else
    # Our rule is the last one in /etc/sudoers, so this only happens when sudo
    # rules come from somewhere else (e.g. LDAP/SSSD via 'sudoers:' in nsswitch.conf).
    next_steps \
        "sudo still asks for a password even though '$user_rule' is the last rule in /etc/sudoers." \
        "Sudo rules probably come from another source. Check:" \
        "  grep sudoers /etc/nsswitch.conf        (anything besides 'files'?)" \
        "  sudo -l                                (which rules apply to you?)" \
        "Ask the VM administrator to grant NOPASSWD, then re-run ./01-configure-sudo.sh."
    exit 1
fi
