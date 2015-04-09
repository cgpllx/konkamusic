package com.konka.music.listener;

import com.konka.music.pojo.MusicInfo;

/**
 * 播放器响应接口，播放器做一下动作后会调用响应的方法
 * 
 * @author Administrator
 * 
 */
 public interface MusicPlayerAction {
	void onMusicPaly(MusicInfo musicinfo);
	void onMusicPause();

	void onMusicStop();

//	void onMusicPrev();

//	void onMusicNext();

	void onMusicPlaybackProgress(int progress);

	void onMusicBufferingUpdateProgress(int progress);
	void introductionData(MusicInfo musicinfo);
	
	void onServiceBindComplete();//服务绑定成功后调用

}



