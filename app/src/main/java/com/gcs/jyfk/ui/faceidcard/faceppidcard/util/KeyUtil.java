package com.gcs.jyfk.ui.faceidcard.faceppidcard.util;


import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeyUtil {

    public static String API_KEY;
    public static String API_SECRET;

    public static boolean isReadKey(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            inputStream = context.getAssets().open("key");
            while ((count = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String str = new String(byteArrayOutputStream.toByteArray());
        String authKey = null;
        String authScrect = null;
        try {
            String[] strs = str.split(";");
            authKey = strs[0].trim();
            authScrect = strs[1].trim();
        } catch (Exception e) {
        }
        API_KEY = "_CL8-uALqGVtx29AxAb6LMSeU7R7AGbP";
        API_SECRET = "z87yQQGyIgP75T0jJOhMP6oEvjy9A8Wh";

         /*API_KEY = "67pxVzlMJKqx4wJrzkDkHVOtsxK7vl8D";
         API_SECRET = "SSSSP0zYUV48y3-r1kXvmAmaU3UO4GET";*/
        if (API_KEY == null || API_SECRET == null)
            return false;

        return true;
    }


}
