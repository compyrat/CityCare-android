package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;


import java.util.Calendar;

/**
 * Created by albertribgar on 12/05/2016.
 */
public class Incidence {
    private String incidenceId;
    private String title;
    private int category;
    private Calendar date;
    private String pictureURL;
    private String location;
    private int status;

    public Incidence(String incidenceId, String title, int category, Calendar date, String pictureURL, String location, int status) {
        setIncidenceId(incidenceId);
        setTitle(title);
        setCategory(category);
        setDate(date);
        setPicture(pictureURL);
        setLocation(location);
        setStatus(status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getPicture() {
        return pictureURL;
    }

    public void setPicture(String picture) {
        this.pictureURL = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getIncidenceId() {
        return incidenceId;
    }

    public void setIncidenceId(String incidenceId) {
        this.incidenceId = incidenceId;
    }
}
