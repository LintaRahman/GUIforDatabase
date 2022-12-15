//Requirement 5
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.text.ParseException;

public class GUI extends JFrame implements ActionListener {
    private final Model modelClass = new Model();
    private final JFrame frame = new JFrame();
    private TableModel model = createModel();
    private TableRowSorter sorter = getSorter();
    private JTable dataTable;
    private JButton searchButton;
    private JButton JSONButton;
    private JTextField searchBox;
    private JComboBox<String> filter;
    private JComboBox<String> sort;
    private JComboBox<String> chartChoices;

    GUI() {
        frame.setTitle("Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        collateGUI();
        frame.pack();
        frame.setResizable(false);
        frame.setSize(1230, 600);
        frame.setVisible(true);
    }

    //method holds all panels and tables added to frame
    public void collateGUI() {
        frame.add(SearchData());
        frame.add(displayFilterOptions());
        frame.add(createJSONFile());
        frame.add(displayChartOptions());
        frame.add(displayTable());
        frame.add(TablePanelDisplay());
        frame.add(displaySortOptions());
        frame.add(getTopBar());
    }

    public JPanel getTopBar() {
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(15, 102, 102));
        topBar.setBounds(0, 0, 1230, 40);
        return topBar;
    }

    //JPanel for JSON file writer button
    public JPanel createJSONFile() {
        JPanel JSONPanel = new JPanel();
        JSONPanel.setBackground(new Color(15, 102, 102));
        JSONPanel.setBounds(1050, 0, 170, 40);
        JSONButton = new JButton("Create JSON File");
        JSONButton.addActionListener(this);
        JSONPanel.add(JSONButton);
        return JSONPanel;
    }

    public JPanel displayFilterOptions() {
        JPanel displayCheckBoxes = new JPanel();
        displayCheckBoxes.setBackground(new Color(15, 102, 102));
        displayCheckBoxes.setBounds(350, 0, 200, 40);
        JLabel text = new JLabel();
        text.setForeground(Color.white);
        text.setText("Filter: ");

        String[] options = {"None", "Oldest Patient", "Youngest Patient", "Female Patients", "Male Patients", "Married Patients", "Single Patients"};//, "Prefix", "Marital", "Race", "Ethnicity", "Gender", "City", "State"
        filter = new JComboBox<>(options);
        filter.addActionListener(this);
        filter.setBackground(Color.white);

        displayCheckBoxes.add(text);
        displayCheckBoxes.add(filter);
        return displayCheckBoxes;
    }

    public JPanel displaySortOptions() {
        JPanel displayCheckBoxes = new JPanel();
        displayCheckBoxes.setBackground(new Color(15, 102, 102));
        displayCheckBoxes.setBounds(800, 0, 200, 40);
        JLabel SortText = new JLabel();
        SortText.setForeground(Color.white);
        SortText.setText("Sort by: ");
        String[] sortOptions = {"None", "Oldest Age", "Youngest Age", "ZIP"};
        sort = new JComboBox<>(sortOptions);
        sort.addActionListener(this);
        sort.setBackground(Color.white);
        displayCheckBoxes.add(SortText);
        displayCheckBoxes.add(sort);
        return displayCheckBoxes;
    }

    public JPanel displayChartOptions() {
        JPanel cPanel = new JPanel();
        cPanel.setBackground(new Color(15, 102, 102));
        cPanel.setBounds(550, 0, 260, 50);
        JLabel text = new JLabel();
        text.setForeground(Color.white);
        text.setText("Display Chart: ");
        String[] options = {"None", "Marital Status", "Race", "Ethnicity", "Gender", "Age Distribution"};
        chartChoices = new JComboBox<>(options);
        chartChoices.addActionListener(this);
        chartChoices.setBackground(Color.white);
        cPanel.add(text);
        cPanel.add(chartChoices);
        return cPanel;
    }

