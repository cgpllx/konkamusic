package com.ouyang.music.showlock;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class fgViewPagerAdapter extends FragmentPagerAdapter{

	private ArrayList<Fragment> list;
	
	public fgViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
