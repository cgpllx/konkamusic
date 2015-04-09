//package com.konka.music.ui.fragment;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.PopupMenu.OnMenuItemClickListener;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.konka.lrc.ISetLrcProgress;
//import com.konka.lrc.LyricObject;
//import com.konka.lrc.LyricView;
//import com.konka.lrc.ParserLrc;
//import com.konka.music.R;
//import com.konka.music.adapter.PopupAdapter;
//import com.konka.music.player.MusicPalyModel;
//import com.konka.music.pojo.MusicInfo;
//import com.konka.music.service.MusicInfoManager;
//import com.konka.music.ui.fragment.abstractfragment.AbstractKBaseFragment;
//import com.konka.music.ui.view.PlayListView;
//import com.konka.music.util.ArrayUtils;
//import com.konka.music.util.TimeHelper;
//import com.konka.music.util.ViewTag;
//import com.konka.music.util.ViewUtility;
//
//public class MusicPlayFragment_bf extends AbstractKBaseFragment implements OnItemClickListener, ISetLrcProgress {
//	public static final String TAG = AbstractKBaseFragment.class.getSimpleName();
//	private MusicPlayViewHolder mMusicPlayViewHolder = new MusicPlayViewHolder();
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.layout_musicplay, container, false);
//		return view;
//	}
//
//	private PopupMenu mOverflowPopupMenu = null;
//	List<LyricObject> lrcList;
//
//	@SuppressLint("SdCardPath")
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		mLyricView = (LyricView) view.findViewById(R.id.lyricshow);
//		initViewHolder(mMusicPlayViewHolder);
//
//		String path = "/mnt/sdcard/konka/分手的理由.lrc";
//		try {
//			lrcList = ParserLrc.parserFile(path);
//		} catch (IOException e) {
//			e.printStackTrace();
//			Log.e("Ouyang", "解析歌词文件出错：" + e.toString());
//		}
//		mLyricView.setLyricObjects(lrcList);
//		mLyricView.setLrcInterface(this);
//	}
//
//	@Override
//	public void onServiceBindComplete() {
//		super.onServiceBindComplete();
//		// initPlaybuttonView();
//
//		assignViewHolderDisplay(mMusicPlayViewHolder);
//	}
//
//	@Override
//	public void onMusicPlaybackProgress(int progress) {
//		super.onMusicPlaybackProgress(progress);
//		mMusicPlayViewHolder.play_progress.setProgress(progress);
//		mLyricView.setCurPosition(getCurPosition());
//	}
//
//	private void assignViewHolderDisplay(MusicPlayViewHolder musicPlayViewHolder) {
//		MusicInfo musicInfo = getMusicInfo();
//		if (musicInfo != null) {
//			mMusicPlayViewHolder.play_song_title.setText(musicInfo.getTitle());
//			// index=getMusicPalyModel();
//			musicPlayViewHolder.play_mode.setImageResource(musicPalyModels[index]);
//		}
//	}
//
//	private void initViewHolder(MusicPlayViewHolder musicPlayViewHolder) {
//		musicPlayViewHolder.play_playbutton = ViewUtility.findViewById(getActivity(), R.id.play_playbutton, this);
//		initPlaybuttonView();
//		musicPlayViewHolder.play_playbutton.setTag(ViewTag.PLAY);
//		musicPlayViewHolder.play_playnext = ViewUtility.findViewById(getActivity(), R.id.play_playnext, this);
//		musicPlayViewHolder.play_playnext.setTag(ViewTag.NEXT);
//		musicPlayViewHolder.play_playprevious = ViewUtility.findViewById(getActivity(), R.id.play_playprevious, this);
//		musicPlayViewHolder.play_playprevious.setTag(ViewTag.PREV);
//		musicPlayViewHolder.play_progress = ViewUtility.findViewById(getActivity(), R.id.play_progress);
//		musicPlayViewHolder.play_progress.setOnSeekBarChangeListener(this);
//		musicPlayViewHolder.play_mode = ViewUtility.findViewById(getActivity(), R.id.play_mode, this);
//		musicPlayViewHolder.play_list = ViewUtility.findViewById(getActivity(), R.id.play_list, this);
//		musicPlayViewHolder.play_button_back = ViewUtility.findViewById(getActivity(), R.id.play_button_back, this);
//		musicPlayViewHolder.play_current_time = ViewUtility.findViewById(getActivity(), R.id.play_current_time);
//		musicPlayViewHolder.play_song_total_time = ViewUtility.findViewById(getActivity(), R.id.play_song_total_time);
//		musicPlayViewHolder.play_song_title = ViewUtility.findViewById(getActivity(), R.id.play_song_title);
//
//		musicPlayViewHolder.play_more_functions = ViewUtility.findViewById(getActivity(), R.id.play_more_functions, this);
//
//		mOverflowPopupMenu = new PopupMenu(getActivity(), musicPlayViewHolder.play_more_functions);
//		mOverflowPopupMenu.getMenuInflater().inflate(R.menu.track_operations_in_player, mOverflowPopupMenu.getMenu());
//		mOverflowPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				MusicInfo mPlaySong = null;
//				mPlaySong = getMusicInfo();
//				if (mPlaySong != null) {
//					DialogFragment df = null;
//
//					switch (item.getItemId()) {
//					case R.id.track_addto:
//
//						break;
//					case R.id.track_info:
//						if (mPlaySong != null) {
//							df = TrackDetailDialogFragment.newInstance(mPlaySong);
//							df.show(getFragmentManager(), null);
//						}
//						break;
//					}
//				}
//
//				return true;
//			}
//		});
//	}
//
//	/**
//	 * 第一次初始化view状态
//	 */
//	private void initPlaybuttonView() {
//		updataView(isPlaying() ? R.drawable.button_pause : R.drawable.button_play);
//	}
//
//	List<LyricObject> lyricObjects;
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.play_playbutton:
//			break;
//		case R.id.play_playnext:
//			mLyricView.setLyricObjects(null);
//			break;
//		case R.id.play_playprevious:
//			mLyricView.setLyricObjects(null);
//			break;
//		case R.id.play_mode:
//			index = ++index % 4;
//			setMusicPalyModel(musicPalyModels[index]);
//			mMusicPlayViewHolder.play_mode.setImageResource(musicPalyModels[index]);
//
//			break;
//		case R.id.play_button_back:
//			getActivity().finish();
//			return;
//		case R.id.play_list:
//			showpup(v);
//			break;
//		case R.id.play_more_functions:
//			mOverflowPopupMenu.show();
//			break;
//		}
//		super.onClick(v);
//	}
//
//	PlayListView mPlayListView;
//	PopupAdapter adapter;
//
//	public void showpup(View v) {
//		mPlayListView = new PlayListView(getActivity());
//		mPlayListView.setOnItemClickListener(this);
//		adapter = new PopupAdapter(getActivity());
//		List<MusicInfo> musicInfos = getPlaylist();
//		if (!ArrayUtils.isEmpty(musicInfos)) {
//			adapter.addAll(musicInfos);
//		}
//		mPlayListView.setAdapter(adapter);
//		mPlayListView.setAnchorView(v);
//		mPlayListView.show();
//
//	}
//
//	public static int index = 0;
//
//	private int[] musicPalyModels = { MusicPalyModel.REPEAT, MusicPalyModel.SINGLE_LOOP, MusicPalyModel.RANDOM, MusicPalyModel.SEQUENCE };
//	private LyricView mLyricView;
//
//	private class MusicPlayViewHolder {
//		public ImageButton play_playbutton;// 开关
//		public ImageButton play_playnext;// 下一首歌
//		public ImageButton play_playprevious;// 上一首歌
//		public SeekBar play_progress;
//		public ImageView play_mode;// 播放模式(单曲循环...)
//		public ImageView play_list;// 播放列表play_list
//		public ImageView play_button_back;// 返回
//		public TextView play_current_time;// 当前播放时长
//		public TextView play_song_total_time;// 过去总时间
//		public TextView play_song_title;// 播放歌曲的标题
//		public ImageButton play_more_functions;
//	}
//
//	@Override
//	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//		super.onProgressChanged(seekBar, progress, fromUser);
//		mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString//
//				(progress * getDuration() / seekBar.getMax()));
//
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		MusicInfo musicInfo = adapter.getItem(position);
//		MusicInfoManager.addMusic2PlayList(getActivity(), musicInfo, true);
//	}
//
//	@Override
//	public void onMusicPaly(MusicInfo musicInfo) {
//		super.onMusicPaly(musicInfo);
//		mMusicPlayViewHolder.play_current_time.setText(TimeHelper.milliSecondsToFormatTimeString(0));
//		mMusicPlayViewHolder.play_song_total_time.setText//
//				(TimeHelper.milliSecondsToFormatTimeString(getDuration()));
//		mMusicPlayViewHolder.play_song_title.setText(musicInfo.getTitle());
//
//		updataView(R.drawable.button_pause);
//	}
//
//	@Override
//	public void onMusicPause() {
//		super.onMusicPause();
//		updataView(R.drawable.button_play);
//	}
//
//	private void updataView(int resId) {
//		mMusicPlayViewHolder.play_playbutton.setImageResource(resId);
//	}
//
//	@Override
//	public void setLrcProgress(int progress) {
//		seekTo(progress);
//	}
//
//	@Override
//	protected ArrayList<MusicInfo> getMusicInfoArray() {
//		return null;
//	}
//
//}