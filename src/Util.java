import java.util.ArrayList;
import java.util.HashMap;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors

public class Util {
  public int nbrNurses;
  public int capacityNurse;
  public Depot depot;
  public double[][] travelTimes;
  public HashMap<Integer, Patient> patients;

  public Util(int nbrNurses, int capacityNurse, Depot depot, HashMap<Integer, Patient> patients,
      double[][] travelTimes) {
    this.nbrNurses = nbrNurses;
    this.capacityNurse = capacityNurse;
    this.depot = depot;
    this.travelTimes = travelTimes;
    this.patients = patients;

  }

  public String getValidationFormat(Individual individual) {
    ArrayList<Integer> routes = individual.routes;
    String result = new String("[[");
    for (int i = 0; i < routes.size(); i++) {
      if (routes.get(i) == 0) {
        result += "], [";
      } else {
        if (i != routes.size() - 1) {
          if (routes.get(i + 1) != 0) {
            result += Integer.toString(routes.get(i)) + ", ";
          } else {
            result += Integer.toString(routes.get(i));
          }
        } else {
          result += Integer.toString(routes.get(i));
        }
      }
    }
    result += "]]";
    return result;
  }

  public void createFile(String routes, int instance) {
    try {
      File myObj = new File("routes.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    try {
      FileWriter myWriter = new FileWriter("routes.txt");
      myWriter.write(instance + "\n");
      myWriter.write(routes);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void showTime(ArrayList<Individual> population) {
    System.out.println("\n");
    ArrayList<Double> fitness = new ArrayList<Double>();
    double penaltyScale = 100;
    for (int i = 0; i < population.size(); i++) {

      Individual individual = population.get(i);
      ArrayList<Integer> routes = individual.routes;

      double travelDuration = 0;
      double penaltyCapacity = 0;
      double penaltyMissedCareTime = 0;
      int nurseCount = 1;

      double nurseClock = 0;
      double nurseUsage = 0;
      boolean newNurse = true;

      String routeString = "";

      for (int j = 0; j <= routes.size(); j++) {
        Patient patient;
        double travel;
        boolean toDeposit = j < routes.size() ? (routes.get(j) == 0 ? true : false) : false;
        boolean lastStop = j == routes.size() ? true : false;

        if (newNurse & toDeposit) { // Skip nurse
          System.out.println("Nurse " + nurseCount + " was not in use" + "\n");
          nurseCount++;
          continue;
        }

        else if (toDeposit) { // To Deposit
          travel = travelTimes[routes.get(j - 1)][routes.get(j)];
          nurseClock += travel;
          travelDuration += travel;
          routeString += " -> D(" + nurseClock + ")";

          System.out.println("Nurse " + nurseCount + " " + nurseClock + " " + nurseUsage + " " + routeString + "\n");
          routeString = "";
          nurseUsage = 0; // Reset for new nurse
          nurseClock = 0;
          newNurse = true;
          nurseCount++;

        }

        else if (lastStop) { // To Last Depot
          travel = travelTimes[routes.get(j - 1)][0];
          nurseClock += travel;
          travelDuration += travel;

          routeString += " -> D(" + nurseClock + ")";

          System.out.println("Nurse " + nurseCount + " " + nurseClock + " " + nurseUsage + " " + routeString + "\n");
          routeString = "";
        }

        else { // To Patient
          if (newNurse) { // From Deposit
            patient = this.patients.get(routes.get(j));
            travel = travelTimes[0][routes.get(j)];
            newNurse = false;
            routeString += "D(0) -> " + routes.get(j);

          } else { // From Patient
            patient = this.patients.get(routes.get(j));
            travel = travelTimes[routes.get(j - 1)][routes.get(j)];
            routeString += " -> " + routes.get(j);

          }

          nurseClock += travel;
          travelDuration += travel;

          if (nurseClock < patient.start_time) { // Wait
            nurseClock = patient.start_time;
          }

          double availableCareTime = patient.end_time - nurseClock;
          double restCareTime = availableCareTime - patient.care_time;

          routeString += "(" + nurseClock + "-" + (nurseClock + patient.care_time) + ")";
          routeString += "[" + patient.start_time + "-" + patient.end_time + "]";

          nurseUsage += patient.demand;
          nurseClock += patient.care_time;
        }
      }
    }
  }
}
