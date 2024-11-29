package com.neotechInnovations.retailrevamp.API;
// RevampRetrofit.java

import android.util.Log;

import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevampRetrofit {
    private static final String TAG = "RevampRetrofit";
    private ApiService apiService;

    // Constructor to initialize Retrofit
    public RevampRetrofit() {
        String baseUrl = "https://retail-revamp-backend.vercel.app/api/"; // Update to your base URL
        apiService = RetrofitClient.getClient(baseUrl).create(ApiService.class);
    }

    public void postData(String url, HashMap<String, Object> data, ResponseListener responseListner) {
        Call<ResponseBody> call = apiService.postData(url, data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: called");
                if (!response.isSuccessful()) {
                    try {
//                    if(response.code() == Tags.STATUS_CODE_503){
                        responseListner.onFailure(response.body());
//                    }
//                    else {
//                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
//                    }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: postData response :: "+response.toString(), e);
                    }
                } else {
                    Log.d(TAG, "onResponse: response first " + response);
                    try {
                        responseListner.onSuccess(response.body());
                        Log.d(TAG, "onResponse: reponse got" + response.body().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: 2" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                responseListner.onRequestFailed(t.getMessage());
            }
        });
    }
    public void deleteData(String url, HashMap<String, Object> data, ResponseListener responseListner) {
        Call<ResponseBody> call = apiService.deleteData(url, data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: called");
                if (!response.isSuccessful()) {
                    try {
//                    if(response.code() == Tags.STATUS_CODE_503){
                        responseListner.onFailure(response.body());
//                    }
//                    else {
//                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
//                    }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: postData response :: "+response.toString(), e);
                    }
                } else {
                    Log.d(TAG, "onResponse: response first " + response);
                    try {
                        responseListner.onSuccess(response.body());
                        Log.d(TAG, "onResponse: reponse got" + response.body().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: 2" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                responseListner.onRequestFailed(t.getMessage());
            }
        });
    }
//    public void deleteKhatas(String url, HashMap<String, Object> data, ResponseListener responseListner) {
//        Call<ResponseBody> call = apiService.deleteData(url, data);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: called");
//                if (!response.isSuccessful()) {
//                    try {
////                    if(response.code() == Tags.STATUS_CODE_503){
//                        responseListner.onFailure(response.body());
////                    }
////                    else {
////                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
////                    }
//                    } catch (IOException | JSONException e) {
//                        Log.e(TAG, "onResponse: postData response :: "+response.toString(), e);
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: response first " + response);
//                    try {
//                        responseListner.onSuccess(response.body());
//                        Log.d(TAG, "onResponse: reponse got" + response.body().string());
//                    } catch (IOException e) {
//                        Log.d(TAG, "onResponse: " + e.getMessage());
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        Log.d(TAG, "onResponse: 2" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//                responseListner.onRequestFailed(t.getMessage());
//            }
//        });
//    }

    public void postDataTransaction(String url, TransactionModel transactionModel, ResponseListener responseListner) {
        Call<ResponseBody> call = apiService.postData(url, transactionModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: called 000 ,,,");
                if (!response.isSuccessful()) {
                    try {
//                    if(response.code() == Tags.STATUS_CODE_503){
                        responseListner.onFailure(response.body());
//                    }
//                    else {
//                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
//                    }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: postData response :: "+response.toString(), e);
                    }
                } else {
                    Log.d(TAG, "onResponse: 111: " + response);
                    try {
                        responseListner.onSuccess(response.body());
                        Log.d(TAG, "onResponse: reponse got 222: " + response.body().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: 333: " + e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: 2" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                responseListner.onRequestFailed(t.getMessage());
            }
        });
    }
    public void postDataKhata(String url, KhataModel khataModel, ResponseListener responseListner) {
        Call<ResponseBody> call = apiService.postData(url, khataModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: called 000 ,,,");
                if (!response.isSuccessful()) {
                    try {
//                    if(response.code() == Tags.STATUS_CODE_503){
                        responseListner.onFailure(response.body());
//                    }
//                    else {
//                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
//                    }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: postData response :: "+response.toString(), e);
                    }
                } else {
                    Log.d(TAG, "onResponse: 111: " + response);
                    try {
                        responseListner.onSuccess(response.body());
                        Log.d(TAG, "onResponse: reponse got 222: " + response.body().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: 333: " + e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: 2" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                responseListner.onRequestFailed(t.getMessage());
            }
        });
    }
    // Method to make an API call with a response listener
//    public void postData(String url, HashMap<String, Object> data, ResponseListener responseListner) {
//        Call<ResponseBody> call = apiService.postData(url, data);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: called");
//                if (!response.isSuccessful()) {
////                    if(response.code() == Tags.STATUS_CODE_503){
//                        responseListner.onFailure(response.body());
////                    }
////                    else {
////                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
////                    }
//                }
//                else {
//                    Log.d(TAG, "onResponse: response first " + response);
//                    try {
//                        responseListner.onSuccess(response.body());
//                    } catch (IOException e) {
//                        Log.d(TAG, "onResponse: " + e.getMessage());
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        Log.d(TAG, "onResponse: 2" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//                responseListner.onRequestFailed(t.getMessage());
//            }
//        });
//    }
//    public void getDataWithBody(String url, Object data, ResponseListener<Object> listener) {
//        Call<Object> call = apiService.getDataWithBody(url, data);
//
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    listener.onSuccess(response.body);
//                } else {
//                    listener.onFailed("Error: " + response.code() + " : "+response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                listener.onRequestFailure(t);
//            }
//        });
//    }
//    public void getData(String url, Map<String, Object> params, ResponseListener listener) {
//        Call<Object> call = apiService.getData(url, params);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: called");
//                if (!response.isSuccessful()) {
////                    if(response.code() == Tags.STATUS_CODE_503){
//                    listener.onFailure(response.body());
////                    }
////                    else {
////                        responseListner.onRequestFailed(Tags.TRY_AGAIN);
////                    }
//                } else {
//                    Log.d(TAG, "onResponse: response first " + response);
//                    try {
//                        listener.onSuccess(response.body());
//                    } catch (IOException e) {
//                        Log.d(TAG, "onResponse: " + e.getMessage());
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        Log.d(TAG, "onResponse: 2" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//                responseListner.onRequestFailed(t.getMessage());
//            }
//        });    }

    public void getData(String url, HashMap<String, Object> data, ResponseListener responseListner) {
        Call<ResponseBody> call = apiService.getData(url, data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: called : "+response+" : "+response.headers());
                if (!response.isSuccessful()) {
                    try {
                    if(response.code() == 400) {
                        Log.d(TAG, "onResponse: response : " + response.errorBody() + " :11233:  " + response.errorBody().toString());
                        responseListner.onFailure(response.errorBody());
                    }else{
                        // Read the error message sent by the backend
                        String errorMessage = response.errorBody().string();
                        Log.d(TAG,"Error: " + errorMessage);
                        // Pass the error message to the listener or handle accordingly
                        responseListner.onRequestFailed(errorMessage);
                    }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: getData response :: "+response.toString(), e);
                    }
//                    } else if (response.code() == Tags.STATUS_CODE_400) {
//                        responseListner.onRequestFailed(Tags.UPI_ALREADY_EXIST);
//                    } else {
//                        responseListner.onRequestFailed("Request was unsuccesfull");
//                    }

                } else {
                    Log.d(TAG, "onResponse: response first " + response.body());
                    try {
                        responseListner.onSuccess(response.body());
                        Log.d(TAG, "onResponse: reponse got" + response.body().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: 2" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                responseListner.onRequestFailed("Request Failed");
            }
        });
    }
}

