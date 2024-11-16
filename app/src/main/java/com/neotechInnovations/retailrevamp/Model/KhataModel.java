package com.neotechInnovations.retailrevamp.Model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KhataModel implements Serializable {
//    UUID khataUserId;
    String khataUserIdString;
    String khataSerialNumber;
    String khataUserName;
    UUID khataUserImage;
    int khataBalance;
    String khataUserPhone;
    JSONArray khataTransactions;
    String userId;
    boolean edited;
    boolean deleted;
    boolean backedUp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isBackedUp() {
        return backedUp;
    }

    public void setBackedUp(boolean backedUp) {
        this.backedUp = backedUp;
    }

    public int getKhataBalance() {
        return khataBalance;
    }

    public void setKhataBalance(int khataBalance) {
        this.khataBalance = khataBalance;
    }

    public String getKhataUserIdString() {
        return khataUserIdString;
    }

    public void setKhataUserIdString(String khataUserIdString) {
        this.khataUserIdString = khataUserIdString;
    }

    public String getKhataSerialNumber() {
        return khataSerialNumber;
    }

    public void setKhataSerialNumber(String khataSerialNumber) {
        this.khataSerialNumber = khataSerialNumber;
    }
    public String getKhataUserName() {
        return khataUserName;
    }

    public void setKhataUserName(String khataUserName) {
        this.khataUserName = khataUserName;
    }

    public UUID getKhataUserImage() {
        return khataUserImage;
    }

    public void setKhataUserImage(UUID khataUserImage) {
        this.khataUserImage = khataUserImage;
    }

    public String getKhataUserPhone() {
        return khataUserPhone;
    }

    public void setKhataUserPhone(String khataUserPhone) {
        this.khataUserPhone = khataUserPhone;
    }

    public JSONArray getKhataTransactions() {
        return khataTransactions;
    }

    public void setKhataTransactions(JSONArray khataTransactions) {
        this.khataTransactions = khataTransactions;
    }


    public static KhataModel khataJSONToKhataModel(JSONObject khataItem) throws IOException, JSONException {
        return new Gson().fromJson(String.valueOf(khataItem), KhataModel.class);
    }
    public static List<KhataModel> khataResponseToKhataModelList(JSONArray khataArray) throws IOException, JSONException {
        List<KhataModel> khataModelList = new ArrayList<>();
        Gson gson = new Gson();

        for (int i = 0; i < khataArray.length(); i++) {
            JSONObject transactionObject = khataArray.getJSONObject(i);
            KhataModel khataModel = gson.fromJson(transactionObject.toString(), KhataModel.class);
            khataModelList.add(khataModel);
        }

        return khataModelList;
    }
}
