package com.example.jere.garbageapp.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.Constants;
import com.example.jere.garbageapp.libraries.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatButton btn_login;
    private EditText et_email,et_password;
    private TextView tv_register;
    private SharedPreferences pref;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.tv_register);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        sessionManager=new SessionManager(getActivity());
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_register:
                goToRegister();
                break;

            case R.id.btn_login:
                login();
        }
    }
    public void login() {
        if (!validate()){
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging In.Please Wait....");
        progressDialog.show();

        final String uemail = et_email.getText().toString();
        final String upassword = et_password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEND_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String code=jsonObject.getString("login_code");
                            if (code.equals("login_failed")){
                                String message=jsonObject.getString("message");
                                btn_login.setEnabled(true);
                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            }else if(code.equals("login_success")){
                                btn_login.setEnabled(true);
                                sessionManager.setLoggedIn(true);
                                JSONObject details=jsonObject.getJSONObject("details");
                                sessionManager.setnumber(details.getString("user_no"));
                                sessionManager.setname(details.getString("name"));
                                sessionManager.setemail(details.getString("email"));
                                //Log.d("Success",details.getString("name"));
                                Snackbar.make(getView(),"Login Successful",Snackbar.LENGTH_LONG).show();
                                onLoginSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        btn_login.setEnabled(true);
                        //Log.d("Error","volley error");
                        Snackbar.make(getView(), error.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_EMAIL, uemail);
                params.put(Constants.KEY_PASSWORD, upassword);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void onLoginSuccess() {
        et_email.setText("");
        et_password.setText("");
        getEventsFragment();
    }

    private void onLoginFailed() {
        Snackbar.make(getView(), "Login failed !", Snackbar.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    private boolean validate(){
        boolean valid = true;
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            et_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            et_password.setError(null);
        }

        return valid;
    }


    private void goToRegister(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_container,new RegisterFragment());
        ft.commit();
    }
    private void getEventsFragment(){
        FragmentTransaction fragmentTransaction =(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_container,new EventsFragment());
        fragmentTransaction.commit();
    }

    private void goToProfile(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_container,new ProfileFragment());
        ft.commit();
    }
}
