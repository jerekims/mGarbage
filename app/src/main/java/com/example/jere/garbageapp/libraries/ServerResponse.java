package com.example.jere.garbageapp.libraries;

/**
 * Created by jere on 11/18/2016.
 */

public class ServerResponse {

    private String result="";
    private String message="";
    private User user;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}