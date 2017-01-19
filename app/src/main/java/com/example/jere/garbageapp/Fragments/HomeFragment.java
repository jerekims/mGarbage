package com.example.jere.garbageapp.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.jere.garbageapp.Adapters.EventsViewAdapter;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.Constants;
import com.example.jere.garbageapp.libraries.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    List<Events> GetEvents;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    TextView txt;
    FloatingActionButton fab,part;
    SwipeRefreshLayout swipeRefreshLayout;


    String JSON_EVENT_ID = "event_id";
    String JSON_EVENT_NAME = "event_name";
    String JSON_DESC = "event_description";
    String JSON_VENUE = "venue";
    String JSON_DATE="event_date";

    //String JSON_PHONE_NUMBER = "phone_number";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_home, container, false);
        myrecycler(view);
        return view;
    }

    public void myrecycler(View view){
        GetEvents = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_home_progressbar);
        fab=(FloatingActionButton)view.findViewById(R.id.fragment_home_events_row_fab);
        part=(FloatingActionButton)view.findViewById(R.id.fragment_home_events_row_fab_participate);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(true);
        JSON_DATA_WEB_CALL();
        progressBar.setVisibility(View.VISIBLE);

    }

    public void JSON_DATA_WEB_CALL(){
            jsonArrayRequest = new JsonArrayRequest(Constants.GET_EVENTS,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            progressBar.setVisibility(View.GONE);

                            JSON_PARSE_DATA_AFTER_WEBCALL(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.GONE);
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
                    });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

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
            GetEvents.add(myevents);
        }

        recyclerViewadapter = new EventsViewAdapter(GetEvents,getActivity());

        recyclerView.setAdapter(recyclerViewadapter);
    }

}
