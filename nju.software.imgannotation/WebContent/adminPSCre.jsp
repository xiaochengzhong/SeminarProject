<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="adminpermission.jsp"%>
<%
request.setAttribute("currentIndex", "2"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>创建图像集</title>
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
		<center><h2>生成图像集</h2></center>
			<div id="pictureset" style="float:left;width: 100%">
				<jsp:include flush="true" page="adminPStools.jsp"></jsp:include>
			</div>
			<div id="FVContent" class="fileMenuList">
				<jsp:include flush="true" page="adminfileshow.jsp"></jsp:include>
			</div>
			<div class="fileMenuReview">
			<div id="ps_review" style="float:left;width:100%">
				<h3><center>已选文件/文件夹</center></h3>
				<hr/>
			</div>
			<div style="float:left">
			<hr/>
			<input type="button" value="返回" class="button" onclick="javascript :history.back(-1);"></input>
			<input type="button" value="提交图像集" class="button" onclick="postPSToserver()"></input>
			</div>
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>