package com.konka.music.musicexception;

public class DownLoadException extends Exception {

	private static final long serialVersionUID = 9223270617772123345L;

	public DownLoadException() {
		super();
	}

	public DownLoadException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DownLoadException(String detailMessage) {
		super(detailMessage);
	}

	public DownLoadException(Throwable throwable) {
		super(throwable);
	}

}
