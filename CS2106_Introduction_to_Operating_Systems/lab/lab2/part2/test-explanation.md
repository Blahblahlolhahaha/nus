# Explanation of `test.c`

The program recreates this command using C system calls:

```bash
./slow 5 | ./talk > results.out
```

The data flows like this:

```text
slow’s stdout → pipe → talk’s stdin
                       talk’s stdout → results.out
```

## 1. Create the pipe

```c
int p[2];
pipe(p);
```

`p[0]` is the reading end; `p[1]` is the writing end. These are file descriptors—integers identifying open input/output resources.

## 2. Start `slow` in the first child

```c
pid_t slow_pid = fork();
```

`fork()` returns `0` in the child, the child’s PID in the parent, or `-1` on failure.

In this child:

```c
close(p[0]);
dup2(p[1], STDOUT_FILENO);
```

It closes the unused reading end and makes standard output point to the pipe’s writing end. After `dup2`, the original `p[1]` descriptor can be closed because standard output now refers to the same pipe. The code checks that the original descriptor is different from standard output before closing it.

```c
execl("./slow", "slow", "5", (char *) 0);
```

This replaces the child’s program with `slow`, passing `"5"` as its argument. Its printed numbers now enter the pipe. Successful `execl()` never returns.

## 3. Start `talk` in the second child

The parent calls `fork()` again. This child closes the unused writing end and redirects its input:

```c
dup2(p[0], STDIN_FILENO);
```

Now, when `talk` reads standard input, it receives bytes from `slow`.

It also opens the output file:

```c
open("./results.out", O_CREAT | O_WRONLY | O_TRUNC, 0644);
```

- `O_CREAT`: create the file if missing.
- `O_WRONLY`: open for writing.
- `O_TRUNC`: erase existing contents, matching shell `>`.
- `0644`: request owner read/write permission and group/others read permission for a newly created file, subject to the process’s `umask`.

Then:

```c
dup2(fp_out, STDOUT_FILENO);
execl("./talk", "talk", (char *) 0);
```

`talk` now reads from the pipe and writes its responses into `results.out`. As in the first child, the original descriptors are closed after redirection when they differ from the standard descriptors.

## 4. The parent closes its pipe descriptors and waits

```c
close(p[0]);
close(p[1]);
```

**Closing unused writing ends is essential:** after consuming any remaining data, `talk` sees end-of-file only when every descriptor referring to the pipe’s writing end has closed. Leaving the parent’s writing end open could make `talk` wait forever.

Both children are started **before** the parent waits, so they can run concurrently. This also prevents a producer from getting stuck on a full pipe while no consumer is running.

## 5. Wait reliably and return the result

`wait_for_child()` uses `waitpid()` to wait for a specific child and retries if a signal interrupts the wait (`errno == EINTR`). Waiting also reaps the child, releasing its retained process information.

The parent returns `talk`’s exit status, matching the shell pipeline’s default behavior. `slow` intentionally exits with `11` after counting from `5` through `10`; that is expected here.

If a child encounters a setup or execution error, `perror()` reports it and `_exit()` terminates that child. `_exit()` avoids flushing copies of the parent’s buffered introductory messages into the redirected output.

**The key distinction:** `fork()` creates a process, `dup2()` connects its input/output, and `execl()` changes which program it runs. Together, they build the pipeline.
