public class task1 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};

        try {
            System.out.println(arr[5]); // Invalid index
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of range!");
        } finally {
            System.out.println("Done!");
        }
    }
}