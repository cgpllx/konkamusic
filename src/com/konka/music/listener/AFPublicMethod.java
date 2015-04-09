package com.konka.music.listener;

import java.util.List;

import com.konka.music.pojo.MusicInfo;

public interface AFPublicMethod {
	boolean isPlaying();
	 
	void seekTo(int progress);
	
	int getCurPosition();
	
 	int getDuration();
 	
 	MusicInfo getMusicInfo();
 	
 	void setMusicPalyModel(int musicPalyModel);
 	
 	int getMusicPalyModel();
 	 	
 	List<MusicInfo> getPlaylist();
 	
 	int getPlayListIndex();
 	
 	void clearPlayList();
}
