package com.gcs.jyfk.ui.account.bean;

import java.io.Serializable;

/**
 * 登录用户
 * Created by lyw on 2017/8/8.
 */

public class User implements Serializable {
    protected long id;
    private String phone;
    private String status;
    private String createtime;
    private String updatetime;
    private String certno;
    protected String name;
    protected String userid;
    // 本地缓存多余信息
    private String cookie;
    private String token;
    private More more;
    private AuthState authstate;

    public User() {
        //this(0,"","","","","","","");
        more = new More();
        authstate = new AuthState();
    }

    public User(long id, String phone, String status, String createtime, String updatetime, String certno, String cookie, String token) {
        this.id = id;
        this.phone = phone;
        this.status = status;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.certno = certno;
        this.cookie = cookie;
        this.token = token;
    }


    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(long id, String name, String cookie, String token, More more, AuthState authstate) {
        this.id = id;
        this.name = name;
        this.cookie = cookie;
        this.token = token;
        this.more = more;
        this.authstate = authstate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public AuthState getAuthstate() {
        return authstate;
    }

    public void setAuthstate(AuthState authstate) {
        this.authstate = authstate;
    }

    public static class More implements Serializable {
        public More() {
        }

        public More(String imei, String imsi, String ip, String mac, String address, String platform) {
            this.imei = imei;
            this.imsi = imsi;
            this.ip = ip;
            this.mac = mac;
            this.address = address;
            this.platform = platform;
        }

        private String imei;
        private String imsi;
        private String ip;
        private String mac;
        private String address;
        private String platform;


        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
        public String getImsi() {
            return address;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }
        public String getIpnum() {
            return ip;
        }

        public void setIpnum(String ip) {
            this.ip = ip;
        }
        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }

    public static class AuthState implements Serializable {
        public AuthState() {
        }

        public AuthState(String auth_identity, String auth_bankcard, String auth_zhima, String auth_alipay, String auth_taobao, String auth_jd, String auth_operator, String auth_contact, String auth_drivercard, String auth_creditcard) {
            this.auth_identity = auth_identity;
            this.auth_bankcard = auth_bankcard;
            this.auth_zhima = auth_zhima;
            this.auth_alipay = auth_alipay;
            this.auth_taobao = auth_taobao;
            this.auth_jd = auth_jd;
            this.auth_operator = auth_operator;
            this.auth_contact = auth_contact;
            this.auth_drivercard = auth_drivercard;
            this.auth_creditcard = auth_creditcard;
        }

        private String auth_identity;
        private String auth_bankcard;
        private String auth_zhima;
        private String auth_alipay;
        private String auth_taobao;
        private String auth_jd;
        private String auth_operator;
        private String auth_contact;
        private String auth_drivercard;
        private String auth_creditcard;


        public String getAuth_identity() {
            return auth_identity;
        }

        public void setAuth_identity(String auth_identity) {
            this.auth_identity = auth_identity;
        }

        public String getAuth_bankcard() {
            return auth_bankcard;
        }

        public void setAuth_bankcard(String setAuth_bankcard) {
            this.auth_bankcard = setAuth_bankcard;
        }
        public String getAuth_zhima() {
            return auth_zhima;
        }

        public void setAuth_zhima(String zhima) {
            this.auth_zhima = zhima;
        }
        public String getAuth_alipay() {
            return auth_alipay;
        }

        public void setAuth_alipay(String alipay) {
            this.auth_alipay = alipay;
        }
        public String getAuth_taobao() {
            return auth_taobao;
        }

        public void setAuth_taobao(String taobao) {
            this.auth_taobao = taobao;
        }
        public String getAuth_jd() {
            return auth_jd;
        }

        public void setAuth_jd(String jd) {
            this.auth_jd = jd;
        }
        public String getAuth_operator() {
            return auth_operator;
        }

        public void setAuth_operator(String operator) {
            this.auth_operator = operator;
        }
        public String getAuth_contact() {
            return auth_contact;
        }

        public void setAuth_contact(String contact) {
            this.auth_contact = contact;
        }
        public String getAuth_drivercard() {
            return auth_drivercard;
        }

        public void setAuth_drivercard(String auth_drivercard) {
            this.auth_drivercard = auth_drivercard;
        }
        public String getAuth_creditcard() {
            return auth_creditcard;
        }

        public void setAuth_creditcard(String auth_creditcard) {
            this.auth_creditcard = auth_creditcard;
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name +
                ", token='" + token +
                ", cookie='" + cookie  + '\'' +
                '}';
    }

}
