package com.example.yapp.o2o;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yapp.R;
import com.skt.o2o.client.WizturnCheckInO2OLib;

/*
 * EventÎ•?Î∞õÏ? Í≤ΩÏö∞ WebView ?îÎ©¥??URL??Î°úÎìú?òÏó¨ ?úÏãú?òÏó¨ Ï§?ã§.
 */
public class EventView extends Activity {

    private String TAG = getClass().getSimpleName();
    Context mCtx;
    private TextView versionLabel;
    private WebView wvMain;
    private Button btn_event;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        mCtx = this;
        btn_event = (Button) findViewById(R.id.btn_event);
        wvMain = (WebView) findViewById(R.id.wvMain);
        versionLabel = (TextView)findViewById(R.id.versionLabel);
        
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.setWebChromeClient(new WebChromeClient()); 
        
        WebViewClient webviewc = new WebViewClient() {

        	//?πÌôîÎ©¥Ïóê??ÎßÅÌÅ¨ ?¥Î¶≠???¥Î≤§???ÑÌôò????ïú ?ïÎ≥¥Î•??ÑÎã¨?úÎã§.
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
        };
        wvMain.setWebViewClient(webviewc);
        
        setEventListener();
        
        int remoteVersion = com.skt.o2o.agentlib.spt.ServiceConstant.SERVICE_VERSION;
        int runningRemoteVersion = WizturnCheckInO2OLib.getinstance(getApplicationContext()).getRemoteVersion();
        
        if (runningRemoteVersion == 0)
        {
        	versionLabel.setText("Sample22, remoteVer:"+remoteVersion + ", runningRemote:not bind"); 
        }
        else
        {
	        versionLabel.setText("Sample22, remoteVer:"+remoteVersion + ", runningRemote:" + 
	        		WizturnCheckInO2OLib.getinstance(getApplicationContext()).getServicePackageName() + "(" +
	        		runningRemoteVersion + ")");
        }
    }
    


	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}



	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}


	// ?¥Î≤§?∏Í? Î∞úÏÉù??Í≤ΩÏö∞??url???àÎ°ú Î°úÎìú?úÌÇ®??
	@Override
    protected void onResume() {
        super.onResume();
        
        String url = null;
        String eventtype = null;
        String metadata = null;
        String imageurl = null;
        
        try {
            url = getIntent().getExtras().getString("eventUrl");
            
            eventtype =     getIntent().getExtras().getString("eventtype");
            metadata = getIntent().getExtras().getString("metadata");
            imageurl = getIntent().getExtras().getString("imageurl");
            
        } catch (Exception e) {
            Log.e(TAG, "fail event view : " + e.getMessage());
            finish();
        }
        
        Log.d(TAG, "eventView Url : " + url);
        
        if(url == null || url.equals("")) {
            url = "http://m.naver.com";
        }
        
        if( eventtype == null || eventtype.equals("")) {
            eventtype = "";
        }
        
        if( metadata == null || metadata.equals("")) {
        	metadata = "";
        }
        
        if ( imageurl == null || imageurl.equals("")) {
        	imageurl = "";
        }
        
        this.setTitle("O2O Event - [" + eventtype + "]");
        
        // ?¥Î≤§?∏Î? ?µÌï¥ ?ÑÎã¨Î∞õÏ? ?ïÎ≥¥Î•??†Ïä§??Î©îÏãúÏß?°ú Î≥¥Ïó¨Ï§?ã§.
        Toast.makeText(this, "CallEventView : [" + url + "]\n" +
        		             "medadata : ["   + metadata +  "]\n" +
        		             "imageurl : ["   + imageurl +  "]" 
        		, Toast.LENGTH_LONG).show();
        
        Vibrator vibe =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(1000);
        
        wvMain.loadUrl(url);

    }
    
	
	 private void setEventListener() {

    	btn_event.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			if(O2OSampleApplication.eventReceived != null) {
    	        	Toast.makeText(getApplicationContext(),  "O2OLib.reportEvent()(Main.java)",Toast.LENGTH_SHORT).show();
    	        	WizturnCheckInO2OLib.reportEvent(O2OSampleApplication.eventReceived);
    	        }
    	        O2OSampleApplication.eventReceived = null;
    		}
    	});
	}
}