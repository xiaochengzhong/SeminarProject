package com.imagest.action;

import com.imagest.domain.Task;
import com.imagest.service.MatchImageService;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class TaskAction extends ActionSupport {
      
	String tasks;
    String result = "success";
    MatchImageService matchImageService;
    Task task;
    String executor;
    String taskId;
      
    public String goTask() throws Exception {
    	matchImageService.matchImage(tasks);
    	return "success";
    }

    public String fetchTaskInfo() throws Exception {
    	task = matchImageService.getTaskInfo(executor, taskId);
    	return "success";
    }  
    
	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

	public String getResult() {
		return result;
	}

	public void setMatchImageService(MatchImageService matchImageService) {
		this.matchImageService = matchImageService;
	}

	public Task getTask() {
		return task;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
