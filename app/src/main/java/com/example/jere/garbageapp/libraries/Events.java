package com.example.jere.garbageapp.libraries;

import android.graphics.Bitmap;

/**
 * Created by jere on 1/5/2017.
 */

public class Events {
    private int event_id;
    private String event_name;
    private String event_description;
    private String venue;
    private String event_date;
    private Bitmap image;

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getVenue() {
        return venue;
    }

    public String getEvent_date() {
        return event_date;
    }

    public Bitmap getImage() {
        return image;
    }
}

