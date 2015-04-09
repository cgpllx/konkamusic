package com.konka.music.loader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import android.content.Context;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.util.Assist;
import com.konka.music.util.RequestUtil;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class SearchMusicLoader extends BaseLoader<ArrayList<MusicInfo>> {
	private String searchkey;

	public SearchMusicLoader(Context context, String searchkey) {
		super(context);
		this.searchkey = searchkey;
	}
	final LinkedBlockingDeque<ArrayList<MusicInfo>> queue = new LinkedBlockingDeque<ArrayList<MusicInfo>>();

	@Override
	public ArrayList<MusicInfo> loadInBackground() {
		try {
			queue.clear();
			try {
				searchkey = URLEncoder.encode(searchkey, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String url = String.format(Assist.SEARCHMUSIC, searchkey);
			System.out.println("搜索地址="+url);
			RequestUtil.handleMusicInfosFromNet(url, queue);
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
