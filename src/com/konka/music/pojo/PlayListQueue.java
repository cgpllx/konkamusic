package com.konka.music.pojo;

import android.provider.BaseColumns;

import com.kubeiwu.commontool.db.utils.A.KProperty;

public class PlayListQueue {
	@KProperty(column = BaseColumns._ID)
	private int id;
	private byte[] bytes;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PlayListQueue() {
		super();
	}

}
