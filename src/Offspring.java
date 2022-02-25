import java.util.ArrayList;

public class Offspring {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;
    public double[] parentFitness;

    public Offspring(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public ArrayList<ArrayList<Integer>> createOffspring(ArrayList<ArrayList<Integer>> parents,
            ArrayList<Double> parentsFitness, double pC, double pM, int lambda) {
        ArrayList<ArrayList<Integer>> offsprings = new ArrayList<ArrayList<Integer>>((int) lambda * parents.size());
        // TODO Implement Crossover and more mutations

        // Swap mutation
        for (int epoch = 1; epoch <= lambda; epoch++) {
            for (int i = 0; i < parents.size(); i++) {
                ArrayList<Integer> individual = new ArrayList<Integer>(parents.get(i)); // Shallow copy O(n)
                for (int j = 0; j < individual.size(); j++) {
                    if (individual.get(j) != 0 & Math.random() < pM) {
                        int tempValue = individual.get(j);
                        int swapValue = 0;
                        int swapIndex = 0;
                        while (swapValue == 0) {
                            swapIndex = (int) ((Math.random() * individual.size()));
                            swapValue = individual.get(swapIndex);
                        }
                        individual.set(j, swapValue);
                        individual.set(swapIndex, tempValue);
                    }
                }
                offsprings.add(individual);
            }
        }
        return offsprings;
    }
}
