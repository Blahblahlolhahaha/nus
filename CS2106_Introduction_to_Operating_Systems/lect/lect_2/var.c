#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

int main() {
    int var = 1234;
    int result = 0;
    
    result = fork();

    if(result == 0) {
        printf("Parent var: %d\n", var);
        var++;
        printf("Parent var: %d\n", var);
    } else {
        printf("Child var: %d\n", var);
        var = 9;
        printf("Child var: %d\n", var);
    }
}
