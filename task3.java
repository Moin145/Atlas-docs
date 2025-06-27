class RunnableDemo implements Runnable {
    private Thread t;
    private String task3;
    RunnableDemo( String name){
        task3 = name;
        System.out.println("Creating " + task3 );
    }
    public void run() {
        System.out.println("Running " + task3 );
        try {
            for(int i = 4; i > 0; i--) {
                System.out.println("Thread: " + task3 + ", " + i);
// Let the thread sleep for a while.
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + task3 + " interrupted.");
        }
        System.out.println("Thread " + task3 + " exiting.");

    }
    public void start ()
    {
        System.out.println("Starting " + task3 );
        if (t == null)
        {
            t = new Thread (this, task3);
            t.start ();
        }
    }
}
public class task3 {
    public static void main(String args[]) {
        RunnableDemo R1 = new RunnableDemo( "Thread-1");
        R1.start();
        RunnableDemo R2 = new RunnableDemo( "Thread-2");
        R2.start();
    }
}