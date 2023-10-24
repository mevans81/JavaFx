package com.app.gui;

import eu.hansolo.medusa.Fonts;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.text.*;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AnalogClock {

    private static final double START_RADIUS = 240;
    private static final int NO_HOUR_TICKS = 12;
    private static final int NO_MINUTE_TICKS = 60;
    private final AnalogClockWork clockwork = new AnalogClockWork();
/*
    public static void main(String[] args) {
        launch(args);
    }
*/
//    @Override
    public Node getClock() {

        Group group = new Group();
        group.getChildren().add(clockDial());
        group.getChildren().add(minuteTickMarks());
        group.getChildren().add(hourTickMarks());
        group.getChildren().add(hourHand());
        group.getChildren().add(minuteHand());
        group.getChildren().add(secondsHand());
        group.getChildren().add(hourNumbers());

        return group;
        
        /*
         final Parent root = GroupBuilder.create()
         .children(
         clockDial(),
         minuteTickMarks(),
         hourTickMarks(),
         hourHand(),
         minuteHand(),
         secondsHand()
         )
         .build();
         setUpMouseForScaleAndMove(stage, root);
         Scene scene = transparentScene(root);
         showTransparentStage(stage, scene);
         */
    }

    private Node clockDial() {

        Stop stops[] = {
            new Stop(0.92, Color.WHITE),
            new Stop(0.98, Color.BLACK),
            new Stop(1.0, Color.BLACK)
        };
        RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);

        Circle circle = new Circle(START_RADIUS, gradient);
        circle.setCenterX(START_RADIUS);
        circle.setCenterY(START_RADIUS);
        return circle;
    }

    private Node hourHand() {
        double distanceFromRim = START_RADIUS * 0.5;
        Rotate rotate = handRotation(clockwork.hourHandAngle());
        return hourOrMinuteHand(distanceFromRim, Color.BLACK, rotate);
    }

    private Node minuteHand() {
        double distanceFromRim = START_RADIUS * 0.75;
        Rotate rotate = handRotation(clockwork.minuteHandAngle());
        return hourOrMinuteHand(distanceFromRim, Color.BLACK, rotate);
    }

    private Node secondsHand() {
        double distanceFromRim = START_RADIUS * 0.7;
        Color handColor = Color.RED;
        Rotate rotate = handRotation(clockwork.secondsHandAngle());

        Group group = new Group();
        group.getChildren().add(secondsHandLine(distanceFromRim, handColor));
       // group.getChildren().add(secondsHandTip(distanceFromRim, handColor));
        group.getChildren().add(centerPoint(handColor));
        group.getTransforms().add(rotate);

        return group;
        /*
         return GroupBuilder.create()
         .children(
         secondsHandLine(distanceFromRim, handColor),
         secondsHandTip(distanceFromRim, handColor),
         centerPoint(handColor)
         )
         .transforms(rotate)
         .build();
         */
    }

    private Node secondsHandTip(double distanceFromRim, Color handColor) {
        double handTipRadius = START_RADIUS * 0.07;

        Circle circle = new Circle();
        circle.setCenterX(START_RADIUS);
        circle.setCenterY(START_RADIUS - distanceFromRim);
        circle.setFill(handColor);
        circle.setRadius(handTipRadius);

        return circle;
        /*
         return CircleBuilder.create()
         .centerX(START_RADIUS)
         .centerY(START_RADIUS - distanceFromRim)
         .fill(handColor)
         .radius(handTipRadius)
         .build();
         */
    }

    private Node secondsHandLine(double distanceFromRim, Paint handColor) {
        double handCenterExtension = START_RADIUS * 0.15;
        double handWidth = START_RADIUS * 0.02;

        Line line = new Line();
        line.setStartX(START_RADIUS);
        line.setStartY(START_RADIUS - distanceFromRim);
        line.setEndX(START_RADIUS);
        line.setEndY(START_RADIUS + handCenterExtension);
        line.setStrokeWidth(handWidth);
        line.setStroke(handColor);

        return line;
        /*
         return LineBuilder.create()
         .startX(START_RADIUS)
         .startY(START_RADIUS - distanceFromRim)
         .endX(START_RADIUS)
         .endY(START_RADIUS + handCenterExtension)
         .strokeWidth(handWidth)
         .stroke(handColor)
         .build();
         */
    }

    private Rotate handRotation(ObservableDoubleValue handAngle) {

        Rotate handRotation = new Rotate();
        handRotation.setPivotX(START_RADIUS);
        handRotation.setPivotY(START_RADIUS);
        /*
         Rotate handRotation = RotateBuilder.create()
         .pivotX(START_RADIUS)
         .pivotY(START_RADIUS)
         .build();
         */
        handRotation.angleProperty().bind(handAngle);
        return handRotation;
    }

    private Node hourOrMinuteHand(double distanceFromRim, Color color, Rotate rotate) {
        double handBaseWidth = START_RADIUS * 0.05;
        double handTipWidth = START_RADIUS * 0.03;
        double handCenterExtension = START_RADIUS * 0.15;
        double leftBaseCornerX = START_RADIUS - handBaseWidth;
        double baseY = START_RADIUS + handCenterExtension;
        double tipY = START_RADIUS - distanceFromRim;
        double leftTipCornerX = START_RADIUS - handTipWidth;
        double rightTipCornerX = START_RADIUS + handTipWidth;
        double rightCornerBaseX = START_RADIUS + handBaseWidth;

        Path path = new Path();
        path.setFill(color);
        path.setStroke(Color.TRANSPARENT);
        path.getElements().add(new MoveTo(leftBaseCornerX, baseY));
        path.getElements().add(new LineTo(leftTipCornerX, tipY));
        path.getElements().add(new LineTo(rightTipCornerX, tipY));
        path.getElements().add(new LineTo(rightCornerBaseX, baseY));
        path.getElements().add(new LineTo(leftBaseCornerX, baseY));
        path.getTransforms().add(rotate);

        return path;
        /*
         return PathBuilder.create()
         .fill(color)
         .stroke(Color.TRANSPARENT)
         .elements(
         new MoveTo(leftBaseCornerX, baseY),
         new LineTo(leftTipCornerX, tipY),
         new LineTo(rightTipCornerX, tipY),
         new LineTo(rightCornerBaseX, baseY),
         new LineTo(leftBaseCornerX, baseY)
         )
         .transforms(rotate)
         .build();
         */
    }


