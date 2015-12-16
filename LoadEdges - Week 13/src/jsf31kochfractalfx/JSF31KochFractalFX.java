/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import calculate.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 */
public class JSF31KochFractalFX extends Application {

    // Zoom and drag
    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;
    private double lastDragX = 0.0;
    private double lastDragY = 0.0;

    // Koch manager
    // TO DO: Create class KochManager in package calculate
    // Current level of Koch fractal
    private int currentLevel = 1;

    // Labels for level, nr edges, calculation time, and drawing time
    private Label labelLevel;
    private Label labelNrEdges;
    private Label labelNrEdgesText;
    private Label labelCalc;
    private Label labelCalcText;
    private Label labelDraw;
    private Label labelDrawText;

    // Koch panel and its size
    private Canvas kochPanel;
    private final int kpWidth = 500;
    private final int kpHeight = 500;

    @Override
    public void start(Stage primaryStage) {

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);
        // Drawing panel for Koch fractal
        kochPanel = new Canvas(kpWidth, kpHeight);
        grid.add(kochPanel, 0, 3, 25, 1);

        // Labels to present number of edges for Koch fractal
        labelNrEdges = new Label("Nr edges:");
        labelNrEdgesText = new Label();
        grid.add(labelNrEdges, 0, 0, 4, 1);
        grid.add(labelNrEdgesText, 3, 0, 22, 1);

        // Labels to present time of calculation for Koch fractal
        labelCalc = new Label("Calculating:");
        labelCalcText = new Label();
        grid.add(labelCalc, 0, 1, 4, 1);
        grid.add(labelCalcText, 3, 1, 22, 1);

        // Labels to present time of drawing for Koch fractal
        labelDraw = new Label("Drawing:");
        labelDrawText = new Label();
        grid.add(labelDraw, 0, 2, 4, 1);
        grid.add(labelDrawText, 3, 2, 22, 1);

        // Label to present current level of Koch fractal
        labelLevel = new Label("Level: " + currentLevel);
        grid.add(labelLevel, 0, 6);

