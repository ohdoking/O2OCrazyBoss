package com.example.yapp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GetGCMActivity extends ActionBarActivity {
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

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void init(){
//		mDisplay = (TextView) findViewById(R.id.display);

		context = getApplicationContext();

		// Î©îÏãúÏß??òÏã†???ÑÌï¥ GCM?úÎ≤Ñ ?±Î°ù???ÑÏöî?òÎ©∞
		// ?±Î°ù?òÎ©¥ registration idÎ•?Î∞õÎäî??		// ?±Î°ù??Project Number???¨Ïö©(SENDER_ID = Project Number)
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			// ?¥Î? ??û•??regidÍ∞??àÎäîÏß??ïÏù∏

			if (regid.isEmpty()) { // ?ÜÎã§Î©?				registerInBackground(); // ?±Î°ù
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

	// Google Play ServiceÍ∞??¨Ïö© Í∞?ä•?úÏ? Ï≤¥ÌÅ¨?òÏó¨ Î∂àÍ??òÎ©¥ ?§Ïö¥Î°úÎìúÎ•??†ÎèÑ?òÎäî ?§Ïù¥?ºÎ°úÍ∑??úÏãú

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

	// redidÎ•???û•
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
	// ??û•??registration id Î∞òÌôò
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

	// GCM ?úÎ≤Ñ???±Î°ù?òÎäî ?ëÏóÖ
	// Î∞òÌôòÍ∞íÏúºÎ°?registration idÎ•?Î∞õÎäî??	// ?±Î°ù?òÏó¨ Î∞õÏ? regidÎ•?sharedpreference????û•
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

					storeRegistrationId(context, regid); // regidÍ∞???û•
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
//		// ?úÎ≤Ñ?êÏÑú Î∞õÏïÑ???ïÎ≥¥
//		// jsonparserÏ≤òÎ¶¨
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
//				params.put("key1", "?úÎ∞ú?úÎ∞ú");
//				params.put("key2", "?òÎùº?òÎùº");
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
