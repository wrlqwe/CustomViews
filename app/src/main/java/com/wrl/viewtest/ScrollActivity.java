package com.wrl.viewtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ScrollView;

import com.wrl.viewtest.views.OutScrollView;


public class ScrollActivity extends ActionBarActivity {

    private OutScrollView outScrollView;
    private ScrollView innerScrollView;

    private void assignViews() {
        outScrollView = (OutScrollView) findViewById(R.id.outScrollView);
        innerScrollView = (ScrollView) findViewById(R.id.innerScrollView);
        outScrollView.setInnerView(innerScrollView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
    }

}
