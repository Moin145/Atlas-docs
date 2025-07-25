// A Class that represents user-defined exception
class MyException extends Exception { // exception is a predefined class – parent class for MyException
    public MyException(String m) {
        super(m); // calling the parent class constructor with parameters
    }
}

// A Class that uses the above MyException
public class task8 {
    public static void main(String args[]) {
        try {

            // Throw an object of user-defined exception
            throw new MyException("This is a custom exception");
        }
        catch (MyException ex) {
            System.out.println("Caught");
            System.out.println(ex.getMessage());
        }
    }
}