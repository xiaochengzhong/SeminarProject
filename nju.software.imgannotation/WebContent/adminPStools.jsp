<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="conf.jsp"%>
<%@ page import="model.fileOperator"%>

<%
String psid = request.getParameter("psid");
boolean update = false;
if(Tool.isEmp(psid))
	update= false;
else
{
	psid = new String((request.getParameter("psid").getBytes("UTF-8")),"UTF-8");
}
%>
<table style="width:100%;margin-bottom: 0px;">
  <tr>
    <td style="width:10%">标题: </td>
    <td style="width:90%"><input id="ps_tittle" type="text" style="width:500px"></td>
  </tr>
  <tr>
    <td style="width:10%">描述: </td>
    <td><textarea style="overflow:auto;width: 500px; height: 120px;" id="ps_desp"></textarea></td>
  </tr>
  <tr>
    <td style="width:10%">图像集的内容: </td>
  </tr>
</table>