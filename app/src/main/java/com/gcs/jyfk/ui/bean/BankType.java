package com.gcs.jyfk.ui.bean;

/**
 * Created by Administrator on 0017 10-17.
 */

public class BankType {

    /**
     * id : 3
     * bankCode : ICBC
     * bankName : 中国工商银行
     */

    private int id;
    private String bankCode;
    private String bankName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
