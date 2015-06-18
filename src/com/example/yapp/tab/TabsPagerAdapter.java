package com.example.yapp.tab;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yapp.R;

public class TabsPagerAdapter extends PagerAdapter {
    String tabs[]={"Test 1","Test 2"};
    Activity activity;
    WebView webview;
    
    
    public TabsPagerAdapter(Activity activity){
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return tabs.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }
 
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view=null;
        if(position%2==0){
          view = getViewForPageOne(container);
        }else{
          view = activity.getLayoutInflater().inflate(R.layout.page_item_2,container, false);
        }
        // Add the newly created View to the ViewPager
        container.addView(view);
 
        // Retrieve a TextView from the inflated View, and update it's text
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(tabs[position]);
 
        // Return the View
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    
    private View getViewForPageOne(ViewGroup container){
        View v = activity.getLayoutInflater().inflate(R.layout.web_view,container, false);
        
         webview = (WebView)v.findViewById(R.id.webview);
         webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
         webview.setWebViewClient(new WebClient()); // 응룡프로그램에서 직접 url 처리
         WebSettings set = webview.getSettings();
         set.setJavaScriptEnabled(true);
         set.setBuiltInZoomControls(true);
         webview.loadUrl("http://www.google.com");
           
         
         return v;
    }
    
 
    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}