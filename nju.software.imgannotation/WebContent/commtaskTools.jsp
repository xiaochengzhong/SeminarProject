<%@page import="model.*,java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
//get the session
HttpSession pSession = request.getSession(true);
String userID = (String)pSession.getAttribute("pID");

String relTSID = request.getParameterValues("relTSID")[0];
String webPath = request.getSession().getServletContext().getRealPath("/");
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
		<li style="height: auto;">
		<div>
		<input type="button" id="<%=((tempmydate.get(i)).getTaskID()) %>" value="清空" style="position:relative;padding-left: 2px;padding-right: 2px;" onclick="cleanSelectArray(this.id)" class="button">
		<input type="radio"  id="<%=(tempmydate.get(i)).getTaskID() %>" name="check"
			class="notdone" onclick="changeAttr(this.id)"> <%out.print((tempmydate.get(i)).getTaskName()); %>
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
			</div>
			<div style="position:relative;margin-left: 50px;display:none;" id="act_<%=((tempmydate.get(i)).getTaskID()) %>">
			</div>
			
		</li>
		<% } %>
	</ul>
</div>
<input type="button" value="下一张" onclick="nextPicture(1)"></input>
<input type="button" value="跳过" onclick="nextPicture(0)"></input>