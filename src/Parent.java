import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler;

public class Parent {
    public int nbrNurses;
    public int capacityNurse;
    public Depot depot;
    public double[][] travelTimes;
    public Patient[] patients;
    public ArrayList<Double> parentFitness;

    public Parent(int nbrNurses, int capacityNurse, Depot depot, Patient[] patients, double[][] travelTimes) {
        this.nbrNurses = nbrNurses;
        this.capacityNurse = capacityNurse;
        this.depot = depot;
        this.travelTimes = travelTimes;
        this.patients = patients;
    }
    // fetches elements in O(1), 
    // unlike what you mostly find on the internet or on StackOverflow, where naive implementations run in O(n) or O(log(n));
    ArrayList<ArrayList<Integer>>  selectParentsProbabilistic(ArrayList<Double> fitness, ArrayList<ArrayList<Integer>> population, int nbrParents){
        Random random = new Random();
        ArrayList<ArrayList<Integer>> parents = new ArrayList<ArrayList<Integer>>();
        ArrayList<Double> parentFitness = new ArrayList<Double>();

        RandomSelector<ArrayList<Integer>> selector = RandomSelector.weighted(population, fitness);
        for(int i = 0; i < population.size(); i++){
            ArrayList<Integer> drop = selector.next(random);
            int index = selector.currentIndex;
            parents.add(drop);
            parentFitness.add(fitness.get(index));
        }
        this.parentFitness = parentFitness;
        return parents;
    }
}
