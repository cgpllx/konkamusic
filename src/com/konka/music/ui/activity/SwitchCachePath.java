package com.konka.music.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.util.FileUtils;
import com.konka.music.util.MyPreference;
import com.konka.music.util.SDCardUtil;
import com.konka.music.util.SDCardUtil.CapacityUtil;
import com.konka.music.wedget.MusicApplication;
/**
 * @author wangxu
 * @description 选择缓存歌曲路径
 * */
public class SwitchCachePath extends SuperActivity implements OnClickListener{
	
	private LinearLayout mInternalPathLayout, mExternalPathLayout;  //内置 ， 外置
	private TextView mInternalCapacityTxt, mExternalCapacityTxt; 	//内置总容量， 外置总容量
	private TextView mInternalMovieTxt, mExternalMovieTxt;			//内置影片容量，外置影片容量
	private TextView mInternalOtherTxt, mExternalOtherTxt;			//内置其他容量，外置其他容量
	private TextView mInternalSurplusTxt, mExternalSurplusTxt;		//内置剩余容量，外置剩余容量
	private TextView mInternalRadioBtn, mExternalRadioBtn;
	private ProgressBar mInternalProgressBar, mExternalProgressBar;	//内置容量条，外置容量条
	private ProgressBar mProgressBar;

	private String mDownloadPath = "";
	private SDCardUtil mSdCardUtil;
	private CapacityUtil mCapacityUtil1, mCapacityUtil2;
	
	private MusicApplication mApplication;
	
	private static final String KEY_DOWNLOAD_PATH = "download_path";
	private static final int MSG_SHOW_VIEW = 1;
	private static final int TYPE_INTERNAL = 2;
	private static final int TYPE_EXTERNAL = 3;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_VIEW:
				dismissProgress();
				showView();
				break;

