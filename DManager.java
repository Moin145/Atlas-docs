//Singleton + List Manager
//
//1. Singleton Pattern Requirements
//   - A private static variable that stores the single instance.
//        - A private constructor to prevent direct instantiation.
//   - A **public static synchronized `getInstance()`** that ensures only one instance ever exists.
//   - If no instance exists → create one.
//If it exists → return the existing instance.
//
//2. List Management
//   - Use a private list (thread‑safe by synchronizing methods).
//        - Public methods:
//        - addItem(String item) → adds item to list.
//        - removeItem(String item) → removes item from list.
//        - listItems() → retrieves all items.
//
//3. Thread Safety
//   - Make `synchronized so multiple threads can't create multiple objects simultaneously.
//        - Also make list‑manipulation methods (`addItem`, `removeItem`, `listItems`) synchronized to avoid race conditions

import java.util.ArrayList;
import java.util.List;

public class DManager {

    private static DManager instance;

    private List<String> items;

    private DManager() {
        items = new ArrayList<>();
    }

    public static synchronized DManager getInstance() {
        if (instance == null) {
            instance = new DManager();
        }
        return instance;
    }

    public synchronized void addItem(String item) {
        items.add(item);
    }

    public synchronized void removeItem(String item) {
        items.remove(item);
    }

    public synchronized List<String> listItems() {
        // Return a copy to prevent external modifications
        return new ArrayList<>(items);
    }
}
class SingletonDemo {
    public static void main(String[] args) {
        DManager manager = DManager.getInstance();

        manager.addItem("Apple");
        manager.addItem("Banana");
        manager.addItem("Cherry");

        System.out.println("Items: " + manager.listItems());

        manager.removeItem("Banana");

        System.out.println("Items after removal: " + manager.listItems());

        // Another reference will return the same instance
        DManager anotherManager = DManager.getInstance();
        anotherManager.addItem("Date");

        System.out.println("Items from second reference: " + anotherManager.listItems());
    }
}


