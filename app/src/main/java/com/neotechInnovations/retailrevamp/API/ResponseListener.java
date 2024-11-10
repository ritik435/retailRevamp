package com.neotechInnovations.retailrevamp.API;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;

//public interface ResponseListener<T> {
//    void onSuccess(T response);
//    void onFailed(T response);
//    void onRequestFailure(Throwable t);
//}

public interface ResponseListener {
    public void onSuccess(ResponseBody responseBody) throws IOException, JSONException;

    public void onFailure(ResponseBody responseBody) throws IOException, JSONException;

    public void onRequestFailed(String message);
}
