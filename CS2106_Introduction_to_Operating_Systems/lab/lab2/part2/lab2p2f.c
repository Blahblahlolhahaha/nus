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
    int pid = fork();
    if(pid != 0) {
        char* buffer = (char*)malloc(128);
    } else {
        char *args[] = {"5"};
        close(p[0]);
        dup2(p[1], STDOUT_FILENO);
        execvp("slow", args);
        
    }
    // I.e. your program should do the equivalent of ./slow 5 | talk > results.out
    // WITHOUT using | and > from the shell.
    // Look at how we did < and > and | in the previous parts of this lab, and do the same!

}

