package com.gcs.fengkong.ui.account.bean;

import java.io.Serializable;

/**
 * 登录用户
 * Created by lyw on 2017/8/8.
 */

public class User implements Serializable {
    protected long id;
    protected String name;
    // 本地缓存多余信息
    private String cookie;
    private String token;

    private More more;
    private AuthState authstate;

    public User() {
        more = new More();
        authstate = new AuthState();
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


    }

    public static class AuthState implements Serializable {
        private Boolean auth_identity;
        private Boolean auth_bankcard;
        private Boolean auth_zhima;
        private Boolean auth_alipay;
        private Boolean auth_taobao;
        private Boolean auth_jd;
        private Boolean auth_operator;
        private Boolean auth_contact;



        public Boolean getAuth_identity() {
            return auth_identity;
        }

        public void setAuth_identity(Boolean auth_identity) {
            this.auth_identity = auth_identity;
        }

        public Boolean getAuth_bankcard() {
            return auth_bankcard;
        }

        public void setAuth_bankcard(Boolean setAuth_bankcard) {
            this.auth_bankcard = setAuth_bankcard;
        }
        public Boolean getAuth_zhima() {
            return auth_zhima;
        }

        public void setAuth_zhima(Boolean zhima) {
            this.auth_zhima = zhima;
        }
        public Boolean getAuth_alipay() {
            return auth_alipay;
        }

        public void setAuth_alipay(Boolean alipay) {
            this.auth_alipay = alipay;
        }
        public Boolean getAuth_taobao() {
            return auth_taobao;
        }

        public void setAuth_taobao(Boolean taobao) {
            this.auth_taobao = taobao;
        }
        public Boolean getAuth_jd() {
            return auth_jd;
        }

        public void setAuth_jd(Boolean jd) {
            this.auth_jd = jd;
        }
        public Boolean getAuth_operator() {
            return auth_operator;
        }

        public void setAuth_operator(Boolean operator) {
            this.auth_operator = operator;
        }
        public Boolean getAuth_contact() {
            return auth_contact;
        }

        public void setAuth_contact(Boolean contact) {
            this.auth_contact = contact;
        }
        @Override
        public String toString() {
            return "AuthState{" +
                    "identity=" + auth_identity +
                    ", bankcard='" + auth_bankcard +
                    ", zhima='" + auth_zhima +
                    ", alipay='" + auth_alipay +
                    ", taobao='" + auth_taobao +
                    ", jd='" + auth_jd +
                    ", operator='" + auth_operator +
                    ", contact='" + auth_contact   + '\'' +
                    '}';
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
