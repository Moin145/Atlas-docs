class OuterClass2 {
    static int x = 10;

    // Static nested class
    static class InnerClass2 {
        int y = 5;
    }
}

public class task13 {
    public static void main(String[] args) {
        OuterClass2.InnerClass2 myInner = new OuterClass2.InnerClass2();
        OuterClass2 myOuter2 = new OuterClass2();
        System.out.println(myInner.y + myOuter2.x);
    }
}