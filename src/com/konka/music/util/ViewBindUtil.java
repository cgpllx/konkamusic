package com.konka.music.util;

import android.view.View;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.PlayListItemViewHolder;

public class ViewBindUtil {
	public static void bindView(View convertView, PlayListItemViewHolder holder, View.OnClickListener l) {

		holder.indicator = ViewUtility.findViewById(convertView, R.id.indicator);
		holder.title = ViewUtility.findViewById(convertView, R.id.title);
		holder.btn_toggle_menu = ViewUtility.findViewById(convertView, R.id.btn_toggle_menu);

		holder.audio_item_icon = ViewUtility.findViewById(convertView, R.id.audio_item_icon);
		if (l != null) {
			holder.btn_toggle_menu.setOnClickListener(l);
			holder.audio_item_icon.setOnClickListener(l);
		}
	}

	public static void assignToView(MusicInfo musicInfo, PlayListItemViewHolder holder) {
		holder.title.setText(musicInfo.getDisplayName());
		holder.btn_toggle_menu.setTag(musicInfo);
		holder.audio_item_icon.setTag(musicInfo);
	}
}
