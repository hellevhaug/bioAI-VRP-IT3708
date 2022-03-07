import java.util.ArrayList;
import java.util.HashMap;

public class Offspring {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public HashMap<Integer, Patient> patients;
    public double[] parentFitness;

    public Offspring(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients,
            double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public ArrayList<Individual> createOffspring(ArrayList<Individual> parents,
            ArrayList<Double> parentsFitness, double pC, double pM, int lambda) {

        // TODO Implement Crossover and more mutations

        ArrayList<Individual> offsprings = insertionMutation(parents, pM, lambda);

        return offsprings;
    }

    public ArrayList<Individual> routeCrossover(ArrayList<Individual> parents, double pC, int lambda) {
        ArrayList<Individual> offsprings = new ArrayList<Individual>((int) lambda * parents.size());
        for (int epoch = 1; epoch <= lambda; epoch++) {
            for (int i = 0; i < parents.size(); i++) {
                Individual individual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                individual.routes = routes;

                for (int j = 0; j < individual.routes.size(); j++) {
                    if (Math.random() < pC) {
                        int tempValue = individual.routes.get(j);
                        int swapIndex = (int) ((Math.random() * individual.routes.size()));

                        individual.routes.remove(j);
                        individual.routes.add(swapIndex, tempValue);
                    }
                }
                offsprings.add(individual);
            }
        }
        return offsprings;
    }

    public ArrayList<Individual> insertionMutation(ArrayList<Individual> parents, double pM, int lambda) {
        ArrayList<Individual> offsprings = new ArrayList<Individual>((int) lambda * parents.size());
        for (int epoch = 1; epoch <= lambda; epoch++) {
            for (int i = 0; i < parents.size(); i++) {
                Individual individual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                ArrayList<Integer> routeIndices = new ArrayList<Integer>(parents.get(i).routeIndices); // Shallow copy O(n)
                individual.routes = routes;
                individual.routeIndices = routeIndices;

                for (int j = 0; j < individual.routes.size(); j++) {
                    if (Math.random() < pM) {
                        int tempValue = individual.routes.get(j);
                        int swapIndex = (int) ((Math.random() * individual.routes.size()));

                        individual.routes.remove(j);
                        individual.routes.add(swapIndex, tempValue);

                        if(tempValue == 0){
                            individual.routeIndices.remove(new Integer(j));
                            individual.routeIndices.add(swapIndex);

                        }
                    }
                }
                // System.out.println("Routes " + individual.routes);
                // System.out.println("Indices " + individual.routeIndices + "\n");

                offsprings.add(individual);
            }
        }
        return offsprings;
    }

    public ArrayList<Individual> swapMutation(ArrayList<Individual> parents, double pM, int lambda) {
        ArrayList<Individual> offsprings = new ArrayList<Individual>((int) lambda * parents.size());
        for (int epoch = 1; epoch <= lambda; epoch++) {
            for (int i = 0; i < parents.size(); i++) {
                Individual individual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                individual.routes = routes;
                for (int j = 0; j < individual.routes.size(); j++) {
                    if (Math.random() < pM) {
                        int tempValue = individual.routes.get(j);
                        int swapIndex = (int) ((Math.random() * individual.routes.size()));
                        int swapValue = individual.routes.get(swapIndex);
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
