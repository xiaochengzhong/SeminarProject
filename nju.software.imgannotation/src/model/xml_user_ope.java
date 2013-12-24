package model;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class xml_user_ope {

	public xml_user_ope() {
		// do nothing
	}

	// 查看是否有为完成的任务
	/*
	 * List<String>: 返回未完成的任务的ID号 userID: 待检测的用户ID号 webPath: 当前部署到服务器的地址
	 */
	public ArrayList<String> checkTask(String userID, String webPath) {
		ArrayList<String> status = null;
		String filePath = webPath + "/data/users/" + userID + ".xml";

		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前用户还没有任务分配
			return status;
		// deal the xml files
		status = new ArrayList<String>();
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务
			for (short i = 0; i < taskList.size(); ++i) {
				Element task = (Element) taskList.get(i);
				if (task.getChildText("status").contentEquals("0")) // 该任务未被完成
				{
					status.add(task.getAttributeValue("id"));
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return status;
	}

	/**
	 * 得到用户所有的taskset的初步信息
	 */
	public ArrayList<reltsBean> getAllTSLt(String userID) {
		ArrayList<reltsBean> mtsboj = null;
		
		userObj mUserInfo = userOperator.getUserTSList(userID);
		if(mUserInfo == null)
			return mtsboj;
		
		if (mUserInfo.getTSLsist() == null) // 当前用户还没有任务分配
		{
			return mtsboj;
		}

		// deal the xml files
		mtsboj = new ArrayList<reltsBean>();
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(new StringReader(mUserInfo.getTSLsist()));
			Element root = doc.getRootElement(); // 获取根元素
			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务
			xml_taskset_ope mtaskOp = new xml_taskset_ope();
			for (short i = 0; i < taskList.size(); ++i) {
				Element task = (Element) taskList.get(i);
				String relTSID = task.getAttributeValue("id");
				reltsBean mRelTS = mtaskOp.getRelTSInfo(relTSID);
				tasksetobj mtsobj = mtaskOp.getTaskSet(mRelTS.getM_tsID());
				mRelTS.setM_status(task.getChildText("status"));
				mRelTS.setM_tsInfo(mtsobj);
				mtsboj.add(mRelTS);
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return mtsboj;
	}
	
	public ArrayList<reltsBean> getAllTSList(String userID)
	{
		ArrayList<reltsBean> memTSObj = null;
		memTSObj = userOperator.getrelTSList(userID);
		if(memTSObj != null)
		{
			int mSize =  memTSObj.size();
			xml_taskset_ope mtaskOp = new xml_taskset_ope();
			for(short i = 0 ; i < mSize ; ++i)
			{
				reltsBean tempData = mtaskOp.getRelTSInfo(memTSObj.get(i).getM_id());
				tempData.set_canSubmmit(memTSObj.get(i).get_canSubmmit());
				tempData.setM_status(memTSObj.get(i).getM_status());
				tasksetobj tsobj = mtaskOp.getTaskSet(tempData.getM_tsID());
				tempData.setM_tsInfo(tsobj);
				memTSObj.set(i, tempData);
			}
		}
		return memTSObj;
	}

	/**
	 * 通过给定的taskSetID 以及 用户的 ID , 生成用户当前的一份picture列表
	 */
	public ArrayList<picObj> genListTask(String userID, String taskID,
			String webPath) {
		ArrayList<picObj> taskPicList = null;

		String filePath = webPath + "/data/users/" + userID + ".xml";

		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前用户还没有任务分配
			return taskPicList;

		// deal the xml files
		taskPicList = new ArrayList<picObj>();
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务

			for (short i = 0; i < taskList.size(); ++i) {
				Element task = (Element) taskList.get(i);
				if (task.getAttributeValue("id").contentEquals(taskID)) // 得到任务集的列表
				{
					// 由于一个任务集中只有一个complete属性，所以直接获取即可
					List picList = ((Element) task.getChildren("complete").get(
							0)).getChildren("pic");
					for (short picCnt = 0; picCnt < picList.size(); ++picCnt) {
						Element picElement = (Element) picList.get(picCnt);
						picObj newPic = new picObj(
								picElement.getAttributeValue("id"),
								picElement.getText());
						newPic.setStatus(picElement.getAttributeValue("status"));
						newPic.setPSID(picElement.getAttributeValue("psid"));
						taskPicList.add(newPic);
					}
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return taskPicList;
	}

	// 提交任务时，生成用户达额Xml文件
	public boolean genUserXml(String userID, String tasksetID, String webPath,
			String[] picsetID) {
		boolean opSuccess = false;
		String filePath = webPath + "/data/users/" + userID + ".xml";
		File pTSFile = new File(filePath);

		boolean fileExists = true;
		if (!pTSFile.exists()) // 当前用户还没有任务分配
		{
			fileExists = false;
			try { // 生成文件
				if (!pTSFile.createNewFile())
					throw new Exception("Can't create the file");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			Element root = null;
			if (fileExists) {
				sb = new SAXBuilder();
				doc = sb.build(pTSFile);
				root = doc.getRootElement(); // 获取根元素
			} else {
				root = new Element("INFORMATION");
			}

			xml_picture_ope mPicSet = new xml_picture_ope();
			picSetObj mPSData = null;

			Element taskSet = new Element("taskSet");
			taskSet.setAttribute(new Attribute("id", tasksetID));
			Element status = new Element("status");
			status.addContent("0");
			Element currentPic = new Element("current");
			currentPic.setAttribute(new Attribute("id", ""));
			currentPic.setAttribute(new Attribute("psid", ""));
			currentPic.setAttribute(new Attribute("width", ""));
			currentPic.setAttribute(new Attribute("height", ""));
			currentPic.addContent("");
			Element complete = new Element("complete");
			boolean mCurrent = true;
			for (short i = 0; i < picsetID.length; ++i) {
				mPSData = mPicSet.getPicListWithPSID(picsetID[i], webPath);
				ArrayList<picObj> mPicList = mPSData.getPicList();
				for (short j = 0; j < mPicList.size(); ++j) {
					Element newElement = new Element("pic");
					newElement.setAttribute(new Attribute("id", mPicList.get(j)
							.getID()));
					newElement.setAttribute(new Attribute("status", "0"));
					newElement.setAttribute(new Attribute("psid", picsetID[i]));
					newElement.addContent(xmlOperator.dealString(mPicList.get(j).getPath()) + "/" + xmlOperator.dealString(mPicList.get(j).getName()));
					newElement.setAttribute(new Attribute("width", mPicList.get(j).getWidth()));
					newElement.setAttribute(new Attribute("height", mPicList.get(j).getHeight()));
					complete.addContent(newElement);
					
					if(mCurrent)
					{
						currentPic.setAttribute("id", mPicList.get(j)
								.getID());
						currentPic.setAttribute("psid" , picsetID[i]);
						currentPic.setAttribute("width", newElement.getAttributeValue("width"));
						currentPic.setAttribute("height", newElement.getAttributeValue("height"));
						currentPic.addContent(newElement.getText());
						mCurrent = false;
					}
				}
			}
			taskSet.addContent(status);
			taskSet.addContent(currentPic);
			taskSet.addContent(complete);
			root.addContent(taskSet);
			if (fileExists)
				opSuccess = fileOperator.saveXMLFile(filePath, doc);
			else
				opSuccess = fileOperator.saveXMLFile(filePath, root);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //

		return opSuccess;
	}
	
	//get the next picture
	public picObj getNextPic(String userID , String taskSetID , String picsetID , String picID , String webPath)
	{
		picObj nextPic = null;
		String filePath = webPath + "/data/users/" + userID + ".xml";

		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前用户还没有任务分配
			return nextPic;

		// deal the xml files
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务
			
			for (short i = 0; i < taskList.size(); ++i) {
				Element task = (Element) taskList.get(i);
				if (task.getAttributeValue("id").contentEquals(taskSetID)) // 得到任务集的列表
				{
					// 由于一个任务集中只有一个complete属性，所以直接获取即可
					List picList = ((Element) task.getChildren("complete").get(
							0)).getChildren("pic");
					for (short picCnt = 0; picCnt < picList.size(); ++picCnt) {
						if((((Element)picList.get(picCnt)).getAttributeValue("id")).contentEquals(picID) &&
								(((Element)picList.get(picCnt)).getAttributeValue("psid")).contentEquals(picsetID))
						{
							((Element)picList.get(picCnt)).setAttribute("status", "1");
							Element mCurrent = task.getChild("current");
							mCurrent.setAttribute("id", picID);
							mCurrent.setAttribute("psid" , picsetID);
							mCurrent.setAttribute("width" , ((Element)picList.get(picCnt)).getAttributeValue("width"));
							mCurrent.setAttribute("height" , ((Element)picList.get(picCnt)).getAttributeValue("height"));
							mCurrent.removeContent();
							mCurrent.addContent(((Element)picList.get(picCnt)).getText());
							if(picCnt+1 == picList.size())       //to the first
							{
								Element firPic = (Element)picList.get(0);
								mCurrent.setAttribute("id", firPic.getAttributeValue("id"));
								mCurrent.setAttribute("psid" , firPic.getAttributeValue("psid"));
								mCurrent.setAttribute("width" , firPic.getAttributeValue("width"));
								mCurrent.setAttribute("height" , firPic.getAttributeValue("height"));
								mCurrent.removeContent();
								mCurrent.addContent(firPic.getText());
							}
							else
							{
								Element nextElement = (Element)picList.get(picCnt+1);
								nextPic = new picObj(nextElement.getAttributeValue("id"), nextElement.getText());
								nextPic.setPSID(nextElement.getAttributeValue("psid"));
								nextPic.setHeight(nextElement.getAttributeValue("height"));
								nextPic.setWidth(nextElement.getAttributeValue("width"));
							}
							fileOperator.saveXMLFile(filePath, doc);
							break;
						}
					}
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return nextPic;
	}
	
	//get taskset current-pic
	public picObj getCurrentPic(String taskSetID , String userID)
	{
		picObj mData = null;
		
		String filePath =" webPath" + "/data/users/" + userID + ".xml";

		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前用户还没有任务分配
			return mData;

		// deal the xml files
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务
			
			for (short i = 0; i < taskList.size(); ++i) {
				Element task = (Element) taskList.get(i);
				if(task.getAttributeValue("id").contentEquals(taskSetID))
				{
					
					Element currentPic = task.getChild("current");
					mData = new picObj(currentPic.getAttributeValue("id"), currentPic.getText());
					mData.setPSID(currentPic.getAttributeValue("psid"));
					mData.setHeight(currentPic.getAttributeValue("height"));
					mData.setWidth(currentPic.getAttributeValue("width"));
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return mData;	
	}
	
	//get taskset current-pic from the database
	public picObj getCuPicObj(String relTSID , String userID , String picSetID)
	{
		picObj mPicData = null;
		picSetObj mPicSetData = picOperator.getPSInfo(picSetID);
		userObj mUseData = userOperator.getUserTSList(userID);
		if(mUseData == null || mPicSetData == null)
			return mPicData;
		if (mUseData.getTSLsist() == null) // 当前用户还没有任务分配
		{
			return mPicData;
		}
		
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			Element root = null;
			sb = new SAXBuilder();
			
			String currentID = userOperator.getCurrentPic(userID, relTSID);
			doc = sb.build(new StringReader(mPicSetData.getContent()));
			root = doc.getRootElement();
			List mpicList = root.getChild("Pictures").getChildren("pic");
			for (short index = 0; index < mpicList.size(); ++index) {
				if (((Element) mpicList.get(index)).getAttributeValue("id")
						.contentEquals(currentID)) {
					Element mpic = (Element) mpicList.get(index);
					mPicData = new picObj(mpic.getAttributeValue("id"),
							mpic.getText() + mpic.getAttributeValue("name"));
					mPicData.setPSID(picSetID);
					mPicData.setHeight(mpic.getAttributeValue("height"));
					mPicData.setWidth(mpic.getAttributeValue("width"));
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //
		return mPicData;
	}
	
	//get the next picture from the database
	public picObj getNextPicFDB(String userID , String ReltsID , String picSetID , String picID)
	{
		picObj mPicData = null;
		
		picSetObj mPicSetData = picOperator.getPSInfo(picSetID);
		userObj mUseData = userOperator.getUserTSList(userID);
		if(mUseData == null || mPicSetData == null)
			return mPicData;
		if (mUseData.getTSLsist() == "") // 当前用户还没有任务分配
		{
			return mPicData;
		}
		
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			sb = new SAXBuilder();
			doc = sb.build(new StringReader(mPicSetData.getContent()));
			Element root = doc.getRootElement(); // 获取根元素
			List mpicList = root.getChild("Pictures").getChildren("pic");
			
			for (short i = 0; i < mpicList.size(); ++i) {
				Element mPic = (Element) mpicList.get(i);
				if (mPic.getAttributeValue("id").contentEquals(picID)) // 得到匹配的图片
				{
					updateCurrentPic(mUseData, picID, ReltsID);
					if(i+1 == mpicList.size())       //to the first
					{
						Element firPic = (Element)mpicList.get(0);
						userOperator.updateTSofUser(userID, ReltsID , firPic.getAttributeValue("id"));   //更新可提交状态和当前的图像ID
					}
					else
					{
						Element nextElement = (Element)mpicList.get(i+1);
						mPicData = new picObj(nextElement.getAttributeValue("id"), nextElement.getText()+nextElement.getAttributeValue("name"));
						mPicData.setPSID(picSetID);
						mPicData.setHeight(nextElement.getAttributeValue("height"));
						mPicData.setWidth(nextElement.getAttributeValue("width"));
					}
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return mPicData;
	}
	
	
	//submit the data, don't allow the user to change the data
	public boolean submitTSDataToDB(String relTSID , String userID)
	{
		return userOperator.updateTSStatus(userID, relTSID);
//原版XML操作
//		boolean opSuccess = false;
//		userObj mUserInfo = userOperator.getUserTSList(userID);
//		
//		if (mUserInfo.getTSLsist() == null) // 当前用户还没有任务分配
//			return opSuccess;
//
//		// deal the xml files
//		SAXBuilder sb = new SAXBuilder();
//		Document doc = null;
//		try {
//			doc = sb.build(new StringReader(mUserInfo.getTSLsist()));
//			Element root = doc.getRootElement(); // 获取根元素
//			List taskList = root.getChildren("taskSet");// 得到用户已经配分配的用户任务
//			
//			for (short i = 0; i < taskList.size(); ++i) {
//				Element task = (Element) taskList.get(i);
//				if(task.getAttributeValue("id").contentEquals(relTSID))
//				{
//					Element status = task.getChild("status");
//					status.removeContent();
//					status.addContent("1");
//					mUserInfo.setTSLsist(stringOperator.docToString(doc));
//					opSuccess =  userOperator.updateUserTSList(mUserInfo);
//					break;
//				}
//			}
//		} catch (JDOMException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // 构造文档对象
//		return opSuccess;
	}
	
	// 提交任务时，生成用户的任务记录
	public boolean addNewTaskToUser(String userID, String reltasksetID) 
	{
		return userOperator.insertTSFUser(userID, reltasksetID);
	}

	//update the user current picID
	public boolean updateCurrentPic(userObj mUserData , String newPicID , String relTSID)
	{
		return userOperator.updateCurentPic(mUserData.getID(), relTSID, newPicID);
	}

	//用于管理员浏览用户图像数据库之用
	public picObj admingetNextPicFDB(String userID , String ReltsID , String picSetID , String picID)
	{
		picObj mPicData = null;
		
		picSetObj mPicSetData = picOperator.getPSInfo(picSetID);
		userObj mUseData = userOperator.getUserTSList(userID);
		if(mUseData == null || mPicSetData == null)
			return mPicData;
		if (mUseData.getTSLsist() == "") // 当前用户还没有任务分配
		{
			return mPicData;
		}
		
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			sb = new SAXBuilder();
			doc = sb.build(new StringReader(mPicSetData.getContent()));
			Element root = doc.getRootElement(); // 获取根元素
			List mpicList = root.getChild("Pictures").getChildren("pic");
			
			for (short i = 0; i < mpicList.size(); ++i) {
				Element mPic = (Element) mpicList.get(i);
				if (mPic.getAttributeValue("id").contentEquals(picID)) // 得到匹配的图片
				{
					if(i+1 == mpicList.size())       //to the first
					{
						Element firPic = (Element)mpicList.get(0);
						mPicData = new picObj(firPic.getAttributeValue("id"), firPic.getText()+firPic.getAttributeValue("name"));
						mPicData.setPSID(picSetID);
						mPicData.setHeight(firPic.getAttributeValue("height"));
						mPicData.setWidth(firPic.getAttributeValue("width"));
					}
					else
					{
						Element nextElement = (Element)mpicList.get(i+1);
						mPicData = new picObj(nextElement.getAttributeValue("id"), nextElement.getText()+nextElement.getAttributeValue("name"));
						mPicData.setPSID(picSetID);
						mPicData.setHeight(nextElement.getAttributeValue("height"));
						mPicData.setWidth(nextElement.getAttributeValue("width"));
					}
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return mPicData;
	}

	//用于管理员浏览用户图像数据库之用
	public picObj adminGetFirstPic(String relTSID ,  String picSetID)
	{
		picObj mPicData = null;
		picSetObj mPicSetData = picOperator.getPSInfo(picSetID);
		if(mPicSetData == null)
			return mPicData;

		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			Element root = null;
			sb = new SAXBuilder();

			doc = sb.build(new StringReader(mPicSetData.getContent()));
			root = doc.getRootElement();
			List mpicList = root.getChild("Pictures").getChildren("pic");

			Element mpic = (Element)mpicList.get(0);
			mPicData = new picObj(mpic.getAttributeValue("id"), mpic.getText()+mpic.getAttributeValue("name"));
			mPicData.setPSID(picSetID);
			mPicData.setHeight(mpic.getAttributeValue("height"));
			mPicData.setWidth(mpic.getAttributeValue("width"));

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //
		return mPicData;
	}
	
	//create a new person-set to the database
	public boolean addNewPerSetToDB(String tittle , String description , String[] userIDList , String[] adminInfo)
	{
		String userListContent = "<persions>";
		for(short i = 0 ; i < userIDList.length ; ++i)
		{
			userListContent = userListContent + "<persion id=\\\""+ userIDList[i] + "\\\"></persion>";
		}
		userListContent += "</persions>";
		return userOperator.addNewPerSetToDB(tittle, description, userListContent, adminInfo);
	}
	
	//get all the person-set list from the database
	public ArrayList<usersetBean> getAllPerSetFDB()
	{
		return userOperator.getAllPersonList();
	}
	
	//get the person-set information from the database
	public usersetBean getPerSerInfoWithID(String persetID)
	{
		usersetBean muserset = userOperator.getPerSerInfoWithID(persetID);
		
		if(muserset == null)
			return muserset;
		
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			Element root = null;
			sb = new SAXBuilder();

			doc = sb.build(new StringReader(muserset.getContent()));
			root = doc.getRootElement();
			List mpersonList = root.getChildren("persion");
			String[] userIDList = new String[mpersonList.size()];
			for(short i = 0 ; i < mpersonList.size(); ++i)
			{
				userIDList[i] = ((Element)mpersonList.get(i)).getAttributeValue("id");
			}
			muserset.setUserList(userOperator.getUserName(userIDList));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //
		return muserset;
	}

	//get the leave person who is not in the particularly person-set
	public ArrayList<userObj> getleaveusersWithID(String persetID)
	{
		ArrayList<userObj> mData= null;
		usersetBean muserset = userOperator.getPerSerInfoWithID(persetID);
		
		if(muserset == null)
			return mData;
		
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = null;
		Document doc = null;
		try {
			Element root = null;
			sb = new SAXBuilder();

			doc = sb.build(new StringReader(muserset.getContent()));
			root = doc.getRootElement();
			List mpersonList = root.getChildren("persion");
			String[] userIDList = new String[mpersonList.size()];
			for(short i = 0 ; i < mpersonList.size(); ++i)
			{
				userIDList[i] = ((Element)mpersonList.get(i)).getAttributeValue("id");
			}
			mData = userOperator.getLeaveUseName(userIDList);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //
		return mData;
	}
	
	public ArrayList<userObj> getleaveusersuserset(usersetBean muserset)
	{
		ArrayList<userObj> mData= null;
		
		ArrayList<userObj> mcurrent = muserset.getUserList();
		String[] userIDList = new String[mcurrent.size()];
		for(short i = 0 ; i < mcurrent.size(); ++i)
		{
			userIDList[i] = mcurrent.get(i).getID();
		}
		mData = userOperator.getLeaveUseName(userIDList);
		return mData;
	}

	//update the personList with the person-set id
	//only can change the tittle/description/content
	public boolean updatePerSetFDB(String tittle , String description, String[] userIDList , String persetID)
	{
		String userListContent = "<persions>";
		for(short i = 0 ; i < userIDList.length ; ++i)
		{
			userListContent = userListContent + "<persion id=\\\""+ userIDList[i] + "\\\"></persion>";
		}
		userListContent += "</persions>";
		return userOperator.updatePerSetFDB(tittle, description, userListContent, persetID);
	}
}
