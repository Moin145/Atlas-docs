import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ItemManager {
    private static ItemManager singleInstance;
    private final List<String> items;

    private ItemManager() {
        items = new ArrayList<>();
    }

    public static synchronized ItemManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new ItemManager();
        }
        return singleInstance;
    }

    public synchronized void insert(String item) {
        items.add(item);
    }

    public synchronized List<String> fetchAll() {
        return new ArrayList<>(items);
    }
}

public class DelafterDone {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ItemManager manager = ItemManager.getInstance();

        System.out.println("Enter items (type 'exit' to finish program):");

        boolean beforeDone = true; // flag to track if we are before/after "done"

        while (true) {
            String input = sc.nextLine();


            if (input.equalsIgnoreCase("exit")) {
                break;
            }


            if (input.equalsIgnoreCase("done")) {
                beforeDone = false;
                continue;
            }

            // only save items entered before "done"
            if (beforeDone) {
                manager.insert(input);
            }
        }


        System.out.println("\nFinal List of Items (before 'done'):");
        for (String item : manager.fetchAll()) {
            System.out.println("- " + item);
        }

        sc.close();
    }
}
