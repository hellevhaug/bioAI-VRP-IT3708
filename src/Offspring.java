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

    public ArrayList<Individual> createOffspring(ArrayList<Individual>  parents,
            ArrayList<Double> parentsFitness, double pC, double pM, int lambda) {
                ArrayList<Individual> offsprings = new ArrayList<Individual>((int) lambda * parents.size());
        // TODO Implement Crossover and more mutations

        // Swap mutation
        for (int epoch = 1; epoch <= lambda; epoch++) {
            for (int i = 0; i < parents.size(); i++) {
                Individual individual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                individual.routes = routes;
                for (int j = 0; j < individual.routes.size(); j++) {
                    if (individual.routes.get(j) != 0 & Math.random() < pM) {
                        int tempValue = individual.routes.get(j);
                        int swapValue = 0;
                        int swapIndex = 0;
                        while (swapValue == 0) {
                            swapIndex = (int) ((Math.random() * individual.routes.size()));
                            swapValue = individual.routes.get(swapIndex);
                        }
                        individual.routes.set(j, swapValue);
                        individual.routes.set(swapIndex, tempValue);
                    }
                }
                offsprings.add(individual);
            }
        }
        return offsprings;
    }
}
