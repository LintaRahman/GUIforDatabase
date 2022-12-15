// Requirement 9
//used classes from link below as guides to create classes ChartBar and BarColour
//https://stackoverflow.com/questions/29708147/custom-graph-java-swing
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class GUICharts extends JPanel {
    private JPanel barPanel;
    private final JPanel bLabels;
    private final Color colour = new Color(15,102,102);
    private final List<GUICharts.ChartBar> bars = new ArrayList<>();

    GUICharts(){
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        bLabels = new JPanel(new GridLayout(1, 0, 10, 0));
        bLabels.setBorder(new EmptyBorder(5, 10, 0, 10));
        add(getBarPanel(), BorderLayout.CENTER);
        add(bLabels, BorderLayout.PAGE_END);
    }

    //display title of bar chart
    private JLabel getChartLabel(String[] columnName, int colNum){
        JLabel chartLabel = new JLabel();
        chartLabel.setText(columnName[colNum]);
        chartLabel.setHorizontalTextPosition(JLabel.CENTER);
        chartLabel.setHorizontalAlignment(JLabel.CENTER);
        chartLabel.setVerticalTextPosition(JLabel.TOP);
        chartLabel.setVerticalAlignment(JLabel.BOTTOM);
        return chartLabel;
    }

    //method creates borderline frame for bar chart
    private JPanel getBarPanel(){
        barPanel = new JPanel(new GridLayout(1, 0, 10, 0));
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder(compound);
        return barPanel;
    }

    public void setupJFrame(JPanel jPanel){
        JFrame frame = new JFrame("Data Charts");
        frame.add(jPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void addColumnBar(String label, int value, Color colour) {
        GUICharts.ChartBar bar = new GUICharts.ChartBar(label, value, colour);
        bars.add(bar);
    }

    //method helps collate all bars onto the chart
    public void layoutChart(){
        barPanel.removeAll();
        bLabels.removeAll();

        int maxValue = 0;

        for (GUICharts.ChartBar bar : bars)
            maxValue = Math.max(maxValue, bar.getValue());

        setupChartBars(maxValue);
    }

    //method supports layoutChart to create bars per chart
    public void setupChartBars(int MaxVal){
        for (GUICharts.ChartBar bar : bars) {
            JLabel label = barLabels(bar);
            int height = 200;
            int barHeight = (bar.getValue() * height) / MaxVal;
            Icon icon = new GUICharts.BarColour(bar.getColor(), 35, barHeight);
            label.setIcon(icon);
            barPanel.add(label);

            JLabel barLabel = new JLabel(bar.getLabel());
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            bLabels.add(barLabel);
        }
    }

    //method displays labels per bar in chart
    public JLabel barLabels(GUICharts.ChartBar bar){
        JLabel label = new JLabel(bar.getValue() + "");
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setVerticalAlignment(JLabel.BOTTOM);
        return label;
    }

    //new class ChartBar helps create new bars per chart
    private class ChartBar {
        private final String label;
        private final int value;
        private final Color color;

        public ChartBar(String label, int value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public int getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }

    //class BarColour helps to draw bars for the bar chart implementing icon
    private class BarColour implements Icon {
        private final Color colour;
        private final int width;
        private final int height;

        public BarColour(Color color, int width, int height) {
            this.colour = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(colour);
            int shade = 3;
            g.fillRect(x, y, width - shade, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shade, y + shade, shade, height - shade);
        }
    }

    //method collates all GUIChart components
    //uses data from Hashtable to display bar chart
    public void createGUICharts(int colNum) {
        GUICharts panel = new GUICharts();
        HashMap<String, Integer> val = new Model().returnChartValues(colNum);
        for (String key : val.keySet()){
            int i = val.get(key);
            panel.addColumnBar(key, i, colour);
        }
        panel.setBackground(colour);
        String[] colNames = new Model().sendColumnNames();
        JLabel cLabel = getChartLabel(colNames, colNum);
        cLabel.setForeground(Color.white);
        panel.add(cLabel, BorderLayout.PAGE_START);
        panel.layoutChart();
        setupJFrame(panel);
    }
}