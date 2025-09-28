class OuterClass4 {
    int x = 10;

    // Private inner class
    private class InnerClass4 {
        int y = 5;

        int getSum() {
            return x + y;  // Inner class can access outer class's variable
        }
    }

    // Public method to access the inner class logic
    public int calculateSum() {
        InnerClass4 inner = new InnerClass4();
        return inner.getSum();
    }
}

public class task12 {
    public static void main(String[] args) {
        OuterClass4 myOuter = new OuterClass4();
        int result = myOuter.calculateSum();
        System.out.println("Sum: " + result);
    }
}