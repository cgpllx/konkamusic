package com.konka.music.util;

import android.graphics.Bitmap;

import com.konka.music.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public interface Assist {

	// 广播接受的action
	String BROADCAST_ACTION_PLAY = "com.konka.music.BROADCAST_ACTION_PLAY";
	String BROADCAST_ACTION_NEXT = "com.konka.music.BROADCAST_ACTION_NEXT";
	String BROADCAST_ACTION_PREV = "com.konka.music.BROADCAST_ACTION_PREV";
	String BROADCAST_ACTION_PAUSE = "com.konka.music.BROADCAST_ACTION_PAUSE";
	String BROADCAST_ACTION_STOP = "com.konka.music.BROADCAST_ACTION_STOP";
	String BROADCAST_ACTION_EXIT  = "com.konka.music.BROADCAST_ACTION_EXIT";
	String BROADCAST_ACTION_INTRODUCTIONDATA = "com.konka.music.BROADCAST_ACTION_INTRODUCTIONDATA";

	String BROADCAST_ACTION_ALARM = "com.konka.music.BROADCAST_ACTION_ALARM";// wx
	// 广播intent传值的key
	String KEY_PLAYPROGRESS = "key_playprogress";
	String KEY_BUFFERINGUPDATE_PROGRESS = "key_bufferingupdate_progress";
	// String KEY_MUSICINFO="KEY_MUSICINFO";

	String BROADCAST_ACTION_UPDATA_PLAYBACKPROGRESS = "com.konka.music.BROADCAST_ACTION_UPDATA_PLAYBACKPROGRESS";
	String BROADCAST_ACTION_UPDATE_BUFFERING_PROGRESS = "com.konka.music.BROADCAST_ACTION_UPDATE_BUFFERING_PROGRESS";

	// 服务绑定成功后的广播
	String BROADCAST_ACTION_SERVICEBINDCOMPLETE1 = "com.konka.music.BROADCAST_ACTION_SERVICEBINDCOMPLETE";
	String[] actions = { BROADCAST_ACTION_UPDATA_PLAYBACKPROGRESS, // 播放进度
			BROADCAST_ACTION_UPDATE_BUFFERING_PROGRESS,// 缓冲进度
			BROADCAST_ACTION_STOP,// 停止
			BROADCAST_ACTION_PLAY,// 播放
			BROADCAST_ACTION_NEXT, // 下一曲
			BROADCAST_ACTION_PAUSE, // 暂停
			BROADCAST_ACTION_PREV, // 上一曲
			BROADCAST_ACTION_INTRODUCTIONDATA, // 传入数据
	};
	// 锁屏广播的action
	String SCREEN_ON = "android.intent.action.SCREEN_ON";
	String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
	String[] lackBroadcastActions = { SCREEN_ON, SCREEN_OFF };
	// 服务的intent action
	String SERVICE_ACTION_NEXT = "com.konka.music.SERVICE_ACTION_NEXT";
	String SERVICE_ACTION_PREV = "com.konka.music.SERVICE_ACTION_PREV";
	String SERVICE_ACTION_PLAY_OR_PAUSE = "com.konka.music.SERVICE_ACTION_PLAY_OR_PAUSE";
	String SERVICE_ACTION_STOP = "com.konka.music.SERVICE_ACTION_STOP";
	String SERVICE_ACTION_CLOSE = "com.konka.music.SERVICE_ACTION_CLOSE";
	// String SERVICE_ACTION_PLAY_MUSIC = "com.konka.music.SERVICE_ACTION_PLAY_MUSIC";
	String SERVICE_ACTION_Add_MUSIC = "com.konka.music.SERVICE_ACTION_Add_MUSIC";
	String SERVICE_ACTION_ADD_MUSICINFO_ARRAY = "com.konka.music.SERVICE_ACTION_ADDMUSICINFOARRAY";
	String SERVICE_ACTION_PALYMUSIC_OF_POSITION_THE_LIST = "com.konka.music.SERVICE_ACTION_PALYMUSIC_OF_POSITION_THE_LIST";
	String SERVICE_ACTION_CLEARPALYLIST = "com.konka.music.SERVICE_ACTION_CLEARPALYLIST";

	// 服务的intent传值的key
	String KEY_MUSICINFO = "key_musicInfo";
	String KEY_PLAYTHIS = "key_playthis";
	String KEY_MUSICINFO_LIST = "key_musicinfo_list";
	String KEY_PLAYLIST_INDEX = "key_playlist_index";
	// 播放进度更新频率
	long RATE = 1000;

	// 请求数据地址
	String BIGLABLE_URL = "http://music.konkacloud.cn/Client/?getTags";
	// http://music.konkacloud.cn/Client/?music_Tag.html&tag_id=37
	String SMALLLABLE_URL = "http://music.konkacloud.cn/Client/?music_Tag.html&tag_id=%1$d";
	String HOTSINGER_URL = "http://music.konkacloud.cn/Client/?HotSinger.html&area=%1$s";
	String SINGER_URL = "http://music.konkacloud.cn/Client/?getSinger.html&area=%1$s&category=%2$s";
	String SINGERSMUSIC = "http://music.konkacloud.cn/Client/?music_Singer.html&singer_id=%1$d";
	String SEARCHMUSIC = "http://music.konkacloud.cn/Client/?music_Search.html&key=%1$s";
	String UPGRADE_URL = "http://music.konkacloud.cn/Client/?getVersion.html"; // 升级
	String FEEDBACK_URL = "http://music.konkacloud.cn/Client/?feedback.html"; // 反馈
	// String TOPLIST = "http://music.konkacloud.cn/Client/?getRank.html";
	// http://music.konkacloud.cn/Client/?music_rank.html&rank_id=2
	// String TOPLIST_MUSIC = "http://music.konkacloud.cn/Client/?music_rank.html&rank_id=2";
	int TOPLIST_ID = 1 << 5;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()// ap_default_banner.jpg
			.showImageOnLoading(R.drawable.new_classification_default_album_icon)//
			.showImageForEmptyUri(R.drawable.new_classification_default_album_icon)// ic_empty
			.cacheInMemory(true)//
			.cacheOnDisc(true)//
			.resetViewBeforeLoading(true)//
			.considerExifParams(true)//
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//
			.bitmapConfig(Bitmap.Config.RGB_565)//
			.build();//

	// loader ids
	int MUSICLIBRARY_LOADER_ID = 1;
	int BIGLABEL_LOADER_ID = 2;
	int SMALLLABLE_LOADER_ID = 3;
	int SINGERCHILE_LOADER_ID = 4;
	int SINGERSEX_LOADER_ID = 5;
	int DOWNLOADING_LOADER_ID = 6;
	int DOWNLOADFINISH_LOADER_ID = 7;
	int SEARCH_LOADER_ID = 8;
	int CLASSIFYLIST_LOADER_ID = 9;
	int MAIN_LOCALMUSIC_LOADER_ID = 11;

}
