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
    public ArrayList<ArrayList<Double>> fitnessMatrix; // Keeps record of the fitness for each path

    public Fitness(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public ArrayList<Double> getRegularFitness(ArrayList<ArrayList<Integer>> population) {
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
