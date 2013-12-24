package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.stringOperator;
import model.tasksetOperator;
import model.userObj;
import model.usersetBean;
import model.xml_taskset_ope;
import model.xml_user_ope;

/**
 * Servlet implementation class relTSControl
 */
public class relTSControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public relTSControl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");                            //set the post character encoding
		response.setCharacterEncoding("utf-8");
		HttpSession pSession = request.getSession(true);
		String userName = (String)pSession.getAttribute("pName");
		String userID = (String)pSession.getAttribute("pID");
		if(userName == null || userID == null)
			response.sendRedirect("login.jsp");
		
		String[] mTSID = request.getParameterValues("taskSet_ID");
		String[] mPSID = request.getParameterValues("pictureSet_ID");
		String[] mPLID = request.getParameterValues("personlist_ID");
		String[] adminInfo = {userID , userName};

		if (mTSID == null || mPSID == null || mPLID == null) // get no data
		{

		} else {
			addNewRelTS(mTSID , mPSID , mPLID , adminInfo);
			response.sendRedirect("adminTSList.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

	private boolean addNewRelTS(String[] mTSID, String[] mPSID, String[] mPLID,
			String[] adminInfo) {
		boolean opSuccess = false;
		xml_taskset_ope mTSOperator = new xml_taskset_ope();
		xml_user_ope mUSOperator = new xml_user_ope();
		for (short index = 0; index < mTSID.length; ++index) {
			String relTSID = stringOperator.timeFormat()+adminInfo[0];
			opSuccess = mTSOperator.addRelTaskSettoDB(mTSID[index], mPSID[0], mPLID, adminInfo , relTSID);
			ArrayList<userObj> mUserList = mUSOperator.getPerSerInfoWithID(mPLID[0]).getUserList();
			// generate the user XML
			for (short cnt = 0; cnt < mUserList.size(); ++cnt)
				mUSOperator.addNewTaskToUser(mUserList.get(cnt).getID(), relTSID);
		}
		return opSuccess;
	}

}
