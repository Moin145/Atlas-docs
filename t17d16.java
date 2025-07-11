public class t17d16 {

    public static void main(String[] args) {

        //  unsorted array
        int[] arr = {9, 3, 7, 1, 6, 4};

        // Startingg the quick sorting process on the entire array
        quickSort(arr, 0, arr.length - 1);


        System.out.println("Sorted array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    // Thiis method performs quick sorting on the array between low and high
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {

            int pivotIndex = partition(arr, low, high);

            // Apply quick sorting to the left subarray
            quickSort(arr, low, pivotIndex - 1);

            // Apply quick sorting to the right subarray
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // This method places the pivot in its correct position and arranges elementss round it
    static int partition(int[] arr, int low, int high) {

        // Choosing the first element as pivot
        int pivot = arr[low];
        int m = low;

        // Loop through elements after pivot
        for (int k = low + 1; k <= high; k++) {

            // If current element is less than pivot,move it to left partition
            if (arr[k] < pivot) {
                m++;
                // Swap current element with the element at position m
                int temp = arr[k];
                arr[k] = arr[m];
                arr[m] = temp;
            }
        }

        // Finallly, place the pivot at its correct sorted position
        int temp = arr[low];
        arr[low] = arr[m];
        arr[m] = temp;

        // Return the index where the pivot is placed
        return m;
    }
}