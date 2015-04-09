package com.konka.music.service;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;

public class MusicInfoManager {

	public static void addMusicInfoArray(Context context, ArrayList<MusicInfo> musicInfos, boolean palythis) {
		if (!ArrayUtils.isEmpty(musicInfos)) {
			Intent intent = new Intent(context, MusicService.class);
			intent.setAction(Assist.SERVICE_ACTION_ADD_MUSICINFO_ARRAY);
			intent.putExtra(Assist.KEY_MUSICINFO_LIST, musicInfos);
			intent.putExtra(Assist.KEY_PLAYTHIS, palythis);
			context.startService(intent);
		}
	}

	public static void addMusic2PlayList(Context context, MusicInfo musicInfo, boolean palythis) {
		if (musicInfo != null) {
			Intent intent = new Intent(context, MusicService.class);
			intent.setAction(Assist.SERVICE_ACTION_Add_MUSIC);
			intent.putExtra(Assist.KEY_MUSICINFO, musicInfo);
			intent.putExtra(Assist.KEY_PLAYTHIS, palythis);
			context.startService(intent);
		}
	}

	public static void playPositionInTheList(Context context, int index) {
		Intent intent = new Intent(context, MusicService.class);
		intent.setAction(Assist.SERVICE_ACTION_PALYMUSIC_OF_POSITION_THE_LIST);
		intent.putExtra(Assist.KEY_PLAYLIST_INDEX, index);
		context.startService(intent);
	}

	public static void clearPalyList(Context context) {
		Intent intent = new Intent(context, MusicService.class);
		intent.setAction(Assist.SERVICE_ACTION_CLEARPALYLIST);
		context.startService(intent);
	}

	public static void startService(Context mContext, String action) {
		Intent intent = new Intent(mContext, MusicService.class);
		intent.setAction(action);
		mContext.startService(intent);
	}

	public static void exitApp(Context mContext) {
		startService(mContext, Assist.SERVICE_ACTION_CLOSE);
	}

	public static void playNext(Context mContext) {
		startService(mContext, Assist.SERVICE_ACTION_NEXT);
	}

	public static void stopMusic(Context mContext) {
		startService(mContext, Assist.SERVICE_ACTION_STOP);
	}

	public static void playOrPauseMusic(Context mContext) {
		startService(mContext, Assist.SERVICE_ACTION_PLAY_OR_PAUSE);
	}
}
