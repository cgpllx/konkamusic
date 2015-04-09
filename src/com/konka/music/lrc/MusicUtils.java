/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.konka.music.lrc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class MusicUtils {

	private static final String TAG = MusicUtils.class.getSimpleName();

	private static final String LYRICS_DIR = "/konka/music/lyrics";

	public static String getLyricDir() {
		File file = Environment.getExternalStorageDirectory();
		if (file == null) {
			return null;
		}
		File f = new File(file.getAbsolutePath() + LYRICS_DIR);
		if (!f.exists()) {
			f.mkdirs();
		}

		return f.getAbsolutePath();
	}

	public static String createLyricName(String song, String artist) {
		StringBuilder sb = new StringBuilder();
		sb.append(song);
		if (artist != null && artist.length() > 0 && !MediaStore.UNKNOWN_STRING.equals(artist)) {
			sb.append("_");
			sb.append(artist);
		}
		sb.append(".lrc");
		return sb.toString();
	}

	public static String getLyricFile(String song, String artist) {
		String lyricDir = getLyricDir();
		if (lyricDir == null) {
			return null;
		}
		return lyricDir + "/" + createLyricName(song, artist);
	}

	public static boolean saveFile(String filePath, InputStream inputStream) throws IOException {
		boolean result = false;
		if (filePath != null && inputStream != null) {
			Log.d(TAG, "filePath:" + filePath);
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				byte[] buf = new byte[1024];
				int size = 0;
				while ((size = inputStream.read(buf, 0, 1024)) != -1) {
					fos.write(buf, 0, size);
				}

				fos.flush();
				fos.close();
				inputStream.close();
				result = true;
			}
		}
		return result;
	}

	public static boolean saveFile(String filePath, String str) throws IOException {
		boolean result = false;
		if (filePath != null && str != null) {
			Log.d(TAG, "filePath:" + filePath);
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				fos.write(str.getBytes("gb18030"));
				fos.flush();
				fos.close();
				result = true;
			}
		}
		return result;
	}
}
