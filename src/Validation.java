import java.util.ArrayList;

public class Validation {
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
}
