package com.example.yapp;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class PushSetting extends Activity implements OnClickListener {
	private Context context = this;
	MyDataShared mds;

	private Button next;
	private List<Button> buttons;
	private int[] status = { 0, 0, 0, 0, 0, 0 };
	private static final int[] BUTTON_IDS = { R.id.button1, R.id.button2,
			R.id.button3, R.id.button4, R.id.button5, R.id.button6 };
	ArrayList<String> userFavarite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_setting);
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(getResources().getString(
						R.color.navy))));

		mds = new MyDataShared(context);

		System.out.println("userId = " + mds.getValue("userId", ""));
		System.out.println("userPw = " + mds.getValue("userPw", ""));
		buttons = new ArrayList<Button>();
		for (int id : BUTTON_IDS) {
			Button button = (Button) findViewById(id);
			button.setOnClickListener(this); // maybe
			buttons.add(button);
			// button.setBackgroundColor(Color.GRAY);
		}

		next = (Button) findViewById(R.id.nextBtn);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userFavarite = new ArrayList<String>();

				if (status.length == 0) {
					Toast.makeText(context, "理쒖냼 1媛쒖씠���좏깮!", Toast.LENGTH_SHORT)
							.show();
				} else {

					for (int i = 0; i < status.length; i++) {
						if (status[i] == 1) // �좏깮���섏뿀����
						{
							userFavarite.add(String.valueOf(i));
						}
					}
					// ��옣�쒓쾬 �뺤씤�섍린
					String favoriteStr = "";
					for (int i = 0; i < userFavarite.size(); i++) {
						// System.out.println(userFavarite.get(i));
						if (i < userFavarite.size() - 1) {
							favoriteStr += userFavarite.get(i);
							favoriteStr += ",";
						} else
							favoriteStr += userFavarite.get(i);

					}
					System.out.println("userFavorite = " + favoriteStr);

					// �먯뼱�쒖뿉 ��옣
					mds.put("userFavorite", favoriteStr);
					Intent intent = new Intent(context, MapActivity.class);
					intent.putExtra("status", staticConst.USERS_STATUS);
					startActivity(intent);
					Toast.makeText(context, "�몄돩 �ㅼ젙 �꾨즺", Toast.LENGTH_SHORT)
							.show();
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			if (status[0] == 0) {
				buttons.get(0).setTextColor(Color.RED);
				status[0] = 1;
			} else {
				buttons.get(0).setTextColor(Color.BLACK);
				status[0] = 0;

			}
			break;

		case R.id.button2:
			if (status[1] == 0) {
				buttons.get(1).setTextColor(Color.RED);
				status[1] = 1;
			} else {
				buttons.get(1).setTextColor(Color.BLACK);
				status[1] = 0;

			}
			break;
		case R.id.button3:
			if (status[2] == 0) {
				buttons.get(2).setTextColor(Color.RED);
				status[2] = 1;
			} else {
				buttons.get(2).setTextColor(Color.BLACK);
				status[2] = 0;

			}

			break;
		case R.id.button4:
			if (status[3] == 0) {
				buttons.get(3).setTextColor(Color.RED);
				status[3] = 1;
			} else {
				buttons.get(3).setTextColor(Color.BLACK);
				status[3] = 0;

			}

			break;
		case R.id.button5:
			if (status[4] == 0) {
				buttons.get(4).setTextColor(Color.RED);
				status[4] = 1;
			} else {
				buttons.get(4).setTextColor(Color.BLACK);
				status[4] = 0;

			}

			break;
		case R.id.button6:
			if (status[5] == 0) {
				buttons.get(5).setTextColor(Color.RED);
				status[5] = 1;
			} else {
				buttons.get(5).setTextColor(Color.BLACK);
				status[5] = 0;

			}

			break;
		}

	}

}
