
class Counter3 {
    private int count = 0;

    public void increment() {
        synchronized (this) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}


class ThreadDemo3 extends Thread {
    Counter3 counter3;

    ThreadDemo3(Counter3 counter3) {
        this.counter3 = counter3;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            counter3.increment(); // Thread-safe
        }
    }
}


public class task7 {
    public static void main(String[] args) {
        Counter3 counter = new Counter3();

        ThreadDemo3 t1 = new ThreadDemo3(counter);
        ThreadDemo3 t2 = new ThreadDemo3(counter);

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