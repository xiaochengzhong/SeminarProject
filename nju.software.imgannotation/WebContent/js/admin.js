m_lineWidth = 1 ;
m_colorSet = "rgb(255,127,0)";
m_TaskArray = new Array(); //use to remember the tasks
taskObj = function(typeName , shapeType , id , descrip)
{
	this.id = id;    //use to delete the taskobj for the ui-client
	this.descrip = descrip;
	this.typeName = typeName;
	this.shapeType = shapeType;
	this.lineWidth = 1;
	this.colorSet;
	
	this.setInfo = function()
	{
		this.lineWidth = m_lineWidth;
		this.colorSet = m_colorSet.colorHex();
	};
};

m_workerArray = new Array();    //use to remember the worker
m_picArray = new Array();       //use to remember the picture
m_picsetArray = new Array();     //use to remember the picture-foler has select

function size_bar_move(e){
	var thumb=$("#size_thumb");
	var main_w=$("#size_bar").width();
	var mainLeft=$("#size_bar").offset().left;
	if(e.clientX-mainLeft<0)
		thumb.css("left",-thumb.width()/2+"px");
	else if(e.clientX-mainLeft>main_w)
		thumb.css("left",main_w-thumb.width()/2+"px");
	else
		thumb.css("left",e.clientX-mainLeft-thumb.width()/2+"px");
	$("#size_span").html(Math.ceil($(thumb).position().left/main_w*5)+1);
	//change_attr(-1,$("#size_span").html(),-1);
	m_lineWidth = $("#size_span").html();
}

//获取控件的位置
function CPos(x, y) {
	this.x = x;
	this.y = y;
}

function GetObjPos(ATarget) {
	var target = ATarget;
	var pos = new CPos(target.offsetLeft, target.offsetTop);

	var target = target.offsetParent;
	while (target) {
		pos.x += target.offsetLeft;
		pos.y += target.offsetTop;
		target = target.offsetParent;
	}
	return pos;
}

//依次代表着曲线，直线，椭圆，矩形，多边形，不规则闭合区域
enumShape = {1:"CardinalSplineCurve" , 2:"Line" , 3:"Oval" , 4:"Rectangle" , 5:"MultiShape", 6:"Curve"};
//use the post methor to submit the data
function commintTasks()
{
	var descrip =$("#taskset_desp").attr("value");
	var tittle = $("#ts_tittle").attr("value");
	if(m_TaskArray.length == 0)
	{
		alert("No task has needed to commit!");
		return;
	}
	if(tittle == "")
	{
		alert("No tittle for current task-set!");
	}
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "tasksetControl";
	//format the task
	for (var i = 0; i < m_TaskArray.length; i++) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name = "task_name";
		//var shapeIndex = m_TaskArray[i].shapeType;
		myInput.value = "<Task name=\\\"" + m_TaskArray[i].typeName + "\\\" id=\\\""+ i + "\\\">" + 
		"<Shape Type=\\\"" + getShapName(m_TaskArray[i].shapeType) + "\\\" id=\\\"" + m_TaskArray[i].shapeType + "\\\"><Settings>" + 
	     "<Line Width=\\\"" + m_TaskArray[i].lineWidth +"\\\"><Color>" + m_TaskArray[i].colorSet + "</Color></Line>" + 
	      "<Fill FillType=\\\"false\\\"><Color></Color></Fill></Settings></Shape><despcription>"+m_TaskArray[i].descrip+"</despcription></Task>";
		myForm.appendChild(myInput);
	}
	var mydesp = document.createElement("input");
	mydesp.type = "text";
	mydesp.name = "desp";
	mydesp.value = descrip;
	myForm.appendChild(mydesp);
	var mytittle = document.createElement("input");
	mytittle.type = "text";
	mytittle.name = "tittle";
	mytittle.value = tittle;
	myForm.appendChild(mytittle);
	document.body.appendChild(myForm);
	myForm.submit();
	document.body.removeChild(myForm);
}

