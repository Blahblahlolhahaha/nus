#!/usr/bin/env bash
# Step 3 - Build and install P4.
#
#   * apt install git; create /opt/p4 owned by you
#   * git clone --recurse-submodules jafingerhut/p4-guide into /opt/p4
#   * export INSTALL_P4C_SOURCE_VERSION=v1.2.4.14
#   * cd /opt/p4 && ./p4-guide/bin/install-p4dev-v8.sh |& tee log.txt
#
# The build takes 1-8 hours and >15 GB of disk. Run this INSIDE screen or tmux
# (e.g. `screen -S buildP4`) so it survives a dropped SSH connection.
#
# Usage: ./03-build-p4.sh [--no-session-check]

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

session_check=1
for arg in "$@"; do
    case "$arg" in
        --no-session-check) session_check=0 ;;
        -h|--help) sed -n '2,14p' "$0"; exit 0 ;;
        *) die "Unknown argument: $arg" ;;
    esac
done

require_not_root
require_passwordless_sudo

# --- Pre-flight checks -------------------------------------------------------
if [[ $session_check -eq 1 && -z "${STY:-}" && -z "${TMUX:-}" ]]; then
    next_steps \
        "Not running inside screen or tmux. The build takes hours; start a session first:" \
        "  screen -S buildP4        (detach: Ctrl-a d,  reattach: screen -r buildP4)" \
        "  tmux new -s buildP4      (detach: Ctrl-b d,  reattach: tmux attach -t buildP4)" \
        "then re-run ./03-build-p4.sh inside it." \
        "(Use --no-session-check to skip this check.)"
    exit 1
fi

arch=$(uname -m)
if [[ "$arch" != "x86_64" ]]; then
    warn "Architecture is $arch. install-p4dev-v8.sh targets x86_64 and the course only"
    warn "validated x64. p4-guide has other scripts for arm64; choose one manually."
    confirm "Continue with install-p4dev-v8.sh anyway?" || exit 1
fi

if [[ -r /etc/os-release ]]; then
    # shellcheck disable=SC1091
    log "OS: $(. /etc/os-release && echo "${PRETTY_NAME:-unknown}")"
fi

avail_gb=$(df --output=avail -BG / | tail -n 1 | tr -dc '0-9')
if (( avail_gb < 15 )); then
    warn "Only ${avail_gb} GB free on /; the build needs more than 15 GB."
    confirm "Continue anyway?" || exit 1
else
    ok "${avail_gb} GB free on /"
fi

# --- Configuring The Environment To Compile P4 -------------------------------
log "Installing git"
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y git

log "Creating $P4_ROOT owned by $USER"
sudo mkdir -p "$P4_ROOT"
sudo chown "$USER:$USER" "$P4_ROOT"

cd "$P4_ROOT"
if [[ -d p4-guide/.git ]]; then
    log "p4-guide already cloned; updating submodules only"
    git -C p4-guide submodule update --init --recursive
else
    log "Cloning $P4_GUIDE_URL"
    git clone --recurse-submodules "$P4_GUIDE_URL"
fi

export INSTALL_P4C_SOURCE_VERSION="$P4C_VERSION"
log "INSTALL_P4C_SOURCE_VERSION=$INSTALL_P4C_SOURCE_VERSION"

# --- Compiling and Installing P4 ---------------------------------------------
if [[ -f log.txt ]]; then
    old_log="log.txt.$(date +%Y%m%d-%H%M%S)"
    log "Keeping previous build log as $P4_ROOT/$old_log"
    mv log.txt "$old_log"
fi

log "Starting ./p4-guide/bin/install-p4dev-v8.sh at $(date). Log: $P4_ROOT/log.txt"
log "This takes 1-8 hours. You can detach from screen/tmux and come back later."
start=$(date +%s)
# pipefail (from lib.sh) makes the pipeline fail if the installer fails, not just tee.
if ./p4-guide/bin/install-p4dev-v8.sh 2>&1 | tee log.txt; then
    build_status=0
else
    build_status=$?
fi
elapsed=$(( $(date +%s) - start ))
log "Installer finished after $(( elapsed / 3600 ))h $(( elapsed % 3600 / 60 ))m with exit code $build_status"

if [[ $build_status -ne 0 || ! -f "$P4_ROOT/p4setup.bash" ]]; then
    next_steps \
        "The P4 build did not complete successfully." \
        "  1. Inspect the end of the log:  tail -n 100 $P4_ROOT/log.txt" \
        "  2. Fix the cause (disk space, network, apt locks, ...)." \
        "  3. Re-run ./03-build-p4.sh (the clone step is skipped on re-runs)."
    exit 1
fi

ok "P4 environment built; $P4_ROOT now contains:"
ls -ahl "$P4_ROOT"

next_steps \
    "You no longer need the screen/tmux session; exit it if you like." \
    "Run (from any terminal): ./04-configure-p4.sh"
