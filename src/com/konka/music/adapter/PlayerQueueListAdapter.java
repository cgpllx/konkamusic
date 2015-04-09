package com.konka.music.adapter;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.konka.music.pojo.MusicInfo;

public class PlayerQueueListAdapter extends BaseAdapter {

	private List<MusicInfo> mMusicInfos;
	private SetIcon mSetIcon;

	public PlayerQueueListAdapter() {
		mMusicInfos = new LinkedList<MusicInfo>();
	}

	// public PlayerQueueListAdapter(List<MusicInfo> infos) {
	// this.mMusicInfos = infos;
	// }

	@Override
	public int getCount() {
		return mMusicInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mMusicInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mSetIcon.getView(position, convertView);
	}

	public void setIconInterface(SetIcon icon) {
		this.mSetIcon = icon;
	}

	public void setMusicInfos(List<MusicInfo> infos) {
		this.mMusicInfos = infos;
		notifyDataSetChanged();
	}

	public void setMusicInfo(MusicInfo info) {
		this.mMusicInfos.add(info);
	}

	public interface SetIcon {
		public View getView(int position, View convertView);
	}
}
