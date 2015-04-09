package com.konka.music.service;

import java.util.List;

import com.konka.music.pojo.MusicInfo;

public interface IServiceHandle {
	void prev();

	void next();

	void paly();

	void pause();

	void addMusicInfoToPlayList(List<MusicInfo> musicLists,boolean playthis);

	void setMusicPalyModel(int PalyModel);

	List<MusicInfo> getPlaylist();

	void continuePlay();

	void addMusicInfo(MusicInfo musicList, boolean flag);
}
