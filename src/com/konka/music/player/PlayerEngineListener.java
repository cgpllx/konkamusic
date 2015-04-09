package com.konka.music.player;

import com.konka.music.pojo.MusicInfo;


public interface PlayerEngineListener {
	
	public void onTrackPlay(MusicInfo itemInfo); 
	public void onTrackStop(MusicInfo itemInfo);
	public void onTrackPause(MusicInfo itemInfo);
	public void onTrackPrepareSync(MusicInfo itemInfo);
	public void onTrackPrepareComplete(MusicInfo itemInfo);
	public void onTrackStreamError(MusicInfo itemInfo);
	public void onTrackPlayComplete(MusicInfo itemInfo);
	public void onTrackIntroductionData(MusicInfo itemInfo);//传人数据
}
