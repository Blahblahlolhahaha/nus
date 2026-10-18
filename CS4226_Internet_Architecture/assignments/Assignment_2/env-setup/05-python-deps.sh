#!/usr/bin/env bash
# Step 5 - Install the Python packages.
#
#   * python3 -m pip install -r env-setup/requirements.txt
#   * python3 -m pip install --force-reinstall protobuf==5.28.2
#
# Both run inside p4dev-python-venv (activated via /opt/p4/p4setup.bash).
# pip will complain that p4runtime-shell requires protobuf<4.22.0; this can be
# ignored.
#
# env-setup/requirements.txt is a copy of the assignment's requirements.txt, so this
# step works even when only env-setup/ has been copied onto the VM. Keep the two in sync.

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root

requirements="$SCRIPT_DIR/requirements.txt"
[[ -f "$requirements" ]] || die "$requirements not found. Copy the whole env-setup/ folder onto the VM."

[[ -f "$P4_ROOT/p4setup.bash" ]] || die "$P4_ROOT/p4setup.bash not found. Run ./03-build-p4.sh and ./04-configure-p4.sh first."

log "Activating the P4 environment ($P4_ROOT/p4setup.bash)"
set +u  # venv activation scripts may reference unset variables
# shellcheck disable=SC1091
source "$P4_ROOT/p4setup.bash"
set -u

py_prefix=$(python3 -c 'import sys; print(sys.prefix)')
if [[ "$py_prefix" != *p4dev-python-venv* ]]; then
    die "python3 is not from p4dev-python-venv (sys.prefix=$py_prefix). Check $P4_ROOT/p4setup.bash."
fi
ok "Using $(python3 --version) from $py_prefix"

log "pip install -r $requirements"
if ! python3 -m pip install -r "$requirements"; then
    warn "pip returned an error. Errors about p4runtime-shell can be ignored;"
    warn "anything else should be checked. The import test at the end will tell."
fi

log "pip install --force-reinstall protobuf==$PROTOBUF_VERSION"
if ! python3 -m pip install --force-reinstall "protobuf==$PROTOBUF_VERSION"; then
    warn "pip returned an error. Errors about p4runtime-shell can be ignored."
fi

log "Smoke test: importing the assignment's Python dependencies"
python3 - "$PROTOBUF_VERSION" <<'EOF'
import sys
import finsy, scapy, mininet  # noqa: F401
import google.protobuf
expected = sys.argv[1]
print(f"finsy {getattr(finsy, '__version__', '?')}, protobuf {google.protobuf.__version__}")
if google.protobuf.__version__ != expected:
    sys.exit(f"protobuf is {google.protobuf.__version__}, expected {expected}")
EOF
ok "Python dependencies installed."

next_steps \
    "P4 setup complete. Suggested follow-ups:" \
    "  * Install FRR for Task 5: ./06-install-frr.sh" \
    "  * (Optional, after step 6) remove passwordless sudo again: ./99-revert-sudo-nopasswd.sh" \
    "  * Try the environment from the assignment repository, e.g.:" \
    "      make run-task-4     then type 'pingall' at the mininet> prompt"
