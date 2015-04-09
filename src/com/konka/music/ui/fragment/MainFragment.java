package com.konka.music.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.music.R;
import com.konka.music.loader.MusicRetrieveLoader;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.activity.AlarmActivity;
import com.konka.music.ui.activity.MainActivity;
import com.konka.music.ui.activity.SettingActivityFragment;
import com.konka.music.ui.fragment.abstractfragment.KBaseFragment;
import com.konka.music.ui.fragment.downloadmanager.DownloadParentFragment;
import com.konka.music.ui.fragment.local.FavouriteFragment;
import com.konka.music.ui.fragment.local.HistoryFragment;
import com.konka.music.ui.fragment.local.LocalFragment;
import com.konka.music.ui.fragment.mysonglist.MyClassifyList_Fragment;
import com.konka.music.ui.fragment.singer.SingerTabFragment;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.FragmentCache;
import com.konka.music.util.FragmentManagerUtil;
import com.konka.music.util.ObjectUtil;
import com.konka.music.util.ViewTag;
import com.konka.music.util.ViewUtility;
import com.konka.music.util.WindowUtil;

public class MainFragment extends KBaseFragment<ArrayList<MusicInfo>> implements OnClickListener {
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(R.string.app_name);
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.main_layout);
		rootView.setOnClickListener(this);
		return rootView;

	}

	class MainFragmentViewHolder {
		View searchMusic;// 搜索音乐
		View lacolMusic;// 本地音乐
		View favourite;// 我喜欢的
		View myClassify;// 我的分类
		View downloadManager;// 下载管理
		View historyplay;// 历史记录
		View musicLibrary;// 乐库
		View singer;// 歌手
		View hotMusic;// 热门
		View topList;// 榜单
		View setting;// 设置
		View timing;// 定时
		View palylocalmusic_imageview;
		TextView localmusic_count_textView;

		ImageButton navigation_search_button;
		AutoCompleteTextView navigation_search_edit;
	}

	private MainFragmentViewHolder mainFragmentViewHolderviewHolder;

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initMainViewHolder(view);
		getLoaderManager().initLoader(Assist.MAIN_LOCALMUSIC_LOADER_ID, getArguments(), this);
	}

	private void initMainViewHolder(View view) {
		mainFragmentViewHolderviewHolder = new MainFragmentViewHolder();
		mainFragmentViewHolderviewHolder.searchMusic = ViewUtility.findViewById(view, R.id.navigation_search_button, this);
		mainFragmentViewHolderviewHolder.lacolMusic = ViewUtility.findViewById(view, R.id.localmusic_LinearLayout, this);
		mainFragmentViewHolderviewHolder.favourite = ViewUtility.findViewById(view, R.id.navigation_localentry_fav, this);
		mainFragmentViewHolderviewHolder.myClassify = ViewUtility.findViewById(view, R.id.navigation_localentry_playlist, this);
		mainFragmentViewHolderviewHolder.downloadManager = ViewUtility.findViewById(view, R.id.navigation_localentry_download, this);
		mainFragmentViewHolderviewHolder.historyplay = ViewUtility.findViewById(view, R.id.navigation_localentry_history, this);
		mainFragmentViewHolderviewHolder.musicLibrary = ViewUtility.findViewById(view, R.id.navigation_netmusic_finder, this);
		mainFragmentViewHolderviewHolder.singer = ViewUtility.findViewById(view, R.id.navigation_netmusic_singer, this);
		mainFragmentViewHolderviewHolder.hotMusic = ViewUtility.findViewById(view, R.id.navigation_hotMusic, this);
		mainFragmentViewHolderviewHolder.topList = ViewUtility.findViewById(view, R.id.navigation_topList, this);
		mainFragmentViewHolderviewHolder.setting = ViewUtility.findViewById(view, R.id.navigation_setting, this);
		mainFragmentViewHolderviewHolder.timing = ViewUtility.findViewById(view, R.id.navigation_timing, this);
		mainFragmentViewHolderviewHolder.localmusic_count_textView = ViewUtility.findViewById(view, R.id.localmusic_count_textView);
		mainFragmentViewHolderviewHolder.navigation_search_button = ViewUtility.findViewById(view, R.id.navigation_search_button, this);
		mainFragmentViewHolderviewHolder.navigation_search_edit = ViewUtility.findViewById(view, R.id.navigation_search_edit, this);
		mainFragmentViewHolderviewHolder.navigation_search_edit.setClickable(true);

		mainFragmentViewHolderviewHolder.palylocalmusic_imageview = ViewUtility.findViewById(view, R.id.palylocalmusic_imageview, this);
		mainFragmentViewHolderviewHolder.palylocalmusic_imageview.setTag(ViewTag.PLAYALL);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.localmusic_LinearLayout:
			swichFragment(getFragment(getActivity(), LocalFragment.class, null));
			break;
		case R.id.navigation_netmusic_finder:
			swichFragment(new MusicLibraryFragment());
			break;
		case R.id.navigation_localentry_history:
			swichFragment(getFragment(getActivity(), HistoryFragment.class, null));
			break;
		case R.id.navigation_netmusic_singer:
			swichFragment(getFragment(getActivity(), SingerTabFragment.class, null));
			break;
		case R.id.navigation_setting:
			getActivity().startActivity(new Intent(getActivity(), SettingActivityFragment.class));
			break;
		case R.id.navigation_timing: // 定时退出
			getActivity().startActivity(new Intent(getActivity(), AlarmActivity.class));
			break;
		case R.id.navigation_localentry_fav:
			swichFragment(getFragment(getActivity(), FavouriteFragment.class, null));
			break;
		case R.id.navigation_hotMusic:
			swichFragment(BigLabelFragment.newInstance(1));
			break;
		case R.id.navigation_topList:
			swichFragment(BigLabelFragment.newInstance(9));
			break;
		case R.id.navigation_localentry_download:
			swichFragment(getFragment(getActivity(), DownloadParentFragment.class, null));
			break;
		case R.id.navigation_search_button:// 搜索
			Editable editable = mainFragmentViewHolderviewHolder.navigation_search_edit.getText();
			String searchKey = editable.toString();
			if (!TextUtils.isEmpty(searchKey)) {
				swichFragment(SearchFragment.newInstance(searchKey));
			} else {
				// ToastUtil.showToast(getActivity(), getString(R.string.search_edit_toast));
				Toast.makeText(getActivity(), R.string.search_edit_toast, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.navigation_localentry_playlist://
			swichFragment(new MyClassifyList_Fragment());
			break;
		case R.id.palylocalmusic_imageview://
			int[] location = WindowUtil.getViewInTheWindowPosition(v);
			if (!ArrayUtils.isEmpty(musicinfos)) {
				playMusicInfoArray(musicinfos);
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.playAnimotion_playAllMusic(location[0] + (v.getWidth() >> 1), location[1] - v.getHeight(), musicinfos);
			}
			break;

		}
		if (!(mainFragmentViewHolderviewHolder==null|| mainFragmentViewHolderviewHolder.navigation_search_edit==null)) {
			if (v.getId() == R.id.navigation_search_edit) {
				mainFragmentViewHolderviewHolder.navigation_search_edit.findFocus();
			} else {
				mainFragmentViewHolderviewHolder.navigation_search_edit.clearFocus();
			}
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public void swichFragment(Fragment fragment) {
		FragmentManagerUtil.swichFragment(getFragmentManager(), fragment);
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		StringBuffer select = new StringBuffer(" 1=1 ");
		select.append(" and " + MediaColumns.SIZE + " > " + 1024 * 8);
		select.append(" and " + MediaColumns.DATA + " not like " + "'%.amr'");
		MusicRetrieveLoader loader = new MusicRetrieveLoader(getActivity(), select.toString(), null, AudioColumns.TITLE_KEY);
		return loader;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(Assist.MAIN_LOCALMUSIC_LOADER_ID);
		rootView = null;
		mainFragmentViewHolderviewHolder = null;
		musicinfos = null;
	}

	private Fragment getFragment(Context mContext, Class<? extends Fragment> fclass, Bundle bundle) {
		Fragment fragment = FragmentCache.getFragment(fclass.getName());
		if (fragment == null) {
			fragment = Fragment.instantiate(mContext, fclass.getName(), bundle);
			FragmentCache.addFragment(fclass.getName(), fragment);
		}
		return fragment;
	}

	ArrayList<MusicInfo> musicinfos = null;

	@Override
	public void onLoadFinished(Loader<ArrayList<MusicInfo>> arg0, ArrayList<MusicInfo> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1)) {
			musicinfos = arg1;
			mainFragmentViewHolderviewHolder.localmusic_count_textView.setText(getString(R.string.main_localmusic_count, arg1.size()));
		}else{
			mainFragmentViewHolderviewHolder.localmusic_count_textView.setText(getString(R.string.main_localmusic_count, 0));
		}
	}

}
