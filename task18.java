import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class task18 {
    public static void main(String[] args) {
        // Create a list of integers
        List<Integer> myList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            myList.add(i);
        }

        // 1. Traverse using Iterator
        System.out.println("---- Using Iterator ----");
        Iterator<Integer> it = myList.iterator();
        while (it.hasNext()) {
            Integer value = it.next();
            System.out.println("Iterator Value:: " + value);
        }

        // 2. Using anonymous class for Consumer
        System.out.println("\n---- Using Anonymous Class ----");
        myList.forEach(new Consumer<Integer>() {
            public void accept(Integer t) {
                System.out.println("forEach Anonymous Class Value:: " + t);
            }
        });

        // 3. Using named Consumer implementation
        System.out.println("\n---- Using Named Consumer Class ----");
        MyConsumer action = new MyConsumer();
        myList.forEach(action);

        // 4. Using Lambda Expression
        System.out.println("\n---- Using Lambda Expression ----");
        myList.forEach(t -> System.out.println("Lambda Value:: " + t));
    }
}

// Consumer implementation class
class MyConsumer implements Consumer<Integer> {
    public void accept(Integer t) {
        System.out.println("Consumer Impl Value:: " + t);
    }
}


//WHy THiS OUTPUT ?
//
//1 Iterators traversal is a traditional way
//2 Anonymous class shows flexibility of inline consumer logic
//3 Named Consumer is reusable and shows functional interface use