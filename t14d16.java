public class t14d16 {


    public static void main(String[] args) {
        // Defining an unsorted array
        int[] arr = {6, 3, 8, 5, 2, 7, 4, 1};

        // Callingg mergeSort on the full array
        mergeSort(arr, 0, arr.length - 1);

        // Printing the sorted array
        System.out.print("Sorted array: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    // This method divides the array and calls merge on sorted halves
    static void mergeSort(int[] arr, int low, int high) {
        if (low < high) {
            // Finding the middle index
            int mid = (low + high) / 2;

            // Sorting the left half
            mergeSort(arr, low, mid);

            // Sorting the right half
            mergeSort(arr, mid + 1, high);

            // Merging the sorted halves
            merge(arr, low, mid, high);
        }
    }

    // This method merges two sorted parts of the array
    static void merge(int[] arr, int low, int mid, int high) {
        // Calculating sizes of left and right subarrays
        int n1 = mid - low + 1;
        int n2 = high - mid;

        // Creating temporary arrays
        int[] left = new int[n1];
        int[] right = new int[n2];

        // Copying data into temp arrays
        for (int i = 0; i < n1; i++)
            left[i] = arr[low + i];
        for (int j = 0; j < n2; j++)
            right[j] = arr[mid + 1 + j];

        // Merging the two sorted arrays
        int i = 0, j = 0, k = low;
        while (i < n1 && j < n2) {
            // Comparing elements from both arrays
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }

        // Copying remaining elements from left array (if any)
        while (i < n1) {
            arr[k] = left[i];
            i++;
            k++;
        }

        // Copying remaining elements from right array (if any)
        while (j < n2) {
            arr[k] = right[j];
            j++;
            k++;
        }
    }
}