#!/usr/bin/python

import os, sys
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.cli import CLI
from mininet.log import setLogLevel, info, debug
from mininet.node import Host, RemoteController

def host_to_mac(host_number):
    return "ca:ff:ee:ed:00:" + f'{host_number:0{2}x}'

class IPv4Host(Host):
    def config(self, **params):
        super(IPv4Host, self).config(**params)
        # Disable IPv6
        self.cmd('sysctl -w net.ipv6.conf.all.disable_ipv6=1')
        self.cmd('sysctl -w net.ipv6.conf.default.disable_ipv6=1')
        self.cmd('sysctl -w net.ipv6.conf.lo.disable_ipv6=1')
        
class PATopo( Topo ):
    "PA topology"

    def build( self ):
        # TODO: Parse the input file topology.in
        with open("topology.in", "r") as f:
            lines = f.read().split("\n")
            nums = lines[0].split(" ")
            links = lines[1:]
            num_host = int(nums[0])
            num_switch = int(nums[1])
            num_link = int(nums[2])

        hosts = []
        switches = []
        # TODO: Add hosts
        # Use the self.addHost(HOST_NAME, mac=MAC_ADDRESS) API
        # For auto-grading purposes
        # The i-th (1-indexed) host will have its host name set to h<i>
        # e.g. the first host should have host name "h1"
        # The MAC_ADDRESS should be retrieved by calling a function host_to_mac(host_name)
        # > host_name = 'h%d' % [HOST_NUMBER]
        # > self.addHost(host_name, mac=host_to_mac([HOST_NUMBER]))
        for i in range(1,num_host+1):
            host_name = f"h{i}"
            hosts.append(self.addHost(host_name, mac=host_to_mac(i)))
        # TODO: Add switches
        # Use the self.addHost(SWITCH_NAME, config) API
        # For auto-grading purposes
        # The i-th (1-indexed) switch will have its host name set to s<i>
        # e.g. the first switch should have host name "s1"
        # > sconfig = {'dpid': "%016x" % [SWITCH NUMBER]}
        # > self.addSwitch('s%d' % [SWITCH NUMBER], **sconfig)
        for i in range(1, num_switch+1):
            switch_name = f"s{i}"
            sconfig = {'dpid': "%016x" % i}
            switches.append(self.addSwitch(switch_name, **sconfig))
        # TODO: Add links
        # > self.addLink([DEVICE1], [DEVICE2]) 
        for i in range(num_link):
            x = links[i]
            devices = x.split(",")
            device_str_1 = devices[0]
            device_str_2 = devices[1]
            is_host1 = device_str_1[0] == "h"
            is_host2 = device_str_2[0] == "h"
            num1 = int(device_str_1[1:]) - 1
            num2 = int(device_str_2[1:]) - 1
            device1 = hosts[num1] if is_host1 else switches[num1]
            device2 = hosts[num2] if is_host2 else switches[num2]
            self.addLink(device1, device2)
        
                    
def run():
    c = RemoteController('c', '127.0.0.1', 6633)
    net = Mininet(topo=PATopo(), host=IPv4Host, controller=None)
    net.addController(c)
    net.start()

    CLI(net)
    net.stop()

# if the script is run directly (sudo custom/optical.py):
if __name__ == '__main__':
    setLogLevel('info')
    run()
