package com.imagest.utility;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.struts2.ServletActionContext;

import com.imagest.domain.Image;
import com.mathworks.toolbox.javabuilder.MWException;

import correlationalgorithm.CorrelationMatch;

public class CorrelationImageMatch {
	
	double matchRate = 0.0;
	
	public CorrelationImageMatch() {
		
	}
	
	public CorrelationImageMatch(double matchRate) {
		this.matchRate = matchRate;
	}
	
	public ArrayList<Image> match(String imagePath, ArrayList<String> testImagePath) throws Exception {
		ArrayList<Image> images = new ArrayList<Image>();
		for(String path : testImagePath){
			Image image = matchImage(imagePath, path);
			if(image != null)
				images.add(image);
		}
		Collections.sort(images);
		return images;
	}
	
	public Image matchImage(String imagePath1, String imagePath2) {
		try {
			CorrelationMatch correlationMatch = new CorrelationMatch();
			String[] paths = new String[2];
			Object[] r = new Object[1];
			paths[0] = ServletActionContext.getServletContext().getRealPath("/" + imagePath1);
			paths[1] = ServletActionContext.getServletContext().getRealPath("/" + imagePath2);
//			long startMili = System.currentTimeMillis();
			correlationMatch.correlationMatch(r, paths);
			double matchRatio = Double.parseDouble(r[0].toString());
//			long endMili = System.currentTimeMillis();
//			double matchTime = (endMili - startMili) / 1000.0;
			if(matchRate <= matchRatio) {
				Image matchImage = new Image();
				matchImage.setPath(imagePath2);
				matchImage.setMatchRate(matchRatio);
//				matchImage.setMatchTime(matchTime);
				if(imagePath2.substring(imagePath2.lastIndexOf('/') + 1, imagePath2.lastIndexOf('.')).startsWith(imagePath1.substring(imagePath1.lastIndexOf('/') + 1, imagePath1.lastIndexOf('.'))))
					matchImage.setCorrespond(true);
				return matchImage;
			}
			else if(imagePath2.substring(imagePath2.lastIndexOf('/') + 1, imagePath2.lastIndexOf('.')).startsWith(imagePath1.substring(imagePath1.lastIndexOf('/') + 1, imagePath1.lastIndexOf('.')))) {
				Image matchImage = new Image();
				matchImage.setPath(imagePath2);
				matchImage.setMatchRate(matchRatio);
//				matchImage.setMatchTime(matchTime);
				matchImage.setCorrespond(true);
				matchImage.setMatch(false);
				return matchImage;
			}
		} catch (MWException e) {
			e.printStackTrace();
		}
		return null;
	}
}
