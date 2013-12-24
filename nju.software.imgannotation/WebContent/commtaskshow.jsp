<%@page import="model.reltsBean"%>
<%@page import="model.xml_user_ope"%>
<%@page import="model.tasksetobj"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
//get the session
HttpSession pSession = request.getSession(true);
String userName = (String)pSession.getAttribute("pName");
String userID = (String)pSession.getAttribute("pID");
if(userName == null || userID == null)
	response.sendRedirect("login.jsp");

String path = request.getContextPath();
String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	request.setCharacterEncoding("utf-8"); //set the post character encoding
	response.setCharacterEncoding("utf-8");
	String webPath = request.getSession().getServletContext()
			.getRealPath("/");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>任务查看</title>
<link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="css/buttons.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/buttons.js"></script>
<script type="javascript" src="js/bootstrap-sortable.js"></script>
<link href="css/commtaskshow.css" type="text/css"  rel="stylesheet"/>
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
									<a onclick="window.location.href='exitSys'" href="">退出</a></div>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
<div class="body-div container">
<div class="commUndo">
	<h2>尚未完成的任务集</h2>
    <table id="queryTable" class="table table-striped table-bordered table-hover sortable">
		<thead>
		<tr>
		<th id="query-item-th">发布者</th>
		<th id="query-item-th">发布时间</th>
		<th id="query-item-th">任务状态</th>
		<th id="query-item-th">操作</th>
		</tr>
		</thead>
		<tbody id="queryBody">
		<%
 	xml_user_ope mDataInfo = new xml_user_ope();
 	ArrayList<reltsBean> mTSinfo = mDataInfo.getAllTSList(userID);
 	for (short i = 0; i < mTSinfo.size(); ++i) {
		if (mTSinfo.get(mTSinfo.size() - 1 - i).getM_status()
				.contains("0")) {
 		%>
		<tr id="data-td">
		<td id="data-td"><%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_authorName())%></td>
		<td id="data-td"><%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_time())%></td>
		<td id="data-td"><% out.print("未完成");%></td>
		<td id="data-td">
		<input class="button glow button-rounded button-flat" type="button" 
		id="<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id())%>" value="做任务"
		onclick="window.location.href='commdealtask.jsp?relTSID=<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id()) %>'" />
		<% if(mTSinfo.get(mTSinfo.size() - 1 - i).get_canSubmmit().contentEquals("0")){%>
			<input class="button glow button-rounded button-flat-primary" type="button" value="提交任务" onclick="alert('请先浏览所有的图片')"/>
		<% }else{%>
			<input type="button" class="button glow button-rounded button-flat-primary"
			id="<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id()) %>" value="提交任务" 
			onclick="window.location.href='submitTask?tasksetid=<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id())%>&userID=<%=(userID) %>'"/>
		<% }%>	
		</td>
		</tr>
		<%
		}
	}
	%>
		</tbody>
	</table>
</div>
<div class="commAlldo">
	<h2>已完成的任务集</h2>
    <table id="queryTable" class="table table-striped table-bordered table-hover sortable">
		<thead>
		<tr>
		<th id="query-item-th">发布者</th>
		<th id="query-item-th">发布时间</th>
		<th id="query-item-th">任务状态</th>
		<th id="query-item-th">操作</th>
		</tr>
		</thead>
		<tbody id="queryBody">
		<%
		for (short i = 0; i < mTSinfo.size(); ++i) {
			if (!mTSinfo.get(mTSinfo.size() - 1 - i).getM_status()
					.contains("0")) {
 		%>
		<tr id="data-td">
		<td id="data-td"><%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_authorName())%></td>
		<td id="data-td"><%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_time())%></td>
		<td id="data-td"><% out.print("已完成");%></td>
		<td id="data-td">
		<input class="button glow button-rounded button-flat-primary" type="button" 
		id="<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id())%>" value="查看任务"
		onclick="window.location.href='commtaskview.jsp?tasksetid=<%=(mTSinfo.get(mTSinfo.size() - 1 - i).getM_id())%>'" />
		</td>
		</tr>
		<%
		}
	}
	%>
		</tbody>
	</table>
</div>
</div>
</body>
</html>