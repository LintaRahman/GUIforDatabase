//Requirement 8
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONWriter {
    private final DataFrame frame = new Model().sendDataFrame();

    //method creates a new JSON file
    public void createJSONFile(){
        try {
            File jsonFile = new File("JSONFile.json");
            FileWriter writer = new FileWriter(jsonFile);
            StringBuilder line = new StringBuilder();
            line.append("{");
            line = appendNameValuePairs(line);
            writer.write(String.valueOf(line));
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //method supports createJSONFile to setup the name colon value structure
    public StringBuilder appendNameValuePairs(StringBuilder lines){
        ArrayList<String> colNames = frame.getColumnNames();
        for (int i = 0; i < colNames.size(); i++) {
            String columnName = colNames.get(i);
            Column column = frame.getColumn(columnName);
            lines.append('"');
            lines.append(columnName);
            lines.append('"');
            lines.append(":[");
            lines = appendArrayElements(column, lines);
            if (i == colNames.size() - 1) {
                lines.append("]}\n");
            } else {
                lines.append("],\n");
            }
        }
        return lines;
    }

    //method supports appendNameValuePairs to add values to the array
    public StringBuilder appendArrayElements(Column col, StringBuilder lines){
        for (int row = 0; row < frame.getRowCount(col); row++) {
            if (row != 0){
                lines.append(',');
            }
            lines.append('"');
            String rowValue = col.getRowValue(row);
            lines.append(rowValue);
            lines.append('"');
        }
        return lines;
    }
}
