// Runnable implementation
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Code executed in a new thread via Runnable.");
    }
}

// Thread class extension
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Code executed in a new thread via Thread extension.");
    }
}


public class task19 {
    public static void main(String[] args) {
        // Create instance of MyRunnable and wrap it in a Thread
        MyRunnable runnableInstance = new MyRunnable();
        Thread t1 = new Thread(runnableInstance); // thread using Runnable


        MyThread threadInstance = new MyThread(); // thread by extending Thread


//        t1.start();
        t1.run();// Executes MyRunnable's run()
        threadInstance.start(); // Executes MyThread's run()
    }
}