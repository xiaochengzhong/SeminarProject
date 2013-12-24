<%@page import="model.reltsBean"%>
<%@page import="model.xml_taskset_ope"%>
<%@page import="model.tasksetobj"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.ArrayList"
	pageEncoding="UTF-8"%>
<%@ include file="adminpermission.jsp"%>
<%
if(userName == null || userID == null)
	response.sendRedirect("login.jsp");

request.setAttribute("currentIndex", "1");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>任务展示</title>
</head>
<body>
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
<body class="left-sidebar">
	<div id="wrapper">
		<div id="content" style="height: 0px;">
<div class="tscontent" >
	<div style="margin-top: 30px;">
	<input type="button" value="添加任务集" class="button" onclick="window.location.href='adminTSCre.jsp'" style=" height: 33px;"></input>
	</div>
		<%
		boolean hasUnrelease = false;
		xml_taskset_ope mtsOpe = new xml_taskset_ope();
		ArrayList<tasksetobj> mData = mtsOpe.getUnReleaseTS();
		if(mData.size() == 0)            //没有当前的记录
			;
		else    //显示未发布的图像集的内容
		{
			hasUnrelease = true;
			%>
			<div id="unreleasets" class="unreleasets">
			<center>尚未发布的任务集</center>
			<hr/>
			<%
			for(short i = 0 ; i < mData.size() ; ++i)
			{
				%>
				<div class="comtasksetshow" style="position: relative; width: 280px; height: 120px; max-width: 300px; display: inline; float: left; margin-left: 5px; margin-top: 10px; border: 3px solid #C1D1E9;">
			<table>
				<tr>
				<td>发布者：</td>
				<td><%=(mData.get(i).getTSAuthor()) %></td>
				</tr>
				<tr>
				<td>发布的时间：</td>
				<td><%=(mData.get(i).getTSTime()) %></td>
				</tr>
				<tr>
				<td>标题：</td>
				<td><a class="tooltipsy"
					title="<%=(mData.get(i).getDescription()) %>"> <%=(mData.get(i).getTittle()) %>
				</a></td>
				</tr>
				<tr>
				<td><input type="button" id="<%=(mData.get(i).getTSID()) %>"
					value="详细信息"
					onclick="window.location.href='adminTSView.jsp?tsid=<%=(mData.get(i).getTSID()) %>'" /></td>
				<td><input type="button" id="<%=(mData.get(i).getTSID()) %>"
					value="修改信息"
					onclick="window.location.href='adminTSUpdate.jsp?tsid=<%=(mData.get(i).getTSID()) %>'" />
					<input type="button" id="<%=(mData.get(i).getTSID()) %>" value="发布"
					onclick="window.location.href='adminTSRel.jsp?tsid=<%=(mData.get(i).getTSID()) %>'" />
					</td>
				</tr>
			</table>
		</div>
				<%
			}
			%>
			</div>
			<%
		}
		%>
		</div>
		<div class="alltsshow">
		<center>已发布的任务集</center>
			<hr/>
		<%
		
		//展示所有已发布的taskset的ID
		ArrayList<reltsBean> m_data = mtsOpe.getRelTSInfo();
		for(short i = 0 ; i < m_data.size() ; ++i)
		{
			%>
			<div class="comtasksetshow" style="position: relative; width: 280px; height: 120px; max-width: 300px; display: inline; float: left; margin-left: 5px; margin-top: 10px; border: 3px solid #C1D1E9;">
		<table>
			<tr>
			<td>发布者：</td>
			<td><%=(m_data.get(i).getM_authorName()) %></td>
			</tr>
			<tr>
			<td>发布的时间：</td>
			<td><%=(m_data.get(i).getM_time()) %></td>
			</tr>
			<tr>
			<td>标题：</td>
			<td><a class="tooltipsy"
				title="<%=(m_data.get(i).getM_tsDesp()) %>"> <%=(m_data.get(i).getM_tsTittle()) %>
			</a></td>
			</tr>
			<tr>
			<td><input type="button" id="<%=(m_data.get(i).getM_tsID()) %>"
				value="详细信息"
				onclick="window.location.href='adminTSView.jsp?tsid=<%=(m_data.get(i).getM_tsID()) %>'" />
				<input type="button" id="<%=(m_data.get(i).getM_tsID()) %>" value="数据"
				onclick="window.location.href='adminTSDataShow.jsp?relTSID=<%=(m_data.get(i).getM_id()) %>'" />
				</td>
			<td><input type="button" id="<%=(m_data.get(i).getM_tsID()) %>"
				value="修改信息"
				onclick="window.location.href='adminTSUpdate.jsp?tsid=<%=(m_data.get(i).getM_tsID()) %>'" />
				<input type="button" id="<%=(m_data.get(i).getM_tsID()) %>" value="发布"
				onclick="window.location.href='adminTSRel.jsp?tsid=<%=(m_data.get(i).getM_tsID()) %>'" />
				</td>
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