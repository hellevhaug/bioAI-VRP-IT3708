import org.json.simple.JSONObject;

public class Depot {
    public int return_time;
    public int x_coord;
    public int y_coord;

    public Depot(JSONObject object){
        this.return_time = ((Long) object.get("return_time")).intValue();;
        this.x_coord = ((Long) object.get("x_coord")).intValue();
        this.y_coord = ((Long) object.get("y_coord")).intValue();
    }
}
