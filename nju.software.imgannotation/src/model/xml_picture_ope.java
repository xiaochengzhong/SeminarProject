package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

//提供关于picture xml文件的一组操作
public class xml_picture_ope {
	public xml_picture_ope() {

	}

	// 根据给定的picture的ID获取当前picture所要完成的任务以及相应操作定义
	public picObj getPicTaskInfo(String picID, String taskSetID, String webPath) {
		xml_taskset_ope mtsOp = new xml_taskset_ope();
		tasksetobj mtsObj = mtsOp.getTaskSet(webPath, taskSetID);
		picObj mPObj = new picObj(picID, "TMPE NO DATA");
		ArrayList<taskObj> mTaskObj = mtsObj.getTSTask();
		for (short i = 0; i < mTaskObj.size(); ++i) {
			mPObj.addTaskObj(mTaskObj.get(i));
		}
		return mPObj;
	}

	// 给定picture-set的Id，获取该图片集中图像的列表
	public picSetObj getPicListWithPSID(String psID, String webPath) {
		picSetObj mPSInfo = picOperator.getPicSetInfo(psID);

		// get the picture-set information
		if (mPSInfo == null) // get no data
		{
			return mPSInfo;
		} else {
			String filepath = mPSInfo.getPath();
			filepath = webPath + filepath;
			File pTSFile = new File(filepath);

			if (!pTSFile.exists()) // 当前图片集不存在对应的Xml文件
				return mPSInfo;

			// 创建XML文件对象的应用以及XML操作中相关的变量
			SAXBuilder sb = new SAXBuilder();
			Document doc = null;
			try {
				doc = sb.build(pTSFile);
				mPSInfo.setPath(filepath); // 全路径名

				Element root = doc.getRootElement();
				List picList = root.getChildren("picture");
				for (short i = 0; i < picList.size(); ++i) {
					Element picture = (Element) picList.get(i);
					picObj mPicObj = new picObj(
							picture.getAttributeValue("id"), picture.getText(),
							picture.getAttributeValue("name"));
					mPicObj.setWidth(picture.getAttributeValue("width"));
					mPicObj.setHeight(picture.getAttributeValue("height"));
					mPSInfo.addPicture(mPicObj);
				}
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 构造文档对象
		}
		return mPSInfo;
	}

	// 给定picture-set的ID，在image annotation中生成对应的目录，以及记录文件

	// 根据给定的picture的ID picture-set id，任务集ID，任务ID组，任务图像操作数据包，将数据写入图片的xml文件中
	public boolean addPicShapeData(String webPath, String taskSetID,
			String userID, String picSetID, String picID,
			ArrayList<String> taskIndex, ArrayList<String> taskDataDetail) {
		boolean opSuccess = false;
		// picObj recordData = null;
		// recordData = picOperator.getPicInfo(picID);
		if (picID == null) {
			// the picID is wrong
			return opSuccess;
		} else // deal with the xml files
		{
			String filePath = webPath + "/data/imgannoset/" + taskSetID
					+ picSetID + userID + ".xml"; // to make the picOperator
													// file
													// to be the one

			File pTSFile = new File(filePath);

			boolean fileExists = true;
			if (!pTSFile.exists()) // 当前图片记录集不存在
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
					root = new Element("TaskSet");
					root.setAttribute(new Attribute("id", taskSetID));
				}

				List pictureList = root.getChildren("picture");
				boolean findPic = false;
				for (short picCnt = 0; picCnt < pictureList.size(); ++picCnt) {
					Element currentPic = (Element) pictureList.get(picCnt);
					if (currentPic.getAttributeValue("id").contentEquals(picID)) {
						findPic = true;
						List mTask = currentPic.getChildren("Task");
						boolean findTask = false;
						boolean findRecord = false;
						for (short index = 0; index < taskIndex.size(); ++index) {
							findTask = false;
							for (short cnt = 0; cnt < mTask.size(); ++cnt) {
								if (taskIndex.get(index).contentEquals("-1")) // don't
																				// need
																				// to
																				// record
								{
									findTask = true;
									findRecord = true;
								} else {
									if (((Element) mTask.get(cnt))
											.getAttributeValue("id")
											.contentEquals(taskIndex.get(index))) {
										findTask = true;
										List record = ((Element) mTask.get(cnt))
												.getChildren("record");
										for (short i = 0; i < record.size(); ++i) {
											findRecord = false;
											if (((Element) record.get(i))
													.getAttributeValue("id")
													.contentEquals(userID)) {
												findRecord = true;
												// cover the data
												((Element) record.get(i))
														.removeContent();
												((Element) record.get(i))
														.addContent(new CDATA(
																taskDataDetail
																		.get(index)));
												break;
											}
										}

										if (findRecord == false) {
											Element newRecord = new Element(
													"record");
											newRecord
													.setAttribute(new Attribute(
															"id", userID));
											newRecord.addContent(new CDATA(
													taskDataDetail.get(index)));
											((Element) mTask.get(cnt))
													.addContent(newRecord);
										}
									}
								}
							}

							// create the task for the taskset
							if (findTask == false) {
								Element newTask = new Element("Task");
								newTask.setAttribute(new Attribute("id",
										taskIndex.get(index)));
								Element newRecord = new Element("record");
								newRecord.setAttribute(new Attribute("id",
										userID));
								newRecord.addContent(new CDATA(taskDataDetail
										.get(index)));
								newTask.addContent(newRecord);
								currentPic.addContent(newTask);
							}
						}
					}
				}

				if (!findPic) {
					Element newPic = new Element("picture");
					newPic.setAttribute(new Attribute("id", picID));
					for (short index = 0; index < taskIndex.size(); ++index) {
						if (taskIndex.get(index).contentEquals("-1")) {
						} else {
							Element newTask = new Element("Task");
							newTask.setAttribute(new Attribute("id", taskIndex
									.get(index)));
							Element newRecord = new Element("record");
							newRecord.setAttribute(new Attribute("id", userID));
							newRecord.addContent(new CDATA(taskDataDetail
									.get(index)));
							newTask.addContent(newRecord);
							newPic.addContent(newTask);
						}
					}
					root.addContent(newPic);

				}

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
	}

	//根据给定的picture的ID picture-set的id，发布任务集的ID，任务ID组，任务图像操作数据包，将数据写入数据库中
	public boolean addPicShapeToDB(String relTSID,
			String userID, String picSetID, String picID,
			ArrayList<String> taskIndex, ArrayList<String> taskDataDetail) {
		boolean opSuccess = false;
		reltsBean mrelData = picOperator.getPicOpeDataFromDB(relTSID, picSetID, picID, userID);
		
		Element root = new Element("picture");
		root.setAttribute(new Attribute("id", picID));
		for (short index = 0; index < taskIndex.size(); ++index) {
			if (taskIndex.get(index).contentEquals("-1")) {
			} else {
				Element newTask = new Element("Task");
				newTask.setAttribute(new Attribute("id", taskIndex
						.get(index)));
				Element newRecord = new Element("record");
				newRecord.setAttribute(new Attribute("id", userID));
				newRecord.addContent(new CDATA(taskDataDetail.get(index)));
				newTask.addContent(newRecord);
				root.addContent(newTask);
			}
		}
		if(mrelData == null)      //当前记录不存在，则创建该条记录
		{
			opSuccess = picOperator.insertPicOpeDataToDB(relTSID, picSetID, picID, userID, stringOperator.cleanString(stringOperator.elementToString(root)));
		}
		else                      //更新当前记录
		{
			opSuccess = picOperator.updatePicOpeDataOfDB(relTSID, picSetID, picID, userID, stringOperator.cleanString(stringOperator.elementToString(root)));
		}
		
		return opSuccess;
	}
	
	
	// 根据给定的tasksetId, picture-set id , picture id , userid得到用户关于该图片的操作数据
	public ArrayList<shapeBean> getUserPicDataWithID(String taskSetID,
			String picSetID, String picID, String userID, String webPath) {
		ArrayList<shapeBean> mShapeData = null;
		// picObj recordData = null;
		// recordData = picOperator.getPicInfo(picID);
		if (picID == null) {
			// the picID is wrong
			return mShapeData;
		} else // deal with the xml files
		{
			String filePath = webPath + "/data/imgannoset/" + taskSetID
					+ picSetID + userID + ".xml"; // to make the picOperator
													// file
													// to be the one
			File pTSFile = new File(filePath);

			if (pTSFile.exists()) // to judgement the file exist
			{
				// 创建XML文件对象的应用以及XML操作中相关的变量
				SAXBuilder sb = new SAXBuilder();
				Document doc = null;
				try {
					doc = sb.build(pTSFile);
					Element root = doc.getRootElement();
					List pictureList = root.getChildren("picture");
					xml_taskset_ope mtsObj = new xml_taskset_ope();
					ArrayList<taskObj> mTaskInfo = mtsObj.getTaskSet(webPath,taskSetID).getTSTask();
					mShapeData = new ArrayList<shapeBean>();
					Element mRecord = null;
					Element mPicture = null;
					for (short picCnt = 0; picCnt < pictureList.size(); ++picCnt) {
						mPicture = (Element) pictureList.get(picCnt);
						if (mPicture.getAttributeValue("id").contentEquals(
								picID)) {
							List taskList = mPicture.getChildren("Task");
							for (short i = 0; i < taskList.size(); ++i) {
								List recordData = ((Element) taskList.get(i))
										.getChildren("record");
								for (short index = 0; index < recordData.size(); ++index) {
									mRecord = (Element) recordData.get(index);
									for (short j = 0; j < mTaskInfo.size(); ++j) {
										if (mTaskInfo.get(j).getTaskID().contentEquals(((Element) taskList.get(i)).getAttributeValue("id"))) {
											shapeBean newShape = new shapeBean();
											newShape.setLineWidth(mTaskInfo.get(j).getLineWidth());
											newShape.setLineColor(mTaskInfo.get(j).getLineCol());
											newShape.setShapeID(mTaskInfo.get(j).getShapeID());
											newShape.setTaskID(mTaskInfo.get(j).getTaskID());
											String shapeID = newShape.getShapeID();
											Element mXmlSData = mRecord.getChild("Data");
											String formatData = "";
											if (shapeID.contentEquals("3")|| shapeID.contains("4")) // circle or rect
											{
												List extendData = mXmlSData.getChildren("Extend");
												for (short cnt = 0; cnt < extendData.size(); ++cnt) {
													Element start = ((Element) extendData.get(cnt)).getChild("start");
													Element end = ((Element) extendData.get(cnt)).getChild("end");
													formatData = formatData+ start.getAttributeValue("X")+ ","+ start.getAttributeValue("Y")+ ","
															+ end.getAttributeValue("X")+ ","+ end.getAttributeValue("Y")+ "&";
												}
											} else if (shapeID.contentEquals("2")) // line
											{
												List extendData = mXmlSData.getChildren("Extend");
												for (short cnt = 0; cnt < extendData.size(); ++cnt) {
													List mPoints = ((Element) extendData.get(cnt)).getChild("points").getChildren("point");
													Element start = (Element) mPoints.get(0);
													Element end = (Element) mPoints.get(1);
													formatData = formatData+ start.getAttributeValue("X")+ ","+ start.getAttributeValue("Y")
															+ ","+ end.getAttributeValue("X")+ ","+ end.getAttributeValue("Y")+ "&";

												}
											} else // 曲线，多边形，不规则区域
											{
												String mxmlSInfo = "";
												List extendData = mXmlSData.getChildren("Extend");
												for (short cnt = 0; cnt < extendData.size(); ++cnt) {
													List mPoints = ((Element) extendData.get(cnt)).getChild("points").getChildren("point");
													for (short pointIndex = 0; pointIndex < mPoints.size(); ++pointIndex) {
														Element point = (Element) mPoints.get(pointIndex);
														formatData = formatData+ point.getAttributeValue("X")+ ","+ point.getAttributeValue("Y")+ ",";
													}
													formatData += "&";

												}
											}
											newShape.setData(formatData);
											mShapeData.add(newShape);
											break;
										}
									}
									break;
								}
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
			}
		}
		return mShapeData;
	}
	
	// 根据给定的tasksetId, picture-set id , picture id , userid从数据库中得到用户关于该图片的操作数据
	public ArrayList<shapeBean> getUserPicDataWithIDFDB(String relTSID,String picSetID, String picID, String userID) {
		ArrayList<shapeBean> mShapeData = null;
		reltsBean mrelData = picOperator.getPicOpeDataFromDB(relTSID, picSetID, picID, userID);
		if(mrelData == null)
			return mShapeData;
		// 创建XML文件对象的应用以及XML操作中相关的变量
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(new StringReader(mrelData.getM_content()));
			Element root = doc.getRootElement();
			List taskList = root.getChildren("Task");
			xml_taskset_ope mtsObj = new xml_taskset_ope();
			ArrayList<taskObj> mTaskInfo = mtsObj.getTaskSet(mtsObj.getRelTSInfo(relTSID).getM_tsID()).getTSTask();
			mShapeData = new ArrayList<shapeBean>();
			Element mRecord = null;
			for (short i = 0; i < taskList.size(); ++i) {
				List recordData = ((Element) taskList.get(i)).getChildren("record");
				for (short index = 0; index < recordData.size(); ++index) {
					mRecord = (Element) recordData.get(index);
					for (short j = 0; j < mTaskInfo.size(); ++j) {
						if (mTaskInfo.get(j).getTaskID().contentEquals(((Element) taskList.get(i)).getAttributeValue("id"))) {
							shapeBean newShape = new shapeBean();
							newShape.setLineWidth(mTaskInfo.get(j).getLineWidth());
							newShape.setLineColor(mTaskInfo.get(j).getLineCol());
							newShape.setShapeID(mTaskInfo.get(j).getShapeID());
							newShape.setTaskID(mTaskInfo.get(j).getTaskID());
							String shapeID = newShape.getShapeID();
							Element mXmlSData = mRecord.getChild("Data");
							String formatData = "";
							if (shapeID.contentEquals("3")|| shapeID.contains("4")) // circle or rect
							{
								List extendData = mXmlSData.getChildren("Extend");
								for (short cnt = 0; cnt < extendData.size(); ++cnt) {
									Element start = ((Element) extendData.get(cnt)).getChild("start");
									Element end = ((Element) extendData.get(cnt)).getChild("end");
									formatData = formatData+ start.getAttributeValue("X")+ ","+ start.getAttributeValue("Y")+ ","
											+ end.getAttributeValue("X")+ ","+ end.getAttributeValue("Y")+ "&";
								}
							} else if (shapeID.contentEquals("2")) // line
							{
								List extendData = mXmlSData.getChildren("Extend");
								for (short cnt = 0; cnt < extendData.size(); ++cnt) {
									List mPoints = ((Element) extendData.get(cnt)).getChild("points").getChildren("point");
									Element start = (Element) mPoints.get(0);
									Element end = (Element) mPoints.get(1);
									formatData = formatData+ start.getAttributeValue("X")+ ","+ start.getAttributeValue("Y")
											+ ","+ end.getAttributeValue("X")+ ","+ end.getAttributeValue("Y")+ "&";

								}
							} else // 曲线，多边形，不规则区域
							{
								List extendData = mXmlSData.getChildren("Extend");
								for (short cnt = 0; cnt < extendData.size(); ++cnt) {
									List mPoints = ((Element) extendData.get(cnt)).getChild("points").getChildren("point");
									for (short pointIndex = 0; pointIndex < mPoints.size(); ++pointIndex) {
										Element point = (Element) mPoints.get(pointIndex);
										formatData = formatData+ point.getAttributeValue("X")+ ","+ point.getAttributeValue("Y")+ ",";
									}
									formatData += "&";

								}
							}
							newShape.setData(formatData);
							mShapeData.add(newShape);
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
		return mShapeData;
	}
	
	//添加新的图像数据集到数据库
	public boolean addNewPicset(String webPath , String userID , String useName , String tittle , String desp , ArrayList<String> filePath)
	{
		//constructor the xml files
		ArrayList<picObj> mPicList = fileOperator.getPictureList(webPath, filePath);
		String content = "<INFORMATION><Folders>";
		for(short i = 0 ; i < filePath.size(); ++i)
		{
			content = content + "<Folder>" + filePath.get(i) + "</Folder>";
		}
		content = content + "</Folders><Pictures>";
		for(short i = 0 ; i < mPicList.size() ; ++i)
		{
			content = content + "<pic id=\\\"" + i +"\\\" width=\\\"" +mPicList.get(i).getWidth() +"\\\" height=\\\"" +mPicList.get(i).getHeight() +"\\\" name=\\\"" + mPicList.get(i).getName() + "\\\">";
			content = content + mPicList.get(i).getPath() + "</pic>";
		}
		content = content + "</Pictures></INFORMATION>";
		picSetObj tempOne = new picSetObj("PS"+stringOperator.timeFormat()+userID, tittle);
		tempOne.setContent(content);
		tempOne.setTime(stringOperator.timeFormatT());
		tempOne.setAuthorName(useName);
		tempOne.setdesp(desp);
		
		return picOperator.inNewPicset(tempOne);
	}
	
	//更新给定的图像集Id的内容
	public boolean updatePicset(String webPath , String picID , String tittle , String desp , ArrayList<String> filePath)
	{
		//constructor the xml files
		ArrayList<picObj> mPicList = fileOperator.getPictureList(webPath, filePath);
		String content = "<INFORMATION><Folders>";
		for(short i = 0 ; i < filePath.size(); ++i)
		{
			content = content + "<Folder>" + filePath.get(i) + "</Folder>";
		}
			content = content + "</Folders><Pictures>";
			for(short i = 0 ; i < mPicList.size() ; ++i)
		{
			content = content + "<pic id=\\\"" + i +"\\\" width=\\\"" +mPicList.get(i).getWidth() +"\\\" height=\\\"" +mPicList.get(i).getHeight() +"\\\" name=\\\"" + mPicList.get(i).getName() + "\\\">";
			content = content + mPicList.get(i).getPath() + "</pic>";
		}
		content = content + "</Pictures></INFORMATION>";
		picSetObj tempOne = new picSetObj(picID, tittle);
		tempOne.setContent(content);
		tempOne.setTime(stringOperator.timeFormatT());
		tempOne.setdesp(desp);
		
		return picOperator.updateNewPicset(tempOne);
	}
	
	//从数据库中得到图像集的基本信息（图像集的Id，创建的的名字 ， tittle ， description）
	public ArrayList<picSetObj> getAllPicsInfo()
	{
		return picOperator.getPSAInfo();
	}
	
	//从数据库中获取给定图像集的所有信息
	public picSetObj getPicsetInfo(String psID)
	{
		picSetObj mData = picOperator.getPSInfo(psID);
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(new StringReader(mData.getContent()));
			Element root = doc.getRootElement();
			List mFolderls = root.getChild("Folders").getChildren("Folder");
			for(short cnt = 0 ; cnt < mFolderls.size() ; ++cnt)
			{
				mData.addPicsetPath(((Element)mFolderls.get(cnt)).getText());
			}
			mData.setContent("");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mData;
	}
}
