package com.konka.music.receiver;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.util.FileUtils;

public class MusicDownloadComplete extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		switch (intent.getAction()) {
		case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
			// scanDirAsync(arg0);
			// sdScan(arg0);
//			sdScan(arg0);
			sdScan2(arg0.getApplicationContext());
			break;
		case Intent.ACTION_MEDIA_SCANNER_STARTED:
			break;
		case Intent.ACTION_MEDIA_SCANNER_FINISHED:
			break;
		}
	}
//	Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
	public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

	public void scanDirAsync(Context ctx) {
		String s = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		String path = FileUtils.createDirs("/konka/music/");
		scanIntent.setData(Uri.fromFile(new File(path)));
		// scanIntent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		ctx.sendBroadcast(scanIntent);
	}

	public void sdScan(Context ctx) {
		System.out.println("发送广播了");
		ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//		ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
	}
	MediaScannerConnectionClient d;
	public void sdScan2(Context ctx) {
		String path = FileUtils.createDirs("/konka/music/");
		File file=new File(path);
//		MediaScannerService d;
//		MediaScannerService
		MediaScannerConnection.scanFile(ctx, file.list(), null, new MediaScannerConnectionClient() {
			
			@Override
			public void onScanCompleted(String arg0, Uri arg1) {
//				System.out.println("arg1"+arg1);
//				System.out.println("arg0"+arg0);
			}

			@Override
			public void onMediaScannerConnected() {
				System.out.println("连接上了");
			}
		});
	}
}
