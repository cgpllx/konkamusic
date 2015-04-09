package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.konka.music.core.providers.DownloadManager;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class DownLoadIngLoader extends BaseLoader<Cursor> {
	private DownloadManager mDownloadManager;

	public DownLoadIngLoader(Context context, DownloadManager downloadManager) {
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
		// contents=new long[]{1,2,3,4,5,6};
		// Cursor mDownloadingCursor = mDownloadManager.query(baseQuery.orderBy(DownloadManager.COLUMN_TOTAL_SIZE_BYTES, DownloadManager.Query.ORDER_DESCENDING));
		// Cursor mDownloadingCursor = mDownloadManager.query(baseQuery.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL).orderBy(DownloadManager.COLUMN_TOTAL_SIZE_BYTES, DownloadManager.Query.ORDER_DESCENDING));
		// closeCursor(mDownloadingCursor);
		mDownloadingCursor = mDownloadManager.query(baseQuery.setFilterById(contents).setFilterByStatus(~DownloadManager.STATUS_SUCCESSFUL).orderBy(DownloadManager.COLUMN_TOTAL_SIZE_BYTES, DownloadManager.Query.ORDER_DESCENDING));

		// String select = ""+Downloads.COLUMN_STATUS + "!= "+DownloadManager.STATUS_SUCCESSFUL;
		// new CursorLoader(getContext(), Downloads.CONTENT_URI, null, select, null, null);
		return mDownloadingCursor;
	}

	private void closeCursor(Cursor mDownloadingCursor) {
		if (mDownloadingCursor != null && !mDownloadingCursor.isClosed()) {
			mDownloadingCursor.close();
			mDownloadingCursor = null;
		}
	}

	@Override
	protected void onReleaseResources(Cursor data) {
		super.onReleaseResources(data);
		if (data != null && !data.isClosed()) {
//			data.close();
//			System.out.println("关闭");
		}
	}

 

}
