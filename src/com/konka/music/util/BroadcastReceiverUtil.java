package com.konka.music.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.konka.music.pojo.MusicInfo;

public class BroadcastReceiverUtil {
	public static void registerReceiver(Context context, BroadcastReceiver receiver, String... actions) {
		IntentFilter intentFilter = new IntentFilter();
		for (String action : actions) {
			intentFilter.addAction(action);
		}
		context.registerReceiver(receiver, intentFilter);
	}

	public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
		context.unregisterReceiver(receiver);
	}

	public static void sendBroadcast(Context context, String action) {
		Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}
	public static void sendBroadcastAttExtra(Context context, String action,MusicInfo musicinfo) {
		Intent intent = new Intent(action);
		intent.putExtra(Assist.KEY_MUSICINFO, musicinfo);
		context.sendBroadcast(intent);
		
	}

	public static void sendBroadcast(Context context, String action, int progress) {
		Intent intent = new Intent(action);
		intent.putExtra(Assist.KEY_PLAYPROGRESS, progress);
		context.sendBroadcast(intent);
	}

	public static void sendBroadcast(Context context, String action, String key, int progress) {
		Intent intent = new Intent(action);
		intent.putExtra(key, progress);
		context.sendBroadcast(intent);
	}
}
