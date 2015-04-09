package com.konka.music.wedget;

import android.content.Context;

import com.konka.music.lrc.LrcLoader;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.service.ShakeService;
import com.konka.music.util.FileUtils;
import com.konka.music.util.ParaSetting;
import com.kubeiwu.commontool.KApplication;
import com.kubeiwu.commontool.db.KCommonToolDb;
import com.kubeiwu.commontool.db.KCommonToolDb.DaoConfig;
import com.kubeiwu.crash.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MusicApplication extends KApplication {

	private static MusicApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		 CrashHandler.getInstance().init(this); // Useful for debug.
		ParaSetting.ParaUtil.initPrar(this);
		if (ParaSetting.WAVE_CUT_SONG.value) {
			ShakeService.actionStart(this); // 启动摇一摇切歌
		}
		instance = this;
		LrcLoader.init();
		initdb();
		initImageLoader(this);

	}


	public static KCommonToolDb mKCommonToolDb;

	private void initdb() {
		mKCommonToolDb = KCommonToolDb.create(new DaoConfig().setContext(this).setDbVersion(1));
	}

	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)//
				.threadPriority(Thread.NORM_PRIORITY - 2)//
				.denyCacheImageMultipleSizesInMemory()//
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//
				.tasksProcessingOrder(QueueProcessingType.FIFO)//
				.discCacheSize(1024 * 1024 * 1024).discCacheFileCount(3000)//
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
		ImageLoader.getInstance().init(config);
	}

	public static Context getAppContext() {
		return instance;
	}

	// 资源不足时候调用
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	// 当后台程序已经终止资源还匮乏时会调用这个方法。好的应用程序一般会在这个方法里面释放一些不必要的资源来应付当后台程序已经终止，前台应用程序内存还不够时的情况。
	// onLowMemory

	/**
	 * 获取下载音乐路径，为 /mnt/sdcard(0)/konka/music/
	 * */
	public String getDownloadMusicPath() {
		return FileUtils.createDirs("/konka/music/");
	}

	/**
	 * 获取下载歌词路径，为 /mnt/sdcard(0)/konka/lrc/
	 * */
	public String getDownloadLrcPath() {
		return FileUtils.createDirs("/konka/lrc/");
	}

	/**
	 * 获取下载app路径，为 /mnt/sdcard(0)/konka/app/
	 * */
	public String getDownloadAppPath() {
		return FileUtils.createDirs("/konka/app/");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mKCommonToolDb.close();
	}
}
