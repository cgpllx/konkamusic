package com.konka.music.lrc;

public class LyricObject {
	public int getBegintime() {
		return begintime;
	}
	public void setBegintime(int begintime) {
		this.begintime = begintime;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public int getTimeline() {
		return timeline;
	}
	public void setTimeline(int timeline) {
		this.timeline = timeline;
	}
	public String getLrc() {
		return lrc;
	}
	public void setLrc(String lrc) {
		this.lrc = lrc;
	}
	private int begintime; // Begin time
	private int endtime; // End time
	private int timeline; // Time of single line
	private String lrc; // Lyrics of single line
}
