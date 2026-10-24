#include <core.p4>
#include <v1model.p4>

struct metadata { 
    bit<16> srcPort;
    bit<16> dstPort;
}

typedef bit<48> macAddr_t;
typedef bit<32> ip4Addr_t;

header ethernet_t {
    macAddr_t dstAddr;
    macAddr_t srcAddr;
    bit<16>   etherType;
}

header ipv4_t {
    bit<4>    version;
    bit<4>    ihl;
    bit<8>    diffserv;
    bit<16>   totalLen;
    bit<16>   identification;
    bit<3>    flags;
    bit<13>   fragOffset;
    bit<8>    ttl;
    bit<8>    protocol;
    bit<16>   hdrChecksum;
    ip4Addr_t srcAddr;
    ip4Addr_t dstAddr;
}

header icmp_t {
    bit<8> type;
    bit<8> code;
    bit<16> checksum;
}

header tcp_t {
    bit<16> srcPort;
    bit<16> dstPort;
    bit<32> seqNo;
    bit<32> ackNo;
    bit<4>  dataOffset;
    bit<4>  reserved;
    bit<8>  flags;      // Or split into individual bit flags (urg, ack, psh, rst, syn, fin)
    bit<16> window;
    bit<16> checksum;
    bit<16> urgentPtr;
}

header udp_t {
    bit<16> srcPort;    // Source Port number
    bit<16> dstPort;    // Destination Port number
    bit<16> length;     // Total length of UDP header and payload (or length_)
    bit<16> checksum;   // UDP checksum
}

struct headers {
ethernet_t   ethernet;
    ipv4_t       ipv4;
    tcp_t       tcp;
    udp_t       udp;
}

struct digest_t {
    macAddr_t srcAddr;
    bit<9> ingressPort;
}

parser MyParser(
    packet_in packet,
    out headers hdr,
    inout metadata meta,
    inout standard_metadata_t standard_metadata
) {
    state start {
        meta.srcPort = 0;
        meta.dstPort = 0;
        transition parse_ethernet;
    }

    state parse_ethernet {
        packet.extract(hdr.ethernet);
        transition select(hdr.ethernet.etherType) {
            0x800: parse_ipv4;
            default: accept;
        }
    }

    state parse_ipv4 {
        packet.extract(hdr.ipv4);
        transition select(hdr.ipv4.protocol){
            6: parse_tcp;
            17: parse_udp;
            default: accept;
        }
    }

    state parse_tcp {
        packet.extract(hdr.tcp);
        meta.srcPort = hdr.tcp.srcPort;
        meta.dstPort = hdr.tcp.dstPort;
        transition accept;
    }
    
    state parse_udp {
        packet.extract(hdr.udp);
        meta.srcPort = hdr.udp.srcPort;
        meta.dstPort = hdr.udp.dstPort;
        transition accept;
    }

}

control MyVerifyChecksum(
    inout headers hdr,
    inout metadata meta
) {
    apply { }
}

control MyIngress(
    inout headers hdr,
    inout metadata meta,
    inout standard_metadata_t standard_metadata
) {
    action forward(bit<9> port) {
        standard_metadata.egress_spec = port;
    }

    action flood() {
        standard_metadata.mcast_grp = (bit<16>) standard_metadata.ingress_port;
    }

    action learn() {
        digest<digest_t>(0, {hdr.ethernet.srcAddr, standard_metadata.ingress_port});
    }

    action alt_route(macAddr_t mac, bit<9> port) {
        hdr.ethernet.dstAddr = mac;
        standard_metadata.egress_spec = port;
    }

    table mac_forward {
        key = {
            hdr.ethernet.dstAddr: exact;
        }
        actions = {
            forward;
            flood;
        }
        size = 1024;
        default_action = flood();
    }

    table route_alt{
        key = {
            hdr.ipv4.srcAddr: exact;
            hdr.ipv4.dstAddr: exact;
            hdr.ipv4.protocol: exact;
            meta.srcPort: exact;
            meta.dstPort: exact;
        }
        actions = {
            alt_route;
            NoAction;
        }
        size = 1024;
        default_action = NoAction();
    }

    apply { 
        if(hdr.ethernet.isValid()){
            learn();
            if(hdr.ipv4.isValid()) {
                if(!route_alt.apply().hit) {
                    mac_forward.apply();
                }
            } else {
                mac_forward.apply();
            }
            if(standard_metadata.egress_spec == standard_metadata.ingress_port) {
                mark_to_drop(standard_metadata);
            }
        }
    }
}

control MyEgress(
    inout headers hdr,
    inout metadata meta,
    inout standard_metadata_t standard_metadata
) {
    apply { }
}

control MyComputeChecksum(
    inout headers hdr,
    inout metadata meta
) {
    apply { }
}

control MyDeparser(
    packet_out packet,
    in headers hdr
) {
    apply { 
        packet.emit(hdr.ethernet);
        packet.emit(hdr.ipv4);
        packet.emit(hdr.tcp);
        packet.emit(hdr.udp);

    }
}

V1Switch(
    MyParser(),
    MyVerifyChecksum(),
    MyIngress(),
    MyEgress(),
    MyComputeChecksum(),
    MyDeparser()
) main;
