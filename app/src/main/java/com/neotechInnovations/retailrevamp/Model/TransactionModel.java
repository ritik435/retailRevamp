package com.neotechInnovations.retailrevamp.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;

public class TransactionModel {
    private static final String TAG = "TransactionModel";
    UUID transactionId;
    String userId;
    String userName;
    Timestamp date;
    Integer balance;
    Integer totalAmount;
    Integer amountTransferred;
    String mode;
    String paymentType;
    Boolean isTransaction;
    String key;
    UUID khataId;
    String khataNumber;
    boolean deleted;
    boolean edited;
    boolean backedUp;

    public UUID getId() {
        return transactionId;
    }

    public void setId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isBackedUp() {
        return backedUp;
    }

    public void setBackedUp(boolean backedUp) {
        this.backedUp = backedUp;
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

    public String getKhataNumber() {
        return khataNumber;
    }

    public void setKhataNumber(String khataNumber) {
        this.khataNumber = khataNumber;
    }

    public UUID getKhataId() {
        return khataId;
    }

    public void setKhataId(UUID khataId) {
        this.khataId = khataId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getTransaction() {
        return isTransaction;
    }

    public void setTransaction(Boolean transaction) {
        isTransaction = transaction;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(Integer amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static TransactionModel transactionResponseToTransactionModel(ResponseBody response) {
        TransactionModel transaction = null;
        try {
            // Parse the response body into the User model
            transaction = new Gson().fromJson(response.string(), TransactionModel.class);
            Log.d(TAG, "transactionResponseToTransactionModel: " + transaction);
//            RoomAndPollModel roomAndPollModel = new Gson().fromJson(String.valueOf(roomItems.get(i)), RoomAndPollModel.class);
        } catch (IOException e) {
            Log.e(TAG, "userResponseToUserModel: ", e);
        }
        return transaction;
    }

    public static TransactionModel transactionJSONToTransactionModel(JSONObject transactionItem) throws IOException, JSONException {
        TransactionModel transactionModel = new TransactionModel();
        try {
            transactionModel = new Gson().fromJson(String.valueOf(transactionItem), TransactionModel.class);
        } catch (Exception e) {
            Log.d(TAG, "transactionJSONToTransactionModel: Error inn : " + e.getMessage());
            e.printStackTrace();
        }
        return transactionModel;
    }

    public static List<TransactionModel> transactionResponseToTransactionModelList(JSONArray transactionArray) throws IOException, JSONException {
        List<TransactionModel> transactionList = new ArrayList<>();
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // Use the exact date format from your JSON
                .create();
        for (int i = 0; i < transactionArray.length(); i++) {
            JSONObject transactionObject = transactionArray.getJSONObject(i);
            TransactionModel transaction = gson.fromJson(transactionObject.toString(), TransactionModel.class);
            transactionList.add(transaction);
        }

        return transactionList;
    }
}
