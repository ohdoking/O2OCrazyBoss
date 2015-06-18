package com.example.yapp;

import java.util.*;

import org.json.*;

import com.android.volley.*;
import com.android.volley.Request.*;
import com.android.volley.Response.*;
import com.android.volley.toolbox.*;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class Comer_Signup extends Activity {

	private EditText id, pw, repw, name, discript;
	private Context context = this;
	private Button nextBtn;
	private MyDataShared mds;
	private Spinner categoryspinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager__signup);

		id = (EditText) findViewById(R.id.signupIdEdit);
		pw = (EditText) findViewById(R.id.signupPwEdit);
		repw = (EditText) findViewById(R.id.rePwEdit);
		name = (EditText) findViewById(R.id.name);
		discript = (EditText) findViewById(R.id.discript);
		nextBtn = (Button) findViewById(R.id.comerNextBtn);
		mds = new MyDataShared(context);
		
		categoryspinner = (Spinner) findViewById(R.id.categoryspinner);

		String[] items = new String[] { "청과", "정육", "수산", "야채", "생필품", "베이커리" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, items);
		categoryspinner.setAdapter(adapter);
		categoryspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String item = String.valueOf(categoryspinner
						.getSelectedItemPosition());
				mds.put("comerCategory", item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		nextBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pw.getText().toString().equals(repw.getText().toString()) != true) {
					Toast.makeText(context, "비밀 번호 불일치", Toast.LENGTH_LONG)
							.show();
				} else {
					mds.put("comerId", id.getText().toString());
					mds.put("comerPw", pw.getText().toString());
					mds.put("comerName", name.getText().toString());
					mds.put("comerDescrib", discript.getText().toString());
					Intent intent = new Intent(context, MapActivity.class);
					intent.putExtra("status", staticConst.ADMIN_STATUS);
					startActivity(intent);
				}

			}

		});
	}
}
