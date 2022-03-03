import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EA {
    public static void main(String args[]) {
        // Hyper-parameters
        int epochs = 300;
        int trainInstanceIndex = 2;
        int popSize = 1000;
        double pC = 0.9;
        double pM = 0.007;
        int lambda = 7; // 3 is more populare now a days

        // Load JSON
        TrainData data = new TrainData();
        JSONArray trainArray = data.getTrainArray();
        JSONObject trainInstance = (JSONObject) trainArray.get(trainInstanceIndex);

        // Get datastructuresgit 
        int nbrNurses = Integer.parseInt(Long.toString((Long) trainInstance.get("nbr_nurses")));
        int capacityNurse = Integer.parseInt(Long.toString((Long) trainInstance.get("capacity_nurse")));
        double benchmark = (double) trainInstance.get("benchmark");
        Depot depot = data.getDepot(trainInstance);
        HashMap<Integer, Patient> patients = data.getPatients(trainInstance);
        double[][] travelTimes = data.getTravelTime(trainInstance);

        // Set up SGA
        Population populationClass = new Population(nbrNurses, capacityNurse, depot, patients, travelTimes);
        ArrayList<Individual> population = populationClass.generatePopArray(trainInstance, nbrNurses, popSize);

        for (int epoch = 1; epoch < epochs; epoch++) {
            // Fitness of new Popuation
            Fitness fitnessClass = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
            ArrayList<Double> populationgFitness = fitnessClass.getPenaltyFitness(population);
            double popMaxFitVal = fitnessClass.prevMaxFitness;
            // System.out.println("Min Fitness " + fitnessClass.prevMinFitness);
            System.out.println("Min Penalty " + fitnessClass.prevMinPenalty);
            System.out.println("Min Fitness " + fitnessClass.prevMinFitness);
            ArrayList<Double> transFitness = fitnessClass.transformFitnessArray(populationgFitness, popMaxFitVal);

            // Parent Selection
            // Consider to normalize fitness for greater selection pressure
            Parent parentClass = new Parent(nbrNurses, capacityNurse, depot, patients, travelTimes);
            ArrayList<Individual> parents = parentClass.selectParentsProbabilistic(transFitness, population,
                    popSize);
            ArrayList<Double> parentTransFitness = parentClass.parentFitness;

            // Offspring
            Offspring offspringClass = new Offspring(nbrNurses, capacityNurse, depot, patients, travelTimes);
            ArrayList<Individual> offspring = offspringClass.createOffspring(parents, parentTransFitness, pC,
                    pM, lambda);
            ArrayList<Double> offspringFitness = fitnessClass.getPenaltyFitness(offspring); // This can definitly
                                                                                                 // be optimized
            double offMaxFitVal = fitnessClass.prevMaxFitness;
            ArrayList<Double> offspringTransFitness = fitnessClass.transformFitnessArray(offspringFitness,
                    offMaxFitVal);

            // Survivor Selection
            // (lambda, mu)-selection, based on offspring only (lambda > mu)
            Survivor survivorClass = new Survivor(nbrNurses, capacityNurse, depot, patients, travelTimes);
            ArrayList<Individual> survivors = survivorClass.deterministicOffspringSelection(offspring,
                    offspringTransFitness, popSize);
            ArrayList<Double> survivorTransFitness = survivorClass.prevSurvivorFitness;
            population = survivors;
        }
    }
}