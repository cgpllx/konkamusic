package com.konka.music.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.PowerManager;

import com.konka.music.pojo.MusicInfo;

public abstract class AbstractMediaPlayEngine implements IBasePlayEngine, OnCompletionListener, OnPreparedListener, OnErrorListener {

	// private static final CommonLog log = LogFactory.createLog();

	protected MediaPlayer mMediaPlayer;
	protected MusicInfo mMusicInfo;
	protected Context mContext;
	protected int mPlayState;

	protected PlayerEngineListener mPlayerEngineListener;

	protected abstract boolean prepareSelf();

	protected abstract boolean prepareComplete(MediaPlayer mp);

	public void setLooping(boolean looping) {
		mMediaPlayer.setLooping(looping);
	}

	protected void defaultParam() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// mMediaPlayer.set
		// mMediaPlayer.setOnBufferingUpdateListener(this);
		mMusicInfo = null;
		mPlayState = PlayState.MPS_NOFILE;

	}

	public void setWakeMode(Context context) {
		// 确保我们的MediaPlayer在播放时获取了一个唤醒锁，
		// 如果不这样做，当歌曲播放很久时，CPU进入休眠从而导致播放停止
		// 要使用唤醒锁，要确保在AndroidManifest.xml中声明了android.permission.WAKE_LOCK权限
		mMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
	}

	public AbstractMediaPlayEngine() {
		defaultParam();
	}

	public void setPlayerListener(PlayerEngineListener listener) {
		mPlayerEngineListener = listener;
	}

	@Override
	public void play() {

		switch (mPlayState) {
		case PlayState.MPS_PAUSE:
			mMediaPlayer.start();
			mPlayState = PlayState.MPS_PLAYING;
			performPlayListener(mPlayState);
			break;
		case PlayState.MPS_STOP:
			prepareSelf();
			break;
		default:
			break;
		}

	}

	@Override
	public void pause() {

		switch (mPlayState) {
		case PlayState.MPS_PLAYING:
			mMediaPlayer.pause();
			mPlayState = PlayState.MPS_PAUSE;
			performPlayListener(mPlayState);
			break;
		default:
			break;
		}

	}

	@Override
	public void stop() {
		if (mPlayState != PlayState.MPS_NOFILE) {
			mMediaPlayer.reset();
			mMusicInfo = null;
			mPlayState = PlayState.MPS_STOP;
			performPlayListener(mPlayState);
		}
	}

	@Override
	public void skipTo(int time) {

		switch (mPlayState) {
		case PlayState.MPS_PLAYING:
		case PlayState.MPS_PAUSE:
			int time2 = reviceSeekValue(time);
			mMediaPlayer.seekTo(time2);
			break;
		default:
			break;
		}

	}

	public void exit() {
		stop();
		mMediaPlayer.release();
		mMusicInfo = null;
		mPlayState = PlayState.MPS_NOFILE;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {

		prepareComplete(mp);
	}

	// 播放完毕
	@Override
	public void onCompletion(MediaPlayer mp) {
		// log.e("onCompletion...");
		if (mPlayerEngineListener != null) {
			mPlayerEngineListener.onTrackPlayComplete(mMusicInfo);
		}

	}

	public boolean isPlaying() {
		return mPlayState == PlayState.MPS_PLAYING;
	}

	public boolean isPause() {
		return mPlayState == PlayState.MPS_PAUSE;
	}

	public void playMedia(MusicInfo mediaInfo) {

		if (mediaInfo != null) {
			mMusicInfo = mediaInfo;
			if (mPlayerEngineListener != null) {
				mPlayerEngineListener.onTrackIntroductionData(mediaInfo);
			}
			prepareSelf();
		}
	}

	public int getCurPosition() {
		if (mPlayState == PlayState.MPS_PLAYING || mPlayState == PlayState.MPS_PAUSE) {
			return mMediaPlayer.getCurrentPosition();
		}

		return 0;
	}

	public int getDuration() {

		switch (mPlayState) {
		case PlayState.MPS_PLAYING:
		case PlayState.MPS_PAUSE:
		case PlayState.MPS_PARECOMPLETE:
			return mMediaPlayer.getDuration();
		}

		return 0;
	}

	public int getPlayState() {
		return mPlayState;
	}

	public MusicInfo getMusicInfo() {
		return mMusicInfo;
	}

	protected void performPlayListener(int playState) {
		if (mPlayerEngineListener != null) {
			switch (playState) {
			case PlayState.MPS_INVALID:
				mPlayerEngineListener.onTrackStreamError(mMusicInfo);
				break;
			case PlayState.MPS_STOP:
				mPlayerEngineListener.onTrackStop(mMusicInfo);
				break;
			case PlayState.MPS_PLAYING:
				mPlayerEngineListener.onTrackPlay(mMusicInfo);
				break;
			case PlayState.MPS_PAUSE:
				mPlayerEngineListener.onTrackPause(mMusicInfo);
				break;
			case PlayState.MPS_PARESYNC:
				mPlayerEngineListener.onTrackPrepareSync(mMusicInfo);
				break;
			}
		}
	}

	private int reviceSeekValue(int value) {
		if (value < 0) {
			value = 0;
		}

		if (value > mMediaPlayer.getDuration()) {
			value = mMediaPlayer.getDuration();
		}

		return value;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {

		// log.e("onError --> what = " + what);
		mPlayState = PlayState.MPS_INVALID;
		performPlayListener(mPlayState);
		return false;
	}

}
