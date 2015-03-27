package com.wrl.viewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class LauncherActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
	}

	public void onScrollClick(View v) {
		startActivity(new Intent(this, ScrollActivity.class));
	}

	public void onArcMenuClick(View v) {
		startActivity(new Intent(this, ArcMenuActivity.class));
	}

	public void onNormalSlideClick(View v) {
		startActivity(new Intent(this, NormalSlideActivity.class));
	}

	public void onAnimateProgressClick(View v) {
		startActivity(new Intent(this, AnimateProgressActivity.class));
	}

	public void onOGLESClick(View v) {
		startActivity(new Intent(this, OpenGLESActivity.class));
	}

	public void onQQSlideClick(View v) {
		startActivity(new Intent(this, QQSlideActivity.class));
	}
	public void onRatioImageViewClick(View v) {
		startActivity(new Intent(this, RatioImageViewActivity.class));
	}
}