        // Button to increase level of Koch fractal
        Button buttonByteBuffer = new Button();
        buttonByteBuffer.setText("Draw Buffered Byte File");
        buttonByteBuffer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawFromBByte();
            }
        });
        grid.add(buttonByteBuffer, 3, 6);

        // Button to decrease level of Koch fractal
        Button buttonByte = new Button();
        buttonByte.setText("Draw Byte File");
        buttonByte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawFromByte();
            }
        });
        grid.add(buttonByte, 5, 6);

        // Button to fit Koch fractal in Koch panel
        Button buttonTextBuffer = new Button();
        buttonTextBuffer.setText("Draw Buffered Text File");
        buttonTextBuffer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawFromBText();
            }
        });
        grid.add(buttonTextBuffer, 7, 6);

        Button buttonText = new Button();
        buttonText.setText("Draw Text File");
        buttonText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawFromText();
            }
        });
        grid.add(buttonText, 9, 6);

        // Add mouse clicked event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        kochPanelMouseClicked(event);
                    }
                });

        // Add mouse pressed event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        kochPanelMousePressed(event);
                    }
                });

        // Add mouse dragged event to Koch panel
        kochPanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kochPanelMouseDragged(event);
            }
        });

        // Create Koch manager and set initial level
        resetZoom();

        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, kpWidth + 150, kpHeight + 170);
        root.getChildren().add(grid);

        // Define title and assign the scene for main window
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void clearKochPanel() {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0, 0.0, kpWidth, kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0, 0.0, kpWidth, kpHeight);
    }

    public void drawEdge(Edge e) {
        // Graphics
        GraphicsContext gc = kochPanel.getGraphicsContext2D();

        // Adjust edge for zoom and drag
        Edge e1 = edgeAfterZoomAndDrag(e);

        // Set line color
        gc.setStroke(e1.color);

        // Set line width depending on level
        if (currentLevel <= 3) {
            gc.setLineWidth(2.0);
        } else if (currentLevel <= 5) {
            gc.setLineWidth(1.5);
        } else {
            gc.setLineWidth(1.0);
        }

        // Draw line
        gc.strokeLine(e1.X1, e1.Y1, e1.X2, e1.Y2);
    }

    public void setTextNrEdges(String text) {
        labelNrEdgesText.setText(text);
    }

    public void setTextCalc(String text) {
        labelCalcText.setText(text);
    }

    public void setTextDraw(String text) {
        labelDrawText.setText(text);
    }

    public void requestDrawEdges(final ArrayList<Edge> edges) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Edge e : edges) {
                    drawEdge(e);
                }
            }
        });
    }

    private void increaseLevelButtonActionPerformed(ActionEvent event) {
        if (currentLevel < 12) {
            // resetZoom();
            currentLevel++;
            labelLevel.setText("Level: " + currentLevel);
            //kochManager.changeLevel(currentLevel);
        }
    }

    private void decreaseLevelButtonActionPerformed(ActionEvent event) {
        if (currentLevel > 1) {
            // resetZoom();
            currentLevel--;
            labelLevel.setText("Level: " + currentLevel);
            //kochManager.changeLevel(currentLevel);
        }
    }

    private void fitFractalButtonActionPerformed(ActionEvent event) {
        resetZoom();
        //kochManager.drawEdges();
    }

    private boolean drawFromBByte() {
        clearKochPanel();
        TimeStamp ts = new TimeStamp();
        DataInputStream dis;
        ArrayList<Edge> alEdges = new ArrayList<>();
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream("edgesNoColor7.byte")));
            // lees velden van Edges
            int level = dis.readInt();
            currentLevel = level;
            labelLevel.setText("Level: " + currentLevel);
            ts.setBegin("Start Buffered Byte Reading, Level: " + level);
            while (true) {
                double x1 = dis.readDouble();
                double y1 = dis.readDouble();
                double x2 = dis.readDouble();
                double y2 = dis.readDouble();
                //double hue = dis.readDouble();
                Edge edge = new Edge(x1, y1, x2, y2, Color.hsb(150, 1.0, 1.0));
                alEdges.add(edge);
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
        }
        ts.setEnd("End Buffered Byte Reading");
        System.out.println(ts.toString());
        requestDrawEdges(alEdges);
        return false;
    }

    private boolean drawFromByte() {
        clearKochPanel();
        DataInputStream dis;
        TimeStamp ts = new TimeStamp();
        ArrayList<Edge> alEdges = new ArrayList<>();
        try {
            dis = new DataInputStream(new FileInputStream("edgesNoColor7.byte"));
            // lees velden van Edges
            int level = dis.readInt();
            currentLevel = level;
            labelLevel.setText("Level: " + currentLevel);
            ts.setBegin("Start Byte Reading, Level: " + level);
            while (true) {
                double x1 = dis.readDouble();
                double y1 = dis.readDouble();
                double x2 = dis.readDouble();
                double y2 = dis.readDouble();
                //double hue = dis.readDouble();
                Edge edge = new Edge(x1, y1, x2, y2, Color.hsb(0, 1.0, 1.0));
                alEdges.add(edge);
            }
            

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
        }
        ts.setEnd("End Byte Reading");
        System.out.println(ts.toString());
        requestDrawEdges(alEdges);
        return false;
    }

    private boolean drawFromBText() {
        clearKochPanel();
        try {
            BufferedReader br = null;
            br = new BufferedReader(new FileReader("edgesColor7.txt"));
            // weinig methoden om te lezen, gebruik Scanner klasse hiervoor
            Scanner inputScanner = new Scanner(br);

            // parse Strings naar de juiste waarden
            String regel = inputScanner.nextLine();
            int level = Integer.parseInt(regel);
            currentLevel = level;
            labelLevel.setText("Level: " + currentLevel);
            ArrayList<Edge> edges = new ArrayList<>();
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start Buffered Text Reading, Level: " + level);
            while (inputScanner.hasNext()) {            
                regel = inputScanner.nextLine();
                // split regel in velden, gescheiden door , 
                String[] velden = regel.split(",");
                double x1 = Double.parseDouble(velden[0]);
                double y1 = Double.parseDouble(velden[1]);
                double x2 = Double.parseDouble(velden[2]);
                double y2 = Double.parseDouble(velden[3]);
                double hue = Double.parseDouble(velden[4]);
                Edge edge = new Edge(x1, y1, x2, y2, Color.hsb(hue, 1.0, 1.0));
                edges.add(edge);
            }
            ts.setEnd("End Buffered Text Reading");
            System.out.println(ts.toString());
            requestDrawEdges(edges);
            br.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean drawFromText() {
        clearKochPanel();
        try {
            FileReader fr = new FileReader("edgesColor7.txt");
            // weinig methoden om te lezen, gebruik Scanner klasse hiervoor
            Scanner inputScanner = new Scanner(fr);

            // parse Strings naar de juiste waarden
            String regel = inputScanner.nextLine();
            int level = Integer.parseInt(regel);
            currentLevel = level;
            labelLevel.setText("Level: " + currentLevel);
            ArrayList<Edge> edges = new ArrayList<>();
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start Text Reading, Level: " + level);
            while (inputScanner.hasNext()) {
                
                regel = inputScanner.nextLine();
                // split regel in velden, gescheiden door , 
                String[] velden = regel.split(",");
                double x1 = Double.parseDouble(velden[0]);
                double y1 = Double.parseDouble(velden[1]);
                double x2 = Double.parseDouble(velden[2]);
                double y2 = Double.parseDouble(velden[3]);
                double hue = Double.parseDouble(velden[4]);
                Edge edge = new Edge(x1, y1, x2, y2, Color.hsb(hue, 1.0, 1.0));
                edges.add(edge);
            }
            ts.setEnd("End Text Reading");
            requestDrawEdges(edges);
            System.out.println(ts.toString());
            fr.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

    private void kochPanelMouseClicked(MouseEvent event) {
        if (Math.abs(event.getX() - startPressedX) < 1.0
                && Math.abs(event.getY() - startPressedY) < 1.0) {
            double originalPointClickedX = (event.getX() - zoomTranslateX) / zoom;
            double originalPointClickedY = (event.getY() - zoomTranslateY) / zoom;
            if (event.getButton() == MouseButton.PRIMARY) {
                zoom *= 2.0;
            } else if (event.getButton() == MouseButton.SECONDARY) {
                zoom /= 2.0;
            }
            zoomTranslateX = (int) (event.getX() - originalPointClickedX * zoom);
            zoomTranslateY = (int) (event.getY() - originalPointClickedY * zoom);
            //kochManager.drawEdges();
        }
    }

    private void kochPanelMouseDragged(MouseEvent event) {
        zoomTranslateX = zoomTranslateX + event.getX() - lastDragX;
        zoomTranslateY = zoomTranslateY + event.getY() - lastDragY;
        lastDragX = event.getX();
        lastDragY = event.getY();
        //kochManager.drawEdges();
    }

    private void kochPanelMousePressed(MouseEvent event) {
        startPressedX = event.getX();
        startPressedY = event.getY();
        lastDragX = event.getX();
        lastDragY = event.getY();
    }

    private void resetZoom() {
        int kpSize = Math.min(kpWidth, kpHeight);
        zoom = kpSize;
        zoomTranslateX = (kpWidth - kpSize) / 2.0;
        zoomTranslateY = (kpHeight - kpSize) / 2.0;
    }

    private Edge edgeAfterZoomAndDrag(Edge e) {
        return new Edge(
                e.X1 * zoom + zoomTranslateX,
                e.Y1 * zoom + zoomTranslateY,
                e.X2 * zoom + zoomTranslateX,
                e.Y2 * zoom + zoomTranslateY,
                e.color);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
