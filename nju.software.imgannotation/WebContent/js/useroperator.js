//use to commit the tasks to the serer	
function nextPicture(type)
{
	cleanAllAction();
	if (type == 1) {
		postDatatoServer();
	}
	else {
		$("#picShow").attr("src", "");
		getNextPictureData();
	}
}

//clean the child action
function cleanAllAction()
{
	var mapLength = m_mapindex.length;
	for(var tempcnt = 0 ; tempcnt < mapLength ; ++tempcnt)
	{
		for(var key in m_mapindex[tempcnt]){
			var divCon = document.getElementById("div_"+key);
			if (divCon != null)
				divCon.parentNode.removeChild(divCon);
		}
		m_mapindex[tempcnt]= new Array();
	}
}

//use to get the next picture with post no data
function getNextPictureData()
{
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	var psID = $("#current_pics_ID").attr("value");
	var userID = $("#user_id").attr("value");
	$.ajax({
		type : "POST",
		url : "reqNewPic",
		data : {'tasksetID' : taskSetID, 'psID' : psID , 'picID' : picID , 'userID':userID},
		dataType: 'json',
		success : function(msg) {
			if(msg == null)
			{
				alert("Last one");
				window.location.href='commtaskshow.jsp';
			}
			else
 			{
				 $("#current_pic_ID").attr("value" , msg.id);
				 $("#current_pics_ID").attr("value" , msg.psid);
				 $("#picShow").attr("src", msg.path);
				 cleanCurrentPicture();
				 showUserOperData(msg.shapeList , msg.width , msg.height);
			}
		}
	});
}

//use to generate the xml data to the server
function genPostDataTwo(taskindex , widthIn , heightIn)
{
	var time = getNowFormatDate();
	var formatData="";
	//rectangle and the the oval has the same data structure, put together
	if(m_recordType[taskindex].shapeID == enumType.RECT || m_recordType[taskindex].shapeID == enumType.CIRCLE)
	{
		formatData = "<author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><Data>";
		for(var tempIndex=0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData + "<Extend>" + 
				"<start X=\"" + parseInt(m_recordData[taskindex][tempIndex].startX*widthIn) + "\" Y=\"" + parseInt(m_recordData[taskindex][tempIndex].startY*heightIn) +"\"></start><end X=\"" + parseInt(m_recordData[taskindex][tempIndex].endX * widthIn)+
				"\" Y=\"" + parseInt(m_recordData[taskindex][tempIndex].endY*heightIn) +"\"></end></Extend>";
		}
		formatData =formatData +  "</Data>";
	}
	else if(m_recordType[taskindex].shapeID == enumType.LINE)
	{
		formatData = "<author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><Data>";
		for(var tempIndex=0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData +"<Extend><points><point X=\"" + parseInt(m_recordData[taskindex][tempIndex].startX*widthIn) + "\" Y=\"" +
			parseInt(m_recordData[taskindex][tempIndex].startY*heightIn) +"\"></point>" + 
				"<point X=\"" + parseInt(m_recordData[taskindex][tempIndex].endX*widthIn)+ "\" Y=\"" + parseInt(m_recordData[taskindex][tempIndex].endY*heightIn) +"\"></point>" + "</points></Extend>";
		}
		formatData =formatData +  "</Data>";
	}
	else
	{
		formatData = "<author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><Data>";
		for(var tempIndex = 0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData + "<Extend><points>";
			for(var tempCnt = 0  ; tempCnt < m_recordData[taskindex][tempIndex].length ; ++tempCnt)
			{
				formatData = formatData + "<point X=\"" + parseInt(m_recordData[taskindex][tempIndex][tempCnt].x*widthIn) +"\" Y=\""+
				parseInt(m_recordData[taskindex][tempIndex][tempCnt].y*heightIn) +"\"></point>";
			}
			formatData = formatData + "</points></Extend>";
		}
		formatData =formatData +  "</Data>";
	}
	return formatData;
}

//use to connect the data
function postDatatoServer()
{
	var img = new Image();
	img.src = $("#picShow").attr("src");
	var widthIn = img.width/imgSWidth;               //to the real data
	var heightIn = img.height/imgStHeight;
	
	$("#picShow").attr("src", "");
	var taskDataInfo = new Array();
	var taskDataID = new Array();
	for(var tempIndex = 0 ; tempIndex < m_recordType.length ; ++tempIndex)
	{
		taskDataID.push(m_recordType[tempIndex].indexID);        //当前任务中该任务的编号
		taskDataInfo.push(genPostDataTwo(tempIndex , widthIn , heightIn));
	}

	taskDataID = toJSON(taskDataID);
	taskDataInfo = toJSON(taskDataInfo);
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	var psID = $("#current_pics_ID").attr("value");
	var userID = $("#user_id").attr("value");
	$.ajax({
		type : "POST",
		url : "taskdataControl",
		data : {'tasksetID' : taskSetID, 'psID' : psID , 'picID' : picID , 'userID':userID , 'taskDataID' : taskDataID , 'taskDataInfo' : taskDataInfo},
		dataType: 'json',
		success : function(msg) {
			if(msg == null)
			{
				alert("Last one");
				window.location.href='commtaskshow.jsp';
			}
			else
 			{
				 $("#current_pic_ID").attr("value" , msg.id);
				 $("#current_pics_ID").attr("value" , msg.psid);
				 $("#picShow").attr("src", msg.path);
				 cleanCurrentPicture();
				 showUserOperData(msg.shapeList , msg.width , msg.height);
				 //init the tools
				 //$("#task_list_size li:first input").click();
			}
		}
	});
}

