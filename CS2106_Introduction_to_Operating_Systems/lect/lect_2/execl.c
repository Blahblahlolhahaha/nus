#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {

    int result; 

    result = fork();

    if(result == 0) {
        execl("/bin/ls", "-al", NULL);
    } else {
        waitpid(-1, NULL, WNOHANG);
        printf("yay\n");
    }
    printf("hello\n");
}
