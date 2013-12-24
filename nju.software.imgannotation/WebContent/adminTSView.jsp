<%@page import="model.taskObj"%>
<%@page import="model.xml_taskset_ope"%>
<%@page import="model.tasksetobj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.picSetObj"%>
<%@page import="model.xml_picture_ope"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="conf.jsp"%>
<%@ include file="adminpermission.jsp"%>
<%
if(userName == null || userID == null)
{
	response.sendRedirect("login.jsp");
	return;
}
else if (request.getParameter("tsid") == null)
{
	response.sendRedirect("adminTSList.jsp");
	return;
}

String[] tsid = request.getParameterValues("tsid");
if(tsid == null)
	response.sendRedirect("adminTSList.jsp");

xml_taskset_ope mtsOpe = new xml_taskset_ope();
tasksetobj mtaskset = null;
mtaskset = mtsOpe.getTaskSet(tsid[0]);
ArrayList<taskObj> mTask= mtaskset.getTSTask();

%>
<%request.setAttribute("currentIndex", "1"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>图像集信息</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />

<link rel="stylesheet" href="css/skel-noscript.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/style-desktop.css" />
<link rel="stylesheet" href="css/style-wide.css" />
<link rel="stylesheet" href="css/style-admin.css">
<link rel="stylesheet" href="css/skin_1/style.css">

<script src="js/jquery.js"></script>
<script src="js/config.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/jscroll.js"></script>
<script src="js/admin.js"></script>
</head>
<body class="left-sidebar">
	<div id="wrapper">
		<div id="content">
		<center><h2>当前任务集集信息</h2></center>
		<div id="tasksetInfo">
		<table style="margin-bottom: 2px;">
		<tr><td>发布时间：</td><td><%=(mtaskset.getTSTime()) %></td></tr>
		<tr><td>发布者：</td><td><%=(mtaskset.getTSAuthor()) %></td></tr>
		<tr><td>任务集标题：</td><td><%=(mtaskset.getTittle()) %></td></tr>
		<tr><td>任务集描述：</td><td></td></tr>
		</table>
		<div><%=(mtaskset.getDescription()) %></div>
		</div>
		<div id="btnContent" style="float:left;width:100%;display:block">
		<input type="button" value="返回" class="button"
						onclick="javascript :history.back(-1);"></input>
		<input type="button" value="更新任务集信息" class="button"
						onclick="window.location.href='adminTSUpdate.jsp?tsid=<%=(mtaskset.getTSID()) %>'"></input>
		</div>
		<div id="detailContent" style="float:left;width:100%;display:block">
		<%
		for(short i = 0 ; i < mTask.size() ; ++i)
		{
		%>
			<div id="tasksetContent" style="float:left;width:600px;display:inline">
			
			<table style="width: 100%; margin-bottom: 0px;">
				<tr>
					<td style="width: 5%"><%=(i+1) %>: </td>
					<td style="width: 10%">名称:</td>
					<td style="width: 85%"><input id="ts_tittle" type="text"
						style="width: 500px" value="<%=(mTask.get(i).getTaskName()) %>" disabled="disabled"></td>
				</tr>
				<tr>
					<td></td>
					<td style="width: 10%">描述:</td>
					<td><textarea
							style="overflow: auto; width: 500px; height: 60px;"
							id="ps_desp" disabled="disabled"><%=(mTask.get(i).getDescription()) %></textarea></td>
				</tr>
				<tr>
					<td></td>
					<td>工具：</td>
					<td><span style="padding-left: 50px;" id="shape_span_<%=(mTask.get(i).getTaskID()) %>"
					class="<% switch(Integer.parseInt((mTask.get(i).getShapeID()))){
					case 1:
						out.println("pencil");
						break;
					case 2:
						out.println("line");
						break;
					case 3:
						out.println("circle");
						break;
					case 4:
						out.println("rect");
						break;
					case 5:
						out.println("multipoint");
						break;
					case 6:
						out.println("irregular");
						break;
						default:
							break;
					} %>"></span>
					<input id="color_span_<%=(mTask.get(i).getTaskID()) %>"
					style="border-right-width: 10px; border: 1px solid #999; background-color: <%=((mTask.get(i)).getLineCol()) %>; width: 15px; height: 17px; margin-top: 10px; display: inline; float: left margin-left: 10px; padding-left: 0px; padding-right: 0px; margin-right: 20px;"
					value="<%=((mTask.get(i)).getLineWidth()) %>"
					readonly="readonly" class="<%=mTask.get(i).getShapeID() %>"></input></td>
				</tr>
			</table>
		</div>
		<%	
		}
		%>
		</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>