private Node hourNumbers(){
   Group hourNumbersGroup = new Group();

        int noNumbers = 12;
        for (int n = 1; n <= noNumbers; n++) {
            hourNumbersGroup.getChildren().add(hourNumber(n, noNumbers));
        }
  
    return hourNumbersGroup;
}

private Node hourNumber(int n, int noNumbers) {
        int[] numbtrans = new int[] {0,2, 1, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3};
        Text Number = new Text(Integer.toString(numbtrans[n]));
        Number.setX(START_RADIUS - 12 + (START_RADIUS*.63 * Math.cos(2*Math.PI/noNumbers * n)));
        Number.setY(START_RADIUS + 14 - (START_RADIUS*.63 * Math.sin(2*Math.PI/noNumbers * n)));
        if (n == 3) Number.setX(START_RADIUS - 21 + (START_RADIUS*.63 * Math.cos(2*Math.PI/noNumbers * n)));
        if (n == 4) Number.setX(START_RADIUS - 17 + (START_RADIUS*.63 * Math.cos(2*Math.PI/noNumbers * n)));
        Number.setFont(Fonts.latoBold(40));
        Number.setTextAlignment(TextAlignment.RIGHT);
        return Number;
}


    private Node minuteTickMarks() {
        Group tickMarkGroup = new Group();
        int noTicks = NO_MINUTE_TICKS;
        for (int n = 0; n < noTicks; n++) {
            tickMarkGroup.getChildren().add(tickMark(n, 1, noTicks));
        }
        return tickMarkGroup;
    }

    private Node hourTickMarks() {
        Group tickMarkGroup = new Group();
        int noTicks = NO_HOUR_TICKS;
        for (int n = 0; n < noTicks; n++) {
            tickMarkGroup.getChildren().add(tickMark(n, 6, noTicks));
        }
        return tickMarkGroup;
    }

    private Node tickMark(int n, double width, int noTicks) {
        Line line = new Line();
        line.setStartX(START_RADIUS);
        line.setStartY(START_RADIUS * 0.12);
        line.setEndX(START_RADIUS);
        line.setEndY(START_RADIUS * 0.2 + width * 2);

        line.setStrokeWidth(width);

        Rotate rotate = new Rotate();
        rotate.setPivotX(START_RADIUS);
        rotate.setPivotY(START_RADIUS);
        rotate.setAngle(360 / noTicks * n);

        line.getTransforms().add(rotate);

        return line;
        /*
         return LineBuilder.create()
         .startX(START_RADIUS)
         .startY(START_RADIUS * 0.12)
         .endX(START_RADIUS)
         .endY(START_RADIUS * 0.2 + width * 2)
         .transforms(
         RotateBuilder.create()
         .pivotX(START_RADIUS)
         .pivotY(START_RADIUS)
         .angle(360 / noTicks * n)
         .build()
         )
         .strokeWidth(width)
         .build();
         */
    }

    private Node centerPoint(Color color) {

        Circle circle = new Circle();
        circle.setFill(color);
        circle.setRadius(0.03 * START_RADIUS);
        circle.setCenterX(START_RADIUS);
        circle.setCenterY(START_RADIUS);

        return circle;
        /*
         return CircleBuilder.create()
         .fill(color)
         .radius(0.03 * START_RADIUS)
         .centerX(START_RADIUS)
         .centerY(START_RADIUS)
         .build();
         */
    }
