package com.example.retailrevamp.Model;

import org.json.JSONArray;

import java.util.UUID;

public class KhataModel {
    UUID khataUserId;
    String khataSerialNumber;
    String khataUserName;
    UUID khataUserImage;
    String khataUserPhone;
    JSONArray khataTransactions;

    public String getKhataSerialNumber() {
        return khataSerialNumber;
    }

    public void setKhataSerialNumber(String khataSerialNumber) {
        this.khataSerialNumber = khataSerialNumber;
    }

    public UUID getKhataUserId() {
        return khataUserId;
    }

    public void setKhataUserId(UUID khataUserId) {
        this.khataUserId = khataUserId;
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
}
