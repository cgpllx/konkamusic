package com.konka.music.util;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.pojo.MusicInfo;

public class MyMusicInfoUtil {
	private static int mIdColumnId = -1;
	private static int mStatusColumnId;
	private static int mTotalBytesColumnId;
	private static int mCurrentBytesColumnId;
	private static int mLocalUriId;
	private static int mTitleColumnId;
	private static int mURIColumnId;

	public static void init(Cursor cursor) {
		mIdColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);//id
		mStatusColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS); //status 
		mTotalBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);//总大小
		mCurrentBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);//下载大小
		mLocalUriId = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);//本地路径  apkpath
		mTitleColumnId = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE); //title
		mURIColumnId = cursor.getColumnIndex(DownloadManager.COLUMN_URI);//下载diz
	}

	public static MusicInfo getMyAppDownLoad(Cursor cursor) {
		if (mIdColumnId == -1) {
			init(cursor);
		}
		long id = cursor.getLong(mIdColumnId);
		int status = cursor.getInt(mStatusColumnId);
		long mTotal = cursor.getLong(mTotalBytesColumnId);
		long mCurrent = cursor.getLong(mCurrentBytesColumnId);
		//------------
		String apkUripath = cursor.getString(mLocalUriId);
		String apkpath = "";
		if (!TextUtils.isEmpty(apkUripath)) {
			Uri uri = Uri.parse(apkUripath);
			apkpath = uri.getPath();
		}
		//------------

		String title = cursor.getString(mTitleColumnId);
		String apkurl = cursor.getString(mURIColumnId);

//		int progress = Util.getProgressValue(mTotal, mCurrent);

//		MyAppDownLoad myAppDownLoad = new MyAppDownLoad(id, apkpath, title, iconurl, mTotal, versionname, versioncode, apkurl, progress, status, packagename);
		return null;
	}

}
