package com.konka.music.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class MusicImage {
	public static Bitmap getImageFromMp3File(String path) {
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(path);
		byte[] artByteArray = metaRetriever.getEmbeddedPicture();
		if (artByteArray != null) {
			Bitmap artBitmap = BitmapFactory.decodeByteArray(artByteArray, 0, artByteArray.length);
			metaRetriever.release();
			return artBitmap;
		}
		return null;
	}


}
