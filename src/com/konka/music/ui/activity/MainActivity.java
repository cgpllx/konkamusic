package com.konka.music.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.fragment.MainFragment;
import com.konka.music.ui.fragment.SearchFragment;
import com.konka.music.ui.fragment.downloadmanager.DownloadParentFragment;
import com.konka.music.ui.widget.KGSeekBar;
import com.konka.music.util.AnimationUtil;
import com.konka.music.util.FragmentManagerUtil;
import com.konka.music.util.ViewTag;
import com.konka.music.util.ViewUtility;

public class MainActivity extends BaseActivity implements OnSeekBarChangeListener, OnClickListener {

	public MainViewHolder mainViewHolder = new MainViewHolder();
	private boolean DEVELOPER_MODE = false;
	MainFragment mainFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initKBaseViewHolder(mainViewHolder);
		mainFragment = new MainFragment();
		FragmentManagerUtil.swichFragment(getSupportFragmentManager(), mainFragment, false);

		if (DEVELOPER_MODE) {
			StrictMode.setVmPolicy(new VmPolicy.Builder()//
					.detectLeakedSqlLiteObjects()//
					.detectLeakedClosableObjects()//
					.detectActivityLeaks()//
					.detectAll()//
					.build());
		}
		MusicInfoManager.startService(this.getApplicationContext(), "dddd");
		onNewIntent(getIntent());
	}

	private void reasonIntentToSwitchFragment(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras.containsKey("fname")) {
			String fname = extras.getString("fname");
			Bundle bundle = intent.getExtras().getBundle("bundle");
			Fragment fragment = Fragment.instantiate(this, fname, bundle);
			FragmentManagerUtil.swichFragment(getSupportFragmentManager(), fragment, true);
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		switch (action) {
		case "swichFragment":
			int count = getSupportFragmentManager().getBackStackEntryCount();
			if (count > 0) {
				BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
				String backStackName = backStackEntry.getName();
				if (!DownloadParentFragment.TAG.equals(backStackName)) {
					reasonIntentToSwitchFragment(intent);
				}
			} else {
				reasonIntentToSwitchFragment(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initPlaybuttonView(getMusicInfo());
	}

	private void initKBaseViewHolder(MainViewHolder viewHolder) {
		viewHolder.playing_bar_toggle = ViewUtility.findViewById(this, R.id.playing_bar_toggle, this);
		viewHolder.playing_bar_toggle.setTag(ViewTag.PLAY);

		viewHolder.playing_bar_albumart = ViewUtility.findViewById(this, R.id.playing_bar_albumart, this);
		viewHolder.playing_bar_next = ViewUtility.findViewById(this, R.id.playing_bar_next, this);
		viewHolder.playing_bar_next.setTag(ViewTag.NEXT);
		viewHolder.playing_bar_seeker = ViewUtility.findViewById(this, R.id.playing_bar_seeker);
		viewHolder.playing_bar_seeker.setOnSeekBarChangeListener(this);
		viewHolder.playing_bar_seeker.setPreventTapping(true); // konka-wangxu-20141216-禁止点击
		viewHolder.playing_bar_song_name = ViewUtility.findViewById(this, R.id.playing_bar_song_name);
		// viewHolder.playing_bar_clickable_bg_flash = ViewUtility.findViewById(this, R.id.playing_bar_clickable_bg_flash, this);
		viewHolder.insert_music_note = ViewUtility.findViewById(this, R.id.insert_music_note);
		viewHolder.playing_bar_singer_name = ViewUtility.findViewById(this, R.id.playing_bar_singer_name);
		ViewUtility.findViewById(this, R.id.playing_bar_clickable_bg, this);

	}

	@Override
	public void onServiceBindComplete() {
		super.onServiceBindComplete();
		initPlaybuttonView(getMusicInfo());
	}

	public class MainViewHolder {
		public ImageButton playing_bar_toggle;
		public ImageButton playing_bar_next;
		public TextView playing_bar_song_name;
		public KGSeekBar playing_bar_seeker;
		public ImageView playing_bar_albumart;// 小图片
		// public View playing_bar_clickable_bg_flash;
		public ImageView insert_music_note;
		public TextView playing_bar_singer_name;
	}

	@Override
	public void onMusicBufferingUpdateProgress(int progress) {
		super.onMusicBufferingUpdateProgress(progress);
		mainViewHolder.playing_bar_seeker.setSecondaryProgress(progress);
	}

	@Override
	public void onMusicPlaybackProgress(int progress) {
		super.onMusicPlaybackProgress(progress);
		mainViewHolder.playing_bar_seeker.setProgress(progress);
	}

	@Override
	public void onMusicPause() {
		super.onMusicPause();
		updataView(R.drawable.ic_playing_bar_play);
	}

	/**
	 * 第一次初始化view状态
	 */
	private void initPlaybuttonView(MusicInfo musicInfo) {
		if (musicInfo != null) {
			mainViewHolder.playing_bar_song_name.setText(musicInfo.getMusicname());
			mainViewHolder.playing_bar_singer_name.setText(musicInfo.getSinger());
		} else {
			System.out.println("mainViewHolder=" + mainViewHolder);
			System.out.println("mainViewHolder.playing_bar_song_name=" + mainViewHolder.playing_bar_song_name);
			mainViewHolder.playing_bar_song_name.setText(getString(R.string.app_name));
			mainViewHolder.playing_bar_singer_name.setText(getString(R.string.spread_good_music));
		}
		updataView(isPlaying() ? R.drawable.ic_playing_bar_pause : R.drawable.ic_playing_bar_play);
		mainViewHolder.playing_bar_seeker.setProgress(0);
		mainViewHolder.playing_bar_seeker.setSecondaryProgress(0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.content, new SearchFragment());
			ft.commitAllowingStateLoss();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMusicPaly(MusicInfo musicinfo) {
		super.onMusicPaly(musicinfo);
		initPlaybuttonView(musicinfo);
		// Bitmap bitmap=MusicImage.getImageFromMp3File(musicinfo.getData());
		// if(bitmap!=null){
		// mainViewHolder.playing_bar_albumart.setImageBitmap(bitmap);
		// }else{
		// mainViewHolder.playing_bar_albumart.setImageResource(R.drawable.playing_bar_default_avatar);
		// }
	}

	private void updataView(int resId) {
		if (mainViewHolder != null && mainViewHolder.playing_bar_toggle != null)
			mainViewHolder.playing_bar_toggle.setImageResource(resId);
	}

	@Override
	public void onClick(View v) {
		Object tagobj = v.getTag();
		if (tagobj != null && tagobj instanceof String) {
			onViewTagClick((String) tagobj, null);
		}
		switch (v.getId()) {
		case R.id.playing_bar_clickable_bg:
		case R.id.playing_bar_clickable_bg_flash:
		case R.id.playing_bar_albumart:
			startActivity(new Intent(this, PlayerActivity.class));
			break;
		}
	}

	public void playAnimotion_playThisMusic(float fromXDelta, float fromYDelta, final MusicInfo musicInfo) {
		AnimationUtil.playAnimotion_playThisMusic(this, fromXDelta, fromYDelta, musicInfo, mainViewHolder.insert_music_note);
	}

	public void playAnimotion_playAllMusic(float fromXDelta, float fromYDelta, ArrayList<MusicInfo> musicInfos) {
		AnimationUtil.playAnimotion_playAllMusic(this, fromXDelta, fromYDelta, musicInfos, mainViewHolder.insert_music_note);
	}

	public void playAnimotion_insert(float fromXDelta, float fromYDelta) {
		AnimationUtil.playAnimotion_insert(this, fromXDelta, fromYDelta, mainViewHolder.insert_music_note);
	}

	@Override
	public void introductionData(MusicInfo musicinfo) {
		super.introductionData(musicinfo);
		// 有music传人到播放器了，需要initview
		initPlaybuttonView(musicinfo);
		updataView(R.drawable.ic_playing_bar_pause);

	}

	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && checkStackZore()) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// ToastUtil.showToast(MainActivity.this, "再按一次退出程序");
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
				return false;
			} else {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	// 播放进度不能比缓冲进度快,没有数据时候不应该拖动
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			int secondaryProgress = seekBar.getSecondaryProgress();
			if (secondaryProgress > 0) {
				if (progress > secondaryProgress) {
					seekBar.setProgress(secondaryProgress);
				}
			} else {
				MusicInfo musicInfo = getMusicInfo();
				if (musicInfo == null) {
					seekBar.setProgress(0);
				} else {
					if (musicInfo.getData().startsWith("http")) {
						seekBar.setProgress(0);
					}
					;
				}
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	private boolean checkStackZore() {

		return getSupportFragmentManager().getBackStackEntryCount() == 0;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int time = getDuration() * seekBar.getProgress() / 1000;
		seekTo(time);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mainViewHolder.playing_bar_seeker = null;
		mainViewHolder = null;
		mainFragment = null;
	}
}
