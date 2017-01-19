package com.example.jere.garbageapp.libraries;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jere on 11/18/2016.
 */

    public interface RequestInterface {

        @POST("garbage/")
        Call<ServerResponse> operation(@Body ServerRequest request);

    }

