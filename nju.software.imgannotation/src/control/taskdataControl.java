package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.picObj;
import model.reltsBean;
import model.shapeBean;
import model.stringOperator;
import model.xml_picture_ope;
import model.xml_user_ope;

/**
 * Servlet implementation class taskdataControl
 */
public class taskdataControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public taskdataControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");                            //set the post character encoding
		response.setCharacterEncoding("utf-8");
		PrintWriter pWriter = response.getWriter();

		ArrayList<String> taskDataInfo = stringOperator.parseJsonToArray(request.getParameterValues("taskDataInfo")[0]);
		ArrayList<String> taskIndex = stringOperator.parseJsonToArray(request.getParameterValues("taskDataID")[0]);
		String taskSetID = request.getParameterValues("tasksetID")[0];
		String picSetID = request.getParameterValues("psID")[0];
		String picID = request.getParameterValues("picID")[0];
		String userID = request.getParameterValues("userID")[0];
		String webPath = request.getContextPath() + "/";
		
		//String taskData = ;
		//pWriter.println(taskData);
		//pWriter.println(picSetID);
		
		if(addUserTaskData(webPath, taskSetID, picSetID, picID, userID, taskIndex, taskDataInfo))
		{
			//get the next picture
			pWriter.print(getNextPic(userID, taskSetID, picSetID, picID, webPath));
		}
		else
		{
			pWriter.print("Error!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	
	
	
	protected boolean addUserTaskData(String webPath , String taskSetID , String picSetID , String picID ,String userID , ArrayList<String> taskIndex , ArrayList<String> taskDataDetail)
	{
		boolean opSuccess = false;
		xml_picture_ope openModel = new xml_picture_ope();
		opSuccess = openModel.addPicShapeToDB(taskSetID, userID, picSetID, picID, taskIndex, taskDataDetail);
		return opSuccess;
	}
	
	protected String getNextPic(String userID , String taskSetID , String picsetID , String picID , String webPath) {
		xml_user_ope mOperator = new xml_user_ope();
		picObj nextPic =  mOperator.getNextPicFDB(userID , taskSetID , picsetID , picID);
		xml_picture_ope mPicture = new xml_picture_ope();
		//recontruct the path
		if(nextPic == null)
			return null;
		ArrayList<shapeBean> data = mPicture.getUserPicDataWithIDFDB(taskSetID, nextPic.getPSID(), nextPic.getID(), userID);
		nextPic.setShapeList(data);
		nextPic.setPath(webPath + nextPic.getPath());
		StringWriter out = new StringWriter();
		JsonGenerator jg = null;
		
		try {
			JsonFactory jf = new JsonFactory();
			jg = jf.createGenerator(out);
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(jg, nextPic);
			if(jg != null)
				jg.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out.toString();
	}
}
