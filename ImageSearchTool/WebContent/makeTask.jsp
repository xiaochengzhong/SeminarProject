<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>任务生成</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <script language="javascript" src="js/jquery-1.9.1.min.js"></script>
        <script language="javascript" src="js/jquery.ztree.core-3.5.js"></script>
        <script language="javascript" src="js/jquery.ztree.excheck-3.5.js"></script>
        <script language="javascript" src="js/json2.js"></script>
        <script language="javascript" src="js/bootstrap.min.js"></script>
        <link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>
        <link href="css/makeTask.css" type="text/css"  rel="stylesheet"/>
        <link href="css/zTreeStyle/zTreeStyle.css" type="text/css"  rel="stylesheet"/>
<script language="javascript">
 var executor = "<%=session.getAttribute("username")%>";
 var zTree1,zTree2;//树
 var setting;//参数设置
 var zTreeNodes;
 var tasks = new Array();
 var index = -1;
 var isfold = true;
 setting = {
   showLine: true,//显示虚线
   check: {
		enable: true,
		chkStyle : "checkbox"
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: 0,
		}
	},
	callback: {
		onClick: zTreeOnClick
	},

   //下面的三个属性是同时出现的 用于关联父子节点
   isSimpleData : true,
   treeNodeKey : "id",
   treeNodeParentKey : "pId",
   nameCol : "name"
  };

 var Task = function(){
	 var id;
	 var name;
	 var queryNodes;
	 var dataSetNodes;
	 var feature;
	 var similarity;
	 var topN = -1;
	 var matchRate = -1;
	 var matchDistance = -1;
};
 var Node = function(){
	 var name;
	 var urlAddr;
	 var id =0;
	 var pId =0;
 };
 function zTreeOnClick(event, treeId, treeNode){
	 var name = treeNode.name;
	 if(name.indexOf('.')<0)
		 return;
	 var imgStr = '<p>图片：'+name+'</p>'
	              +'<img src="'+treeNode.urlAddr+'" id="imgShow"/>';
	 $("#imageShowDiv").html(imgStr);
 }
 
 function submitTask(){
	 var name;
	 if(index==-1){
	 name = prompt("请输入任务名：", "");
	 if(!name){
		 if(name!=null)
		 alert("任务名不能为空");
	     return;
	 }
	 }else
		 name = tasks[index].name;
	 var task = new Task();
	 var date = new Date();
	 task.name = name;
	 month = date.getMonth() + 1;
	 task.id = name+"_"+date.getFullYear()+month+date.getDate()+date.getHours()+date.getMinutes()+date.getSeconds();
     task.queryNodes = zTree1.getCheckedNodes();
     task.dataSetNodes = zTree2.getCheckedNodes();
     task.feature = new Array();
     for(var i=0;i<4;i++){
    	 if($("#fBox"+i).is(":checked"))
    		 task.feature.push($("#fBox"+i).val());
     }
     task.similarity = $("input[type='radio'][name=similarity]:checked").val();
     task.topN = $("#topN").val();
     task.matchRate = $("#matchRate").val();
     task.matchDistance = $("#matchDistance").val();
     if(index!=-1){
    	 tasks[index] = task;
    	 $("#cancelEdit").hide();
    	 if($("#taskBox"+index).is(":checked"))
    		 $("#tr"+index).css({"background-color":"#98bf21"});
    	 else
    		 $("#tr"+index).css({"background-color":"#ffffff"});
    	 index = -1;
     }
     else
    	 tasks.push(task);
     showTask();
     clearAll(false);
 }
 function showTask(){
	 var tableStr = "";
	 for(var i=0;i<tasks.length;i++){
		 var j = i+1;
		var trStr =  '<tr id="tr'+i+'" class="taskTr"><td class="taskTd">'		              
		              +'<div class="taskName">'+j+'.'+tasks[i].id+'</div>'
		              +'<a href="javaScript:editTask('+i+')" class="doA">编辑</a>'
		              +'<a href="javaScript:deleteTask('+i+')" class="doA">删除</a>'
		              +'</td></tr>';
		 tableStr = tableStr+trStr;
	 }
	 $("#taskTable").html(tableStr);
}

 function clearAll(b){
	 if(b&&!confirm("是否确定清空？"))
		return; 
	   zTree1.checkAllNodes(false);
	   zTree2.checkAllNodes(false);
	   $("#choiceDiv input[type='checkbox']").attr("checked",false);
	   $("#choiceDiv input[type='text']").val("");
	   $("#sRadio1")[0].checked = true;
	 };
 function deleteTask(i){
	if(confirm("是否确定删除任务"+tasks[i].name+"?"))
	{	
	tasks.splice(i,1);
	showTask();
	}
 }
 function editTask(i){
	 if(index!=-1)
	{
	alert("有任务在编辑，不能开启新编辑任务");
	return;
	}
	 index = i;
	 $("#tr"+i).css({"background-color":"#98bf21"});
	 clearAll(false);
	 var task = tasks[i];
	 $.each(task.queryNodes, function(j, node){   
		 zTree1.checkNode(node, true, false);      
});
	 $.each(task.dataSetNodes, function(j, node){   
		 zTree2.checkNode(node, true, false);
});
	  for(var j=0;j<task.feature.length;j++)
		 $("#featureDiv input[value='"+task.feature[j]+"']")[0].checked=true;
     $("#similarityDiv input[value='"+task.similarity+"']")[0].checked=true;
	 $("#topN").val(task.topN);
	 $("#matchRate").val(task.matchRate);
	 $("#matchDistance").val(task.matchDistance);
	 $("#cancelEdit").show();
 }
 function cancelEdit(){
	 clearAll(false);
	 $("#cancelEdit").hide();
	 $("#tr"+index).css({"background-color":"#ffffff"});
	 index = -1;
 }
 
 function goTasks(){
	 if(!confirm("是否确定提交当前任务序列？"))
		 return;
	 var goTasks = new Array();
	 $("#goBtn").attr("disabled",true);
	 for(var i=0;i<tasks.length;i++){
			 var task = new Task();
			 task.executor = executor;
			 task.id = tasks[i].id;
			 task.name = tasks[i].name;
			 task.feature = tasks[i].feature;
			 task.similarity = tasks[i].similarity;
			 task.topN = tasks[i].topN;
			 task.matchRate = tasks[i].matchRate;
			 task.matchDistance = tasks[i].matchDistance;
			 task.queryNodes = new Array();
			 task.dataSetNodes = new Array();
			 $.each(tasks[i].queryNodes, function(j, node){
				 if(node.name.indexOf('.')<0)
					 return;
				var simpleNode = new Node(); 
				simpleNode.name = node.name;
				simpleNode.urlAddr = node.urlAddr;
				task.queryNodes.push(simpleNode);
		});
			 $.each(tasks[i].dataSetNodes, function(j, node){
				   if(node.name.indexOf('.')<0)
					 return;
					var simpleNode = new Node(); 
					simpleNode.name = node.name;
					simpleNode.urlAddr = node.urlAddr;
					task.dataSetNodes.push(simpleNode);
			});
			 goTasks.push(task); 
	 }
     var tasksStr = JSON.stringify(goTasks);
     //alert(tasksStr.toString());
	 $.ajax({ 
         type: "post", 
         url: "goTask.action",
         data:"tasks="+tasksStr.toString(),
         dataType: "json",
         success: function (data) { 
        	 //alert(data.result);
         }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) {
                 //alert(errorThrown); 
         } 
 });
	 tasks.splice(0,tasks.length);
	 showTask();
	 $("#goBtn").attr("disabled",false); 
 }
