package com.konka.music.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.konka.music.R;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.service.ShakeService;
import com.konka.music.setting.UpdateChecker;
import com.konka.music.util.ParaSetting;
import com.konka.music.util.SDCardUtil;
import com.konka.music.util.ToastUtil;
import com.konka.music.util.Util;
import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.requestimpl.ClearCacheRequest;
import com.kubeiwu.commontool.view.setting.GroupView;
import com.kubeiwu.commontool.view.setting.KSettingView;
import com.kubeiwu.commontool.view.setting.RowView;
import com.kubeiwu.commontool.view.setting.viewimpl.CheckBoxRowView;
import com.kubeiwu.commontool.view.setting.viewimpl.DefaultRowView;
import com.kubeiwu.commontool.view.util.DisplayOptions;
import com.kubeiwu.commontool.view.util.ItemBgSelectorUtil.RowStyle;
import com.kubeiwu.commontool.view.util.OnRowClickListener;
import com.kubeiwu.commontool.view.util.RowViewActionEnum;

public class SettingActivityFragment extends SuperActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayOptions selectorPara = new DisplayOptions.Builder()//
				.setGroupTitleSizePx(15)//
				.setOut_circle_Size(10)//
				.setNormalLineColorId(R.color.setting_line_color).setRowStyle(RowStyle.ALL_AROUND).setRowleftpadding(21)//
				.build();

		ScrollView scrollView = new ScrollView(this);
		scrollView.setVerticalScrollBarEnabled(false);
		View view = initView(selectorPara);
		view.setPadding(20, 10, 20, 10);
		scrollView.addView(view);

		setContentView(scrollView);
		getActionBar().setTitle("设置");
	}

	protected View initView(DisplayOptions selectorPara) {
		KSettingView containerView = new KSettingView(SettingActivityFragment.this);

		containerView.setDisplayOptions(selectorPara);
		GroupView groupView2 = containerView.addGroupViewItem(2, "控制功能");
		GroupView groupView3 = containerView.addGroupViewItem(3, "其他功能");
		GroupView groupView4 = containerView.addGroupViewItem(3, "");

		groupView2.addRowViewItem(CheckBoxRowView.class, 1, "锁屏显示", R.drawable.setting_view_item_selector, ParaSetting.LOCK_SCREEN).setOnRowClickListener(getOnRowClickListener(CheckBoxRowView.class));// .setPara(ParaSetting.LOCK_SCREEN);
		groupView2.addRowViewItem(CheckBoxRowView.class, 2, "摇一摇切歌", R.drawable.setting_view_item_selector, ParaSetting.WAVE_CUT_SONG).setOnRowClickListener(getOnRowClickListener(CheckBoxRowView.class));
		// groupView2.addRowViewItem(CheckBoxRowView.class, 8, "仅在wifi联网", R.drawable.setting_view_item_selector, ParaSetting.only_wifi_network);
		if (SDCardUtil.getInstance().hasExternalSD()) // konka-wangxu-20141216-有两个sd卡是方显示此设置
			groupView2.addRowViewItem(DefaultRowView.class, 3, "自定义歌曲下载目录", R.drawable.arrow_to_right, ParaSetting.CUSTOM_SONG_DOWNLOAD_DIRECTORY).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class));
		groupView2.addRowViewItem(DefaultRowView.class, 4, "清空缓存", R.drawable.arrow_to_right, ParaSetting.CLEAR_CACHE).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class));
		groupView3.addRowViewItem(DefaultRowView.class, 5, "用户反馈", R.drawable.arrow_to_right, ParaSetting.FOR_MY_SCORE).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class));
		groupView3.addRowViewItem(DefaultRowView.class, 6, "检测更新", R.drawable.arrow_to_right, ParaSetting.CHECK_FOR_UPDATES).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class)); /* .setOnRowClickListener(getOnRowClickListener(DefaultRowView.class)) */
		groupView3.addRowViewItem(DefaultRowView.class, 7, "关于我们", R.drawable.arrow_to_right, ParaSetting.ABOUT_US).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class));/* .setOnRowClickListener(getOnRowClickListener(DefaultRowView.class)) */

		groupView4.addRowViewItem(DefaultRowView.class, 8, "退出", R.drawable.arrow_to_right, ParaSetting.EXIT).setOnRowClickListener(getOnRowClickListener(DefaultRowView.class));/* .setOnRowClickListener(getOnRowClickListener(DefaultRowView.class)) */

		containerView.commit();
		return containerView;
	}

	public <T extends RowView> OnRowClickListener<T> getOnRowClickListener(Class<T> clazz) {
		return new OnRowClickListener<T>() {
			@Override
			public void onRowClick(T rowView, RowViewActionEnum action) {
				switch (rowView.getItemId()) {
				case 1:// 锁屏显示
					doClickPowerOffShow();
					break;
				case 2:// 摇一摇切歌
					doClickSwitchMusic((CheckBoxRowView) rowView);
					break;
				case 3:// 自定义歌曲下载目录
					doClickDownloadPath();
					break;
				case 4:// 清空缓存
					doClickClearCache();
					break;
				case 5:// 反馈
					doClickFeedback();
					break;
				case 6:// 检测更新
					doClickUpgrade();
					break;
				case 7:// 关于我们
					doClickAboutUs();
					break;
				case 8:// 关于我们
					onExit();
					break;
				}
				// ToastUtil.showToast(SettingActivityFragment.this, rowView.getTitle().toString());
			}
		};
	}

	protected void onExit() {
		MusicInfoManager.exitApp(this);
	}

	private void doClickPowerOffShow() {
		// System.out.println(ParaSetting.LOCK_SCREEN.value);
	}

	private void doClickSwitchMusic(CheckBoxRowView t) {
		if (t.getCurrentValue()) { // 开通摇一摇切歌
			ShakeService.actionStart(getApplicationContext());
		} else { // 关闭摇一摇切歌
			ShakeService.actionStop(getApplicationContext());
		}
	}

	private void doClickDownloadPath() {
		startActivity(new Intent(SettingActivityFragment.this, SwitchCachePath.class));
	}

	private void doClickClearCache() {
		Toast.makeText(getApplicationContext(), "正在清除...", Toast.LENGTH_SHORT).show();
		KRequestQueueManager.getRequestQueue().add(new ClearCacheRequest(KRequestQueueManager.getRequestQueue().getCache(), new Runnable() {
			@Override
			public void run() {
				ToastUtil.showToast(SettingActivityFragment.this, "清空完成");
			}
		}));
	}

	private void doClickFeedback() {
		startActivity(new Intent(SettingActivityFragment.this, FeedBackActivity.class));
	}

	private void doClickUpgrade() {
		UpdateChecker updatechker = new UpdateChecker(SettingActivityFragment.this);
		int curver = updatechker.getCurVernum();
		if (!Util.checkIntent(SettingActivityFragment.this)) {
			ToastUtil.showToast(SettingActivityFragment.this, R.string.no_network);
		} else {
			updatechker.execute(Integer.valueOf(curver));
		}
	}

	private void doClickAboutUs() {
		startActivity(new Intent(SettingActivityFragment.this, AboutActivity.class));
	}
}
