package com.gcs.fengkong.ui.bean;

/**
 * Created by Administrator on 0031 8-31.
 */

public class ContactBean {
    public ContactBean() {
    }

    public ContactBean(long id, String name, String company, String phone, String mail) {

        this.name = name;
        this.company = company;
        this.phone = phone;
        this.mail = mail;
    }

    /**
     * name : 张飞
     * company : 三国公司
     * phone : 13026608636
     * mail : dengfeng_wan@163.com
     */

    private String name;
    private String company;
    private String phone;
    private String mail;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
