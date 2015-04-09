package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.LongSparseArray;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.pojo.DownLoadFinishResult;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class DownLoadFinishLoader extends BaseLoader<DownLoadFinishResult> {
	private DownloadManager mDownloadManager;

	public DownLoadFinishLoader(Context context, DownloadManager downloadManager) {
		super(context);
		this.mDownloadManager = downloadManager;
	}

	private Cursor mCurrentCursor;

	@Override
	public DownLoadFinishResult loadInBackground() {
		String where = "download_id != 0";
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, where);
		int count = musicinfos.size();
		if (count == 0) {
			return null;
		}
		long[] ids = new long[count];
		LongSparseArray<MusicInfo> array = new LongSparseArray<>();
		for (int i = 0; i < musicinfos.size(); i++) {
			MusicInfo musicInfo = musicinfos.get(i);
			long id = musicInfo.getDownload_id();
			ids[i] = id;
			array.put(id, musicInfo);
		}
		// ids=new long[]{1,2,3,4,5,6};
		DownloadManager.Query baseQuery = new DownloadManager.Query().setOnlyIncludeVisibleInDownloadsUi(true);
//		closeCursor(mCurrentCursor);
		Cursor mDownloadingCursor = mDownloadManager.query(baseQuery.setFilterById(ids).setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL).orderBy(DownloadManager.COLUMN_TOTAL_SIZE_BYTES, DownloadManager.Query.ORDER_DESCENDING));
//		mCurrentCursor = mDownloadingCursor;
		DownLoadFinishResult downLoadFinishResult = new DownLoadFinishResult(mDownloadingCursor, array);
		return downLoadFinishResult;
	}

	private void closeCursor(Cursor mCurrentCursor) {
		if (mCurrentCursor != null) {
			if (!mCurrentCursor.isClosed()) {
				mCurrentCursor.close();
			}
			mCurrentCursor = null;
		}
	}
}
