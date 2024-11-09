package com.neotechInnovations.retailrevamp.API;

import com.neotechInnovations.retailrevamp.Model.TransactionModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    Call<Object> postData1(@Url String url, @Body Object data);  // Replace `Object` with your data model class
    @GET
    Call<Object> getDataWithBody(@Url String url, @Body Object data);  // Replace `Object` with your data model class
    @GET()
    Call<ResponseBody> getData(@Url String url, @QueryMap Map<String, Object> data);

    @POST()
    Call<ResponseBody> postData(@Url String url, @Body HashMap<String, Object> hashMap);
    @POST()
    Call<ResponseBody> postData(@Url String url, @Body TransactionModel transactionModel);

}
