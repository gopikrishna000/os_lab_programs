#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>  
#include <pthread.h>

#define NUM_PHILOSOPHERS 5

int chopsticks[NUM_PHILOSOPHERS];

void *philosopher_behaviour(void* id)
{  
    int philosopher_number = *((int*)id);

    while(1)
    {
        printf("PID : (%ld)  Philosopher %d is THINKING\n", pthread_self(), philosopher_number+1);
        sleep(1);
       
        printf("PID : (%ld)  Philosopher %d is Hungry\n", pthread_self(), philosopher_number+1);

        while(1)
        {
            if(chopsticks[philosopher_number] == 1)
                continue;            
            if(chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] == 1)
                continue;

            chopsticks[philosopher_number] = 1;
            chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] = 1;

            printf("PID : (%ld)  Philosopher %d picks #%d and #%d chopsticks up\n", pthread_self(), philosopher_number+1, philosopher_number+1, 1 + ((philosopher_number+1)%NUM_PHILOSOPHERS));

            printf("PID : (%ld)  Philosopher %d is Eating Noodles\n", pthread_self(), philosopher_number+1);
            sleep(1);

            chopsticks[philosopher_number] = 0;
            chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] = 0;
           
            printf("PID : (%ld)  Philosopher %d puts #%d and #%d chopsticks down\n", pthread_self(), philosopher_number+1, philosopher_number+1, 1 + ((philosopher_number+1)%NUM_PHILOSOPHERS));
            break;
        }
    }
}

void *philosopher_behaviour_rev(void* id)
{
    int philosopher_number = *((int*)id);

    while(1)
    {
        printf("PID : (%ld)  Philosopher %d is THINKING\n", pthread_self(), philosopher_number+1);
        sleep(1);
       
        printf("PID : (%ld)  Philosopher %d is Hungry\n", pthread_self(), philosopher_number+1);

        while(1)
        {
            if(chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] == 1)
                continue;

            if(chopsticks[philosopher_number] == 1)
                continue;            
           
            chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] = 1;
            chopsticks[philosopher_number] = 1;

            printf("PID : (%ld)  Philosopher %d picks #%d and #%d chopsticks up\n", pthread_self(), philosopher_number+1, philosopher_number+1, 1 + ((philosopher_number+1)%NUM_PHILOSOPHERS));

            printf("PID : (%ld)  Philosopher %d is Eating Noodles\n", pthread_self(), philosopher_number+1);
            sleep(1);

            chopsticks[philosopher_number] = 0;
            chopsticks[(philosopher_number+1)%NUM_PHILOSOPHERS] = 0;
           
            printf("PID : (%ld)  Philosopher %d puts #%d and #%d chopsticks down\n", pthread_self(), philosopher_number+1, philosopher_number+1, 1 + ((philosopher_number+1)%NUM_PHILOSOPHERS));

            break;
        }
    }
}

int main(int argc, char const *argv[])
{
    pthread_t thread_ids[NUM_PHILOSOPHERS];
    int philosopher_numbers[NUM_PHILOSOPHERS];
   
    for (int i = 0; i < NUM_PHILOSOPHERS; i++)
    {
        philosopher_numbers[i] = i;
    }

    for (int i = 0; i < NUM_PHILOSOPHERS; i++)
    {
        chopsticks[i] = 0;
    }

    for (int i = 0; i < NUM_PHILOSOPHERS-1; i++)
    {
        pthread_create(&thread_ids[i], NULL, philosopher_behaviour, (void*)&philosopher_numbers[i]);
    }

    pthread_create(&thread_ids[NUM_PHILOSOPHERS-1], NULL, philosopher_behaviour_rev,(void*) &philosopher_numbers[NUM_PHILOSOPHERS-1]);

    for (int i = 0; i < NUM_PHILOSOPHERS; i++)
    {
        pthread_join(thread_ids[i], NULL);
    }

    exit(0);


}