import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

interface Greeting {
    default void hello() {
        System.out.println("Hello from Interface!");
    }

    static void staticMethod() {
        System.out.println("Static method in interface.");
    }

    void greet(String name); // functional method
}

public class task17 {

    public static void main(String[] args) {

        // 1. Lambda Expression
        Greeting g = (name) -> System.out.println("Hi, " + name + "!");
        g.greet("Moin");

        // 2. Static Method in Interface
        Greeting.staticMethod();

        // 3. Default Method
        g.hello();

        // 4. forEach + Functional Interface + Method Reference
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        System.out.println("\nUsing forEach with lambda:");
        names.forEach(n -> System.out.println(n));

        System.out.println("\nUsing forEach with method reference:");
        names.forEach(System.out::println);

        // 5. Stream API
        System.out.println("\nNames starting with 'A':");
        names.stream()
                .filter(name -> name.startsWith("A"))
                .forEach(System.out::println);

        // 6. java.time API
        LocalDate today = LocalDate.now();
        System.out.println("\nToday's Date: " + today);
    }
}