//Requirement 3
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class DataLoader {
    private final File fileName = new File("patients1000.csv");

    public ArrayList<String> CSVReader() {
        String line;
        BufferedReader file;
        ArrayList<String> lines = new ArrayList<>();
        try {
            file = new BufferedReader(new FileReader(fileName));
            while ((line = file.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("File Not Found Exception");
        }catch (NoSuchElementException exception){
            System.out.println("File Elements are Invalid");
        }catch (IOException exception) {
            exception.printStackTrace();
        }
        return lines;
    }

    //method creates dataframe from lines read from the CSV file
    public DataFrame returnDataFrame(){
        ArrayList<String> lines = CSVReader();
        DataFrame logData = new DataFrame();
        ArrayList<Column> columnsNames = new ArrayList<>();
        //code retrieves first line of CSV file
        //first line used to create names of columns
        String[] names = lines.get(0).split(",");
        logData = setColumns(names, columnsNames, logData);
        //rest of values from CSV file stored in respective column
        logData = setColumnValues(lines, logData, columnsNames );
        return logData;
    }

    //method supports returnDataFrame to create dateFrame columns
    public DataFrame setColumns(String[] names, ArrayList<Column> columnsNames, DataFrame logData){
        for (String name: names){
            Column currentColumn = new Column(name);
            columnsNames.add(currentColumn);
            logData.addColumn(currentColumn);
        }
        return logData;
    }

    //method supports returnDataFrame to add dateFrame columns values
    public DataFrame setColumnValues(ArrayList<String> lines, DataFrame logData, ArrayList<Column> columnsNames){
        for (int i = 1; i < lines.size(); i++){ //for the number of lines in CSV
            String[] line = lines.get(i).split(","); //separate each line to a value in string array
            for (int j = 0; j < line.length; j++){ //for words in line
                Column currentColumn = columnsNames.get(j);
                logData.addValue(currentColumn, line[j]);
            }
        }
        return logData;
    }
}


