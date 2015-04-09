package com.konka.music.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicService;
import com.konka.music.ui.activity.MainActivity;

public class NotificationUtil extends BroadcastReceiver {
	public final int NOTIF_CONNECTED = 3;
	public NotificationManager manager;
	public Notification notification;

	public NotificationUtil(Service mContext) {
		RemoteViews	contentView_dif = new RemoteViews(mContext.getPackageName(), R.layout.statusbar);
		contentView_dif.setOnClickPendingIntent(R.id.statusbar_super_content_next_btn, getPendingIntent(mContext, Assist.SERVICE_ACTION_NEXT));
		contentView_dif.setOnClickPendingIntent(R.id.statusbar_super_content_pause_or_play, getPendingIntent(mContext, Assist.SERVICE_ACTION_PLAY_OR_PAUSE));
		contentView_dif.setOnClickPendingIntent(R.id.statusbar_super_content_close_btn, getPendingIntent(mContext, Assist.SERVICE_ACTION_CLOSE));

		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		stackBuilder.addNextIntentWithParentStack(new Intent(mContext, MainActivity.class));
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(NOTIF_CONNECTED, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setAutoCancel(false);
		builder.setContentIntent(resultPendingIntent);//
		builder.setSmallIcon(R.drawable.statusbar_logo_small);// builder.setSmallIcon(R.drawable.ic_launcher);
		notification = builder.build();
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.contentView = contentView_dif;
		manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void show(MusicInfo musicInfo) {
		if (musicInfo != null) {
			notification.contentView.setTextViewText(R.id.statusbar_track_name, musicInfo.getTitle());
			notification.tickerText = musicInfo.getTitle();
		}
		manager.notify(NOTIF_CONNECTED, notification);
	 
	}

	private PendingIntent getPendingIntent(Context mContext, String action) {
		Intent intent = new Intent(mContext, MusicService.class);
		intent.setAction(action);
		PendingIntent next = PendingIntent.getService(mContext, 0, intent, 0);
		return next;
	}

	public void cancelNotification() {
		manager.cancelAll();
		notification.contentView=null;
		notification=null;
		manager=null;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		switch (action) {
		case Assist.BROADCAST_ACTION_PLAY:
			updataNotifi(R.drawable.statusbar_btn_pause);
			break;
		case Assist.BROADCAST_ACTION_NEXT:

			break;
		case Assist.BROADCAST_ACTION_PAUSE:
			updataNotifi(R.drawable.statusbar_btn_play);
			break;
		case Assist.BROADCAST_ACTION_PREV:

			break;
		default:
			break;
		}
		manager.notify(NOTIF_CONNECTED, notification);
	}
	private void updataNotifi(int resId){
		notification.contentView.setImageViewResource(R.id.statusbar_super_content_pause_or_play,resId);
		
	}
}
