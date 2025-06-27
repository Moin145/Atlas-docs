// Custom exception class
class AgeException extends Exception {
    public AgeException(String message) {
        super(message); // Pass message to Exception class
    }
}

public class task008 {


    static void checkAge(int age) throws AgeException {
        if (age < 18) {
            throw new AgeException("Age must be at least 18 to proceed.");
        } else {
            System.out.println("Access granted. Age is valid: " + age);
        }
    }

    public static void main(String[] args) {
        try {
            int userAge = 19;
            checkAge(userAge);
        } catch (AgeException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
}