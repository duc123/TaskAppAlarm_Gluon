/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.model;

import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author duc
 */
public class Task {

    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();

    public Task() {
        name.set("");
        time.set(LocalTime.of(8, 0, 0));
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }
    
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getTime() {
        return this.time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name.get())
                .append(" ").append("(")
                .append(String.valueOf(time.get().getHour()==23?24:time.get().getHour()))
                .append(":")
                .append(String.valueOf(time.get().getMinute()==59?60:time.get().getMinute()))
                .append(":")
                .append(String.valueOf(time.get().getSecond()==59?60:time.get().getSecond()))
                .append(":)")
                .toString();
    }

}
