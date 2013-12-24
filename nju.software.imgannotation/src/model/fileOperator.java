package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import cn.gh.util.FileUtil;


public class fileOperator {
	
	//直接保存文件到指定文件名和目录
	public static boolean saveFileWithStream(String filePath , String fileData)
	{
		boolean opOk = false;
		File tempFile = new File(filePath);
		if(!tempFile.exists())
		{
			try {
				if(!tempFile.createNewFile())
					throw new Exception("Can't create the file");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return opOk;
			}
		}
		
		//将数据流写入文件（文件存在的话，直接覆盖）
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(fileData);
			opOk =  true;
		} catch (Exception e) {
			e.printStackTrace();
			opOk = false;
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return opOk;
	}

	//去除在XML中存在的CDATA标签符
	public static boolean cleanFile(String filePath)
	{
		StringBuffer bs = new StringBuffer();
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				bs.append(line + "\n");
			}
			br.close();
			String str = bs.toString();
			str = str.replace("<![CDATA[", "");
			str = str.replace("]]>", "");
			return fileOperator.saveFileWithStream(filePath, str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//保存xml文件
	public static boolean saveXMLFile(String filePath , Element element)
	{
		XMLOutputter outputter = new XMLOutputter();
		FileOutputStream out;
		try {
			out = new FileOutputStream(filePath);
			outputter.output(element, out);
			out.close();
			cleanFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean saveXMLFile(String filePath , Document doc)
	{
		XMLOutputter outputter = new XMLOutputter();
		FileOutputStream out;
		try {
			out = new FileOutputStream(filePath);
			outputter.output(doc, out);
			out.close();
			cleanFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//get image width and height
	public static int[] getImgSize(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int size[] = {-1,-1};
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            size[0] = src.getWidth(); // width
            size[1] = src.getHeight();   //height
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
	
	//get the files which only pictures(to judge the file is a picture or not)
	public static boolean isPicture(String fileext)
	{
		Pattern mPattern = Pattern.compile("(bmp|gif|jpeg|jpg|png|jpg|tiff|psd|svg|pcx|dxf|wmf|emf|lic|eps|tga)");
		Matcher mMatcher = mPattern.matcher(fileext);
		return mMatcher.find();
	}
	
	//generate the picture-list xml
	public static ArrayList<picObj> getPictureList(String webPath , ArrayList<String> folderList)
	{
		ArrayList<picObj> mPicListInfo = new ArrayList<picObj>();
		for(short i = 0 ; i < folderList.size() ; ++i)
		{
			getFile(webPath+folderList.get(i), folderList.get(i), mPicListInfo);
		}
		return mPicListInfo;
	}
	
	//use to view the files
	public static void getFile(String absolutepath , String filePath , ArrayList<picObj> mPicListInfo)
	{
		File currentFile = new File(absolutepath);
		if(currentFile.isFile())
		{
			picObj tempPic = new picObj("", filePath.replace(currentFile.getName(), ""));
			int[] mtempSize = fileOperator.getImgSize(currentFile);
			tempPic.setWidth(String.valueOf(mtempSize[0]));
			tempPic.setHeight(String.valueOf(mtempSize[1]));
			tempPic.setName(currentFile.getName());
			mPicListInfo.add(tempPic);
			return;
		}
		
		File[] fileList = currentFile.listFiles();
		
		for(short i=0; i < fileList.length ; ++i)
		{
			if(fileList[i].isDirectory())
			{
				getFile(fileList[i].getAbsolutePath() , filePath + fileList[i].getName() + "/" , mPicListInfo);
			}
			else if(fileOperator.isPicture(FileUtil.getFileExt(fileList[i].getName())))
			{
				picObj tempPic = new picObj("", filePath);
				int[] mtempSize = fileOperator.getImgSize(fileList[i]);
				tempPic.setWidth(String.valueOf(mtempSize[0]));
				tempPic.setHeight(String.valueOf(mtempSize[1]));
				tempPic.setName(fileList[i].getName());
				mPicListInfo.add(tempPic);
			}
		}
	}
}
