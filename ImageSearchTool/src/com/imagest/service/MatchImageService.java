package com.imagest.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.imagest.domain.Image;
import com.imagest.domain.ImageNode;
import com.imagest.domain.Task;
import com.imagest.utility.CorrelationImageMatch;
import com.imagest.utility.MD5AndBase64;
import com.imagest.utility.SIFTImageMatch;
import com.imagest.utility.SURFImageMatch;

public class MatchImageService {
	
	public void matchImage(ArrayList<Task> taskList) throws Exception {
		for(Task task : taskList)
			matchImage(task);
	}
	
	public void matchImage(String jsonTaskList) throws Exception {
		matchImage(taskListJsonToArray(jsonTaskList));
	}
	
	public void matchImage(Task task) throws Exception {
		String tmpPath = ServletActionContext.getServletContext().getRealPath("/tasks/" + task.getExecutor() + "/" + task.getTaskId() + ".tmp");
		String realPath = ServletActionContext.getServletContext().getRealPath("/tasks/" + task.getExecutor() + "/" + task.getTaskId() + ".xml");
		File file = new File(tmpPath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if(!file.exists())
			file.createNewFile();
		Writer w = new FileWriter(file);
		OutputFormat opf = OutputFormat.createPrettyPrint();
		opf.setEncoding("GB2312");
		XMLWriter xw = new XMLWriter(w, opf);
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("task");
		
		Element taskInfo = root.addElement("info");
		taskInfo.addElement("id").setText(task.getTaskId());
		taskInfo.addElement("name").setText(task.getName());
		taskInfo.addElement("executor").setText(task.getExecutor());
		taskInfo.addElement("queryNumber").setText(task.getQueryNodes().size() + "");
		taskInfo.addElement("dataSetNumber").setText(task.getDataSetNodes().size() + "");
		Element features = taskInfo.addElement("features");
		for(String i : task.getFeature())
			features.addElement("feature").setText(i);
		taskInfo.addElement("similarity").setText(task.getSimilarity());
		Element parameters = taskInfo.addElement("parameters");
		for(String i : task.getParameters().keySet())
			parameters.addElement(i).setText(task.getParameters().get(i) + "");
		
		int totalMatchAndCorrespondNumber = 0;
		int totalMatchNumber = 0;
		int totalCorrespondNumber = 0;
		double totalPrecision = 0.0;
		double totalRecall = 0.0;
		Element taskResult = root.addElement("result");
		ArrayList<String> dataSetAddressList = new ArrayList<String>();
		for(int i = 0; i < task.getDataSetNodes().size(); i++)
			dataSetAddressList.add(task.getDataSetNodes().get(i).getUrlAddr());
		for(ImageNode image : task.getQueryNodes()) {
			ArrayList<Image> results;
			if(task.getFeature().get(0).startsWith("sift")) {
//				results = new SIFTImageMatch(task.getParameters().get("matchDistance"), task.getParameters().get("matchRate")).match(image.getUrlAddr(), dataSetAddressList);
				results = new CorrelationImageMatch(task.getParameters().get("matchRate")).match(image.getUrlAddr(), dataSetAddressList);
			}
			else if(task.getFeature().get(0).startsWith("surf")) {
//				results = new SURFImageMatch(task.getParameters().get("matchDistance"), task.getParameters().get("matchRate")).match(image.getUrlAddr(), dataSetAddressList);
				results = new CorrelationImageMatch(task.getParameters().get("matchRate")).match(image.getUrlAddr(), dataSetAddressList);
			}
			else {
				results = new CorrelationImageMatch(task.getParameters().get("matchRate")).match(image.getUrlAddr(), dataSetAddressList);
			}
			Element queryImage = taskResult.addElement("queryImage");
			queryImage.addAttribute("name", image.getName());
			queryImage.addAttribute("path", image.getUrlAddr());
			int matchAndCorrespondNumber = 0;
			int matchNumber = 0;
			int correspondNumber = 0;
			double precision = 0.0;
			double recall = 0.0;
			for(int i = 0; i < results.size(); i++) {
				Image result = results.get(i);
				if((double)matchNumber == task.getParameters().get("top-n"))    result.setMatch(false);
				if(result.isMatch() || result.isCorrespond()) {
					Element dataSetImage = queryImage.addElement("dataSetImage");
					dataSetImage.addAttribute("path", result.getPath());
					dataSetImage.addElement("match").setText(result.isMatch() + "");
					if(result.isMatch() && result.isCorrespond()){	
						matchAndCorrespondNumber++;
						matchNumber++;
						correspondNumber++;
						dataSetImage.addElement("rank").setText(i + 1 + "");
					}
					else if(result.isMatch()){    
						matchNumber++;
						dataSetImage.addElement("rank").setText(i + 1 + "");
					}
					else    correspondNumber++;
					dataSetImage.addElement("correspond").setText(result.isCorrespond() + "");
					dataSetImage.addElement("matchRate").setText(result.getMatchRate() + "");
				}
			}
			if(matchNumber != 0)    precision = (matchAndCorrespondNumber + 0.0) / matchNumber;
			if(correspondNumber != 0)    recall = (matchAndCorrespondNumber + 0.0) / correspondNumber;
			queryImage.addElement("matchAndCorrespondNumber").setText(matchAndCorrespondNumber + "");
			queryImage.addElement("matchNumber").setText(matchNumber + "");
			queryImage.addElement("correspondNumber").setText(correspondNumber + "");
			queryImage.addElement("precision").setText(precision + "");
			queryImage.addElement("recall").setText(recall + "");
			totalMatchAndCorrespondNumber += matchAndCorrespondNumber;
			totalMatchNumber += matchNumber;
			totalCorrespondNumber += correspondNumber;
		}
		if(totalMatchNumber != 0)    totalPrecision = (totalMatchAndCorrespondNumber + 0.0) / totalMatchNumber;
		if(totalCorrespondNumber != 0)    totalRecall = (totalMatchAndCorrespondNumber + 0.0) / totalCorrespondNumber;
		Element evaluation = root.addElement("evaluation");
		evaluation.addElement("totalPrecision").setText(totalPrecision + "");
		evaluation.addElement("totalRecall").setText(totalRecall + "");
		
		xw.write(doc);		
		xw.close();
		w.close();
		file.renameTo(new File(realPath));
	}
	
	public ArrayList<String> getTaskListByUser(String executor) {
		ArrayList<String> taskList = new ArrayList<String>();
		String realpath = ServletActionContext.getServletContext().getRealPath("/tasks/" + executor);
		File directory = new File(realpath);
		if(directory.exists()) {
			File[] files = directory.listFiles();
			for(int i = 0; i < files.length; i++) {
				if(files[i].getName().endsWith(".xml"))
					taskList.add(files[i].getName().replace(".xml", ""));
			}
		}
		return taskList;
	}
	
	@SuppressWarnings("rawtypes")
	public Task getTaskInfo(String executor, String taskId) {
		Task task = new Task();
		task.setTaskId(taskId);
		task.setExecutor(executor);
		String realPath = ServletActionContext.getServletContext().getRealPath("/tasks/" + executor + "/" + taskId + ".xml");
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new FileReader(new File(realPath)));
			Element root = document.getRootElement();
			Element basicInfo = root.element("info");
			task.setName(basicInfo.elementText("name"));
			ArrayList<String> features = new ArrayList<String>();
			for (Iterator i = basicInfo.element("features").elementIterator("feature"); i.hasNext();) {
				features.add(((Element)i.next()).getText());
			}
			task.setFeature(features);
			task.setSimilarity(basicInfo.elementText("similarity"));
			Map<String, Double> parameters = new HashMap<String, Double>();
			for (Iterator i = basicInfo.element("parameters").elementIterator(); i.hasNext();) {
				parameters.put("matchRate", Double.valueOf(((Element)i.next()).getText()));
				parameters.put("topN", Double.valueOf(((Element)i.next()).getText()));
				parameters.put("matchDistance", Double.valueOf(((Element)i.next()).getText()));
			}
			task.setParameters(parameters);
			
			Element evaluation = root.element("evaluation");
			task.setPrecision(Double.valueOf(evaluation.elementText("totalPrecision")));
			task.setRecall(Double.valueOf(evaluation.elementText("totalRecall")));
			
			ArrayList<ImageNode> queryNodes = new ArrayList<ImageNode>();
			Element result = root.element("result");
			for (Iterator i = result.elementIterator(); i.hasNext();) {
				ImageNode imageNode = new ImageNode();
				Element queryImage = (Element)i.next();
				imageNode.setName(queryImage.attributeValue("name"));
				imageNode.setUrlAddr(queryImage.attributeValue("path"));
				imageNode.setPrecision(Double.valueOf(queryImage.elementText("precision")));
				imageNode.setRecall(Double.valueOf(queryImage.elementText("recall")));
				queryNodes.add(imageNode);
			}
			task.setQueryNodes(queryNodes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Image> viewImageDescription(String executor, String taskId, String queryPath) {
		ArrayList<Image> imageList = new ArrayList<Image>();
		String realPath = ServletActionContext.getServletContext().getRealPath("/tasks/" + executor + "/" + taskId + ".xml");
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new FileReader(new File(realPath)));
			Iterator queryImageList = document.getRootElement().element("result").elementIterator();
			while(queryImageList.hasNext()) {
				Element queryImage = (Element) queryImageList.next();
				if(queryImage.attributeValue("path").equals(queryPath)) {
					for(Iterator i = queryImage.elementIterator("dataSetImage"); i.hasNext();) {
						Element dataSetElement = (Element) i.next();
						Image dataSetImage = new Image();
						dataSetImage.setPath(dataSetElement.attributeValue("path"));
						dataSetImage.setMatch(Boolean.parseBoolean(dataSetElement.elementText("match")));
						dataSetImage.setCorrespond(Boolean.parseBoolean(dataSetElement.elementText("correspond")));
						dataSetImage.setMatchRate(Double.parseDouble(dataSetElement.elementText("matchRate")));
						imageList.add(dataSetImage);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageList;
	}
    
	public ArrayList<ImageNode> getImageNodes(int type) {
		String path = "images/Query";
		if(type == 1)
			path = "images/DataSet";
		String filePath = ServletActionContext.getServletContext().getRealPath("/" + path);
		ArrayList<ImageNode> imageNodes = new ArrayList<ImageNode>();
		id = 0;
		traverseFile(filePath, imageNodes, 0, path);
		return imageNodes;
	}
	
	public void traverseFile(String filePath, ArrayList<ImageNode> imageNodes, int pId, String urlAddr) { 
	    File dir = new File(filePath); 
	    File[] files = dir.listFiles(); 
	    
	    if (files == null) 
	    	return;
	    for (int i = 0; i < files.length; i++) {
	       	id++;
	       	ImageNode imageNode = new ImageNode();
	       	imageNode.setId(id);
	       	imageNode.setpId(pId);
	       	imageNode.setName(files[i].getName());
	       	imageNode.setUrlAddr(urlAddr + "/" + files[i].getName());
	       	imageNodes.add(imageNode);
	       	if (files[i].isDirectory()) {
	       		traverseFile(files[i].getAbsolutePath(), imageNodes, id, urlAddr + "/" + files[i].getName()); 
	       	}
	    } 
	}
	
	public ArrayList<Task> taskListJsonToArray(String jsonTasklist) {
		ArrayList<Task> taskList = new ArrayList<Task>();  
		JSONArray taskArray = JSONArray.fromObject(jsonTasklist);
	  	for (int i = 0; i < taskArray.size(); i++) {
	  		JSONObject taskJsonObject = JSONObject.fromObject(taskArray.get(i));
	  		Task task = new Task();
	  		task.setTaskId(taskJsonObject.getString("id"));
	  		task.setExecutor(taskJsonObject.getString("executor"));
	  		task.setName(taskJsonObject.getString("name"));
	  		JSONArray featureArray = JSONArray.fromObject(taskJsonObject.getString("feature"));
	  		if(featureArray.size() == 0)
	  			task.getFeature().add("correlation");
	  		else 
	  			for(int k = 0; k < featureArray.size(); k++) {
		  			task.getFeature().add(featureArray.getString(k));
		  		}
	  		task.setSimilarity(taskJsonObject.getString("similarity"));
	  		 
	  		String topN = taskJsonObject.getString("topN");
	  		if(!topN.equals(""))
	  			task.getParameters().put("top-n", Double.parseDouble(topN));
	  		String matchRate = taskJsonObject.getString("matchRate");
	 		if(!matchRate.equals(""))
	 			task.getParameters().put("matchRate", Double.parseDouble(matchRate));
	 		String matchDistance = taskJsonObject.getString("matchDistance");
	 		if(!matchDistance.equals(""))
	 			task.getParameters().put("matchDistance", Double.parseDouble(matchDistance));
	  		 
	  		JSONArray nodelist = JSONArray.fromObject(taskJsonObject.getString("queryNodes"));
	  		for (int j = 0; j < nodelist.size(); j++) {
	  			JSONObject nodeJsonObject = JSONObject.fromObject(nodelist.get(j));
				ImageNode node =(ImageNode)JSONObject.toBean(nodeJsonObject,ImageNode.class);
				task.getQueryNodes().add(node);
			}
	  		nodelist = JSONArray.fromObject(taskJsonObject.getString("dataSetNodes"));
	  		for (int j = 0; j < nodelist.size(); j++) {
	  			JSONObject nodeJsonObject = JSONObject.fromObject(nodelist.get(j));
				ImageNode node = (ImageNode)JSONObject.toBean(nodeJsonObject,ImageNode.class);
				task.getDataSetNodes().add(node);
			}
	  		taskList.add(task);
	  	}
	 	return taskList;
    }
	   
	public int login(String username, String password) {
		if(username==null||password==null)
			return 1;
		String encodedPwd = MD5AndBase64.encryption(password);
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(encodedPwd);
		encodedPwd = m.replaceAll("");
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><auth><email>" + username + "</email><password>" + encodedPwd + "</password></auth>";
		String urlStr = "http://software.nju.edu.cn:8000/api/auth/user/";
		return doPost(urlStr, data);
	}
	   
	private int doPost(String url, String data){
		int result = 1;
		try {
			HttpPost httppost = new HttpPost(url);
			HttpClient httpClient = new DefaultHttpClient();
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("fromdata", data));
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse rs = httpClient.execute(httppost);
			String info = EntityUtils.toString(rs.getEntity());
	        if(info.contains(">0<")) {
	        	result = 0;
	        }
	        else {
	            result = 1;
	        }			 
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	static int id = 0;
}
