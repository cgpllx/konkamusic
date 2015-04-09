package com.konka.music.setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.konka.music.R;
import com.konka.music.ui.activity.MainActivity;
import com.konka.music.util.ToastUtil;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.util.KLog;

public class UpdateManager {
	
	private Context mContext;
	private String apkUrl ; 
	private String saveFileName = "";
	public ProgressBar mProgress;
	
	private Thread downLoadThread;
	
	private int downloadCount = 0;
    private int _progress = 0;
    private boolean isStop = false;
    
    private Notification notification = null;
    private NotificationManager manager = null;
	
	private static final int DOWN_UPDATE 	= 1;
	private static final int DOWN_OVER 		= 2;
	private static final int DOWN_ERROR 	= 3;
	private static final int NOTIFICATION_ID = 0x12;
	
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
									
			switch (msg.what) {
			case DOWN_UPDATE:
				notification.contentView.setProgressBar(R.id.progressBar1, 100, msg.arg1, false);
				notification.contentView.setTextViewText(R.id.textView1, "进度"+msg.arg1+"%");
				manager.notify(NOTIFICATION_ID, notification);
				KLog.d("test", " "+msg.arg1);
				break;
			case DOWN_OVER:
				_progress = 0;
				manager.cancel(NOTIFICATION_ID);
				isStop = false;
				ToastUtil.showToast(mContext, R.string.upgrade_download_apk_success);
				
//				if (downloadDialog != null)
//					downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_ERROR:
//				downloadDialog.dismiss();
				manager.cancel(NOTIFICATION_ID);
				ToastUtil.showToast(mContext, R.string.upgrade_download_apk_failure);
				break;
				
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, String apkUrl) {
		this.saveFileName = ((MusicApplication)context.getApplicationContext()).getDownloadAppPath() + "/kplayer.apk";
		this.mContext = context;
		this.apkUrl = apkUrl;
		KLog.i("wangxu", "saveFileName=" + saveFileName);
		notification = new Notification(R.drawable.icon, context.getResources().getString(R.string.config_software_upgrade), 
				System.currentTimeMillis());

        notification.contentView = new RemoteViews(mContext.getApplicationContext().getPackageName(), R.layout.notify_content);
        notification.contentView.setProgressBar(R.id.progressBar1, 100, 0, false);
        notification.contentView.setTextViewText(R.id.textView1, "下载进度 " + _progress + "%");
        
        notification.contentIntent = PendingIntent.getActivity(mContext, 0,new Intent(mContext, MainActivity.class), 0);
      
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

//				File file = new File(savePath);
//				if (!file.exists()) {
//					file.mkdir();
//				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					_progress = (int) (((float) count / length) * 100);
					if(downloadCount == 0 || _progress-5>downloadCount){
						downloadCount += 5; 
						Message msg = mHandler.obtainMessage();
		                msg.arg1 = downloadCount;
		                msg.what = DOWN_UPDATE;
		                msg.sendToTarget();
					}

					if (numread <= 0) {

						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (isStop);

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				mHandler.sendEmptyMessage(DOWN_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				mHandler.sendEmptyMessage(DOWN_ERROR);
				e.printStackTrace();
			}

		}
	};

	public void downloadApk() {
		isStop = true;
        manager.notify(NOTIFICATION_ID, notification);
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			KLog.d("downloadapk", "apkfile is not exist, return!" );
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // huanghui 解决安装完成不弹出完成 打开界面
		mContext.startActivity(i);

	}
}
