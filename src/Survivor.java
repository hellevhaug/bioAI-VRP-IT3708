import java.util.ArrayList;
import java.util.Iterator;

public class Survivor {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;
    public double[] parentFitness;

    public Survivor(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }
    
    static int maxSum(int arr[], int n, int k)
    {
        // n must be greater
        if (n < k) {
            System.out.println("Invalid");
            return -1;
        }
 
        // Compute sum of first window of size k
        int max_sum = 0;
        for (int i = 0; i < k; i++)
            max_sum += arr[i];
 
        // Compute sums of remaining windows by
        // removing first element of previous
        // window and adding last element of
        // current window.
        int window_sum = max_sum;
        for (int i = k; i < n; i++) {
            window_sum += arr[i] - arr[i - k];
            max_sum = Math.max(max_sum, window_sum);
        }
 
        return max_sum;
    }

    public ArrayList<ArrayList<Integer>> deterministicOffspringSelection(ArrayList<ArrayList<Integer>> offspring, ArrayList<Double> offspringfitness, int nbrOffspring){
        ArrayList<ArrayList<Integer>> survivors = new ArrayList<ArrayList<Integer>>();

        KGreatestElements kGreatestElements = new KGreatestElements();
        Iterator<double[]> iterator = kGreatestElements.FirstKelements(offspringfitness, offspringfitness.size(), nbrOffspring);
        while (iterator.hasNext()) {
            System.out.print(iterator.next()[1] + " ");
        }

        return survivors;
    }
}
