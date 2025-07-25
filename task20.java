import java.util.*;

public class task20 {

    public static void main(String[] args) {

        // ArrayList demonstration
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Zara");
        arrayList.add("Mahnaz");
        arrayList.add("Ayan");
        System.out.println("ArrayList Elements:");
        arrayList.forEach(name -> System.out.println("\t" + name));

        // LinkedList demonstration
        List<String> linkedList = new LinkedList<>();
        linkedList.add("Zara");
        linkedList.add("Mahnaz");
        linkedList.add("Ayan");
        System.out.println("\nLinkedList Elements:");
        linkedList.forEach( name -> System.out.println("\t" + name));

        // HashSet demonstration
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Zara");
        hashSet.add("Mahnaz");
        hashSet.add("Ayan");
        System.out.println("\nHashSet Elements (no duplicates, unordered):");
        hashSet.forEach(name -> System.out.println("\t" + name));

        // HashMap demonstration
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Zara", "8");
        hashMap.put("Mahnaz", "31");
        hashMap.put("Ayan", "12");
        hashMap.put("Daisy", "14");
        System.out.println("\nHashMap Elements (key-value pairs):");
        hashMap.forEach((key, value) -> System.out.println("\t" + key + " = " + value));
    }
}