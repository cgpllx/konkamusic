package com.konka.music.ui.fragment.abstractfragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.konka.music.listener.AFPublicMethod;
import com.konka.music.listener.FACallBack;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.ObjectUtil;
import com.konka.music.util.ViewTag;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

public abstract class AbstractKBaseFragment<T> extends BaseLoaderCallbacksFragment<T> implements OnClickListener, AFPublicMethod, OnSeekBarChangeListener {

	protected FACallBack fACallBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// protected abstract KBaseHolder getKBaseHolder();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fACallBack = (FACallBack) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ===-----------------用户调用start
	@Override
	public List<MusicInfo> getPlaylist() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getPlaylist();
		return null;
	}

	@Override
	public MusicInfo getMusicInfo() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getMusicInfo();
		return null;
	}

	@Override
	public int getDuration() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getDuration();
		return 0;
	}

	@Override
	public int getCurPosition() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getCurPosition();
		return 0;
	}

	@Override
	public int getPlayListIndex() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getPlayListIndex();
		return 0;

	}

	@Override
	public void setMusicPalyModel(int musicPalyModel) {
		if (!ObjectUtil.isEmpty(fACallBack))
			fACallBack.setMusicPalyModel(musicPalyModel);
	}

	@Override
	public int getMusicPalyModel() {
		if (!ObjectUtil.isEmpty(fACallBack))
			return fACallBack.getMusicPalyModel();
		return 0;

	}

	@Override
	public void clearPlayList() {
		if (!ObjectUtil.isEmpty(fACallBack)) {
			fACallBack.clearPlayList();
		}

	}

	// ===-----------------用户调用end

	// -SeekBar--------------------------start
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int time = getDuration() * seekBar.getProgress() / 1000;
		seekTo(time);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	// -SeekBar--------------------------end

	@Override
	public void seekTo(int progress) {// 毫秒
		if (!ObjectUtil.isEmpty(fACallBack)) {
			fACallBack.seekTo(progress);
		}

	}

	@Override
	public boolean isPlaying() {
		if (!ObjectUtil.isEmpty(fACallBack)) {
			return fACallBack.isPlaying();
		}
		return false;

	}

	@Override
	public void onClick(View v) {
		Object tagobj = v.getTag();
		if (tagobj != null && tagobj instanceof String) {
			if (!ObjectUtil.isEmpty(fACallBack)) {
				fACallBack.onViewTagClick((String) tagobj, null);
			}
		}
	}

	protected void playMusicInfoArray(ArrayList<MusicInfo> musicinfos) {
		if (!ObjectUtil.isEmpty(fACallBack) && !ArrayUtils.isEmpty(musicinfos)) {
			fACallBack.onViewTagClick(ViewTag.PLAYALL, musicinfos);
		}
	}

}
