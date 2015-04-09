package com.konka.music.ui.fragment.abstractfragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.konka.music.adapter.ArrayAdapter;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.ObjectUtil;
import com.konka.music.util.Util;
import com.konka.music.util.ViewTag;

public abstract class KBaseListFragment_MusicInfoArray extends KBaseListFragment_T<ArrayList<MusicInfo>> {
	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public static class KBaseHolder {
		public ArrayAdapter<MusicInfo> listViewAdapter = null;
		public ListView kBaseListView;
	}

	protected abstract KBaseHolder getKBaseHolder();

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Object tagobj = v.getTag();
		if (tagobj != null && tagobj instanceof String) {
			switch ((String) tagobj) {
			case ViewTag.PLAYALL:
				ArrayList<MusicInfo> lists = getMusicInfoArray();
				if (!ArrayUtils.isEmpty(lists)) {
					MusicInfoManager.addMusicInfoArray(getActivity(), lists, true);
				}
				break;
			default:
				super.onClick(v);
				break;
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			ArrayList<MusicInfo> musicInfos = getMusicInfoArray();
			if (!ArrayUtils.isEmpty(musicInfos)) {
				MusicInfo musicInfo=musicInfos.get(position - getHeardercCount());
				startAnimationToPlayMusic(view, musicInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected int getHeardercCount() {
		KBaseHolder baseHolder = getKBaseHolder();
		if (!ObjectUtil.isEmpty(baseHolder, baseHolder.kBaseListView)) {
			return baseHolder.kBaseListView.getHeaderViewsCount();
		}
		return 0;
	}

	protected ArrayList<MusicInfo> getMusicInfoArray() {
		KBaseHolder baseHolder = getKBaseHolder();
		if (!ObjectUtil.isEmpty(baseHolder, baseHolder.listViewAdapter)) {
			ArrayList<MusicInfo> lists = baseHolder.listViewAdapter.getAll();
			return lists;
		}
		return null;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		final AdapterContextMenuInfo mInfo = (AdapterContextMenuInfo) menuInfo;
		int index = mInfo.position - getHeardercCount();
		KBaseHolder baseHolder = getKBaseHolder();
		if (index >= 0 && index < baseHolder.listViewAdapter.getCount()) {
			menu.setHeaderTitle(baseHolder.listViewAdapter.getItem(index).getTitle());
			menu.add(0, CONTEXT_MENU_ADD_TO_PLAYLIST, Menu.NONE, "添加到播放列表");
			menu.add(0, CONTEXT_MENU_PLAT, Menu.NONE, "播放");
		}
	}

	protected final int CONTEXT_MENU_ADD_TO_PLAYLIST = 1;// 添加到播放列表
	protected final int CONTEXT_MENU_CHECK_DETAIL = 2;// 查看详情
	protected final int CONTEXT_MENU_PLAT = 3;// 播放当前music
	protected final int CONTEXT_MENU_DELETE = 4;// 删除当前
	protected final int CONTEXT_MENU_DOWNLOAD = 5;// 下载当前

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		KBaseHolder baseHolder = getKBaseHolder();
		if (baseHolder.listViewAdapter == null) {
			return false;
		}
		int index = menuInfo.position - getHeardercCount();
		if (!(index >= 0 && index < baseHolder.listViewAdapter.getCount())) {
			return false;
		}
		MusicInfo musicInfo = baseHolder.listViewAdapter.getItem(index);
		if (musicInfo == null) {
			return false;
		}
		switch (item.getItemId()) {
		case CONTEXT_MENU_ADD_TO_PLAYLIST:
			// 添加到列表
			MusicInfoManager.addMusic2PlayList(getActivity(), musicInfo, false);
			break;

		case CONTEXT_MENU_CHECK_DETAIL:// 查看详情

			break;
		case CONTEXT_MENU_PLAT:// 播放
			MusicInfoManager.addMusic2PlayList(getActivity(), musicInfo, true);
			break;
		case CONTEXT_MENU_DELETE:// 删除

			break;
		case CONTEXT_MENU_DOWNLOAD:// 下载
			Util.downLoadMusic(getActivity(), musicInfo);
			break;

		default:
			return false;
		}
		return true;
	}
}
