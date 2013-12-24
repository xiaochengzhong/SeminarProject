
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
//get the session
HttpSession pSession = request.getSession(true);
String userName = (String)pSession.getAttribute("pName");
String userID = (String)pSession.getAttribute("pID");
if(userName == null || userID == null)
	response.sendRedirect("login.jsp");
String[] relTSIDArr =request.getParameterValues("relTSID");
if(relTSIDArr == null)
{
	response.sendRedirect("commtaskshow.jsp");
	return;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>正在做任务</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script src="js/imageannotation.js"></script>
<script src="js/config.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/jscroll.js"></script>
<script src="js/useroperator.js"></script>
<link rel="stylesheet" href="css/skel-noscript.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/style-desktop.css" />
<link rel="stylesheet" href="css/style-wide.css" />
<link rel="stylesheet" href="css/style-admin.css">
</head>
<body class="left-sidebar">
	<input type="hidden" value="<%=(userName) %>" id="user_name" />
	<input type="hidden" value="<%=(userID) %>" id="user_id" />
	<div id="wrapper">
		<div id="content">
			<div id="content-inner">
				<article class="is-post is-post-excerpt">
				<div>
					<jsp:include flush="true" page="imgPanel.jsp"></jsp:include>
				</div>

				<div class="taskShow">
					<jsp:include flush="true" page="commtaskTools.jsp"></jsp:include>
				</div>
			</div>
			</article>

		</div>
		</article>
	</div>
	</div>
	<jsp:include flush="true" page="comleft.jsp"></jsp:include>
	</div>
</body>
</html>