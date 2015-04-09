package com.konka.music.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * @description 文件辅助类，获取目录容量，格式化字节
 * */
public class FileUtils {
	
	/***
	 * 
	 * 功能：创建目录
	 * 作者：Ouyangweize
	 * 时间：2014-1-9上午8:51:29
	 * @param path
	 * @return
	 */
	public static String createDirs(String dirPath)
	{
		//konka-wangxu-20141209-根据默认路径，创建文件目录-start
		String rootDir = SDCardUtil.getInstance().getCurrentSDPath(); // 创建一个文件夹对象，赋值为外部存储器的目录
//		File sdcardDir = new File(rootDir);
		String path = /*sdcardDir.getPath()*/ rootDir + dirPath;  //得到一个路径，内容是sdcard的文件夹路径和名字
		File path1 = new File(path);
		if (!path1.exists()) {
			//若不存在，创建目录，可以在应用启动的时候创建
			path1.mkdirs();
		}
//		KLog.v("wangxu", "rootDir=" + rootDir + ", path:"+path);
		return path1.getPath() + "/";
	}
	
	/**
	 * 格式化容量
	 * */
	public static String formatFileSize(double fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if(fileS < 0) {
			fileSizeString = "0B";
		}else if (fileS < 1024) {
			fileSizeString = df.format(fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format(fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format(fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format(fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	/**
	 * 获取文件大小
	 * */
	public long getFileSizes(File f) throws Exception{
		long s=0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s= fis.available();
		} else {
			f.createNewFile();
//			System.out.println("文件不存在");
		}
		return s;
	}
	
	/**
	 * 递归方法获取文件大小
	 * */
	public double getFileSize(File f)throws Exception
	{
		double size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
	
	/**
	 * 递归求取目录文件个数
	 * */
	public long getlist(File f){
		long size = 0;
		File flist[] = f.listFiles();
		size=flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
	
	/**
	 * 取得文件大小
	 * */
	public String getsize(String path){
		double l = 0;
		try{
			File ff = new File(path);
			if (ff.isDirectory()) { //如果路径是文件夹的时候
				l = getFileSize(ff);
//				System.out.println(path + "目录的大小为：" + formatFileSize(l));
			} else {
				l = getFileSizes(ff);
//				System.out.println(path + "文件的大小为：" + formatFileSize(l));
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return formatFileSize(l);
	}
	
	/**
	 * 取得文件大小
	 * */
	public double get_size(String path){
		double l = 0;
		try{
			File ff = new File(path);
			if (ff.isDirectory()) { //如果路径是文件夹的时候
				l = getFileSize(ff);
			} else {
				l = getFileSizes(ff);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return l;
	}
}
