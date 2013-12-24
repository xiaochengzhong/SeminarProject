<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Sidebar -->
<div id="sidebar">

	<!-- Logo -->
	<div id="logo">
		<h1>STRIPED</h1>
	</div>

	<!-- Nav -->
	<nav id="nav">
		<ul>
			<li class="current_page_item"><a href="commtaskshow.jsp">查看任务</a></li>
			<li><a >个人设置</a></li>
			<li><a >问题反馈</a></li>
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