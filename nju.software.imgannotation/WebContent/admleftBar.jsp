<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Sidebar -->
<%
String current = (String)request.getAttribute("currentIndex");
int curIndex = Integer.parseInt(current);
%>
<div id="sidebar">

	<div id="logo">
		<h1>IMAGE</h1>
	</div>

	<nav id="nav">
		<ul>
			<%
		if(curIndex == 1) { 
		%>
			<li class="current_page_item"><a href="adminTSList.jsp">查看任务集</a></li>
			<li><a href="adminPSList.jsp">查看图像集</a></li>
			<li><a href="adminPerSList.jsp">查看人员集</a></li>
			<%
		}
		else if(curIndex == 2)
		{
		%>
			<li class="current_page_item"><a href="adminPSList.jsp">查看图像集</a></li>
			<li><a href="adminTSList.jsp">查看任务集</a></li>
			<li><a href="adminPerSList.jsp">查看人员集</a></li>
			<%
		}
		else if(curIndex == 3)
		{
		%>
			<li class="current_page_item"><a href="adminPerSList.jsp">查看人员集</a></li>
			<li><a href="adminPSList.jsp">查看图像集</a></li>
			<li><a href="adminTSList.jsp">查看任务集</a></li>
			
			<%
		}
		else
		{
			%>
			<li><a href="adminTSList.jsp">查看任务集</a></li>
			<li><a href="adminPSList.jsp">查看图像集</a></li>
			<li><a href="adminPerSList.jsp">查看人员集</a></li>
			<%
		}
		%>
			<li><a href="#">问题反馈</a></li>
		</ul>
	</nav>
	
	<section class="is-calendar">
		<div class="inner">
			Welcome 
			<%
			HttpSession pSession = request.getSession(true);
			out.println((String)pSession.getAttribute("pName"));
			String userID = (String)pSession.getAttribute("pID");
			%>
		<input class="button" type="button" value="退出系统" onclick="window.location.href='exitSys'" style="padding-right: 4px;padding-left: 4px;">
		</div>
	</section>
	
</div>