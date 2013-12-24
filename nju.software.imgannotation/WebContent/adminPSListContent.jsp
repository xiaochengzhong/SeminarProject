<%@page import="model.picSetObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.xml_picture_ope"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="js/tooltipsy.min.js"></script>
<div class="pscontent">
	<div style="margin-top: 30px;">
	<input type="button" value="添加图像集" class="button" onclick="window.location.href='adminPSCre.jsp'" style=" height: 33px;"></input>
	</div>
<div style="background-color:#99A5A2; height:900px;">
<center>
	<h3>已有图像集</h3>
</center>
<%
xml_picture_ope mPicset = new xml_picture_ope();
ArrayList<picSetObj> mpsList = mPicset.getAllPicsInfo();
for(short i = 0 ; i < mpsList.size() ; ++i)
{
	%>
	<div class="comtasksetshow"
		style="position: relative; width: 280px; height: 120px; max-width: 300px; display: inline; float: left; margin-left: 5px; margin-top: 10px; border: 3px solid #C1D1E9;">
		<table>
			<tr>
				<td>发布者：</td>
				<td><%=(mpsList.get(i).getAuthorName()) %></td>
			</tr>
			<tr>
				<td>发布的时间：</td>
				<td><%=(mpsList.get(i).getTime()) %></td>
			</tr>
			<tr>
				<td>标题：</td>
				<td><a class="tooltipsy"
					title="<%=(mpsList.get(i).getdesp()) %>"> <%=(mpsList.get(i).getTittle()) %>
				</a></td>
			</tr>
			<tr>

				<td><input type="button" id="<%=(mpsList.get(i).getID()) %>"
					value="详细信息"
					onclick="window.location.href='adminPSView.jsp?psid=<%=(mpsList.get(i).getID()) %>'" /></td>
				<td><input type="button" id="<%=(mpsList.get(i).getID()) %>"
					value="修改信息"
					onclick="window.location.href='adminPSUpdate.jsp?psid=<%=(mpsList.get(i).getID()) %>'" /></td>
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