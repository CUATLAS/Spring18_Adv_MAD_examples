package com.example.aileen.spring;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class OrderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //get reference to action bar
        ActionBar actionBar = getActionBar();
        //enable the up button
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
