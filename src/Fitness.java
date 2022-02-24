import java.util.ArrayList;
import java.util.ListIterator;

public class Fitness {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;
    public double prevMaxFitness;
    public double prevMinFitness;

    public Fitness(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public double[] getRegularFitness(int[][][] population) {
        double[] fitness = new double[population.length];
        double min_val = 30000;
        for (int i = 0; i < population.length; i++) {
            int[][] individual = population[i];
            double indFitness = 0;
            for (int j = 0; j < individual.length; j++) {
                int[] patientRoute = individual[j];
                for (int k = 0; j < patientRoute.length; k++) {
                    indFitness += travelTimes[patientRoute[k]][patientRoute[k + 1]];
                    if (patientRoute[k + 1] == 0) {
                        break;
                    }
                }
            }
            fitness[i] = indFitness;
            if (min_val > indFitness) {
                min_val = indFitness;
                System.out.println("i: " + i + " Fitness: " + min_val);
            }
        }
        return fitness;
    }

    public ArrayList<Double> getRegularFitnessArray(ArrayList<ArrayList<Integer>> population) {
        ArrayList<Double> fitness = new ArrayList<Double>();
        double maxVal = 0;
        double minVal = Math.pow(10, 10);
        for (int i = 0; i < population.size(); i++) {
            ArrayList<Integer> individual = population.get(i);
            double indFitness = 0;
            for (int j = 0; j < individual.size() - 1; j++) {
                indFitness += travelTimes[individual.get(j)][individual.get(j + 1)];

            }
            if(indFitness > maxVal){
                maxVal = indFitness;
            }
            if(indFitness < minVal){
                minVal = indFitness;
            }
            fitness.add(indFitness);
        }
        this.prevMaxFitness = maxVal;
        this.prevMinFitness = minVal;

        return fitness;
    }

    public ArrayList<Double> transformFitnessArray(ArrayList<Double> fitness, double maxValue) {
        ArrayList<Double> transformedFitness = new ArrayList<Double>();
        
        ListIterator<Double> iter = fitness.listIterator();
        while( iter.hasNext() ){
            Double value = iter.next();
            Double newValue = maxValue - value;
            transformedFitness.add(newValue);
        }
        return transformedFitness;
    }   
}
