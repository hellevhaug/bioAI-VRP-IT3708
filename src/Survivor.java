import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Survivor {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;
    public double[] parentFitness;
    public ArrayList<Double> prevSurvivorFitness;

    public Survivor(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public static Map sortByValue(Map unsortedMap) {
		Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

    public ArrayList<Individual> deterministicOffspringSelection(ArrayList<Individual>offspring, ArrayList<Double> offspringTransFitness, int nbrOffspring){
        ArrayList<Individual> survivors = new ArrayList<Individual>();
        ArrayList<Double> survivorFitness = new ArrayList<Double>();
        
        HashMap<Integer, Double> map = new HashMap<Integer, Double>(); // This is not neccassary, create this in Offspring class
        for (int i = 0; i < offspringTransFitness.size(); i++) {
            map.put(i, offspringTransFitness.get(i));
        }

        Map sortedMap = sortByValue(map); 

        int i = 0;
        for (Object entry : sortedMap.keySet()){
            // System.out.println(entry + "  " + sortedMap.get(entry));
            survivors.add(offspring.get((int) entry));
            survivorFitness.add((double) sortedMap.get(entry));
            if(i == nbrOffspring-1){
                break;
            }
            i ++;

}
        this.prevSurvivorFitness = survivorFitness;
        return survivors;
    }
}
