//class Test extends Thread{
//    public void run(){
//        System.out.println("thread started.");
//    }
//}

public class task18 extends Thread {
    public void run(){
        System.out.println("thread started.");
    }
    public static void main(String args[]){

//        Test t1 = new Test();
        task18 th1 = new task18();
        th1.start();

//        t1.run1();
    }
}