//use the post methor to update the tasksetData
function updateTSData()
{
	var descrip =$("#taskset_desp").attr("value");
	var tittle = $("#ts_tittle").attr("value");
	var tasketID = $("#taskset_id").attr("value");
	var mauthorID = $("#ts_authorID").attr("value");
	var mauthorName = $("#ts_author").attr("value");
	if(m_TaskArray.length == 0)
	{
		alert("No task has needed to commit!");
		return;
	}
	if(tittle == "")
	{
		alert("No tittle for current task-set!");
	}
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "tasksetControl";
	//format the task
	for (var i = 0; i < m_TaskArray.length; i++) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name = "task_name";
		//var shapeIndex = m_TaskArray[i].shapeType;
		myInput.value = "<Task name=\\\"" + m_TaskArray[i].typeName + "\\\" id=\\\""+ i + "\\\">" + 
		"<Shape Type=\\\"" + getShapName(m_TaskArray[i].shapeType) + "\\\" id=\\\"" + m_TaskArray[i].shapeType + "\\\"><Settings>" + 
	     "<Line Width=\\\"" + m_TaskArray[i].lineWidth +"\\\"><Color>" + m_TaskArray[i].colorSet + "</Color></Line>" + 
	      "<Fill FillType=\\\"false\\\"><Color></Color></Fill></Settings></Shape><despcription>"+m_TaskArray[i].descrip+"</despcription></Task>";
		myForm.appendChild(myInput);
	}
	var mydesp = document.createElement("input");
	mydesp.type = "text";
	mydesp.name = "desp";
	mydesp.value = descrip;
	myForm.appendChild(mydesp);
	var mytittle = document.createElement("input");
	mytittle.type = "text";
	mytittle.name = "tittle";
	mytittle.value = tittle;
	myForm.appendChild(mytittle);
	var myTSID = document.createElement("input");
	myTSID.type = "text";
	myTSID.name = "taskset_id";
	myTSID.value = tasketID;
	myForm.appendChild(myTSID);
	var authorID = document.createElement("input");
	authorID.type = "text";
	authorID.name = "authorID";
	authorID.value = mauthorID;
	myForm.appendChild(authorID);
	var authorName = document.createElement("input");
	authorName.type = "text";
	authorName.name = "authorName";
	authorName.value = mauthorName;
	myForm.appendChild(authorName);
	document.body.appendChild(myForm);
	myForm.submit();
	document.body.removeChild(myForm);
}

function getShapName(shapekind)
{
	var typeName;
	switch(shapekind)
	{
	case '1':
		typeName = "SplineCurve";
		break;
	case '2':
		typeName = "Line";
		break;
	case '3':
		typeName = "Oval";
		break;
	case '4':
		typeName = "Rectangle";
		break;
	case '5':
		typeName = "MultiShape";
		break;
	case '6':
		typeName = "Curve";
		break;
	default:
		typeName = "";
		break;
	}
	return typeName;
}

//删除任务
function deleteTaskWithID(contentID)
{
	var divCon = document.getElementById(contentID);
    if (divCon != null)
    	divCon.parentNode.removeChild(divCon);
    //删除数组中的taskset的记录
    for(var i = 0 ; i < m_TaskArray.length ; ++i)
    {
    	if(m_TaskArray[i].id == contentID)
    		m_TaskArray.splice(i, 1);
    }
}

//处理预加载时数据的重复添加(picture-set)
function dealTSReLoad(btnID)
{
	for(var index = 0 ; index < m_TaskArray.length ; ++index)
	{
		if(m_TaskArray[index].id == btnID)
			return true;
	}
	return false;
}


