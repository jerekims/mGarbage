package com.example.jere.garbageapp.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jere on 12/2/2016.
 */

public class BaseFragment extends android.support.v4.app.Fragment {
    public boolean netstate=false;
    protected boolean isNetworkConnected() {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo!= null && mNetworkInfo.isConnected()){
                netstate=true;
            }else {
                netstate=false;

            }
        return netstate;
    }

}
