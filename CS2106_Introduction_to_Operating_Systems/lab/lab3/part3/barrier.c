#include <stdio.h>
#include <semaphore.h>
#include <sys/ipc.h>
#include <unistd.h>
#include <sys/shm.h>
#include "barrier.h"

int nproc = 0;
int shmid = 0;
Gate* gate;

void init_barrier(int numproc) {
    shmid = shmget(IPC_PRIVATE, sizeof(Gate), IPC_CREAT | 0600);
    gate = (Gate*) shmat(shmid, NULL, 0);
    sem_init(&gate->mutex, 1, 0);
    sem_post(&gate->mutex);
    sem_init(&gate->barrier, 1, 0);
    nproc = numproc;
    gate->count = 0;
}

void reach_barrier() {
    sem_wait(&gate->mutex);
    gate->count++;
    if(gate->count == nproc) {
        for(int i = 0; i < nproc; i++) {
            sem_post(&gate->barrier);
        }
    }
    sem_post(&gate->mutex);
    sem_wait(&gate->barrier);
}

void destroy_barrier(int my_pid) {
    if(my_pid != 0) {
        sem_destroy(&gate->barrier);
        sem_destroy(&gate->mutex);
        shmdt(gate);
        shmctl(shmid, IPC_RMID,0);
        // Destroy the semaphores and detach
        // and free any shared memory. Notice
        // that we explicity check that it is
        // the parent doing it.
    }
}
