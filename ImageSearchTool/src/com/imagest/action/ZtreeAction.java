package com.imagest.action;

import java.util.ArrayList;

import com.imagest.domain.ImageNode;
import com.imagest.service.MatchImageService;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ZtreeAction extends ActionSupport {
  
	private ArrayList<ImageNode> imageNodes = new ArrayList<ImageNode>();
    private MatchImageService matchImageService;
    private int type;
    
	public String zTreeNodes() throws Exception {
		imageNodes.clear();
		imageNodes = matchImageService.getImageNodes(type);
		return "success";
	}

	public ArrayList<ImageNode> getImageNodes() {
		return imageNodes;
	}

	public void setMatchImageService(MatchImageService matchImageService) {
		this.matchImageService = matchImageService;
	}

	public void setType(int type) {
		this.type = type;
	}    
}
