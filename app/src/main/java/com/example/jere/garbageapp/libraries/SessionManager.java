package com.example.jere.garbageapp.libraries;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jere on 1/24/2017.
 */

public class SessionManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public SessionManager(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("creds", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(Boolean islogged) {
        editor.putBoolean("loggedInmode",islogged);
        editor.commit();
    }
    public  boolean loggedIn(){
        return prefs.getBoolean("loggedInmode",false);
    }
    public void setNumber(String num){
        editor.putString("phone_number",num);
        editor.commit();
    }
    public String getNumber(){
        return prefs.getString("phone_number","");
    }

    public void setEmail(String email){
        editor.putString("email",email);
        editor.commit();
    }
    public String getEmail(){
        return prefs.getString("email","");
    }

    public void setName(String name){
        editor.putString("name",name);
        editor.commit();
    }
    public String getName(){
        return prefs.getString("name","");
    }

    public void setUserId(String id){
        editor.putString("user_id",id);
        editor.commit();
    }
    public String getUserId(){
        return prefs.getString("user_id","");
    }

    public void setHouse(String house){
        editor.putString("house",house);
        editor.commit();
    }
    public String getHouse(){
        return prefs.getString("house","");
    }

    public void setEstate(String estate){
        editor.putString("estate",estate);
        editor.commit();
    }
    public String getEstate(){
        return prefs.getString("estate","");
    }

    public void setLocation(String location){
        editor.putString("location",location);
        editor.commit();
    }
    public String getLocation(){
        return prefs.getString("location","");
    }


}
