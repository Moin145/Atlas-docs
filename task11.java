class OuterClass1 {
    int x = 10;

    class InnerClass1 {
        int y = 5;
    }
}

public class task11 {
    public static void main(String[] args) {
        OuterClass1 myOuter = new OuterClass1();
        OuterClass1.InnerClass1 myInner = myOuter.new InnerClass1();
        System.out.println(myInner.y + myOuter.x);
    }
}