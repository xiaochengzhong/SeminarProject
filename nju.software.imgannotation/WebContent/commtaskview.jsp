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
<link rel="stylesheet" href="css/skel-noscript.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/style-desktop.css" />
<link rel="stylesheet" href="css/style-wide.css" />
<link rel="stylesheet" href="css/style-admin.css">
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
	<input type="hidden" value="<%=(userName) %>" id="user_name" />
	<input type="hidden" value="<%=(userID) %>" id="user_id" />
	<div id="wrapper">
		<div id="content">
			<div id="content-inner">
				<article class="is-post is-post-excerpt">
				<div>
					<jsp:include flush="true" page="imgPanel.jsp"></jsp:include>
				</div>

<!--  				<div class="picShow">
					<div class="picthese">
						<h3>Picture List</h3>
					</div>
				</div>-->

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
	<h3>Task List</h3>
	<input type="hidden" value="<%=(relTSID) %>" id="current_taskset_ID" />
	<input type="hidden" value="<%=(currentInfo.getID()) %>" id="current_pic_ID" />
	<input type="hidden" value="<%=(currentInfo.getPSID()) %>" id="current_pics_ID"/>
	<!-- 动态变化的 -->
	<ul id="task_list_size" class="<%=tempmydate.size()%>">
		<% for(short i = 0 ; i < tempmydate.size() ; ++i){ %>
		<li><input type="radio"  disabled="disabled" name="check"></input>
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
			style="border-right-width: 10px; border: 1px solid #999; background-color: <%=((tempmydate.get(i)).getLineCol()) %>; width: 15px; height: 17px; margin-top: 10px; display: block; float: right; margin-left: 10px; padding-left: 0px; padding-right: 0px; margin-right: 20px;"
			value="<%=((tempmydate.get(i)).getLineWidth()) %>"
			readonly="readonly" class="<%=tempmydate.get(i).getShapeID() %>"></input>
			<input type="button" id="<%=((tempmydate.get(i)).getTaskID()) %>" value="清空" style="position:relative;" onclick="cleanSelectArray(this.id)" disabled="disabled">
		</li>
		<% } %>
	</ul>
</div>
<input type="button" value="下一张" onclick="nextPicture(0)"></input>
				</div>
			</div>
			</article>

		</div>
		</article>
	</div>
	</div>
	<jsp:include flush="true" page="comleft.jsp"></jsp:include>
	</div>
</body>
</html>