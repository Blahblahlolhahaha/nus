# shellcheck shell=bash
# Shared helpers for the CS4226 P4 environment setup scripts.
# Sourced by the numbered scripts; not meant to be executed directly.

set -euo pipefail

# Paths and versions used by the setup scripts.
P4_ROOT=/opt/p4
P4_GUIDE_URL=https://github.com/jafingerhut/p4-guide.git
P4C_VERSION=v1.2.4.14
PYENV_PYTHON_VERSION=3.10.12
PROTOBUF_VERSION=5.28.2
P4_SECURE_PATH_PREFIX="/opt/p4/p4-guide/bin:/opt/p4/behavioral-model/tools:/opt/p4/p4dev-python-venv/bin"

SUDOERS_SECUREPATH_FILE=/etc/sudoers.d/zz-cs4226-p4-securepath
# Pristine copy of /etc/sudoers taken by 01-configure-sudo.sh, restored by 99-revert-sudo-nopasswd.sh.
SUDOERS_BACKUP=/var/backups/cs4226-p4-env/sudoers.orig

if [[ -t 1 ]]; then
    _c_blue=$'\033[1;34m'; _c_yellow=$'\033[1;33m'; _c_red=$'\033[1;31m'
    _c_green=$'\033[1;32m'; _c_reset=$'\033[0m'
else
    _c_blue=''; _c_yellow=''; _c_red=''; _c_green=''; _c_reset=''
fi

log()  { printf '%s==>%s %s\n' "$_c_blue" "$_c_reset" "$*"; }
ok()   { printf '%s[ok]%s %s\n' "$_c_green" "$_c_reset" "$*"; }
warn() { printf '%s[warn]%s %s\n' "$_c_yellow" "$_c_reset" "$*" >&2; }
die()  { printf '%s[error]%s %s\n' "$_c_red" "$_c_reset" "$*" >&2; exit 1; }

# Prints a boxed "what to do next" message for the human operator.
next_steps() {
    printf '\n%s==================== NEXT (manual) ====================%s\n' "$_c_yellow" "$_c_reset"
    printf '%s\n' "$@"
    printf '%s=======================================================%s\n' "$_c_yellow" "$_c_reset"
}

# Asks a yes/no question; returns 0 for yes. Defaults to no.
confirm() {
    local reply
    read -r -p "$1 [y/N] " reply
    [[ "$reply" =~ ^[Yy]$ ]]
}

# The scripts call sudo themselves and rely on $USER being the real user.
require_not_root() {
    if [[ "$(id -u)" -eq 0 ]]; then
        die "Run this script as your normal user (not root / not via sudo). It calls sudo itself."
    fi
}

require_passwordless_sudo() {
    if ! sudo -n true 2>/dev/null; then
        die "Passwordless sudo is not working. Run ./01-configure-sudo.sh first (see README.md)."
    fi
}

# Validates a sudoers fragment and installs it with the correct owner/mode.
install_sudoers_fragment() {
    local src=$1 dest=$2
    sudo visudo -cf "$src" >/dev/null || die "Generated sudoers fragment failed validation: $src"
    sudo install -o root -g root -m 0440 "$src" "$dest"
    # Re-validate the whole configuration; roll back if something is off.
    if ! sudo visudo -c >/dev/null; then
        sudo rm -f "$dest"
        die "sudoers configuration invalid after installing $dest; it has been removed again."
    fi
}
