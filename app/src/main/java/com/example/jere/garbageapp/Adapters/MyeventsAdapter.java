package com.example.jere.garbageapp.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jere.garbageapp.Fragments.MyEventsFragment;
import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.app.AppController;
import com.example.jere.garbageapp.libraries.Constants;
import com.example.jere.garbageapp.libraries.Events;
import com.example.jere.garbageapp.libraries.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jere on 1/10/2017.
 */

public class MyeventsAdapter extends RecyclerView.Adapter<MyeventsAdapter.ViewHolder>  {

    Context context;
    private SessionManager sessionManager;

    List<Events> getDataAdapter;

    public MyeventsAdapter(List<Events> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public MyeventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_events_row, parent, false);

        MyeventsAdapter.ViewHolder viewHolder = new MyeventsAdapter.ViewHolder(v);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final MyeventsAdapter.ViewHolder holder, final int position) {

        final Events event =  getDataAdapter.get(position);
        holder.evtname.setText(event.getEvent_name());
        holder.evtdesc.setText(event.getEvent_description());
        holder.evtvenue.setText(event.getVenue());

        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder dataString = new StringBuilder();
                String e_name=event.getEvent_name();
                String e_desc=event.getEvent_description();
                String e_venue=event.getVenue();

                dataString.append(" Event Description : " + e_desc + "\n\n");
                dataString.append(" Event venue : " + e_venue);


                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, "Event Name : " + e_name);
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {"recipient@example.com"});
                i.putExtra(Intent.EXTRA_TEXT, dataString.toString());

                try{

                    context.startActivity(Intent.createChooser(i, "Send mail..."));

                }catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Please install email client before sending",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.Unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("UnSubscribe.")
                        .setMessage("Would you like to unsubscribe from participating "+event.getEvent_name()+" event?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UNSUBSCRIBE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                                    String code=jsonObject.getString("code");
                                                    String message=jsonObject.getString("message");
                                                    if(code.equals("unsub_success")){
                                                       Toast.makeText(context, message +" "+event.getEvent_name(), Toast.LENGTH_SHORT).show();
                                                        Intent intent= new Intent(context, MyEventsFragment.class);
                                                        context.startActivity(intent);

                                                    }
                                                    else if(code.equals("unsub_failed")){
                                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //Log.d("Error","volley error");
                                                Toast.makeText(context, "Error occurred while connecting", Toast.LENGTH_SHORT).show();
                                            }
                                        }) {

                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        String user_id=sessionManager.getNumber();
                                        params.put(Constants.KEY_ID, user_id);
                                        params.put("event_id", String.valueOf(event.getEvent_id()));
                                        return params;
                                    }

                                };
                                AppController.getInstance().addToRequestQueue(stringRequest);

                            }

                        })
                        .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.unsub)
                        .show();
            }
        });
    }


    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView evtname;
        public TextView evtdesc;
        public TextView evtvenue;
        public DatePicker evtdate;
        public FloatingActionButton fab, Unsubscribe;

        public ViewHolder(View itemView) {

            super(itemView);
            evtname= (TextView) itemView.findViewById(R.id.fragment_my_events_event_name) ;
            evtdesc = (TextView) itemView.findViewById(R.id.fragment_my_events_row_event_description) ;
            evtvenue= (TextView) itemView.findViewById(R.id.fragment_my_event_row_venue_name) ;
            fab=(FloatingActionButton)itemView.findViewById(R.id.fragment_my_events_row_fab);
            Unsubscribe =(FloatingActionButton)itemView.findViewById(R.id.fragment_my_events_row_fab_unsubscribe);
            evtdate = (DatePicker) itemView.findViewById(R.id.fragment_my_events_row_event_date) ;

        }
    }
}
