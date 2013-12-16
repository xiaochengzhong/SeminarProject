package com.imagest.action;

import java.util.List;

import com.imagest.service.MatchImageService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class MatchedTaskAction extends ActionSupport {
	
	MatchImageService matchImageService;
	List<String> matchedTasks;
	String executor;
	
	public String fetchMatchedTasks() throws Exception {
		
		String executor = ActionContext.getContext().getSession().get("username").toString();
		if(executor.equals(executor))
			matchedTasks = matchImageService.getTaskListByUser(executor);
		return "success";
	}
	
	public List<String> getMatchedTasks() {
		return matchedTasks;
	}
	
	public void setMatchImageService(MatchImageService matchImageService) {
		this.matchImageService = matchImageService;
	}
	
	public void setExecutor(String executor) {
		this.executor = executor;
	}	
	
}
