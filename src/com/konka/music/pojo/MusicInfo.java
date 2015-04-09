package com.konka.music.pojo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.kubeiwu.commontool.db.utils.A.KIsNotProperty;
import com.kubeiwu.commontool.db.utils.A.KProperty;

public class MusicInfo implements Parcelable   {

	/** 在MedieStore存储的主键 */
	// @KProperty(column = BaseColumns._ID)
	private long id;

	/** 不带扩展名的文件名 */
	private String title;

	/** 文件名 */
	@KProperty(unique = true)
	private String displayName="";

	/** 专辑名，一般为文件夹名 */
	private String album;

	/** 艺术家 */
	private String artist;

	/** 文件的绝对路径 */
	private String data;

	/** 文件大小，单位为 byte */
	private long size;

	/** 时长 */
	private long duration;

	/** 歌曲标题索引，用来搜索、排序用 */
	private String titleKey;

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/** 艺术家名称索引，用来搜索、排序用 */
	private String artistKey;

	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}

	private int history = 0;// 播放历史
	private int favourite = 0;// 我喜欢
	private long download_id = 0;// 下载管理
	private int myClassify_id = -1;// 我的分类
	private String singer;// 歌手
	private String musicname;// 歌曲名称
	private int palylist=0;

	public int getPalylist() {
		return palylist;
	}

	public void setPalylist(int palylist) {
		this.palylist = palylist;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getMusicname() {
		return musicname;
	}

	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}

	public int getMyClassify_id() {
		return myClassify_id;
	}

	public void setMyClassify_id(int myClassify_id) {
		this.myClassify_id = myClassify_id;
	}

	private String lrc_url;

	public String getLrc_url() {
		return lrc_url;
	}

	public void setLrc_url(String lrc_url) {
		this.lrc_url = lrc_url;
	}

	public MusicInfo() {

	}

	public int getHistory() {
		return history;
	}

	public void setHistory(int history) {
		this.history = history;
	}

	public int getFavourite() {
		return favourite;
	}

	public void setFavourite(int favourite) {
		this.favourite = favourite;
	}

	public long getDownload_id() {
		return download_id;
	}

	public void setDownload_id(long download_id) {
		this.download_id = download_id;
	}

	public String getArtistKey() {
		return artistKey;
	}

	public String getTitleKey() {
		return titleKey;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MusicInfo) {
			MusicInfo object = (MusicInfo) o;
			return object.getId() == this.id;
		} else {
			return super.equals(o);
		}
	}

	@Override
	public String toString() {
		return "song_id:" + id + ",song_title:" + title;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
//		this.titleKey = StringHelper.getPingYin(title);
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
//		this.artistKey = StringHelper.getPingYin(artist);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 写数据进行保存
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putLong("id", id);
		bundle.putString("title", title);
		bundle.putString("displayName", displayName);
		bundle.putString("album", album);
		bundle.putString("artist", artist);
		bundle.putString("data", data);
		bundle.putLong("size", size);
		bundle.putLong("duration", duration);
		bundle.putString("singer", singer);
		bundle.putString("musicname", musicname);
		bundle.putInt("favourite", favourite);

		dest.writeBundle(bundle);
	}

	// 用来创建自定义的Parcelable的对象
	@KIsNotProperty
	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
		@Override
		public MusicInfo createFromParcel(Parcel in) {
			return new MusicInfo(in);
		}

		@Override
		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
	};

	// 读数据进行恢复
	private MusicInfo(Parcel in) {
		Bundle bundle = in.readBundle();
		id = bundle.getLong("id");
		title = bundle.getString("title");
		displayName = bundle.getString("displayName");
		album = bundle.getString("album");
		artist = bundle.getString("artist");
		data = bundle.getString("data");
		size = bundle.getLong("size");
		duration = bundle.getLong("duration");

		singer = bundle.getString("singer");
		musicname = bundle.getString("musicname");
		favourite = bundle.getInt("favourite");
	}

}
