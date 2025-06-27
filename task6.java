class Counter1 {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class ThreadDemo2 extends Thread {
    Counter3 counter;

    ThreadDemo2(Counter3 counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.increment();

        }
    }
}

public class task6 {
    public static void main(String[] args) {
        Counter3 counter = new Counter3();
        ThreadDemo2 t1 = new ThreadDemo2(counter);
        ThreadDemo2 t2 = new ThreadDemo2(counter);

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