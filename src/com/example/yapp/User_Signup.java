package com.example.yapp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class User_Signup extends Activity {
	
	private EditText id, pw, repw;
	private Button nextBtn;
	MyDataShared mds;
	static final String TAG = "GCM Demo";
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "194583239442";

	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;

	String regid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common__signup);
		
		init();
		
		id = (EditText) findViewById(R.id.signupIdEdit);
		pw = (EditText) findViewById(R.id.signupPwEdit);
		repw = (EditText) findViewById(R.id.rePwEdit);
		nextBtn = (Button) findViewById(R.id.nextBtn);
		mds = new MyDataShared(context);
		nextBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String userId = id.getText().toString();
				String userPw = pw.getText().toString();
				String repwstr = repw.getText().toString();

				if (userPw.equals(repwstr) != true) {
					Toast.makeText(context, "비밀번호가 불일치", Toast.LENGTH_LONG)
							.show();
				} else { 
					// 비밀번호와 비밀번호 확인이 일치하면
					// 1. sharedpreference에 userId와 userPw저장
					// 2. 푸쉬정보 설정 창으로 이동
					Toast.makeText(context, "비밀번호가 일치", Toast.LENGTH_LONG)
					.show();
					mds.put("userId", userId);
					mds.put("userPw", userPw);

					Intent intent = new Intent(context, PushSetting.class);
					startActivity(intent);
				}

			}
		});
	}
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void init(){
//		mDisplay = (TextView) findViewById(R.id.display);

		context = getApplicationContext();

		// 硫붿떆吏??섏떊???꾪빐 GCM?쒕쾭 ?깅줉???꾩슂?섎ŉ
		// ?깅줉?섎㈃ registration id瑜?諛쏅뒗??		// ?깅줉??Project Number???ъ슜(SENDER_ID = Project Number)
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			// ?대? ??옣??regid媛??덈뒗吏??뺤씤

			if (regid.isEmpty()) { // ?녿떎硫?				registerInBackground(); // ?깅줉
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
		
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					Bundle data = new Bundle();
					data.putString("key1", "Hello World");
					data.putString("key2",
							"com.google.android.gcm.demo.app.ECHO_NOW");
					String id = Integer.toString(msgId.incrementAndGet());

					gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
					msg = "Sent message";
					
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		// Check device for Play Services APK.
//		checkPlayServices();
//	}

	// Google Play Service媛??ъ슜 媛?뒫?쒖? 泥댄겕?섏뿬 遺덇??섎㈃ ?ㅼ슫濡쒕뱶瑜??좊룄?섎뒗 ?ㅼ씠?쇰줈洹??쒖떆

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	// redid瑜???옣
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	// ??옣??registration id 諛섑솚
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		Log.d("SF", registrationId);
		return registrationId;
	}

	// GCM ?쒕쾭???깅줉?섎뒗 ?묒뾽
	// 諛섑솚媛믪쑝濡?registration id瑜?諛쏅뒗??	// ?깅줉?섏뿬 諛쏆? regid瑜?sharedpreference????옣
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					storeRegistrationId(context, regid); // regid媛???옣
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				System.out.println(msg + "\n");
				mds.put("GCMregitId", msg);
			}
		}.execute(null, null, null);
	}

	// Send an upstream message.
	public void onClick(final View view) {

		
		
//		if (view == findViewById(R.id.send)) {
//			
//			
////			sendGCMMessage();
//		}
//			else if (view == findViewById(R.id.clear)) {
//			mDisplay.setText("");
//		}
		
	}

//	private void sendGCMMessage() {
//		String url = "http://52.68.42.56:5001/push/send";
//		RequestQueue mQueue = Volley.newRequestQueue(context);
//
//		// ?쒕쾭?먯꽌 諛쏆븘???뺣낫
//		// jsonparser泥섎━
//		Listener<String> listener = new Listener<String>() {
//
//			@Override
//			public void onResponse(String result) {
//				try {
//					JSONObject output = new JSONObject(result);
//					final String value = output.getString("sending");
//					JSONArray jArr = output.getJSONArray("data");
//					
//					
//
//					if (value.equals("success")) {
//						Log.d("sending", "set comment success");
//
//					} else {
//						Log.d("sending", "fail");
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//		};// listener
//
//		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError arg0) {
//				Toast.makeText(context, "network error: " + arg0,
//						Toast.LENGTH_SHORT).show();
//				System.out.println("network error: " + arg0);
//			}
//
//		};// ErrorListener
//
//		StringRequest request = new StringRequest(Method.POST, url, listener,
//				errorListener) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("Content-Type", "x-www-form-urlencoded");
//				params.put("regid", regid);
//				params.put("key1", "?쒕컻?쒕컻");
//				params.put("key2", "?섎씪?섎씪");
//
//
//				return params;
//			}
//
//		};// StringRequest
//
//		mQueue.add(request);
//		
//	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(GetGCMActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}


}
