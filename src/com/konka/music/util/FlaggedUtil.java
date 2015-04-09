package com.konka.music.util;

import java.util.List;

import com.konka.music.pojo.MusicInfo;
import com.kubeiwu.commontool.db.KCommonToolDb;

public class FlaggedUtil {
	/**
	 * 标记为历史
	 * 
	 * @param commonToolDb
	 * @param musicInfo
	 */
	public static void flaggedMusicInfoAsHistory(KCommonToolDb commonToolDb, MusicInfo musicInfo) {
		// 标记为历史记录
		List<MusicInfo> musicInfos = commonToolDb.findAllByWhere(MusicInfo.class, "data = '" + musicInfo.getData()+"'");
		if (!ArrayUtils.isEmpty(musicInfos)) {
			musicInfo = musicInfos.get(0);
			if (musicInfo != null) {
				if (musicInfo.getHistory() == 0) {
					musicInfo.setHistory(1);
					commonToolDb.insert(musicInfo);
				}
				return;
			}
		}
		commonToolDb.insert(musicInfo);
	}

	/**
	 * 标记为我喜欢
	 * 
	 * @param commonToolDb
	 * @param musicInfo
	 */
	public static void flaggedMusicInfoAsDownload(KCommonToolDb commonToolDb, MusicInfo musicInfo) {
		// 标记为历史记录
		List<MusicInfo> musicInfos = commonToolDb.findAllByWhere(MusicInfo.class, "data = " + musicInfo.getData());
		if (!ArrayUtils.isEmpty(musicInfos)) {
			musicInfo = musicInfos.get(0);
			if (musicInfo != null) {
				if (musicInfo.getHistory() == 0) {
					musicInfo.setHistory(1);
					commonToolDb.insert(musicInfo);
				}
				return;
			}
		}
		commonToolDb.insert(musicInfo);
	}

	/**
	 * 标记为下载
	 * 
	 * @param commonToolDb
	 * @param musicInfo
	 */
	public static void flaggedMusicInfoAsFavourite(KCommonToolDb commonToolDb, MusicInfo musicInfo) {
		// 标记为历史记录
		List<MusicInfo> musicInfos = commonToolDb.findAllByWhere(MusicInfo.class, "data = " + musicInfo.getData());
		if (!ArrayUtils.isEmpty(musicInfos)) {
			musicInfo = musicInfos.get(0);
			if (musicInfo != null) {
				if (musicInfo.getHistory() == 0) {
					musicInfo.setHistory(1);
					commonToolDb.insert(musicInfo);
				}
				return;
			}
		}
		commonToolDb.insert(musicInfo);
	}
}
