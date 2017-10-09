package com.gcs.jyfk.ui.bean;

/*导入要放入替换的同一个activity下可后退的fragment*/


import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.frags.subfrags.AboutGCSFragment;
import com.gcs.jyfk.ui.frags.subfrags.AlipayAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.BankAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.BrowserFragment;
import com.gcs.jyfk.ui.frags.subfrags.FeedbackFragment;
import com.gcs.jyfk.ui.frags.subfrags.IdentityAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.JdAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.OperatorAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.PersonalDataFragment;
import com.gcs.jyfk.ui.frags.subfrags.ResetPasswordFrament;
import com.gcs.jyfk.ui.frags.subfrags.SettingsFragment;
import com.gcs.jyfk.ui.frags.subfrags.TaobaoAuthFragment;
import com.gcs.jyfk.ui.frags.subfrags.ZhimaAuthFragment;

public enum SimpleBackPage {
        /*枚举*/

    IDENTITY_AUTH(1, R.string.identity_string, IdentityAuthFragment.class),
    BANK_AUTH(2, R.string.bank_string, BankAuthFragment.class),
    ZHIMA_AUTH(3, R.string.zhima_string, ZhimaAuthFragment.class),
    ALIPAY_AUTH(4, R.string.alipay_string, AlipayAuthFragment.class),
    TAOBAO_AUTH(5, R.string.taobao_string, TaobaoAuthFragment.class),
    JD_AUTH(6, R.string.jd_string, JdAuthFragment.class),
    OPERATOR_AUTH(7, R.string.operator_string, OperatorAuthFragment.class),
    PERSONAL_DATA(14, R.string.personal_data, PersonalDataFragment.class),
    SETTING(15, R.string.actionbar_title_setting, SettingsFragment.class),

    BROWSER(26, R.string.app_name, BrowserFragment.class),
    FEEDBACK_GCS(16, R.string.feedback, FeedbackFragment.class),
    RESET_PASSWORD(18, R.string.setting_reset_password, ResetPasswordFrament.class),
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
