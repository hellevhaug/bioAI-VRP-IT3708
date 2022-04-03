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
    public ArrayList<Integer> routeIndices; // Indices of all the 0s. Not working because og insertion mutation

    public Individual(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }

    public int getTotPatCap(){
        int totPatCap = 0;
        for (int i = 1; i < this.patients.size()+1; i++){
            totPatCap += patients.get(i).demand;
        }
        return totPatCap;
    }

    public ArrayList<Integer> sortPatPri(){
        ArrayList<Integer> patPri = new ArrayList<Integer>();
            for (int i = 1; i <= this.patients.size(); i++){
                int patStartTime = this.patients.get(i).end_time;
                if (patPri.isEmpty()){
                    patPri.add(i);
                }
                else {
                    for (int j = 0; j < patPri.size(); j++){
                        int patStartTime1 = this.patients.get(patPri.get(j)).end_time;
                        if (patStartTime < patStartTime1) {
                            patPri.add(j, i);
                            break;
                        }
                        else if ((j+1) == patPri.size()){
                            patPri.add(j+1, i);
                            break;
                        }
                    }
                }
            }
        return patPri;
    }
    
    public void generateIndArray(JSONObject trainInstance, int activeNurses) {
        ArrayList<Integer> individual = new ArrayList<Integer>();
        ArrayList<Integer> routeIndices = new ArrayList<Integer>();

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
                routeIndices.add(individual.size()-1);
            }
        }
        if (!(patientIndices.size() == 0)) {
            throw new AssertionError("Not all patients are distributed, extend functionality!");
        }
        this.routes = individual;
        this.routeIndices = routeIndices;
    }
    public void generateGreedyIndArray(JSONObject trainInstance, int activeNurses){
        ArrayList<Integer> individual = new ArrayList<Integer>();
        ArrayList<Integer> routeIndices = new ArrayList<Integer>();
        ArrayList<Integer> patientIndices = this.sortPatPri();

        for (int i = 0; i < activeNurses; i++) {
            for (int j = 1; j <= 12; j++) {
                if (patientIndices.size()==0){
                    break;
                }
                int rando = (int) ((Math.random() * patientIndices.size()));
                individual.add(patientIndices.get(rando));
                patientIndices.remove(rando);
            }
                
            if (i < activeNurses - 1) {  // Depot divider between nurses
                individual.add(0);
                routeIndices.add(individual.size()-1);
            }
        }

        this.routes = individual;
        this.routeIndices = routeIndices;
    }

    public void generateGreedyIndArray2(JSONObject trainInstance, int activeNurses){
        HashMap<Integer, ArrayList<Integer>> individual = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> patPri = this.sortPatPri();

        for (int i = 1; i <= this.nbrNurses; i++){
            individual.put(i, new ArrayList<Integer>());
        }
        for (int i = 0; i < 4; i++){
            ArrayList<Integer> nurseIndices = new ArrayList<Integer>();
            for (int j = 1; j <= activeNurses; j++){
                nurseIndices.add(j);
            }
            for (int j = 0; j < activeNurses; j++){
                int ind = i*activeNurses + j;
                int rando = (int) (((Math.random() * nurseIndices.size())+1));
                individual.get(rando).add(patPri.get(ind));
                nurseIndices.remove(rando-1);
            }
        }
        ArrayList<Integer> individualFixed = new ArrayList<Integer>();
        for (int i = 1; i <= individual.size(); i++){
            if (i != 1) {
                individualFixed.add(0);
            }
            for (int j = 0; j < individual.get(i).size(); j++){
                individualFixed.add(individual.get(i).get(j));
            }
        }
        this.routes = individualFixed;
    }
}

