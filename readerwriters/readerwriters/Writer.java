// U30503758
// Jason Maynard
// Operating Systems, Summer '14

package readerwriters;

import java.util.Random;

// A thread is a thread of execution in a program.
// http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html
public class Writer extends Thread
{
    // Global class level variables shared by instance objects of class
	private boolean finish;
	private Random r;
	private SharedResource sr;

    // Constructor -------------------------------------------------------------
    public Writer(SharedResource sr) {
        finish = false;
        r = new Random();

        // this instance of Writer now shares the same global resource
        this.sr = sr;
    }

    public void run() // overrides method in Thread
    {
        while (!finish) {
            write();

            try {
                Thread.sleep(r.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }//end while
    }//end run()

	private void write()
    {
        // First acquire readWriteSem
        try {
            Main.readWriteSem.acquire();  // ENTER CRITICAL SECTION ////////////
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Do the writing. (Will take 100 milliSec  - SharedResource)
        sr.write();  // *** write **********************************************

        // Critical section is over now release the semaphore
        Main.readWriteSem.release();  // LEAVING CRITICAL SECTION //////////////
	}
	
	public void finish()
    {
		finish = true;
	}

}//end Writer class
