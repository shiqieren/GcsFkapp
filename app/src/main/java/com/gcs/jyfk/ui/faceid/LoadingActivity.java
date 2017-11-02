package com.gcs.jyfk.ui.faceid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.gcs.jyfk.ui.faceid.util.ConUtil;
import com.gcs.jyfk.ui.faceid.view.LivenessActivity;
import com.gcs.jyfk.ui.widget.SimplexToast;
import com.gcs.jyfk.ui.widget.statusbar.StatusBarCompat;
import com.gcs.jyfk.utils.AppOperator;
import com.gcs.jyfk.utils.DialogUtil;
import com.gcs.jyfk.utils.MyLog;
import com.gcs.jyfk.utils.TDevice;
import com.gcs.jyfk.utils.UIUtils;
import com.gcs.jyfk.utils.VibratorUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardlib.util.SharedUtil;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class LoadingActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
	private String uuid;
	private SharedUtil mSharedUtil;
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
    private String idpath;

	@Override
	protected void onResume() {
		super.onResume();
		String language_save = mSharedUtil.getStringValueByKey("language");
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (!language.equals(language_save))
			showLanguage(language);
	}
	protected void showLanguage(String language) {
		// 设置应用语言类
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
		if (language.equals("zh")) {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		} else {
			config.locale = Locale.ENGLISH;
		}
		resources.updateConfiguration(config, dm);
		mSharedUtil.saveStringValue("language", language);
		freshView();
	}
	private void freshView() {
		Intent intent = new Intent(this, LoadingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

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
		mSharedUtil = new SharedUtil(this);
		uuid = ConUtil.getUUIDString(this);
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
				//String uuid = "13213214321424";
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
		if(mIvfront.getDrawable()==null){
			SimplexToast.showMyToast("请拍摄身份证正面",GlobalApplication.getContext());
			return;
		}
		if(mIvreverse.getDrawable()==null){
			SimplexToast.showMyToast("请拍摄身份证背面",GlobalApplication.getContext());
			return;
		}
		netWorkWarranty();
		/*Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("identity_name", tempUsername);
		intent.putExtra("identity_card", tempIdcard);
		intent.putExtra("side", mydata.getIntExtra("side", 0));
		intent.putExtra("idcardImg", mydata.getByteArrayExtra("idcardImg"));
		intent.putExtra("portraitImg", mydata.getByteArrayExtra("portraitImg"));

		startActivity(intent);*/
		if (android.os.Build.VERSION.SDK_INT >= M) {
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.CAMERA)
					!= PackageManager.PERMISSION_GRANTED) {
				//进行权限请求
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.CAMERA},
						EXTERNAL_STORAGE_REQ_CAMERA_CODE);
			} else {
				enterNextAuth();
			}
		} else {
			enterNextAuth();
		}

	}

	/**
	 * 联网授权
	 */
	private void netWorkWarranty() {
		mBtsubmit.setVisibility(View.GONE);
		barLinear.setVisibility(View.VISIBLE);
		againWarrantyBtn.setVisibility(View.GONE);
		WarrantyText.setText(R.string.meglive_auth_progress);
		WarrantyBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Manager manager = new Manager(LoadingActivity.this);
				LivenessLicenseManager licenseManager = new LivenessLicenseManager(LoadingActivity.this);
				manager.registerLicenseManager(licenseManager);

				manager.takeLicenseFromNetwork(uuid);
				if (licenseManager.checkCachedLicense() > 0)
					mHandler.sendEmptyMessage(1);
				else
					mHandler.sendEmptyMessage(2);
			}
		}).start();
	}
	private void enterNextAuth() {
		startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
	}

	private static final int PAGE_INTO_LIVENESS = 100;

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

	private static final int INTO_IDCARDSCAN_PAGE = 99;
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
	private MediaPlayer mMediaPlayer = null;
	private void doPlay(int rawId) {
		if (mMediaPlayer == null)
			mMediaPlayer = new MediaPlayer();

		mMediaPlayer.reset();
		try {
			AssetFileDescriptor localAssetFileDescriptor = getResources()
					.openRawResourceFd(rawId);
			mMediaPlayer.setDataSource(
					localAssetFileDescriptor.getFileDescriptor(),
					localAssetFileDescriptor.getStartOffset(),
					localAssetFileDescriptor.getLength());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception localIOException) {
			localIOException.printStackTrace();
		}
	}
	/**
	 * 如何调用Verify2.0方法
	 * <p>
	 */
	/*public void imageVerify(Map<String, byte[]> images,String idpath, String delta,File bestimg,File envimg) {
		Map<String, String> params = new HashMap<>();
		String api_key ="67pxVzlMJKqx4wJrzkDkHVOtsxK7vl8D";
		String api_secret = "SSSSP0zYUV48y3-r1kXvmAmaU3UO4GET";
		params.put("api_key",api_key);
		params.put("api_secret", api_secret);
		params.put("comparison_type", 1 + "");
		params.put("face_image_type", "meglive");
		String name = mTvname.getText().toString();
		String idnum = mIdcard.getText().toString();
		params.put("idcard_name", name);
		params.put("idcard_number", idnum);
		params.put("delta", delta);
		MyLog.i("GCS",bestimg.toString());
		MyLog.i("GCS",envimg.toString());
		*//*for (Map.Entry<String, byte[]> entry : images.entrySet()) {
			params.put(entry.getKey(),
					new ByteArrayInputStream(entry.getValue()));
		}*//*

            File idimg = new File(idpath);

		MyApi.facelive(params,idimg,bestimg,envimg,new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				MyLog.i("GCS","异常e"+e);
			}

			@Override
			public void onResponse(String response, int id) {
				MyLog.i("GCS","响应结果response"+response);
				//String successStr = new String(bytes);
				//Log.i("result","verify成功："+successStr);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					if (!jsonObject.has("error")) {
						// 活体最好的一张照片和公安部系统上身份证上的照片比较
						double confidence = jsonObject.getJSONObject(
								"result_faceid")
								.getDouble("confidence");
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("result_faceid")
								.getJSONObject("thresholds");
						double threshold = jsonObject2
								.getDouble("1e-3");
						double tenThreshold = jsonObject2
								.getDouble("1e-4");
						double hundredThreshold = jsonObject2
								.getDouble("1e-5");

						try {
							// 活体最好的一张照片和拍摄身份证上的照片的比较
							JSONObject jObject = jsonObject
									.getJSONObject("result_ref1");
							double idcard_confidence = jObject
									.getDouble("confidence");
							double idcard_threshold = jObject
									.getJSONObject("thresholds")
									.getDouble("1e-3");
							double idcard_tenThreshold = jObject
									.getJSONObject("thresholds")
									.getDouble("1e-4");
							double idcard_hundredThreshold = jObject
									.getJSONObject("thresholds")
									.getDouble("1e-5");
						} catch (Exception e) {

						}
						// 解析faceGen
						JSONObject jObject = jsonObject
								.getJSONObject("face_genuineness");

						float mask_confidence = (float) jObject
								.getDouble("mask_confidence");
						float mask_threshold = (float) jObject
								.getDouble("mask_threshold");
						float screen_replay_confidence = (float) jObject
								.getDouble("screen_replay_confidence");
						float screen_replay_threshold = (float) jObject
								.getDouble("screen_replay_threshold");
						float synthetic_face_confidence = (float) jObject
								.getDouble("synthetic_face_confidence");
						float synthetic_face_threshold = (float) jObject
								.getDouble("synthetic_face_threshold");
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		});
		//AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		//String url = "https://api.megvii.com/faceid/v2/verify";
		*//*asyncHttpClient.post(url, requestParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int i, Header[] headers, byte[] bytes) {


					}

					@Override
					public void onFailure(int i, Header[] headers,
										  byte[] bytes, Throwable throwable) {
						// 请求失败
						Log.i("result","verify失败");

					}
				});*//*
	}*/
	public void imageVerify(Map<String, byte[]> images, String delta,String idpath) {
		String api_key ="67pxVzlMJKqx4wJrzkDkHVOtsxK7vl8D";
		String api_secret = "SSSSP0zYUV48y3-r1kXvmAmaU3UO4GET";
		String name = mTvname.getText().toString();
		String idnum = mIdcard.getText().toString();
		RequestParams requestParams = new RequestParams();
		requestParams.put("idcard_name", name);
		requestParams.put("idcard_number", idnum);
		try {
			requestParams.put("image_ref1", new FileInputStream(new File(
					idpath)));// 传入身份证头像照片路径
		} catch (Exception e) {
		}
		requestParams.put("delta", delta);
		requestParams.put("api_key", api_key);
		requestParams.put("api_secret", api_secret);

		requestParams.put("comparison_type", 1 + "");
		requestParams.put("face_image_type", "meglive");

		for (Map.Entry<String, byte[]> entry : images.entrySet()) {
			requestParams.put(entry.getKey(),
					new ByteArrayInputStream(entry.getValue()));
		}
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String url = "https://api.megvii.com/faceid/v2/verify";
		asyncHttpClient.post(url, requestParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int i, Header[] headers, byte[] bytes) {

						String successStr = new String(bytes);
						MyLog.i("GCS","verify成功："+successStr);
						showAuthbookconfirm("认证成功","确认");
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(successStr);
							if (!jsonObject.has("error")) {
								// 活体最好的一张照片和公安部系统上身份证上的照片比较
								double confidence = jsonObject.getJSONObject(
										"result_faceid")
										.getDouble("confidence");
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("result_faceid")
										.getJSONObject("thresholds");
								double threshold = jsonObject2
										.getDouble("1e-3");
								double tenThreshold = jsonObject2
										.getDouble("1e-4");
								double hundredThreshold = jsonObject2
										.getDouble("1e-5");

								try {
									// 活体最好的一张照片和拍摄身份证上的照片的比较
									JSONObject jObject = jsonObject
											.getJSONObject("result_ref1");
									double idcard_confidence = jObject
											.getDouble("confidence");
									double idcard_threshold = jObject
											.getJSONObject("thresholds")
											.getDouble("1e-3");
									double idcard_tenThreshold = jObject
											.getJSONObject("thresholds")
											.getDouble("1e-4");
									double idcard_hundredThreshold = jObject
											.getJSONObject("thresholds")
											.getDouble("1e-5");
								} catch (Exception e) {

								}
								// 解析faceGen
								JSONObject jObject = jsonObject
										.getJSONObject("face_genuineness");

								float mask_confidence = (float) jObject
										.getDouble("mask_confidence");
								float mask_threshold = (float) jObject
										.getDouble("mask_threshold");
								float screen_replay_confidence = (float) jObject
										.getDouble("screen_replay_confidence");
								float screen_replay_threshold = (float) jObject
										.getDouble("screen_replay_threshold");
								float synthetic_face_confidence = (float) jObject
										.getDouble("synthetic_face_confidence");
								float synthetic_face_threshold = (float) jObject
										.getDouble("synthetic_face_threshold");
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}

					@Override
					public void onFailure(int i, Header[] headers,
										  byte[] bytes, Throwable throwable) {
						// 请求失败
						showAuthbookconfirm("认证失败","重新认证");

					}
				});
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
				byte[] portraitImg=data.getByteArrayExtra("portraitImg");
				Bitmap portraitBmp = BitmapFactory.decodeByteArray(portraitImg, 0,
						portraitImg.length);
                idpath = saveBitmap(LoadingActivity.this, portraitBmp);
				MyLog.i("GCS","头像路径"+idpath);
                File portraitfile = new File(idpath);

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
			return;
		}if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
