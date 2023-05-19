/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophers;

/**
 *
 * @author Khurram Bhatti
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;  // Import the Scanner class

public class Main {
    
/*  Definitions */
// N=# of philosophers
public static int N = 4;

// For philosopher states
private static int THINKING=0, HUNGRY=1, EATING=2; 
private static int GOT_1st=10, GOT_2nd=20; // 1st or 2nd fork

// For Forks states
//"waiting" makes it easier to track fork philosopher is waiting for
private static int taken=1, NOTtaken=0, waiting=3;

// For philosophers
static int[] state = new int[N]; //  Initialized/filled in main
// Fork array shared by all philosohpers
static int[] fork = new int[N]; // Initialized/filled in main
// Track which fork each philosopher has, for easier printing
static int[][] phil_fork = new int[N][2]; // Index 0=left fork, 1=right

public static int eaten=0; // To track completed dinners

// Create/define semaphores
static Semaphore[] s = new Semaphore[N]; // One for each philosopher
static Semaphore[] fork_sem = new Semaphore[N]; // One for each fork

// left fork=i, right fork=(i+1)%N
// Left and Right neightbours of philosopher
public static int LEFT(int i){ // Philosopher on the left
    //int result = (i+N-1)%N // From center of tables perspective
    int result = (i+1)%N; // From philosophers perspective
    return result;
}
public static int RIGHT(int i){ // Philosopher on the right
    //int result = (i+1)%N;
    int result = (i+N-1)%N; // From philosophers perspective
    return result;
}

public static void print_take(int i, int fork_num, int got){
    //System.out.println("in take");
    state[i] = got; // Indicate how many forks philosopher has, e.g, 1 or 2
    fork[fork_num] =  taken; // Indicate that fork was taken
    System.out.println("Fork "+(fork_num+1)+" taken by Philosopher "+(i+1));
    // Last philosopher, don't take both forks until printing
    if(i == (N-1) && fork_num == (i+N-1)%N){ 
        print_tot_eaten(eaten); // Print total eaten dinners
    }
}

public static void wait(int i, int fork_num){
    s[LEFT(i)].up(); // Signal philosopher on left to continue
    fork_sem[fork_num].down(); // Wait until signaled fork available
}

public static void print_wait(){ // Print waiting statements
    think(); think(); // Wait for other philosophers before printing
    for (int phil=0; phil < N; phil++){// print waiting philosophers
        int left_fork=phil, right_fork=(phil+N-1)%N;
        //int l_fork_index=0, r_fork_index=1; // In fork[][] array
        if(state[phil]==HUNGRY || state[phil]==GOT_1st){
            if(phil_fork[phil][0] == waiting){ // Left fork = index 0
                System.out.println("Philosopher "+(phil+1)
                        +" is waiting for fork "+(left_fork+1));
            }
            if(phil_fork[phil][1] == waiting){// Right fork = index 1
                System.out.println("Philosopher "+(phil+1)
                        +" is waiting for fork "+(right_fork+1));
            }
        }
    }
    //System.out.println("before eaten");
    print_tot_eaten(eaten); // Print completed dinners
    s[0].up(); // Signal to thread on right

}

public static void test_1st(int i){ // Check if can get 1st fork
    if (state[i] == HUNGRY && state[LEFT(i)] != EATING && 
                                        state[RIGHT(i)] != EATING) {
        int left_fork=i, right_fork=(i+N-1)%N; // Define left and right fork
        int l_fork_index=0, r_fork_index=1; // In phil_fork[][] array

        if (i == (N-1)){ // Last philosopher, right fork 1st
            if(fork[right_fork] == NOTtaken){
                System.out.println("getting 1st fork, last phil");
                phil_fork[i][r_fork_index] = taken; // Indicate that fork taken
                print_take(i, right_fork, GOT_1st); // Print taken statements
            }else{
                phil_fork[i][r_fork_index] = waiting; // Indicate fork=waiting
                print_wait(); // Print waiting statement
                wait(i, right_fork); // wait() signals other thread and waits
                print_take(i, right_fork, GOT_1st); // Print taken statement
            }
        } else if (i < (N-1)){ // All other philosophers, left fork 1st
            if(fork[left_fork] == NOTtaken){ // If fork not taken
                phil_fork[i][l_fork_index] = taken; // Fork taken
                print_take(i, left_fork, GOT_1st); // Print taken statement
            }else{
                phil_fork[i][l_fork_index] = waiting; // Fork waiting
                wait(i, left_fork); // wait() signals other thread and waits
                print_take(i, left_fork, GOT_1st); // Print taken statement
            }
        }
    }
}

