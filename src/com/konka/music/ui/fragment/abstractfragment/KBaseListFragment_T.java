package com.konka.music.ui.fragment.abstractfragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.activity.MainActivity;
import com.konka.music.ui.activity.PlayerActivity;
import com.konka.music.util.ViewUtility;
import com.konka.music.util.WindowUtil;

public abstract class KBaseListFragment_T<T> extends KBaseFragment<T> implements OnItemClickListener {
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playing_bar_toggle:
		case R.id.playing_bar_next:// 由于是异步加载，点击下一步后mIMusicControl.isPlaying()的值不会马上变化，
		case R.id.playing_bar_albumart:
			break;
		case R.id.playing_bar_clickable_bg_flash:
			Intent intent = new Intent(getActivity(), PlayerActivity.class);
			startActivity(intent);
			break;
		case R.id.audio_item_icon:
			int[] location = WindowUtil.getViewInTheWindowPosition(v);
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.playAnimotion_insert(location[0] + (v.getWidth() >> 1), location[1] - v.getHeight());
			MusicInfo musicInfo = (MusicInfo) v.getTag();
			if (musicInfo == null)
				return;
			MusicInfoManager.addMusic2PlayList(getActivity(), musicInfo, false);

			break;
		case R.id.btn_toggle_menu:
			v.showContextMenu();
			break;
		}
	}
	protected void startAnimationToPlayMusic(View view,MusicInfo musicInfo) {
		View view2 = ViewUtility.findViewById(view, R.id.audio_item_icon);
		int[] location = WindowUtil.getViewInTheWindowPosition(view2);
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.playAnimotion_playThisMusic(location[0] + (view2.getWidth() >> 1), location[1] - view2.getHeight(), musicInfo);

	}
}
