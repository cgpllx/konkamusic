//2014-8-27
package com.konka.music.ui.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.konka.music.R;
import com.konka.music.loader.BigLabelInfoLoader;
import com.konka.music.pojo.BigLabelInfo;
import com.konka.music.ui.view.TabPageIndicator;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-27
 */
public class MusicLibraryFragment extends BaseLoaderCallbacksFragment<ArrayList<BigLabelInfo>> implements OnClickListener {
	public static final String TAG = MusicLibraryFragment.class.getName();

	public static MusicLibraryFragment newInstance() {
		MusicLibraryFragment musicLibraryFragment = new MusicLibraryFragment();
		return musicLibraryFragment;
	}

	private ViewHolder viewHolder;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.net_simple_tabs);
		return rootView;
	}

	class ViewHolder {
		MusicAdapter adapter;
		TabPageIndicator indicator;
		ViewPager pager;
		View fail;
		Button btn_refresh;
		View emptyView;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (rootView.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.adapter = new MusicAdapter(getChildFragmentManager());

			viewHolder.pager = (ViewPager) view.findViewById(R.id.pager);

			viewHolder.pager.setOffscreenPageLimit(2);
			viewHolder.pager.setAdapter(viewHolder.adapter);
			viewHolder.indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
			viewHolder.indicator.setViewPager(viewHolder.pager);
			viewHolder.fail = ViewUtility.findViewById(view, R.id.refresh_layout);
			viewHolder.btn_refresh = ViewUtility.findViewById(viewHolder.fail, R.id.btn_refresh, this);
			viewHolder.emptyView = ViewUtility.findViewById(view, R.id.empty_loading);
			rootView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rootView.getTag();
		}
		viewinitcomplete = true;
		lazyLoad();
		System.out.println("MusicLibraryFragment--onViewCreated"+this);
	}

	private boolean viewinitcomplete = false;

	@Override
	protected void lazyLoad() {
		super.lazyLoad();
		if (viewinitcomplete && isVisible) {
			getLoaderManager().initLoader(Assist.MUSICLIBRARY_LOADER_ID, null, this);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getActivity().getActionBar().setTitle("乐库");
	}

	@Override
	public Loader<ArrayList<BigLabelInfo>> onCreateLoader(int arg0, Bundle arg1) {
		viewHolder.fail.setVisibility(View.GONE);
		viewHolder.emptyView.setVisibility(View.VISIBLE);
		return new BigLabelInfoLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<BigLabelInfo>> arg0, ArrayList<BigLabelInfo> bigLabelInfos) {
		super.onLoadFinished(arg0, bigLabelInfos);

		if (!ArrayUtils.isEmpty(bigLabelInfos) && viewHolder.adapter.getCount() == 0) {
			viewHolder.adapter.setBigLabelInfos(bigLabelInfos);
			viewHolder.indicator.notifyDataSetChanged();
		} else {
			if (viewHolder.adapter.getCount() == 0) {
				viewHolder.fail.setVisibility(View.VISIBLE);
			}
		}
		viewHolder.emptyView.setVisibility(View.GONE);
	}

	class MusicAdapter extends FragmentPagerAdapter {
		ArrayList<BigLabelInfo> bigLabelInfos;

		public MusicAdapter(FragmentManager fm) {
			super(fm);
			bigLabelInfos = new ArrayList<BigLabelInfo>();
		}

		public void setBigLabelInfos(ArrayList<BigLabelInfo> bigLabelInfos) {
			if (bigLabelInfos != null) {
				this.bigLabelInfos.addAll(bigLabelInfos);
				notifyDataSetChanged();
			}
		}

		@Override
		public Fragment getItem(int position) {
			return BigLabelFragment.newInstance(bigLabelInfos.get(position).getId());
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return bigLabelInfos.get(position).getBigLabelName();
		}

		@Override
		public int getCount() {
			return bigLabelInfos.size();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:
			getLoaderManager().restartLoader(Assist.MUSICLIBRARY_LOADER_ID, null, this);
			break;
		}
	}
}
