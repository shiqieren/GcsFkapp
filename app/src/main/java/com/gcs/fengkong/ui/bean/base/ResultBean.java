package com.gcs.fengkong.ui.bean.base;


//import com.lyw.app.ui.notice.NoticeBean;
//import com.lyw.app.ui.notice.NoticeManager;

import com.gcs.fengkong.ui.notice.NoticeBean;

/**
 * Created by lyw
 * on 16-5-23.
 */
public class ResultBean<T> {
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_UNKNOW = 0;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_NOT_FIND = 404;
    public static final int RESULT_NOT_LOGIN = 201;
    public static final int RESULT_TOKEN_EXPRIED = 202;
    public static final int RESULT_NOT_PERMISSION = 203;
    public static final int RESULT_TOKEN_ERROR = 204;

    private T obj;
    private int code;
    private String msg;
    ///private String time;
    private NoticeBean notice;

    public T getResult() {
        return obj;
    }

    public void setResult(T result) {
        this.obj = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void getMessage(String message) {
        this.msg = message;
    }

    /*public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }*/

    public boolean isOk() {
        return code == RESULT_SUCCESS;
    }

    public boolean isSuccess() {
        // 每次回来后通知消息到达
       // NoticeManager.publish(this, this.notice);
        return code == RESULT_SUCCESS && obj != null;
    }
    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "code:" + code
                + " + message:" + msg
                + " + result:" + (obj != null ? obj.toString() : null);
    }
}