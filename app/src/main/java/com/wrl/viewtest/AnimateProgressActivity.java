package com.wrl.viewtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;

import com.wrl.viewtest.views.AnimateProgressView;


public class AnimateProgressActivity extends ActionBarActivity {
    private RelativeLayout mainContent;
    private AnimateProgressView animProgressView;

    private void assignViews() {
        mainContent = (RelativeLayout) findViewById(R.id.mainContent);
        animProgressView = (AnimateProgressView) findViewById(R.id.animProgressView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_progress);
        assignViews();
        animProgressView.setProgress(60);

    }
}
