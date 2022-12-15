//Requirement 4
import java.awt.*;
import java.text.*;
import java.util.*;
import java.util.List;

public class Model {
    private final DataLoader load = new DataLoader();
    private final DataFrame data = load.returnDataFrame();
    private final ArrayList<String> names = data.getColumnNames();

    //method returns dataframe created from CSV file
    public DataFrame sendDataFrame() {
        return data;
    }

    //method creates a String Array fo column names
    public String[] sendColumnNames() {
        String[] tableColumnNames = new String[data.getColumnNames().size()];
        for (int i = 0; i < names.size(); i++) {
            tableColumnNames[i] = names.get(i);
        }
        return tableColumnNames;
    }

    //method supports GUIColumns method to retrieve Column data
    public String[][] getColumns() {
        String[] columnNames = sendColumnNames();
        String[][] column = new String[columnNames.length][data.getColumn(columnNames[0]).getSize()];
        for (int i = 0; i < columnNames.length; i++) {
            Column currentColumn = data.getColumn(columnNames[i]);
            for (int row = 0; row < currentColumn.getSize(); row++) {
                String rowValue = currentColumn.getRowValue(row);
                column[i][row] = rowValue;
            }
        }
        return column;
    }

    //method for restructuring dataframe before displaying pn GUI JTable
    public String[][] GUIColumns(){
        String[][] data = getColumns();
        String[][] reformatColumns = new String[data[0].length][data.length];
        for (int i = 0; i < reformatColumns.length; i++){
            for (int j = 0; j < data.length; j++){
                reformatColumns[i][j] = data[j][i];
            }
        }
        return reformatColumns;
    }

    //Methods for GUI Filter Function
    //method finds oldest person based on Date of Birth
    public String findOldestPerson() throws ParseException{
        String name = sendColumnNames()[1];
        Column birthDates = sendDataFrame().getColumn(name);
        Date oldest = null;
        for (int i = 0; i < birthDates.getSize(); i++){
            String birthDate = data.getValue(birthDates, i);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
            if (oldest == null || oldest.after(date)){
                oldest = date;
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(oldest);
    }

    //method finds youngest person based on Date of Birth
    public String findYoungestPerson() throws ParseException{
        String name = sendColumnNames()[1];
        Column birthDates = sendDataFrame().getColumn(name);
        Date youngest = null;
        for (int i = 0; i < birthDates.getSize(); i++){
            String birthDate = data.getValue(birthDates, i);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
            if (youngest == null || youngest.before(date)){
                youngest = date;
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(youngest);
    }

    //Requirement 8
    //method writes JSONFile from data from data from DataFrame
    public void writeJSONFile(){
        new JSONWriter().createJSONFile();
    }

    //method creates Hashmap to store data to display charts
    public HashMap<String, Integer> returnChartValues(int colIndex){
        //Hashmap used to store kay and record occurrences of that key
        HashMap<String, Integer> chartValues = new HashMap<>();
        ArrayList<String> colNames = data.getColumnNames();
        Column column = data.getColumn(colNames.get(colIndex));
        for (int i = 0; i < column.getSize(); i++){
            String rowValues = column.getRowValue(i);
            evaluateRowValue(chartValues, rowValues);
        }
        return chartValues;
    }

    //method supports returnChartValues method to create a Hashmap
    public void evaluateRowValue(HashMap<String, Integer> values, String rowVal){
        if (rowVal.equals("")){
            rowVal = "Not recorded";
        }
        if (values.isEmpty()){
            values.put(rowVal, 1);
        } else {
            if (values.containsKey(rowVal)){
                values.put(rowVal, values.get(rowVal) + 1);
            } else {
                values.put(rowVal, 1);
            }
        }
    }

    //Methods used in GUIGraphs
    //method creates list of dates for GUI Age Distribution Graph
    public int[] listOfDates() throws ParseException {
        Column birthDates = data.getColumn(sendColumnNames()[1]);
        int colSize = birthDates.getSize();
        int[] dates = new int[colSize];
        for (int i = 0; i < colSize; i++){
            String strDate = data.getValue(birthDates, i);
            int date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate).getYear();
            dates[i] = date;
        }
        Arrays.sort(dates);
        return dates;
    }

    //method creates a Hashmap counting the number of records held per year
    public HashMap<Integer, Integer> datesForGraph() throws ParseException {
        HashMap<Integer, Integer> graphVal = new HashMap<>();
        int[] dates = listOfDates();
        for (int date: dates){
            if (graphVal.isEmpty()){
                graphVal.put(date, 1);
            } else {
                if (graphVal.containsKey(date)){
                    graphVal.put(date, graphVal.get(date) + 1);
                } else {
                    graphVal.put(date, 1);
                }
            }
        }
        return graphVal;
    }

    //method converts Hashmap values to a list of Points to display on graph
    public List<Point> pointsForGraph() throws ParseException {
        List<Point> graphVal = new ArrayList<>();
        HashMap<Integer, Integer> graphMap = datesForGraph();
        for (Integer key: graphMap.keySet()){
            graphVal.add(new Point(key + 1900, graphMap.get(key)));
        }
        return graphVal;
    }

    //method returns minimum value for the Y axis
    public int minYPoint() throws ParseException {
        HashMap<Integer, Integer> graphMap = datesForGraph();
        int lowY = Integer.MAX_VALUE;
        for (Integer key: graphMap.keySet()){
            if (graphMap.get(key) < lowY ){
                lowY = graphMap.get(key);
            }
        }
        return lowY;
    }

    //method returns maximum y value for the Y axis
    public int maxYPoint() throws ParseException {
        HashMap<Integer, Integer> graphMap = datesForGraph();
        int highY = 0;
        for (Integer key: graphMap.keySet()){
            if (graphMap.get(key) > highY){
                highY = graphMap.get(key);
            }
        }
        return highY;
    }
}
