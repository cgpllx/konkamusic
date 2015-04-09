package com.konka.music.player;

import com.konka.music.R;

public class MusicPalyModel {
//0代表顺序播放	1代表单曲循环   2代表随机播放  3代表列表循环播放 
	/**
	 * 顺序播放
	 */
	public static final int SEQUENCE = R.drawable.ic_player_mode_sequence;
	/**
	 * 单曲循环播放
	 */
	public static final int SINGLE_LOOP = R.drawable.ic_player_mode_single;
	/**
	 * 随机播放
	 */
	public static final int RANDOM = R.drawable.ic_player_mode_random;//
	/**
	 *列表循环 
	 */
	public static final int REPEAT = R.drawable.ic_player_mode_all;//
}
/**
 * 播放模式<br>
 * 0代表单曲循环，1代表列表循环，2代表顺序播放，3代表随机播放
 */
//private int[] ii = { R.drawable.button_playmode_sequential, R.drawable.button_playmode_repeat_single, R.drawable.button_playmode_shuffle, R.drawable.button_playmode_repeat };