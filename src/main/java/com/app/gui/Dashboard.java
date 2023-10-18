package com.app.gui;

import com.app.data.RobotData;
import com.app.util.FRCConfig;

import edu.wpi.first.networktables.ValueEventData;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.GaugeDesign;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.skins.ModernSkin;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Dashboard {
    private RobotData robotData;
    private TextArea textArea = new TextArea();
    private VBox smartDashboardVBox = new VBox();
    private VBox checkBoxVBox = new VBox();  // Class-level variable
    private Map<String, CheckBox> createdCheckBoxes = new HashMap<>();
    private Map<String, Gauge> gauges = new HashMap<>();

     public double Test;

    public void init(Stage primaryStage) {
        // Initialize RobotData
        robotData = new RobotData("10.20.28.2", 1735);
     
   
     
     
        // Initialize UI
        BorderPane root = new BorderPane();
        ScrollPane checkBoxScrollPane = new ScrollPane(checkBoxVBox);

        // Create SplitPane and add TextArea and VBox for gauges
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(textArea, smartDashboardVBox);

        // Add to root layout
        root.setLeft(checkBoxScrollPane);
        root.setCenter(splitPane);

        // Stage setup
        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(new Scene(root, 1900, 1060, Color.BLACK));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Map<String, Object> data = robotData.fetchData();
            Platform.runLater(() -> {
                updateCheckBoxes(data);
                updateSmartDashboard(data);
            });
        }, 0, 20, TimeUnit.MILLISECONDS);
    }

    private void updateCheckBoxes(Map<String, Object> data) {

        for (String key : data.keySet()) {
            String rootTable = key.split("/")[0];
            if (!createdCheckBoxes.containsKey(rootTable)) {
                CheckBox checkBox = new CheckBox(rootTable);
                checkBox.setSelected(true);
                createdCheckBoxes.put(rootTable, checkBox);
                checkBoxVBox.getChildren().add(checkBox);
            }
        }
    }

    private void updateSmartDashboard(Map<String, Object> data) {
        GridPane gridPane = new GridPane();
        int row = 0, col = 0;
        StringBuilder textOutput = new StringBuilder();
    
        // Handle Buttons and Gauges
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String shortKey = key.replace("SmartDashboard/", "");
            FRCConfig.UIType uiType = FRCConfig.getUIType(shortKey);
            
            String rootTable = key.split("/")[0];
            CheckBox checkBox = createdCheckBoxes.get(rootTable);
    
           /*  if (checkBox != null && checkBox.isSelected()) {
                textOutput.append(key).append(": ").append(value).append("\n");
    
                if (uiType == FRCConfig.UIType.BUTTON) {
                    ToggleButton button = new ToggleButton(shortKey);
                    button.setSelected(robotData.getBooleanValue(shortKey));
                    button.setOnAction(e -> {
                        boolean currentValue = robotData.getBooleanValue(shortKey);
                        robotData.setBooleanValue(shortKey, !currentValue);
                        System.out.println("Toggled " + shortKey + " to: " + !currentValue);  // Debug
                    });
                    gridPane.add(button, col, row);
            
                } else if (uiType == FRCConfig.UIType.GAUGE && value instanceof Number) {
                    FRCConfig.GaugeConfig config = FRCConfig.getConfig(shortKey);
                    Gauge gauge = gauges.getOrDefault(key, GaugeBuilder.create().title(shortKey).build());
                    gauge.setMinValue(config.minValue);
                    gauge.setMaxValue(config.maxValue);
                    gauge.setUnit(config.unit);
                    gauge.setValue(((Number) value).doubleValue());
                    gauges.put(key, gauge);
                    gridPane.add(gauge, col, row);
                }
    
                col++;
                if (col > 3) {  // Reset column and increment row after every 4th element
                    col = 0;
                    row++;
                }
            }
            */
        }

Test = robotData.getDoubleValue("BatV"); 

Gauge Voltage = new Gauge();

Voltage.setValue(Test);

Voltage.setSkinType(Gauge.SkinType.SIMPLE);
Voltage.setTitle("Voltage");  

Voltage.setUnit("V");  //unit
         Voltage.setUnitColor(Color.BLACK);
         Voltage.setDecimals(1);  

         Voltage.setMinValue(7.0);
         Voltage.setMaxValue(13.0);
         Voltage.setMajorTickMarksVisible(true);
         Voltage.setMinorTickMarksVisible(true);
         Voltage.setAnimated(true);
         Voltage.setAnimationDuration(500);

         Voltage.setValueColor(Color.BLACK);  
         Voltage.setTitleColor(Color.BLACK);  
         Voltage.setSubTitleColor(Color.BLACK);  
         Voltage.setBarColor(Color.rgb(0, 214, 215));  
         Voltage.setNeedleColor(Color.RED);  
         Voltage.setThresholdColor(Color.RED); 
         Voltage.setThreshold(85);
         Voltage.setThresholdVisible(true);
         Voltage.setTickLabelColor(Color.rgb(151, 151, 151));  
         Voltage.setTickMarkColor(Color.BLACK);  
         Voltage.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL); 

        textArea.setText(textOutput.toString());
        smartDashboardVBox.getChildren().clear();
        smartDashboardVBox.getChildren().add(gridPane);
           
gridPane.getChildren().add(Voltage);

    }    
}
