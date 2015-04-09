package com.konka.music.ui.fragment.singer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.fragment.KFragmentTab;

public class SingerTabFragment extends KFragmentTab {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=super.onCreateView(inflater, container, savedInstanceState);
		View root=inflater.inflate(R.layout.singertabfragment_layout, container, false);
		FrameLayout frameLayout=ViewUtility.findViewById(root, R.id.singertabfragment);
		frameLayout.addView(view);
		getActivity().getActionBar().setTitle("歌手");
		return root;

	}

	@SuppressLint("InflateParams")
	@Override
	protected void initTab(FragmentTabHost mTabHost) {
		for (int i = 0; i < singername.length; i++) {
			Bundle bundle = new Bundle();
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.singertab_item_view, null);
			TextView textView = ViewUtility.findViewById(view, R.id.singertab_textview);
			bundle.putString("area", areas[i]);
			textView.setText(singername[i]);
			mTabHost.addTab(mTabHost.newTabSpec(areas[i]).setIndicator(view), SingerChileFragment.class, bundle);
		}
	}

	@Override
	public TabConfig getTabConfig() {
		TabConfig config = new TabConfig();
		config.setWidgetBackgroundResource(R.drawable.singertab_weiget_bg);
		return config;
	}

	private String[] singername = { "华语", "欧美", "日韩" };
	private String[] areas = { "cn", "western", "kr" };// cn， western， kr ， jp

}