// generate the tasks in the web
function addTaskList()
{
	var taskName = $("#mTaskName").val();
	var toolName = $("#shapeChoose").find("option:selected").text();
	var toolSelect = $("#shapeChoose").select().val();
	var descrip =$("#ts_desp").attr("value");
	var toolSpanClass = null;
	switch (toolSelect) {
	case '1':
		toolSpanClass = "adminpencil";
		break;
	case '2':
		toolSpanClass = "adminline";
		break;
	case '3':
		toolSpanClass = "admincircle";
		break;
	case '4':
		toolSpanClass = "adminrect";
		break;
	case '5':
		toolSpanClass = "adminmultipoint";
		break;
	case '6':
		toolSpanClass = "adminirregular";
		break;
	default:
		break;
	}
	var id = "tsdesp_"+ getStringDate() + m_TaskArray.length;
	var newTask = new taskObj($("#mTaskName").val() , toolSelect , id , descrip);
	newTask.setInfo();
	var newli = document.createElement("div");
	newli.setAttribute("id", id);
	var deleBtn = document.createElement("input");
	deleBtn.setAttribute("type" , "button");
	deleBtn.setAttribute("class" , "button");
	deleBtn.setAttribute("value" , "删除");
	deleBtn.setAttribute("style", "padding-left:10px;padding-right:10px;");
	deleBtn.setAttribute("onclick", "deleteTaskWithID('"+ id + "')");
	newli.appendChild(deleBtn);
	var tipA = document.createElement("a");
	tipA.setAttribute("class", "tooltipsy");
	tipA.setAttribute("tittle", descrip);
	tipA.innerHTML = taskName;
	
	//tittleSpan.innerHTML = taskName;

	//tipA.appendChild(tipA);
	newli.appendChild(tipA);
	var toolSpan = document.createElement("span");
	toolSpan.setAttribute("class", toolSpanClass);
	newli.appendChild(toolSpan);
	var colorInput = document.createElement("input");
	colorInput.setAttribute("class", "color_span");
	colorInput.setAttribute("style", "background-color:" + newTask.colorSet);
	colorInput.setAttribute("value", m_lineWidth);
	colorInput.setAttribute("readonly", "");
	newli.appendChild(colorInput);
	$("#taskListContent").append(newli);
	//add the newTask to the
	m_TaskArray.push(newTask);
	$("#tooltipsy").tooltipsy();
}

//to make the code can run on chrome
var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
String.prototype.colorHex = function(){
	var that = this;
	if(/^(rgb|RGB)/.test(that)){
		var aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g,"").split(",");
		var strHex = "#";
		for(var i=0; i<aColor.length; i++){
			var hex = Number(aColor[i]).toString(16);
			if(hex === "0"){
				hex += hex;	
			}
			strHex += hex;
		}
		if(strHex.length !== 7){
			strHex = that;	
		}
		return strHex;
	}else if(reg.test(that)){
		var aNum = that.replace(/#/,"").split("");
		if(aNum.length === 6){
			return that;	
		}else if(aNum.length === 3){
			var numHex = "#";
			for(var i=0; i<aNum.length; i+=1){
				numHex += (aNum[i]+aNum[i]);
			}
			return numHex;
		}
	}else{
		return that;
	}
};

//用于记录用户的点击时间
mTSSelect = new Array();
mPSSelect = new Array();
mPerSSelect = new Array();
mPLSelect = new Array();

function tasksetClick(btnID)
{
	var pattern = "ts_";
	btnID = btnID.replace(new RegExp(pattern), "");
	for ( var index = 0; index < mTSSelect.length; ++index) {
		if (mTSSelect[index] == btnID) {
			mTSSelect.splice(index, 1);
		}
	}
	if ($("#ts_" + btnID).attr("checked") == "checked")
		mTSSelect.push(btnID);
}

function picSetSelClick(btnID)
{
	var pattern = "psl_";
	btnID = btnID.replace(new RegExp(pattern), "");
	if(mPSSelect.length == 1)
	{
		if(mPSSelect[0] == btnID)
			;
		else
			$("#psl_"+mPSSelect[0]).attr("checked",false);
		mPSSelect.splice(0, 1);
		if($("#psl_" + btnID).attr("checked") == "checked")
			mPSSelect.push(btnID);
	}
	else
	{
		mPSSelect.push(btnID);
	}
	//for ( var index = 0; index < mPSSelect.length; ++index) {
	//	if (mPSSelect[index] == btnID) {
	//		mPSSelect.splice(index, 1);
	//	}
	//}
	//if ($("#psl_" + btnID).attr("checked") == "checked")
	//	mPSSelect.push(btnID);
}

function perListClick(btnID)
{
	var pattern = "pl_";
	btnID = btnID.replace(new RegExp(pattern), "");
	for ( var index = 0; index < mPLSelect.length; ++index) {
		if (mPLSelect[index] == btnID) {
			mPLSelect.splice(index, 1);
		}
	}
	if ($("#pl_" + btnID).attr("checked") == "checked")
		mPLSelect.push(btnID);
}

