package com.gcs.fengkong.ui.api.verify;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * App 校验类 AES
 * Created by qiujuer
 * on 2016/10/9.
 */
public final class Verifier {


    /**
     * 获取签名Sha1
     * 正常返回类似于 "76:A4:D1:65:AC:1E:0E:78:E3:7D:13:5F:FC:D6:EB:0F:9A:86:32:72"
     * 失败返回 NULL 或 ""
     *
     * @param application Application
     * @return 签名Sha1
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static String getSignatureSha1(Application application) {
        PackageManager pm = application.getPackageManager();
        String packageName = application.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);

        CertificateFactory cf;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }

        //X509
        X509Certificate c;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
        String hexString;
        try {
            //Use sha1
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byteToHexFormatted(publicKey);
        } catch (NoSuchAlgorithmException | CertificateEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        return hexString;
    }


    private static String byteToHexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

    /**
     * 获取APP私钥Token信息，用于高防网络认证
     * 返回类似于 "339f5dfcb3445b35311a23cfee395cc5dfca98c2" 字符串
     * 失败返回 NULL 或 ""
     *
     * @param application Application
     * @return Token
     */
    public static String getPrivateToken(Application application) {

        try {
         //   return native_getPrivateToken(application);
            return "";
        } catch (Exception e) {
            // error
            return "";
        }
    }

    /**
     * 对字符串数组做签名，用于高仿Url放篡改
     * 返回类似于 "339f5dfcb3445b35311a23cfee395cc5dfca98c2" 字符串
     *
     * @param values 待签名的字符串
     * @return 签名后的字符串
     */
    public static String signStringArray(String... values) {
      //  return native_signStringArray(values);
        return "";
    }




}
