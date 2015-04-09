package com.konka.music.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;

public class AnimationUtil {
	public static void playAnimotion_playThisMusic(final Context context, float fromXDelta, float fromYDelta, final MusicInfo musicInfo, final ImageView imageview) {

		AnimationSet animationSet = new AnimationSet(true);
		Animation translateAnimation = new TranslateAnimation(fromXDelta - (imageview.getWidth() >> 1), fromXDelta - (imageview.getWidth() >> 1), fromYDelta, 800f);
		animationSet.addAnimation(translateAnimation);
		animationSet.setDuration(800);
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				imageview.setImageResource(R.drawable.music_note);
				imageview.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageview.setVisibility(View.GONE);
				MusicInfoManager.addMusic2PlayList(context, musicInfo, true);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
		});
		imageview.startAnimation(animationSet);
	}

	public static void playAnimotion_playAllMusic(final Context context, float fromXDelta, float fromYDelta, final ArrayList<MusicInfo> musicInfos, final ImageView imageview) {

		AnimationSet animationSet = new AnimationSet(true);
		Animation translateAnimation = new TranslateAnimation(fromXDelta - (imageview.getWidth() >> 1), fromXDelta - (imageview.getWidth() >> 1), fromYDelta, 800f);
		animationSet.addAnimation(translateAnimation);
		animationSet.setDuration(1000);
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				imageview.setImageResource(R.drawable.music_note);
				imageview.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageview.setVisibility(View.GONE);
				MusicInfoManager.addMusicInfoArray(context, musicInfos, true);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
		});
		imageview.startAnimation(animationSet);
	}

	public static void playAnimotion_insert(final Context context, float fromXDelta, float fromYDelta, final ImageView imageview) {
		AnimationSet animationSet = new AnimationSet(true);
		// 初始化
		Animation translateAnimation = new TranslateAnimation(fromXDelta - (imageview.getWidth() >> 1), 240f, fromYDelta-(imageview.getHeight()>>1), 800f);
		// 设置动画时间
		RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(translateAnimation);
		animationSet.setDuration(1000);
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				imageview.setImageResource(R.drawable.insert_play_note);
				imageview.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageview.setVisibility(View.GONE);
				ToastUtil.showToast(context, "已加入到播放列表");
			}
		});
		imageview.startAnimation(animationSet);
	}
}
