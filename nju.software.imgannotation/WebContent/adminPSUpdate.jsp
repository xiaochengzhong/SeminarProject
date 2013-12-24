<%@page import="model.stringOperator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.picSetObj"%>
<%@page import="model.xml_picture_ope"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="adminpermission.jsp"%>
<%
if(userName == null || userID == null)
{
	response.sendRedirect("login.jsp");
	return;
}
else if (request.getParameter("psid") == null)
{
	response.sendRedirect("adminPSList.jsp");
	return;
}

request.setAttribute("currentIndex", "2"); 
%>
<%@ include file="conf.jsp"%>
<%
String psid = request.getParameter("psid");
if(psid == null)
	response.sendRedirect("adminPSList.jsp");
if(Tool.isEmp(psid))
	response.sendRedirect("adminPSList.jsp");
xml_picture_ope mpic = new xml_picture_ope();
picSetObj mpicset = mpic.getPicsetInfo(psid);
if(mpicset == null)
	response.sendRedirect("adminPSList.jsp");
ArrayList<String> folderls = mpicset.getPicPath();
String timeId = stringOperator.timeFormat();
%>
<%request.setAttribute("currentIndex", "4"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>图像集更新</title>
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
		<center><h2>更新图像集</h2></center>
		<input type="hidden" value="<%=(psid) %>" id="pictureset_id" />
			<div id="pictureset" style="float:left;width: 100%">
					<table style="width: 100%; margin-bottom: 0px;">
					<tr>
						<td style="width: 10%">标题:</td>
						<td style="width: 90%"><input id="ps_tittle" type="text"
							style="width: 500px" value="<%=(mpicset.getTittle()) %>"></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">描述:</td>
						<td><textarea
								style="overflow: auto; width: 500px; height: 120px;"
								id="ps_desp"><%=(mpicset.getdesp()) %></textarea></td>
					</tr>
					<tr>
						<td style="width: 10%">创建时间:</td>
						<td style="width: 90%"><input id="ps_time" type="text"
							style="width: 500px" value="<%=(mpicset.getTime()) %>" disabled="disabled"></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">作者:</td>
						<td style="width: 90%"><input id="ps_author" type="text"
							style="width: 500px" value="<%=(mpicset.getAuthorName()) %>" disabled="disabled"></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">图像集的内容:</td>
					</tr>
				</table>
			</div>
			<div id="FVContent" class="fileMenuList">
				<jsp:include flush="true" page="adminfileshow.jsp"></jsp:include>
			</div>
			<div class="fileMenuReview">
				<div id="ps_review" style="float: left; width: 100%">
					<h3>
						<center>已选文件/文件夹</center>
					</h3>
					<hr />
					<%
					for(short i = 0 ; i < folderls.size() ; ++i)
					{
					%>
					<div id="con_<%=(String.valueOf(i)+timeId) %>">
						<div id="ps_<%=(String.valueOf(i)+timeId) %>" class="togetherdelete">
							<input type="button" value="Delete"
								onclick="deleteDiv('<%=(String.valueOf(i)+timeId) %>')">
							<iframe id="tmp_downloadhelper_iframe" style="display: none;"></iframe>
						</div>
						<div id="ps_content" class="togethercontent"
							style="margin-left: 0px;">
							<a onclick="getFilelist('<%=(folderls.get(i)) %>')" class="filefolder">/<%=(folderls.get(i)) %></a>
						</div>
					</div>
					<script type="text/javascript">
					if(!dealReLoad("<%=(folderls.get(i)) %>"))
					{
						m_picsetArray.push(new picPosition("<%=(folderls.get(i)) %>", "<%=(String.valueOf(i)+timeId) %>"));
					}
					</script>
					<%
					}
					%>
				</div>
				<div style="float: left">
					<hr />
					<input type="button" value="返回" class="button" onclick="javascript :history.back(-1);"></input>
					<input type="button" value="更新图像集" class="button" onclick="updatePictureset()"></input>
				</div>
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>