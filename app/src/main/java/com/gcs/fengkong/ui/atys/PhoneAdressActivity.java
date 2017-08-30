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

import java.io.InputStream;
import java.util.ArrayList;

public class PhoneAdressActivity extends ListActivity {

	Context mContext = null;

	/** 获取库Phone表字�?**/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名�?**/
	private static final int PHONES_DISPLAY_NAME = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID = 3;

	/** 联系人名�?**/
	private ArrayList<String> mContactsName = new ArrayList<String>();

	/** 联系人头�?**/
	private ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** 联系人头�?**/
	private ArrayList<Bitmap> mContactsImg = new ArrayList<Bitmap>();

	ListView mListView = null;
	private MyListAdapter mAdapter;

	private RelativeLayout rely;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mListView = this.getListView();
		/** 得到手机通讯录联系人信息 **/
		getPhoneContacts();

		mAdapter = new MyListAdapter(this);
		setListAdapter(mAdapter);

		

	}

	// 获取手机联系�?
	private void getPhoneContacts() {
		// rely=(RelativeLayout) findViewById(R.id.relationId);
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系�?
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		// 不为�?
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
				// 当手机号码为空的或�?为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名�?
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);

				// 得到联系人头像ID
				Long imgid = phoneCursor.getLong(PHONES_PHOTO_ID);

				// 得到联系人头像Bitamp
				Bitmap bitmap = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他�?��默认�?
				if (imgid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					bitmap = BitmapFactory.decodeStream(input);
				} else {
					// 设置默认
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.mipmap.ic_launcher);
				}

				mContactsName.add(contactName);
				Log.i("info", "contactName---" + contactName);
				// Log.i("info","mContactsName111"+mContactsName);
				mContactsNumber.add(phoneNumber);
				mContactsImg.add(bitmap);
			}

			phoneCursor.close();

		}
	}

	/** 得到手机SIM卡联系人人信�?**/
	private void getSIMContacts() {
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
	}

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
			ImageView iamge = null;
			TextView name = null;
			TextView moble = null;
			if (convertView == null || position < mContactsNumber.size()) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.lianxiren, null);
				iamge = (ImageView) convertView.findViewById(R.id.image);
				name = (TextView) convertView.findViewById(R.id.name);
				moble = (TextView) convertView.findViewById(R.id.moble);
			}
			// 绘制联系人名�?
			if (mContactsName.get(position) != null) {
				name.setText(mContactsName.get(position));
			}
			Log.i("info",
					position + "mContactsName------"
							+ mContactsName.get(position));
			// 绘制联系人号�?
			moble.setText(mContactsNumber.get(position));
			// Log.i("info","mContactsNumber------"+mContactsNumber.get(position));
			// 绘制联系人头�?
			iamge.setImageBitmap(mContactsImg.get(position));
			return convertView;
		}

	}
}
