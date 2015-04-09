package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.wedget.MusicApplication;

public class DownLoadIngCursorLoader extends CursorLoader {
	private DownloadManager mDownloadManager;

	public DownLoadIngCursorLoader(Context context, DownloadManager downloadManager) {
		super(context);
		this.mDownloadManager = downloadManager;
	}

	Cursor mDownloadingCursor;

	@Override
	public Cursor loadInBackground() {
		String where = "download_id != 0";
		ArrayList<MusicInfo> musicinfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, where);
		int count = musicinfos.size();
		if (count == 0) {
			return null;
		}
		long[] contents = new long[count];
		for (int i = 0; i < count; i++) {
			contents[i] = musicinfos.get(i).getDownload_id();
		}
		DownloadManager.Query baseQuery = new DownloadManager.Query().setOnlyIncludeVisibleInDownloadsUi(true);
		mDownloadingCursor = mDownloadManager.query(baseQuery.setFilterById(contents).setFilterByStatus(~DownloadManager.STATUS_SUCCESSFUL).orderBy(DownloadManager.COLUMN_TOTAL_SIZE_BYTES, DownloadManager.Query.ORDER_DESCENDING));

		return mDownloadingCursor;
	}
 

 

}
