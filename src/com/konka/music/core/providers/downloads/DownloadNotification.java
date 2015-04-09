package com.konka.music.core.providers.downloads;

import java.util.Collection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.konka.music.R;
import com.konka.music.ui.activity.MainActivity;
import com.konka.music.ui.fragment.downloadmanager.DownloadParentFragment;

class DownloadNotification {
	private Notification notification;
	private NotificationManager manager;
	private final int NOTIF_CONNECTED = 2;
	private RemoteViews contentView_dif;
	private Context context;
	private boolean isShow = false;

	public DownloadNotification(Service mContext) {
		contentView_dif = new RemoteViews(mContext.getPackageName(), R.layout.download_notification_mian);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setAction("swichFragment");
		intent.putExtra("fname", DownloadParentFragment.TAG);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setAutoCancel(false);
		builder.setSmallIcon(R.drawable.download_notification_small_tips) // 这个不写不会显示
				.setContentIntent(pendingIntent);//
		notification = builder.build();
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.contentView = contentView_dif;
		manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.tickerText = "歌曲开始下载,点击查看";
		this.context = mContext;

	}

	public void updateNotification(Collection<DownloadInfo> downloadInfos) {
		contentView_dif.setTextViewText(R.id.download_notication_text, context.getString(R.string.downloading_listmun_tim, downloadInfos.size()));
		manager.notify(NOTIF_CONNECTED, notification);
		isShow = true;
	}

	public void updateNotificationComplete() {
		if(isShow){
			contentView_dif.setTextViewText(R.id.download_notication_text, "下载结束");
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			manager.notify(NOTIF_CONNECTED, notification);
			isShow=false;
		}
	}
}
