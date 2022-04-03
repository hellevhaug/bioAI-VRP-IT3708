import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LocalSearch {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public HashMap<Integer, Patient> patients;
    public double[] parentFitness;

    public LocalSearch(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients,
            double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public static void main(String[] args) {

        int[][] bestRoutes = { {}, {}, {},
                { 95, 92, 42, 57, 15, 43, 14, 44, 38, 86, 16, 85, 99, 96, 6, 94, 97, 87, 2, 40, 53 },
                { 89, 18, 45, 46, 36, 47, 48, 7, 88, 62, 11, 63, 64, 49, 19, 82, 8, 83, 60, 5, 84, 17, 61, 91, 100, 37,
                        98, 93, 59, 13, 58 },
                { 26, 21, 73, 72, 39, 67, 23, 41, 22, 74, 75, 56, 4, 25, 55, 54, 24, 29, 68, 80, 77, 12, 28 }, {}, {},
                {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
                { 27, 69, 1, 33, 51, 30, 20, 65, 71, 9, 81, 50, 76, 3, 79, 78, 34, 35, 66, 32, 90, 10, 70, 31, 52 },
                {} };
        int trainInstanceIndex = 9;

        // Load JSON
        TrainData data = new TrainData();
        JSONArray trainArray = data.getTrainArray();
        JSONObject trainInstance = (JSONObject) trainArray.get(trainInstanceIndex);

        // Get datastructuresgit
        int nbrNurses = Integer.parseInt(Long.toString((Long) trainInstance.get("nbr_nurses")));
        int capacityNurse = Integer.parseInt(Long.toString((Long) trainInstance.get("capacity_nurse")));
        // double benchmark = (double) trainInstance.get("benchmark");
        Depot depot = data.getDepot(trainInstance);
        HashMap<Integer, Patient> patients = data.getPatients(trainInstance);
        double[][] travelTimes = data.getTravelTime(trainInstance);

        Population populationClass = new Population(nbrNurses, capacityNurse, depot, patients, travelTimes);
        Fitness fitnessClass = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
        Parent parentClass = new Parent(nbrNurses, capacityNurse, depot, patients, travelTimes);
        Offspring offspringClass = new Offspring(nbrNurses, capacityNurse, depot, patients, travelTimes);
        Survivor survivorClass = new Survivor(nbrNurses, capacityNurse, depot, patients, travelTimes);

        Individual bestIndividual = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
        ArrayList<Integer> tempRoutes = new ArrayList<Integer>();
        for (int[] route : bestRoutes) {
            for (int nbr : route) {
                tempRoutes.add(nbr);
            }
            tempRoutes.add(0);
        }
        bestIndividual.routes = tempRoutes;
        LocalSearch localSeach = new LocalSearch(nbrNurses, capacityNurse, depot, patients, travelTimes);
        for (int i = 0; i < 6; i++) {
            bestIndividual = localSeach.oneStepHillClimb(bestIndividual);
        }

        ArrayList<Individual> bestInd = new ArrayList<Individual>();
        bestInd.add(bestIndividual);
        System.out.println(fitnessClass.getPenaltyFitness(bestInd));
        fitnessClass.getPenaltyFitness(bestInd);

        Util utilClass = new Util(nbrNurses, capacityNurse, depot, patients, travelTimes);
        String bestRoute = utilClass.getValidationFormat(bestIndividual);
        System.out.println("\n" + bestRoute + "\n");
        utilClass.createFile(bestRoute, trainInstanceIndex);
    }

    public Individual oneStepHillClimb(Individual individual) {
        Fitness fitnessClass = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
        ArrayList<Integer> originalBestInd = new ArrayList<Integer>(individual.routes); // Shallow Copy
        ArrayList<Individual> population = new ArrayList<Individual>(originalBestInd.size() * originalBestInd.size());
        for (int i = 0; i < originalBestInd.size(); i++) {
            int mutationNbr = originalBestInd.get(i);
            for (int j = 0; j < originalBestInd.size(); j++) {
                Individual newInd = new Individual(nbrNurses, capacityNurse, depot, patients, travelTimes);
                ArrayList<Integer> routes = new ArrayList<Integer>(originalBestInd); // Shallow copy O(n)
                newInd.routes = routes;
                if (i > j) {
                    newInd.routes.remove(i);
                    newInd.routes.add(j, mutationNbr);
                } else if (i < j) {
                    newInd.routes.remove(i);
                    newInd.routes.add(j - 1, mutationNbr);
                }
                population.add(newInd);
            }
        }
        ArrayList<Double> populationgFitness = fitnessClass.getPenaltyFitness(population);
        return fitnessClass.bestFeasibleIndividual;
    }
}
