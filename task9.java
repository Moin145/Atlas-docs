import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Counter6 {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

// Thread class using Counter4
class ThreadDemo6 extends Thread {
    private final Counter6 counter6;

    ThreadDemo6(Counter6 counter) {
        this.counter6 = counter;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            counter6.increment();
        }
    }
}


public class task9 {
    public static void main(String[] args) {
        Counter6 counter = new Counter6(); // ✅ instance of Counter4

        ThreadDemo6 t1 = new ThreadDemo6(counter);
        ThreadDemo6 t2 = new ThreadDemo6(counter);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final count: " + counter.getCount()); // Should print 20
    }
}