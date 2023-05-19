# DiningPhilosophers

**The Dining Philosophers problem is an example of a classic Operating System problem**

There are N philosphers (representing a thread, process, or task in an OS) sitting around a table eating and discussing philosophy 

A fork (representing a resource) is placed between each philosopher

The problem is that there are only N forks (same as number of philosophers) and each philosopher needs 2 forks to eat

The goal is to design an algorithm to ensures that no one starves (as long as each philosopher eventually stops eating), 
and also so that a maximum number of philosophers can eat at once. 

The algorithm must not reach a deadlock state: a state in which 2 or more philosophers are waiting for a resource held by the 
other - thus, they are stuck indefinitely waiting for the other to release the resource before releasing their own

Using multitasking techniques and through the use of semaphores + mutexes (which can be used to make philosophers wait for specific actions before continuing) 
we can create an algorithm to make the philosophers eat in any order we desire.

**For my implementation the philosophers perform actions in the following order (which is also printed during execution)**

Fork 1 taken by Philosopher 1
Fork 2 taken by Philosopher 2
Fork 3 taken by Philosopher 3
Philosopher 4 is waiting for fork 3
Till now num of philosophers completed dinner are 0
Fork 4 taken by Philosopher 1
Philosopher 2 is waiting for Fork 1
Philosopher 3 is waiting for Fork 2
Philosopher 4 is waiting for fork 3
Till now num of philosophers completed dinner are 0
Philosopher 1 completed his dinner
Philosopher 1 released fork 1 and fork 4
Fork 1 taken by Philosopher 2
Philosopher 3 is waiting for Fork 2
Philosopher 4 is waiting for fork 3
Till now num of philosophers completed dinner are 1
Philosopher 1 completed his dinner
Philosopher 2 completed his dinner
Philosopher 2 released fork 2 and fork 1
Fork 2 taken by Philosopher 3
Philosopher 4 is waiting for fork 3
Till now num of philosophers completed dinner are 2
Philosopher 1 completed his dinner
Philosopher 2 completed his dinner
Philosopher 3 completed his dinner
Philosopher 3 released fork 3 and fork 2
Fork 3 taken by philosopher 4
Till now num of philosophers completed dinner are 3
Philosopher 1 completed his dinner
Philosopher 2 completed his dinner
Philosopher 3 completed his dinner
Fork 4 taken by philosopher 4
Till now num of philosophers completed dinner are 3
Philosopher 1 completed his dinner
Philosopher 2 completed his dinner
Philosopher 3 completed his dinner
Philosopher 4 completed his dinner
Philosopher 4 released fork 4 and fork 3
Till now num of philosophers completed dinner are 4

