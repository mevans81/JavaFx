package com.app.gui;

import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;




public class DateAndTime {

private final SimpleDoubleProperty currentHour = new SimpleDoubleProperty(0);
private final SimpleDoubleProperty currentMinute = new SimpleDoubleProperty(0);
private final SimpleDoubleProperty currentSecond = new SimpleDoubleProperty(0);
  
public DateAndTime() {

}

public DoubleProperty currentHour() {
    return currentHour();
}
public DoubleProperty currentMinute() {
    return currentMinute();
}
public DoubleProperty currentSecond() {
    return currentSecond();
}


public static void DateAndTime1()
{

int Year =  Calendar.getInstance().get(Calendar.YEAR);
int Month = Calendar.getInstance().get(Calendar.MONTH);
int Date =  Calendar.getInstance().get(Calendar.DATE);
int Hour =  Calendar.getInstance().get(Calendar.HOUR);
int Minute =  Calendar.getInstance().get(Calendar.MINUTE);
int Second =  Calendar.getInstance().get(Calendar.SECOND);

System.out.println(Hour + ":" + Minute + "." + Second);
System.out.println(Date + "-" + Month + "-" + Year);



}







    
}
