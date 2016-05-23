package com.bastosbf.peladaarte.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bastosbf on 18/05/16.
 */
public class Pelada implements Serializable {
    private Integer id;
    private Player owner;
    private String name;
    private Integer day;
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
