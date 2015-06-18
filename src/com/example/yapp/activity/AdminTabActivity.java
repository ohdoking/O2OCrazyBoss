package com.example.yapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.yapp.R;
import com.example.yapp.tab.SlidingFragment;

public class AdminTabActivity extends FragmentActivity{
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
        setUpFragment();
    }
    void setUpView(){
         setContentView(R.layout.activity_admin_tab);
    }
    void setUpFragment(){
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         SlidingFragment fragment = new SlidingFragment();
         transaction.replace(R.id.sample_content_fragment, fragment);
         transaction.commit();
    }
}