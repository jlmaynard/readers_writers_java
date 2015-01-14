// U30503758
// Jason Maynard
// Operating Systems, Summer '14

package readerwriters;

import java.util.Random;

public class Reader extends Thread
{
    // Global class level variables shared by instance objects of class
	private boolean finish;
	private SharedResource sr;
	private Random r;

    // Constructor -------------------------------------------------------------
    public Reader(SharedResource sr) {
        finish = false;
        r = new Random();

        // this instance of Writer now shares the same global resource
        this.sr = sr;

    }//end Reader

    public void run() // overrides method in Thread
    {
        while (!finish) {
            read();
            try {
                Thread.sleep(r.nextInt(20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//end while
    }//end run()

	private void read()
    {
        try {
            Main.mutex.acquire();  // ENTER CRITICAL SECTION 1 /////////////////
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Increment reader count
        Main.reader_count++;  // *** Update global reader count ****************

        System.out.print("Reader count: " + Main.reader_count + "\n");

        // First reader gets the readWriteSem
        if (Main.reader_count == 1)
        {
            // The first reader takes a read_write mutex
            System.out.println("I am the first reader - getting readWriteSem." +
                    "*****************");

            try {
                System.out.print("\nReadWriteSem before acquire by reader: " +
                        Main.readWriteSem.availablePermits() + "\n");

                Main.readWriteSem.acquire();  // ENTER CRITICAL SECTION 2 //////

                System.out.print("ReadWriteSem after acquire by reader: " +
                        Main.readWriteSem.availablePermits() + "\n\n");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Release read_count mutex
        Main.mutex.release();  // LEAVING CRITICAL SECTION 1 ///////////////////


        // Do the reading. (Will take 20 milliSec - SharedResource)
		sr.read(); // *** reading **********************************************

        // Get the reader_count mutex
        try {
            Main.mutex.acquire();  // ENTER CRITICAL SECTION 3 /////////////////
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Decrement reader_count
        Main.reader_count--;  // *** Update global reader count ****************

        // The last reader gives up the readWriteSem semaphore
        if (Main.reader_count == 0)
        {
            System.out.println("\nI am the last reader, releasing readWriteSem." +
            "*****************");

            System.out.print("ReadWriteSem before release from reader: " +
                    Main.readWriteSem.availablePermits() + "\n");

            Main.readWriteSem.release();  // LEAVING CRITICAL SECTION 2 ////////

            System.out.print("ReadWriteSem after release from reader: " +
                    Main.readWriteSem.availablePermits() + "\n\n");
        }

        // Release the reader_count mutex
        Main.mutex.release();  // LEAVING CRITICAL SECTION 3 ///////////////////

	}//end read()
	
	public void finish(){
		finish = true;
	}

}//end Reader class
