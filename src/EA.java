import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EA {
        public static void main(String args[]) {
                // Hyper-parameters
                int epochs = 200;
                int trainInstanceIndex = 1;
                int popSize = 1000;
                double pC = 0.5;
                double pM = 0.007;
                int lambda = 20; // 3 is more popular now a days

                // Load JSON
                TrainData data = new TrainData();
                JSONArray trainArray = data.getTrainArray();
                JSONObject trainInstance = (JSONObject) trainArray.get(trainInstanceIndex);

                // Get datastructuresgit
                int nbrNurses = Integer.parseInt(Long.toString((Long) trainInstance.get("nbr_nurses")));
                int capacityNurse = Integer.parseInt(Long.toString((Long) trainInstance.get("capacity_nurse")));
                //double benchmark = (double) trainInstance.get("benchmark");
                Depot depot = data.getDepot(trainInstance);
                HashMap<Integer, Patient> patients = data.getPatients(trainInstance);
                double[][] travelTimes = data.getTravelTime(trainInstance);

                Population populationClass = new Population(nbrNurses, capacityNurse, depot, patients, travelTimes);
                Fitness fitnessClass = new Fitness(nbrNurses, capacityNurse, depot, patients, travelTimes);
                Parent parentClass = new Parent(nbrNurses, capacityNurse, depot, patients, travelTimes);
                Offspring offspringClass = new Offspring(nbrNurses, capacityNurse, depot, patients, travelTimes);
                Survivor survivorClass = new Survivor(nbrNurses, capacityNurse, depot, patients, travelTimes);

                // Set up SGA
                ArrayList<Individual> population = populationClass.greedyGeneratePopArray(trainInstance, nbrNurses, popSize);

                for (int epoch = 1; epoch < epochs; epoch++) {
                        // Fitness of new Popuation
                        ArrayList<Double> populationgFitness = fitnessClass.getPenaltyFitness(population);
                        double popMaxFitVal = fitnessClass.prevMaxFitness;
                        System.out.println("Min Penalty " + fitnessClass.prevMinPenalty);
                        System.out.println("Min Fitness " + fitnessClass.bestFitness);
                        System.out.println("Epoch " + epoch);
                        ArrayList<Double> transFitness = fitnessClass.transformFitnessArray(populationgFitness,
                                        popMaxFitVal);

                        // Parent Selection
                        ArrayList<Individual> parents = parentClass.selectParentsProbabilistic(transFitness, population,
                                        popSize);
                        ArrayList<Double> parentTransFitness = parentClass.parentFitness;

                        // Offspring
                        ArrayList<Individual> offspring = offspringClass.createOffspring(parents, parentTransFitness,
                                        pC,
                                        pM, lambda);
                        ArrayList<Double> offspringFitness = fitnessClass.getPenaltyFitness(offspring); // This can
                                                                                                        // definitly
                                                                                                        // be optimized
                        double offMaxFitVal = fitnessClass.prevMaxFitness;
                        ArrayList<Double> offspringTransFitness = fitnessClass.transformFitnessArray(offspringFitness,
                                        offMaxFitVal);

                        // Survivor Selection
                        // (lambda, mu)-selection, based on offspring only (lambda > mu)
                        ArrayList<Individual> survivors = survivorClass.deterministicOffspringSelection(offspring,
                                        offspringTransFitness, popSize);
                        population = survivors;
                }
                ArrayList<Double> populationgFitness = fitnessClass.getPenaltyFitness(population);
                Validation validationClass = new Validation();
                if (fitnessClass.bestFitness < Math.pow(10, 10)) {
                        System.out.println("\n" + validationClass.getValidationFormat(fitnessClass.bestIndividual) + "\n");
                        System.out.println("Routes " + fitnessClass.bestIndividual.routes);
                }
        }
}