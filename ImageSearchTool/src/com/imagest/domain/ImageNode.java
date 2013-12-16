package com.imagest.domain;

public class ImageNode {
	int id;
	int pId;
	String name;
	String urlAddr;
	double precision;
	double recall;
   
	public ImageNode() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getpId() {
		return pId;
	}
	
	public void setpId(int pId) {
		this.pId = pId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrlAddr() {
		return urlAddr;
	}
	
	public void setUrlAddr(String urlAddr) {
		this.urlAddr = urlAddr;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}
	
}
