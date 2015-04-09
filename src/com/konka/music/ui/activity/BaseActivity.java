package com.konka.music.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.konka.music.listener.FACallBack;
import com.konka.music.listener.MusicPlayerAction;
import com.konka.music.player.MusicPalyModel;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.IMusicControl;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.service.MusicService;
import com.konka.music.util.Assist;
import com.konka.music.util.BroadcastReceiverUtil;
import com.konka.music.util.FragmentCache;
import com.konka.music.util.ViewTag;

public class BaseActivity extends SuperActivity implements FACallBack, MusicPlayerAction {
	protected IMusicControl mIMusicControl;
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mIMusicControl = IMusicControl.Stub.asInterface(service);
			onServiceBindComplete();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			mIMusicControl=null;
		}
	};

	public Fragment getFragment(Context mContext, Class<? extends Fragment> fclass, Bundle bundle) {
		Fragment fragment = FragmentCache.getFragment(fclass.getName());
		if (fragment == null) {
			fragment = Fragment.instantiate(mContext, fclass.getName(), bundle);
			FragmentCache.addFragment(fclass.getName(), fragment);
		}
		return fragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService(new Intent(this, MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
		BroadcastReceiverUtil.registerReceiver(this, receiver, Assist.actions);
	}

	// _______________播放器响应start
	@Override
	public void onMusicPaly(MusicInfo musicinfo) {
		handleListener(PlayAction.PLAY, musicinfo);
	}

	@Override
	public void onMusicPause() {
		handleListener(PlayAction.PAUSE, null);
	}
	@Override
	public void onMusicStop() {
		handleListener(PlayAction.STOP, null);
	}

	@Override
	public void onMusicPlaybackProgress(int progress) {
		handleListener(PlayAction.PROGRESS, progress);
	}

	@Override
	public void onMusicBufferingUpdateProgress(int progress) {
		handleListener(PlayAction.BUFFERINGUPDATEPROGRESS, progress);
	}

	@Override
	public void introductionData(MusicInfo musicinfo) {
		handleListener(PlayAction.INTRODUCTIONDATA, musicinfo);
	}


	// _______________播放器响应 end

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (TextUtils.isEmpty(action)) {
				return;
			}
			switch (action) {
			case Assist.BROADCAST_ACTION_PLAY:
				onMusicPaly(getMusicInfo());
				break;
			case Assist.BROADCAST_ACTION_NEXT:
				// onMusicNext();
				break;
			case Assist.BROADCAST_ACTION_PAUSE:
				onMusicPause();
				break;
			case Assist.BROADCAST_ACTION_STOP:
				onMusicStop();
				break;
			case Assist.BROADCAST_ACTION_PREV:
				// onMusicPrev();
				break;
			case Assist.BROADCAST_ACTION_UPDATA_PLAYBACKPROGRESS:// 播放进度
				int progress = intent.getExtras().getInt(Assist.KEY_PLAYPROGRESS, 0);
				onMusicPlaybackProgress(progress);
				break;
			case Assist.BROADCAST_ACTION_UPDATE_BUFFERING_PROGRESS:// 播放2进度
				progress = intent.getExtras().getInt(Assist.KEY_BUFFERINGUPDATE_PROGRESS, 0);
				onMusicBufferingUpdateProgress(progress);
				break;
			case Assist.BROADCAST_ACTION_INTRODUCTIONDATA:
				MusicInfo musicinfo = intent.getParcelableExtra(Assist.KEY_MUSICINFO);
				if (musicinfo != null) {
					introductionData(musicinfo);
				}
				break;
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
		BroadcastReceiverUtil.unregisterReceiver(this, receiver);
	}

	// -------------用户调用-start
	@Override
	public List<MusicInfo> getPlaylist() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getPlaylist();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MusicInfo getMusicInfo() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getMusicInfo();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getDuration() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getDuration();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;

	};

	@Override
	public int getCurPosition() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getCurPosition();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;

	};

	@Override
	public int getPlayListIndex() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getPlayListIndex();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public void setMusicPalyModel(int musicPalyModel) {
		try {
			if (mIMusicControl != null) {
				mIMusicControl.setMusicPalyModel(musicPalyModel);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getMusicPalyModel() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.getMusicPalyModel();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return MusicPalyModel.SINGLE_LOOP;
	}

	@Override
	public void seekTo(int progress) {// 毫秒
		try {
			if (mIMusicControl != null) {
				mIMusicControl.seekTo(progress);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPlaying() {
		try {
			if (mIMusicControl != null) {
				return mIMusicControl.isPlaying();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void clearPlayList() {
		try {
			if (mIMusicControl != null) {
				mIMusicControl.clearPlayList();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerListener(MusicPlayerAction action) {
		musicPlayerActions.add(action);
	}

	// -------------用户调用 end
	ArrayList<MusicPlayerAction> musicPlayerActions = new ArrayList<MusicPlayerAction>();

	@Override
	public void unRegisterListener(MusicPlayerAction action) {
		musicPlayerActions.remove(action);
	}

	void handleListener(int playAction, Object object) {
		for (MusicPlayerAction action : musicPlayerActions) {
			switch (playAction) {
			case PlayAction.PLAY:
				action.onMusicPaly((MusicInfo) object);
				break;
			case PlayAction.NEXT:
				// action.onMusicNext();
				break;
			case PlayAction.PAUSE:
				action.onMusicPause();
				break;
			case PlayAction.STOP:
				action.onMusicStop();
				break;
			case PlayAction.PREV:
				// action.onMusicPrev();
				break;
			case PlayAction.PROGRESS:
				action.onMusicPlaybackProgress((int) object);
				break;
			case PlayAction.INTRODUCTIONDATA:
				action.introductionData((MusicInfo) object);
				break;
			case PlayAction.SERVICEBINDCOMPLETE:
				action.onServiceBindComplete();
				break;
			case PlayAction.BUFFERINGUPDATEPROGRESS:
				action.onMusicBufferingUpdateProgress((int) object);
				break;
			}
		}

	}

	interface PlayAction {
		int PLAY = 0, NEXT = 1, PAUSE = 2, STOP = 3, PREV = 4,//
				PROGRESS = 6, INTRODUCTIONDATA = 7, SERVICEBINDCOMPLETE = 8, BUFFERINGUPDATEPROGRESS = 9;
	}

	@Override
	public void onServiceBindComplete() {
		handleListener(PlayAction.SERVICEBINDCOMPLETE, null);
	}



	@Override
	public void onViewTagClick(String viewTag, ArrayList<MusicInfo> musicInfos) {
		switch (viewTag) {
		case ViewTag.PLAY:
			MusicInfoManager.startService(this, Assist.SERVICE_ACTION_PLAY_OR_PAUSE);
			break;
		case ViewTag.NEXT:
			MusicInfoManager.startService(this, Assist.SERVICE_ACTION_NEXT);
			break;
		case ViewTag.PREV:
			MusicInfoManager.startService(this, Assist.SERVICE_ACTION_PREV);
			break;
		case ViewTag.PLAYALL:
			MusicInfoManager.addMusicInfoArray(this, musicInfos, true);
			break;
		}
	}
}
