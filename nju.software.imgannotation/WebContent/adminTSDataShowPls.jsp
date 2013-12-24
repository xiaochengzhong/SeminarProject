<%@page import="model.*,java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String relTSID = request.getParameterValues("relTSID")[0];

xml_taskset_ope mTSinfo = new xml_taskset_ope();
ArrayList<userObj> mUserData = mTSinfo.getTSPersonList(relTSID);
%>

<div class="picthese">
	<h3>Person List</h3>
<ul id="person_list_size" class="<%=mUserData.size()%>">
<%
for(short i =0 ; i < mUserData.size() ; ++i)
{
	%>
	<li>
	<input type="radio" id="<%=(mUserData.get(i).getID()) %>" name="check" class="notdone" onclick="changePerson(this.id)">
	<%=(mUserData.get(i).getName()) %>
	</li>
	<%
}
%>
</ul>
	
	
</div>