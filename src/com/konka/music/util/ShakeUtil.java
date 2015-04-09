package com.konka.music.util;

public class ShakeUtil {

	private static long lastTime;
    public static boolean isAllow() {
        long time = System.currentTimeMillis();
        long timeD = time - lastTime;
        if ( lastTime == 0 || timeD > 800) {  
            lastTime = time;  
            return true;  
        }  
        return false;  
    }
}
