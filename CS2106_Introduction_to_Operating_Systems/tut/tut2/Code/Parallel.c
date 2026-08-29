/*
Demonstration of using multiple processes for parallel problem solving.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main()
{
    int numDigits, childResult;
    //Since largest number is 10 digits, a 12 characters string is more
    //than enough
    char cStringExample[12];

    scanf("%d", &numDigits);

    if(numDigits > 9) {
        printf("Too many digits, max 9!\n");
        exit(-1);
    }

    int* userInput = calloc(numDigits, sizeof(int));
    int* childPids = calloc(numDigits, sizeof(pid_t)); 

    for(int i = 0; i < numDigits; i++) {
        scanf("%d", &userInput[i]);
    }

    for (int i = 0; i < numDigits; i++) {
        childPids[i] = fork();

        if (childPids[i] != 0 ){

        } else {
            //Easy way to convert a number into a string
            sprintf(cStringExample, "%d", userInput[i]);

            execl("./PF", "PF", cStringExample, NULL);
        }

    } 
    int numResults = 0;
    while(numResults < numDigits) {
        pid_t pid = wait( &childResult);
        for(int i = 0; i < numDigits; i++) {
            if(pid == childPids[i]) {
                printf("%d has %d prime factors\n", userInput[i], WEXITSTATUS(childResult));
            }
        }
        numResults++;
    }
}
