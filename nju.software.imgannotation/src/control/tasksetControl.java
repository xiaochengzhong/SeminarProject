package control;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.picObj;
import model.picOperator;
import model.userObj;
import model.userOperator;
import model.xmlOperator;
import model.xml_taskset_ope;

/**
 * Servlet implementation class taskControl
 */
public class tasksetControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public tasksetControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession pSession = request.getSession(true);
		String userName = (String)pSession.getAttribute("pName");
		String userID = (String)pSession.getAttribute("pID");
		if(userName == null || userID == null)
			response.sendRedirect("login.jsp");
		
		String[] adminInfo = {userID , userName};               //get the admin ID
		request.setCharacterEncoding("UTF-8");
		
		String[] tasksetID = request.getParameterValues("taskset_id");
		if(tasksetID == null)             //add new taskset
		{
			String[] taskArr = request.getParameterValues("task_name");
			String tittle = request.getParameter("tittle");
			String mDescp = request.getParameter("desp");

			if(taskArr == null || tittle == null || mDescp == null)      //couldn't get the data
			{
				System.out.println("Couldn't get the name");
				return;
			}
			//generator the file data
			String taskData="";
			for(short i = 0 ; i <taskArr.length ; ++i)
			{
				taskData += taskArr[i];
			}

			if(addNewTaskSet(taskData, tittle, mDescp, adminInfo))
			{
				System.out.print("Success");
				response.sendRedirect("adminTSList.jsp");
			}
			else
			{
				System.out.print("failed");
				response.sendRedirect("adminTSList.jsp");
			}
		}
		else                              //update new taskset
		{
			String[] authorInfo = {request.getParameter("authorID") , request.getParameter("authorName")};
			String[] taskArr = request.getParameterValues("task_name");
			String tittle = request.getParameter("tittle");
			String mDescp = request.getParameter("desp");
			String tsID = tasksetID[0];
			System.out.println(tsID);
			if(taskArr == null || tittle == null || mDescp == null)      //couldn't get the data
			{
				System.out.println("Couldn't get the name");
				return;
			}
			//generator the file data
			String taskData="";
			for(short i = 0 ; i <taskArr.length ; ++i)
			{
				taskData += taskArr[i];
			}

			if(updateNewTaskSet(taskData, tittle, mDescp, tsID , authorInfo))
			{
				System.out.print("Success");
				response.sendRedirect("adminTSList.jsp");
			}
			else
			{
				System.out.print("failed");
				response.sendRedirect("adminTSList.jsp");
			}
		}
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	
	private boolean addNewTaskSet(String mContent , String mTittle , String mDesp ,  String[] adminInfo)
	{
		xml_taskset_ope mts = new xml_taskset_ope();
		return mts.addNewTaskSet(mContent, mTittle, mDesp, adminInfo);
	}
	
	private boolean updateNewTaskSet(String mContent , String mTittle , String mDesp , String tsID , String[] adminInfo)
	{
		xml_taskset_ope mts = new xml_taskset_ope();
		return mts.updateTSWithID(mContent, mTittle, mDesp, tsID , adminInfo);
	}
	
	//提交任务集到服务器，暂时不添加图片的XML文件，以及用户的XML文件
	private boolean createTaskSet(String webPath , String taskSetContent , String[] adminInfo) 
	{
		boolean opSuccess = false;
		//构造数据集格式
		String taskSetPath = webPath + "data/taskSet/";
		DateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		String fileName = nameFormat.format(currentTime) + adminInfo[0];        //需要加上管理员的ID以确认唯一性
		taskSetPath = taskSetPath + fileName + ".xml"; 
		// set the xml file data
		taskSetContent = "<TaskSet id=\"" + fileName + "\"><author id=\"" + adminInfo[0]+ "\">"
				+ adminInfo[1] + "</author>" + "<time>"
				+ timeFormat.format(currentTime) + "</time>" + taskSetContent
				+ "</TaskSet>";
		//将记录存入文件目录中
		opSuccess = xmlOperator.xmlOpeRight().createXmlFile(taskSetPath, taskSetContent);
		//将记录插入数据库中
		//opSuccess = tasksetOperator.insertTStoDB(fileName, adminInfo);
		return opSuccess;
	}

	//提交任务清单后需要生成三份XML文件，分别是：任务清单XML，用户任务XML，以及图像设置XML
	/**create the taskSet XML
	 * webPath : the path of current web application 
	 */
	private boolean createTaskSet(String webPath , String taskContent , String[] workerArr , String[] picArray)
	{
		String taskPath = webPath + "data/taskSet/";
		//create the file Name
		DateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		String fileName = nameFormat.format(currentTime);        //需要加上管理员的ID以确认唯一性
		taskPath = taskPath + fileName + ".xml";
		String[] operInfo = {webPath , fileName , timeFormat.format(currentTime)};
		//set the xml file data
		taskContent = "<TaskSet id=\"" + fileName +"\"><author>" + timeFormat.format(currentTime) + "</author>" + 
				 "<time>"+ timeFormat.format(currentTime) +"</time>"+ taskContent + "</TaskSet>";
		
		taskContent += taskSetHelper(operInfo, taskContent, workerArr, picArray);
		return xmlOperator.xmlOpeRight().createXmlFile(taskPath, taskContent);
	}
	//help the createTaskSet to set the member content
	private String taskSetHelper(String[] webInfo , String taskContent , String[] userID , String[] picID)
	{
		ArrayList<userObj> userInfo = userOperator.getUserName(userID);
		ArrayList<picObj> picInfo = picOperator.getPicInfo(picID);
		String content = "";
		
		//create the picture xml files
		if(createPicSet(webInfo , taskContent , picInfo))
		{
			//create the xml file success, then create the user's task xml 
			if(applendTaskForW(webInfo, userInfo, picInfo))
			{
				content += "<pictures>";
				for(short i=0 ; i < picInfo.size() ; ++i)
				{
					content = content + "<pic id=\""+ picInfo.get(i).getID() +"\">" +
							picInfo.get(i).getPath() + "/" + picInfo.get(i).getName()+"</pic>";
				}
				content += "</pictures><members>";
				for(short i = 0 ; i < userInfo.size() ; ++i)
				{
					content = content + "<mem id=\""+ userInfo.get(i).getID() +"\">"+userInfo.get(i).getName()+"</mem>";
				}
				content += "</members>";
			}
		}
		return content;
	}
	
	/**create the xml files for the picture
	 */
	private boolean createPicSet(String[] webInfo , String taskContent , ArrayList<picObj> picInfo)
	{
		//format the content for the picture xml file
		//taskContent = taskContent + "<Element id=\"" + webInfo[1]  + "\"></Element>";
		for(short i = 0 ; i < picInfo.size() ; ++i)
		{
			if(!xmlOperator.xmlOpeRight().createXmlFile(webInfo[0] + picInfo.get(i).getPath() + "/" + picInfo.get(i).getName()+".xml", taskContent))
				return false;
		}
		return true;
	}
	
	/**
	 * create the xml files for the users
	 */
	private boolean applendTaskForW(String[] taskInfo , ArrayList<userObj> useInfo , ArrayList<picObj> picInfo)
	{
		//format the content for the worker
		String content = "<taskSet id=\""+taskInfo[1]+"\"><time>"+ taskInfo[2]  +"</time><status>0</status><complete>";
		for(short i = 0 ; i < picInfo.size() ; ++i)
		{
			content = content + "<pic id=\"" + picInfo.get(i).getID()  + "\" status=\"0\">" +
					picInfo.get(i).getPath() + picInfo.get(i).getName() + "</pic>";
		}
		content += "</complete></taskSet>";
		for(short i = 0 ; i < useInfo.size(); ++i)
		{
			//System.out.println(taskInfo[0] + "/users/" + useInfo.get(i).getID() + ".xml");
			if(!xmlOperator.xmlOpeRight().createXmlFile(taskInfo[0] + "data/users/" + useInfo.get(i).getID() + ".xml" , content))
				return false;
		}
		return true;
	}
}
