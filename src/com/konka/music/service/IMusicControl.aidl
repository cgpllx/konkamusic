package com.konka.music.service;

import java.util.List;

import com.konka.music.pojo.MusicInfo;
 

interface IMusicControl {

	boolean isPlaying();
	 
	void seekTo(int progress);
	
	int getCurPosition();
	
 	int getDuration();
 	
 	MusicInfo getMusicInfo();
 	
 	void setMusicPalyModel(in int musicPalyModel);
 	
 	int getMusicPalyModel();
 	 	
 	List<MusicInfo> getPlaylist();
 	
 	int getPlayListIndex();
 	
 	void clearPlayList();
 	
}
 
