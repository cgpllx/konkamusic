package com.konka.music.pojo;

import android.provider.BaseColumns;

import com.kubeiwu.commontool.db.utils.A.KProperty;

/**
 * 乐库小类别
 * 
 * @author Administrator
 * 
 */
public class SmallLabelInfo {
	@KProperty(column = BaseColumns._ID)
	private int id;
	private String smallLabelName;
	private int bigLabelid;
	private String imageurl;

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public int getId() {
		return id;
	}

	public SmallLabelInfo(int id, String smallLabelName, int bigLabelid, String imageurl) {
		super();
		this.id = id;
		this.smallLabelName = smallLabelName;
		this.bigLabelid = bigLabelid;
		this.imageurl = imageurl;
	}

	public SmallLabelInfo() {
		super();
	}

	public int getBigLabelid() {
		return bigLabelid;
	}

	public void setBigLabelid(int bigLabelid) {
		this.bigLabelid = bigLabelid;
	}

	@Override
	public String toString() {
		return "SmallLabelInfo [id=" + id + ", smallLabelName=" + smallLabelName + "]";
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSmallLabelName() {
		return smallLabelName;
	}

	public void setSmallLabelName(String smallLabelName) {
		this.smallLabelName = smallLabelName;
	}

}
