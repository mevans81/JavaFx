package com.app.gui;

import com.app.data.RobotData;
import com.app.util.FRCConfig;

import edu.wpi.first.networktables.ValueEventData;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.GaugeDesign;
import eu.hansolo.medusa.LcdDesign;
import eu.hansolo.medusa.LcdFont;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.TickLabelLocation;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.TickMarkType;
import eu.hansolo.medusa.Gauge.KnobType;
import eu.hansolo.medusa.Gauge.LedType;
import eu.hansolo.medusa.Gauge.NeedleShape;
import eu.hansolo.medusa.Gauge.NeedleSize;
import eu.hansolo.medusa.Gauge.ScaleDirection;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.skins.ModernSkin;
import javafx.application.Platform;
import javafx.geometry.HorizontalDirection;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

    private double Test;
private Gauge Voltage, Current;
    /**
     * @param primaryStage
     */
    public void init(Stage primaryStage) {
        // Initialize RobotData
        robotData = new RobotData("10.20.28.2", 1735);
     
        // Initialize UI
     //   BorderPane root = new BorderPane();
     //   ScrollPane checkBoxScrollPane = new ScrollPane(checkBoxVBox);

        // Create SplitPane and add TextArea and VBox for gauges
      //  SplitPane splitPane = new SplitPane();
      //  splitPane.getItems().addAll(textArea, smartDashboardVBox);

        // Add to root layout
     //   root.setLeft(checkBoxScrollPane);
     //   root.setCenter(splitPane);



        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Map<String, Object> data = robotData.fetchData();
            Platform.runLater(() -> {
                updateCheckBoxes(data);
                updateSmartDashboard(data);
            }); 
        }, 0, 20, TimeUnit.MILLISECONDS);

Voltage  = GaugeBuilder.create()
                    .skinType(Gauge.SkinType.HORIZONTAL)
                     .backgroundPaint(Color.BLACK)
                     .foregroundBaseColor(Color.WHITE)
                       .prefSize(380, 400)
                       .startAngle(290)
                       .angleRange(220)
                       .minValue(7)
                       .maxValue(13)
                       .valueVisible(true)
                     
                       .minorTickMarksVisible(false)
                       .majorTickMarkType(TickMarkType.BOX)
                       .mediumTickMarkType(TickMarkType.BOX)
                       .title("Battery\nVoltage")
        
                       .needleShape(NeedleShape.ROUND)
                       .needleSize(NeedleSize.THICK)
                       .needleColor(Color.RED)
                       .knobColor(Color.WHITE)
                       .customTickLabelsEnabled(true)
                       .customTickLabelFontSize(40)
                       .customTickLabels("7", "8", "9", "10", "11", "12", "13")
                       .sections(new Section(7, 9, Color.RED),
                                 new Section(9, 11, Color.YELLOW),
                                 new Section(11, 13, Color.FORESTGREEN))
                       .sectionsVisible(true)
                       .animated(true)
                       .build();


                       
//Voltage.setLayoutX(0);Voltage.setLayoutY(0);

//Current.setLayoutX(300);Current.setLayoutY(0);


Current  = GaugeBuilder.create()
                    .skinType(Gauge.SkinType.HORIZONTAL)
                     .backgroundPaint(Color.BLACK)
                     .foregroundBaseColor(Color.WHITE)
                       .prefSize(380, 400)
                       .startAngle(290)
                       .angleRange(220)
                       .minValue(0)
                       .maxValue(80)
                       .valueVisible(true)
                       .minorTickMarksVisible(false)
                       .majorTickMarkType(TickMarkType.BOX)
                       .mediumTickMarkType(TickMarkType.BOX)
                       .title("Battery\nCurrent")
        
                       .needleShape(NeedleShape.ROUND)
                       .needleSize(NeedleSize.THICK)
                       .needleColor(Color.RED)
                       .knobColor(Color.WHITE)
                       .customTickLabelsEnabled(true)
                       .customTickLabelFontSize(40)
                       .customTickLabels("0", "10", "20", "30", "40", "50", "60", "70", "80")
                      
                       .sectionsVisible(true)
                       .animated(true)
                       .build();
        // Stage setup
       
        HBox displayHBox= new HBox(Current, Voltage);
       
        StackPane root = new StackPane(displayHBox); 
       
       ///////////////////////////////////////////
        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(new Scene(root, Color.BLACK));

        displayHBox.setSpacing(20);
        
        primaryStage.setFullScreen(true);
        primaryStage.show();


//root.getChildren().add(Voltage);  
  //   root.getChildren().add(Current);



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


//Test = robotData.getDoubleValue("BatV");
//System.out.println(Test);
//Voltage.setValue(Test);
        }
        

Voltage.setValue(robotData.getDoubleValue("BatV"));
Current.setValue(robotData.getDoubleValue("BatI"));
}
    //    textArea.setText(textOutput.toString());
     //   smartDashboardVBox.getChildren().clear();
     //   smartDashboardVBox.getChildren().add(gridPane);
           
//root.getChildren().add(Voltage);
}    
