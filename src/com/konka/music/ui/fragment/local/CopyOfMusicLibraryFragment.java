//2014-8-27
package com.konka.music.ui.fragment.local;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.R;
import com.konka.music.ui.fragment.BigLabelFragment;
import com.konka.music.ui.view.TabPageIndicator;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-27
 */
public class CopyOfMusicLibraryFragment extends Fragment {
	private static final String[] CONTENT = new String[] { "头条", "娱乐", "手机", "游戏", "苹果", "国际", "头条", "娱乐", "手机", "游戏", "苹果", "国际", "头条", "娱乐", "手机",
			"游戏", "苹果", "国际" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.net_simple_tabs, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getChildFragmentManager());

		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(5);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new BigLabelFragment();
//			return NewsFragment.newInstance(CONTENT[position % CONTENT.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}
