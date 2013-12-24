<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Post -->
<article class="is-post is-post-excerpt">
	<div>
		<div class="mainPic">
			<img src="./images/i.jpg" width="600" height="550"
				style="margin-top: 10px;" />
		</div>
		<div class="picShow">
			<ul class="nav">
				<li><a href="#">链接一</a></li>
				<li><a href="#">链接二</a></li>
				<li><a href="#">链接三</a></li>
				<li><a href="#">链接四</a></li>
			</ul>
		</div>
		<div class="taskSet">
			<div>任务列表：</div>
			<div id="TaskShow"></div>
			<div>分配人员:</div>
			<div id="member"></div>
		</div>
	</div>
	<table class="toolTable">

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
					value="标出水平线"> </input>
			</div></td>
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
			<td><input type="button" value="生成任务到列表" onClick="addTaskList()" /></td>
		</tr>
		<tr>
			<td><input type="button" value=" 发布任务列表 "
				onClick="commintTasks()" /></td>
		</tr>
	</table>
</article>