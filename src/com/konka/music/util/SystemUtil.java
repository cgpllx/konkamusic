package com.konka.music.util;

public class SystemUtil {
	public static boolean DEBUG = false;


	public static void print(Object object) {
		if (DEBUG) {
			System.out.println(object);
		}
	}
}
