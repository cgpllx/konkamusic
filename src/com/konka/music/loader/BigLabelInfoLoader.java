package com.konka.music.loader;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import android.content.Context;

import com.konka.music.pojo.AllLableInfos;
import com.konka.music.pojo.BigLabelInfo;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.RequestUtil;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class BigLabelInfoLoader extends BaseLoader<ArrayList<BigLabelInfo>> {

	public BigLabelInfoLoader(Context context) {
		super(context);
	}

	LinkedBlockingDeque<AllLableInfos> queue = new LinkedBlockingDeque<AllLableInfos>();

	@Override
	public ArrayList<BigLabelInfo> loadInBackground() {
		String where = "_id not in (1 ,9)";
		ArrayList<BigLabelInfo> bigLabelInfos = MusicApplication.mKCommonToolDb.findAllByWhere(BigLabelInfo.class, where);
		try {
			if (ArrayUtils.isEmpty(bigLabelInfos)) {
				RequestUtil.handleLableFromNet2Db(queue);
				queue.take();
				bigLabelInfos = MusicApplication.mKCommonToolDb.findAllByWhere(BigLabelInfo.class, where);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bigLabelInfos;
	}
}