/*
    private void setUpMouseForScaleAndMove(final Stage stage, final Parent root) {
        SimpleDoubleProperty mouseStartX = new SimpleDoubleProperty(0);
        SimpleDoubleProperty mouseStartY = new SimpleDoubleProperty(0);
        root.setOnMousePressed(setMouseStartPoint(mouseStartX, mouseStartY));
        root.setOnMouseDragged(moveWhenDragging(stage, mouseStartX, mouseStartY));
        root.onScrollProperty().set(scaleWhenScrolling(stage, root));
    }

    private EventHandler<? super MouseEvent> setMouseStartPoint(final SimpleDoubleProperty mouseStartX, final SimpleDoubleProperty mouseStartY) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseStartX.set(mouseEvent.getX());
                mouseStartY.set(mouseEvent.getY());
            }
        };
    }

    private EventHandler<MouseEvent> moveWhenDragging(final Stage stage, final SimpleDoubleProperty mouseStartX, final SimpleDoubleProperty mouseStartY) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(stage.getX() + mouseEvent.getX() - mouseStartX.doubleValue());
                stage.setY(stage.getY() + mouseEvent.getY() - mouseStartY.doubleValue());
            }
        };
    }

    private EventHandler<ScrollEvent> scaleWhenScrolling(final Stage stage, final Parent root) {
        return new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                double scroll = scrollEvent.getDeltaY();
                root.setScaleX(root.getScaleX() + scroll / 100);
                root.setScaleY(root.getScaleY() + scroll / 100);
                root.setTranslateX(root.getTranslateX() + scroll);
                root.setTranslateY(root.getTranslateY() + scroll);
                stage.sizeToScene();
            }
        };
    }
*/
    private Scene transparentScene(Parent root) {

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        return scene;
        /*
         return SceneBuilder.create()
         .root(root)
         .fill(Color.TRANSPARENT)
         .build();
         */
    }

    private void showTransparentStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

}

