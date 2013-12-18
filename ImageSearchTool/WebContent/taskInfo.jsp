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
        <script language="javascript" src="js/moment.min.js"></script>
        <script language="javascript" src="js/bootstrap-sortable.js"></script>
        <link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
        <link href="css/bootstrap-sortable.css" type="text/css"  rel="stylesheet"/>
        <link href="css/taskInfo.css" type="text/css"  rel="stylesheet"/>  
<script language="javascript">
var username = "<%=session.getAttribute("username")%>";
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
function doNothing(){
	 alert("请选择要查看的详细匹配");
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
		 var queryTr = '<tr><td id="query-item-td"><a href="'+query.urlAddr+'" target=_blank><img src="'
		 			   +query.urlAddr+'" class="queryImg"/></a><br/>'+query.name+'</td>'
		               +'<td id="query-item-td">'+intercept(query.precision)+'</td>'
		               +'<td id="query-item-td">'+intercept(query.recall)+'</td>'
                       +'<td id="query-item-td"><a href="queryInfo.jsp?executor='
                       +task.executor+'&taskId='+task.taskId+'&queryPath='+query.urlAddr
                       +'" target=_blank>Detail</a></td></tr>';
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
	$("#username").html("欢迎，"+executor+"  <a href='logOut.action'>退出</a>");
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
<div class="container" style="height:65px;">
		<div class="navbar-wrapper">
			<div class="navbar">
				<div class="navbar-inner">
					<div class="container">
						<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</a>
						<a class="brand" href="#">检索检测工具</a>
						<div class="nav-collapse collapse">
							<ul class="nav">
								<li>
									<a href="/ImageSearchTool/login.action">创建任务</a>
								</li>
								<li class="active">
									<a href="#">任务信息</a>
								</li>
								<li>
									<a href="#" onClick="doNothing()">匹配结果</a>
								</li>
								<div id="username"></div>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<div class="body-div container" id="taskBody">
	<div class="row-fluid" id="userInfoDiv">
		<div class="span6" id="matchedTasks">
			<label>任务信息</label> 
		</div>
	</div>
<div class="container-fluid">
    <div class="row-fluid">
    <div class="span4 div-border" id="taskInfoDiv">
        <label class="title">详细信息</label> 
    <div class="base-info">
    <div class="info-item">
    <span class="list-item-head">任务序号</span>
    <span class="list-item-tail" id="taskId"></span>
    </div>
    <div class="info-item">
    <span class="list-item-head">执行人</span>
    <span class="list-item-tail" id="executor"></span>
    </div>
    <div class="info-item">
    <span class="list-item-head">算法选择</span>
    <span class="list-item-tail" id="feature"></span>
    </div>
    <div class="info-item">
    <span class="list-item-head">相似距离</span>
    <span class="list-item-tail" id="similarity"></span>
    </div>
    <div class="info-item">
    <span class="list-item-head">top—n</span>
    <span class="list-item-tail" id="topN"></span>
    </div>
    <div class="info-item">
    <span class="list-item-head">相似取值(matchRate)</span>
    <span class="list-item-tail" id="rate"></span>
    </div>
    <div class="info-item end">
    <span class="list-item-head">距离取值(matchDistnce)</span>
    <span class="list-item-tail" id="distance"></span>
    </div>
</div>
    </div>
    <div class="span4 div-border" id="taskIndexDiv">
    <label class="title">指标</label> 
    <div class="base-info">
    <div class="info-item">
    <span class="list-item-head">准确率计算公式</span>
    <span class="list-item-tail">匹配且对应的数量/匹配的数量</span>
    </div>
    <div class="info-item">
    <span class="list-item-head">召回率计算公式：</span>
    <span class="list-item-tail">匹配且对应的数量/对应的数量</span>
    </div>
    <div class="info-item">
    <span class="list-item-head">总准确率</span>
    <span class="list-item-tail" id="precision"></span>
    </div>
    <div class="info-item end">
    <span class="list-item-head">总召回率</span>
    <span class="list-item-tail" id="recall"></span>
    </div>
	</div>
    </div>
    <div class="span4 div-border" id="taskDetailDiv">
    <label class="title">详细匹配</label>
    <div class="query-info">
    <table id="queryTable" class="table table-striped table-bordered table-hover sortable">
		<thead>
		<tr>
		<th id="query-item-th">query</th>
		<th id="query-item-th">准确率</th>
		<th id="query-item-th">召回率</th>
		<th id="query-item-th">详细</th>
		</tr>
		</thead>
		<tbody id="queryBody">
		</tbody>
	</table>
	</div>
    </div>
  </div>
  </div>
  </div>
</body>
</html>
