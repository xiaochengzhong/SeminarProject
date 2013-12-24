<%@page import="model.picOperator"%>
<%@page import="model.picSetObj"%>
<%@page import="model.xml_picture_ope"%>
<%@page import="model.xml_taskset_ope"%>
<%@page import="model.tasksetOperator"%>
<%@page import="model.tasksetobj"%>
<%@page import="model.taskObj"%>
<%@page import="model.userObj"%>
<%@page import="model.usersetBean"%>
<%@page import="model.userOperator"%>
<%@ page language="java" import="java.util.ArrayList"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

request.setAttribute("currentIndex", "1");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务发布</title>
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
<script src="js/tooltipsy.min.js"></script>
<style>

.cmmBtn {
	position: absolute;
	top: 0;
	right: 0;
}
</style>
<script type="text/javascript">
	function showMoreData(id) {
		{
			$("#" + id).toggle(function() {
				$(this).next(".tasksettext").animate({
					height : 'toggle',
					opacity : 'toggle'
				}, "1000");
			});
		}
		$(function() {
			$("#" + id).toggle(function() {
			});
		});
	}
</script>
</head>
<%
String mTaskID = request.getParameter("tsid");
xml_taskset_ope mOpTaskset_ope = new xml_taskset_ope();
if(mTaskID == null)
	response.sendRedirect("adminTSList.jsp");
tasksetobj mTSobj = mOpTaskset_ope.getTaskSet(mTaskID);

	ArrayList<userObj> mUserData = userOperator.getAllUserInfo();
	ArrayList<picSetObj> mPSData = picOperator.getAllPSInfo();
	ArrayList<usersetBean> mPESData = userOperator.getAllPersonList();
	xml_taskset_ope mOperator = new xml_taskset_ope();
	xml_picture_ope mPicSet = new xml_picture_ope();
	ArrayList<taskObj> mTask = null;
	String path = request.getSession().getServletContext()
			.getRealPath("/");
%>
<body class="left-sidebar">
	<div id="wrapper">
		<div id="content">
			<div class="cmmBtn">
				<input class="smbutton smbutton-size-default smbutton-shadow smbutton-blue" type="button" value="发布任务集" onclick="releaseToServer()" />
			</div>
			<div class="releaseTS">
				<center>当前为分配的任务</center>
				<hr />
				<%
					if (mTSobj == null)
						out.print("任务已经全部发布");
					else {
							mTask = mTSobj.getTSTask();
				%>
				<div class="tasksetbox">
					<input type="checkbox" id="ts_<%=(mTSobj.getTSID())%>" onclick="tasksetClick(this.id)" style="height: 30px;width: 20px;" class=“<%=(mTSobj.getTSID())%>>
					
					<a id="a_<%=(mTSobj.getTSID())%>" onClick="showMoreData(this.id)" class="tasksetboxa">收缩/展开</a>
					</input>
					<div class="tasksettext">
					<center><a class="tooltipsy" onclick="window.open('adminTSView.jsp?tsid=<%=(mTSobj.getTSID()) %>' , '_blank')" style="cursor: pointer" tittle="<%=(mTSobj.getDescription())%>"><%=("标题： " + mTSobj.getTittle()) %></center></a>
						<%
							//生成任务选项
									for (short j = 0; j < mTask.size(); ++j) {
										out.print(j+1);
						%>
						:
						<%=(mTask.get(j).getTaskName())%>
						<br />
						<%
							}
						%>
					</div>
				</div>
				<%
					}
				%>

			</div>
			<div class="releasePicS">
				<center>可用图片集</center>
				<hr />
				<%
				for(short i=0 ; i < mPSData.size() ; ++i)
				{
				%>
					<input type="checkbox" id="psl_<%=(mPSData.get(i).getID())%>" onclick="picSetSelClick(this.id)" style="height: 30px;width: 20px;">
				<a class="tooltipsy" onclick="window.open('adminPSView.jsp?psid=<%=(mPSData.get(i).getID()) %>' , '_blank')" style="cursor: pointer" tittle="<%=(mPSData.get(i).getdesp()) %>"><%=( mPSData.get(i).getTittle()) %></a>
				</input><br/>
				<%
				}
				
				%>
				
			</div>
			<div class="releasePerS">
				<center>人员列表</center>
				<hr />
				<%                    //output the user information
				for(short i = 0 ; i<mPESData.size() ; ++i)
				{
				%>
				<input type="checkbox" id="pl_<%=(mPESData.get(i).getID())%>" onclick="persetselClick(this.id)" style="height: 30px;width: 20px;">
				<a class="tooltipsy" onclick="window.open('adminPerSView.jsp?pesid=<%=(mPESData.get(i).getID()) %>' , '_blank')" style="cursor: pointer" tittle="<%=(mPESData.get(i).getDesp()) %>"><%=( mPESData.get(i).getTittle()) %></a>
				</input><br/>
				<%
				}
				%>
			</div>
		</div>

		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
<script type="text/javascript">
$('#tooltipsy').tooltipsy();
</script>
</body>
</html>