function siftToggle(){
	if(isfold)
	$("#siftToggle img").attr("src","icon/unfold.png");
	else
	$("#siftToggle img").attr("src","icon/fold.png");	
	$("#siftDiv").slideToggle("slow");
	isfold = !isfold;
}
function goPage(){
	var taskid = $("#matchedTaskSelect").val();
	if(taskid!="")
		window.open("taskInfo.jsp?executor="+executor+"&taskId="+taskid);
}
function showMatchedTasks(matchedTasks){
	//alert(matchedTasks[0]);
	if(matchedTasks==null)
		return;
	var options = '<option value="">选择要查看的任务</option>';  
	$.each(matchedTasks,function(i,taskid){
		options = options+'<option value="'+taskid+'">'+taskid+'</option>';
	});
	$("#matchedTaskSelect").html(options);
}

 $(document).ready(function(){
	 if(executor=="null")
		 location.href = "login.jsp";
	 $("#username").html("欢迎，"+executor+"  <a href='logOut.action'>退出</a>");
	   $.ajax({ 
         type: "post", 
         url: "fetchMatchedTasks.action",
         data:"executor="+executor,
         dataType: "json",
         success: function (data) { 
        	var matchedTasks = data.matchedTasks;
        	showMatchedTasks(matchedTasks);
         }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) {
        	     //alert(XMLHttpRequest.status);
                 //alert(XMLHttpRequest.readyState);
                 //alert(textStatus); 
         } 
 });  
	   $.ajax({ 
         type: "post", 
         url: "zTreeNodes.action",
         data:"type=0",
         dataType: "json", 
         success: function (data) { 
        	 zTreeNodes = data.imageNodes;  
        	 zTree1 = $.fn.zTree.init($("#ztree1"), setting, zTreeNodes);
        	 $.ajax({ 
    	         type: "post", 
    	         url: "zTreeNodes.action",
    	         data:"type=1",
    	         dataType: "json",
    	         success: function (data) { 
    	        	 zTreeNodes = data.imageNodes;  
    	        	 zTree2 = $.fn.zTree.init($("#ztree2"), setting, zTreeNodes);
    	         }, 
    	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
    	                 //alert(errorThrown); 
    	         } 
    	 }); 
         }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
                 //alert(errorThrown); 
         } 
 });
});
  </script>
