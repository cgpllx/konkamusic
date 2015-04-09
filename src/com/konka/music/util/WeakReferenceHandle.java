package com.konka.music.util;

import java.lang.ref.WeakReference;

import android.os.Handler;

public class WeakReferenceHandle {
	WeakReference<Handler> mHandleWeakReference = null;

	public WeakReferenceHandle(Handler handle) {
		mHandleWeakReference = new WeakReference<Handler>(handle);
	}

}
