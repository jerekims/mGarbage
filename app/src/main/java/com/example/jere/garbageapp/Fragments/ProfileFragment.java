package com.example.jere.garbageapp.Fragments;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static com.example.jere.garbageapp.libraries.Constants.KEY_NUMBER;
import static com.example.jere.garbageapp.libraries.Constants.KEY_PASSWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_message,tx_message_details;
    private SharedPreferences pref;
    private AppCompatButton btn_change_password,btn_logout,btn_details;
    private EditText et_old_password,et_new_password,firstname,lastname,email,phoneno,houseno;
    private AlertDialog dialog;
    private ProgressBar progress;
    AlertDialog.Builder builder;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        tv_name.setText("Welcome : "+pref.getString(Constants.NAME,""));
//        tv_email.setText(pref.getString(Constants.EMAIL,""));

    }

    private void initViews(View view){
        sessionManager=new SessionManager(getContext());
        btn_details = (AppCompatButton)view.findViewById(R.id.btn_chg_userdetails);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_details.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);

    }

    private void showDialog(){
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){
                    progress.setVisibility(View.VISIBLE);
                   changePasswordProcess(sessionManager.getnumber(),new_password);
                }else {
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chg_password:
                showDialog();
                break;
            case R.id.btn_chg_userdetails:
                showDetailsDialog();
                break;
        }
    }

    private void showDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_details, null);
        firstname = (EditText)view.findViewById(R.id.dialog_change_deatils_firstname);
        lastname = (EditText)view.findViewById(R.id.dialog_change_deatils_lastname);
        email = (EditText) view.findViewById(R.id.dialog_change_deatils_email);
        phoneno = (EditText) view.findViewById(R.id.dialog_change_deatils_phoneno);
        houseno = (EditText) view.findViewById(R.id.dialog_change_deatils_houseno);
        tv_message = (TextView)view.findViewById(R.id.dialog_change_deatils_message);
        progress = (ProgressBar)view.findViewById(R.id.dialog_change_deatils_progress);
        builder.setView(view);
        builder.setTitle("Change User Details");
        builder.setPositiveButton("Change details", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ftname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String uemail = email.getText().toString();
                String phone = phoneno.getText().toString();
                String hseno = houseno.getText().toString();
                if(!ftname.isEmpty() && !lname.isEmpty()&&!uemail.isEmpty()&&!phone.isEmpty()&&!hseno.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
//                    changePasswordProcess(pref.getString(Constants.EMAIL,""),old_password,new_password);
                    changeDetails(pref.getString(Constants.EMAIL,""),ftname,lname,uemail,phone,hseno);
                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changeDetails(String unqid,String fname,String lname,String mail,String phone, String hseno ){

    }

    private void goToLogin(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_container,new LoginFragment());
        ft.commit();
    }

    private void changePasswordProcess(final String pnumber, final String new_password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CPASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String update=jsonObject.getString("update");
                            String message=jsonObject.getString("message");
                            if (update.equals("update_failed")){
                              //btn_register.setEnabled(true);
                                dialog.hide();
                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            }else if(update.equals("update_success")){
                                Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                            }

                            Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        //Log.d("Error","volley error");
                        Snackbar.make(getView(), error.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_NUMBER, pnumber);
                params.put(KEY_PASSWORD, new_password);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

