package com.konka.music.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.konka.music.R;
import com.kubeiwu.baseclass.util.KLog;

public class GuideActivity extends SuperActivity implements OnClickListener{
	
	private static final String TAG = "GuideActivity";

	private ViewPager mViewPager;
	private GuideAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_guide_layout);
		this.mViewPager = (ViewPager)findViewById(R.id.guide_viewpager);
		
		this.mAdapter = new GuideAdapter();
		this.mViewPager.setAdapter(this.mAdapter);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_in:
			goMainActivity();
			break;
		}
	}
	
	private void goMainActivity() {
		startActivity(new Intent(this, SplashActivity.class));
		this.finish();
	}
	
	
	class GuideAdapter extends PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
			object = null;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			
			KLog.i(TAG, "instantiateItem->position is " + position);
			View view = null;
			switch (position) {
			case 0:
				view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_layout_item1, null);
				container.addView(view, position);
				return view;
			case 1:
				view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_layout_item2, null);
				container.addView(view, position);
				return view;
			case 2:
				view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_layout_item3, null);
				view.findViewById(R.id.guide_in).setOnClickListener(GuideActivity.this);
				container.addView(view, position);
				return view;
			}
			
			return super.instantiateItem(container, position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (View)arg1;
		}

	}
}
