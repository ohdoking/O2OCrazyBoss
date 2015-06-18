package com.example.yapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.yapp.Login;
import com.example.yapp.R;
import com.example.yapp.o2o.O2OCallbacks;
import com.skt.o2o.client.WizturnCheckInO2OLib;

public class Intro extends Activity {
	final private String TAG = getClass().getSimpleName();
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		Log.e(TAG, "Intro onCreate");
        
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // O2O API�?초기???�킨??
        //String apiKey = "q04TGu7EW9K69ni0H9OZ7A4c03EO3fr4"; // 교보문고 
        String apiKey = "6EdD89yw5o9m11k5D0g2S6X05R2K2l3P"; // ?�수?�험 ??

		boolean ret = WizturnCheckInO2OLib.initO2OApp(getApplicationContext(), apiKey, O2OCallbacks.o2oCallbacks);
		WizturnCheckInO2OLib.isNotificationEvent(true);
		
      /*  if (!ret) // ?�속?�애. ?�류메세�?�� Callback???�해 ?�출
        {
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
        	alert.setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() {
        	    @Override
        	    public void onClick(DialogInterface dialog, int which) {
        	    dialog.dismiss();     //?�기
        	    }
        	});
        	alert.setMessage("�ʱ�ȭ�� �����Ͽ����ϴ�. �����޼����� Ȯ���Ͻñ� �ٶ��ϴ�.");
        	alert.show();
        }
        else
        {
	      // 3�??�에 intro animation???�행?�킨??
	      new Handler().postDelayed(new Runnable() {
	          public void run() {
	          	introAnimation();        
	          }
	      }, 3000);
        }*/
        
        new Handler().postDelayed(new Runnable() {
	          public void run() {
	          	introAnimation();        
	          }
	      }, 3000);
    }
	
	@Override
    protected void onStart() {
        super.onStart();
        
        Toast.makeText(this, "Intro.onStart", 0).show();
       
    }
	 
	private void introAnimation() {
	
	    final View intro = findViewById(R.id.layIntro);
	
	    Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_down);
	    animation.setAnimationListener(new Animation.AnimationListener() {
	
	        public void onAnimationStart(Animation animation) {
	        }
	
	        public void onAnimationRepeat(Animation animation) {
	        }
	
	        public void onAnimationEnd(Animation animation) {
	            intro.setVisibility(View.GONE);
	
	            Intent intent = new Intent(Intro.this, Login.class);
	
	            if (getIntent() != null && getIntent().getExtras() != null) {
	                intent.putExtras(getIntent().getExtras());
	            }
	
	            startActivity(intent);
	            finish();
	        }
	    });
	
	    intro.startAnimation(animation);
	} 
}
