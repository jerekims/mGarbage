package com.example.jere.garbageapp.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.Constants;
import com.example.jere.garbageapp.libraries.SessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.jere.garbageapp.R.id.fragment_change_details_button;
import static com.example.jere.garbageapp.libraries.Constants.KEY_EMAIL;
import static com.example.jere.garbageapp.libraries.Constants.KEY_ESTATE;
import static com.example.jere.garbageapp.libraries.Constants.KEY_HOUSE;
import static com.example.jere.garbageapp.libraries.Constants.KEY_ID;
import static com.example.jere.garbageapp.libraries.Constants.KEY_LOCATION;
import static com.example.jere.garbageapp.libraries.Constants.KEY_NAME;
import static com.example.jere.garbageapp.libraries.Constants.KEY_NUMBER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeProfileFragment extends BaseFragment {

    private TextView tv_message,tx_message_details;
    private SharedPreferences pref;
    private AppCompatButton btn_details;
    private EditText name,email,phoneno,houseno;
    AlertDialog.Builder builder;
    private SessionManager sessionManager;
    private MaterialBetterSpinner estate,location;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_details,container,false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        name = (EditText)view.findViewById(R.id.fragment_change_details_name);
        email = (EditText) view.findViewById(R.id.fragment_change_details_email);
        phoneno = (EditText) view.findViewById(R.id.fragment_change_details_phoneno);
        houseno = (EditText) view.findViewById(R.id.fragment_change_details_houseno);
        estate=(MaterialBetterSpinner)view.findViewById(R.id.fragment_change_details_estate);
        location=(MaterialBetterSpinner)view.findViewById(R.id.fragment_change_details_location);
        btn_details=(AppCompatButton)view.findViewById(fragment_change_details_button);

        builder=new AlertDialog.Builder(getActivity());
        sessionManager= new SessionManager(getActivity());

        SetInformation();

        List<String> mylocations= new ArrayList<>();
        mylocations.add("Langata");
        mylocations.add("Nairobi West");
        mylocations.add("StrathMore");
        mylocations.add("HighRise");

        ArrayAdapter<String> locationadapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mylocations);
        locationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(locationadapter);

        List<String> myestates = new ArrayList<>();
        myestates.add("Funguo");
        myestates.add("Akila");
        myestates.add("Airport View");
        myestates.add("Blue Sky");
        myestates.add("Siwaka Estate");

        ArrayAdapter<String> estateadapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myestates);
        estateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estate.setAdapter(estateadapter);

        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!validate()){
                   builder.setTitle("Empty fields");
                   builder.setMessage("Fill in the empty fields");
                   builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   builder.show();
               } else if(!isNetworkConnected()){
                   Snackbar.make(getView(),"No Network Connection.Turn on Your Wifi", Snackbar.LENGTH_LONG).show();
               }
               else{

                   btn_details.setEnabled(false);
                   progressDialog = new ProgressDialog(getActivity());
                   progressDialog.setIndeterminate(true);
                   progressDialog.setMessage("Updating Profile.Please Wait....");
                   progressDialog.show();

                   String userid=sessionManager.getUserId();
                   changeDetails(userid,name.getText().toString(),email.getText().toString(),phoneno.getText().toString(),houseno.getText().toString(),
                           estate.getText().toString(),location.getText().toString());
               }
            }
        });

    }

    private void SetInformation() {
        name.setText(sessionManager.getName());
        email.setText(sessionManager.getEmail());
        phoneno.setText(sessionManager.getNumber());
        houseno.setText(sessionManager.getHouse());
        estate.setText(sessionManager.getEstate());
        location.setText(sessionManager.getLocation());
    }

    public boolean validate(){
        boolean valid=true;
        String uname=name.getText().toString();
        String uemail=email.getText().toString();
        String uphone=phoneno.getText().toString();
        String uhouse=houseno.getText().toString();
        String uestate=estate.getText().toString();
        String ulocation=location.getText().toString();

        if (uname.isEmpty() || uname.length() < 3) {
            name.setError("At least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }
        if (uemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            email.setError("Enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (uphone.isEmpty() || uphone.length() < 9 ) {
            phoneno.setError("At least 9 characters");
            valid = false;
        } else {
            phoneno.setError(null);
        }
        if (uhouse.isEmpty() || uhouse.length() < 3) {
            houseno.setError("At least 3 characters");
            valid = false;
        } else {
            houseno.setError(null);
        }

        if(uestate.isEmpty()){
            estate.setError("Select your Estate");
            valid =false;
        }
        else{
            estate.setError(null);
        }

        if(ulocation.isEmpty()){
            location.setError("Select your Location");
            valid=false;
        }else {
            location.setError(null);
        }
        return valid;
    }


    private void changeDetails(final String userid, final String name, final String email, final String phone, final String hseno, final String estate, final String loc){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHANGEDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String update=jsonObject.getString("update");
                            String message=jsonObject.getString("message");
                            if (update.equals("update_failed")){
                                btn_details.setEnabled(true);
                                progressDialog.hide();
                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            }else if(update.equals("update_success")){
                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        btn_details.setEnabled(true);
                        progressDialog.hide();
                        if (volleyError instanceof NetworkError) {
                            Snackbar.make(getView(),"Cannot connect to Internet...Please check your connection!",Snackbar.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Snackbar.make(getView(),"The server could not be found. Please try again after some time!!",Snackbar.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Snackbar.make(getView(),"Cannot connect to Internet...Please check your connection!",Snackbar.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Snackbar.make(getView(),"Parsing error! Please try again after some time!!",Snackbar.LENGTH_LONG).show();
                        } else if (volleyError instanceof NoConnectionError) {
                            Snackbar.make(getView(),"Cannot connect to Internet...Please check your connection!",Snackbar.LENGTH_LONG).show();
                        } else if (volleyError instanceof TimeoutError) {
                            Snackbar.make(getView(),"Connection TimeOut! Please check your internet connection.",Snackbar.LENGTH_LONG).show();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID,userid);
                params.put(KEY_NAME,name);
                params.put(KEY_EMAIL,email);
                params.put(KEY_NUMBER,phone);
                params.put(KEY_HOUSE,hseno);
                params.put(KEY_ESTATE,estate);
                params.put(KEY_LOCATION, loc);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

