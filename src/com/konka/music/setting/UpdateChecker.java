package com.konka.music.setting;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.konka.music.R;
import com.konka.music.util.Assist;
import com.konka.music.util.Net;
import com.konka.music.util.SDCardUtil;
import com.konka.music.util.ToastUtil;
import com.kubeiwu.baseclass.util.KLog;

/**
 * Checks for updates to the game.
 */

public class UpdateChecker extends AsyncTask<Integer, Void, String> {

	private static final String TAG = "UpdateChecker";

	private ProgressBar mCreateCustomProgress;
	private Context mContext;
	private boolean hasException = false;
	private boolean self = false;

	public UpdateChecker(Context paramContext) {
		this.mContext = paramContext;
		initProgress();
	}

	public UpdateChecker(Context paramContext, boolean self) {
		this.mContext = paramContext;
		this.self = self;
	}

	private String checkForNewerVersion(int currentVersion) {

		return Net.getLatestVersionCode(Assist.UPGRADE_URL, currentVersion);
	}

	private void initProgress() {
	}

	public int getCurVernum() {
		int currentVersion = 0;
		try {
			currentVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KLog.v(TAG, "getCurVernum ... " + currentVersion);
		return currentVersion;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		// showProgressBar();
	}

	@Override
	protected String doInBackground(Integer... num) {
		this.hasException = false;
		return checkForNewerVersion(num[0].intValue());
	}

	@Override
	protected void onPostExecute(String upgradePath) {
		super.onPostExecute(upgradePath);

		if (isCancelled())
			return;

		if (mCreateCustomProgress != null) {
			mCreateCustomProgress.setVisibility(View.VISIBLE);
		}

		if (upgradePath != null && !TextUtils.isEmpty(upgradePath)) {
			if (!SDCardUtil.getInstance().hasInternalSD()/* !SDCardUtil.isSDCardExist() */) {
				ToastUtil.showToast(mContext, R.string.sdcard_not_avaliable);
				return;
			}

			final String apkPaht = upgradePath;
			AlertDialog dialog = new AlertDialog.Builder(this.mContext).setTitle(R.string.upgrade).setMessage(R.string.found_new_version).setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// UpdateManager updateManager = new UpdateManager(UpdateChecker.this.mContext, apkPaht);
					// updateManager.downloadApk();
					down(apkPaht);
				}
			}).setNegativeButton(R.string.update_later, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create();
			dialog.show();

		} else {
			ToastUtil.showToast(mContext, R.string.no_upgrade);
		}
	}

	protected void down(String url) {
		Uri resource = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir("/download/", "康佳音乐.apk");
		request.setTitle("康佳音乐.apk");
		DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
		long id = downloadManager.enqueue(request);
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().putLong("download_id", id).commit();
	}
}
