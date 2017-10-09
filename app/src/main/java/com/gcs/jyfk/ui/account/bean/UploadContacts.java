package com.gcs.jyfk.ui.account.bean;

import com.gcs.jyfk.ui.bean.ContactBean;

import java.util.List;

/**
 * Created by Administrator on 0022 9-22.
 */

public class UploadContacts {
    public UploadContacts(String token, List<ContactBean> contacts) {
        this.token = token;
        this.contacts = contacts;
    }

    private String token;
    private List<ContactBean> contacts;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ContactBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactBean> contacts) {
        this.contacts = contacts;
    }


}
