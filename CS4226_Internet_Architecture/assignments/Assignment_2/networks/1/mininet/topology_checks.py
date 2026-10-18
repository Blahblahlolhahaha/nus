from time import sleep

from mininet.link import TCLink
from mininet.log import output, setLogLevel
from mininet.net import Mininet

from networks import Topology

PRE_TEST_DELAY = 5
POST_TEST_DELAY = 5

if __name__ == "__main__":
    setLogLevel("info")

    topology = Topology()

    network = Mininet(
        topo=topology,
        link=TCLink,
        autoSetMacs=False,
    )
    network.start()

    output("*** Generating traffic\n")

    sleep(PRE_TEST_DELAY)

    h1 = network.getNodeByName("h1")
    h1.cmd("/opt/p4/p4dev-python-venv/bin/python3 tests/1/checks/traffic_generator.py")

    sleep(POST_TEST_DELAY)

    network.stop()
