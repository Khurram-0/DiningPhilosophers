/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophers;

/**
 * A <code>Semaphore</code> object implements a semaphore (invented by Edsger
 * Dijkstra).
 * <p>
 * A semaphore controls access to resources. The number of resources currently
 * available is the Semaphore <em>value</em>.
 * </p>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Semaphore_(programming)">wikipedia
 * article</a>
 * @author Khurram Bhatti
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class Semaphore {

    private int value;

    /**
     * Create a semaphore.
     * @param value The initial value of the Semaphore ( must be &ge; 0).
     */
    public Semaphore(int value) {
        this.value = value; // Track number of semaphores
    }
    /**
     * Increment the number of available resources.  This method never blocks.
     * It may wakeup a Thread waiting for the Semaphore. 
     */
    public synchronized void up(){
        this.value++; // Increment # of semaphores
        if (this.value >= 0){ // 0 = thread is 
           notify(); // Signal to waiting thread (indicate that a resource is available)
        }
    }
    
    /**
     * Request a resource. If no resources are available (value), the calling Thread
     * block until a resource controlled by the Semaphore becomes available.
     */
    public synchronized void down(){
        this.value--; // Decrement
        while (this.value < 0){ // No resources available, wait
            try{
                wait(); // Block current thread until notify() from other thread
            } catch (InterruptedException e){
                Thread.currentThread().interrupt(); // Interrupt current thread
                // Find/create logger with same name as Semaphore.class &
                // Log exception
                Logger.getLogger(Semaphore.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}