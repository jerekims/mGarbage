package com.example.jere.garbageapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.libraries.ComplainResponses;
import com.example.jere.garbageapp.libraries.SessionManager;

import java.util.List;


/**
 * Created by jere on 1/10/2017.
 */

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder>  {

    Context context;


    List<ComplainResponses> getDataAdapter;

    public ResponseAdapter(List<ComplainResponses> getDataAdapter, Context context){

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;

    }

    @Override
    public ResponseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_response_row, parent, false);

        ResponseAdapter.ViewHolder viewHolder = new ResponseAdapter.ViewHolder(v);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ResponseAdapter.ViewHolder holder, final int position) {

        final ComplainResponses resp =  getDataAdapter.get(position);
        holder.complaintdescription.setText(resp.getComplaintdescription());
        holder.complaindate.setText(resp.getDate());
        holder.response.setText(resp.getResponse());
    }


    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView complaintdescription;
        public TextView complaindate;
        public TextView response;

        public ViewHolder(View itemView) {
            super(itemView);
            complaintdescription= (TextView) itemView.findViewById(R.id.complain_response_row_complain_description) ;
            complaindate = (TextView) itemView.findViewById(R.id.complain_response_row_complain_date) ;
            response= (TextView) itemView.findViewById(R.id.complain_response_row_complain_response_message) ;

        }
    }
}
