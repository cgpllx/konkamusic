package com.konka.music.util;
import java.io.File;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.util.KLog;

/**
 * @author wangxu
 * @description 判断是否有內外置sd卡，并获取各自路径  （根目录不限于 /mnt 、 /storage）
 * */
public class SDCardUtil
{

	private static SDCardUtil mSdCardUtil = null;
	private String mExternalPath = "";
	private String mInternalPath = "";
	private boolean mHasExternalSD = false;
	private boolean mHasInternalSD = false;

	/**
	 * 是否有外置sd卡
	 * */
	private void checkSDcard() {
		String externalSDPath = Environment.getExternalStorageDirectory().toString();
		String anotherPath = "";
		String root = externalSDPath.split("/")[1];
		File file = new File("/"+root);
		File storages[] = file.listFiles();
		int count = 0;
		for(File tmp: storages) {
			if(tmp.toString().contains("sdcard") && tmp.length() > 0) {
				count++;
				if(!tmp.toString().equals(externalSDPath)) {
					anotherPath = tmp.toString();
				}
			}
		}
		if(count > 1) {
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {  //android 4.0 及以下
//			if(externalSDPath.contains("mnt")) {
				mExternalPath = externalSDPath;
				mInternalPath = anotherPath;
			}else {
				mExternalPath = anotherPath;
				mInternalPath = externalSDPath;
			}
			mHasExternalSD = true;
			mHasInternalSD = true;
		} else if(count==1) {
			mExternalPath = "";
			mHasExternalSD = false;
			mInternalPath = externalSDPath;
			mHasInternalSD = true;
		}
		KLog.v("wangxu", "SDCardUtil->checkSDcard->SD卡="+mExternalPath 
				+ ", 手机存储=" + mInternalPath);
	}


	private SDCardUtil() {
		checkSDcard();
	}

	public static SDCardUtil getInstance() {
		if(mSdCardUtil == null)
			mSdCardUtil = new SDCardUtil();
		return mSdCardUtil;
	}

	/**
	 * 是否有内置sd卡
	 * */
	public boolean hasInternalSD() {
		return mHasInternalSD;
	}

	public boolean hasExternalSD() {
		return mHasExternalSD;
	}

	/**
	 * 获取内置sd卡路径
	 * */
	public String getInternalSDPath() {
		return mInternalPath;
	}
	/**
	 * 获取外置sd卡路径
	 * */
	public String getExternalSDPath() {
		return mExternalPath;
	}
	
	/**
	 * 返回当前sd卡路径
	 * */
	public String getCurrentSDPath() {
		//检测到2个sd卡
		if(hasExternalSD()) {
			return MyPreference.getPref("download_path", getInternalSDPath());
		}else {
			return getInternalSDPath();
		}
	}

	/**
	 * 获取某个目录的容量情况，
	 * @param rootSDPath sd卡根目录
	 * @param cachePath 本地缓存目录
	 * */
	public CapacityUtil getCapacity(String rootSDPath, String cachePath) {
		try {
//			FileSize getFilesize = new FileSize();
			FileUtils fileUtil = new FileUtils();
			StatFs statFs = new StatFs(new File(rootSDPath).getPath());
			double blockSize = statFs.getBlockSize();				// Block的size 
			double blockCount = statFs.getBlockCount();				// 总Block数量 
			double availableBlocks = statFs.getAvailableBlocks();	// 可用的Block数量 

			CapacityUtil capacityUtil = new CapacityUtil();
			capacityUtil.totalSize = blockCount * blockSize;
			capacityUtil.avaliableSize = availableBlocks * blockSize;
			capacityUtil.movieSize = fileUtil.get_size(cachePath);           //本地缓存目录容量
			capacityUtil.otherSize = (blockCount-availableBlocks) * blockSize;  //包含本地缓存

//			KLog.i("wangxu", "SwitchCachePath->getCapacity->" + capacityUtil);
			return capacityUtil;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new CapacityUtil();
	}
	/**
	 * 获取当前选中sd的本地缓存容量
	 * */
	public CapacityUtil getCapacity(String rootSDPath) {
		return getCapacity(rootSDPath, ((MusicApplication)MusicApplication.getAppContext()).getDownloadMusicPath());
	}
	
	public class CapacityUtil{
		public double totalSize = 0;
		public double  movieSize = 0;
		public double otherSize = 0;
		public double avaliableSize = 0;
		
		public CapacityUtil() {}
		
		@Override
		public String toString() {
			return "容量：" + totalSize
					+ ", 本地缓存：" + movieSize
					+ ", 其他：" + otherSize
					+ ", 剩余：" + avaliableSize;
		}
	}
}
