<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="conf.jsp"%>
<%
	String file = new String(request.getParameter("file").getBytes(
			"iso-8859-1"), "GBK");
	file = FileUtil.replFileToWebPath(file);
%>
<html>
<head>
<title>Õº∆¨‘§¿¿</title>
<link href="skin_1/style.css" type="text/css" rel="stylesheet" />
<script src="jquery.js"></script>
</head>
<body>
<img id="canvas" src="<%=(webRoot + file) %>" style="border:1px solid #999;position:absolute;top:50px;left:50px;"></img>
</body>
</html>