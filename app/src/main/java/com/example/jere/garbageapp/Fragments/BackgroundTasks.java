package com.example.jere.garbageapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.jere.garbageapp.libraries.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jere on 1/4/2017.
 */

public class BackgroundTasks extends AsyncTask<String, Void, String> {
    private RecyclerView.Adapter adapter;
    Context context;

    public BackgroundTasks(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... args) {


        String function = args[0];

       if(function.equals("login")){
            String email = args[1];
            String pass=args[2];

            try {
                URL url= new URL(Constants.SEND_LOGIN);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                Log.d("Login","Submitted");
                String data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("Login","Submitted");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONObject resultJSON = null;
                try {
                    resultJSON = new JSONObject(result);
                    JSONObject userDetails = resultJSON.getJSONObject("userdetails");
                    String userid = userDetails.getString("user_id");
                    String uname = userDetails.getString("name");
                    String uemail = userDetails.getString("email");
                    String houseno = userDetails.getString("house_number");
                    String estatename = userDetails.getString("estate_name");
                    String ulocation = userDetails.getString("location");

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("userid", userid);
                    editor.putString("name", uname);
                    editor.putString("email", uemail);
                    editor.putString("house_number", houseno);
                    editor.putString("estate_name", estatename);
                    editor.putString("location", ulocation);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(function.equals("complain")) {
            String phone=args[1];
            String desc = args[2];
            String wtype = args[3];
            String image = args[4];

            try {
                URL url= new URL(Constants.SEND_COMPLAIN);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                Log.d("Complain","Submitted bufferwriter");
                String data= URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(desc,"UTF-8")+"&"+
                        URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                        URLEncoder.encode("wastetype","UTF-8")+"="+URLEncoder.encode(wtype,"UTF-8")+"&"+
                        URLEncoder.encode("cimage","UTF-8")+"="+URLEncoder.encode(image,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("Complain","Submitted outputstream");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
                    Boolean type;
            String message="";
            try {
                JSONObject resultJSON = new JSONObject(result);
                type = resultJSON.getBoolean("type");

                if (type==false)
                {
                    message=resultJSON.getString("message");
                    //Snackbar.make(getView(),message, Snackbar.LENGTH_LONG).show();
                   Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                }
                else
                {
                    message=resultJSON.getString("message");
                    //Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