public static void test_2nd(int i){ // Check if can get 2nd fork
    if (state[i] == GOT_1st && state[LEFT(i)] != EATING && 
                                state[RIGHT(i)] != EATING) {
        int left_fork=i, right_fork=(i+N-1)%N; // Define left & right fork
        int l_fork_index=0, r_fork_index=1; // In fork[][] array

        if (i == (N-1)){ // Last philosopher, take left fork 2nd
            if(fork[left_fork] == NOTtaken){
                phil_fork[i][l_fork_index] = taken; // Indicate fork taken
                print_take(i, left_fork, GOT_2nd); // Print taken statement
            }else{
                phil_fork[i][l_fork_index] = waiting; // Indicate fork waiting
                print_wait(); // Print waiting statement(s)
                wait(i, left_fork); // wait() signals other thread and waits
                print_take(i, left_fork, GOT_2nd); // Print taken statements
            }
        } else if (i < (N-1)){ // All other philosophers, take right fork 2nd
            if(fork[right_fork] == NOTtaken){
                phil_fork[i][r_fork_index] = taken; // Indicate fork taken
                print_take(i, right_fork, GOT_2nd); // print taken statements
            }else{
                phil_fork[i][r_fork_index] = waiting;
                wait(i, right_fork); // wait() signals other thread and waits
                print_take(i, right_fork, GOT_2nd); // print taken statements
            }
        }
    }
}
public static void test(int i){
    if (state[i] == GOT_2nd && state[LEFT(i)] != EATING && 
                                    state[RIGHT(i)] != EATING) {
        state[i] = EATING; // Set philosopher state to eating
        s[i].up(); // Increment, may unblock thread
    }
}

public static void print_tot_eaten(int eaten){
    System.out.println("Till now num of philosophers completed dinner are "
            +eaten);
    print_completed(); // Print philosophers who completed  already
}
public static void print_completed(){
    for (int phil=0; phil < N; phil++){// Print all philosophers completed
        //int left_fork=i, right_fork=(i+N-1)%N;
        //int l_fork_index=0, r_fork_index=1; // In fork[][] 
        
        if(state[phil]==THINKING){ // Only if philosopher state reset
            System.out.println("Philosopher "+(phil+1)+" completed his dinner");
        }
    }
}

public static void get_forks(int i) {
    if (i==0){ // Signal first philosopher to start
        s[i].up();
    }
    
    // Get first fork
    s[i].down(); // Wait for signal
    state[i] = HUNGRY; // Got no forks so far
    test_1st(i); // Check if can get 1st fork
    s[LEFT(i)].up(); // Signal philosopher on left to continue

    //System.out.println("back");

    // Get second fork
    s[i].down(); // Wait for signal from philosopher
    state[i] = GOT_1st; // Got first fork, get second fork
    test_2nd(i); // Check if can get 2nd fork
    s[LEFT(i)].up(); // Signal philosopher on left to continue

    //System.out.print("back2");

    // Make sure philosopher can eat for sure
    s[i].down(); // Wait for signal from other philosopher
    //System.out.println("before wait");
    print_wait(); // Print waiting staement(s)
    test(i); // Check to make sure philosopher adjacent philosophers not eating
    s[LEFT(i)].up(); // Signal philosopher on left to continue

}

