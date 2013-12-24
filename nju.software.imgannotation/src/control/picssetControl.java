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

/**
 * Servlet implementation class picssetControl
 */
public class picssetControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public picssetControl() {
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
		
		String[] picsetID = request.getParameterValues("ps_picsetid");
		if(picsetID == null)                 //insert a new picture-set
		{
			String tittle = request.getParameterValues("ps_tittle")[0];
			String desp = request.getParameterValues("ps_desp")[0];
			ArrayList<String> filePath = stringOperator.parseJsonToArray(request.getParameterValues("ps_fileFolder")[0]);
			xml_picture_ope mpic = new xml_picture_ope();
			if(mpic.addNewPicset(request.getSession().getServletContext().getRealPath("/"), userID, userName, tittle, desp, filePath))
			{
				//System.out.println("success");
				response.sendRedirect("adminPSList.jsp");
			} 
			else
			{
				System.out.println("failed");
			}
		}
		else                                 //update the picture-set
		{
			String psID = picsetID[0];
			String tittle = request.getParameterValues("ps_tittle")[0];
			String desp = request.getParameterValues("ps_desp")[0];
			ArrayList<String> filePath = stringOperator.parseJsonToArray(request.getParameterValues("ps_fileFolder")[0]);
			xml_picture_ope mpic = new xml_picture_ope();
			if(mpic.updatePicset(request.getSession().getServletContext().getRealPath("/"), psID, tittle, desp, filePath))
			{
				response.sendRedirect("adminPSList.jsp");
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
