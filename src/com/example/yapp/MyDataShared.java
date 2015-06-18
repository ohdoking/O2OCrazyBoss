package com.example.yapp;

import android.app.*;
import android.content.*;

public class MyDataShared {
	private final String PREF_NAME = "PREF";
	static Context mContext;
	private SharedPreferences pref;

	public MyDataShared(Context c) {
		mContext = c;
		
		pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	public void put(String key, String value) {
		SharedPreferences.Editor editor = pref.edit();

		editor.putString(key, value);
		editor.commit();
	}

	public void put(String key, boolean value) {
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean(key, value);
		editor.commit();
	}

	public void put(String key, int value) {
		SharedPreferences.Editor editor = pref.edit();

		editor.putInt(key, value);
		editor.commit();
	}

	public String getValue(String key, String dftValue) {
		try {
			return pref.getString(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}

	}

	public int getValue(String key, int dftValue) {
		try {
			return pref.getInt(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}

	}

	public boolean getValue(String key, boolean dftValue) {
		try {
			return pref.getBoolean(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}
	}
}