</head>
<body>
<div id="header">
<h2>检索检测工具</h2>
<ul class="linkUL">
<li class="linkLI">
<a href="#" target=_blank>链接1</a>
</li>
<li class="linkLI">
<a href="#" target=_blank>链接2</a>
</li>
<li class="linkLI">
<a href="#" target=_blank>链接3</a>
</li>
<li class="linkLI">
<a href="#" target=_blank>链接4</a>
</li>
<li class="linkLI">
<a href="#" target=_blank>链接5</a>
</li>
</ul>
</div>
<div id="userInfoDiv">
<div id="username"></div>
<div id="matchedTasks">已完成匹配任务列表：
<select id="matchedTaskSelect" onchange="goPage()">
<option value="">选择要查看的任务</option>
</select>
</div>
</div>
<div id="bodyDiv">
<div id="dataDiv">
<div>
<p>Query:</p>
<div class="dataSetDiv">
<ul id="ztree1" class="ztree" style="overflow:auto;"></ul>
</div>
</div>
<div>
<p>DataSet:</p>
<div class="dataSetDiv">
<ul id="ztree2" class="ztree" style="overflow:auto;"></ul>
</div>
</div>
<div id="imageShowDiv">
</div>
</div>
<div id="choiceDiv">
<div id="featureDiv">
<h4>feature:</h4>
<div id="featureBox">
<div><a href="javaScript:siftToggle()" id="siftToggle"><img src="icon/fold.png"></a> sift</div>
<div id="siftDiv">
<div>&nbsp<input type="checkbox" id="fBox0" value="sift8"/>sift8</div>
<div>&nbsp<input type="checkbox" id="fBox1" value="sift16"/>sift16</div>
<div>&nbsp<input type="checkbox" id="fBox2" value="sift32"/>sift32</div>
</div>
<div><input type="checkbox" id="fBox3" value="surf"/>surf</div>
</div>
</div>
<div id="similarityDiv">
<h4>similarity:</h4>
<div><input type="radio" id="sRadio1" name="similarity" value="euclidean" checked="checked"/>欧式距离</div>
<div><input type="radio" id="sRadio2" name="similarity" value="cosine"/>余弦距离</div>
<div><input type="radio" id="sRadio3" name="similarity" value="hamming"/>汉明距离</div>
</div>
<div id="parameterDiv">
<h4>parameters:</h4>
<div>top—n ：    <input type="text" id="topN"></div>
<div>相似度 ：     <input type="text" id="matchRate"></div>
<div>匹配距离：<input type="text" id="matchDistance"></div>
</div>
<div id="buttonDiv">
<input type="button" value="清空" onclick="clearAll(true)"/>
<input type="button" id="submit" value="提交" onclick="submitTask()"/>
<input type="button" id="cancelEdit" value="取消编辑" onclick="cancelEdit()"/>
</div>
</div>
<div id="taskDiv">
<h3>任务列表：</h3>
<div style="overflow:auto" id="tableDiv">
<table id="taskTable">
</table>
</div>
<div id="goButton">
<input type="button" id="goBtn" value="开始匹配" onclick="goTasks()"/>
</div>
</div>
</div>
</body>
</html>
