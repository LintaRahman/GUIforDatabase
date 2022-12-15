//Requirement 1
import java.util.ArrayList;

public class Column {
    private final ArrayList<String> rows = new ArrayList<>();
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getSize(){
        return rows.size();
    }

    public String getRowValue(int position){
        return rows.get(position);
    }

    public void setRowValue(int position, String value){
        rows.set(position, value);
    }

    public void addRowValue(String value){
        rows.add(value);
    }
}
