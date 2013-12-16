package com.imagest.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task {
	
	String taskId;
	String name;
	String executor;
	ArrayList<ImageNode> queryNodes = new ArrayList<ImageNode>();
	ArrayList<ImageNode> dataSetNodes = new ArrayList<ImageNode>();
	ArrayList<String> feature = new ArrayList<String>();
	String similarity;
	Map<String, Double> parameters = new HashMap<String, Double>();
	double precision;
	double recall;

	public Task() {
		similarity = "Euclidena";
		parameters.put("top-n", 20.0);
		parameters.put("matchRate", 0.0);
		parameters.put("matchDistance", 0.1);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<ImageNode> getQueryNodes() {
		return queryNodes;
	}
	
	public void setQueryNodes(ArrayList<ImageNode> queryNodes) {
		this.queryNodes = queryNodes;
	}
	
	public ArrayList<ImageNode> getDataSetNodes() {
		return dataSetNodes;
	}
	
	public void setDataSetNodes(ArrayList<ImageNode> dataSetNodes) {
		this.dataSetNodes = dataSetNodes;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public ArrayList<String> getFeature() {
		return feature;
	}

	public void setFeature(ArrayList<String> feature) {
		this.feature = feature;
	}

	public String getSimilarity() {
		return similarity;
	}

	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	public Map<String, Double> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Double> parameters) {
		this.parameters = parameters;
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
