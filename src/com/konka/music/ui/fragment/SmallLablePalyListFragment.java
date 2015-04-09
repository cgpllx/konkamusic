package com.konka.music.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.konka.music.R;
import com.konka.music.adapter.TrackAdapter;
import com.konka.music.loader.SmallLablePlayListLoader;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBaseListFragment_MusicInfoArray;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.ViewTag;
import com.konka.music.util.ViewUtility;

public class SmallLablePalyListFragment extends KBaseListFragment_MusicInfoArray implements OnScrollListener {
	// private listViewAdapter listViewAdapter;
	
	private static final String SMALLLABLE_ID_KEY = "smallLable_key";
	private static final String SMALLLABLE_IMAGE_KEY = "smalllable_image_key";
	private static final String SMALLLABLE_NAME_KEY = "smalllable_name_key";
	// private static final String SMALLLABLE_KEY = "smallLable_key";
	public static final String TAG = SmallLablePalyListFragment.class.getSimpleName();

	public static SmallLablePalyListFragment newInstance(int smallLabelId, String imageurl, String name) {
		SmallLablePalyListFragment bigLabelFragment = new SmallLablePalyListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(SMALLLABLE_ID_KEY, smallLabelId);
		bundle.putString(SMALLLABLE_IMAGE_KEY, imageurl);
		bundle.putString(SMALLLABLE_NAME_KEY, name);
		bigLabelFragment.setArguments(bundle);
		return bigLabelFragment;
	}

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.net_list_track);
		getActivity().getActionBar().setTitle(getArguments().getString(SMALLLABLE_NAME_KEY, getString(R.string.app_name)));
		return rootView;
	}

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if(rootView.getTag()==null){
			initListView(view);
			rootView.setTag(smallLablePalyListFragmentHolder);
		}else{
			smallLablePalyListFragmentHolder=(SmallLablePalyListFragmentHolder) rootView.getTag();
		}
		getLoaderManager().initLoader(Assist.SMALLLABLE_LOADER_ID, getArguments(), this);
	}




	SmallLablePalyListFragmentHolder smallLablePalyListFragmentHolder;
	class SmallLablePalyListFragmentHolder extends KBaseHolder{
		  View suspendheader;
		  View common_play_list_header_bar_allplay1;// 全部播放
		  View common_play_list_header_bar_allplay2;// 全部播放
		  View headerImage;
		  View header;
		  View mView_EmptyLoading;
		  ImageView net_list_header_imageView;
		  View fail;
	}
	private void initListView(View view) {
		smallLablePalyListFragmentHolder=new SmallLablePalyListFragmentHolder();
		smallLablePalyListFragmentHolder.mView_EmptyLoading = view.findViewById(R.id.empty_loading);
		smallLablePalyListFragmentHolder.suspendheader = View.inflate(getActivity(), R.layout.common_play_list_header_bar, null);
		smallLablePalyListFragmentHolder.common_play_list_header_bar_allplay1 = ViewUtility.findViewById(smallLablePalyListFragmentHolder.suspendheader, R.id.common_play_list_header_bar_allplay, this);
		smallLablePalyListFragmentHolder.common_play_list_header_bar_allplay2 = ViewUtility.findViewById(view, R.id.common_play_list_header_bar_allplay, this);
		smallLablePalyListFragmentHolder.	common_play_list_header_bar_allplay1.setTag(ViewTag.PLAYALL);
		smallLablePalyListFragmentHolder.common_play_list_header_bar_allplay2.setTag(ViewTag.PLAYALL);
		smallLablePalyListFragmentHolder.	fail = ViewUtility.findViewById(view, R.id.refresh_layout, this);
		// progressLayout = ViewUtility.findViewById(view,
		// R.id.progress_layout);

		ViewUtility.findViewById(smallLablePalyListFragmentHolder.fail, R.id.btn_refresh, this);
		smallLablePalyListFragmentHolder.headerImage = View.inflate(getActivity(), R.layout.net_list_header, null);

		smallLablePalyListFragmentHolder.kBaseListView = ViewUtility.findViewById(view, R.id.net_listview_music);
		smallLablePalyListFragmentHolder.header = ViewUtility.findViewById(view, R.id.header);
		smallLablePalyListFragmentHolder.net_list_header_imageView = ViewUtility.findViewById(smallLablePalyListFragmentHolder.headerImage, R.id.net_list_header_imageView);

		smallLablePalyListFragmentHolder.kBaseListView.addHeaderView(smallLablePalyListFragmentHolder.headerImage);
		smallLablePalyListFragmentHolder.kBaseListView.addHeaderView(smallLablePalyListFragmentHolder.suspendheader);
		smallLablePalyListFragmentHolder.kBaseListView.setEmptyView(smallLablePalyListFragmentHolder.mView_EmptyLoading);
		smallLablePalyListFragmentHolder.listViewAdapter = new TrackAdapter(getActivity(), this);
		smallLablePalyListFragmentHolder.kBaseListView.setAdapter(smallLablePalyListFragmentHolder.listViewAdapter);
		smallLablePalyListFragmentHolder.kBaseListView.setOnItemClickListener(this);
		registerForContextMenu(smallLablePalyListFragmentHolder.kBaseListView);
		smallLablePalyListFragmentHolder.kBaseListView.setOnScrollListener(this);
		Assist.imageLoader.displayImage(getArguments().getString(SMALLLABLE_IMAGE_KEY), smallLablePalyListFragmentHolder.net_list_header_imageView, Assist.options);
		// fail.setVisibility(View.VISIBLE);
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		smallLablePalyListFragmentHolder.fail.setVisibility(View.GONE);
		return new SmallLablePlayListLoader(getActivity(), String.format(Assist.SMALLLABLE_URL, arg1.getInt(SMALLLABLE_ID_KEY, 0)));
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<MusicInfo>> arg0, ArrayList<MusicInfo> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1)) {
			smallLablePalyListFragmentHolder.listViewAdapter.addAll(arg1);
			smallLablePalyListFragmentHolder.listViewAdapter.notifyDataSetChanged();
		} else {
			// if (listViewAdapter.getCount() == getHeardercCount()) {
			smallLablePalyListFragmentHolder.fail.setVisibility(View.VISIBLE);
			// }
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_refresh:
			getLoaderManager().restartLoader(Assist.SMALLLABLE_LOADER_ID, getArguments(), this);
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_MENU_DOWNLOAD, Menu.NONE, "下载");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		smallLablePalyListFragmentHolder.header.setVisibility(firstVisibleItem >= 1 ? View.VISIBLE : View.GONE);
	}

	@Override
	protected KBaseHolder getKBaseHolder() {
		return smallLablePalyListFragmentHolder;
	}

 
}
