import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class Util {
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

  public void createFile(String routes) {
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
        myWriter.write(routes);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
  }

}
