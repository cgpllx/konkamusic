package com.ouyang.music.showlock;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.konka.music.R;
import com.konka.music.ui.activity.BaseActivity;
import com.kubeiwu.baseclass.util.KLog;



public class ShowLockActivity extends BaseActivity implements OnPageChangeListener{
//	 private List<View> mViewList;//需要滑动的页卡  
	 private ArrayList<Fragment> mFragmentList;//需要滑动的页卡  
	 private ViewPager viewPager;//viewpager  
	 private fgViewPagerAdapter vpAdapter;   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 Log.d("Ouyang", "ShowLockActivity-onCreate");
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_showlock);
		initViewPager();
	}
	
	 private void initViewPager() { 
	        LayoutInflater lyInflater = LayoutInflater.from(this);  
	        mFragmentList = new ArrayList<Fragment>(); 
	        Fragment fgOne= new LockScreenFragmentOne();
	        Fragment fgTwo =  new LockScreenFragmentTwo();
	        mFragmentList.add(fgOne);
	        mFragmentList.add(fgTwo);
	   	     Log.d("Ouyang", "加载数据");
	        // 初始化Adapter  
	   	     try{
	        vpAdapter = new fgViewPagerAdapter(getSupportFragmentManager(),mFragmentList);  	          
	        viewPager = (ViewPager) findViewById(R.id.viewpager);  
	        viewPager.setAdapter(vpAdapter);  
	        viewPager.setCurrentItem(1);//默认显示第二个界面
	        // 绑定回调  
	        viewPager.setOnPageChangeListener(this);   
	        Log.d("Ouyang", "加载数据结束");
	   	     }
	   	     catch(Exception e)
	   	     {
	   	    	 e.printStackTrace();
	   	    	 KLog.e("Ouyang","initViewPager报错");
	   	     }

	    }
	 

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		KLog.d("Ouyang", "ShowLockActivity-onDestroy");
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		KLog.d("Ouyang", "ShowLockActivity-onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		KLog.d("Ouyang", "ShowLockActivity-onResume");
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		Log.d("Ouyang","当前选中："+arg0);
		if(arg0==0)
		{
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK==keyCode)
			return false;
		if(KeyEvent.KEYCODE_MENU==keyCode)
			return false;
		return super.onKeyDown(keyCode, event);
	}
	
	
}
