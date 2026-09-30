#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <errno.h>

/* Retry waiting if a signal interrupts waitpid. */
static int wait_for_child(pid_t child, int *status) {
    while (waitpid(child, status, 0) < 0) {
        if (errno != EINTR) {
            perror("test: waitpid");
            return -1;
        }
    }
    return 0;
}

int main(void) {

    printf("Be patient, the program will take around 7 seconds to run.\n");
    printf("At the end you can do \"cat results.out\" to see the result.\n");

    int p[2];
    if (pipe(p) < 0) {
        perror("test: pipe");
        return EXIT_FAILURE;
    }

    // Child 1: runs ./slow 5, with its stdout going into the pipe
    pid_t slow_pid = fork();
    if (slow_pid < 0) {
        perror("test: fork slow");
        close(p[0]);
        close(p[1]);
        return EXIT_FAILURE;
    }
    if (slow_pid == 0) {
        close(p[0]);                    // not reading from the pipe
        if (dup2(p[1], STDOUT_FILENO) < 0) {
            perror("test: dup2 slow stdout");
            _exit(EXIT_FAILURE);
        }
        if (p[1] != STDOUT_FILENO)
            close(p[1]);                // stdout now owns the pipe's write end
        execl("./slow", "slow", "5", (char *) 0);
        perror("test: execl slow");     // only reached if exec fails
        _exit(127);
    }

    // Child 2: runs ./talk, reading from the pipe and writing to results.out
    pid_t talk_pid = fork();
    if (talk_pid < 0) {
        perror("test: fork talk");
        close(p[0]);
        close(p[1]);
        wait_for_child(slow_pid, NULL);
        return EXIT_FAILURE;
    }
    if (talk_pid == 0) {
        close(p[1]);                    // not writing to the pipe
        if (dup2(p[0], STDIN_FILENO) < 0) {
            perror("test: dup2 talk stdin");
            _exit(EXIT_FAILURE);
        }
        if (p[0] != STDIN_FILENO)
            close(p[0]);

        int fp_out = open("./results.out", O_CREAT | O_WRONLY | O_TRUNC, 0644);
        if (fp_out < 0) {
            perror("test: open results.out");
            _exit(EXIT_FAILURE);
        }
        if (dup2(fp_out, STDOUT_FILENO) < 0) {
            perror("test: dup2 talk stdout");
            _exit(EXIT_FAILURE);
        }
        if (fp_out != STDOUT_FILENO)
            close(fp_out);
        execl("./talk", "talk", (char *) 0);
        perror("test: execl talk");
        _exit(127);
    }

    // Parent: close both pipe ends, then wait for both children
    close(p[0]);
    close(p[1]);
    int talk_status;
    int slow_wait = wait_for_child(slow_pid, NULL);
    int talk_wait = wait_for_child(talk_pid, &talk_status);
    if (slow_wait < 0 || talk_wait < 0)
        return EXIT_FAILURE;

    // Like the shell pipeline, return the final command's exit status.
    // slow deliberately returns 11 after counting from 5 through 10.
    if (WIFEXITED(talk_status))
        return WEXITSTATUS(talk_status);
    return 128 + WTERMSIG(talk_status);
}
