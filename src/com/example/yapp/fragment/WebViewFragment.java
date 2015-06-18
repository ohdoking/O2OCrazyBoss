package com.example.yapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.yapp.R;

public class WebViewFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private WebView mWebView;
	
	public WebViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_fragment, container, false);
		
		setLayout(rootView);
		
		mWebView.getSettings().setJavaScriptEnabled(true); 
	    mWebView.loadUrl("https://www.wizturnhub.com/service/login.poc");
	    mWebView.setWebViewClient(new WebViewClientClass());  
	    
		/*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
		dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
		return rootView;
	}
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {       
	        super.onActivityCreated(savedInstanceState);
	    }
	 
	 
    private class WebViewClientClass extends WebViewClient { 
        @Override 
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
    
    private void setLayout(View rootView){
		mWebView = (WebView) rootView.findViewById(R.id.webview);
	}
    
    
}