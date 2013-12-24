<%@page import="model.userObj"%>
<%@page import="model.xml_user_ope"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.usersetBean"%>
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
else if (request.getParameter("pesid") == null)
{
	response.sendRedirect("adminPerSList.jsp");
	return;
}

request.setAttribute("currentIndex", "3"); 
%>
<%@ include file="conf.jsp"%>
<%
String psid = request.getParameter("pesid");
if(psid == null)
	response.sendRedirect("adminPerSList.jsp");
if(Tool.isEmp(psid))
	response.sendRedirect("adminPerSList.jsp");

xml_user_ope muser = new xml_user_ope();
usersetBean mPESet = muser.getPerSerInfoWithID(psid);
if(mPESet == null)
	response.sendRedirect("adminPerSList.jsp");

%>
<%request.setAttribute("currentIndex", "4"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员集信息</title>
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
		<div id="content" style="height: 0px;">
		<input type="hidden" value="<%=(psid) %>" id="ps_personsetID" />
		<center><h2>更新人员集信息</h2></center>
			<div id="pictureset" style="float:left;width: 100%">
				<table style="width: 100%; margin-bottom: 0px;margin-left: 15px;">
					<tr>
						<td style="width: 10%">标题:</td>
						<td style="width: 90%"><input id="pers_tittle" type="text"
							style="width: 500px" value="<%=(mPESet.getTittle()) %>" ></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">描述:</td>
						<td><textarea
								style="overflow: auto; width: 500px; height: 120px;"
								id="pers_desp"><%=(mPESet.getDesp()) %></textarea></td>
					</tr>
					<tr>
						<td style="width: 10%">创建时间:</td>
						<td style="width: 90%"><input id="ps_time" type="text"
							style="width: 500px" value="<%=(mPESet.getTime()) %>" disabled="disabled"></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">作者:</td>
						<td style="width: 90%"><input id="ps_author" type="text"
							style="width: 500px" value="<%=(mPESet.getAuthorName()) %>" disabled="disabled"></td>
						</td>
					</tr>
					<tr>
						<td style="width: 10%">人员集的内容:</td>
					</tr>
				</table>
			</div>
			<div class="createPersonList">
					<center>人员列表</center>
					<hr />
					<table>
						<%
							//output the userlist information
							ArrayList<userObj> mUseList = mPESet.getUserList();
							for (short i = 0; i < mUseList.size(); ++i) {
						%>
						<div class="personDiv">
							<input type="checkbox" id="pl_<%=(mUseList.get(i).getID())%>"
								style="height: 30px; width: 20px;" checked onclick="perListClick(this.id)">
							<%
								out.print(mUseList.get(i).getName());
							%>
						</div>
						<script type="text/javascript">
						perListClick('<%=(mUseList.get(i).getID())%>');
						</script>
						<%
							}
							ArrayList<userObj> mLeaveList = muser.getleaveusersuserset(mPESet);
							for (short i = 0; i < mLeaveList.size(); ++i) {
								%>
								<div class="personDiv">
									<input type="checkbox" id="pl_<%=(mLeaveList.get(i).getID())%>"
										style="height: 30px; width: 20px;" onclick="perListClick(this.id)">
									<%
										out.print(mLeaveList.get(i).getName());
									%>
								</div>
								<%
							}
						%>
					</table>
					<input type="button" value="返回" class="button"
						onclick="javascript :history.back(-1);"></input>
					<input type="button" value="提交修改" class="button"
						onclick="updatePerSet()"></input>
				</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>