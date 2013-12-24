package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class stringOperator {

	public static ArrayList<String> parseJsonToArray(String data) {
		ArrayList<String> list = new ArrayList<String>();
		if (null == data || data.length() == 0) {
			return null;
		}
		if (data.length() < 4) {
			return null;
		}
		String temp = data.substring(2, data.length() - 2);
		temp = temp.replaceAll("\\'", "");
		String splitResult[] = temp.split(",");
		for (int i = 0; i < splitResult.length; i++) {
			list.add(splitResult[i].substring(splitResult[i].indexOf(":") + 1));
		}
		return list;
	}
	
	//format the time(YYYYMMDDHHMMSS)
	public static String timeFormat() {
		DateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date currentTime = new Date();
		return nameFormat.format(currentTime);
	}

	public static String timeFormatT()
	{
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		return timeFormat.format(currentTime);
	}
	
	public static String docToString(Document mDoc)
	{
		 Format format = Format.getRawFormat();  
	     format.setEncoding("UTF-8"); 
	     XMLOutputter xmlout = new XMLOutputter(format);
	     return xmlout.outputString(mDoc);
	}
	
	public static String elementToString(Element mEle)
	{
		 Format format = Format.getRawFormat();  
	     format.setEncoding("UTF-8"); 
	     XMLOutputter xmlout = new XMLOutputter(format);
	     return xmlout.outputString(mEle);
	}
	
	//去除在XML中存在的CDATA标签符
	public static String cleanString(String content)
	{
		content = content.replace("<![CDATA[", "");
		content = content.replace("]]>", "");
		return content;
	}
}
