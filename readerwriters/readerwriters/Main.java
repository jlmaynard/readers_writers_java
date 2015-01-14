// U30503758
// Jason Maynard
// Operating Systems, Summer '14

package readerwriters;

import java.util.concurrent.Semaphore;

public class Main
{
    // Global variables
    // Note: more readers than writers and 2 possible writers.
    private static final int NUM_WRITERS = 2;
	private static final int NUM_READERS = 2;

    // shared resources
    public static Semaphore mutex = new Semaphore(1);           // Locks reader_count
    public static Semaphore readWriteSem = new Semaphore(1);    // Read/Write access
    public static int reader_count = 0;                         // Counts num readers

	public static void main(String[] args)
    {
		// Instantiate a new shared resource object
        SharedResource sr = new SharedResource();

        // Create new writers with the same shared resource object
		for (int i = 0; i < NUM_WRITERS; i++)
        {
            // Writer extends Thread. start() is an inherited method to
            // start the new thread's execution.
            (new Writer(sr)).start();                           // places the thread into a runnable state
        }

        // Create new readers with the same shared resource
        for (int i = 0; i < NUM_READERS; i++)
        {
            // Reader extends Thread. start() is an inherited method to
            // start the new thread's execution.
            (new Reader(sr)).start();                           // places the thread into a runnable state
        }
	}
	
}//end main

