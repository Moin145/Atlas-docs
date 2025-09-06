import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DataManager {
    private static DataManager instance;
    private List<String> itemList;

    private DataManager() {
        itemList = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void addItem(String item) {
        if (itemList.size() < 100) {
            itemList.add(item);
        }
    }

    public void removeItem(String item) {
        itemList.remove(item);
    }

    public List<String> getList() {
        return new ArrayList<>(itemList);
    }
}

public class prac2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            DataManager dataManager = DataManager.getInstance();
        } catch (IllegalStateException e) {
            System.out.println("Caught IllegalStateException: " + e.getMessage());
        }

        DataManager dataManager = DataManager.getInstance();

        String input;
        while (!(input = scanner.nextLine()).equalsIgnoreCase("done")) {
            dataManager.addItem(input);
        }

        String itemToRemove = scanner.nextLine();
        dataManager.removeItem(itemToRemove);

        List<String> updatedList = dataManager.getList();
        for (String item : updatedList) {
            System.out.println(item);
        }

        scanner.close();
    }
}