package com.konka.music.pojo;

import java.util.ArrayList;

public class MyClassifyList {
	private ArrayList<MusicInfo> mMusicInfo;
	private MyClassifyName mySongListName;

	public MyClassifyList(ArrayList<MusicInfo> mMusicInfo, MyClassifyName mySongListName) {
		super();
		this.mMusicInfo = mMusicInfo;
		this.mySongListName = mySongListName;
	}

	public ArrayList<MusicInfo> getmMusicInfo() {
		return mMusicInfo;
	}

	public void setmMusicInfo(ArrayList<MusicInfo> mMusicInfo) {
		this.mMusicInfo = mMusicInfo;
	}

	public MyClassifyName getMySongListName() {
		return mySongListName;
	}

	public void setMySongListName(MyClassifyName mySongListName) {
		this.mySongListName = mySongListName;
	}

}