//clean the canvase
function cleanCurrentPicture()
{
	//clean the canvas
	context.clearRect(0, 0, canvas_size.x, canvas_size.y);
	context2.clearRect(0, 0, canvas_size.x, canvas_size.y);
	
	//clean the data
	//m_recordData = new Array();                     //use to record the use information
	//m_recordType = new Array();
	for ( var i = 0; i < m_recordType.length; ++i) {
		m_recordData[i] = new Array();
		m_recordType[i] = new pRecordInfo();
	}
	
	//init the tools
	$("#task_list_size li:first input").click();
}

//clean select
function cleanSelectArray(id)
{
	m_recordData[id] = new Array();
	//btnID is the task ID
	if($("#color_span_"+id).attr("class") == enumType.QUAD)
		m_recordData[id].push(new Array());
	context2.clearRect(0, 0, canvas_size.x, canvas_size.y);
	//delete the child div
	for(var key in m_mapindex[id]){
		var divCon = document.getElementById("div_"+key);
	    if (divCon != null)
	    	divCon.parentNode.removeChild(divCon);
	}
	m_mapindex[id] = new Array();
	redraw();
}

//load the current picture operator data
function showUserOperData(opeData  , imgRWidth , imgRHeight)
{
	if(imgRHeight == "-1" || imgRHeight == "-1")  //not correct
		return;
	if(opeData == null)
		return;

	var widthIn = imgSWidth/imgRWidth;               //to the real data
	var heightIn = imgStHeight/imgRHeight;
	
	for ( var index = 0; index < opeData.length; ++index) 
	{
		if (opeData[index].data != "") {
			m_recordType[opeData[index].taskID].setIndexID(opeData[index].taskID);
			m_recordType[opeData[index].taskID].setShapeID(opeData[index].shapeID);
			m_recordType[opeData[index].taskID].setStyle(opeData[index].lineColor);
			m_recordType[opeData[index].taskID].setLineWidth(opeData[index].lineWidth);
			var data = opeData[index].data.split("&");

			for ( var cnt = 0; cnt < data.length; ++cnt) {
				if (data[cnt] != "") {
					var pointData = data[cnt].split(",");

					if (pointData.length >= 2) {
						// format the data
						if (opeData[index].shapeID == enumType.RECT) {
							var tempRect = new pRectangle(parseInt(pointData[0]*widthIn),
									parseInt(pointData[1]*heightIn), parseInt(pointData[2]*widthIn), parseInt(pointData[3]*heightIn),
									context);
							m_recordData[opeData[index].taskID].push(tempRect);
							addMapkey(opeData[index].taskID);
						} else if (opeData[index].shapeID == enumType.CIRCLE) {
							var tempEll = new pEllipse(parseInt(pointData[0]*widthIn),
									parseInt(pointData[1]*heightIn), parseInt(pointData[2]*widthIn), parseInt(pointData[3]*heightIn),
									context);
							m_recordData[opeData[index].taskID].push(tempEll);
							addMapkey(opeData[index].taskID);
						} else if (opeData[index].shapeID == enumType.LINE) {
							m_recordData[opeData[index].taskID].push(new pLine(
									parseInt(pointData[0]*widthIn),
									parseInt(pointData[1]*heightIn), parseInt(pointData[2]*widthIn), parseInt(pointData[3]*heightIn),
									context));
							addMapkey(opeData[index].taskID);
						} else if (opeData[index].shapeID == enumType.IRREGULAR) {
							m_recordData[opeData[index].taskID]
									.push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_recordData[opeData[index].taskID][m_recordData[opeData[index].taskID].length - 1]
										.push(new pCurvedLine(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn)));
							}
							addMapkey(opeData[index].taskID);
						} else if (opeData[index].shapeID == enumType.CURVE) {
							m_recordData[opeData[index].taskID].push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_recordData[opeData[index].taskID][m_recordData[opeData[index].taskID].length - 1]
										.push(new pCurvedLine(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn)));
							}
							addMapkey(opeData[index].taskID);
						} else if (opeData[index].shapeID == enumType.QUAD && pointData[0]) {
							m_recordData[opeData[index].taskID].push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_recordData[opeData[index].taskID][m_recordData[opeData[index].taskID].length - 1]
										.push(new pCircle(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn),
												quadPSize, context));
							}
							addMapkey(opeData[index].taskID);
							//m_recordData[opeData[index].taskID].push(new Array());
						}
					}
				}
			}
		}
	}
	redraw();
}

//get current picture data when the web first load
function getCurrentPicData()
{
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	var psID = $("#current_pics_ID").attr("value");
	var userID = $("#user_id").attr("value");
	$.ajax({
		type : "POST",
		url : "curPicDataInfo",
		data : {'tasksetID' : taskSetID, 'psID' : psID , 'picID' : picID , 'userID':userID},
		dataType: 'json',
		success : function(msg) {
			if(msg == null)
			{
			}
			else
 			{
				showUserOperData(msg.shapeList, msg.width,
						msg.height);
			}
		}
	});
}

