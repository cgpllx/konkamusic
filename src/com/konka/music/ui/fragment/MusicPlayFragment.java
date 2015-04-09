package com.konka.music.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.konka.lrc.ISetLrcProgress;
import com.konka.lrc.LyricObject;
import com.konka.lrc.LyricView;
import com.konka.lrcUtil.LrcUtil;
import com.konka.music.R;
import com.konka.music.adapter.PopupAdapter;
import com.konka.music.listener.MusicPlayerAction;
import com.konka.music.musicexception.DownLoadException;
import com.konka.music.player.MusicPalyModel;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.fragment.abstractfragment.AbstractKBaseFragment;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.MusicInfoUtil;
import com.konka.music.util.TimeHelper;
import com.konka.music.util.Util;
import com.konka.music.util.ViewTag;
import com.konka.music.util.ViewUtility;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MusicPlayFragment extends AbstractKBaseFragment implements OnItemClickListener, ISetLrcProgress, MusicPlayerAction {
	public static final String TAG = AbstractKBaseFragment.class.getName();
	private MusicPlayViewHolder mMusicPlayViewHolder = new MusicPlayViewHolder();
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.player_fragment);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mLyricView = (LyricView) view.findViewById(R.id.lyricshow);
		try{
		initViewHolder(mMusicPlayViewHolder);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		mLyricView.setLrcInterface(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (fACallBack != null) {
			fACallBack.registerListener(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (fACallBack != null) {
			fACallBack.unRegisterListener(this);
		}
	}

	@Override
	public void onServiceBindComplete() {
		// initPlaybuttonView();
		assignViewHolderDisplay(getMusicInfo());
	}

	@Override
	public void onMusicPlaybackProgress(int progress) {
		mMusicPlayViewHolder.play_progress.setProgress(progress);
		mLyricView.setCurPosition(getCurPosition());
	}

	private void instanceDownBtn(MusicInfo musicInfo) {

		if (musicInfo.getData().startsWith("http")) {
			try {
				MusicInfoUtil.checkMusicInfoDownload(musicInfo);
				mMusicPlayViewHolder.player_btn_download.setImageResource(R.drawable.btn_download_default_1);
				mMusicPlayViewHolder.player_btn_download.setEnabled(true);
				return;
			} catch (DownLoadException e) {
				// 已经下载过了，不用重新下载
			}
		}
		mMusicPlayViewHolder.player_btn_download.setImageResource(R.drawable.btn_download_default_2);
		mMusicPlayViewHolder.player_btn_download.setEnabled(false);
	}

	private void assignViewHolderDisplay(MusicInfo musicInfo) {
		mMusicPlayViewHolder.play_mode.setImageResource(musicPalyModels[index]);
		instanceLikeBtn(musicInfo);
		showLrc(musicInfo);
		initPlaybuttonView();
		if (musicInfo != null) {
			mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString(getCurPosition()));
			mMusicPlayViewHolder.play_song_total_time.setText(TimeHelper.milliSecondsToFormatTimeString(getDuration()));
			mMusicPlayViewHolder.play_song_title.setText(musicInfo.getMusicname());
			mMusicPlayViewHolder.play_song_songer.setText(musicInfo.getSinger());
			instanceDownBtn(musicInfo);
		} else {
			mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString(0));
			mMusicPlayViewHolder.play_song_total_time.setText(TimeHelper.milliSecondsToFormatTimeString(0));
			mMusicPlayViewHolder.play_song_title.setText("");
			mMusicPlayViewHolder.play_song_songer.setText("");
			mMusicPlayViewHolder.player_btn_download.setImageResource(R.drawable.btn_download_default_2);
			mMusicPlayViewHolder.player_btn_download.setEnabled(false);
		}
	}

	private void initViewHolder(MusicPlayViewHolder musicPlayViewHolder) {
		musicPlayViewHolder.play_playbutton = ViewUtility.findViewById(getActivity(), R.id.player_playback_toggle, this);
		// initPlaybuttonView();
		musicPlayViewHolder.play_playbutton.setTag(ViewTag.PLAY);
		musicPlayViewHolder.play_playnext = ViewUtility.findViewById(getActivity(), R.id.player_playback_next, this);
		musicPlayViewHolder.play_playnext.setTag(ViewTag.NEXT);
		musicPlayViewHolder.play_playprevious = ViewUtility.findViewById(getActivity(), R.id.player_playback_prev, this);
		musicPlayViewHolder.play_playprevious.setTag(ViewTag.PREV);
		musicPlayViewHolder.play_progress = ViewUtility.findViewById(getActivity(), R.id.player_seeker);
		musicPlayViewHolder.play_progress.setOnSeekBarChangeListener(this);
		musicPlayViewHolder.play_mode = ViewUtility.findViewById(getActivity(), R.id.player_playback_mode, this);
		musicPlayViewHolder.play_list = ViewUtility.findViewById(getActivity(), R.id.player_btn_current_playlist, this);
		musicPlayViewHolder.player_btn_back = ViewUtility.findViewById(getActivity(), R.id.player_btn_back, this);
		musicPlayViewHolder.play_current_time = ViewUtility.findViewById(getActivity(), R.id.player_current_time);
		musicPlayViewHolder.play_song_total_time = ViewUtility.findViewById(getActivity(), R.id.player_total_time);
		musicPlayViewHolder.play_song_title = ViewUtility.findViewById(getActivity(), R.id.player_song_name);
		musicPlayViewHolder.play_song_songer = ViewUtility.findViewById(getActivity(), R.id.player_singer_name);
		// musicPlayViewHolder.player_btn_share = ViewUtility.findViewById(getActivity(), R.id.player_btn_share, this);
		musicPlayViewHolder.player_btn_like = ViewUtility.findViewById(getActivity(), R.id.player_btn_like, this);
		musicPlayViewHolder.player_btn_download = ViewUtility.findViewById(getActivity(), R.id.player_btn_download, this);

	}

	/**
	 * 第一次初始化view状态
	 */
	private void initPlaybuttonView() {
		updataView(isPlaying() ? R.drawable.ic_player_pause : R.drawable.ic_player_play);
	}

	List<LyricObject> lyricObjects;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_playbutton:
			break;
		case R.id.play_playnext:
			mLyricView.setLyricObjects(null);
			break;
		case R.id.play_playprevious:
			mLyricView.setLyricObjects(null);
			break;
		case R.id.player_playback_mode:
			index = ++index % 4;
			setMusicPalyModel(musicPalyModels[index]);
			mMusicPlayViewHolder.play_mode.setImageResource(musicPalyModels[index]);
			break;
		case R.id.play_button_back:
			getActivity().finish();
			return;

		case R.id.player_btn_current_playlist: // 播放列表
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, new PlayerQueueListFragment(), "");
			ft.addToBackStack(null);
			ft.commit();
			break;
		// case R.id.play_list:
		// case R.id.play_more_functions:
		// break;
		case R.id.player_btn_download:
			MusicInfo musicInfo = getMusicInfo();
			if (musicInfo != null) {
				Util.downLoadMusic(getActivity(), musicInfo);
				instanceDownBtn(musicInfo);
			}
			break;
		case R.id.player_btn_like:// 我喜欢
			doClickMyLike();
			break;
		case R.id.player_btn_back:// 我喜欢
			getActivity().finish();
			break;
		}
		super.onClick(v);
	}

	private void doClickMyLike() {
		MusicInfo musicInfo = getMusicInfo();
		if (musicInfo != null) {
			musicInfo.setFavourite(~musicInfo.getFavourite());
			MusicInfoUtil.setMusicInfoToLike(musicInfo);
			instanceLikeBtn(musicInfo);
		}
	}

	private void instanceLikeBtn(MusicInfo musicInfo) {
		if (musicInfo != null) {
			MusicInfoUtil.handleMyLikeFromDb(musicInfo);
			mMusicPlayViewHolder.player_btn_like.setImageResource(musicInfo.getFavourite() == 0 ? R.drawable.ic_player_like : R.drawable.ic_player_liked);
		} else {
			mMusicPlayViewHolder.player_btn_like.setImageResource(R.drawable.ic_player_like);
		}

	}

	PopupAdapter adapter;
	private static int index = 0;

	private int[] musicPalyModels = { MusicPalyModel.REPEAT, MusicPalyModel.SINGLE_LOOP, MusicPalyModel.RANDOM, MusicPalyModel.SEQUENCE };
	private LyricView mLyricView;

	private class MusicPlayViewHolder {
		public ImageButton play_playbutton;// 开关 ok
		public ImageButton play_playnext;// 下一首歌ok
		public ImageButton play_playprevious;// 上一首歌ok
		public SeekBar play_progress;// ok
		public ImageView play_mode;// 播放模式(单曲循环...)ok
		public ImageView play_list;// 播放列表play_listok
		public ImageView player_btn_back;// 返回ok
		public TextView play_current_time;// 当前播放时长ok
		public TextView play_song_total_time;// 过去总时间ok
		public TextView play_song_title;// 播放歌曲的标题ok
		public TextView play_song_songer;// 作者
		// public ImageButton player_btn_share;
		public ImageButton player_btn_like;
		public ImageButton player_btn_current_playlist;
		public ImageButton player_btn_download;// 下载按钮
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		super.onProgressChanged(seekBar, progress, fromUser);
		mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString//
				(progress * getDuration() / seekBar.getMax()));

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MusicInfo musicInfo = adapter.getItem(position);
		MusicInfoManager.addMusic2PlayList(getActivity(), musicInfo, true);
	}

	@Override
	public void onMusicPaly(MusicInfo musicInfo) {
		updataView(R.drawable.ic_player_pause);
		mMusicPlayViewHolder.play_song_total_time.setText(TimeHelper.milliSecondsToFormatTimeString(getDuration()));
	}

	@Override
	public void introductionData(MusicInfo musicInfo) {
		mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString(0));
		mMusicPlayViewHolder.play_song_total_time.setText(TimeHelper.milliSecondsToFormatTimeString(getDuration()));
		mMusicPlayViewHolder.play_song_title.setText(musicInfo.getMusicname());
		mMusicPlayViewHolder.play_song_songer.setText(musicInfo.getSinger());
		mMusicPlayViewHolder.play_progress.setProgress(0);
		mMusicPlayViewHolder.play_progress.setSecondaryProgress(0);
		updataView(R.drawable.ic_player_pause);
		instanceLikeBtn(musicInfo);
		showLrc(musicInfo);
		instanceDownBtn(musicInfo);
		// mLyricView.setLyricObjects(lrcList);
	}

	private void showLrc(MusicInfo musicInfo) {
		if (musicInfo != null) {
			String lrcUrl = musicInfo.getLrc_url();
			if (TextUtils.isEmpty(lrcUrl)) {
				LrcUtil.showLrc(mLyricView, musicInfo.getMusicname(), musicInfo.getSinger());
			} else {
				LrcUtil.showOnlineLrc(mLyricView, musicInfo.getMusicname(), lrcUrl);
			}
		} else {
			mLyricView.setLyricObjects(null);
		}
	}

	@Override
	public void onMusicPause() {
		updataView(R.drawable.ic_player_play);
	}

	@Override
	public void onMusicStop() {
		assignViewHolderDisplay(null);
		updataView(R.drawable.ic_player_play);

	}

	private void updataView(int resId) {
		mMusicPlayViewHolder.play_playbutton.setImageResource(resId);
	}

	@Override
	public void setLrcProgress(int progress) {
		seekTo(progress);
	}

	@Override
	public void onMusicBufferingUpdateProgress(int progress) {
		mMusicPlayViewHolder.play_progress.setSecondaryProgress(progress);
	}

}