package readerwriters;

import java.util.concurrent.Semaphore;

public class SharedResource {

	private int reading;
	private boolean writing;

	private Semaphore rSem;             // Read semaphore
	private Semaphore wSem;             // Write semaphore

	// Constructor -------------------------------------------------------------
    public SharedResource(){

		reading = 0;
		writing = false;
		
		rSem = new Semaphore(1);        // Why initialize to 1?
		wSem = new Semaphore(1);        // To create binary semaphore?
	}

    // Do the actual reading of the resource -----------------------------------
    // i.e., shared memory
    public void read(){
		System.out.println("\nMartin is Reading shared resource");
		
		try {
			rSem.acquire();             // ENTER CRITICAL SECTION //////////////
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

        reading++;                      // Added a reader
		rSem.release();                 // LEAVING CRITICAL SECTION ////////////
		
		if (writing)
			throw new RuntimeException("Trying to read while writing");

		try {
			Thread.sleep(20);           // Simulates the reading time
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			rSem.acquire();             // ENTER CRITICAL SECTION //////////////
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		reading--;                      // Removed a reader
		rSem.release();                 // LEAVING CRITICAL SECTION ////////////
	}//end read()

    // Do the actual writing of the resource -----------------------------------
    // i.e., shared memory
	public void write(){
		System.out.println("Martin is Writing shared resource\n");
		if (reading > 0)
			throw new RuntimeException("Trying to write while reading");
		if (writing)
			throw new RuntimeException("Trying to write while writing");
		try {
			wSem.acquire();             // ENTER CRITICAL SECTION //////////////
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		writing = true;                 // do the writing
		wSem.release();                 // LEAVING CRITICAL SECTION ////////////

        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wSem.acquire();             // ENTER CRITICAL SECTION //////////////
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		writing = false;                // do the writing
		wSem.release();                 // LEAVING CRITICAL SECTION ////////////

	}//end write()

}//end SharedResource
