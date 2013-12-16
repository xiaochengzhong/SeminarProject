<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>任务信息(<%=request.getParameter("taskId")%>)</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <script language="javascript" src="js/jquery-1.9.1.min.js"></script>
        <script language="javascript" src="js/bootstrap.min.js"></script>
        <link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
        <link href="css/taskInfo.css" type="text/css"  rel="stylesheet"/>  
<script language="javascript">
var taskId = '<%=request.getParameter("taskId")%>';
var executor = '<%=request.getParameter("executor")%>';
var task;
var accuracySort = true;//up sort
var recallSort = true;//up sort
function sortByAccuracy(){
	if(accuracySort)
	$("#sortAccuracy").css("background-image","url(icon/down.png)");
	else
	$("#sortAccuracy").css("background-image","url(icon/up.png)");
	if(accuracySort)
		accuracyUpSort();
	else
		accuracyDownSort();
		showQueryInfo();
	accuracySort = !accuracySort;
}
function sortByRecall(){
	if(recallSort)
	$("#sortRecall").css("background-image","url(icon/down.png)");
	else
	$("#sortRecall").css("background-image","url(icon/up.png)");	
	if(recallSort)
		recallUpSort();
	else
		recallDownSort();
	showQueryInfo();
	recallSort = !recallSort;
}
function accuracyUpSort(){
	if(task.queryNodes==null)
		return;
	for(var i=1;i<task.queryNodes.length;i++){
		var index = i;
		for(var j=i-1;j>=0;j--){
			if(task.queryNodes[index].precision>task.queryNodes[j].precision){
				var temp = task.queryNodes[index];
			    task.queryNodes[index] = task.queryNodes[j];
			    task.queryNodes[j] = temp;
			    index = j;
			}else
				break;
		}
	}
}

function accuracyDownSort(){
	if(task.queryNodes==null)
		return;
	for(var i=1;i<task.queryNodes.length;i++){
		var index = i;
		for(var j=i-1;j>=0;j--){
			if(task.queryNodes[index].precision<task.queryNodes[j].precision){
				var temp = task.queryNodes[index];
			    task.queryNodes[index] = task.queryNodes[j];
			    task.queryNodes[j] = temp;
			    index = j;
			}else
				break;
		}
	}
}

function recallUpSort(){
	if(task==null)
		return;
	for(var i=1;i<task.queryNodes.length;i++){
		var index = i;
		for(var j=i-1;j>=0;j--){
			if(task.queryNodes[index].recall>task.queryNodes[j].recall){
				var temp = task.queryNodes[index];
			    task.queryNodes[index] = task.queryNodes[j];
			    task.queryNodes[j] = temp;
			    index = j;
			}else
				break;
		}
	}
}

function recallDownSort(){
	if(task.queryNodes==null)
		return;
	for(var i=1;i<task.queryNodes.length;i++){
		var index = i;
		for(var j=i-1;j>=0;j--){
			if(task.queryNodes[index].recall<task.queryNodes[j].recall){
				var temp = task.queryNodes[index];
			    task.queryNodes[index] = task.queryNodes[j];
			    task.queryNodes[j] = temp;
			    index = j;
			}else
				break;
		}
	}
}

function showTaskInfo(){
	var temp="";
	$("#taskId").html(task.taskId);
	$("#executor").html(task.executor);
	$.each(task.feature,function(i,feature){
		if(i==task.feature.length-1)
			temp = temp+feature;
		else 
			temp = temp+feature+",";
	});
	$("#feature").html(temp);
	$("#similarity").html(getSimilarity(task.similarity));
	//alert(task.parameters.top-n);
	$("#topN").html(task.parameters.topN);
	$("#rate").html(task.parameters.matchRate);
	$("#distance").html(task.parameters.matchDistance);
	$("#precision").html(intercept(task.precision));
	$("#recall").html(intercept(task.recall));
	showQueryInfo();
}

function showQueryInfo(){
	 var querylist = task.queryNodes;
	 var tBodyStr = "";
	 if(querylist==null)
		 return;
	 $.each(querylist,function(i,query){
		 var queryTr = '<tr><td><a href="'+query.urlAddr+'" target=_blank><img src="'+query.urlAddr+'" class="queryImg"/></a><br/>'+query.name+'</td>'
		               +'<td>'+intercept(query.precision)+'</td>'
		               +'<td>'+intercept(query.recall)+'</td>'
                       +'<td><a href="queryInfo.jsp?executor='+task.executor+'&taskId='+task.taskId+'&queryPath='+query.urlAddr+'" target=_blank><img class="linkImg" src="icon/link.BMP"/></a></td></tr>';
         tBodyStr= tBodyStr+queryTr;
	 });
	 $("#queryBody").html(tBodyStr);
} 

function intercept(str){
	var temp_str = (str*100).toString();
	var i = temp_str.length>5?5:temp_str.length;
    return temp_str.substr(0,i)+"%";
}

function getSimilarity(str){
	if(str=="cosine")
		return "余弦距离";
	else if(str=="hamming")
		return "汉明距离";
	else
		return "欧氏距离";
}

$(document).ready(function(){
	$.ajax({ 
        type: "post", 
        url: "fetchTaskInfo.action",
        data:"executor="+executor+"&taskId="+taskId,
        dataType: "json",
        success: function (data) { 
        	//alert(data.task.executor);
       	task = data.task;
       	showTaskInfo(task);
        }, 
        error: function (XMLHttpRequest, textStatus, errorThrown) {
       	     alert(errorThrown);
        } 
}); 
});
</script>
</head>
<body>
<div id="taskBody">
<div id="taskInfoDiv">
<p>详细信息</p>
<div>
<div class="taskText">任务序号：<span id="taskId"></span></div>
<div class="taskText">执行人：<span id="executor"></span></div>
<div class="taskText">算法选择：<span id="feature"></span></div>
<div class="taskText">相似距离：<span id="similarity"></span></div>
<div class="taskText">top—n：<span id="topN"></span></div>
<div class="taskText">相似取值(matchRate)：<span id="rate"></span></div>
<div class="taskText">距离取值(matchDistnce)：<span id="distance"></span></div>
</div>
</div>
<div id="taskIndexDiv">
<p>指标</p>
<div>
<div class="taskText">准确率计算公式：<span>匹配且对应的数量/匹配的数量</span></div>
<div class="taskText">召回率计算公式：<span>匹配且对应的数量/对应的数量</span></div>
<div class="taskText">总准确率：<span id="precision"></span></div>
<div class="taskText">总召回率：<span id="recall"></span></div>
</div>
</div>
<div id="taskDetailDiv">
<p>详细匹配</p>
<div>
<table id="queryTable">
<thead>
<tr>
<th>query</th>
<th>准确率<input type="button" class="sortBtn" id="sortAccuracy" onclick="sortByAccuracy()"/></th>
<th>召回率<input type="button" class="sortBtn" id="sortRecall" onclick="sortByRecall()"/></th>
<th>详细</th>
</tr>
</thead>
<tbody id="queryBody">
</tbody>
</table>
</div>
</div>
</div>
</body>
</html>
