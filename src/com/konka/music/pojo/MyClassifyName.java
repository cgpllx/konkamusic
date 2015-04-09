package com.konka.music.pojo;

import android.provider.BaseColumns;

import com.kubeiwu.commontool.db.utils.A.KProperty;

public class MyClassifyName {
	@KProperty(column = BaseColumns._ID)
	private int id = -1;
	@KProperty(unique = true)
	private String classify_name = "默认收藏";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MyClassifyName(String classify_name) {
		super();
		this.classify_name = classify_name;
	}

	public String getClassify_name() {
		return classify_name;
	}

	public MyClassifyName(int id, String classify_name) {
		super();
		this.id = id;
		this.classify_name = classify_name;
	}

	public void setClassify_name(String classify_name) {
		this.classify_name = classify_name;
	}

	public MyClassifyName() {
		super();
	}

}