//            String result = data.getStringExtra("result");
//            String delta = data.getStringExtra("delta");
//            Serializable images=data.getSerializableExtra("images");
//			Bundle bundle=data.getExtras();
//			ResultActivity.startActivity(this, bundle);
			Bundle bundle=data.getExtras();
			String resultOBJ = bundle.getString("result");
			try {
				JSONObject result = new JSONObject(resultOBJ);
				int resID = result.getInt("resultcode");
				if (resID == R.string.verify_success) {
					doPlay(R.raw.meglive_success);
				} else if (resID == R.string.liveness_detection_failed_not_video) {
					doPlay(R.raw.meglive_failed);
				} else if (resID == R.string.liveness_detection_failed_timeout) {
					doPlay(R.raw.meglive_failed);
				} else if (resID == R.string.liveness_detection_failed) {
					doPlay(R.raw.meglive_failed);
				} else {
					doPlay(R.raw.meglive_failed);
				}
				File bestimgfile = null;
				File envimgfile= null;
				boolean isSuccess = result.getString("result").equals(
						getResources().getString(R.string.verify_success));
				if (isSuccess){
					String delta = bundle.getString("delta");
					Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
					if (images.containsKey("image_best")) {
						byte[] bestImg = images.get("image_best");
						if (bestImg != null && bestImg.length > 0) {
							Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
							//bestImage.setImageBitmap(bestBitMap);
							String path = saveBitmap(LoadingActivity.this, bestBitMap);
							bestimgfile = new File(path);
						}
					}
					if (images.containsKey("image_env")) {
						byte[] envImg = images.get("image_env");
						if (envImg != null && envImg.length > 0) {
							Bitmap envBitMap = BitmapFactory.decodeByteArray(envImg, 0, envImg.length);
							//envImage.setImageBitmap(envBitMap);
							String path = saveBitmap(LoadingActivity.this, envBitMap);
							envimgfile = new File(path);
						}
					}
					MyLog.i("GCS","活体识别成功");
				//	ll_result_image.setVisibility(View.VISIBLE);
					imageVerify(images,delta,idpath);
					/*if (bestimgfile!=null && envimgfile!=null && !TextUtils.isEmpty(idpath)){
						imageVerify(images,idpath,delta,bestimgfile,envimgfile);
					}
*/
				}else {
					MyLog.i("GCS","活体识别失败");
				}

			//	doRotate(isSuccess);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					mBtsubmit.setVisibility(View.VISIBLE);
					barLinear.setVisibility(View.GONE);
					break;
				case 2:
					againWarrantyBtn.setVisibility(View.VISIBLE);
					WarrantyText.setText(R.string.meglive_auth_failed);
					WarrantyBar.setVisibility(View.GONE);
					break;
			}
		}
	};

	private void showAuthbookconfirm(String tip, final String btnstr) {
       /* TextView title = new TextView(getContext());
        title.setText("通讯录授权");
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);*/

		View dialogview = View.inflate(this,R.layout.custom_dialog,null);
		TextView tv_title = dialogview.findViewById(R.id.dialog_tip);
		tv_title.setText(tip);
		TextView tv_link = dialogview.findViewById(R.id.read_authbook_link);
		tv_link.setVisibility(View.INVISIBLE);
		Button bt_cancle = dialogview.findViewById(R.id.btn_cancel);
		bt_cancle.setText(btnstr);
        /*final AlertDialog dlgShowBack = DialogUtil.getDialog(getContext()).setView(dialogview).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), PhoneAdressActivity.class);
                startActivity(intent);
            }
        }).create();*/
		final AlertDialog dlgShowBack = DialogUtil.getDialog(this).setView(dialogview).setCancelable(true).create();

		bt_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dlgShowBack.dismiss();
				if (btnstr.equals("确认")){
					finish();
					//更改状态值，变为不可点击
				}

			}
		});
		dlgShowBack.show();
		dlgShowBack.getWindow().setBackgroundDrawableResource(R.drawable.rounded_search_text);
		WindowManager.LayoutParams params = dlgShowBack.getWindow().getAttributes();
		params.width = (int) TDevice.dp2px(270);
		params.height = (int) TDevice.dp2px(122);
		dlgShowBack.getWindow().setAttributes(params);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

	}
}