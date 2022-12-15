//Requirement 8
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JSONReader {
    private DataFrame newJSONDataFrame = new DataFrame();

    public void readJSONFile(String name){
        try {
            File file = new File(name);
            Scanner readFile = new Scanner(file);
            ArrayList<String> dataList = new ArrayList<>();
            while (readFile.hasNextLine()) {
                //each JSON file line stored as a String in ArrayList
                dataList.add(readFile.nextLine());
            }
            newJSONDataFrame = createJSONDataFrame(dataList);
            readFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (NoSuchElementException exception){
            System.out.println("File Elements are Invalid");
        }
    }

    //method creates dataframe from data in JSON file
    public DataFrame createJSONDataFrame(ArrayList<String> data){
        data.set(0, data.get(0).substring(1));
        for (String d : data) {
            String[] lines = d.split(":");
            //substring removes speech marks
            lines[0] = lines[0].substring(1, lines[0].length() - 1);
            Column newCol = new Column(lines[0]);
            newJSONDataFrame.addColumn(newCol);
            lines[1] = lines[1].substring(1, lines[1].length() - 2);
            setRowValues(newCol, lines);
        }
        return newJSONDataFrame;
    }

    //method supports createJSONDataFrame in storing values in data frame
    public void setRowValues(Column newColumn, String[] fileLines){
        String[] colItems = fileLines[1].split(",");
        for (int row = 0; row < colItems.length; row++){
            String rowVal = colItems[row];
            //substring removes speech marks
            colItems[row] = rowVal.substring(1, rowVal.length() - 1);
            newJSONDataFrame.addValue(newColumn, colItems[row]);
        }
    }

    public static void main(String[] args) {
        new JSONReader().readJSONFile("JSONFile.json");
    }
}
