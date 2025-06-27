import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Counter4 using ReentrantLock for thread safety
class Counter4 {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // acquire the lock
        try {
            count++;   // critical section
        } finally {
            lock.unlock(); // release the lock
        }
    }

    public int getCount() {
        return count;
    }
}

// ThreadDemo4 uses Counter4
class ThreadDemo4 extends Thread {
    Counter4 counter;

    ThreadDemo4(Counter4 counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            counter.increment(); // safely increment
        }
    }
}

// Main class for Task10
public class task8 {
    public static void main(String[] args) {
        Counter4 counter = new Counter4();

        ThreadDemo4 t1 = new ThreadDemo4(counter);
        ThreadDemo4 t2 = new ThreadDemo4(counter);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final count: " + counter.getCount());
    }
}