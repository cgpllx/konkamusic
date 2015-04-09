package com.konka.music.loader;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import android.content.Context;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.util.RequestUtil;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class SmallLablePlayListLoader extends BaseLoader<ArrayList<MusicInfo>> {
	private String url;

	public SmallLablePlayListLoader(Context context, String url) {
		super(context);
		this.url = url;
	}

	final LinkedBlockingDeque<ArrayList<MusicInfo>> queue = new LinkedBlockingDeque<ArrayList<MusicInfo>>();

	@Override
	public ArrayList<MusicInfo> loadInBackground() {
		queue.clear();
		try {
			RequestUtil.handleMusicInfosFromNet(url, queue);
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
