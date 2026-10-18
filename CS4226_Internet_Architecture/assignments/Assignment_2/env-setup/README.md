# Setting up the P4 environment

If you are not using the provided VM image, these scripts install everything the
assignment needs on the SoC VM (or another x86-64 Ubuntu 22.04 machine).

Before you start, note that:

- The build takes **1 to 8 hours** and needs **more than 15 GB** of free disk space.
- Only x86-64 is supported.
- You need to know your password for `sudo`.

## How to run the scripts

1. Copy the assignment folder onto the VM, open a terminal, and go into this folder:
   ```
   cd cs4226-assignment2/env-setup
   ```
   (use the actual path of your assignment folder).
2. Run the scripts below in order, as your normal user. Do not put `sudo` in front of them;
   they ask for it when needed.
3. Each script finishes with a **NEXT** box that tells you what to do next.
4. If a script fails, fix the problem it reports and run the same script again.

## Steps

1. **Allow sudo without a password.** The build runs `sudo` many times and must not stop to
   ask for your password. Enter your password once when asked.
   ```
   ./01-configure-sudo.sh
   ```

2. **Update the system and set up Python 3.10.**
   ```
   ./02-update-system.sh
   ```
   Then check that `python3 --version` shows `Python 3.10.x`. If it doesn't, open a new
   terminal and check again.

3. **Build P4.** This is the step that takes hours. Start a `screen` session first, so the
   build keeps running even if you close the terminal or lose your connection:
   ```
   screen -S buildP4
   ./03-build-p4.sh
   ```
   You can leave the session with `Ctrl-a` then `d`, and come back later with
   `screen -r buildP4`.

   If the build fails, look at the end of the log with `tail -n 100 /opt/p4/log.txt`, fix the
   problem (for example, free up disk space), and run `./03-build-p4.sh` again.

4. **Configure P4.** You can run this in any terminal; you no longer need the `screen`
   session.
   ```
   ./04-configure-p4.sh
   ```
   From now on, new terminals start with `(p4dev-python-venv)` at the beginning of the prompt.

5. **Install the Python packages.**
   ```
   ./05-python-deps.sh
   ```
   pip may print an error saying that p4runtime-shell needs an older protobuf. You can ignore
   it.

6. **Install FRR**, which Task 5 needs for its BGP routers.
   ```
   ./06-install-frr.sh
   ```

7. **(Optional) Ask for a password for sudo again.** Step 1 turned off the password prompt
   for sudo. Run this when you have finished the setup to turn it back on:
   ```
   ./99-revert-sudo-nopasswd.sh
   ```

## Check that it works

Open a new terminal, go to the assignment folder, and run:

```
make run-task-4
```

If the network starts without errors and you reach the `mininet>` prompt, the environment is
working. Type `exit` to stop it. (Commands such as `pingall` only succeed once you have
completed the task.)
