
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
<link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
<link href="css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="css/buttons.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/buttons.js"></script>
<script type="javascript" src="js/bootstrap-sortable.js"></script>
<link rel="stylesheet" href="css/commdealtask.css" />
</head>
<body class="left-sidebar">
<div class="container" style="height:65px;">
		<div class="navbar-wrapper">
			<div class="navbar navbar-inverse">
				<div class="navbar-inner">
					<div class="container">
						<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</a>
						<a class="brand" href="#">图像标注工具</a>
						<div class="nav-collapse collapse">
							<ul class="nav">
								<li class="active">
									<a href="commtaskshow.jsp">查看任务</a>
								</li>
								<li>
									<a>个人设置</a>
								</li>
								<li>
									<a>问题反馈</a>
								</li>
								<div id="username">欢迎，<%=userName %>
									<a href="login.jsp">退出</a></div>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
	<div class="body-div container">
	<input type="hidden" value="<%=(userName) %>" id="user_name" />
	<input type="hidden" value="<%=(userID) %>" id="user_id" />
	<div id="wrapper">
		<div id="content">
			<div id="content-inner">
				<div>
					<jsp:include flush="true" page="imgPanel.jsp"></jsp:include>
				</div>

				<div class="taskShow">
					<jsp:include flush="true" page="commtaskTools.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>