function persetselClick(btnID)
{
	var pattern = "pl_";
	btnID = btnID.replace(new RegExp(pattern), "");
	
	if(mPerSSelect.length == 1)
	{
		if(mPerSSelect[0] == btnID)
			;
		else
			$("#pl_"+mPerSSelect[0]).attr("checked",false);
		mPerSSelect.splice(0, 1);
		if($("#pl_" + btnID).attr("checked") == "checked")
			mPerSSelect.push(btnID);
	}
	else
	{
		mPerSSelect.push(btnID);
	}
}

//发布任务集到服务器
function releaseToServer()
{
	if(mTSSelect.length == 0)
	{
		alert("No task-set has been has selected!");
		return;
	}
	if(mPSSelect.length == 0)
	{
		alert("No picture-set has been has selected!");
		return;
	}
	if(mPerSSelect.length == 0)
	{
		alert("No person has been has selected!");
		return;
	}
	
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "relTSControl";
	//format the task-set
	for ( var index = 0; index < mTSSelect.length; ++index) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name = "taskSet_ID";
		myInput.value = mTSSelect[index];
		myForm.appendChild(myInput);
	}
	
	//format the picture-set
	for(var index = 0 ; index < mPSSelect.length ; ++index ) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name="pictureSet_ID";
		myInput.value = mPSSelect[index];
		myForm.appendChild(myInput);
	}
	
	//format the person
	for(var index = 0 ; index < mPerSSelect.length ; ++index) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name="personlist_ID";
		myInput.value = mPerSSelect[index];
		myForm.appendChild(myInput);
	}
    
	//commint the data to server
	document.body.appendChild(myForm);
	myForm.submit();
	document.body.removeChild(myForm);
}

//得到服务器端文件列表
function getFilelist(filePath)
{
	var pattern = new RegExp(".bmp|.gif|.jpeg|.jpg|.png|.jpg|.tiff|.psd|.svg|.pcx|.dxf|.wmf|.emf|.lic|.eps|.tga");
	if(pattern.exec(filePath) != null)
	{
		window.open('adminimgView.jsp?file='+filePath,'_blank');
	}
	else
	{
		$.ajax({
			type : "POST",
			url : "adminfileshow.jsp",
			data : {'parent' : filePath},
			success : function(msg) {
				if(msg == null)
				{
					alert("Error");
				}
				else
	 			{
					var fileV = document.getElementById("FVContent");
					fileV.innerHTML=msg;
				}
			}
		});
	}
}

function getReadFilelist(filePath)
{
	var pattern = new RegExp(".bmp|.gif|.jpeg|.jpg|.png|.jpg|.tiff|.psd|.svg|.pcx|.dxf|.wmf|.emf|.lic|.eps|.tga");
	if(pattern.exec(filePath) != null)
	{
		window.open('adminimgView.jsp?file='+filePath,'_blank');
	}
	else
	{
		$.ajax({
			type : "POST",
			url : "adminsimplefs.jsp",
			data : {'parent' : filePath},
			success : function(msg) {
				if(msg == null)
				{
					alert("Error");
				}
				else
	 			{
					var fileV = document.getElementById("FVContent");
					fileV.innerHTML=msg;
				}
			}
		});
	}
}

//delete div
function deleteDiv(divID)
{
	var judgement=arguments[1]?arguments[1]:false;           //set the second default argument
	if(!judgement)
	{
		for(var cnt = 0 ; cnt < m_picsetArray.length ; ++cnt)
			if (m_picsetArray[cnt].fileListBtn == divID)
			{
				$("#"+m_picsetArray[cnt].fileListBtn).attr("checked",false);
				m_picsetArray.splice(cnt, 1);
			}
	}
	var divCon = document.getElementById("con_"+divID);
    if (divCon != null)
    	divCon.parentNode.removeChild(divCon);
}

function addPicture(pictureFolder)
{
	var divCon = document.getElementById("ps_review");
	//var childNodes = divCon.childNodes;
	var length = m_picsetArray[m_picsetArray.length-1].fileListBtn;
	var tempcontent = "<div id=\"con_" + length + "\">";
	tempcontent += "<div id=\"ps_"+ length+"\" class=\"togetherdelete\"><input  type=\"button\" value=\"Delete\" onclick=\"deleteDiv('" + length + "')\"></div>";
	tempcontent += "<div id=\"ps_content\" class=\"togethercontent\" style=\"margin-left: 0px;\"><a onclick=\"getFilelist('"+pictureFolder+"')\" class=\"filefolder\">/"+pictureFolder+"</a></div>";
	tempcontent += "</div>";
	divCon.innerHTML += tempcontent;
}

