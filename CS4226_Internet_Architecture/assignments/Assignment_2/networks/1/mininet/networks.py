import os
import sys

from mininet.topo import Topo

SCRIPT_DIRECTORY = os.path.abspath(
    os.path.dirname(__file__)
)
REPOSITORY_DIRECTORY = os.path.join(
    SCRIPT_DIRECTORY,
    "../../../"
)

sys.path.append(REPOSITORY_DIRECTORY)

# pylint: disable=E0401,C0413
from common.p4.functions import HelperFunctions
from common.mininet.nodes import Client, P4Switch

class Topology(Topo):
    def build(
        self,
        *args,
        **params
    ):
        common_host_commands = [
            "sysctl -w net.ipv6.conf.all.disable_ipv6=1",
            "sysctl -w net.ipv6.conf.lo.disable_ipv6=1",
            "sysctl -w net.ipv6.conf.default.disable_ipv6=1",
            "sysctl -w net.ipv4.ip_forward=0",
        ]

        hosts_mac_addresses = {}
        hosts_mac_addresses["h1"] = {"h1-eth0": "f0:00:0d:00:01:00"}
        hosts_mac_addresses["h2"] = {"h2-eth0": "f0:00:0d:00:02:00"}

        s1 = self.addSwitch(
            "s1",
            cls=P4Switch,
            identifier=1,
            thrift_port=9091,
            grpc_address="0.0.0.0",
            grpc_port=50001,
        )

        h1 = self.addHost(
            "h1",
            cls=Client,
            configCmds=(
                common_host_commands +
                HelperFunctions.generate_set_interface_mac_commands(hosts_mac_addresses["h1"])
            ),
        )
        h2 = self.addHost(
            "h2",
            cls=Client,
            configCmds=(
                common_host_commands +
                HelperFunctions.generate_set_interface_mac_commands(hosts_mac_addresses["h2"])
            ),
        )

        self.addLink(
            h1,
            s1,
            intfName1="h1-eth0",
            params1={"ip": "10.20.30.101/24"},
            intfName2="s1-eth0"
        )
        self.addLink(
            h2,
            s1,
            intfName1="h2-eth0",
            params1={"ip": "10.20.30.102/24"},
            intfName2="s1-eth1"
        )

# pylint: disable=W0108
topos = {
    "topology": (lambda: Topology())
}