			default:
				break;
			}
		}
	};
	
	private void choseSDCardState(boolean internal) {
		if(internal) {
			choseInternalSDCardState();
		}else {
			choseExternalSDCardState();
		}
	}
	
	private void choseInternalSDCardState() {
		try {
			mDownloadPath = mSdCardUtil.getInternalSDPath();
			MyPreference.putPref(KEY_DOWNLOAD_PATH, mDownloadPath);
			mInternalRadioBtn.setEnabled(true);
			mInternalPathLayout.setClickable(false);
			mExternalRadioBtn.setEnabled(false);
			mExternalPathLayout.setClickable(true);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void choseExternalSDCardState() {
		try {
			mDownloadPath = mSdCardUtil.getExternalSDPath();
			MyPreference.putPref(KEY_DOWNLOAD_PATH, mDownloadPath);
			mInternalRadioBtn.setEnabled(false);
			mInternalPathLayout.setClickable(true);
			mExternalRadioBtn.setEnabled(true);
			mExternalPathLayout.setClickable(false);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dismissProgress() {
		if(mProgressBar!=null)
			mProgressBar.setVisibility(View.GONE);
	}
	
	private int getCurrentType() {
		if(mSdCardUtil == null)
			mSdCardUtil = SDCardUtil.getInstance();
		return mSdCardUtil.getCurrentSDPath().equals(mSdCardUtil.getInternalSDPath())?TYPE_INTERNAL:TYPE_INTERNAL;
	}
	
	private String getCurrentName() {
		return getCurrentType()==TYPE_INTERNAL?"手机存储":"SD卡存储";
	}
	
	private int getNextType() {
		if(mSdCardUtil == null)
			mSdCardUtil = SDCardUtil.getInstance();
		return mSdCardUtil.getCurrentSDPath().equals(mSdCardUtil.getInternalSDPath())?TYPE_EXTERNAL:TYPE_INTERNAL;
	}
	
	private String getNextName() {
		return getNextType()==TYPE_INTERNAL?"手机存储":"SD卡存储";
	}
	
	private void hideInternalView() {
		mInternalPathLayout.setVisibility(View.GONE);
	}
	
	private void hideExternalView() {
		mExternalPathLayout.setVisibility(View.GONE);
	}
	
	private boolean isInternalSDCard() {
		if(mDownloadPath.equals(mSdCardUtil.getInternalSDPath()))
			return true;
		return false;
	}
	
	private boolean isExternalSDCard() {
		if(mDownloadPath.equals(mSdCardUtil.getExternalSDPath()))
			return true;
		return false;
	}
	
	private void initData() {
		mSdCardUtil = SDCardUtil.getInstance();
		mDownloadPath = MyPreference.getPref(KEY_DOWNLOAD_PATH, mApplication.getDownloadMusicPath());
//		KLog.i("wangxu", "SwitchCachePath->initData->mDownloadPath=" + mDownloadPath);
	}
	
	private void initView() {
		//View
		mInternalRadioBtn = (TextView)findViewById(R.id.cache_internal_sdcard_path_radio);
		mExternalRadioBtn = (TextView)findViewById(R.id.cache_external_sdcarch_path_radio);
		mInternalPathLayout = (LinearLayout)findViewById(R.id.cache_internal_sdcard_path_layout);
		mExternalPathLayout = (LinearLayout)findViewById(R.id.cache_external_sdcarch_path_layout);
		mProgressBar = (ProgressBar)findViewById(R.id.mprogressbar);
		//Inflater
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 100);
		//内置存储
		View externalView = inflater.inflate(R.layout.cache_bottom_showsize, null);
		externalView.setLayoutParams(params);
		externalView.setBackgroundResource(R.color.white);
		mInternalCapacityTxt = (TextView)externalView.findViewById(R.id.sd_capacity);
		mInternalMovieTxt = (TextView)externalView.findViewById(R.id.sd_offline_capacity);
		mInternalOtherTxt = (TextView)externalView.findViewById(R.id.sd_other_capacity);
		mInternalSurplusTxt = (TextView)externalView.findViewById(R.id.sd_suplus_capacity);
		mInternalProgressBar = (ProgressBar)externalView.findViewById(R.id.progressbar_capacity);
		mInternalPathLayout.addView(externalView);
		//外置存储
		View view = inflater.inflate(R.layout.cache_bottom_showsize, null);
		view.setLayoutParams(params);
		view.setBackgroundResource(R.color.white);
		mExternalCapacityTxt = (TextView)view.findViewById(R.id.sd_capacity);
		mExternalMovieTxt = (TextView)view.findViewById(R.id.sd_offline_capacity);
		mExternalOtherTxt = (TextView)view.findViewById(R.id.sd_other_capacity);
		mExternalSurplusTxt = (TextView)view.findViewById(R.id.sd_suplus_capacity);
		mExternalProgressBar = (ProgressBar)view.findViewById(R.id.progressbar_capacity);
		mExternalPathLayout.addView(view);
		
		mInternalProgressBar.setMax(100);
		mInternalProgressBar.setProgress(0);
		mInternalProgressBar.setSecondaryProgress(0);
		mExternalProgressBar.setMax(100);
		mExternalProgressBar.setSecondaryProgress(0);
		mExternalProgressBar.setProgress(0);
		
		mInternalPathLayout.setOnClickListener(this);
		mExternalPathLayout.setOnClickListener(this);
	}
	
	private void prepareActionBar() {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setTitle("设置");
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setLogo(R.drawable.d_title_back_new);
	}
	
	private void showDialog(final boolean internal) {
		AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("新增缓存将存储在"+getNextName()+"，之前已缓存视频路径不变。")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					choseSDCardState(getNextType()==TYPE_INTERNAL);
					dialog.dismiss();
					choseSDCardState(internal);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.create();
		dialog.show();
	}
	
	private void showInternalView(CapacityUtil capacityUtil) {
		if(capacityUtil == null)
			return;
		mInternalCapacityTxt.setText("" + FileUtils.formatFileSize(capacityUtil.totalSize));
		mInternalMovieTxt.setText("本地缓存 " + FileUtils.formatFileSize(capacityUtil.movieSize));
		mInternalOtherTxt.setText("其他 " + FileUtils.formatFileSize(capacityUtil.otherSize-capacityUtil.movieSize));
		mInternalSurplusTxt.setText("剩余 " + FileUtils.formatFileSize(capacityUtil.avaliableSize));
		mInternalProgressBar.setProgress((int)(capacityUtil.movieSize/capacityUtil.totalSize*mInternalProgressBar.getMax()));
		mInternalProgressBar.setSecondaryProgress((int)((capacityUtil.otherSize)/capacityUtil.totalSize*mInternalProgressBar.getMax()));
	}
	
	
	private void showExternalView(CapacityUtil capacityUtil) {
		if(capacityUtil == null)
			return;
		mExternalCapacityTxt.setText("" + FileUtils.formatFileSize(capacityUtil.totalSize));
		mExternalMovieTxt.setText("本地缓存 " + FileUtils.formatFileSize(capacityUtil.movieSize));
		mExternalOtherTxt.setText("其他 " + FileUtils.formatFileSize(capacityUtil.otherSize-capacityUtil.movieSize));
		mExternalSurplusTxt.setText("剩余 " + FileUtils.formatFileSize(capacityUtil.avaliableSize));
		mExternalProgressBar.setProgress((int)((capacityUtil.movieSize/capacityUtil.totalSize*mExternalProgressBar.getMax())));
		mExternalProgressBar.setSecondaryProgress((int)((capacityUtil.otherSize)/capacityUtil.totalSize*mExternalProgressBar.getMax()));
	}
	
	private void showProgress() {
		if(mProgressBar != null)
			mProgressBar.setVisibility(View.VISIBLE);
	}
	
	private void showView() {
		showInternalView(mCapacityUtil1);
		showExternalView(mCapacityUtil2);
	}
	
	private void updateSDCardState() {
		if(isExternalSDCard()) {
			choseExternalSDCardState();
		}else if(isInternalSDCard()){
			choseInternalSDCardState();
		}else {
			//SD卡未挂载，显示空页面
		}
	}
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mApplication = (MusicApplication)getApplication();
		prepareActionBar();
		setContentView(R.layout.download_switch_path);
		initData();
		initView();
		updateSDCardState();
		showProgress();
		new Thread() {
			@Override
			public void run() {
				mCapacityUtil1 = mSdCardUtil.getCapacity(mSdCardUtil.getInternalSDPath(), mSdCardUtil.getInternalSDPath()+("/konka/video/"));
				mCapacityUtil2 = mSdCardUtil.getCapacity(mSdCardUtil.getExternalSDPath(), mSdCardUtil.getExternalSDPath()+("/konka/video/"));
				mHandler.sendEmptyMessage(MSG_SHOW_VIEW);
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
        return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cache_internal_sdcard_path_layout:
//			choseInternalSDCardState();
			showDialog(true);
			break;
		case R.id.cache_external_sdcarch_path_layout:
//			choseExternalSDCardState();
			showDialog(false);
			break;
		}
	}
	
}
