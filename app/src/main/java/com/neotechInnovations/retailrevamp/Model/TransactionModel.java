package com.neotechInnovations.retailrevamp.Model;

import java.sql.Timestamp;
import java.util.UUID;

public class TransactionModel {
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


}
