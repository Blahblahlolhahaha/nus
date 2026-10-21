#include <core.p4>
#include <v1model.p4>

struct metadata { }

struct headers { }

parser MyParser(
    packet_in packet,
    out headers hdr,
    inout metadata meta,
    inout standard_metadata_t standard_metadata
) {
    state start {
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
    action set_egress_spec(bit<9> port) {
        standard_metadata.egress_spec = port;
    }
    table forward {
        key = { standard_metadata.ingress_port: exact; }
        actions = {
            set_egress_spec;
            NoAction;
        }
        size = 1024;
        default_action = NoAction();
        const entries = {
            1: set_egress_spec(2);
            2: set_egress_spec(1);
        }
    }
    apply { forward.apply(); }
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
    apply { }
}

V1Switch(
    MyParser(),
    MyVerifyChecksum(),
    MyIngress(),
    MyEgress(),
    MyComputeChecksum(),
    MyDeparser()
) main;
