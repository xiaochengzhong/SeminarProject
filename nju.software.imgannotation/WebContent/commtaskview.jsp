<%@page import="model.*,java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>查看任务</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script src="js/imageannotation.js"></script>
<script src="js/config.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/jscroll.js"></script>
<script src="js/useroperator.js"></script>
<link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
<link href="css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="css/buttons.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/buttons.js"></script>
<script type="javascript" src="js/bootstrap-sortable.js"></script>
<link rel="stylesheet" href="css/commtaskview.css" />
</head>
<%
//get the session
HttpSession pSession = request.getSession(true);
String userName = (String)pSession.getAttribute("pName");
String userID = (String)pSession.getAttribute("pID");
if(userName == null || userID == null)
	response.sendRedirect("login.jsp");

if( request.getParameter("tasksetid") == null)
{
	response.sendRedirect("commtaskshow.jsp");           //转登陆
	return;
}
String relTSID = request.getParameterValues("tasksetid")[0];          //设置穿过来的picture-set的ID唯一
%>
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
									<a href="login.jsp">退出</a></div>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
	<div class="body-div container">
	<input type="hidden" value="<%=(userName) %>" id="user_name" />
	<input type="hidden" value="<%=(userID) %>" id="user_id" />
	<div id="wrapper">
		<div id="content">
			<div id="content-inner">
				<div>
					<jsp:include flush="true" page="imgPanel.jsp"></jsp:include>
				</div>
				<div class="taskShow">
				<%
				xml_taskset_ope mTSinfo = new xml_taskset_ope();
				xml_user_ope m_data = new xml_user_ope();
				
				reltsBean mRelTSID = mTSinfo.getRelTSInfo(relTSID);
				picObj currentInfo = m_data.getCuPicObj(mRelTSID.getM_id(), userID, mRelTSID.getM_PSIDString());
				mRelTSID.setM_tsInfo(mTSinfo.getTaskSet(mRelTSID.getM_tsID()));
				tasksetobj mTaskSetData = mRelTSID.getM_tsInfo();
				ArrayList<taskObj> tempmydate = mTaskSetData.getTSTask();
				%>
<input id="firstPic" value="<%=(request.getContextPath() + "/"+ currentInfo.getPath()) %>" type="hidden">
<script type="text/javascript">
	var filePath = $("#firstPic").attr("value");
	var spa = /\040/g;
	filePath = filePath.replace(spa, "");
	$("#picShow").attr("src", filePath);
</script>

<div class="addthese" id="tasklistShow">
	<h3>任务列表</h3>
	<input type="hidden" value="<%=(relTSID) %>" id="current_taskset_ID" />
	<input type="hidden" value="<%=(currentInfo.getID()) %>" id="current_pic_ID" />
	<input type="hidden" value="<%=(currentInfo.getPSID()) %>" id="current_pics_ID"/>
	<!-- 动态变化的 -->
	<ul id="task_list_size" class="<%=tempmydate.size()%>">
		<% for(short i = 0 ; i < tempmydate.size() ; ++i){ %>
		<li style="margin-bottom: 10px;"><input type="radio"  disabled="disabled" name="check" 
		style="margin-top: 0px;margin-right: 20px; margin-left: 5px;"></input>
					<input type="button" 
					class="button glow button-rounded button-flat-highlight" 
					id="<%=((tempmydate.get(i)).getTaskID()) %>" value="清空" 
					style="position:relative;" onclick="cleanSelectArray(this.id)" disabled="disabled">
			<span id="shape_span_<%=(tempmydate.get(i)).getTaskID() %>"
			class="<% switch(Integer.parseInt(tempmydate.get(i).getShapeID())){
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
			<input id="color_span_<%=(tempmydate.get(i)).getTaskID() %>"
				style="text-align: center;
				 border: 1px solid #cccccc;
				-moz-border-radius: 4px;
				-webkit-border-radius: 4px;
				border-radius: 4px;
				-webkit-box-shadow: 1px 1px 1px rgba(0,0,0,0.1);
				-moz-box-shadow: 1px 1px 1px rgba(0,0,0,0.1);
				box-shadow: 1px 1px 1px rgba(0,0,0,0.1);
			 background-color: <%=((tempmydate.get(i)).getLineCol()) %>; width: 20px; height: 20px; margin-top: 5px; display: block; float: right; margin-right: 20px;"
			value="<%=((tempmydate.get(i)).getLineWidth()) %>"
			readonly="readonly" class="<%=tempmydate.get(i).getShapeID() %>"></input>
		</li>
		<% } %>
	</ul>
</div>
<div class="btn-arr">
<input type="button" value="下一张" class="button glow button-rounded button-flat-primary" onclick="nextPicture(0)"></input>
	</div>
	</div>
</div>
	</div>
	</div>
	</div>
</body>
</html>