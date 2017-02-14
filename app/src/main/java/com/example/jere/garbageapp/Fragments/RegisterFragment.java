package com.example.jere.garbageapp.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener{

    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name,et_house,et_phone;
    private TextView tv_login;
    private ProgressBar progress;
    private AlertDialog.Builder builder;
    private MaterialBetterSpinner et_estate,et_location;
   Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_phone=(EditText)view.findViewById(R.id.et_phone);
        et_house=(EditText)view.findViewById(R.id.et_house);
        et_estate=(MaterialBetterSpinner)view.findViewById(R.id.fragment_register_estate);
        et_location=(MaterialBetterSpinner)view.findViewById(R.id.fragment_register_location);
        et_password = (EditText)view.findViewById(R.id.et_password);
        builder = new AlertDialog.Builder(getActivity());

        toolbar = (Toolbar)(getActivity()).findViewById(R.id.toolbar);

        List<String> mylocations= new ArrayList<>();
        mylocations.add("Langata");
        mylocations.add("Nairobi West");
        mylocations.add("StrathMore");
        mylocations.add("HighRise");

        ArrayAdapter<String> locationadapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mylocations);
        locationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_location.setAdapter(locationadapter);

        List<String> myestates = new ArrayList<>();
        myestates.add("Funguo");
        myestates.add("Akila");
        myestates.add("Airport View");
        myestates.add("Blue Sky");
        myestates.add("Siwaka Estate");

        ArrayAdapter<String> estateadapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myestates);
        estateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_estate.setAdapter(estateadapter);


        btn_register = (AppCompatButton)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_login:
                goToLogin();
                break;

            case R.id.btn_register:
                signup();
                break;
        }
    }

    public void signup() {
//    Log.d(TAG, "Signup");

        if (!validate()) {
            builder.setTitle("Empty fields");
            builder.setMessage("Fill in the empty fields");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        }
        else if (!isNetworkConnected()){
            Snackbar.make(getView(),"No Network Connection.Turn on Your Wifi", Snackbar.LENGTH_LONG).show();
        }
        else {
            btn_register.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Registering.Please Wait....");
            progressDialog.show();
            final String uname = et_name.getText().toString();
            final String uemail = et_email.getText().toString();
            final String uphone=et_phone.getText().toString();
            final String uhouse = et_house.getText().toString();
            final String uestate = et_estate.getText().toString();
            final String ulocation = et_location.getText().toString();
            final String upass = et_password.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.register_users,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            try {
                                JSONArray jsonArray= new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                String code=jsonObject.getString("code");
                                String message=jsonObject.getString("message");
                                if (code.equals("reg_failed")){
                                    btn_register.setEnabled(true);
                                    Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                                }else if(code.equals("reg_success")){
                                    Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                                    onSignupSuccess();
                                }

                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
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
//                    String user_id="2";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.KEY_NAME, uname);
                    params.put(Constants.KEY_EMAIL, uemail);
                    params.put(Constants.KEY_NUMBER, uphone);
                    params.put(Constants.KEY_HOUSE, uhouse);
                    params.put(Constants.KEY_ESTATE, uestate);
                    params.put(Constants.KEY_LOCATION, ulocation);
                    params.put(Constants.KEY_PASSWORD, upass);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        }
    }


    public boolean validate() {
        boolean valid = true;

        String name= et_name.getText().toString();
        String email= et_email.getText().toString();
        String phone=et_phone.getText().toString();
        String house=et_house.getText().toString();
        String estate=et_estate.getText().toString();
        String location=et_location.getText().toString();
        String password= et_password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            et_name.setError("At least 3 characters");
            valid = false;
        } else {
            et_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 9 ) {
            et_phone.setError("At least 10 characters");
            valid = false;
        } else {
            et_phone.setError(null);
        }

        if (house.isEmpty() || house.length() < 3) {
            et_house.setError("At least 3 characters");
            valid = false;
        } else {
            et_house.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            et_password.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            et_password.setError(null);
        }
        if(estate.isEmpty()){
            et_estate.setError("Select your Estate");
            valid =false;
        }
        else{
            et_estate.setError(null);
        }
        if(location.isEmpty()){
            et_location.setError("Select your Location");
            valid=false;
        }else {
            et_location.setError(null);
        }

        return valid;
    }

    public void onSignupSuccess() {
        btn_register.setEnabled(true);
        et_name.setText("");
        et_email.setText("");
        et_house.setText("");
        et_password.setText("");
        goToLogin();

    }

    public void goToLogin(){
        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_container,login);
        ft.commit();
        toolbar.setTitle("LOGIN");
    }
}