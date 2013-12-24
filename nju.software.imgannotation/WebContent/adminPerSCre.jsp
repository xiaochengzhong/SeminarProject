<%@page import="model.userObj"%>
<%@page import="model.userOperator"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="adminpermission.jsp"%>
<%
	request.setAttribute("currentIndex", "3");
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />

<link rel="stylesheet" href="css/skel-noscript.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/style-desktop.css" />
<link rel="stylesheet" href="css/style-wide.css" />
<link rel="stylesheet" href="css/style-admin.css">

<script src="js/jquery.js"></script>
<script src="js/config.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/admin.js"></script>
<script src="js/jscroll.js"></script>
</head>
<%
	ArrayList<userObj> mUserData = userOperator.getAllUserInfo();
%>

<body class="left-sidebar">
	<div id="wrapper">
		<div id="content">
			<center>
				<h2>生成人员集</h2>
			</center>
			<div id="personset" style="float: left; width: 100%">
				<table style="width: 100%; margin-bottom: 0px;">
					<tr>
						<td style="width: 10%">标题:</td>
						<td style="width: 90%"><input id="pers_tittle" type="text"
							style="width: 500px"></td>
					</tr>
					<tr>
						<td style="width: 10%">描述:</td>
						<td><textarea
								style="overflow: auto; width: 500px; height: 120px;"
								id="pers_desp"></textarea></td>
					</tr>
					<tr>
						<td style="width: 10%">人员集内容:</td>
					</tr>
				</table>
				<div class="createPersonList">
					<center>人员列表</center>
					<hr />
					<table>
						<%
							//output the user information
							for (short i = 0; i < mUserData.size(); ++i) {
						%>
						<div class="personDiv">
							<input type="checkbox" id="pl_<%=(mUserData.get(i).getID())%>"
								onclick="perListClick(this.id)"
								style="height: 30px; width: 20px;">
							<%
								out.print(mUserData.get(i).getName());
							%>
						</div>
						<%
							}
						%>
					</table>
					<input type="button" value="生成人员集  " onClick="commintperlist()" class="button" style="height: 25px;"/>
				</div>
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>