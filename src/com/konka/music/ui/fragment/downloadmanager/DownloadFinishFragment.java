package com.konka.music.ui.fragment.downloadmanager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.LongSparseArray;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.konka.music.R;
import com.konka.music.core.providers.DownloadManager;
import com.konka.music.core.providers.downloads.Downloads;
import com.konka.music.loader.DownLoadFinishLoader;
import com.konka.music.pojo.DownLoadFinishResult;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.PlayListItemViewHolder;
import com.konka.music.ui.fragment.abstractfragment.KBaseListFragment_T;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.ViewBindUtil;
import com.konka.music.util.ViewUtility;

public class DownloadFinishFragment extends KBaseListFragment_T<DownLoadFinishResult> implements OnClickListener, OnItemClickListener {
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.downloadingfragment_layout);
		return rootView;
	}

	private ListView listView;
	private DownloadFinishBaseAdapter downloadBaseAdapter;
	private DownloadManager mDownloadManager;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listView = ViewUtility.findViewById(view, R.id.downloadingfragment_listview);
		mDownloadManager = new DownloadManager(getActivity().getApplicationContext().getContentResolver(), getActivity().getApplicationContext().getPackageName());
		mDownloadManager.setAccessAllDownloads(true);

		listView.setOnItemClickListener(this);
		getLoaderManager().initLoader(Assist.DOWNLOADFINISH_LOADER_ID, null, this);
	}

	@Override
	public Loader<DownLoadFinishResult> onCreateLoader(int arg0, Bundle arg1) {

		return new DownLoadFinishLoader(getActivity(), mDownloadManager);
	}

	private DownLoadFinishResult downLoadFinishResult;

	@Override
	public void onLoadFinished(Loader<DownLoadFinishResult> arg0, DownLoadFinishResult arg1) {
		super.onLoadFinished(arg0, arg1);
		if (arg1 != null && arg1.getMusicinfos() != null) {
			downLoadFinishResult = arg1;
			downloadBaseAdapter = new DownloadFinishBaseAdapter(getActivity(), downLoadFinishResult.getCursor(), true);
			arg1.getCursor().setNotificationUri(getActivity().getContentResolver(), Downloads.CONTENT_URI);
			listView.setAdapter(downloadBaseAdapter);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view != null && view.getTag() != null) {
			Object object = view.getTag();
			if (object instanceof MusicInfo) {
				MusicInfo musicInfo = (MusicInfo) object;
				startAnimationToPlayMusic(view, musicInfo);
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(Assist.DOWNLOADFINISH_LOADER_ID);
		if (downloadBaseAdapter != null) {
			Cursor c = downloadBaseAdapter.getCursor();
			if (c != null) {
				if (!c.isClosed()) {
					c.close();
				}
				c = null;
			}
		}
	}

	class DownloadFinishBaseAdapter extends CursorAdapter {
		private Cursor cursor;

		final private int mTitleColumnId;
		final private int mStatusColumnId;
		final private int mReasonColumnId;
		final private int mTotalBytesColumnId;
		final private int mCurrentBytesColumnId;
		final private int mMediaTypeColumnId;
		final private int mDateColumnId;
		final private int mIdColumnId;
		final private int mColumnLocalUriId;

		public DownloadFinishBaseAdapter(Context context, Cursor cursor, boolean autoRequery) {
			super(context, cursor, autoRequery);
			this.cursor = cursor;
			mIdColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
			mTitleColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
			mStatusColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
			mReasonColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);
			mTotalBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
			mCurrentBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
			mMediaTypeColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIA_TYPE);
			mDateColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP);
			mColumnLocalUriId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			bindViewData(view, context, cursor);
		}

		@Override
		public View newView(Context context, Cursor arg1, ViewGroup parent) {
			View convertView = LayoutInflater.from(context).inflate(R.layout.audio_list_item, parent, false);
			return convertView;
		}

		private void bindViewData(View view, Context context, Cursor mCursor) {
			PlayListItemViewHolder holder = new PlayListItemViewHolder();
			holder.title = ViewUtility.findViewById(view, R.id.title);
			holder.indicator = ViewUtility.findViewById(view, R.id.indicator);
			holder.btn_toggle_menu = ViewUtility.findViewById(view, R.id.btn_toggle_menu);
			holder.audio_item_icon = ViewUtility.findViewById(view, R.id.audio_item_icon);

			long downlaod_id = mCursor.getLong(mIdColumnId);
			String localuri = mCursor.getString(mColumnLocalUriId);
			Uri uri = Uri.parse(localuri);

			LongSparseArray<MusicInfo> lists = downLoadFinishResult.getMusicinfos();
			if (lists != null && lists.size() != 0) {
				MusicInfo musicInfo = lists.get(downlaod_id);
				if (uri != null) {
					String localpath = uri.getPath();
					if (!TextUtils.isEmpty(localpath)) {
						musicInfo.setData(localpath);
					}
				}
				ViewBindUtil.bindView(view, holder, DownloadFinishFragment.this);
				holder.indicator.setVisibility(View.GONE);
				holder.btn_toggle_menu.setVisibility(View.GONE);
				ViewBindUtil.assignToView(musicInfo, holder);
				view.setTag(musicInfo);
			}

			// }else{
			// // mDownloadManager.remove(downlaod_id);
			// }
			// FragmentPagerAdapter d;

		}

	}

	public static final int CLEARALL = 1;
}
