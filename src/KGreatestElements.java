import java.io.*;
import java.util.*;

class DummyObjectComparator implements Comparator<double[]> {

    // Overriding compare() method of Comparator
    public int compare(double[] s1, double[] s2) {
        if (s1[1] == s2[1]) {
            return 0;
        } else if (s1[1] > s1[1]) {
            return -1;
        }
        return 1;
    }
}

class KGreatestElements {

    public static Iterator<double[]> FirstKelements(ArrayList<Double> arr, int size, int k) {

        // Creating Min Heap for given
        // array with only k elements
        // Create min heap with priority queue
        PriorityQueue<double[]> minHeap = new PriorityQueue<>(size, new DummyObjectComparator());
        for (int i = 0; i < k; i++) {
            double[] temp = { i, arr.get(i) };
            minHeap.add(temp);
        }

        // Loop For each element in array
        // after the kth element
        for (int i = k; i < size; i++) {

            // If current element is smaller
            // than minimum ((top element of
            // the minHeap) element, do nothing
            // and continue to next element
            if (minHeap.peek()[1] > arr.get(i))
                continue;

            // Otherwise Change minimum element
            // (top element of the minHeap) to
            // current element by polling out
            // the top element of the minHeap
            else {
                minHeap.poll();
                double[] temp = { i, arr.get(i) };
                minHeap.add(temp);
            }
        }

        // Now min heap contains k maximum
        // elements, Iterate and print
        Iterator<double[]> iterator = minHeap.iterator();

        return iterator;

        // while (iterator.hasNext()) {
        //     System.out.print(iterator.next()[1] + " ");
        // }
    }

    // test code
    public static void main(String[] args) {
        ArrayList<Double> arr = new ArrayList<Double>();
        arr.add(11.0);
        arr.add(3.0);
        arr.add(2.0);
        arr.add(1.0);
        arr.add(15.0);
        arr.add(5.0);
        arr.add(4.0);
        arr.add(45.0);
        arr.add(88.0);
        arr.add(96.0);
        arr.add(50.0);
        arr.add(45.0);
        int size = arr.size();

        // Size of Min Heap
        int k = 3;

        FirstKelements(arr, size, k);
    }
}

// This code is contributed by Vansh Sethi
