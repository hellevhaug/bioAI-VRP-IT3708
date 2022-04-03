import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TrainData {

  public JSONArray trainArray = readJSON();

  public JSONArray readJSON() {
    JSONArray train = new JSONArray();

    // JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();
    for (int i = 0; i < 13; i++) {
      try (FileReader reader = new FileReader(System.getProperty("user.dir") +
          "/src/train/" + "train_" + i + ".json")) {
        // Read JSON file
        Object obj = jsonParser.parse(reader);
        train.add(obj);

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return train;
  }

  public JSONArray getTrainArray() {
    return trainArray;
  }

  public double[][] getTravelTime(JSONObject trainInstance) {
    JSONArray jsonTravelTimes = (JSONArray) trainInstance.get("travel_times");
    double[][] travelTimes = new double[jsonTravelTimes.size()][((JSONArray) jsonTravelTimes.get(0)).size()];
    for (int i = 0; i < jsonTravelTimes.size(); i++) {
      JSONArray jsonArray = (JSONArray) jsonTravelTimes.get(i);
      for (int j = 0; j < jsonArray.size(); j++) {
        if (jsonArray.get(j) instanceof Long) {
          travelTimes[i][j] = ((Long) jsonArray.get(j)).doubleValue();
        } else {
          travelTimes[i][j] = (Double) jsonArray.get(j);
        }
      }
    }
    return travelTimes;
  }

  public HashMap<Integer, Patient> getPatients(JSONObject trainInstance) {
    JSONObject JSONpatients = (JSONObject) trainInstance.get("patients");
    HashMap<Integer, Patient> patients = new HashMap<Integer, Patient>();
    Iterator<String> keys = JSONpatients.keySet().iterator();

    while (keys.hasNext()) {
      String key = keys.next();
      if (JSONpatients.get(key) instanceof JSONObject) {
        JSONObject jsonPatient = (JSONObject) JSONpatients.get(key);
        Patient patient = new Patient(jsonPatient);
        patients.put(Integer. parseInt(key), patient); // Meaing patient "1" is in index = 0
      }
    }
    return patients;
  }

  public Depot getDepot(JSONObject trainInstance) {
    JSONObject jsonDepot = (JSONObject) trainInstance.get("depot");
    Depot depot = new Depot(jsonDepot);
    return depot;
  }
}
