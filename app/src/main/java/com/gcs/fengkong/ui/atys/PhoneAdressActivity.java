package com.gcs.fengkong.ui.atys;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcs.fengkong.R;
import com.gcs.fengkong.ui.bean.ContactBean;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;

public class PhoneAdressActivity extends ListActivity {

	Context mContext = null;

	/** 获取库Phone表字�?**/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME,  Phone.CONTACT_ID};

	/** 联系人显示名�?**/
	private static final int PHONES_DISPLAY_NAME = 0;


	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID = 1;

	/** 联系人名�?**/
	private ArrayList<String> mContactsName = new ArrayList<String>();

	/** 联系人头�?**/
	private ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** 联系人头公司**/
	private ArrayList<String> mContactsCompany = new ArrayList<String>();

	/** 联系人头邮件**/
	private ArrayList<String> mContactsEmail = new ArrayList<String>();
	/** 所有联系人**/
	private ArrayList<ContactBean> mAllContact = new ArrayList<ContactBean>();

	ListView mListView = null;
	private MyListAdapter mAdapter;

	private RelativeLayout rely;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mListView = this.getListView();
		/** 得到手机通讯录联系人信息 **/
		mAllContact = getPhoneContacts();
		Log.e("获取的电话号码>>",new Gson().toJson(mAllContact));


		mAdapter = new MyListAdapter(this);
		setListAdapter(mAdapter);

		

	}

	// 获取手机联系�?
	private ArrayList<ContactBean> getPhoneContacts() {


		// rely=(RelativeLayout) findViewById(R.id.relationId);
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人信息
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
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
				// 得到联系人名�?
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
				Log.i("info", "contactName---" + contactName);
				// Log.i("info","mContactsName111"+mContactsName);

				mAllContact.add(mGetContact);
			}

			phoneCursor.close();

		}
		return mAllContact;
	}

	/** 得到手机SIM卡联系人人信�?**/
/*	private void getSIMContacts() {
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
				// 当手机号码为空的或�?为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名�?
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

				// Sim卡中没有联系人头�?

				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
			}

			phoneCursor.close();
		}
	}*/

	// 添加适配�?
	class MyListAdapter extends BaseAdapter {
		public MyListAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			// 设置绘制数量
			return mContactsName.size();
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			TextView name = null;
			TextView company = null;
			TextView email = null;
			TextView mobile = null;
			if (convertView == null || position < mContactsNumber.size()) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.lianxiren, null);

				name = (TextView) convertView.findViewById(R.id.name);
				company = (TextView) convertView.findViewById(R.id.company);
				email = (TextView) convertView.findViewById(R.id.email);
				mobile = (TextView) convertView.findViewById(R.id.mobile);
			}
			// 绘制联系人名�?
			if (mContactsName.get(position) != null) {
				name.setText(mContactsName.get(position));
			}
			Log.i("info",
					position + "mContactsName------"
							+ mContactsName.get(position));
			company.setText(mContactsCompany.get(position));
			email.setText(mContactsEmail.get(position));
			// 绘制联系人号�?
			mobile.setText(mContactsNumber.get(position));
			// Log.i("info","mContactsNumber------"+mContactsNumber.get(position));

			return convertView;
		}

	}
}
