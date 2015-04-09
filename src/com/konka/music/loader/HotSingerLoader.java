package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;

import com.konka.music.pojo.Singer;
import com.konka.music.util.Net;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class HotSingerLoader extends BaseLoader<ArrayList<Singer>> {

	private String url;

	public HotSingerLoader(Context context, String url) {
		super(context);
		this.url = url;
	}

	@Override
	public ArrayList<Singer> loadInBackground() {
		return Net.getHotSingerArray(url);

	}

}
