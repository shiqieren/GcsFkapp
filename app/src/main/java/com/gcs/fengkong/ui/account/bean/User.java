package com.gcs.fengkong.ui.account.bean;

import java.io.Serializable;

/**
 * 登录用户
 * Created by lyw on 2017/8/8.
 */

public class User implements Serializable {
    protected long id;
    protected String name;
    // More
    private String desc;
    // 本地缓存多余信息
    private String cookie;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
