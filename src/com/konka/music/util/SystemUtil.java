package com.konka.music.util;

public class SystemUtil {
	public static boolean DEBUG = false;


	public static void print(String text) {
		if (DEBUG) {
			System.out.println(text);
		}
	}
}
