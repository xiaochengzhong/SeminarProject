<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<title>任务管理</title>
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
						<td><input type="button" value="生成任务到列表"
							onClick="addTaskList()" class="button" /></td>
					</tr>

				</table>
			</div>

			<div class="admTCList">

				<center>当前任务集列表</center>
				<hr />
				<div id="taskListContent" class="taskListContent"></div>
				<hr />
				任务集标题： <input id="ts_tittle" type="text" style="width: 460px"
					value=""></input> 任务集描述：
				<textarea style="overflow: auto; width: 420px; height: 120px;"
					id="taskset_desp"></textarea>
				<input type="button" value=" 发布当前任务集  " onClick="commintTasks()"
					class="button" />
			</div>
		</div>
		<jsp:include flush="true" page="admleftBar.jsp"></jsp:include>
	</div>
</body>
</html>