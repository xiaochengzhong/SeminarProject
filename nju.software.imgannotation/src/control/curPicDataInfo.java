package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.picObj;
import model.shapeBean;
import model.xml_picture_ope;
import model.xml_user_ope;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Neg;

/**
 * Servlet implementation class curPicDataInfo
 */
public class curPicDataInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public curPicDataInfo() {
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

		String taskSetID = request.getParameterValues("tasksetID")[0];
		String picSetID = request.getParameterValues("psID")[0];
		String picID = request.getParameterValues("picID")[0];
		String userID = request.getParameterValues("userID")[0];
		String webPath = request.getSession().getServletContext().getRealPath("/");
		
		if(taskSetID != null && picSetID != null && picID != null && userID != null)
		{
			//get the next picture
			pWriter.print(getCurrentPic(userID, taskSetID, picSetID, picID, webPath));
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

	protected String getCurrentPic(String userID , String taskSetID , String picsetID , String picID , String webPath) {
		picObj nextPic = (new xml_user_ope()).getCuPicObj(taskSetID , userID , picsetID);
		xml_picture_ope mPicture = new xml_picture_ope();
		ArrayList<shapeBean> data = mPicture.getUserPicDataWithIDFDB(taskSetID, picsetID , picID, userID);
		nextPic.setShapeList(data);
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
