package com.konka.music.lrc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.konka.music.lrc.LrcDownLoadHelper.ILRCDownLoadCallback;
import com.konka.music.pojo.MusicInfo;

public class LrcLoader {
	public static final String TAG = LrcLoader.class.getSimpleName();
	static LrcDownLoadHelper mLrcDownLoadHelper;

	public static void init() {
		mLrcDownLoadHelper = new LrcDownLoadHelper();
		mLrcDownLoadHelper.init();
	}

	public static void displayLrc(MusicInfo musicInfo, LyricView mLyricView) {
		if (musicInfo == null) {
			throw new IllegalArgumentException("MusicInfo cannot be empty");
		}
		displayLrc(musicInfo.getTitle(), musicInfo.getArtist(), mLyricView);
	}

	public static void displayLrc(String title, String artist, LyricView mLyricView) {
		String lrcPath = MusicUtils.getLyricFile(title, artist);
		if (lrcPath != null) {// 肯定可以进去
			File f = new File(lrcPath);
			if (f.exists()) {
				displayLrcFromSD(title, artist, mLyricView);
			} else {
				displayLrcFromNet(title, artist, mLyricView);
			}
		}
	}

	public static void displayLrcFromSD(String title, String artist, LyricView mLyricView) {
		String lrcPath = MusicUtils.getLyricFile(title, artist);
		List<LyricObject> lyricObjects = null;
		try {
			lyricObjects = ParserLrc.parserFile(lrcPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lyricObjects != null) {
			mLyricView.setLyricObjects(lyricObjects);
			mLyricView.postInvalidate();
		}
	}

	public static void displayLrcFromNet(String title, String artist, final LyricView mLyricView) {
		mLrcDownLoadHelper.syncDownLoadLRC(title, artist, new ILRCDownLoadCallback() {
			@Override
			public void lrcDownLoadComplete(boolean isSuccess, String song, String artist) {
				if (isSuccess) {
					displayLrcFromSD(song, artist, mLyricView);
				}
			}
		});
	}
}
