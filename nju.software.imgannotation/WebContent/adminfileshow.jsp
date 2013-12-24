<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="conf.jsp"%>
<%@ page import="model.fileOperator"%>
<%@ page import="model.stringOperator"%>
<h3>
<center>
文件浏览
</center>
</h3>
<%
String realParent = request.getSession().getServletContext().getRealPath("/");
String parent = request.getParameter("parent");
String relPostion = "";
if(Tool.isEmp(parent))
	parent= realParent;
else
{
	relPostion = new String((request.getParameter("parent").getBytes("UTF-8")),"UTF-8");
	parent = realParent + relPostion;
}
String top ="";
File[] curFiles = null;
if(!Tool.isEmp(parent))
{
	curFiles = FileUtil.getFileList(FileUtil.replWebToFilePath(parent));
	String[] tmp = relPostion.split("/");
	if(tmp.length>0)
	{
		for(int i=0;i<tmp.length-1;i++)
			top += tmp[i]+"/";
	}
}
String idOnly = stringOperator.timeFormat();
%>
	<table class="table1" cellspacing="1" cellpadding="3" align="center" border="0">
		<tr>
			<td class="tableline linetitle" width="200" align="left"
				nowrap="nowrap">
			</td>
			<td class="tableline" width="*" align="right">当前路径：<font size=2 color=blue><%="/"+relPostion%></font>&nbsp;&nbsp;
				<input type=button value=" 向 上 " class="button" onclick="getFilelist('<%=top%>')" />
				&nbsp;&nbsp;
			</td>
		</tr>
	</table>
	<table width="98%" border="0" align="center" cellpadding="3"
		cellspacing="1" class="tableborder">
		<tr>
			<th width="25%">文件名称</th>
			<th width="8%" nowrap="nowrap">类型</th>
			<th width="8%" nowrap="nowrap">选择</th>
			<th width="8%" nowrap="nowrap">大小</th>
			<th width="14%" nowrap="nowrap">修改日期</th>
		</tr>
		<%
if(curFiles!=null&&curFiles.length>0)
{
	int folderNum = 0;
	int fileNum = 0;
	long fileSize = 0;
	String lineBg = "";
	String ext = "";
	for(int i=0;i<curFiles.length;i++)
	{
		boolean canEdit = false;
		if(curFiles[i].isDirectory())
			folderNum ++;
		else
		{
			fileNum ++;
			fileSize += curFiles[i].length();
		}
		ext = FileUtil.getFileExt(curFiles[i].getName());
		
		if(fileOperator.isPicture(ext) || curFiles[i].isDirectory() )
		{
			%>
			<tr align="center">
			<td class="<%=lineBg%>" align="left">&nbsp;
		<%
			if(curFiles[i].isDirectory())     //进入文件夹
			{
			%>
		<a  onclick="getFilelist('<%=(relPostion+curFiles[i].getName()+"/")%>')" class="filefolder">
			<%=curFiles[i].getName()%>
		</a>
		<%
			}
			else
			{         //如果是一张图片，在新窗口中打开
			%>
		<a href="<%=webRoot%>/adminimgView.jsp?file=<%=relPostion+curFiles[i].getName()%>")" target="_blank">
			<%=curFiles[i].getName()%>
		</a>
		<%
			}
			%>
		</td>
		<td class="<%=lineBg%>" nowrap="nowrap"><%=curFiles[i].isDirectory()?"<font color=orange>文件夹</font>":ext+" 文件"%></td>
		<td class="<%=lineBg%>" nowrap="nowrap">
		<input type=checkbox id="<%=(i+idOnly) %>" value="<%=curFiles[i].isDirectory()? relPostion+curFiles[i].getName()+"/" : relPostion+curFiles[i].getName()%>" onclick="picSetClick(this.id)"> </checkbox>
		</td>
		<td class="<%=lineBg%>" nowrap="nowrap"><%=!curFiles[i].isDirectory()?FileUtil.formatSize(curFiles[i].length()):"未知"%></td>
		<td class="<%=lineBg%>" nowrap="nowrap"><%=Tool.parseDateFromLong(curFiles[i].lastModified())%></td>
		</tr>
		<%
		}
	}
}
%>
	</table>
