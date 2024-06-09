package com.example.retailrevamp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.retailrevamp.Model.KhataModel;
import com.example.retailrevamp.Model.TransactionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreference {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_TRANSACTION_LIST = "transactionList";
    private static final String KEY_COLLECTION_LIST = "collectionList";
    private static final String KEY_PAYMENT_LIST = "paymentList";
    private static final String KEY_SALES_LIST = "salesList";
    private static final String KEY_KHATA_ENTRY_LIST = "khataEntryList";
    private static final String KEY_NEW_KHATA_LIST = "newKhataList";
    private static final String KEY_SPREADSHEET_ID = "spreadSheetId";
    Context mContext;
    static SharedPreferences sharedPreferences;
    public static String spreadSheetId;


    public SharedPreference(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static void saveUserSession(String spreadSheetId ){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SPREADSHEET_ID, spreadSheetId);
        editor.apply();
    }
    public static void getUserSession(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        spreadSheetId = sharedPreferences.getString(KEY_SPREADSHEET_ID, null);
        editor.apply();
    }
    public static void clearUserSession(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SPREADSHEET_ID);
        editor.apply();
    }

    public static void saveLists( List<TransactionModel> transactionList, List<TransactionModel> collectionList, List<TransactionModel> paymentList, List<TransactionModel> salesList,List<TransactionModel> khataEntryList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonTransaction = gson.toJson(transactionList);
        String jsonCollection = gson.toJson(collectionList);
        String jsonPayment = gson.toJson(paymentList);
        String jsonSales = gson.toJson(salesList);
        String jsonKhataEntry = gson.toJson(khataEntryList);
        editor.putString(KEY_TRANSACTION_LIST, jsonTransaction);
        editor.putString(KEY_COLLECTION_LIST, jsonCollection);
        editor.putString(KEY_PAYMENT_LIST, jsonPayment);
        editor.putString(KEY_SALES_LIST, jsonSales);
        editor.putString(KEY_KHATA_ENTRY_LIST, jsonKhataEntry);
        editor.apply();
    }
    public static List<TransactionModel> getKhataEntryList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_KHATA_ENTRY_LIST, null);
        Type type = new TypeToken<ArrayList<TransactionModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static List<TransactionModel> getTransactionList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_TRANSACTION_LIST, null);
        Type type = new TypeToken<ArrayList<TransactionModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static List<TransactionModel> getCollectionList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_COLLECTION_LIST, null);
        Type type = new TypeToken<ArrayList<TransactionModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static List<TransactionModel> getPaymentList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_PAYMENT_LIST, null);
        Type type = new TypeToken<ArrayList<TransactionModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static List<TransactionModel> getSalesList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_SALES_LIST, null);
        Type type = new TypeToken<ArrayList<TransactionModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void savenNewKhataLists( List<KhataModel> newKhataList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonNewKhataList = gson.toJson(newKhataList);
        editor.putString(KEY_NEW_KHATA_LIST, jsonNewKhataList);
        editor.apply();
    }
    public static List<KhataModel> getNewKhataList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_NEW_KHATA_LIST, null);
        Type type = new TypeToken<ArrayList<KhataModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static void clearNewKhataList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_NEW_KHATA_LIST);
        editor.apply();
    }
    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Apply changes
    }
    public static void clearLists() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TRANSACTION_LIST);
        editor.remove(KEY_COLLECTION_LIST);
        editor.remove(KEY_PAYMENT_LIST);
        editor.remove(KEY_SALES_LIST);
        editor.remove(KEY_KHATA_ENTRY_LIST);
        editor.apply();
    }
}
