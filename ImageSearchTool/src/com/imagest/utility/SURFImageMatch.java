package com.imagest.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.struts2.ServletActionContext;

import surfalgorithm.ImageMatch;
import surfalgorithm.OpenSurf;

import com.imagest.domain.Image;
import com.mathworks.toolbox.javabuilder.MWException;

public class SURFImageMatch {
	
	double tresh = 0.001;
	double matchDistance = 0.1;
	double matchRate = 0.0;
	
	public SURFImageMatch() {
		
	}
	
	public SURFImageMatch(double matchDistance, double matchRate) {
		this.matchDistance = matchDistance;
		this.matchRate = matchRate;
	}
	
	public SURFImageMatch(double tresh, double matchDistance, double matchRate) {
		this.tresh = tresh;
		this.matchDistance = matchDistance;
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
	
	public Image matchImage(String imagePath1, String imagePath2) throws IOException {
		try {
			OpenSurf openSurf = new OpenSurf();
			ImageMatch imageMatch = new ImageMatch();
			
			Object[] args = new Object[2];
			args[0] = ServletActionContext.getServletContext().getRealPath("/" + imagePath1);
			args[1] = tresh;
			Object[] returnMatrix = new Object[1];
			openSurf.OpenSurf(returnMatrix, args);
			
			Object[] result = new Object[1];
			Object[] parameters = new Object[3];			
			parameters[0] = returnMatrix[0];
			parameters[2] = matchDistance;
			
//			long startMili = System.currentTimeMillis();
			args[0] = ServletActionContext.getServletContext().getRealPath("/" + imagePath2);
			Object[] returnMatrix2 = new Object[1];
			openSurf.OpenSurf(returnMatrix2, args);
				
			parameters[1] = returnMatrix2[0];
				
			imageMatch.ImageMatch(result, parameters);
			Double matchRatio = Double.parseDouble(result[0].toString());
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
