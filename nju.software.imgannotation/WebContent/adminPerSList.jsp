<%@page import="model.xml_user_ope"%>
<%@page import="model.usersetBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="adminpermission.jsp"%>
<%
	request.setAttribute("currentIndex", "3");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员集列表</title>
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
<body class="left-sidebar">
	<div id="wrapper">
		<div id="content" style="height: 0px;">
			<script src="js/tooltipsy.min.js"></script>
			<div class="pscontent">
				<div style="margin-top: 30px;">
					<input type="button" value="添加人员集" class="button"
						onclick="window.location.href='adminPerSCre.jsp'"
						style="height: 33px;"></input>
				</div>
				<div style="background-color: #99A5A2; height: 900px;">
					<center>
						<h3>已有人员集</h3>
					</center>
					<%
						xml_user_ope muser = new xml_user_ope();
						ArrayList<usersetBean> mpsList = muser.getAllPerSetFDB();
						for (short i = 0; i < mpsList.size(); ++i) {
					%>
					<div class="comtasksetshow"
						style="position: relative; width: 280px; height: 120px; max-width: 300px; display: inline; float: left; margin-left: 5px; margin-top: 10px; border: 3px solid #C1D1E9;">
						<table>
							<tr>
								<td>发布者：</td>
								<td><%=(mpsList.get(i).getAuthorName())%></td>
							</tr>
							<tr>
								<td>发布的时间：</td>
								<td><%=(mpsList.get(i).getTime())%></td>
							</tr>
							<tr>
								<td>标题：</td>
								<td><a class="tooltipsy"
									title="<%=(mpsList.get(i).getDesp()) %>"> <%=(mpsList.get(i).getTittle())%>
								</a></td>
							</tr>
							<tr>

								<td><input type="button"
									id="<%=(mpsList.get(i).getID())%>" value="详细信息"
									onclick="window.location.href='adminPerSView.jsp?pesid=<%=(mpsList.get(i).getID())%>'" /></td>
								<td><input type="button"
									id="<%=(mpsList.get(i).getID())%>" value="修改信息"
									onclick="window.location.href='adminPerSUpdate.jsp?pesid=<%=(mpsList.get(i).getID())%>'" /></td>
							</tr>
						</table>
					</div>
					<%
						}
					%>

					<script type="text/javascript">
$('#tooltipsy').tooltipsy();
</script>
				</div>
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>