package com.example.jere.garbageapp.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.jere.garbageapp.libraries.Constants.KEY_NUMBER;
import static com.example.jere.garbageapp.libraries.Constants.KEY_PASSWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_old_password,et_new_password;
    private AppCompatButton btn_change_password;
    private SessionManager sessionManager;
    private AlertDialog.Builder builder;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_change_password, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        et_new_password=(EditText)view.findViewById(R.id.fragment_change_password_et_new_password);
        et_old_password=(EditText)view.findViewById(R.id.fragment_change_password_et_old_password);
        btn_change_password=(AppCompatButton)view.findViewById(R.id.fragment_change_password_button);
        builder = new AlertDialog.Builder(getActivity());
        sessionManager= new SessionManager(getContext());
        btn_change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_change_password_button:
                changePassword();
                break;
        }
    }

    public void changePassword(){
        if(!validate()){
            builder.setTitle("Empty fields");
            builder.setMessage("Fill in the empty fields");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }else if(!isNetworkConnected()){
            Snackbar.make(getView(),"No Network Connection.Turn on Your Wifi", Snackbar.LENGTH_LONG).show();
        } else {

            btn_change_password.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating.Please Wait....");
            progressDialog.show();

            final String phoneno=sessionManager.getNumber();
            final String old=et_old_password.getText().toString();
            final String newp=et_new_password.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CPASS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject= new JSONObject(response);
                                String update=jsonObject.getString("update");
                                String message=jsonObject.getString("message");
                                if (update.equals("update_failed")){
                                    btn_change_password.setEnabled(true);
                                    progressDialog.show();
                                    Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                                }else if(update.equals("update_success")){
                                    Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                                }

                                //Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
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
                    params.put(KEY_NUMBER, phoneno);
                    params.put("old_password",old);
                    params.put(KEY_PASSWORD, newp);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(stringRequest);

        }
    }

    private boolean validate(){

       boolean valid=true;
        String old=et_old_password.getText().toString();
        String newpass=et_new_password.getText().toString();
        if(old.isEmpty()){
            et_old_password.setError("Old password field is empty");
            valid =false;
        }
        if(newpass.isEmpty() || newpass.length() < 4){
            et_new_password.setError("New password must be atleast 4 Characters");
            valid =false;
        }
        return  valid;
    }

}
