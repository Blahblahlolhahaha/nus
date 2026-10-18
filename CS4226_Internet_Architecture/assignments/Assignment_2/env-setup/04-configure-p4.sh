#!/usr/bin/env bash
# Step 4 - Configure P4.
#
#   * apt-get install openvswitch-testcontroller, then stop + disable it
#     (errors from stop/disable are expected and ignored)
#   * append "source /opt/p4/p4setup.bash" to ~/.bashrc
#   * prepend /opt/p4/p4-guide/bin:/opt/p4/behavioral-model/tools:/opt/p4/p4dev-python-venv/bin
#     to sudo's secure_path
#
# The secure_path change is written to a validated drop-in under /etc/sudoers.d
# (a later "Defaults secure_path" overrides the one in /etc/sudoers) rather than
# editing /etc/sudoers with visudo by hand.
#
# Finally it starts a fresh interactive bash (as a new terminal would) and checks
# that ~/.bashrc activates p4dev-python-venv and the prompt shows it.
#
# Does not need to run inside the screen/tmux session used for step 3.

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root
require_passwordless_sudo

[[ -f "$P4_ROOT/p4setup.bash" ]] || die "$P4_ROOT/p4setup.bash not found. Did ./03-build-p4.sh finish successfully?"

# --- openvswitch-testcontroller ----------------------------------------------
log "Installing openvswitch-testcontroller"
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y openvswitch-testcontroller

log "Stopping and disabling openvswitch-testcontroller (errors here are OK)"
sudo systemctl stop openvswitch-testcontroller || warn "stop failed (expected on some systems)"
sudo systemctl disable openvswitch-testcontroller || warn "disable failed (expected on some systems)"

# --- ~/.bashrc ---------------------------------------------------------------
bashrc_line="source $P4_ROOT/p4setup.bash"
if grep -qxF "$bashrc_line" "$HOME/.bashrc" 2>/dev/null; then
    ok "~/.bashrc already contains: $bashrc_line"
else
    log "Appending '$bashrc_line' to ~/.bashrc"
    printf '\n%s\n' "$bashrc_line" >>"$HOME/.bashrc"
fi

# --- sudo secure_path ----------------------------------------------------------
current_path=$(sudo sed -nE 's/^Defaults[[:space:]]+secure_path[[:space:]]*=[[:space:]]*"?([^"]*)"?[[:space:]]*$/\1/p' /etc/sudoers | tail -n 1)
if [[ -z "$current_path" ]]; then
    current_path="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/snap/bin"
    warn "No 'Defaults secure_path' found in /etc/sudoers; using Ubuntu default: $current_path"
fi
if [[ "$current_path" == "$P4_SECURE_PATH_PREFIX:"* ]]; then
    new_path="$current_path"
else
    new_path="$P4_SECURE_PATH_PREFIX:$current_path"
fi

tmp=$(mktemp)
trap 'rm -f "$tmp"' EXIT
printf '# Added by CS4226 env-setup/04-configure-p4.sh\nDefaults secure_path="%s"\n' "$new_path" >"$tmp"

log "Installing $SUDOERS_SECUREPATH_FILE"
install_sudoers_fragment "$tmp" "$SUDOERS_SECUREPATH_FILE"

# shellcheck disable=SC2016  # $PATH must expand inside the sudo shell
sudo_path=$(sudo sh -c 'echo "$PATH"')
if [[ "$sudo_path" == "$P4_SECURE_PATH_PREFIX:"* ]]; then
    ok "sudo PATH is now: $sudo_path"
else
    next_steps \
        "sudo's PATH does not start with the P4 directories:" \
        "  $sudo_path" \
        "Another 'Defaults secure_path' later in the sudoers configuration overrides it." \
        "Fix it by hand:" \
        "  1. sudo visudo" \
        "  2. Add '$P4_SECURE_PATH_PREFIX:' to the START of the secure_path value." \
        "  3. Check: sudo sh -c 'echo \$PATH'"
    exit 1
fi

# --- Check what a new terminal will look like ----------------------------------
# A script cannot change the shell it was started from, so instead start a fresh
# interactive bash (which reads ~/.bashrc) with a clean environment and inspect it.
log "Checking a fresh interactive shell (bash -i reads ~/.bashrc, like a new terminal)"
# shellcheck disable=SC2016  # expanded inside the child shell
probe='printf "__CS4226_%s=%s\n" VENV "${VIRTUAL_ENV:-}" PY "$(command -v python3)" PS1 "$PS1" \
    P4C "$(command -v p4c)" BMV2 "$(command -v simple_switch_grpc)"'
report=$(env -i HOME="$HOME" USER="$USER" LOGNAME="$USER" TERM="${TERM:-dumb}" \
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin \
    bash -ic "$probe" 2>/dev/null </dev/null || true)
field() { sed -n "s/^__CS4226_$1=//p" <<<"$report" | tail -n 1; }

venv_dir="$P4_ROOT/p4dev-python-venv"
shell_ok=1
if [[ "$(field VENV)" == "$venv_dir" ]]; then
    ok "VIRTUAL_ENV=$venv_dir"
else
    warn "VIRTUAL_ENV is '$(field VENV)', expected $venv_dir"; shell_ok=0
fi
if [[ "$(field PY)" == "$venv_dir/"* ]]; then
    ok "python3 -> $(field PY)"
else
    warn "python3 resolves to '$(field PY)', expected something under $venv_dir"; shell_ok=0
fi
if [[ "$(field PS1)" == *"(p4dev-python-venv)"* ]]; then
    ok "Prompt shows (p4dev-python-venv)"
else
    warn "Prompt does not show (p4dev-python-venv)"; shell_ok=0
fi
# Informational only: where the build put the P4 tools.
[[ -n "$(field P4C)" ]] && ok "p4c -> $(field P4C)" || warn "p4c not found on PATH in a new shell"
[[ -n "$(field BMV2)" ]] && ok "simple_switch_grpc -> $(field BMV2)" || warn "simple_switch_grpc not found on PATH in a new shell"

if [[ $shell_ok -ne 1 ]]; then
    next_steps \
        "A new terminal does not activate the P4 environment. Things to check:" \
        "  * tail ~/.bashrc - '$bashrc_line' must be there and not after an early 'return'/'exit'." \
        "  * cat $P4_ROOT/p4setup.bash - it should activate $venv_dir." \
        "  * Run 'bash -i' and look for errors printed while ~/.bashrc loads." \
        "Re-run ./04-configure-p4.sh after fixing."
    exit 1
fi

next_steps \
    "New terminals now start in (p4dev-python-venv). The terminal you ran this from" \
    "is unchanged; open a new one (or run: source ~/.bashrc) when you want it there." \
    "Next, run: ./05-python-deps.sh" \
    "  (05 activates the environment itself, so it works from any terminal.)"
