//Requirement 2
import java.util.ArrayList;

public class DataFrame {
    private final ArrayList<Column> columnsList;

    public DataFrame() {
        this.columnsList = new ArrayList<>();
    }

    public void addColumn(Column newColumn){
        columnsList.add(newColumn);
    }

    public Column getColumn(String columnName){
        for (Column column : columnsList) {
            if (column.getName().equals(columnName)) {
                return column;
            }
        }
        return null;
    }

    public ArrayList<String> getColumnNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Column column: columnsList){
            names.add(column.getName());
        }
        return names;
    }

    public int getRowCount(Column name){
        return name.getSize();
    }

    public String getValue(Column ColumnName, int row){
        for (Column name : columnsList){
            if (name == ColumnName){
                return ColumnName.getRowValue(row);
            }
        }
        return null;
    }

    public void putValue(Column ColumnName, int row, String Value){
        for (Column name : columnsList){
            if (name == ColumnName){
                ColumnName.setRowValue(row, Value);
            }
        }
    }

    public void addValue(Column ColumnName, String Value){
        for (Column name : columnsList){
            if (name == ColumnName){
                ColumnName.addRowValue(Value);
            }
        }
    }
}