    //method creates panel with text box to search the dataframe
    public JPanel SearchData() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(15, 102, 102));
        searchPanel.setBounds(0, 0, 350, 40);
        JLabel search = new JLabel();
        search.setForeground(Color.white);
        search.setText("Search for: ");
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchBox = new JTextField();
        searchBox.setPreferredSize(new Dimension(165, 30));
        searchPanel.add(search);
        searchPanel.add(searchBox);
        searchPanel.add(searchButton);
        return searchPanel;
    }

    public JPanel TablePanelDisplay() {
        JPanel display = new JPanel();
        display.setBackground(new Color(15, 102, 102));
        display.setBounds(0, 40, 1220, 520);
        return display;
    }

    //method creates JTable to display dataframe
    public JScrollPane displayTable() {
        dataTable = new JTable(model);
        dataTable.setRowSorter(sorter);
        JScrollPane tablePanel = new JScrollPane(dataTable);
        tablePanel.setBackground(new Color(15, 102, 102));
        tablePanel.setPreferredSize(new Dimension(1200, 500));
        tablePanel.setBounds(10, 50, 1200, 500);
        return tablePanel;
    }

    //method supports creating JTable
    public TableModel createModel() {
        String[] columnNames = modelClass.sendColumnNames();
        String[][] reformatColumns = modelClass.GUIColumns();
        model = new DefaultTableModel(reformatColumns, columnNames);
        return model;
    }

    //method supports creating JTable, search, filter and sort function
    public TableRowSorter getSorter() {
        sorter = new TableRowSorter<>(model);
        return sorter;
    }

    //search method that filters rows with matched element
    public void search(String element, Integer colNum) {
        if (element.length() == 0) {
            sorter.setRowFilter(null);
        } else if (colNum == null){
            sorter.setRowFilter(RowFilter.regexFilter(element));
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(element, colNum));
        }
    }

    //method sorts all rows based on column via specified order
    public void sortByColumn(SortOrder order, int colNum) {
        //https://www.codejava.net/java-se/swing/6-techniques-for-sorting-jtable-you-should-know
        //used link to learn how JTables can be sorted via specific column
        dataTable.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(colNum, order));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    //methods to run combo box option
    public void runFilterChoices(Object filter) {
        if (filter == "None") {
            search("", null);
        } else if (filter == "Oldest Patient") {
            try {
                search(modelClass.findOldestPerson(), 1);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        } else if (filter == "Youngest Patient") {
            try {
                search(modelClass.findYoungestPerson(), 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (filter == "Female Patients") {
            search("F", 14);
        } else if (filter == "Male Patients") {
            search("M", 14);
        } else if (filter== "Married Patients") {
            search("M", 11);
        } else if (filter == "Single Patients") {
            search("S", 11);
        }
    }

    //methods to run chart display option
    public void runChartChoice(Object choice) {
        if (choice == "Marital Status") {
            displayChart(11);
        } else if (choice == "Race") {
            displayChart(12);
        } else if (choice == "Ethnicity") {
            displayChart(13);
        } else if (choice == "Gender") {
            displayChart(14);
        } else if (choice == "Age Distribution"){
            try {
                new GUIGraphs().createAndShowGui();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //methods to run sort option
    public void runSortChoice(Object choice){
        if (choice == "None") {
            search("", null);
        } else if (choice == "Oldest Age") {
            sortByColumn(SortOrder.ASCENDING, 1);
        } else if (choice == "Youngest Age") {
            sortByColumn(SortOrder.DESCENDING, 1);
        } else if (choice == "ZIP") {
            sortByColumn(SortOrder.ASCENDING, 19);
        }
    }

    //method supports runChoice to display charts
    public void displayChart(int colIndex) {
        EventQueue.invokeLater(() -> new GUICharts().createGUICharts(colIndex));
    }

    //method used to run action based on relevant button pressed
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            search(searchBox.getText(), null);
        } else if (e.getSource() == filter) {
            runFilterChoices(filter.getSelectedItem());
        } else if (e.getSource() == chartChoices) {
            runChartChoice(chartChoices.getSelectedItem());
        } else if (e.getSource() == sort) {
            runSortChoice(sort.getSelectedItem());
        } else if (e.getSource() == JSONButton) {
            modelClass.writeJSONFile();
        }
    }
}