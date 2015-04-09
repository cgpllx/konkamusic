package com.konka.music.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.konka.music.pojo.MusicInfo;

public class MusicRetrieveLoader extends AsyncTaskLoader<ArrayList<MusicInfo>> {
	private final String TAG = MusicRetrieveLoader.class.getSimpleName();

	/** 要从MediaStore检索的列 */
	private final String[] mProjection = new String[] { Media._ID, Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DATA, Media.SIZE, Media.DURATION, Media.DISPLAY_NAME };

	// 数据库查询相关参数
	private String mSelection = null;
	private String[] mSelectionArgs = null;
	private String mSortOrder = null;

	private ContentResolver mContentResolver = null;

	private ArrayList<MusicInfo> mMusicItemList = null;

	public MusicRetrieveLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
		super(context);
		this.mSelection = selection;
		this.mSelectionArgs = selectionArgs;
		this.mSortOrder = sortOrder;
		mContentResolver = context.getContentResolver();
	}

	@Override
	public ArrayList<MusicInfo> loadInBackground() {
		Log.i(TAG, "loadInBackground");
		ArrayList<MusicInfo> itemsList = new ArrayList<MusicInfo>();
		MusicInfo item = null;

		Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, mProjection, mSelection, mSelectionArgs, mSortOrder);

		// 将数据库查询结果保存到一个List集合中(存放在RAM)
		if (cursor != null) {
			while (cursor.moveToNext()) {
				item = createNewItem(cursor);
				itemsList.add(item);
			}
			cursor.close();
		}
		return itemsList;
	}

	@Override
	public void deliverResult(ArrayList<MusicInfo> data) {
		Log.i(TAG, "deliverResult");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		List<MusicInfo> oldList = data;
		mMusicItemList = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldList != null) {
			onReleaseResources(oldList);
		}
	}

	protected void onReleaseResources(List<MusicInfo> data) {
		Log.i(TAG, "onReleaseResources");
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	@Override
	protected void onStartLoading() {
		Log.i(TAG, "onStartLoading");
		if (mMusicItemList != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mMusicItemList);
		}
		// If the data has changed since the last time it was loaded
		// or is not currently available, start a load.
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		Log.i(TAG, "onStartLoading");
		super.onStopLoading();
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(ArrayList<MusicInfo> data) {
		super.onCanceled(data);
		Log.i(TAG, "onCanceled");
		// At this point we can release the resources associated with 'data'
		// if needed.
		onReleaseResources(data);
	}

	@Override
	protected void onReset() {
		super.onReset();
		Log.i(TAG, "onReset");
		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'data'
		// if needed.
		if (mMusicItemList != null) {
			onReleaseResources(mMusicItemList);
			mMusicItemList = null;
		}
	}

	@Override
	protected void onForceLoad() {
		super.onForceLoad();
	}

	private MusicInfo createNewItem(Cursor cursor) {
		MusicInfo item = new MusicInfo();
		String title = cursor.getString(cursor.getColumnIndex(MediaColumns.TITLE));
		item.setTitle(title);
		item.setArtist(cursor.getString(cursor.getColumnIndex(AudioColumns.ARTIST)));
		
//		item.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaColumns.DISPLAY_NAME)));
		
		
		item.setId(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
		item.setAlbum(cursor.getString(cursor.getColumnIndex(AudioColumns.ALBUM)));
		item.setDuration(cursor.getLong(cursor.getColumnIndex(AudioColumns.DURATION)));
		item.setSize(cursor.getLong(cursor.getColumnIndex(MediaColumns.SIZE)));
		item.setData(cursor.getString(cursor.getColumnIndex(MediaColumns.DATA)));

		String[] singername_musicname = title.split("-");
		if (singername_musicname.length == 2) {
			item.setSinger(singername_musicname[0].trim());
			item.setMusicname(singername_musicname[1].trim());
		}else{
			item.setSinger(item.getArtist());
			item.setMusicname(item.getTitle());
		}
		item.setDisplayName(item.getSinger()+" - "+item.getMusicname());
		return item;
	}
}
