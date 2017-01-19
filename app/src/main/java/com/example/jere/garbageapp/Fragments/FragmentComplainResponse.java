package com.example.jere.garbageapp.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.jere.garbageapp.Adapters.ResponseAdapter;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.ComplainResponses;
import com.example.jere.garbageapp.libraries.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComplainResponse extends BaseFragment {

    private RecyclerView recyclerView;
    List<ComplainResponses> Getresponses;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;

    String JSON_COMPLAIN_DATE = "complain_date";
    String JSON_DESC = "complian_description";
    String JSON_RESPONSE = "complain_response";

    JsonArrayRequest jsonArrayRequest ;

    public FragmentComplainResponse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment__complain__response, container, false);
        setupinitialize(view);
        return  view;
    }

    private void setupinitialize(View view) {
        Getresponses = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_complain_response);
        progressBar=(ProgressBar)view.findViewById(R.id.fragment_complain_response_progressbar);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(true);
        JSON_DATA_WEB_CALL();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void JSON_DATA_WEB_CALL() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.RESPONSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(GONE);
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSON_PARSE_DATA_AFTER_WEBCALL(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(GONE);
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
                    String user_id="0702179556";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.KEY_NUMBER, user_id);
                    return params;
                }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONObject object){

        try {
            String complain=object.getString("complain");
            if(complain.equals("no_responses")){
                String message =object.getString("message");
                Snackbar.make(getView(),message,Snackbar.LENGTH_LONG).show();
            }
            else if(complain.equals("responses")){

                JSONArray array= new JSONArray(object.getString("myresponses"));
                for(int i = 0; i<array.length(); i++) {

                    ComplainResponses myresponse = new ComplainResponses();

                    JSONObject json = null;
                    try {
                        json = array.getJSONObject(i);
                        myresponse.setDate(json.getString(JSON_COMPLAIN_DATE));
                        myresponse.setComplaintdescription(json.getString(JSON_DESC));
                        myresponse.setResponse(json.getString(JSON_RESPONSE));

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    Getresponses.add(myresponse);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerViewadapter = new ResponseAdapter(Getresponses,getContext());

        recyclerView.setAdapter(recyclerViewadapter);
    }


}
