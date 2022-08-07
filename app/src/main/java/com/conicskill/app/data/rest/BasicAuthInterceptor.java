package com.conicskill.app.data.rest;


import android.util.Log;

import com.conicskill.app.util.TinyDB;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author vikas@cprep.in
 * @use This class is used to intercept the response of the request made to the server
 */
@Singleton
public class BasicAuthInterceptor implements Interceptor {

    private String credentials;
    private String user;
    private String password;
    private TinyDB tinyDB;

    @Inject
    public BasicAuthInterceptor(TinyDB tinyDB) {
        this.tinyDB = tinyDB;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
//        this.credentials = Credentials.basic(user, password);

        Request authenticatedRequest = request.newBuilder()
//                .header("Authorization", credentials)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();

        Response response = chain.proceed(authenticatedRequest);
        Log.e("Test", "intercept: " + response.body().toString());
        return response;
    }

}

