package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.xml_user_ope;

/**
 * Servlet implementation class submitTask
 */
public class submitTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public submitTask() {
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
		String puserID = (String)pSession.getAttribute("pID");
		if(userName == null || puserID == null)
			response.sendRedirect("login.jsp");
		
		
		String taskSetID = request.getParameterValues("tasksetid")[0];
		String userID = request.getParameterValues("userID")[0];
		if(submitTaskToDB(taskSetID, userID)) {
			response.sendRedirect("commtaskshow.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	
	
	protected boolean submitTaskToDB(String taskSetID , String userID) {
		xml_user_ope mOperator = new xml_user_ope();
		return mOperator.submitTSDataToDB(taskSetID, userID);
	}

}
