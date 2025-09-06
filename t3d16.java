import java.util.Scanner;

public class t3d16 {

    // Method to perform selection sort
    public static void selectionSort(int[] arr) {
        int n = arr.length;

        // Outer loop for each position
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // Inner loop to find the smallest element
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Swap the found minimum with the current element
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    // Main method to read input and call selectionSort
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get size of the array
        System.out.print("Enter the number of elements: ");
        int n = sc.nextInt();

        // Create array and take input
        int[] arr = new int[n];
        System.out.println("Enter the elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        // Sort the array
        selectionSort(arr);

        // Display sorted array
        System.out.println("Sorted array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }

        sc.close();
    }
}