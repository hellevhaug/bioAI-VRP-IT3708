import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;


public class Individual {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public HashMap<Integer, Patient> patients;

    public ArrayList<Integer> routes;


    public Individual(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }
    
    public void generateIndArray(JSONObject trainInstance, int activeNurses) {
        ArrayList<Integer> individual = new ArrayList<Integer>();
        if (!(this.patients.size() % (activeNurses) == 0)) {
            throw new AssertionError("Extend functionality for leftover patients");
        }
        double patientPerNurse = this.patients.size() / activeNurses;
        ArrayList<Integer> patientIndices = new ArrayList<Integer>();
        for (int i = 0; i < this.patients.size(); i++) {
            patientIndices.add(i+1);
        }
        for (int i = 0; i < activeNurses; i++) {
            for (int j = 1; j <= patientPerNurse; j++) {
                int rando = (int) ((Math.random() * patientIndices.size()));
                individual.add(patientIndices.get(rando));
                patientIndices.remove(rando);
                
            }
            if (i < activeNurses - 1) {  // Depot divider between nurses
                individual.add(0);
            }
        }
        if (!(patientIndices.size() == 0)) {
            throw new AssertionError("Not all patients are distributed, extend functionality!");
        }
        this.routes = individual;
    }
}
