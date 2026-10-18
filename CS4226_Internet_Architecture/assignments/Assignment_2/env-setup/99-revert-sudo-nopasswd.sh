#!/usr/bin/env bash
# Optional - Turn the sudo password prompt back on after the setup.
#
# Restores the original /etc/sudoers saved by ./01-configure-sudo.sh, which
# removes passwordless sudo again (for you and for the rules that were switched
# to NOPASSWD).
# The secure_path drop-in from ./04-configure-p4.sh lives in /etc/sudoers.d and
# is kept, since the assignment needs it (e.g. `sudo make ...` must find the P4 tools).

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root

if ! sudo test -e "$SUDOERS_BACKUP"; then
    ok "No backup at $SUDOERS_BACKUP; nothing to revert."
    exit 0
fi

log "Changes that will be undone:"
sudo diff -u --label /etc/sudoers --label "$SUDOERS_BACKUP" /etc/sudoers "$SUDOERS_BACKUP" || true

sudo visudo -cf "$SUDOERS_BACKUP" >/dev/null || die "Backup $SUDOERS_BACKUP does not validate; not restoring it."
log "Restoring /etc/sudoers from $SUDOERS_BACKUP"
sudo install -o root -g root -m 0440 "$SUDOERS_BACKUP" /etc/sudoers.cs4226.new
# Remove the backup while sudo is still passwordless; the staged copy above holds its content.
sudo rm -f "$SUDOERS_BACKUP"
sudo mv -f /etc/sudoers.cs4226.new /etc/sudoers

sudo -k
if sudo -n true 2>/dev/null; then
    warn "sudo is still passwordless; a rule that predates this setup grants NOPASSWD."
else
    ok "sudo asks for a password again."
fi
