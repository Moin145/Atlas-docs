public class t5d16 {

    // Method to perform Bubble Sort
    public static void bubbleSort(int[] arr) {
        int n = arr.length;

        // Outer loop for number of passes
        for (int i = 0; i < n - 1; i++) {

            // Inner loop for pair-wise comparison
            for (int j = 0; j < n - i - 1; j++) {

                // Swapping if the current element is greater than the next
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        // Sample array
        int[] arr = {29, 10, 14, 37, 13};

        System.out.println("Original array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }

        // Call bubble sort
        bubbleSort(arr);

        System.out.println("\nSorted array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}