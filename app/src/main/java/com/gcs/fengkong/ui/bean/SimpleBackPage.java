package com.gcs.fengkong.ui.bean;

/*导入要放入替换的同一个activity下可后退的fragment*/


import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.frags.subfrags.AboutGCSFragment;
import com.gcs.fengkong.ui.frags.subfrags.AlipayAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.BankAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.BrowserFragment;
import com.gcs.fengkong.ui.frags.subfrags.ContactsAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.IdentityAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.JdAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.OperatorAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.SettingsFragment;
import com.gcs.fengkong.ui.frags.subfrags.TaobaoAuthFragment;
import com.gcs.fengkong.ui.frags.subfrags.ZhimaAuthFragment;

public enum SimpleBackPage {
        /*枚举*/

    IDENTITY_AUTH(1, R.string.identity_string, IdentityAuthFragment.class),
    BANK_AUTH(2, R.string.bank_string, BankAuthFragment.class),
    ZHIMA_AUTH(3, R.string.zhima_string, ZhimaAuthFragment.class),
    ALIPAY_AUTH(4, R.string.alipay_string, AlipayAuthFragment.class),
    TAOBAO_AUTH(5, R.string.taobao_string, TaobaoAuthFragment.class),
    JD_AUTH(6, R.string.jd_string, JdAuthFragment.class),
    OPERATOR_AUTH(7, R.string.operator_string, OperatorAuthFragment.class),

    SETTING(15, R.string.actionbar_title_setting, SettingsFragment.class),

    BROWSER(26, R.string.app_name, BrowserFragment.class),

    ABOUT_GCS(17, R.string.actionbar_title_about_gcs, AboutGCSFragment.class);



    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }


}
