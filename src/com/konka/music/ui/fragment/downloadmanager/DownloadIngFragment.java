package com.konka.music.ui.fragment.downloadmanager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.core.providers.DownloadManager;
import com.konka.music.core.providers.downloads.Downloads;
import com.konka.music.loader.DownLoadIngCursorLoader;
import com.konka.music.ui.fragment.downloadmanager.DownloadIngFragment.DownloadIngBaseAdapter.Holder;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

public class DownloadIngFragment extends BaseLoaderCallbacksFragment<Cursor> implements OnClickListener, OnItemClickListener {
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.downloadingfragment_layout);
		return rootView;
	}

	public static final int SUSPENDEDALL = 0;
	public static final int STARTALL = 1;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(Menu.NONE, SUSPENDEDALL, 0, "全部暂停").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(Menu.NONE, STARTALL, 0, "全部开始").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}

	private ListView listView;
	private DownloadIngBaseAdapter downloadBaseAdapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = ViewUtility.findViewById(view, R.id.downloadingfragment_listview);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void lazyLoad() {
		mDownloadManager = new DownloadManager(getActivity().getContentResolver(), getActivity().getPackageName());
		mDownloadManager.setAccessAllDownloads(true);
		getLoaderManager().initLoader(Assist.DOWNLOADING_LOADER_ID, null, this);
	};

	DownloadManager mDownloadManager;
	Cursor mDownloadingCursor;

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new DownLoadIngCursorLoader(getActivity(), mDownloadManager);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		super.onLoadFinished(arg0, arg1);
		if (arg1 != null) {
			mDownloadingCursor = arg1;
			downloadBaseAdapter = new DownloadIngBaseAdapter(getActivity(), mDownloadingCursor, true);
			mDownloadingCursor.setNotificationUri(getActivity().getContentResolver(), Downloads.CONTENT_URI);
			listView.setAdapter(downloadBaseAdapter);
		}
	}

	class DownloadIngBaseAdapter extends CursorAdapter {
		Cursor cursor;

		@Override
		protected void onContentChanged() {
			super.onContentChanged();
		}

		final private int mTitleColumnId;
		final private int mStatusColumnId;
		final private int mReasonColumnId;
		final private int mTotalBytesColumnId;
		final private int mCurrentBytesColumnId;
		final private int mMediaTypeColumnId;
		final private int mDateColumnId;
		final private int mIdColumnId;

		public DownloadIngBaseAdapter(Context context, Cursor cursor, boolean autoRequery) {
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
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			bindViewData(view, context, cursor);
		}

		@Override
		public View newView(Context context, Cursor mCursor, ViewGroup parent) {
			View convertView = LayoutInflater.from(context).inflate(R.layout.downloading_child_item, parent, false);
			return convertView;
		}

		private void bindViewData(View view, Context context, Cursor mCursor) {
			final Holder holder = new Holder();
			holder.download_progressbar = ViewUtility.findViewById(view, R.id.download_progressbar);
			holder.download_state_image = ViewUtility.findViewById(view, R.id.download_state_image);
			holder.download_intro = ViewUtility.findViewById(view, R.id.download_intro);
			holder.download_title = ViewUtility.findViewById(view, R.id.download_title);

			String title = mCursor.getString(mTitleColumnId);
			long totalBytes = mCursor.getLong(mTotalBytesColumnId);
			long currentBytes = mCursor.getLong(mCurrentBytesColumnId);
			holder.status = mCursor.getInt(mStatusColumnId);
			holder.mIdColumn = mCursor.getInt(mIdColumnId);

			holder.download_delete_layout = ViewUtility.findViewById(view, R.id.download_delete_layout, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDownloadManager.remove(holder.mIdColumn);
				}
			});
			if (title.length() == 0) {
				title = context.getString(R.string.missing_title);
			}

			holder.download_title.setText(title);

			int progress = getProgressValue(totalBytes, currentBytes);
			boolean indeterminate = holder.status == DownloadManager.STATUS_PENDING;
			holder.download_progressbar.setIndeterminate(indeterminate);
			if (!indeterminate) {
				holder.download_progressbar.setProgress(progress);
			}
			if (holder.status == DownloadManager.STATUS_FAILED || holder.status == DownloadManager.STATUS_SUCCESSFUL) {
				// holder.download_progressbar.setVisibility(View.GONE);
			} else {
				holder.download_progressbar.setVisibility(View.VISIBLE);
			}
			int stringid = getStatusStringResId(holder.status);
			if (stringid != 0) {
				holder.download_intro.setText(stringid);
			} else {
				String runningString=getSizeText(context, currentBytes) + "/" + (totalBytes>0?getSizeText(context, totalBytes):"未知");
				holder.download_intro.setText(progress == 0 ? getString(R.string.download_the_wait) :runningString);
			}
			holder.download_state_image.setImageResource(getStatusImageResId(holder.status));

			view.setTag(holder);
		}

		private String getSizeText(Context context, long totalBytes) {
			String sizeText = "";
			if (totalBytes >= 0) {
				sizeText = Formatter.formatFileSize(context, totalBytes);
			}
			return sizeText.replace("B", "");
		}

		public int getProgressValue(long totalBytes, long currentBytes) {
			if (totalBytes == -1) {
				return 0;
			}
			return (int) (currentBytes * 100 / totalBytes);
		}

		class Holder {
			ProgressBar download_progressbar;
			ImageView download_state_image;
			TextView download_intro;
			TextView download_title;
			View download_delete_layout;
			int status;
			int mIdColumn;

		}

		private int getStatusImageResId(int status) {
			switch (status) {
			case DownloadManager.STATUS_FAILED:
				return R.drawable.btn_down_stop;

			case DownloadManager.STATUS_SUCCESSFUL:
				return R.drawable.btn_down_waitting;

			case DownloadManager.STATUS_PENDING:
			case DownloadManager.STATUS_RUNNING:
				return R.drawable.btn_down_loading;

			case DownloadManager.STATUS_PAUSED:
				if (cursor.getInt(mReasonColumnId) == DownloadManager.PAUSED_QUEUED_FOR_WIFI) {
					return R.drawable.btn_down_stop;
				} else {
					return R.drawable.btn_down_stop;
				}
			}
			return R.drawable.btn_down_stop;
			// throw new IllegalStateException("Unknown status: " + cursor.getInt(mStatusColumnId));
		}

		private int getStatusStringResId(int status) {
			switch (status) {
			case DownloadManager.STATUS_FAILED:
				return R.string.click_to_redownload;
			case DownloadManager.STATUS_PENDING:
				return R.string.download_the_wait;
			case DownloadManager.STATUS_RUNNING:
				return 0;
			case DownloadManager.STATUS_PAUSED:
				if (cursor.getInt(mReasonColumnId) == DownloadManager.PAUSED_QUEUED_FOR_WIFI) {
					return R.string.waiting_wifi;
				} else {
					return R.string.click_continue_to_download;
				}
			}
			return 0;
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object object = view.getTag();
		handleItemClick(object);
	}

	private long lastTime = 0;
	private int id = 0;
	private final int INTERVALTIME = 1000;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(Assist.DOWNLOADING_LOADER_ID);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		super.onLoaderReset(arg0);
	}

	private void handleItemClick(Object object) {
		if (object != null && object instanceof Holder) {
			synchronized (DownloadIngFragment.class) {
				Holder holder = (Holder) object;
				if (id == holder.mIdColumn) {
					if (System.currentTimeMillis() - lastTime < INTERVALTIME) {
						return;
					}
				}
				id = holder.mIdColumn;
				switch (holder.status) {
				case DownloadManager.STATUS_FAILED:
				case DownloadManager.STATUS_SUCCESSFUL:
					mDownloadManager.restartDownload(holder.mIdColumn);
					break;
				case DownloadManager.STATUS_PAUSED:
					mDownloadManager.resumeDownload(holder.mIdColumn);
					break;
				case DownloadManager.STATUS_RUNNING:
					mDownloadManager.pauseDownload(holder.mIdColumn);
					break;
				default:
					break;
				}
			}
			lastTime = System.currentTimeMillis();
		}
	}
}
