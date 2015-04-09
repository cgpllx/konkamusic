package com.konka.music.ui.fragment.downloadmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.fragment.KFragmentTabsPager;

public class DownloadParentFragment extends KFragmentTabsPager {
	public static final String TAG = DownloadParentFragment.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().setTitle("下载管理");
	}

	// private View rootView;
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// if (rootView == null) {
	// rootView = super.onCreateView(inflater, container, savedInstanceState);
	// } else {
	// ViewGroup group = (ViewGroup) rootView.getParent();
	// if (group != null) {
	// group.removeView(rootView);
	// }
	// }
	// return rootView;
	// }

	// @SuppressLint("InflateParams")
	@Override
	protected void initTab(TabsPagerAdapter mTabsAdapter, TabHost mTabHost) {
		for (int i = 0; i < fragments.length; i++) {
			View view = View.inflate(getActivity(), R.layout.singertab_item_view, null);
			TextView textView = ViewUtility.findViewById(view, R.id.singertab_textview);
			textView.setText(tabsTitle[i]);
			mTabsAdapter.addTab(mTabHost.newTabSpec(tabsTitle[i]).setIndicator(view), fragments[i], null);
		}
	}

	String[] tabsTitle = { "正在下载", "下载完成" };

	Class<?>[] fragments = { DownloadIngFragment.class, DownloadFinishFragment.class };

	@Override
	public TabConfig getTabConfig() {
		TabConfig tabConfig = new TabConfig();
		tabConfig.setWidgetBackgroundResource(R.drawable.singertab_weiget_bg);
		tabConfig.setGravity(TabConfig.TabGravity.TOP);
		return tabConfig;
	}
}
