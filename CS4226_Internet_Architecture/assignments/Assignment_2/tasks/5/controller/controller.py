import logging
import os
import pathlib
import sys
import json

import finsy

SCRIPT_DIRECTORY = os.path.abspath(
    os.path.dirname(__file__)
)
REPOSITORY_DIRECTORY = os.path.join(
    SCRIPT_DIRECTORY,
    "../../../"
)

sys.path.append(REPOSITORY_DIRECTORY)

BUILD_DIRECTORY = os.path.join(
    REPOSITORY_DIRECTORY,
    "build/p4"
)

ROUTE_ALTERATIONS_DIR = os.path.join(
    REPOSITORY_DIRECTORY,
    "networks/5/ixp_switch"
)

logger = finsy.LoggerAdapter(
    logging.getLogger("finsy")
)

async def controller_ready_handler(
    switch: finsy.Switch
):
    # Clear entities in P4 switch
    await switch.delete_all()
    learned = {}
    alt_route_path = pathlib.Path(
        os.path.join(
            ROUTE_ALTERATIONS_DIR,
            f"{switch.name}-route-alterations.json"
        )
    )
    if alt_route_path.exists():
        with alt_route_path.open("r") as f:
            routes = json.load(f)  
            for alt in routes:
                entry = finsy.P4TableEntry(
                    "MyIngress.route_alt",
                    match = finsy.P4TableMatch(
                        srcAddr = alt["src_addr"],
                        dstAddr = alt["dst_addr"],
                        protocol = alt["protocol"],
                        dstPort = alt.get("dst_port",0),
                        srcPort = alt.get("src_port",0)
                    ),
                    action = finsy.P4TableAction(
                        "MyIngress.alt_route",
                        mac = alt["egress_mac"],
                        port = alt["egress_port"]
                    )
                )
                await switch.write([+entry])
    
    async def learn_mac():
        async for batch in switch.read_digests("digest_t"):
            for record in batch:
                mac = record["srcAddr"]
                port = record["ingressPort"]
                if mac == 0 or (mac & 0x010000000000):
                    continue
                old = learned.get(mac)
                if old == port:
                    continue
                
                entry = finsy.P4TableEntry(
                    "MyIngress.mac_forward",
                    match = finsy.P4TableMatch(
                        dstAddr = mac
                    ),
                    action = finsy.P4TableAction(
                        "MyIngress.forward",
                        port=port
                    )
                )

                if old is None:
                    await switch.write([+entry])
                else:
                    await switch.write([~entry])
                learned[mac] = port
                logger.info(
                    "LEARN switch=%s mac=%012x port=%s",
                    switch.name,
                    mac,
                    port,
                )
            await switch.write([batch.ack()])

    ports = switch.stash["interfaces"]
    for ingress_port in ports:
        await switch.write([
            +finsy.P4MulticastGroupEntry(
                multicast_group_id = ingress_port,
                replicas = [
                    port for port in ports if port != ingress_port
                ]
            )
        ])
    switch.create_task(learn_mac())
    await switch.write([
        +finsy.P4DigestEntry(
            "digest_t",
            max_list_size = 1,
            ack_timeout_ns=1_000_000_000
        )
    ])


async def main():

    info_file_path = pathlib.Path(
        os.path.join(
            BUILD_DIRECTORY,
            "main.p4info.txtpb"
        )
    )
    program_file_path = pathlib.Path(
        os.path.join(
            BUILD_DIRECTORY,
            "main.json"
        )
    )

    ixp1s1 = finsy.Switch(
        "ixp1s1",
        "127.0.0.1:50001",
        finsy.SwitchOptions(
            p4info=info_file_path,
            p4blob=program_file_path,
            device_id=1,
            ready_handler=controller_ready_handler,
        ),
        stash={
            "interfaces": [1, 2, 3]
        },
    )
    ixp2s1 = finsy.Switch(
        "ixp2s1",
        "127.0.0.1:50002",
        finsy.SwitchOptions(
            p4info=info_file_path,
            p4blob=program_file_path,
            device_id=2,
            ready_handler=controller_ready_handler,
        ),
        stash={
            "interfaces": [1, 2, 3]
        },
    )

    controller = finsy.Controller(
        [ixp1s1, ixp2s1]
    )

    await controller.run()

if __name__ == "__main__":
    finsy.run(main())