function picSetClick(btnID)
{
	var selectBtn = document.getElementById(btnID);
	var fileFolder = selectBtn.getAttribute("value");
	for ( var cnt = 0; cnt < m_picsetArray.length; ++cnt) {
		if (m_picsetArray[cnt].position == fileFolder) {
			deleteDiv(m_picsetArray[cnt].fileListBtn , true);
			m_picsetArray.splice(cnt, 1);
		}
	}
	
	if(repeatJudge(fileFolder))
	{
		alert("当前目录已被包含！");
		$("#"+btnID).attr("checked",false);
	}
	else
	{
		if ($("#" + btnID).attr("checked") == "checked")
		{
			m_picsetArray.push(new picPosition(fileFolder , btnID));
			addPicture(fileFolder);
		}
			
		//之前目录是否被当前目录所包含
		var currentFolder = new RegExp(fileFolder);
		for(var cnt = 0 ; cnt < m_picsetArray.length-1; ++cnt)
		{
			var array = currentFolder.exec(m_picsetArray[cnt].position);
			if(array != null)
			{
				deleteDiv(m_picsetArray[cnt].fileListBtn , true);
				m_picsetArray.splice(cnt, 1);
				--cnt;
			}
		}
	}
}

function repeatJudge(fileFolder)
{
	for ( var cnt = 0; cnt < m_picsetArray.length; ++cnt) {
		var testFolder = new RegExp(m_picsetArray[cnt].position);
		var array = testFolder.exec(fileFolder);
		if(array != null)
		{
			return true;
		}
	}
	return false;
}

picPosition = function( position , btnID)
{
	this.fileListBtn = btnID;
	this.position = position;
};

function toJSON(obj) {
	var json = '({';
	$.each(obj, function(k, v) {
		var q = typeof v == 'string' ? ~v.indexOf("'") ? '"' : "'" : '';
		if (typeof v == 'object')
			v = toJSON(v).slice(0, -1).substr(1);
		json += k + ':' + q + v + q + ',';
	});
	return json.slice(0, -1) + '})';
}

function postPSToserver()
{
	var tittle = $("#ps_tittle").attr("value");
	var desption = $("#ps_desp").val();
	var fileArray= new Array();
	//judgement
	if(tittle == "")
	{
		alert("No tittle");
		return;
	}
	if(desption == "")
	{
		alert("No description!");
		return;
	}
	if(m_picsetArray.length == 0)
	{
		alert("No picture has selected!");
		return;
	}
	//format the data
	for(var cnt = 0 ; cnt < m_picsetArray.length ; ++cnt)
	{
		fileArray.push(m_picsetArray[cnt].position);
	}
	
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "picssetControl";
	//format the data
	var tittleIn = document.createElement("input");
	tittleIn.type = "text";
	tittleIn.name = "ps_tittle";
	tittleIn.value = tittle;
	myForm.appendChild(tittleIn);
	var despIn = document.createElement("input");
	despIn.type = "text";
	despIn.name = "ps_desp";
	despIn.value = desption;
	myForm.appendChild(despIn);
	var fileFolder = document.createElement("input");
	fileFolder.type = "text";
	fileFolder.name = "ps_fileFolder";
	fileFolder.value = toJSON(fileArray);
	myForm.appendChild(fileFolder);
	document.body.appendChild(myForm);
	//submint the data
	myForm.submit();
	document.body.removeChild(myForm);
}

//处理预加载时数据的重复添加(picture-set)
function dealReLoad(positsion)
{
	for(var index = 0 ; index < m_picsetArray.length ; ++index)
	{
		if(m_picsetArray[index].position == positsion)
			return true;
	}
	return false;
}

