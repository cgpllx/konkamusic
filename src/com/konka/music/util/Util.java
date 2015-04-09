package com.konka.music.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.musicexception.DownLoadException;
import com.konka.music.pojo.MusicInfo;

public class Util {
	public static void exit() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static long downLoadMusic(Context mContext, MusicInfo musicInfo) {
		try {
			MusicInfoUtil.checkMusicInfoDownload(musicInfo);
			DownloadManager manager = new DownloadManager(mContext.getContentResolver(), mContext.getPackageName());
			String url = musicInfo.getData();
			Uri srcUri = Uri.parse(url);
			DownloadManager.Request request = new DownloadManager.Request(srcUri);
//			File file = new File(Environment.getExternalStorageDirectory(), "konka/music");
			File file = new File(FileUtils.createDirs("/konka/music/"));
			if (!file.exists()) {
				file.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
			sdf.applyPattern("-MM_dd_HH_mm_ss");
			String downloadtime = sdf.format(System.currentTimeMillis());
			request.setDestinationInExternalFilesDir(mContext, "konka/music", musicInfo.getTitle() + downloadtime + ".mp3");
			request.setDescription("下载");// allowednetworktypes
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
			request.setShowRunningNotification(false);
			request.setTitle(musicInfo.getDisplayName());
			request.setDescription(musicInfo.getDisplayName());
			long download_id = manager.enqueue(request);
			musicInfo.setDownload_id(download_id);
			MusicInfoUtil.setMusicInfoToDownload(musicInfo);
			ToastUtil.showToast(mContext, "已添加到下载队列");
			return download_id;
		}catch (DownLoadException e) {
			ToastUtil.showToast(mContext, "下载队列中已经存在,不用重复下载");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

 

	public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

	public void scanDirAsync(Context ctx, String dir) {

		Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);

		scanIntent.setData(Uri.fromFile(new File(dir)));

		ctx.sendBroadcast(scanIntent);

	}

	public static void onShareTextClick(Activity activity, String text) {
		ShareCompat.IntentBuilder.from(activity).setType("text/plain").setText(text).startChooser();
	}

	public static boolean checkFileExists(String filepath) {
		if (filepath != null) {
			if (filepath.startsWith("file://")) {
				filepath = filepath.replace("file://", "");
			}
			File file = new File(filepath);
			return file.exists();
		}
		return false;
	}

	public static boolean checkIntent(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			ToastUtil.showToast(context, "请先连接网络！");
			return false;
		}
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if (!wifi) {
			ToastUtil.showToast(context, "建议您使用WIFI以减少流量！");
		}
		return true;
	}
}
