// Requirement 9
// used layout of methods as guides
// to write own methods for GUIGraphs class:  setupXaxisGrid, setupYaxisGrid and paintComponent
// https://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GUIGraphs extends JPanel {
    private final Model modelClass = new Model();
    private final Color lineColor = new Color(15,102,102);
    private final Color gridColor = new Color(200, 200, 200, 200);
    private final int width = 1000;
    private final int height = 400;
    private final List<Point> points;

    public GUIGraphs() throws ParseException {
        this.points = modelClass.pointsForGraph();
    }

    //method displays main graph with all elements
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D lineGraph = (Graphics2D) g;
        lineGraph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int xScale = (width - 75) / (points.size() - 1);
        int yScale = 0;
        try {
            yScale = (height - 75) / (getMaxY() - getMinY());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Point> graphPoints = returnListOfPoints(xScale, yScale);
        drawBackground(lineGraph);
        drawXandYAxis(lineGraph);
        drawGraphLines(lineGraph, graphPoints);
    }

    public void drawXandYAxis(Graphics2D graph){
        try {
            setupYAxisGrid(graph);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setupXAxisGrid(graph);
        // create x and y axes
        graph.drawLine(50, height - 50, 50, 25);
        graph.drawLine(50, height - 50, width - 25, height - 50);
    }

    public void drawBackground(Graphics2D graph){
        // draw white background
        graph.setColor(Color.WHITE);
        graph.fillRect(50, 25, width - 75, height - 75);
        graph.setColor(Color.BLACK);
    }

    public void drawGraphLines(Graphics2D graph, List<Point> gPoints){
        graph.setColor(lineColor);
        for (int i = 0; i < gPoints.size() - 1; i++) {
            int x1 = (int) gPoints.get(i).getX();
            int y1 = (int) gPoints.get(i).getY();
            int x2 = (int) gPoints.get(i + 1).getX();
            int y2 = (int) gPoints.get(i + 1).getY();
            graph.drawLine(x1, y1, x2, y2);
        }
    }

    public List<Point> returnListOfPoints(int XScale, int YScale){
        List<Point> gPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int x1 = (i * XScale + 50);
            int y1 = 0;
            try {
                y1 = (int) (YScale * (getMaxY() - points.get(i).getY()) + 25);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            gPoints.add(new Point(x1, y1));
        }
        return gPoints;
    }

    public void setupYAxisGrid(Graphics2D graph) throws ParseException {
        //loop creates hatch marks and grid lines for y axis.
        int numberYDiv = 10;
        for (int i = 0; i < numberYDiv + 1; i++) {
            int x0 = 50;
            int x1 = 54;
            int y0 = height - ((i * (height - 75)) / numberYDiv + 50);
            if (points.size() > 0) {
                graph.setColor(gridColor);
                graph.drawLine(55, y0, width - 25, y0);
                graph.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinY() + (getMaxY() - getMinY()) * ((i * 1.0) / numberYDiv)) * 100)) / 100.0 + "";
                FontMetrics metrics = graph.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                graph.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            graph.drawLine(x0, y0, x1, y0);
        }
    }

    public void setupXAxisGrid(Graphics2D graph){
        //loop creates hatch marks and grid lines for x axis
        for (int i = 0; i< points.size(); i++){
            if (points.size() > 1) {
                int x0 = i * (width - 75) / (points.size() - 1) + 50;
                int y0 = height - 50;
                int y1 = y0 - 4;
                if ((i % ((int) ((points.size() / 20.0)) + 1)) == 0) {
                    graph.setColor(gridColor);
                    graph.drawLine(x0, height - 55, x0, 25);
                    graph.setColor(Color.BLACK);
                    String xLabel = (int) points.get(i).getX() + "";
                    FontMetrics metrics = graph.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    graph.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight());
                }
                graph.drawLine(x0, y0, x0, y1);
            }
        }
    }

    //methods returns min y axis value
    private int getMinY() throws ParseException {
        return modelClass.minYPoint();
    }

    //methods returns max y axis value
    private int getMaxY() throws ParseException{
        return modelClass.maxYPoint();
    }

    //graph title JLabel
    public JLabel graphLabel(){
        JLabel title = new JLabel();
        title.setText("Number of records held per year");
        title.setForeground(Color.white);
        return title;
    }

    //method creates GUI graph by collating everything onto JFrame
    public static void createAndShowGui() throws ParseException {
        GUIGraphs mainPanel = new GUIGraphs();
        mainPanel.setPreferredSize(new Dimension(1000, 400));
        mainPanel.setBackground(new Color(15,102,102));
        mainPanel.add(mainPanel.graphLabel());
        JFrame frame = new JFrame("Birthdate Distribution Graph");
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}