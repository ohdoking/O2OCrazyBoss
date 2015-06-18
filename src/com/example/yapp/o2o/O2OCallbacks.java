package com.example.yapp.o2o;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.skt.o2o.client.IO2OCallbacks;
import com.skt.o2o.client.datamodel.v1.O2OEvent;
import com.skt.o2o.client.datamodel.v1.O2OEventSendConditions;

/*
 * O2O Lib???�태 �?�� �??�벤???�달????�� callback??구현?�여 �?��. 
 */
public interface O2OCallbacks {
    
    public static IO2OCallbacks o2oCallbacks = new IO2OCallbacks() {
        
        final private String TAG = getClass().getSimpleName();

        // Agent???�결??경우 ?�출?�다.
        @Override
        public void onAgentConnected(Context context) {
            Log.d("IO2OCallbacks", "st onAgentConnected ");
            
        }

        // Agent???�결???�어�?�� ?�출?�다.
        @Override
        public void onAgentDisconnected(Context context) {
            Log.d("IO2OCallbacks", "st onAgentDisconnected ");
            
        }

        // 초기???�업???�료?�면 ?�출?�다.
        @Override
        public void onInitCompleted(Context context) {
            Log.d("IO2OCallbacks", "st onInitCompleted ");
        }

        // 초기???�업???�러�?발생?�면 ?�출?�다.
        @Override
        public void onInitError(Context context, int code, String msg) {
            Log.d("IO2OCallbacks", "st onInitError " + msg);
        }

        // ?�벤?��? 발생?�게 ?�면 ?�출?�다.
        @Override
        public void onEventHandle(Context context, O2OEvent event) {
            
            Log.e("IO2OCallbacks", "st onEventHandle " + event.customContentsText);
            
            //pendingintent example..
            PendingIntent pendingIntent;
            Intent intent = new Intent(context, EventView.class); //call event activity..
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.putExtra("eventUrl", event.notification);
            intent.putExtra("eventUrl", event.customContentsMetadata);
            intent.putExtra("metadata", event.customContentsMetadata);
            intent.putExtra("imageurl", event.customContentsImageUrl);
            
            String eventtype  = event.runType;
            
            Log.e("IO2OCallbacks", "eventtype : " + eventtype);
            
            if(event.eventSendCondition == O2OEventSendConditions.ENTER) {
            	intent.putExtra("eventtype", eventtype + " " +  "Enter");
            } 
            
            if(event.eventSendCondition == O2OEventSendConditions.EXIT) {
            	intent.putExtra("eventtype", eventtype + " " +  "Exit");
            } 
            
            if(event.eventSendCondition == O2OEventSendConditions.STAY) {
            	intent.putExtra("eventtype", eventtype + " " +  "Stay");
            } 
            
            pendingIntent =  PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            O2OSampleApplication.eventReceived = event;
            
            try {
                
                pendingIntent.send();
                
                Log.e("IO2OCallbacks", "pendingIntent");
                
            } catch (CanceledException e) {
                
                Log.e(TAG, "CanceledException " + e.getMessage());
                e.printStackTrace();
            }
            
        }

		@Override
		public void onSimpleEventHandle(Context context,
				com.skt.o2o.client.O2OSimpleEvent event) {
			
		}

		@Override
		public void onNoBleEventHandle(Context context, O2OEvent event) {
			
			Log.d("IO2OCallbacks", "onNoBleEventHandle ");
		}
    };
}