<%@page import="model.taskObj"%>
<%@page import="model.xml_taskset_ope"%>
<%@page import="model.tasksetobj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.picSetObj"%>
<%@page import="model.xml_picture_ope"%>
<%@page import="model.stringOperator"%>
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
String timeId = stringOperator.timeFormat();
%>
<%request.setAttribute("currentIndex", "1"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务集更新</title>
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
<script src="js/admin-tools.js"></script>
<script src="js/jscroll.js"></script>
<script src="js/tooltipsy.min.js"></script>
</head>
<body class="left-sidebar">
	<div id="wrapper">
		<div id="content">
		<input type="hidden" value="<%=(mtaskset.getTSID()) %>" id="taskset_id" />
			<div class="admTCCreateTool">
				<center>任务生成工具</center>
				<hr />
				<table class="toolTable">
					<tr>
						<td style="width: 150px;">任务名:</td>
						<td><div style="position: relative;">
								<select style="width: 200px;"
									onchange="document.getElementById('mTaskName').value=this.value">
									<option value="标出人脸">标出人脸</option>
									<option value="标出水平线" selected="selected">标出水平线</option>
									<option value="标出人的形状">标出人的形状</option>
									<option value="标出模型">标出模型</option>
									<option value="标出树的的位置">标出树的的位置</option>
								</select> <input id="mTaskName" name="input"
									style="position: absolute; width: 180px; height: 18px; left: 2px; top: 6px; border-bottom: 0px; border-right: 0px; border-left: 0px; border-top: 0px;"
									value="标出水平线" />
							</div></td>
					</tr>
					<tr>
						<td>使用的工具名：</td>
						<td><select style="width: 200px;" id="shapeChoose">
								<option value="1">曲线</option>
								<option value="2" selected="selected">直线</option>
								<option value="3">椭圆</option>
								<option value="4">矩形</option>
								<option value="5">多边形</option>
								<option value="6">不规则区域</option>
						</select>
						<td>
					</tr>
					<tr>
						<td>画笔大小：</td>
						<td><span id="size_span"
							style="border: 1px solid #999; width: 15px; height: 30px; margin-top: 7px; margin-top: 7px; display: block; float: left; margin-left: 10px">1</span>
							<div id="size_bar"
								style="width: 100px; height: 5px; background-color: #999; float: left; margin: 12px; position: relative; margin-top: 20px;">
								<span id="size_thumb" class="btn" onClick=""
									style="background-color: #666;; width: 15px; border-top-left-radius: 8px; border-top-right-radius: 8px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px; height: 15px; margin: 0px; margin-top: -5px; position: absolute; left: 0px;"></span>
							</div></td>
					</tr>
					<tr>
						<td>颜色设置：</td>
						<td><span id="color_span"
							style="border: 1px solid #999; background-color: #FF7F00; width: 15px; height: 15px; margin-top: 7px; display: block; float: left; margin-left: 10px"></span>
							<canvas id="canvas_color" width="198" height="15"
								style="border: 1px solid #999; margin-top: 7px; margin-left: 10px; float: left;"></canvas></td>
					</tr>
					<tr>
						<td>描述：</td>
						<td><textarea
								style="overflow: auto; width: 500px; height: 120px;"
								id="ts_desp"></textarea></td>
					</tr>
					<tr>
						<td><p></p></td>
					</tr>
					<tr>
						<td><p></p></td>
					</tr>
					<tr>
					<td><input type="button" value="返回" class="button" onclick="javascript :history.back(-1);"></td>
						<td><input type="button" value="生成任务到列表"
							onClick="addTaskList()" class="button" /></td>
					</tr>
				</table>
			</div>

			<div class="admTCList">

				<center>当前任务集列表</center>
				<hr />
				<div id="taskListContent" class="taskListContent">
				<%
				//当前任务列表
				for(short i = 0 ; i < mTask.size() ; ++i)
				{
				%>
				<div id="tsdesp_<%=(String.valueOf(i)+timeId) %>">
					<input type="button" class="button" value="删除"
						style="padding-left: 10px; padding-right: 10px;"
						onclick="deleteTaskWithID('tsdesp_<%=(String.valueOf(i)+timeId) %>')"> <a
						class="tooltipsy" tittle="<%=(mTask.get(i).getDescription()) %>"><%=(mTask.get(i).getTaskName()) %></a><span class="<% switch(Integer.parseInt((mTask.get(i).getShapeID()))){
					case 1:
						out.println("adminpencil");
						break;
					case 2:
						out.println("adminline");
						break;
					case 3:
						out.println("admincircle");
						break;
					case 4:
						out.println("adminrect");
						break;
					case 5:
						out.println("adminmultipoint");
						break;
					case 6:
						out.println("adminirregular");
						break;
						default:
							break;
					} %>"></span>
					<input class="color_span" style="background-color: <%=(mTask.get(i).getLineCol()) %>"
						value="<%=(mTask.get(i).getLineWidth()) %>" readonly="">
				</div>
				<script type="text/javascript">
					if(!dealTSReLoad("tsdesp_<%=(String.valueOf(i)+timeId) %>"))
					{
						var newTask = new taskObj("<%=(mTask.get(i).getTaskName()) %>" , <%=(mTask.get(i).getShapeID())%> , "tsdesp_<%=(String.valueOf(i)+timeId) %>" , "<%=(mTask.get(i).getDescription()) %>");
						newTask.lineWidth = <%=(mTask.get(i).getLineWidth()) %>;
						newTask.colorSet = "<%=(mTask.get(i).getLineCol()) %>";
						m_TaskArray.push(newTask);
					}
				</script>
				<%
				}
				%>
				</div>
				<hr />
				发布者：<input id="ts_author" type="text" style="width: 100px" value="<%=(mtaskset.getTSAuthor()) %>" disabled="disabled">
				发布时间：<input id="ts_author" type="text" style="width: 220px" value="<%=(mtaskset.getTSTime()) %>" disabled="disabled">
				<input id="ts_authorID" type="hidden" style="width: 500px" value="<%=(mtaskset.getauthorID()) %>" disabled="disabled">
				任务集标题： <input id="ts_tittle" type="text" style="width: 460px"
					value="<%=(mtaskset.getTittle()) %>"></input> 任务集描述：
				<textarea style="overflow: auto; width: 420px; height: 120px;"
					id="taskset_desp"><%=(mtaskset.getDescription()) %></textarea>
				<input type="button" value="更新当前任务集  " onClick="updateTSData()"
					class="button" />
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>