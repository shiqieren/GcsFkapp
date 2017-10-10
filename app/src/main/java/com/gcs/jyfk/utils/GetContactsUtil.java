package com.gcs.jyfk.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.gcs.jyfk.ui.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 0021 9-21.
 */

public class GetContactsUtil {

    /** 获取库Phone表字�?**/
    private static final String[] PHONES_PROJECTION = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,  ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /** 联系人显示名�?**/
    private static final int PHONES_DISPLAY_NAME = 0;

    /** 联系人的ID **/
    private static final int PHONES_CONTACT_ID = 1;
    public static List<ContactBean> getContactslist(Context mContext){

         ArrayList<String> mContactsName = new ArrayList<String>();

         ArrayList<String> mContactsNumber = new ArrayList<String>();

         ArrayList<String> mContactsCompany = new ArrayList<String>();

         ArrayList<String> mContactsEmail = new ArrayList<String>();

         ArrayList<ContactBean> mAllContact = new ArrayList<ContactBean>();

        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人信息
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        // 不为空
        if (phoneCursor != null) {

            while (phoneCursor.moveToNext()) {
                ContactBean mGetContact = new ContactBean();
                // 得到手机号码
                //String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                // 当手机号码为空的或�?为空字段 跳过当前循环
				/*if (TextUtils.isEmpty(phoneNumber))
					continue;*/
                // 得到联系人名
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);
                if (TextUtils.isEmpty(contactName))
                    continue;
                // 得到联系人ID-用于关联查询
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);

                //查询电话类型的数据操作
                Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactid,
                        null, null);
                StringBuilder phonesb = new StringBuilder();
                while(phones.moveToNext())
                {
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //添加Phone的信息
                    phonesb.append(phoneNumber).append(";");

                }
                phones.close();
                mContactsNumber.add(String.valueOf(phonesb));
                mGetContact.setPhone(String.valueOf(phonesb));

                // 得到联系人公司
                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                //查询参数
                String[] orgWhereParams = new String[]{String.valueOf(contactid),
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor companys = resolver.query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                StringBuilder companysb = new StringBuilder();
                while (companys.moveToNext())
                {

                    String company = companys.getString(companys.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    companysb.append(company).append(";");
                    //添加Email的信息
                }
                companys.close();
                mContactsCompany.add(String.valueOf(companysb));
                mGetContact.setCompany(String.valueOf(companysb));
                // 得到联系人邮件
                Cursor emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactid, null, null);
                StringBuilder emailsb = new StringBuilder();
                while (emails.moveToNext())
                {
                    String emailAddress = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    //添加Email的信息
                    emailsb.append(emailAddress).append(";");
                }
                emails.close();
                mContactsEmail.add(String.valueOf(emailsb));
                mGetContact.setMail(String.valueOf(emailsb));

                mContactsName.add(contactName);
                mGetContact.setName(contactName);
                MyLog.i("info", "contactName---" + contactName);
                // Log.i("info","mContactsName111"+mContactsName);
                //电话号码为null时不存入联系人集合
                if (mContactsNumber !=null){
                    mAllContact.add(mGetContact);
                }

            }

            phoneCursor.close();

        }
        return mAllContact;
    }
}
