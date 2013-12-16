package com.imagest.action;

import java.util.ArrayList;

import com.imagest.domain.Image;
import com.imagest.service.MatchImageService;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class MatchAction extends ActionSupport{
	
    private MatchImageService matchImageService;
    private ArrayList<Image> images;
    private String executor;
    private String taskId;
    private String queryPath;
    
    public String fetchQueryInfo() throws Exception {
    	images = matchImageService.viewImageDescription(executor, taskId, queryPath);
		return "success";
    }

    public void setMatchImageService(MatchImageService matchImageService) {
		this.matchImageService = matchImageService;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setQueryPath(String queryPath) {
		this.queryPath = queryPath;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

     
}
