package com.wrl.viewtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        animProgressView.setProgress(100);
        animProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animProgressView.setProgress(0);
            }
        },1500);
        TransitionManager.beginDelayedTransition(mainContent,new Fade(Fade.IN));
        TextView tv = new TextView(this);
        tv.setText("hehehehe");
        tv.setTextSize(60);
        mainContent.addView(tv);

    }
}
