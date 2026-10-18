#!/usr/bin/env bash
# Step 2 - Update the system and set up Python 3.10.
#
#   * apt update / apt upgrade -y (non-interactive)
#   * pyenv install 3.10.12 && pyenv global 3.10.12   (only if pyenv is installed,
#     as on the SoC VM; otherwise skipped with a warning)
#
# apt runs before pyenv so that pyenv's Python build dependencies are installed
# before compiling 3.10.12.

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root
require_passwordless_sudo

apt_get() {
    sudo DEBIAN_FRONTEND=noninteractive apt-get \
        -o Dpkg::Options::=--force-confdef -o Dpkg::Options::=--force-confold "$@"
}

log "Updating APT package lists"
apt_get update

log "Upgrading system packages (this can take a while)"
apt_get upgrade -y

# screen/tmux are needed for step 3, git for the clone.
log "Installing screen, tmux and git"
apt_get install -y screen tmux git

# pyenv is usually only initialised from ~/.bashrc, which a script does not read.
export PYENV_ROOT="${PYENV_ROOT:-$HOME/.pyenv}"
if ! command -v pyenv >/dev/null 2>&1 && [[ -x "$PYENV_ROOT/bin/pyenv" ]]; then
    export PATH="$PYENV_ROOT/bin:$PATH"
fi

if command -v pyenv >/dev/null 2>&1; then
    log "Installing build dependencies for pyenv's Python build"
    apt_get install -y build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev \
        libsqlite3-dev curl libncurses-dev xz-utils tk-dev libxml2-dev libxmlsec1-dev \
        libffi-dev liblzma-dev

    log "pyenv install $PYENV_PYTHON_VERSION (skipped if already installed)"
    pyenv install -s "$PYENV_PYTHON_VERSION"
    log "pyenv global $PYENV_PYTHON_VERSION"
    pyenv global "$PYENV_PYTHON_VERSION"
    export PATH="$(pyenv root)/shims:$PATH"
else
    warn "pyenv not found; skipping the 'pyenv install $PYENV_PYTHON_VERSION' step."
    warn "That is fine if the system python3 is already 3.10 (e.g. stock Ubuntu 22.04)."
fi

py_version=$(python3 --version 2>&1 || true)
if [[ "$py_version" == "Python 3.10."* ]]; then
    ok "python3 is $py_version"
else
    warn "python3 reports '$py_version'; expected $PYENV_PYTHON_VERSION."
fi

next_steps \
    "1. Check in your terminal: python3 --version   (expect 3.10.x;" \
    "   if not, open a new shell so ~/.bashrc sets up pyenv)" \
    "2. Start a long-lived terminal session, e.g.:  screen -S buildP4   (or: tmux new -s buildP4)" \
    "3. Inside it run: ./03-build-p4.sh"
