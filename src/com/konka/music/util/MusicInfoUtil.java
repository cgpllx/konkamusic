package com.konka.music.util;

import java.util.ArrayList;

import android.media.MediaMetadataRetriever;

import com.konka.music.musicexception.DownLoadException;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.wedget.MusicApplication;

public class MusicInfoUtil {
	public static void setMusicInfoToLike(MusicInfo musicinfo) {
		String strWhere = "displayName = ?";
		String[] selectionArgs = { musicinfo.getDisplayName() };
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, strWhere, selectionArgs);
		if (!ArrayUtils.isEmpty(musicinfos)) {
			MusicInfo dbmusicinfo = musicinfos.get(0);
			dbmusicinfo.setFavourite(musicinfo.getFavourite());
			MusicApplication.mKCommonToolDb.insertOrReplace(dbmusicinfo);
		} else {
			MusicApplication.mKCommonToolDb.insertOrReplace(musicinfo);
		}
	}

	public static void handleMyLikeFromDb(MusicInfo musicinfo) {
		String strWhere = "displayName = ? and favourite != 0";
		String[] selectionArgs = { musicinfo.getDisplayName() };
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, strWhere, selectionArgs);
		if (!ArrayUtils.isEmpty(musicinfos)) {
			MusicInfo dbmusicinfo = musicinfos.get(0);
			musicinfo.setFavourite(dbmusicinfo.getFavourite());
		}
	}

	public static void checkMusicInfoDownload(MusicInfo musicinfo) throws DownLoadException {
		String strWhere = "displayName = ? and download_id != 0";
		String[] selectionArgs = { musicinfo.getDisplayName() };
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, strWhere, selectionArgs);

		if (!ArrayUtils.isEmpty(musicinfos)) {
			for (MusicInfo m : musicinfos) {
				System.out.println(m.getTitle());
				System.out.println(m.getDownload_id());
				System.out.println(m.getData());
			}
			throw new DownLoadException("已经在下载队列中");
		}
	}

	public static void setMusicInfoToHistory(MusicInfo musicinfo) {
		String strWhere = "displayName = ?";
		String[] selectionArgs = { musicinfo.getDisplayName() };
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, strWhere, selectionArgs);
		if (!ArrayUtils.isEmpty(musicinfos)) {
			MusicInfo dbmusicinfo = musicinfos.get(0);
			dbmusicinfo.setHistory(musicinfo.getHistory());
			MusicApplication.mKCommonToolDb.insertOrReplace(dbmusicinfo);
		} else {
			MusicApplication.mKCommonToolDb.insertOrReplace(musicinfo);
		}
	}

	public static void clearAllHistory() {
		String strWhere = "history != 0";
		MusicApplication.mKCommonToolDb.update(MusicInfo.class, strWhere, new String[] { "history" }, new String[] { "0" });
	}

	public static void setMusicInfoToDownload(MusicInfo musicinfo) {
		String strWhere = "displayName = ?";
		String[] selectionArgs = { musicinfo.getDisplayName() };
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, strWhere, selectionArgs);
		if (!ArrayUtils.isEmpty(musicinfos)) {
			MusicInfo dbmusicinfo = musicinfos.get(0);
			dbmusicinfo.setDownload_id(musicinfo.getDownload_id());
			MusicApplication.mKCommonToolDb.insertOrReplace(dbmusicinfo);
		} else {
			MusicApplication.mKCommonToolDb.insertOrReplace(musicinfo);
		}
	}
	public static MusicInfo parseFile2MusicInfo(String path) {
		MusicInfo musicInfo = new MusicInfo();
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		String title = "";
		String album = "";
		String artist = "";
		try {
			title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
			artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		musicInfo.setTitle(title);
		musicInfo.setAlbum(album);
		musicInfo.setMusicname(title);
		musicInfo.setArtist(artist);
		musicInfo.setSinger(artist);
		musicInfo.setData(path);
		musicInfo.setDisplayName(artist + "-" + title);

		return musicInfo;
	}
}
