package com.gcs.jyfk.ui.faceid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gcs.jyfk.GlobalApplication;
import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.ShowUIHelper;
import com.gcs.jyfk.ui.account.AccountHelper;
import com.gcs.jyfk.ui.account.RichTextParser;
import com.gcs.jyfk.ui.account.bean.User;
import com.gcs.jyfk.ui.api.MyApi;
import com.gcs.jyfk.ui.atys.BaseActivity;
import com.gcs.jyfk.ui.bean.Identityinfo;
import com.gcs.jyfk.ui.bean.base.ResultBean;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.statusbar.StatusBarCompat;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.UIUtils;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class LoadingActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

	boolean isVertical;
	private RelativeLayout contentRel;
	private LinearLayout barLinear;
	private LinearLayout resettip;
	private TextView WarrantyText;
	private ProgressBar WarrantyBar;
	private Button againWarrantyBtn;
	private Button mBtsubmit;
	protected Toolbar mToolBar;
	protected TextView textView;
	private ImageView mIvfront;
	private ImageView mIvreverse;
	private EditText mTvname;
	private EditText mIdcard;
	private Boolean mIsZHname = false;
	private Boolean mIsIDcardnumber = false;
	private Intent mydata;
	@Override
	protected int getContentView() {
		return R.layout.activity_loading;
	}

	@Override
	protected void initWindow() {
		super.initWindow();
		StatusBarCompat.setStatusBarColor(this, UIUtils.getColor(R.color.base_app_color));

		mToolBar = (Toolbar) findViewById(R.id.toolbar);
		textView = (TextView) findViewById(R.id.toolbar_title);
		if (mToolBar != null) {

			setSupportActionBar(mToolBar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayShowTitleEnabled(false);
				actionBar.setHomeButtonEnabled(true);
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
			mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

		}
		setToolBarTitle("身份证");
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		contentRel = (RelativeLayout) findViewById(R.id.loading_layout_contentRel);
		barLinear = (LinearLayout) findViewById(R.id.loading_layout_barLinear);
		WarrantyText = (TextView) findViewById(R.id.loading_layout_WarrantyText);
		WarrantyBar = (ProgressBar) findViewById(R.id.loading_layout_WarrantyBar);
		againWarrantyBtn = (Button) findViewById(R.id.loading_layout_againWarrantyBtn);
		mBtsubmit = (Button) findViewById(R.id.bt_identity_submit);
		mTvname = (EditText) findViewById(R.id.et_identity_name);
		mIdcard = (EditText) findViewById(R.id.et_identity_number);
		mIvfront = (ImageView) findViewById(R.id.iv_idcard_front);
		mIvreverse = (ImageView) findViewById(R.id.iv_idcard_reverse);
		resettip = (LinearLayout) findViewById(R.id.ll_reset_tip);
		mBtsubmit.setOnClickListener(this);
		findViewById(R.id.reset_identity).setOnClickListener(this);
		findViewById(R.id.iv_idcard_reverse).setOnClickListener(this);
		findViewById(R.id.iv_idcard_front).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		super.initData();
		network();
		if (!mTvname.getText().toString().isEmpty()||!mIdcard.getText().toString().isEmpty()){
			resettip.setVisibility(View.VISIBLE);
		}
		mTvname.setOnFocusChangeListener(this);

		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!RichTextParser.isChinese(source.charAt(i))) {
						//SimplexToast.showMyToast("请输入中文字符",GlobalApplication.getContext());
						return "";
					}
				}
				return null;
			}
		};
		mTvname.setFilters(new InputFilter[]{filter});
		mTvname.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@SuppressWarnings("deprecation")
			@Override
			public void afterTextChanged(Editable s) {
				String username = s.toString().trim();
				mIsZHname = RichTextParser.checkIsZH(username);



			}
		});

		mIdcard.setOnFocusChangeListener(this);

		mIdcard.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return InputType.TYPE_CLASS_TEXT;
			}

			@Override
			protected char[] getAcceptedChars() {
				char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X','Y','Z' };
				return numberChars;
			}
		});
		mIdcard.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@SuppressWarnings("deprecation")
			@Override
			public void afterTextChanged(Editable s) {


			}
		});

	}

	/**
	 * 上传图片
	 */
	private void network() {
		contentRel.setVisibility(View.GONE);
		barLinear.setVisibility(View.VISIBLE);
		againWarrantyBtn.setVisibility(View.GONE);
		WarrantyText.setText("正在联网授权中...");
		WarrantyBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Manager manager = new Manager(LoadingActivity.this);
				IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
						LoadingActivity.this);
				manager.registerLicenseManager(idCardLicenseManager);
				String uuid = "13213214321424";
				manager.takeLicenseFromNetwork(uuid);
				String contextStr = manager.getContext(uuid);
				Log.w("ceshi", "contextStr====" + contextStr);

				Log.w("ceshi",
						"idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
				if (idCardLicenseManager.checkCachedLicense() > 0)
					UIAuthState(true);
				else
					UIAuthState(false);

			}
		}).start();
	}

	private void UIAuthState(final boolean isSuccess) {
		runOnUiThread(new Runnable() {
			public void run() {
				authState(isSuccess);
			}
		});
	}

	private void authState(boolean isSuccess) {
		if (isSuccess) {
			barLinear.setVisibility(View.GONE);
			WarrantyBar.setVisibility(View.GONE);
			againWarrantyBtn.setVisibility(View.GONE);
			contentRel.setVisibility(View.VISIBLE);
		} else {
			barLinear.setVisibility(View.VISIBLE);
			WarrantyBar.setVisibility(View.GONE);
			againWarrantyBtn.setVisibility(View.VISIBLE);
			contentRel.setVisibility(View.GONE);
			WarrantyText.setText("联网授权失败！请检查网络或找服务商");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loading_layout_againWarrantyBtn:
				network();
				break;
			case R.id.iv_idcard_front: {
				requestCameraPerm(0);
			}
				break;
			case R.id.iv_idcard_reverse: {
				requestCameraPerm(1);
				break;
			}
			case R.id.bt_identity_submit: {
				requestNext();
			}
			break;
			case R.id.reset_identity:{
				mTvname.setText(null);
				mIdcard.setText(null);
				mIvfront.setImageBitmap(null);
				mIvreverse.setImageBitmap(null);
				resettip.setVisibility(View.GONE);
				break;
			}
		}
	}

	private void requestNext() {
		String tempUsername = mTvname.getText().toString().trim();
		String tempIdcard = mIdcard.getText().toString().trim();
		if(!TextUtils.isEmpty(tempUsername)){
			if (mIsIDcardnumber){
				SimplexToast.showMyToast("身份证格式有误", GlobalApplication.getContext());
				return;
			}
		}else {
			SimplexToast.showMyToast("姓名不能为空", GlobalApplication.getContext());
			return;
		}
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("identity_name", tempUsername);
		intent.putExtra("identity_card", tempIdcard);
		intent.putExtra("side", mydata.getIntExtra("side", 0));
		intent.putExtra("idcardImg", mydata.getByteArrayExtra("idcardImg"));
		intent.putExtra("portraitImg", mydata.getByteArrayExtra("portraitImg"));

		startActivity(intent);


	}

	int mSide = 0;
	private void requestCameraPerm(int side) {
		mSide = side;
		if (android.os.Build.VERSION.SDK_INT >= M) {
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.CAMERA)
					!= PackageManager.PERMISSION_GRANTED) {
				//进行权限请求
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.CAMERA},
						EXTERNAL_STORAGE_REQ_CAMERA_CODE);
			} else {
				enterNextPage(side);
			}
		} else {
			enterNextPage(side);
		}
	}

	private void enterNextPage(int side){
		Intent intent = new Intent(this, IDCardScanActivity.class);
		intent.putExtra("side", side);
		intent.putExtra("isvertical", isVertical);
		startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
	}

	public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;

	@Override
	public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
		if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
			if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {// Permission Granted

				Util.showToast(this, "获取相机权限失败");
			} else
				enterNextPage(mSide);
		}
	}
	public void setToolBarTitle(int resId) {
		if (resId != 0) {
			setToolBarTitle(getString(resId));
		}
	}
	public void setToolBarTitle(String title) {
		if (TextUtils.isEmpty(title)) {
			title = getString(R.string.app_name);
		}
		if (mToolBar != null) {
			textView.setText(title);
		}
	}

	private static final int INTO_IDCARDSCAN_PAGE = 100;
	/**
	 * 保存bitmap至指定Picture文件夹
	 */
	public static String saveBitmap(Context mContext, Bitmap bitmaptosave) {
		if (bitmaptosave == null)
			return null;

		File mediaStorageDir = mContext.getExternalFilesDir("megvii");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		// String bitmapFileName = System.currentTimeMillis() + ".jpg";
		String bitmapFileName = System.currentTimeMillis() + "";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mediaStorageDir + "/" + bitmapFileName);
			boolean successful = bitmaptosave.compress(Bitmap.CompressFormat.JPEG, 75, fos);

			if (successful)
				return mediaStorageDir.getAbsolutePath() + "/" + bitmapFileName;
			else
				return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
			mydata = data;
			//Intent intent = new Intent(this, ResultActivity.class);
			//intent.putExtra("side", data.getIntExtra("side", 0));
			//intent.putExtra("idcardImg", data.getByteArrayExtra("idcardImg"));
			if (data.getIntExtra("side", 0) == 0) {
				//intent.putExtra("portraitImg", data.getByteArrayExtra("portraitImg"));
				byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
				Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0,
						idcardImgData.length);
					mIvfront.setImageBitmap(idcardBmp);
					String api_key ="67pxVzlMJKqx4wJrzkDkHVOtsxK7vl8D";
					String api_secret = "SSSSP0zYUV48y3-r1kXvmAmaU3UO4GET";
					MyLog.i("GCS","path生成");
					String path = saveBitmap(LoadingActivity.this, idcardBmp);
					File imgfile = new File(path);
					MyLog.i("GCS"," 调用api接口，检查身份数据");
					MyApi.faceidcard(api_key, api_secret, imgfile, new StringCallback() {
						@Override
						public void onError(Call call, Exception e, int id) {
							MyLog.i("GCS","异常e"+e);
						}

						@Override
						public void onResponse(String response, int id) {
							MyLog.i("GCS","结果"+response);
							try {
								Type type = new TypeToken<Identityinfo>() {}.getType();
								Identityinfo resultBean = AppOperator.createGson().fromJson(response, type);
								if (resultBean!=null){
									String name = resultBean.getName();
									String idcard = resultBean.getId_card_number();
									mTvname.setText(name);
									mIdcard.setText(idcard);
									resettip.setVisibility(View.VISIBLE);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			}else {

				byte[] idcardImgData = data.getByteArrayExtra("idcardImg");

				Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0,
						idcardImgData.length);
				mIvreverse.setImageBitmap(idcardBmp);

				//startActivity(intent);
			}
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

	}
}