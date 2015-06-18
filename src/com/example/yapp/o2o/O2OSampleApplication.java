package com.example.yapp.o2o;

import android.app.Application;

import com.skt.o2o.client.datamodel.v1.O2OEvent;

public class O2OSampleApplication extends Application {
	public static O2OSampleApplication  instance;
	public static O2OEvent				eventReceived=null;
	
	public O2OSampleApplication() {
		instance = this; 
	}
	public static O2OSampleApplication getInstance() {
		return instance; 
	}
}
