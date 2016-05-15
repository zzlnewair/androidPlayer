package com.zzl.mobileplayer.ui.activity;

import com.zzl.mobileplayer.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.WindowManager;

public class SplashActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		delayEnterMainActivity(true);
		
	}
	
	private void delayEnterMainActivity(boolean isDelay){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!hasEnterMain){
					hasEnterMain = true;
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
					finish();
				}
			}
		}, isDelay?2000:0);
	}
	
	private boolean hasEnterMain = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			delayEnterMainActivity(false);
			break;
		}
		return super.onTouchEvent(event);
	}
}