public static void print_rel(int i){ // Release forks
    state[i] = THINKING; // Philosopher=thinking once fork released
    int left_fork=i, right_fork=(i+N-1)%N; // Define left and right fork
    System.out.println("Philosopher "+(i+1)+" released fork "+(left_fork+1)
            +" and fork "+(right_fork+1));
    // Indicate in array which forks are being released
    fork[left_fork] = NOTtaken; fork[right_fork] = NOTtaken;
    // Release/Increment/Signal semaphore for both forks
    fork_sem[left_fork].up(); fork_sem[right_fork].up();
}

public static void put_forks(int i) { // Put forks back
    s[i].down(); // Enter critical region/wait for signal to release
    state[i]= THINKING; // Reset philosopher state
    print_rel(i); // Release forks
    s[LEFT(i)].up(); // Exit critical region, signal philosopher on left
}

public static void think(){
    try{ // Sleep to simulate thinking
        Thread.sleep((long)(Math.random()*100));// Random #, 0 to 100 (in msec)
    } catch (InterruptedException e){
        Thread.currentThread().interrupt(); // Interrupt current thread
        Logger.getLogger(Semaphore.class.getName()).log(Level.SEVERE, null, e);
    }
}

public static void eat(){
    try{ // Sleep to simulate eating
        Thread.sleep((long)(Math.random()*100));// Random #, 0 to 100 (in msec)
        eaten=eaten+1;
    } catch (InterruptedException e){
        Thread.currentThread().interrupt(); // Interrupt current thread
        Logger.getLogger(Semaphore.class.getName()).log(Level.SEVERE, null, e);
    }
}

static class philosopher extends Thread{
    final private int process;

    public philosopher(int process) {
        this.process = process; // Integer sent when thread was created
    }

    @Override
    public void run(){
        while(true) {
            think(); // Simulate thinking (random delay)
            get_forks(process); // Try getting forks for current philosopher
            eat(); // Simulate eating (random delay)
            System.out.println("Philosopher "+(process+1)
                    +" completed his dinner");
            //System.out.flush();
            put_forks(process); // Put forks back
            Thread.currentThread().interrupt(); // Interrupt current thread
            break;
        }
        if(process == (N-1)){ // Last completed dinner statement
            System.out.println("Till now num of philosophers completed dinner"
                    + " are "+eaten);
        }
    }
}

public static philosopher phil[] = new philosopher[N];

public static void main(String argv[]) {

        // Philosopher state array, one for each
        for (int i=0; i<N; i++){ // Initially all are thinking
            state[i]=THINKING;
        }
        // Fork state array, shared by all philosophers
        for (int i=0; i<N; i++){ // Initially all forks are NOTtaken
            fork[i]=NOTtaken;
        }
        // Fork array for each philosopher, for easier printing
        for (int phil=0; phil<N; phil++){ // Initially all are NOTtaken
            for (int i=0; i<2; i++){ // Initially all are NOTtaken (0)
                phil_fork[phil][i]=NOTtaken;
            }
        }
        /* // Make sure array initialized correctly
        for (int phil=0; phil<N; phil++){ // Initially all are NOTtaken
            for (int i=0; i<2; i++){ // Initially all are NOTtaken (0)
          System.out.println("Phil "+phil+" fork "+i+" is "+phil_fork[phil][i]);
            }
        }*/
        for (int i=0; i < N; i++){ // One semaphore per philosopher
            s[i] = new Semaphore(0);
        }
        for (int i=0; i < N; i++){ // Intialize to zero
            fork_sem[i] = new Semaphore(0);
        }
        for (int i = 0; i < N; i++) {
            phil[i] = new philosopher(i); 
            phil[i].start(); // Start philosopher thread
            //System.out.println("Created Philosopher "+i);
            //System.out.flush();
        }

        /*
        for (int i = 0; i < N; i++) {
            System.out.println("Philosopher "+i+" left is phil "+LEFT(i)
                    +" right is phil "+RIGHT(i));
        }
        System.out.println("\n");
        */
} // End of main(String argv[])

}