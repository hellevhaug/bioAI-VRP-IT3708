import org.json.simple.JSONObject;

public class Patient {
    public int care_time;
    public int demand;
    public int end_time;
    public int start_time;
    public int x_coord;
    public int y_coord;

    public Patient(JSONObject object){
        this.care_time = ((Long) object.get("care_time")).intValue();;
        this.demand = ((Long) object.get("demand")).intValue();
        this.end_time = ((Long) object.get("end_time")).intValue();
        this.start_time = ((Long) object.get("start_time")).intValue();
        this.x_coord = ((Long) object.get("x_coord")).intValue();
        this.y_coord = ((Long) object.get("y_coord")).intValue();
    }
}
