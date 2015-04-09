package com.konka.music.pojo;

import android.database.Cursor;
import android.support.v4.util.LongSparseArray;

public class DownLoadFinishResult {
	public DownLoadFinishResult() {
		super();
	}

	private Cursor cursor;
	private LongSparseArray<MusicInfo> musicinfos;

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public LongSparseArray<MusicInfo> getMusicinfos() {
		return musicinfos;
	}

	public DownLoadFinishResult(Cursor cursor, LongSparseArray<MusicInfo> musicinfos) {
		super();
		this.cursor = cursor;
		this.musicinfos = musicinfos;
	}

	public void setMusicinfos(LongSparseArray<MusicInfo> musicinfos) {
		this.musicinfos = musicinfos;
	}

}
