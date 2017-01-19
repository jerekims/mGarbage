package com.example.jere.garbageapp.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.jere.garbageapp.Adapters.MyeventsAdapter;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.Constants;
import com.example.jere.garbageapp.libraries.Events;

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
public class MyEventsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    List<Events> MyEvents;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    TextView txt;
    FloatingActionButton fab,part;

    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;

    String JSON_EVENT_ID = "evt_id";
    String JSON_EVENT_NAME = "evt_name";
    String JSON_DESC = "evt_desc";
    String JSON_VENUE = "evt_venue";
    String JSON_DATE="event_date";

    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        intialize(view);
        return view;
    }

    private void intialize(View view) {
        MyEvents = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_my_events_recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_my_events_progressbar);
        fab=(FloatingActionButton)view.findViewById(R.id.fragment_my_events_row_fab);
        part=(FloatingActionButton)view.findViewById(R.id.fragment_my_events_row_fab_unsubscribe);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(true);
        JSON_DATA_WEB_CALL();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void JSON_DATA_WEB_CALL(){
        //String MYEVENTS = "http://savtech.co.ke/garb/myevents.php?user_id=0702179556";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MYEVENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(GONE);
                        JSONObject jsonobject= null;
                        try {
                            jsonobject = new JSONObject(response);
                            JSON_PARSE_DATA_AFTER_WEBCALL(jsonobject);
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
            String event=object.getString("event");
            if(event.equals("no_events")){
                String mem=object.getString("myevents");
                Snackbar.make(getView(),mem,Snackbar.LENGTH_LONG).show();
            }
            else if(event.equals("events")) {
                JSONArray array= new JSONArray(object.getString("myevents"));
                for(int i = 0; i<array.length(); i++) {
                    Events myevents = new Events();
                    JSONObject json = null;
                    try {
                        json = array.getJSONObject(i);
                        myevents.setEvent_id(json.getInt(JSON_EVENT_ID));
                        myevents.setEvent_name(json.getString(JSON_EVENT_NAME));
                        myevents.setEvent_description(json.getString(JSON_DESC));
                        myevents.setVenue(json.getString(JSON_VENUE));
                        myevents.setEvent_date(json.getString(JSON_DATE));

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    MyEvents.add(myevents);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerViewadapter = new MyeventsAdapter(MyEvents,getContext());

        recyclerView.setAdapter(recyclerViewadapter);
    }
}