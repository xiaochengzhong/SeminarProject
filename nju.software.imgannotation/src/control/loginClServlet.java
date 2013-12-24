package control;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.userObj;
import model.userOperator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




import sun.misc.*;

import java.security.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;





/**
 * Servlet implementation class loginClServlet
 */
public class loginClServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginClServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		String rawemail = request.getParameter("email").toLowerCase();
		String email=rawemail+"@software.nju.edu.cn";
		String password=request.getParameter("password");
		
		
		String encodedPassword=null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] b = md.digest();
			encodedPassword=(new BASE64Encoder()).encode(b);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 String data="<?xml version=\"1.0\" encoding=\"UTF-8\"?><auth><email>"+email+"</email><password>"+encodedPassword+"</password></auth>";
		 String url="http://software.nju.edu.cn:8000/api/auth/user/";
		 
		 try{
			 HttpPost httppost = new HttpPost(url);
			 HttpClient httpClient=new DefaultHttpClient();
			 List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			 params.add(new BasicNameValuePair("fromdata", data));
			 httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			 HttpResponse rs = httpClient.execute(httppost);
			 String info=EntityUtils.toString(rs.getEntity());
             if(info.contains(">0<")){
            	 Cookie pIdCookie=new Cookie("pID",rawemail);
            	 pIdCookie.setMaxAge(14*24*3600);
            	 response.addCookie(pIdCookie);
            	 String [] pID = {rawemail};
            	 //set the session
            	 ArrayList<userObj> mUser = userOperator.getUserName(pID);
            	 if(mUser.size() == 1)
            	 {
            		 HttpSession pPersonInfo = request.getSession();
                	 pPersonInfo.setAttribute("pID", rawemail);
                	 pPersonInfo.setAttribute("pName", mUser.get(0).getName());
                	 
                	 response.sendRedirect("commtaskshow.jsp");
            	 }
            	 else
            	 {
            		 response.sendRedirect("nonmember.jsp");
            	 }
            	 
             }else{
            	 response.sendRedirect("loginfault2.jsp");
             }
		 }
		 catch(Exception e){
			 
			 e.printStackTrace();
		 }
		
	}
	
	
}
