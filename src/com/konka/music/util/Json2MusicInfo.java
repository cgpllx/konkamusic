package com.konka.music.util;

import org.json.JSONObject;

import com.konka.music.pojo.MusicInfo;
import com.kubeiwu.baseclass.util.JsonUtil;

public class Json2MusicInfo {
	public static MusicInfo parse(JSONObject jsonObject){
		MusicInfo musicInfo = new MusicInfo();
		String singer=JsonUtil.getString(jsonObject, "singer");
		
		musicInfo.setArtist(singer);// 艺术家，歌手
		musicInfo.setSinger(singer);
		String title=JsonUtil.getString(jsonObject, "name");
		musicInfo.setMusicname(title);
		musicInfo.setTitle(title);
		musicInfo.setAlbum(JsonUtil.getString(jsonObject, "album_name"));// 专辑名
		musicInfo.setId(JsonUtil.getInt(jsonObject, "song_id"));
		musicInfo.setData(JsonUtil.getString(jsonObject, "play_url"));
		
		musicInfo.setDisplayName(musicInfo.getSinger()+" - "+musicInfo.getTitle());
		musicInfo.setLrc_url(JsonUtil.getString(jsonObject, "lrc_url"));
		return musicInfo;
	}
}
