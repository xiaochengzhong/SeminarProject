package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

//用于操作taskset相关的xml文件
public class xml_taskset_ope {

	// 构造函数
	public xml_taskset_ope() {

	}

	// 根据给定taskset的ID号，得到taskset的任务内容
	public tasksetobj getTaskSet(String webPath, String tasksetID) {
		tasksetobj pStorage = null;
		// 得到给定tasksetID的文件路径
		String filePath = webPath + "/data/taskset/" + tasksetID + ".xml";

		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前任务集不存在或者已被删除
			return pStorage;

		// deal the xml files
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			Element element = (Element) root.getChildren("TaskSet").get(0);// 取名字为TaskSet的所有元素(每个任务文件只有一个TaskSet标签)
			String tasksetAuthor = element.getChildText("author"); // 取taskSet子元素author的内容
			String tasksetTime = element.getChildText("time");
			String taskeetAuID = ((Element)(element.getChildren("author").get(0))).getAttributeValue("id");
			List shapeInfo = element.getChildren("Task");

			// define the pStorage
			pStorage = new tasksetobj();
			pStorage.setTSID(tasksetID);
			pStorage.setTStime(tasksetTime);
			pStorage.setTSAuthor(tasksetAuthor);
			// 解析图像任务
			for (short j = 0; j < shapeInfo.size(); ++j) {
				taskObj newTask = new taskObj();
				Element taskelment = (Element) shapeInfo.get(j);
				newTask.setTaskName(taskelment.getAttributeValue("name"));
				newTask.setTaskID(taskelment.getAttributeValue("id"));
				Element shape = (Element) taskelment.getChildren("Shape")
						.get(0);
				newTask.setShapeName(shape.getAttributeValue("Type"));
				newTask.setShapeID(shape.getAttributeValue("id"));
				Element shapeSet = shape.getChild("Settings");
				// get the name information
				Element line = (Element) shapeSet.getChildren("Line").get(0);
				newTask.setLineWidth(Integer.parseInt(line
						.getAttributeValue("Width")));
				newTask.setLineCol(line.getChildText("Color"));
				// get the fill information
				Element fill = (Element) shapeSet.getChildren("Fill").get(0);
				newTask.setFill(fill.getAttributeValue("FillType")
						.contentEquals("true"));
				newTask.setFillCol(fill.getChildText("Color"));
				pStorage.addTask(newTask);
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象

		return pStorage;
	}

	// 根据给定的taskset的ID号，将图片集以及用户列表加入到列表中
	public boolean addPSaPL(String webPath, String tasksetID,
			String[] picsetID, String[] userID) {
		boolean opSuccess = false;
		String filePath = webPath + "/data/taskset/" + tasksetID + ".xml";
		File pTSFile = new File(filePath);

		if (!pTSFile.exists()) // 当前任务集不存在或者已被删除
			return opSuccess;
		// deal the xml files
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(pTSFile);
			Element root = doc.getRootElement(); // 获取根元素
			
			//添加图片集
			Element pictures = new Element("pictures");
			picSetObj mPSData = null;
			xml_picture_ope mPicSet = new xml_picture_ope();
			for(short i = 0 ; i < picsetID.length ; ++i)
			{
				Element newPicSet = new Element("pictureset");
				newPicSet.setAttribute(new Attribute("id", picsetID[i]));
				
				mPSData = mPicSet.getPicListWithPSID(picsetID[i], webPath);
				ArrayList<picObj> mPicList = mPSData.getPicList();
				for(short j=0 ; j < mPicList.size() ; ++j)
				{
					Element newElement = new Element("pic");
					newElement.setAttribute(new Attribute("id", mPicList.get(j).getID()));
					newElement.addContent(xmlOperator.dealString(mPicList.get(j).getPath()) + "/" + xmlOperator.dealString(mPicList.get(j).getName()));
					newElement.setAttribute(new Attribute("width", mPicList.get(j).getWidth()));
					newElement.setAttribute(new Attribute("height", mPicList.get(j).getHeight()));
					newPicSet.addContent(newElement);
				}
				
				pictures.addContent(newPicSet);
			}
			root.addContent(pictures);
			//添加人员列表
			Element perlist = new Element("members");
			ArrayList<userObj> mUsetInfoList = userOperator.getUserName(userID);
			for(short i=0 ; i < mUsetInfoList.size() ; ++i)
			{
				Element newPer = new Element("mem");
				newPer.setAttribute(new Attribute("id", mUsetInfoList.get(i).getID()));
				newPer.addContent(mUsetInfoList.get(i).getName());
				perlist.addContent(newPer);
			}
			root.addContent(perlist);
			
			//write the data to the xml file
			//文件的写入
			XMLOutputter outputter = new XMLOutputter();
			FileOutputStream out=new FileOutputStream(filePath);
			outputter.output(doc, out);
			out.close();
			
			opSuccess = true;
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 构造文档对象
		return opSuccess;
	}
	
	//添加新的任务集列表到数据库
	public boolean addNewTaskSet(String mContent , String mTittle , String mDesp ,  String[] adminInfo)
	{
		//format the database
		mContent = "<TaskSet><author id=\\\""+ adminInfo[0] +"\\\">"+adminInfo[1]+"</author>" + mContent + "</TaskSet>";
		return tasksetOperator.addNewTSToDB(mContent, mTittle, mDesp, adminInfo);
	}
	
	//得到从未发布过的任务集的内容
	public ArrayList<tasksetobj> getUnReleaseTS()
	{
		ArrayList<tasksetobj> mData = tasksetOperator.getUnReleaseTS();
		return mData;
	}
	
	//得到所有已发布的任务集(taskset information)
	public ArrayList<tasksetobj> getAllReleaseTS()
	{
		ArrayList<tasksetobj> mData = tasksetOperator.getAllReleaseTS();
		return mData;
	}
	
	//得到所有已发布的任务集(release taskset information)
	public ArrayList<reltsBean> getRelTSInfo()
	{
		return tasksetOperator.getAllReleaeTSData();
	}
	
	
	//得到给定taskset的具体信息
	public tasksetobj getTaskSet(String tasksetID) {
		tasksetobj mData = tasksetOperator.getTSInfo(tasksetID);
		if(mData == null)
			return mData;
		// deal the xml files
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(new StringReader(mData.getContent()));
			Element mTSet = doc.getRootElement(); // 获取根元素

			String taskeetAuID = ((Element)(mTSet.getChildren("author").get(0))).getAttributeValue("id");
			mData.setauthorID(taskeetAuID);
			List shapeInfo = mTSet.getChildren("Task");

			// 解析图像任务
			for (short j = 0; j < shapeInfo.size(); ++j) {
				taskObj newTask = new taskObj();
				Element taskelment = (Element) shapeInfo.get(j);
				newTask.setTaskName(taskelment.getAttributeValue("name"));
				newTask.setTaskID(taskelment.getAttributeValue("id"));
				Element shape = (Element) taskelment.getChildren("Shape")
						.get(0);
				newTask.setShapeName(shape.getAttributeValue("Type"));
				newTask.setShapeID(shape.getAttributeValue("id"));
				Element shapeSet = shape.getChild("Settings");
				// get the name information
				Element line = (Element) shapeSet.getChildren("Line").get(0);
				newTask.setLineWidth(Integer.parseInt(line
						.getAttributeValue("Width")));
				newTask.setLineCol(line.getChildText("Color"));
				// get the fill information
				Element fill = (Element) shapeSet.getChildren("Fill").get(0);
				newTask.setFill(fill.getAttributeValue("FillType")
						.contentEquals("true"));
				newTask.setFillCol(fill.getChildText("Color"));
				//set description
				newTask.setDescription(taskelment.getChildText("despcription"));
				mData.addTask(newTask);
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
	
	//更新制定任务集的内容
	public boolean updateTSWithID(String mContent , String mtittle , String mDesp , String tsID , String[] adminInfo)
	{
		mContent = "<TaskSet><author id=\\\""+ adminInfo[0] +"\\\">"+adminInfo[1]+"</author>" + mContent + "</TaskSet>";
		return tasksetOperator.updateNewTaskset(mContent, mtittle, mDesp, tsID);
	}
	
	//发布任务集到数据库中，其中包含了添加图像集，任务集，人员列表
	public boolean addRelTaskSettoDB(String tasksetID,String picsetID, String[] userID , String[] adminInfo , String relTSID) {
//		String personList = "<persions>";
//		for(short i = 0 ; i < userID.length ; ++i)
//		{
//			personList = personList + "<persion id=\\\""+ userID[i] + "\\\"></persion>";
//		}
//		personList += "</persions>";

		return tasksetOperator.addNewRelTaskSet(tasksetID, picsetID, userID[0], adminInfo ,relTSID);
	}
	
	//根据release-taskset-id 相关的taskset-id , picture-set id , user-list(Admin API)
	public reltsBean getRelTSInfo(String relTaskID)
	{
		return tasksetOperator.getRelTSInfo(relTaskID);
	}

	//得到一份已发布的任务的当前任务中得人员列表
	public ArrayList<userObj> getTSPersonList(String relTaskID)
	{
		reltsBean mrelts = tasksetOperator.getRelTSInfo(relTaskID);
		if(mrelts == null)
			return null;
		xml_user_ope mData = new xml_user_ope();
		return mData.getPerSerInfoWithID(mrelts.getM_plIDString()).getUserList();
		// 创建XML文件对象的应用以及XML操作中相关的变量
//		SAXBuilder sb = new SAXBuilder();
//		Document doc = null;
//		try {
//			doc = sb.build(new StringReader(mrelts.getM_plIDString()));
//			Element root = doc.getRootElement();
//			List mPerlist = root.getChildren("persion");
//			String[] mUser = new String[mPerlist.size()];
//			
//			for(short i = 0 ; i < mPerlist.size() ; ++i)
//			{
//				mUser[i] = ((Element)mPerlist.get(i)).getAttributeValue("id");
//			}
//			m_data = userOperator.getUserName(mUser);
//			
//		} catch (JDOMException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // 构造文档对象
//		return m_data;
	}

}
