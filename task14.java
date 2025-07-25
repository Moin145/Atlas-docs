class OuterClass3 {
    int x = 50;

    class InnerClass3 {
        public int innerMethod() {
            return x;
        }
    }
}

public class task14 {
    public static void main(String[] args) {
        OuterClass3 myOuter = new OuterClass3();
        OuterClass3.InnerClass3 myInner = myOuter.new InnerClass3();
        System.out.println(myInner.innerMethod());
    }
}