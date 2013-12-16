package com.imagest.action;

import com.imagest.service.MatchImageService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class LoginAction extends ActionSupport {
    
	String username;
	String password;
	MatchImageService matchImageService;
	String failInfo="";

	public String login() throws Exception {
		if(ActionContext.getContext().getSession().containsKey("username")&&ActionContext.getContext().getSession().get("username")!="")
			return "success";
    	int result = matchImageService.login(username, password);
    	if(result == 0){
    		ActionContext.getContext().getSession().put("username", username);
    		return "success";
    	}
    	else {
    		failInfo = "µÇÂ¼Ê§°Ü";
    		return "failure";
    	}
    }
    
    public String logOut() throws Exception {
    	if(ActionContext.getContext().getSession().containsKey("username"))
    		ActionContext.getContext().getSession().remove("username");
    	  failInfo = "";
    	return "success";
    }
    
	public void setUsername(String username) {
		this.username = username + "@software.nju.edu.cn";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMatchImageService(MatchImageService matchImageService) {
		this.matchImageService = matchImageService;
	}

	public String getFailInfo() {
		return failInfo;
	}
 	
}
