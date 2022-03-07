import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class Fitness {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public HashMap<Integer, Patient> patients;
    public double prevMaxFitness;
    public double prevMinFitness;
    public double prevMinPenalty;
    public ArrayList<ArrayList<Double>> fitnessMatrix; // Keeps record of the fitness for each path
    public Individual bestIndividual;
    public double bestFitness;


    public Fitness(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients,
            double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
        this.bestFitness = Math.pow(10,10); 
    }

    // Naively checks route duration without any feasibility test
    public ArrayList<Double> getRegularFitness(ArrayList<Individual> population) {
        ArrayList<Double> fitness = new ArrayList<Double>();
        double maxVal = 0;
        double minVal = Math.pow(10, 10);
        for (int i = 0; i < population.size(); i++) {
            Individual individual = population.get(i);
            ArrayList<Integer> routes = individual.routes;
            double indFitness = 0;
            for (int j = 0; j < routes.size() - 1; j++) {
                indFitness += travelTimes[routes.get(j)][routes.get(j + 1)];

            }
            if (indFitness > maxVal) {
                maxVal = indFitness;
            }
            if (indFitness < minVal) {
                minVal = indFitness;
            }
            fitness.add(indFitness);
        }
        this.prevMaxFitness = maxVal;
        this.prevMinFitness = minVal;

        return fitness;
    }

    public ArrayList<Double> getPenaltyFitness(ArrayList<Individual> population) {
        ArrayList<Double> fitness = new ArrayList<Double>();
        double maxVal = 0;
        double minVal = Math.pow(10, 10);
        double minPenVal = Math.pow(10, 10);
        double penaltyScale = 100;
        for (int i = 0; i < population.size(); i++) {

            Individual individual = population.get(i);
            ArrayList<Integer> routes = individual.routes;

            double travelDuration = 0;
            double penaltyCapacity = 0;
            double penaltyMissedCareTime = 0;

            double nurseClock = 0;
            double nurseUsage = 0;
            boolean newNurse = true;

            for (int j = 0; j < routes.size(); j++) {
                Patient patient;
                double travel;
                boolean toDeposit = routes.get(j) == 0;
                int patientIndex =  routes.get(j);

                if (newNurse & toDeposit) { // Skip nurse
                    continue;
                }

                else if (toDeposit) { // To Deposit
                    travel = travelTimes[routes.get(j - 1)][routes.get(j)];
                    nurseClock += travel;
                    travelDuration += travel;

                    if (nurseClock > depot.return_time) { // To late
                        penaltyMissedCareTime += depot.return_time - nurseClock;
                    }
                    if (nurseUsage > capacityNurse) { // To much work for one nurse
                        penaltyCapacity += nurseUsage - capacityNurse;
                    }
                    nurseUsage = 0; // Reset for new nurse
                    nurseClock = 0;
                    newNurse = true;
                }

                else { // To Patient
                    if (newNurse) { // From Deposit
                        patient = this.patients.get(routes.get(j));
                        travel = travelTimes[0][routes.get(j)];
                        newNurse = false;
                    }
                    else { // From Patient
                        patient = this.patients.get(routes.get(j));
                        travel = travelTimes[routes.get(j - 1)][routes.get(j)];
                    }

                    nurseClock += travel;
                    travelDuration += travel;
                    if (nurseClock < patient.start_time) { // Wait
                        nurseClock = patient.start_time;
                    }

                    if (nurseClock > patient.end_time) { // Missed patient entirely
                        penaltyMissedCareTime += patient.care_time + (nurseClock - patient.end_time);
                    }

                    else { // Arrived in time window
                        double availableCareTime = patient.end_time - nurseClock;
                        double restCareTime = availableCareTime - patient.care_time;
                        if (restCareTime < 0) { // Penalty! Arrived too late
                            penaltyMissedCareTime += -restCareTime; // Care time missing
                            nurseClock = patient.end_time;
                            nurseUsage += availableCareTime;
                        } else { // Nailed it!
                            nurseUsage += patient.care_time;
                            nurseClock += patient.care_time;
                        }
                    }
                }
            }
            double penalty = (penaltyMissedCareTime + penaltyCapacity) * penaltyScale;
            double penaltyFitness = travelDuration + penalty;

            fitness.add(penaltyFitness);

            if (penaltyFitness > maxVal) {
                maxVal = penaltyFitness;
            }
            if (penalty == 0 & penaltyFitness < bestFitness) {
                this.bestFitness = penaltyFitness;
                this.bestIndividual = individual;
            }
            if (penalty < minPenVal) {
                minPenVal = penalty;
            }
        }

        this.prevMaxFitness = maxVal;
        this.prevMinFitness = minVal;
        this.prevMinPenalty = minPenVal;

        return fitness;
    }

    public ArrayList<Double> transformFitnessArray(ArrayList<Double> fitness, double maxValue) {
        ArrayList<Double> transformedFitness = new ArrayList<Double>();

        ListIterator<Double> iter = fitness.listIterator();
        while (iter.hasNext()) {
            Double value = iter.next();
            Double newValue = maxValue - value;
            transformedFitness.add(newValue);
        }
        return transformedFitness;
    }
}
