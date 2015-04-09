package com.konka.music.pojo;

import java.util.ArrayList;

public class AllLableInfos {
	private ArrayList<BigLabelInfo> bigLabelInfos;// = new ArrayList<BigLabelInfo>();
	private ArrayList<SmallLabelInfo> smallLabelInfos;// = new ArrayList<SmallLabelInfo>();

	public ArrayList<BigLabelInfo> getBigLabelInfos() {
		return bigLabelInfos;
	}

	public void setBigLabelInfos(ArrayList<BigLabelInfo> bigLabelInfos) {
		this.bigLabelInfos = bigLabelInfos;
	}

	public ArrayList<SmallLabelInfo> getSmallLabelInfos() {
		return smallLabelInfos;
	}

	public void setSmallLabelInfos(ArrayList<SmallLabelInfo> smallLabelInfos) {
		this.smallLabelInfos = smallLabelInfos;
	}
}
