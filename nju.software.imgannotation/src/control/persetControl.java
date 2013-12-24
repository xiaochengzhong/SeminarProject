package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.stringOperator;
import model.xml_picture_ope;
import model.xml_user_ope;

/**
 * Servlet implementation class persetControl
 */
public class persetControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public persetControl() {
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
		HttpSession pSession = request.getSession(true);
		String userName = (String)pSession.getAttribute("pName");
		String userID = (String)pSession.getAttribute("pID");
		if(userName == null || userID == null)
			response.sendRedirect("login.jsp");
		
		String[] persetID = request.getParameterValues("ps_personsetID");
		if(persetID == null)                 //insert a new person-set
		{
			String tittle = request.getParameterValues("tittle")[0];
			String desp = request.getParameterValues("desp")[0];
			String[] userIDList = request.getParameterValues("personlist_ID");
			String[] adminInfo = {userID , userName};
			xml_user_ope mUserOper = new xml_user_ope();
			if(mUserOper.addNewPerSetToDB(tittle, desp, userIDList, adminInfo))
			{
				response.sendRedirect("adminPerSList.jsp");
			} 
			else
			{
				System.out.println("failed");
			}
		}
		else                                 //update the person-set
		{
			String psID = persetID[0];
			String tittle = request.getParameterValues("tittle")[0];
			String desp = request.getParameterValues("desp")[0];
			String[] userIDList = request.getParameterValues("personlist_ID");
			xml_user_ope muser = new xml_user_ope();
			if(muser.updatePerSetFDB(tittle, desp, userIDList, psID))
			{
				response.sendRedirect("adminPerSList.jsp");
			}
			else
			{
				System.out.println("update failed");
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

}
