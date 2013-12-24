<%@page import="model.userOperator"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	HttpSession pSession = request.getSession(true);
	String permission = (String) pSession.getAttribute("pPermission");
	String userID = (String) pSession.getAttribute("pID");
	String userName = (String) pSession.getAttribute("pName");
	if (userName == null || userID == null) {
		response.sendRedirect("login.jsp");
		return;
	}
	boolean mAccess = false;
	if (permission == null) {
		mAccess = userOperator.judgePermission(userID);
		if (mAccess) {
			//pSession.setAttribute("pPermission", "true");
		} else {
%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="css/bootstrap-responsive.min.css" type="text/css"
	rel="stylesheet" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style type="text/css">
body {
	margin-top: 100px;
	padding: 40px 0px;
	background: #f6f7f1 url(images/login-bg.png) repeat 0 0;
}
</style>
<title>非项目成员</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<div class="hero-unit center">
					<h1>
						权限不足 <small><font face="Tahoma" color="red"></font></small>
					</h1>
					<br />
					<p>对不起，您不是该研讨小组的成员，暂无权限进入.</p>
					<p>
						<b>如果想登录本系统，请联系本研讨小组负责老师!</b>
					</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<%
	//out.println("对不起，您没有权限进入管理员权限，如有问题，请联系老师！");
			pSession.setAttribute("pPermission", "false");
			return;
		}
	} else {
		mAccess = permission.contentEquals("true");
		if (!mAccess) {
%>

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="css/bootstrap-responsive.min.css" type="text/css"
	rel="stylesheet" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style type="text/css">
body {
	margin-top: 100px;
	padding: 40px 0px;
	background: #f6f7f1 url(images/login-bg.png) repeat 0 0;
}
</style>
<title>非项目成员</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<div class="hero-unit center">
					<h1>
						权限不足 <small><font face="Tahoma" color="red"></font></small>
					</h1>
					<br />
					<p>对不起，您不是该研讨小组的成员，暂无权限进入.</p>
					<p>
						<b>如果想登录本系统，请联系本研讨小组负责老师!</b>
					</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<%
	//out.println("对不起，您没有权限进入管理员权限，如有问题，请联系老师！");
			pSession.setAttribute("pPermission", "false");
			return;
		}
	}
%>