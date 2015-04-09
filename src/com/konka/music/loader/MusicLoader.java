package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class MusicLoader extends BaseLoader<ArrayList<MusicInfo>> {

	public interface LoaderType {
		int HISTORY = 0;
		int FAVOURITE = 1;
		int DOWNLOAD = 2;
	}

	private int loaderType;

	public MusicLoader(Context context, int loaderType) {
		super(context);
		this.loaderType = loaderType;
	}

	@Override
	public ArrayList<MusicInfo> loadInBackground() {
		String where = "";
		switch (loaderType) {
		case LoaderType.HISTORY:
			where = "history = 1";
			break;
		case LoaderType.FAVOURITE:
			where = "favourite != 0";
			break;
		case LoaderType.DOWNLOAD:
			where = "download != 0";
			break;
		}
		return MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, where);
	}

}
