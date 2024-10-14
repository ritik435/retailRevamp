package com.neotechInnovations.retailrevamp.Model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.UUID;

public class KhataModel implements Serializable {
    UUID khataUserId;
    String khataUserIdString;
    String khataSerialNumber;
    String khataUserName;
    UUID khataUserImage;
    int khataBalance;
    String khataUserPhone;
    JSONArray khataTransactions;

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
