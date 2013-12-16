package com.imagest.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.struts2.ServletActionContext;

import siftalgorithm.Match;
import siftalgorithm.SIFT;

import com.imagest.domain.Image;
import com.mathworks.toolbox.javabuilder.MWException;


public class SIFTImageMatch {

	double distRatio = 0.1;
	double matchRate = 0.0;
	
	public SIFTImageMatch() {
		
	}
	
	public SIFTImageMatch(double distRatio, double matchRate) {
		this.distRatio = distRatio;
		this.matchRate = matchRate;
	}
	
	public ArrayList<Image> match(String imagePath, ArrayList<String> testImagePath) throws Exception {
		ArrayList<Image> images = new ArrayList<Image>();
		for(String path : testImagePath) {
			Image image = matchImage(imagePath, path);
			if(image != null)
				images.add(image);
		}
		Collections.sort(images);
		return images;
	}
	
	public Image matchImage(String imagePath1, String imagePath2) throws IOException {
		try {
			SIFT sift = new SIFT();
			Match match = new Match();
			
			Object[] currentKeypoints = new Object[4];
			String[] currentImagePath = new String[1];
			currentImagePath[0] = ServletActionContext.getServletContext().getRealPath("/" + imagePath1);
			sift.sift(currentKeypoints, currentImagePath);
			Object descriptors = currentKeypoints[1];
			double keyPointsNumber = Double.parseDouble(currentKeypoints[3].toString());
			
			Object[] parameters = new Object[3];
			Object[] result = new Object[1];

//			long startMili = System.currentTimeMillis();
			Object[] testKeypoints = new Object[3];
			String[] testImagePath = new String[1];
			testImagePath[0] = ServletActionContext.getServletContext().getRealPath("/" + imagePath2);
			sift.sift(testKeypoints, testImagePath);
			Object testDescriptors = testKeypoints[1];
			
			parameters[0] = descriptors;
			parameters[1] = testDescriptors;
			parameters[2] = distRatio;
			match.match(result, parameters);
			int matchNumber = Integer.parseInt(result[0].toString());
			double matchRatio = matchNumber / keyPointsNumber;
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
