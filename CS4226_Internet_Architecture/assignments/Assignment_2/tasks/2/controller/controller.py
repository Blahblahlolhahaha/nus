import logging
import os
import pathlib
import sys

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

logger = finsy.LoggerAdapter(
    logging.getLogger("finsy")
)

async def controller_ready_handler(
    switch: finsy.Switch
):
    # Clear entities in P4 switch
    await switch.delete_all()

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

    s1 = finsy.Switch(
        "s1",
        "127.0.0.1:50001",
        finsy.SwitchOptions(
            p4info=info_file_path,
            p4blob=program_file_path,
            device_id=1,
            ready_handler=controller_ready_handler,
        ),
        stash={
            "interfaces": [1]
        },
    )

    controller = finsy.Controller(
        [s1]
    )

    await controller.run()

if __name__ == "__main__":
    finsy.run(main())