function updatePictureset()
{
	var tittle = $("#ps_tittle").attr("value");
	var desption = $("#ps_desp").val();
	var picset_id = $("#pictureset_id").attr("value");
	var fileArray= new Array();
	//judgement
	if(tittle == "")
	{
		alert("No tittle");
		return;
	}
	if(desption == "")
	{
		alert("No description!");
		return;
	}
	if(m_picsetArray.length == 0)
	{
		alert("No picture has selected!");
		return;
	}
	if(picset_id == "")
	{
		alert("current operator is not correct!");
		return;
	}
	//format the data
	for(var cnt = 0 ; cnt < m_picsetArray.length ; ++cnt)
	{
		fileArray.push(m_picsetArray[cnt].position);
	}
	
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "picssetControl";
	//format the data
	var tittleIn = document.createElement("input");   //tittle
	tittleIn.type = "text";
	tittleIn.name = "ps_tittle";
	tittleIn.value = tittle;
	myForm.appendChild(tittleIn);
	var despIn = document.createElement("input");     //descriptions
	despIn.type = "text";
	despIn.name = "ps_desp";
	despIn.value = desption;
	myForm.appendChild(despIn);
	var fileFolder = document.createElement("input");  //file folder
	fileFolder.type = "text";
	fileFolder.name = "ps_fileFolder";
	fileFolder.value = toJSON(fileArray);
	myForm.appendChild(fileFolder);
	var picsetID = document.createElement("input");    //picture-set id
	picsetID.type = "text";
	picsetID.name = "ps_picsetid";
	picsetID.value = picset_id;
	myForm.appendChild(picsetID);
	
	document.body.appendChild(myForm);
	//submint the data
	myForm.submit();
	document.body.removeChild(myForm);
}

//yyyymmddhhmmss
function getStringDate() {
    var date = new Date();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
	var hours = date.getHours();
	var minutes = date.getMinutes();
	var second = date.getSeconds();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
	if(hours >= 0 && hours <= 9)
		hours = "0" + hours;
	if(minutes >= 0 && minutes <= 9)
		minutes = "0" + minutes;
	if(second >= 0 && second <= 9)
		second = "0" + second;
	
    var currentdate = date.getFullYear() + month +  strDate
            +  hours +  minutes +second;
    return currentdate;
}

//post the person list to the server
function commintperlist()
{
	var tittle =$("#pers_tittle").attr("value");
	var descrip = $("#pers_desp").attr("value");
	if(mPLSelect.length == 0)
	{
		alert("No persons has selected!");
		return;
	}
	if(tittle == "")
	{
		alert("No tittle for current person set!");
		return;
	}
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "persetControl";
	
	//format the persion
	for(var index = 0 ; index < mPLSelect.length ; ++index) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name="personlist_ID";
		myInput.value = mPLSelect[index];
		myForm.appendChild(myInput);
	}
	var mydesp = document.createElement("input");
	mydesp.type = "text";
	mydesp.name = "desp";
	mydesp.value = descrip;
	myForm.appendChild(mydesp);
	var mytittle = document.createElement("input");
	mytittle.type = "text";
	mytittle.name = "tittle";
	mytittle.value = tittle;
	myForm.appendChild(mytittle);
	document.body.appendChild(myForm);
	myForm.submit();
	document.body.removeChild(myForm);
}


function updatePerSet()
{
	var tittle =$("#pers_tittle").attr("value");
	var descrip = $("#pers_desp").attr("value");
	var persetID = $("#ps_personsetID").attr("value");
	if(mPLSelect.length == 0)
	{
		alert("No persons has selected!");
		return;
	}
	if(tittle == "")
	{
		alert("No tittle for current person set!");
		return;
	}
	var myForm = document.createElement("form");
	myForm.method = "post";
	myForm.action = "persetControl";
	
	//format the persion
	for(var index = 0 ; index < mPLSelect.length ; ++index) {
		var myInput = document.createElement("input");
		myInput.type = "text";
		myInput.name="personlist_ID";
		myInput.value = mPLSelect[index];
		myForm.appendChild(myInput);
	}
	var mydesp = document.createElement("input");
	mydesp.type = "text";
	mydesp.name = "desp";
	mydesp.value = descrip;
	myForm.appendChild(mydesp);
	var mytittle = document.createElement("input");
	mytittle.type = "text";
	mytittle.name = "tittle";
	mytittle.value = tittle;
	myForm.appendChild(mytittle);
	var myPSID = document.createElement("input");
	myPSID.type = "text";
	myPSID.name="ps_personsetID";
	myPSID.value = persetID;
	myForm.appendChild(myPSID);
	document.body.appendChild(myForm);
	myForm.submit();
	document.body.removeChild(myForm);
}