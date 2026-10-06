#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <fcntl.h>

int main() {

    printf("Be patient, the program will take around 7 seconds to run.\n");
    printf("At the end you can do \"cat results.out\" to see the result.\n");
    int p[2];
    if(pipe(p) < 0) {
        perror("sad: ");
    }

    // Add code here to pipe from ./slow 5 to ./talk and redirect
    // output of ./talk to results.out
    int pid_slow = fork();
    if(pid_slow != 0) {
        int pid_talk = fork();
        if(pid_talk != 0) {
            close(p[0]);
            close(p[1]);
            int wait_count = 0;
            while(wait_count !=2) {
                wait(NULL);
                wait_count++;
            }
        } else {
            int file = open("results.out", O_WRONLY | O_CREAT | O_TRUNC, 0644);
            close(p[1]);
            dup2(p[0], STDIN_FILENO);
            dup2(file, STDOUT_FILENO);
            execl("talk", "talk", NULL);
        }

    } else {
        char *args[] = {"slow","5"};
        close(p[0]);
        dup2(p[1], STDOUT_FILENO);
        execl("slow", "slow", "5", NULL);
        printf("gae\n");
        
    }
    // I.e. your program should do the equivalent of ./slow 5 | talk > results.out
    // WITHOUT using | and > from the shell.
    // Look at how we did < and > and | in the previous parts of this lab, and do the same!

}

