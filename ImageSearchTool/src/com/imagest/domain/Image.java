package com.imagest.domain;

public class Image implements Comparable<Image> {
	
	String path;
	double matchRate;
	double matchTime;
	boolean correspond = false;
	boolean match = true;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public double getMatchRate() {
		return matchRate;
	}
	
	public void setMatchRate(double matchRate) {
		this.matchRate = matchRate;
	}

	public double getMatchTime() {
		return matchTime;
	}
	
	public void setMatchTime(double matchTime) {
		this.matchTime = matchTime;
	}
	
	public boolean isCorrespond() {
		return correspond;
	}
	
	public void setCorrespond(boolean correspond) {
		this.correspond = correspond;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	@Override
	public int compareTo(Image comparedImage) {
		if(this.matchRate < comparedImage.matchRate)
			return 1;
		else if(this.matchRate > comparedImage.matchRate)
			return -1;
		else
			return 0;
	}
}
