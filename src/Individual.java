import java.util.ArrayList;

import org.json.simple.JSONObject;


public class Individual {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;

    public ArrayList<Integer> routes;


    public Individual(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }
    
    public void generateIndArray(JSONObject trainInstance, int activeNurses) {
        ArrayList<Integer> individual = new ArrayList<Integer>();
        if (!(this.patients.length % (activeNurses) == 0)) {
            throw new AssertionError("Extend functionality for leftover patients");
        }
        double patientPerNurse = this.patients.length / activeNurses;
        ArrayList<Integer> patientIndices = new ArrayList<Integer>();
        for (int i = 0; i < this.patients.length; i++) {
            patientIndices.add(i);
        }
        individual.add(0); // Depot start
        for (int i = 0; i < activeNurses; i++) {
            for (int j = 1; j < this.patients.length; j++) {
                if (j <= patientPerNurse) {
                    int rando = (int) ((Math.random() * patientIndices.size()));
                    individual.add(patientIndices.get(rando));
                    patientIndices.remove(rando);
                }
            }
            individual.add(0); // Depot end
        }
        if (!(patientIndices.size() == 0)) {
            throw new AssertionError("Not all patients are distributed, extend functionality!");
        }
        this.routes = individual;
    }
}
