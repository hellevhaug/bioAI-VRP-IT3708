import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EA {
    public static void main(String args []){
        // Hyper-parameters
        int trainInstanceIndex = 7;
        int popSize = 50000;
        double pC = 0.9;
        double pM = 0.0007;
        int lambda = 7;

        // Load JSON
        TrainData data = new TrainData();
        JSONArray trainArray = data.getTrainArray();
        JSONObject trainInstance = (JSONObject) trainArray.get(trainInstanceIndex);

        // Get datastructures
        int nbrNurses = Integer.parseInt(Long.toString( (Long) trainInstance.get("nbr_nurses")));
        int capacityNurse = Integer.parseInt(Long.toString( (Long) trainInstance.get("capacity_nurse")));
        double benchmark =  (double) trainInstance.get("benchmark");
        Depot depot = data.getDepot(trainInstance);
        Patient[] patients = data.getPatients(trainInstance);
        double[][] travelTimes = data.getTravelTime(trainInstance);
         
        // Set up SGA
        Population pop = new Population(nbrNurses, capacityNurse, depot, patients, travelTimes);
        ArrayList<ArrayList<Integer>> population = pop.generatePopArray(trainInstance, nbrNurses, popSize);

        Fitness fit = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
        double[] fitness = fit.getRegularFitnessArray(population);

        // Consider to normalize fitness for greater selection pressure 
        Parent par = new Parent(nbrNurses, capacityNurse, depot, patients, travelTimes);
        ArrayList<ArrayList<Integer>> parents = par.selectParentsProbabilistic(fitness, population, popSize);
        double[] parentFitness = par.parentFitness;

        Offspring off = new Offspring();
        ArrayList<ArrayList<Integer>> offspring = off.createOffspring(population, pC, pM, lambda);
    }
}