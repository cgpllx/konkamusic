package com.konka.music.ui.fragment.abstractfragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.TrackAdapter;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.activity.MainActivity;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.ViewUtility;
import com.konka.music.util.WindowUtil;

public abstract class KBasePlayListFragment extends KBaseListFragment_MusicInfoArray{
	public static final String TAG = KBasePlayListFragment.class.getSimpleName();

	private View rootView = null;
	private KBasePlayListFragmentHolder basePlayListFragmentHolder;
	private boolean viewinitComplete = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.list_track); 
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (rootView.getTag() == null) {
			basePlayListFragmentHolder = new KBasePlayListFragmentHolder();
			basePlayListFragmentHolder.mView_ListView = (ListView) rootView.findViewById(R.id.listview_local_music);
			basePlayListFragmentHolder.mView_EmptyLoading = rootView.findViewById(R.id.empty_loading);
			basePlayListFragmentHolder.nodata_textView = ViewUtility.findViewById(rootView, R.id.nodata_textView);
			basePlayListFragmentHolder.listViewAdapter = new TrackAdapter(getActivity(), this);
			basePlayListFragmentHolder.mView_ListView.setAdapter(basePlayListFragmentHolder.listViewAdapter);
			registerForContextMenu(basePlayListFragmentHolder.mView_ListView);
			basePlayListFragmentHolder.mView_ListView.setOnItemClickListener(this);
			basePlayListFragmentHolder.mView_ListView.setEmptyView(basePlayListFragmentHolder.mView_EmptyLoading);
			ViewUtility.findViewById(getActivity(), R.id.common_play_list_header_bar_allplay, this);
			rootView.setTag(basePlayListFragmentHolder);
		} else {
			basePlayListFragmentHolder = (KBasePlayListFragmentHolder) rootView.getTag();
		}
		viewinitComplete = true;
		lazyLoad();
	}

	private void startLoader() {
		getLoaderManager().initLoader(MUSIC_RETRIEVE_LOADER, getArguments(), this);
		System.out.println("startLoader");
	}

	@Override
	protected void lazyLoad() {
		super.lazyLoad();
		if (viewinitComplete && isVisible && attactivity) {
			startLoader();
		}
	}

	private boolean attactivity = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("显示了吗isVisible="+isVisible);
		System.out.println("view初始化完成了吗viewinitComplete="+viewinitComplete);
		attactivity = true;
		lazyLoad();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		setUserVisibleHint(false);
		attactivity = false;
//		getLoaderManager().destroyLoader(MUSIC_RETRIEVE_LOADER);
	}

	class KBasePlayListFragmentHolder extends KBaseHolder{
		ListView mView_ListView;
		View mView_EmptyLoading;
		ArrayList<MusicInfo> localMusicInfos;
		TextView nodata_textView;
		// TrackAdapter mAdapter;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.common_play_list_header_bar_allplay:
			if (!ArrayUtils.isEmpty(basePlayListFragmentHolder.localMusicInfos)) {
				MusicInfoManager.addMusicInfoArray(getActivity(), basePlayListFragmentHolder.localMusicInfos, true);
			}
			break;
		case R.id.btn_toggle_menu:
			break;
		}

	}


//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		try {
//			int[] location = WindowUtil.getViewInTheWindowPosition(view);
//			MainActivity mainActivity = (MainActivity) getActivity();
//			mainActivity.playAnimotion_playThisMusic(location[0], location[1] - view.getHeight(), basePlayListFragmentHolder.localMusicInfos.get(position));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		basePlayListFragmentHolder.nodata_textView.setVisibility(View.GONE);
		return super.onCreateLoader(arg0, arg1);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<MusicInfo>> arg0, ArrayList<MusicInfo> arg1) {
		if (!ArrayUtils.isEmpty(arg1)) {
			basePlayListFragmentHolder.listViewAdapter.setmItems(arg1);
			this.basePlayListFragmentHolder.localMusicInfos = arg1;
		} else {
			// TODO 显示没有数据
			basePlayListFragmentHolder.nodata_textView.setVisibility(View.VISIBLE);
			this.basePlayListFragmentHolder.localMusicInfos = null;
			if (basePlayListFragmentHolder.listViewAdapter != null) {
				basePlayListFragmentHolder.listViewAdapter.clear();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<MusicInfo>> arg0) {
		// no used
	}
	@Override
	protected KBaseHolder getKBaseHolder() {
		return basePlayListFragmentHolder;
	}

	protected static final int MUSIC_RETRIEVE_LOADER = 10;
}
