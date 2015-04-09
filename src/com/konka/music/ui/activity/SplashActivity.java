package com.konka.music.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.konka.music.R;
import com.konka.music.util.MyPreference;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.splash);
		
		 if (MyPreference.getPref("first_loaded", true)) {
				 MyPreference.putPref("first_loaded", false);
				 startActivity(new Intent(this, GuideActivity.class));
				 finish();
				 }else{
					 new Handler().postDelayed(new Runnable() {
						 
						 @Override
						 public void run() {
							 startActivity(new Intent(SplashActivity.this, MainActivity.class));
							 SplashActivity.this.finish();
						 }
					 }, 1000);
				 }
		
		
		 
	}

	/**
	 * 退出健监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return true;
	}
}
