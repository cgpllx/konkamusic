package com.konka.music.pojo;

import android.provider.BaseColumns;

import com.kubeiwu.commontool.db.utils.A.KProperty;

/**
 * 乐库大类别
 * 
 * @author Administrator
 * 
 */
public class BigLabelInfo   {
	@KProperty(column = BaseColumns._ID)
	private int id;
	private String bigLabelName;

	public int getId() {
		return id;
	}

	public BigLabelInfo() {
		super();
	}

	@Override
	public String toString() {
		return "BigLabelInfo [id=" + id + ", bigLabelName=" + bigLabelName + "]";
	}

	public BigLabelInfo(int id, String bigLabelName) {
		super();
		this.id = id;
		this.bigLabelName = bigLabelName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBigLabelName() {
		return bigLabelName;
	}

	public void setBigLabelName(String bigLabelName) {
		this.bigLabelName = bigLabelName;
	}

 
}
