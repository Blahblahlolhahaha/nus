#!/usr/bin/env bash
# Step 6 - Install FRR (needed for Task 5 only).
#
#   * apt-get install frr
#   * stop + disable the system frr service (errors are expected and ignored).
#     Task 5's Mininet network starts its own zebra/bgpd daemons inside each
#     router's namespace, so the system-wide service must not be running.
#   * check that /usr/lib/frr/zebra and /usr/lib/frr/bgpd exist
#
# Does not need the P4 build; it can run at any point after ./01-configure-sudo.sh.

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

require_not_root
require_passwordless_sudo

# --- frr -----------------------------------------------------------------------
log "Installing frr"
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y frr

log "Stopping and disabling the frr service (errors here are OK)"
sudo systemctl stop frr || warn "stop failed (expected on some systems)"
sudo systemctl disable frr || warn "disable failed (expected on some systems)"

# --- Check the daemons used by common/mininet/nodes.py (FRRRouter) -------------
missing=0
for daemon in zebra bgpd; do
    if [[ -x "/usr/lib/frr/$daemon" ]]; then
        ok "/usr/lib/frr/$daemon found"
    else
        warn "/usr/lib/frr/$daemon not found"; missing=1
    fi
done
if [[ $missing -ne 0 ]]; then
    die "FRR daemons are missing. Check the output of 'sudo apt-get install -y frr' above."
fi

ok "FRR $(dpkg-query -W -f='${Version}' frr) installed"

next_steps \
    "FRR is installed. Smoke test Task 5 in the assignment repository:" \
    "  1. make run-task-5" \
    "  2. Wait for the Mininet CLI, then: as1h1 ping -c 4 as2h1" \
    "     (about 1000 ms with the template; about 10 ms once route alteration works)"
