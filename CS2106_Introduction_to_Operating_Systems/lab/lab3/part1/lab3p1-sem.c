#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <semaphore.h>
#include <sys/shm.h>
#include <sys/wait.h>

#define NUM_PROCESSES 5

typedef struct Gate{
    sem_t sem;
    int turn;
} Gate;

int wait_sem(Gate* gate) {
    sem_wait(&gate->sem);
    return gate->turn;
}

int main() {

    int i, j, pid, shmid;
    Gate *gate;

    shmid = shmget(IPC_PRIVATE, sizeof(Gate), IPC_CREAT | 0600);
    gate = (Gate*) shmat(shmid, NULL, 0);

    sem_init(&gate->sem, 1, 0);

    for(i=0; i<NUM_PROCESSES; i++)
    {
        if((pid = fork()) == 0) {
            break;
        }
    }

    if(pid == 0) {
        while(wait_sem(gate) != i);

        printf("I am child %d\n", i);

        for(j = i*10; j<i*10 + 10; j++){
            printf("%d ", j);
            fflush(stdout);
            usleep(10);
	    }

        printf("\n\n");
        gate->turn++;
        sem_post(&gate->sem);
    }
    else {
        gate->turn = 0;
        sem_post(&gate->sem);
        for(i=0; i<NUM_PROCESSES; i++) 
            wait(NULL);
        shmdt(gate);
        shmctl(shmid, IPC_RMID, 0);
    }
}
