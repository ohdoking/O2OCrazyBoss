package com.example.yapp;

import java.util.*;

import org.json.*;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.*;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.*;
import com.example.yapp.activity.MainActivity;
import com.example.yapp.o2o.O2OCallbacks;
import com.example.yapp.o2o.O2OSampleApplication;
import com.skt.o2o.client.O2OLib;

public class Login extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private Button loginBtn, signBtn;
	private EditText idEditText, pwEditText;
	private Context context = this;
	private AlertDialog alertdialog;
	private CheckBox isCommer;
	private String userAgent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
	}

	@Override
	protected void onResume() {

		super.onResume();

		Bundle bu = getIntent().getExtras();

		if (bu != null) {
			String url = bu.getString("eventUrl");
			Toast.makeText(this, "url : " + url, 0).show();
		} else {
			Toast.makeText(this, "intent data is null", 0).show();
		}
	}

	private void init() {

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(getResources().getString(
						R.color.navy))));
		
		loginBtn = (Button) findViewById(R.id.loginBtn);
		signBtn = (Button) findViewById(R.id.signBtn);
		idEditText = (EditText) findViewById(R.id.idEditText);
		pwEditText = (EditText) findViewById(R.id.pwEditText);
		loginBtn.setOnClickListener(this);
		signBtn.setOnClickListener(this);
		isCommer = (CheckBox) findViewById(R.id.isCommer);
		isCommer.setOnCheckedChangeListener(this);
		userAgent = "0";
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.loginBtn:
			// 로그인 버튼 누르면
			String id = idEditText.getText().toString();
			String pw = pwEditText.getText().toString();
			login(id, pw);
			break;

		case R.id.signBtn:
			// 가입 버튼 누르면
			showDialog();
			break;
		}

	}

	private void showDialog() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params);
		ContextThemeWrapper newContext = new ContextThemeWrapper(context, R.style.CustomStyleBlueButton);
		Button common = new Button(newContext);
		common.setText("일반");
		ContextThemeWrapper newContext2 = new ContextThemeWrapper(context, R.style.CustomStyleRedButton);
		Button manager = new Button(newContext2);
		manager.setText("관리자");
		layout.addView(common);
		layout.addView(manager);

		alertdialog = new AlertDialog.Builder(context).setView(layout).show();

		common.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertdialog.dismiss();

				Intent intent = new Intent(context, User_Signup.class);
				startActivity(intent);
				Toast.makeText(context, "일반 사용자 가입창", Toast.LENGTH_SHORT)
						.show();

			}
		});

		manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertdialog.dismiss();

				Intent intent = new Intent(context, Comer_Signup.class);
				startActivity(intent);
				Toast.makeText(context, "관리자 가입창", Toast.LENGTH_SHORT).show();

			}
		});
	}

	private void login(final String id, final String pw) {
		String url = staticConst.URL_address + "users/login";
		RequestQueue mQueue = Volley.newRequestQueue(context);

		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				try {
					JSONObject output = new JSONObject(result);
					final String value = output.getString("sending");

					if (value.equals("success")) {
						Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
						
						//사용자가 이벤트 실행 시 통계 보고를 위해 필요한 위치
						if(O2OSampleApplication.eventReceived != null) {
							
							O2OLib.reportEvent(O2OSampleApplication.eventReceived);  
						} 
						O2OSampleApplication.eventReceived = null;  

							
							
						Intent intent = new Intent(context, MainActivity.class);
						startActivity(intent);
						// finish();
					} else {
						Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT)
								.show();

						Log.d("sending", "fail");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};// listener

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(context, "network error: " + arg0,
						Toast.LENGTH_SHORT).show();
				System.out.println("network error: " + arg0);
			}

		};// ErrorListener

		StringRequest request = new StringRequest(Method.POST, url, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "x-www-form-urlencoded");
				params.put("userId", id);
				params.put("userPw", pw);
				params.put("userAgent", userAgent);

				return params;
			}

		};// StringRequest

		mQueue.add(request);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.isCommer) {
			if (isChecked) {
				Toast.makeText(getApplicationContext(), "상인입니다",
						Toast.LENGTH_SHORT).show();
				userAgent = "1";

			} else {
				Toast.makeText(getApplicationContext(), "일반 사용자입니다",
						Toast.LENGTH_SHORT).show();
				userAgent = "0";
			}
		}

	}

}
