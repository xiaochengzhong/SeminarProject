
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="adminpermission.jsp"%>
<%
if(userName == null || userID == null)
{
	response.sendRedirect("login.jsp");
	return;
}
else if(request.getParameterValues("relTSID") == null)
{
	response.sendRedirect("adminTSList.jsp");
	return;
}

request.setAttribute("currentIndex", "1");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>查看任务用户数据</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script src="js/config.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/jscroll.js"></script>
<script src="js/admin-imgdatatools.js"></script>

<link rel="stylesheet" href="css/skel-noscript.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/style-desktop.css" />
<link rel="stylesheet" href="css/style-wide.css" />
<link rel="stylesheet" href="css/style-admin.css">
</head>
<body class="left-sidebar">
	<input type="hidden" value="joke" id="user_name" />
	<input type="hidden" value="1" id="user_id" />
	<div id="wrapper">
		<div id="content">
			<div id="content-inner">
				<article class="is-post is-post-excerpt">
				<div>
					<jsp:include flush="true" page="imgPanel.jsp"></jsp:include>
				</div>

				<div class="personls">
					<jsp:include flush="true" page="adminTSDataShowPls.jsp"></jsp:include>
				</div>

				<div class="admintsShow">
						<jsp:include flush="true" page="adminTSDataShowtools.jsp"></jsp:include>
				</div>
				</article>
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>