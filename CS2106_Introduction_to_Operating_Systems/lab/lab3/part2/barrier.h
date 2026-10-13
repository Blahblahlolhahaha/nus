#include <stdio.h>
#include <semaphore.h>
#include <unistd.h>
#include <sys/shm.h>

typedef struct Gate{
    sem_t barrier;
    sem_t mutex;
    int count;
}Gate;

void init_barrier(int);
void reach_barrier();
void destroy_barrier(int);
