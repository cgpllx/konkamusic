package com.konka.music.adapter;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.PlayListItemViewHolder;
import com.konka.music.util.ViewBindUtil;

public class TrackAdapter extends ArrayAdapter<MusicInfo> {
	private Context mContext = null;
	/** 播放时为相应播放条目显示一个播放标记 */
	private int mActivateItemPos = -1;

	public TrackAdapter(Context context) {
		mContext = context;
	}

	public TrackAdapter(Context context, OnClickListener l) {
		mContext = context;
		this.l = l;
	}

	@Override
	public void addAll(Collection<? extends MusicInfo> collection) {
		super.addAll(collection);
		mActivateItemPos = -1;
		notifyDataSetChanged();

	}

	/** 让指定位置的条目显示一个正在播放标记（活动状态标记） */
	public void setSpecifiedIndicator(int position) {
		mActivateItemPos = position;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PlayListItemViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.audio_list_item, parent, false);
			holder = new PlayListItemViewHolder();
			ViewBindUtil.bindView(convertView, holder, l);
			convertView.setTag(holder);
		} else {
			holder = (PlayListItemViewHolder) convertView.getTag();
		}
		ViewBindUtil.assignToView(getItem(position), holder);
		if (mActivateItemPos == position) {
			holder.indicator.setVisibility(View.VISIBLE);
		} else {
			holder.indicator.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private OnClickListener l = null;

	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

}
