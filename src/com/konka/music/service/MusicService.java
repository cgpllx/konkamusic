package com.konka.music.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.konka.music.player.MusicPalyModel;
import com.konka.music.player.MusicPlayEngineImpl;
import com.konka.music.player.PlayerEngineListener;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.BroadcastReceiverUtil;
import com.konka.music.util.MusicInfoUtil;
import com.konka.music.util.NetWorkManager;
import com.konka.music.util.NotificationUtil;
import com.konka.music.util.ParaSetting;
import com.konka.music.util.ToastUtil;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.util.KLog;
import com.ouyang.music.showlock.ShowLockUtil;

public class MusicService extends Service implements PlayerEngineListener, IServiceHandle, OnBufferingUpdateListener {
	public static final String TAG = MusicService.class.getSimpleName();
	public NotificationUtil mNotificationUtil;
	private MusicPlayEngineImpl mMusicPlayEngineImpl;
	private int musicPalyModel = MusicPalyModel.REPEAT;// 默认列表循环播放
	private int playPosition = 0;// 播放下标
	private MyHandler myHandler;
	public static final int SCROLL_WHAT = 1;
	public static final int PLAYCOMPLETEACTION = 2;
	private Random mRandom = new Random();
	private LinkedList<MusicInfo> musicList = new LinkedList<MusicInfo>();// 播放队列
	private static final String PALYLISTQUEUETABNAME = "palylistqueuetabname";

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationUtil = new NotificationUtil(this);
		startForeground(mNotificationUtil.NOTIF_CONNECTED, mNotificationUtil.notification);
		BroadcastReceiverUtil.registerReceiver(this, mNotificationUtil, Assist.actions);
		BroadcastReceiverUtil.registerReceiver(this, mScreenOffReceiver, Assist.lackBroadcastActions);
		mMusicPlayEngineImpl = new MusicPlayEngineImpl();
		mMusicPlayEngineImpl.setPlayerListener(this);
		mMusicPlayEngineImpl.setOnBuffUpdateListener(this);
		mMusicPlayEngineImpl.setWakeMode(this);
		myHandler = new MyHandler(this);
		initplaylist();
		registerReceiver(receiver, filter);
	}

	IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			long myDownloadReference = PreferenceManager.getDefaultSharedPreferences(MusicService.this).getLong("download_id", 0);
			if (myDownloadReference == reference) {
				DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				Uri downloadFileUri = dManager.getUriForDownloadedFile(myDownloadReference);
				installApk(downloadFileUri);
			}
		}
	};

	private void initplaylist() {
		ArrayList<MusicInfo> lists = MusicApplication.mKCommonToolDb.findAllFromTab(MusicInfo.class, PALYLISTQUEUETABNAME);
		if (!ArrayUtils.isEmpty(lists)) {
			musicList.addAll(lists);
		}
	}

	private void installApk(Uri downloadFileUri) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}

	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			try {
				if (action.equals("android.intent.action.SCREEN_ON")) {
					if (ParaSetting.LOCK_SCREEN.value && mMusicPlayEngineImpl.isPlaying()) {
						KLog.d("Ouyang","收到消息：android.intent.action.SCREEN_ON");
						 ShowLockUtil.startShowActivity(MusicService.this);
					}
				}
				if (action.equals("android.intent.action.SCREEN_OFF")) {
					KLog.d("Ouyang","收到消息：android.intent.action.SCREEN_OFF");
				}
			} catch (Exception e) {
				System.out.println("出错了");
				e.printStackTrace();
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null)
			return START_NOT_STICKY;
		handleIntent(intent, flags, startId);
		System.out.println("启动了服务");
		return START_NOT_STICKY;// 关闭后不重启
	}

	private void handleIntent(Intent intent, int flags, int startId) {

		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)) {
			switch (action) {
			case Assist.SERVICE_ACTION_NEXT:
				this.next();
				break;
			case Assist.SERVICE_ACTION_CLOSE:
				this.stop();
				stopSelf(startId);
				BroadcastReceiverUtil.sendBroadcast(this, Assist.BROADCAST_ACTION_EXIT);
				// Util.exit();
				break;
			case Assist.SERVICE_ACTION_PLAY_OR_PAUSE:
				this.playOrPause();
				break;
			case Assist.SERVICE_ACTION_PREV:
				this.prev();
				break;
			case Assist.SERVICE_ACTION_Add_MUSIC:
				MusicInfo mMusicInfo = intent.getExtras().getParcelable(Assist.KEY_MUSICINFO);
				boolean playthis = intent.getExtras().getBoolean(Assist.KEY_PLAYTHIS, false);
				if (mMusicInfo != null) {
					addMusicInfo(mMusicInfo, playthis);
				}
				break;
			case Assist.SERVICE_ACTION_ADD_MUSICINFO_ARRAY:
				List<MusicInfo> mMusicInfos = intent.getExtras().getParcelableArrayList(Assist.KEY_MUSICINFO_LIST);
				playthis = intent.getExtras().getBoolean(Assist.KEY_PLAYTHIS, false);
				if (!ArrayUtils.isEmpty(mMusicInfos)) {
					this.addMusicInfoToPlayList(mMusicInfos, playthis);
				}
				break;
			case Assist.SERVICE_ACTION_PALYMUSIC_OF_POSITION_THE_LIST:
				int index = intent.getIntExtra(Assist.KEY_PLAYLIST_INDEX, 0);
				palyMusicOnListIndex(index);
				break;
			case Assist.SERVICE_ACTION_CLEARPALYLIST:
				clearPlayList();
				break;
			case "startScreen":

				break;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myIBinder;
	}

	static class MyHandler extends WeakHandler<MusicService> {
		public MyHandler(MusicService service) {
			super(service);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MusicService musicService = getOwner();
			switch (msg.what) {
			case SCROLL_WHAT:
				if (musicService != null) {
					int duration = musicService.mMusicPlayEngineImpl.getDuration();
					if (duration != 0) {
						int progress = musicService.mMusicPlayEngineImpl.getCurPosition() * 1000 / duration;
						if (musicService.mMusicPlayEngineImpl.isPlaying()) {
							BroadcastReceiverUtil.sendBroadcast(musicService, Assist.BROADCAST_ACTION_UPDATA_PLAYBACKPROGRESS, progress);
						}
					}
					musicService.sendUpdataMessage(Assist.RATE);
				}
				break;
			case PLAYCOMPLETEACTION:
				musicService.palyComlateAction();
				break;
			default:
				break;
			}
		}
	}

	private void palyComlateAction() {
		switch (musicPalyModel) {
		case MusicPalyModel.SEQUENCE:
			playPosition = reviseIndex(++playPosition);
			if (playPosition != 0) {
				paly();
			}
			break;
		case MusicPalyModel.SINGLE_LOOP:
			paly();
			break;
		case MusicPalyModel.RANDOM:
			playPosition = reviseIndex(mRandom.nextInt(musicList.size()));
			paly();
			break;
		case MusicPalyModel.REPEAT:
			playPosition = reviseIndex(++playPosition);
			paly();
			break;
		}
	}

	private void sendUpdataMessage(long delayTimeInMills) {
		myHandler.removeMessages(SCROLL_WHAT);
		myHandler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	private final IMusicControl.Stub myIBinder = new IMusicControl.Stub() {

		@Override
		public void setMusicPalyModel(int musicPalyModel) throws RemoteException {
			MusicService.this.setMusicPalyModel(musicPalyModel);
		}

		@Override
		public void seekTo(int progress) throws RemoteException {
			mMusicPlayEngineImpl.skipTo(progress);
		}

		@Override
		public boolean isPlaying() throws RemoteException {
			return mMusicPlayEngineImpl.isPlaying();
		}

		@Override
		public List<MusicInfo> getPlaylist() throws RemoteException {
			return MusicService.this.getPlaylist();
		}

		@Override
		public MusicInfo getMusicInfo() throws RemoteException {
			return mMusicPlayEngineImpl.getMusicInfo();
		}

		@Override
		public int getDuration() throws RemoteException {
			return mMusicPlayEngineImpl.getDuration();
		}

		@Override
		public int getCurPosition() throws RemoteException {
			return mMusicPlayEngineImpl.getCurPosition();
		}

		@Override
		public int getMusicPalyModel() throws RemoteException {
			return MusicService.this.musicPalyModel;
		}

		@Override
		public int getPlayListIndex() throws RemoteException {
			return playPosition;
		}

		@Override
		public void clearPlayList() throws RemoteException {
			MusicService.this.clearPlayList();
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		savePlayList();
		stopForeground(true);
		mNotificationUtil.cancelNotification();
		BroadcastReceiverUtil.unregisterReceiver(this, mNotificationUtil);
		BroadcastReceiverUtil.unregisterReceiver(this, mScreenOffReceiver);
		mNotificationUtil = null;
		unregisterReceiver(receiver);
	}

	private void savePlayList() {
		MusicApplication.mKCommonToolDb.deleteAllByTab(MusicInfo.class, PALYLISTQUEUETABNAME);
		if (!ArrayUtils.isEmpty(musicList)) {
			MusicApplication.mKCommonToolDb.insertAll2Tab(musicList, PALYLISTQUEUETABNAME);
		}
	}

	/**
	 * 正在开始播放
	 */
	@Override
	public void onTrackPlay(MusicInfo itemInfo) {
		sendUpdataMessage(500);
		mNotificationUtil.show(itemInfo);
		BroadcastReceiverUtil.sendBroadcast(this, Assist.BROADCAST_ACTION_PLAY);
	}

	/**
	 * 停止播放
	 */

	@Override
	public void onTrackStop(MusicInfo itemInfo) {
		BroadcastReceiverUtil.sendBroadcast(this, Assist.BROADCAST_ACTION_STOP);
	}

	/**
	 * 暂停播放
	 */

	@Override
	public void onTrackPause(MusicInfo itemInfo) {
		BroadcastReceiverUtil.sendBroadcast(this, Assist.BROADCAST_ACTION_PAUSE);
	}

	/**
	 * 开始加载
	 */
	@Override
	public void onTrackPrepareSync(MusicInfo itemInfo) {
		if (itemInfo.getData().startsWith("http")) {
			ToastUtil.showLongToast(this, "正在缓冲...");
		}
	}

	/**
	 * 加载完成
	 */
	@Override
	public void onTrackPrepareComplete(MusicInfo musicInfo) {
		musicInfo.setHistory(1);
		MusicInfoUtil.setMusicInfoToHistory(musicInfo);
		ToastUtil.cancelToast();// 都取消通知
		errorCount = 0;
	}

	/**
	 * 加载出错
	 */
	@Override
	public void onTrackStreamError(MusicInfo itemInfo) {
		if (NetWorkManager.isConnected(this)) {
			ToastUtil.showLongToast(this, "(" + itemInfo.getDisplayName() + ")加载出错");
//			ToastUtil.showLongToast(this, "(" + itemInfo.getDisplayName() + ")加载出错,马上为您加载下一曲");
		} else {
			ToastUtil.showLongToast(this, "网络不可用");
		}
		errorCount++;
	}

	@Override
	public void onTrackIntroductionData(MusicInfo itemInfo) {// 数据传入时候初始化ui
		BroadcastReceiverUtil.sendBroadcastAttExtra(this, Assist.BROADCAST_ACTION_INTRODUCTIONDATA, itemInfo);
		removeMessages();
	}

	private static int errorCount = 0;

	/**
	 * 播放完成
	 */
	@Override
	public void onTrackPlayComplete(MusicInfo itemInfo) {// 如果有错误会一直循环跳动
		removeMessages();
		if (errorCount <= 0) {
			myHandler.sendEmptyMessageDelayed(PLAYCOMPLETEACTION, 3000);
		}
	}

	int i = 0;
	int j = 0;

	private void removeMessages() {
		if (myHandler.hasMessages(PLAYCOMPLETEACTION)) {
			myHandler.removeMessages(PLAYCOMPLETEACTION);
		}
	}

	/**
	 * 此方法防止数组角标越界
	 * 
	 * @param playPosition
	 * @return
	 */
	private int reviseIndex(int playPosition) {
		if (playPosition >= musicList.size()) {
			playPosition = 0;
		} else if (playPosition < 0) {
			playPosition = musicList.size() - 1 < 0 ? 0 : musicList.size() - 1;
		}
		return playPosition;
	}

	@Override
	public void prev() {
		if (musicPalyModel == MusicPalyModel.RANDOM) {
			playPosition = reviseIndex(mRandom.nextInt(musicList.size()));
		} else {
			playPosition = reviseIndex(--playPosition);
		}
		paly();
	}

	@Override
	public void next() {
		if (musicPalyModel == MusicPalyModel.RANDOM) {
			playPosition = reviseIndex(mRandom.nextInt(musicList.size()));
		} else {
			playPosition = reviseIndex(++playPosition);
		}
		paly();
	}

	public void palyMusicOnListIndex(int index) {
		if (index != playPosition) {
			playPosition = index;
			paly();
		}else{
			if(mMusicPlayEngineImpl.isPause()){
				continuePlay();
			}else{
				paly();
			}
		}
	}

	@Override
	public void paly() {
		if (musicList.size() > reviseIndex(playPosition)) {
			mMusicPlayEngineImpl.playMedia(musicList.get(reviseIndex(playPosition)));
		}
	}

	public void playOrPause() {
		if (mMusicPlayEngineImpl.isPlaying()) {
			mMusicPlayEngineImpl.pause();
		} else {
			if (mMusicPlayEngineImpl.getMusicInfo() == null) {
				paly();
			} else {
				mMusicPlayEngineImpl.play();
			}
		}
	}

	@Override
	public void pause() {
		mMusicPlayEngineImpl.pause();
	}

	public void stop() {
		mMusicPlayEngineImpl.stop();
	};

	public void clearPlayList() {
		if (musicList != null && musicList.size() > 0) {
			musicList.clear();
			stop();
			playPosition =0;
		}

	}

	@Override
	public void addMusicInfoToPlayList(List<MusicInfo> musicLists, boolean playthis) {
		if (!ArrayUtils.isEmpty(musicLists)) {
			this.musicList.clear();
			this.musicList.addAll(musicLists);
			if (playthis) {
				playPosition = 0;
				paly();
			}
		}
	}

	@Override
	public void setMusicPalyModel(int PalyModel) {
		MusicService.this.musicPalyModel = PalyModel;
	}

	@Override
	public List<MusicInfo> getPlaylist() {
		return musicList;// 不会为null
	}

	@Override
	public void continuePlay() {
		if (mMusicPlayEngineImpl.isPause()) {
			mMusicPlayEngineImpl.play();// 如果是暂停，会继续播放，负责是重新播放
		}
	}

	@Override
	public void addMusicInfo(MusicInfo mMusicInfo, boolean playthis) {
		if (mMusicInfo != null) {
			musicList.add(musicList.size(), mMusicInfo);
			if (playthis) {
				playPosition = musicList.size() - 1;
				paly();
			}
		}
	}

	private boolean first100 = false;

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int progress) {
		// arg0.getTrackInfo()
		if (progress < 100) {
			first100 = false;
			sendBroadToHandleBufferingUpdate(arg0, progress);
		} else if (progress == 100) {
			if (!first100) {
				sendBroadToHandleBufferingUpdate(arg0, progress);
				first100 = true;
			}
		}
	}

	private void sendBroadToHandleBufferingUpdate(MediaPlayer arg0, int progress) {
		BroadcastReceiverUtil.sendBroadcast(this, Assist.BROADCAST_ACTION_UPDATE_BUFFERING_PROGRESS, Assist.KEY_BUFFERINGUPDATE_PROGRESS, progress * 10);
	}
}
