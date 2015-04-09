package com.konka.music.loader;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import android.content.Context;

import com.konka.music.pojo.AllLableInfos;
import com.konka.music.pojo.SmallLabelInfo;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.RequestUtil;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class SmallLabelInfoLoader extends BaseLoader<ArrayList<SmallLabelInfo>> {
	private int bigLabelId;

	public SmallLabelInfoLoader(Context context, int bigLabelId) {
		super(context);
		this.bigLabelId = bigLabelId;
	}

	LinkedBlockingDeque<AllLableInfos> queue = new LinkedBlockingDeque<AllLableInfos>();

	// http://music.konkacloud.cn/Client/?getTags
	@Override
	public ArrayList<SmallLabelInfo> loadInBackground() {
		String strWhere = "bigLabelid = " + bigLabelId;
		ArrayList<SmallLabelInfo> smallLabelInfos = MusicApplication.mKCommonToolDb.findAllByWhere(SmallLabelInfo.class, strWhere);
		try {
			if (ArrayUtils.isEmpty(smallLabelInfos)) {
				RequestUtil.handleLableFromNet2Db(queue);
				queue.take();
				smallLabelInfos = MusicApplication.mKCommonToolDb.findAllByWhere(SmallLabelInfo.class, strWhere);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smallLabelInfos;
	}
}
