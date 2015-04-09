package com.konka.music.util;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.konka.music.wedget.MusicApplication;

public class MyPreference {
	
//	private static Context mContext;
	private static String prefFlag = "org.konka.kplayer.vlc" ;
	private static SharedPreferences preferences = MusicApplication.getAppContext().getSharedPreferences(prefFlag, 0);
	private static Editor editor = preferences.edit();
	
//	private  MyPreference(Context context){
//		mContext = context;
//		preferences = mContext.getSharedPreferences(prefFlag, mContext.MODE_PRIVATE);
//		editor = preferences.edit();
//	}
	
	
	public static void putPref(String key, Boolean value){
		try{
			editor.putBoolean(key, value);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void putPref(String key, String value){
		try{
			editor.putString(key, value);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void putPref(String key, Float value){
		try{
			editor.putFloat(key, value);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void putPref(String key, Integer value){
		try{
			editor.putInt(key, value);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@SuppressLint("NewApi")
	public static void putPref(String key, Set<String> values){
		try{
			editor.putStringSet(key, values);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void putPref(String key, Long value){
		try{
			editor.putLong(key, value);
			editor.commit();
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static Boolean getPref(String key, Boolean defValue){
		try{
			return preferences.getBoolean(key, defValue);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return defValue;
	}
	
	public static Integer getPref(String key, Integer defValue){
		try{
			return preferences.getInt(key, defValue);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return defValue;
	}
	
	public static Float getPref(String key, Float defValue){
		try{
			return preferences.getFloat(key, defValue);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return defValue;
	}
	
	public static Long getPref(String key, Long defValue){
		try{
			return preferences.getLong(key, defValue);
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return defValue;
	}
	
	public static String getPref(String key, String defValue){
		try{
			return preferences.getString(key, defValue);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return defValue;
	}
	
	@SuppressLint("NewApi")
	public static Set<String> getPref(String key, Set<String> defValues){
		try{
			return preferences.getStringSet(key, defValues);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return defValues;
	}
	
}
