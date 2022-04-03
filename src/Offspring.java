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

        ArrayList<Individual> offsprings = insertionMutation(parents, pM, lambda);
        // System.out.println(offsprings.size());
        offsprings = routeCrossover(offsprings, pM);
        // System.out.println(offsprings.size());
        return offsprings;
    }

    public ArrayList<Individual> routeCrossover(ArrayList<Individual> parents, double pC) {
        ArrayList<Individual> offsprings = new ArrayList<Individual>(parents.size());
        ArrayList<Integer> availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < parents.size(); i++) {
            availableIndices.add(i);
        }

        for (int i = 0; i < parents.size(); i++) {
            if (Math.random() < pC & availableIndices.contains(i)) {
                Individual individual1 = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes1 = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                individual1.routes = routes1;

                int randint = (int) ((Math.random() * availableIndices.size()));
                int rando = availableIndices.get(randint);

                availableIndices.remove(randint);
                availableIndices.remove(new Integer(i));

                Individual individual2 = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes2 = new ArrayList<Integer>(parents.get(rando).routes); // Shallow copy O(n)
                individual2.routes = routes2;

                int randomZero = (int) ((Math.random() * (this.nbrNurses - 0.5)));
                int zero1Count = 0;
                int route1Start = 0;
                int route1Stop = 0;
                ArrayList<Integer> route1 = new ArrayList<Integer>();

                int zero2Count = 0;
                int route2Start = 0;
                int route2Stop = 0;
                ArrayList<Integer> route2 = new ArrayList<Integer>();

                for (int j = 0; j < individual1.routes.size(); j++) {
                    if (individual1.routes.get(j) == 0) {
                        zero1Count++;
                        if (zero1Count == randomZero) {
                            route1Start = j;
                        } else if (zero1Count == randomZero + 1) {
                            route1Stop = j;
                            break;
                        }
                    } else if (zero1Count == randomZero) {
                        route1.add(individual1.routes.get(j));
                    }
                }

                for (int j = 0; j < individual2.routes.size(); j++) {
                    if (individual2.routes.get(j) == 0) {
                        zero2Count++;
                        if (zero2Count == randomZero) {
                            route2Start = j;
                        } else if (zero2Count == randomZero + 1) {
                            route2Stop = j;
                            break;
                        }
                    } else if (zero2Count == randomZero) {
                        route2.add(individual2.routes.get(j));
                    }
                }

                for (Integer num : route1) {
                    individual2.routes.remove(new Integer(num));
                }
                for (Integer num : route2) {
                    individual1.routes.remove(new Integer(num));
                }
                individual1 = fillMissingPatients(individual1, route2);
                individual2 = fillMissingPatients(individual2, route1);

                offsprings.add(individual1);
                offsprings.add(individual2);
            }
            else {
                Individual individual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(parents.get(i).routes); // Shallow copy O(n)
                individual.routes = routes;
                offsprings.add(individual);
            }
        }
        return offsprings;
    }

    public Individual fillMissingPatients(Individual offspring, ArrayList<Integer> missingVals) {
        Fitness fitnessClass = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
        for (Integer missingNo : missingVals) { // Fill in these patients
            ArrayList<Individual> competingSolutions = new ArrayList<Individual>(offspring.routes.size());
            for (int i = 0; i < offspring.routes.size(); i++) {
                Individual newIndividual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> newArrayList = new ArrayList<Integer>(offspring.routes);
                newArrayList.add(i, missingNo);
                newIndividual.routes = newArrayList;
                competingSolutions.add(newIndividual);
            }
            // Last place
            Individual newIndividual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
            ArrayList<Integer> newArrayList = new ArrayList<Integer>(offspring.routes);
            newArrayList.add(missingNo);
            newIndividual.routes = newArrayList;
            competingSolutions.add(newIndividual);

            Double minValue = Math.pow(10, 10);
            int minIndex = 0;
            int index = 0;
            for (Double fitness : fitnessClass.getPenaltyFitness(competingSolutions)) {
                if (fitness < minValue) {
                    minValue = fitness;
                    minIndex = index;
                }
                index++;
            }
            offspring = competingSolutions.get(minIndex);
        }
        return offspring;
    }

    public ArrayList<Individual> insertionMutation(ArrayList<Individual> parents, double pM, int lambda) {
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

                        individual.routes.remove(j);
                        individual.routes.add(swapIndex, tempValue);
                    }
                }

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
