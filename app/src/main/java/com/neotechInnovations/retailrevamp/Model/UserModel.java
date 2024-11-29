package com.neotechInnovations.retailrevamp.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.ResponseBody;

public class UserModel implements Serializable {
    private static final String TAG = "UserModel";
    @SerializedName("_id")
    String id;
    String name;
    String businessName;
    String email;
    String password;
    String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static UserModel userResponseToUserModel(ResponseBody response){
        UserModel user=null;
        try {
            // Parse the response body into the User model
            user = new Gson().fromJson(response.string(), UserModel.class);
            Log.d(TAG, "userResponseToUserModel: completed : "+user);
//            RoomAndPollModel roomAndPollModel = new Gson().fromJson(String.valueOf(roomItems.get(i)), RoomAndPollModel.class);
        }catch (IOException e) {
            Log.e(TAG, "userResponseToUserModel: ", e);
        }
        return user;
    }
    public static UserModel userJSONToUserModel(JSONObject userItem) throws IOException, JSONException {
        return new Gson().fromJson(String.valueOf(userItem), UserModel.class);
    }
}
