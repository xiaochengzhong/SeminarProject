//use the object to define the quad point
pCircle = function( x , y , rad , contextObj){
	this.x=x;
	this.y=y;
	this.r=rad;
	this.ctx = contextObj;
	this.hasrepeat = false;
	this.drawC = function(){
		this.ctx.beginPath();
		this.ctx.arc(this.x,this.y,this.r,2*Math.PI,0,false);
		this.ctx.stroke();
	};
	//current positon is in the circle
	this.isInPCircle = function(x , y)
	{
		var grapX = x -this.x;
		var grapY = y - this.y;
		if(grapX > -(this.r) && grapX < this.r && grapY > -(this.r) && grapY < this.r)
		{
			this.ctx.beginPath();
			this.ctx.save();
			this.ctx.strokeStyle = 'rgba(255, 0 ,0,1)';
			this.ctx.arc(this.x,this.y,this.r,2*Math.PI,0,false);
			this.ctx.stroke();
			this.ctx.restore();
			this.hasrepeat = true;
			return true;
		}
		if(this.hasrepeat)
		{  //point no in the area , but the area has marked
			this.drawC();
			this.hasrepeat= false;
		}
		return false;
	};
	this.getRepeat = function()
	{
		return this.hasrepeat;
	};
	//set the context3
	this.setContext = function(context)
	{
		this.ctx = context;
	};
};
//define the rectangle object
pRectangle = function(startX , startY , endX , endY , contextObj)
{
	//define the data
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	this.context = contextObj;
	
	//draw the rectangle
	this.drawRect = function()
	{
		this.context.beginPath();
		this.context.rect(this.startX,this.startY,this.endX-this.startX,this.endY - this.startY);
		this.context.closePath();
		this.context.stroke();
	};
	
	//set the context3
	this.setContext = function(context)
	{
		this.context = context;
	};
};

//define the ellipse object 
pEllipse = function(startX , startY , endX , endY , contextObj)
{
	//define the data
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	this.context = contextObj;
	
	//draw the ellipse in the context
	this.drawEll = function()
	{
		var k = ((this.endX-this.startX)/0.75)/2 , w = (this.endX-this.startX)/2 , h = (this.endY-this.startY)/2 , x=(this.endX+this.startX)/2 , y=(this.endY+this.startY)/2;
		this.context.beginPath();
		this.context.moveTo(x, y-h);
		this.context.bezierCurveTo(x+k, y-h, x+k, y+h, x, y+h);
		this.context.bezierCurveTo(x-k, y+h, x-k, y-h, x, y-h);
		this.context.closePath();
		this.context.stroke();
	};
	
	//set the context3
	this.setContext = function(context)
	{
		this.context = context;
	};
};

//record the line
pLine = function(startX , startY , endX , endY , contextObj)
{
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	this.context = contextObj;
	
	this.draw = function()
	{
		this.context.beginPath();
		this.context.moveTo(this.startX,this.startY);
		this.context.lineTo(this.endX,this.endY);
		this.context.stroke();
	};

    //set the context3
	this.setContext = function(context)
	{
		this.context = context;
	};
};

//pencil
pCurvedLine = function(pointX , pointY)
{
	this.x = pointX;
	this.y = pointY;
	
	this.hash = function()
	{
		return this.x + "" + this.y;
	};
};

pRecordInfo = function()
{
	this.indexID = -1;
	this.shapeID = -1;
	this.style = "";
	this.lineWidth = "";
	
	this.setIndexID = function(indexID)
	{
		this.indexID = indexID;
	};
	
	this.setShapeID = function(shapeID)
	{
		this.shapeID = shapeID;
	};
	
	this.setStyle = function(style)
	{
		this.style = style;
	};
	
	this.setLineWidth = function(width)
	{
		this.lineWidth = width;
	};
};

pPersonData = function() {
	this.pID = null;
	this.m_recordData = new Array();
	this.m_recordType = new Array();
	this.initRecordData = function(pID , taskLength)
	{
		this.pID = pID;
		for(var initIndex = 0 ; initIndex < taskLength ; ++initIndex )
		{
			this.m_recordData.push(new Array());
			this.m_recordType.push(new pRecordInfo());
		}
	};
};

enumType = {CURVE:1 , LINE:2 ,  CIRCLE:3 , RECT:4 , QUAD:5 , IRREGULAR: 6};

m_personData = new Array();
m_recordType = new Array();
m_currentPersonID = null;
m_personIndex = null;

$(document).ready(function(){
	$("#drawPanel").css({"top": $("#picShow")[0].offsetTop , "left": $("#picShow")[0].offsetLeft});
    imgSWidth = $("#picShow").css("width").replace("px" , "");
    imgStHeight = $("#picShow").css("height").replace("px" , "");
	
    type=enumType.CURVE;
    //$("#tasklistShow").jscroll({W:"12px",Btn:{btn:false}});

	document.ondragstart=function(){return false;};// not allow the drag move
	document.onselectstart=function(){return false;};//

	canvas_size={x:$("#canvas_drawPanel").width(),y:$("#canvas_drawPanel").height()};  //draw panel size
	origin={x:0,y:0};
	end={x:0,y:0};
	
    drawable=false;

	quadPSize = 3;
	quad_PrePoint = {x:0 , y:0};

	m_recordData = new Array();                     //use to record the use information
	
	canvas=document.getElementById("canvas_drawPanel");         //draw on the image(the finally data)


	context=canvas.getContext("2d");
	context.strokeStyle="#00aeef";                       //set the pencil style
	fill_canvas("#ffffff");                              //clean the canvas
	context.lineWidth = 1;                               //define the line width

    //init the current data
	initData();
	getCurrentPersonPicData();       //update current data
});

//get current picture data when the web first load
function getCurrentPersonPicData()
{
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	var psID = $("#current_pics_ID").attr("value");
	var userID = m_currentPersonID;
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

//get the next picture
function nextPicture()
{
	$("#picShow").attr("src", "");
	//use to get the next picture with post no data
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	var psID = $("#current_pics_ID").attr("value");
	var userID = m_currentPersonID;
	$.ajax({
		type : "POST",
		url : "adminreqnextpic",
		data : {'tasksetID' : taskSetID, 'psID' : psID , 'picID' : picID , 'userID':userID},
		dataType: 'json',
		success : function(msg) {
			if(msg == null)
			{
				alert("Last one");
				window.location.href='adminTSList.jsp';
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
			m_personData[m_personIndex].m_recordType[opeData[index].taskID]
					.setIndexID(opeData[index].taskID);
			m_personData[m_personIndex].m_recordType[opeData[index].taskID]
					.setShapeID(opeData[index].shapeID);
			m_personData[m_personIndex].m_recordType[opeData[index].taskID]
					.setStyle(opeData[index].lineColor);
			m_personData[m_personIndex].m_recordType[opeData[index].taskID]
					.setLineWidth(opeData[index].lineWidth);
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
							m_personData[m_personIndex].m_recordData[opeData[index].taskID].push(tempRect);
						} else if (opeData[index].shapeID == enumType.CIRCLE) {
							var tempEll = new pEllipse(parseInt(pointData[0]*widthIn),
									parseInt(pointData[1]*heightIn), parseInt(pointData[2]*widthIn), parseInt(pointData[3]*heightIn),
									context);
							m_personData[m_personIndex].m_recordData[opeData[index].taskID].push(tempEll);
						} else if (opeData[index].shapeID == enumType.LINE) {
							m_personData[m_personIndex].m_recordData[opeData[index].taskID].push(new pLine(
									parseInt(pointData[0]*widthIn),
									parseInt(pointData[1]*heightIn), parseInt(pointData[2]*widthIn), parseInt(pointData[3]*heightIn),
									context));
						} else if (opeData[index].shapeID == enumType.IRREGULAR) {
							m_personData[m_personIndex].m_recordData[opeData[index].taskID]
									.push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_personData[m_personIndex].m_recordData[opeData[index].taskID][m_personData[m_personIndex].m_recordData[opeData[index].taskID].length - 1]
										.push(new pCurvedLine(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn)));
							}
						} else if (opeData[index].shapeID == enumType.CURVE) {
							m_personData[m_personIndex].m_recordData[opeData[index].taskID].push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_personData[m_personIndex].m_recordData[opeData[index].taskID][m_personData[m_personIndex].m_recordData[opeData[index].taskID].length - 1]
										.push(new pCurvedLine(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn)));
							}
						} else if (opeData[index].shapeID == enumType.QUAD && pointData[0]) {
							m_personData[m_personIndex].m_recordData[opeData[index].taskID]
									.push(new Array());
							for ( var pointcnt = 0; pointcnt < Math.floor(pointData.length/2); ++pointcnt) {
								m_personData[m_personIndex].m_recordData[opeData[index].taskID][m_personData[m_personIndex].m_recordData[opeData[index].taskID].length - 1]
										.push(new pCircle(
												parseInt(pointData[2 * pointcnt]*widthIn),
												parseInt(pointData[2 * pointcnt + 1]*heightIn),
												quadPSize, context));
							}
							m_personData[m_personIndex].m_recordData[opeData[index].taskID]
									.push(new Array());
						}
					}
				}
			}
		}
	}
	redraw();
}

function changeStatus(btnID)
{
	m_recordType[btnID] = !m_recordType[btnID];
	redraw();
}

//change the the user
function changePerson(pID)
{
	var getThePerson = false;
	m_currentPersonID = pID;
	for(var index = 0 ; index < m_personData.length ; ++index)
	{
		if(m_personData[index].pID == pID)
		{
			m_personIndex = index;
			getThePerson = true;
			break;      //end the search
		}
	}
	if(!getThePerson)
	{
		m_personData.push(new pPersonData());
		m_personIndex = m_personData.length-1;
		m_personData[m_personIndex].initRecordData(pID , $("#task_list_size").attr("class"));
		//get the person data
		clear_canvas();
		getCurrentPersonPicData();
	}
	//redraw the person data
	redraw();
}

function clear_canvas(){
	context.clearRect(0,0,canvas_size.x,canvas_size.y);
}

//clean the canvase
function cleanCurrentPicture()
{
	//clean the canvas
	context.clearRect(0, 0, canvas_size.x, canvas_size.y);
	
	//clean the data
	m_personData = new Array();
	m_personData.push(new pPersonData());
	m_personIndex = m_personData.length-1;
	m_personData[m_personIndex].initRecordData(m_currentPersonID , $("#task_list_size").attr("class"));
}

function fill_canvas(col,orix,oriy,w,h){
	context.fillStyle=col;
	context.fillRect(orix,oriy,w,h);
}

function initData()
{
	//return recordData[enumType.CURVE];
	$("#person_list_size li:first input").click();
	for(var initIndex = 0 ; initIndex < $("#task_list_size").attr("class") ; ++initIndex )
	{
		m_recordType.push(false);
	}
	$("#task_list_size li:first input").click();
}

//draw line
function pDrawLine(startX , startY , endX , endY , contextObj)
{
	contextObj.beginPath();
    contextObj.moveTo(startX,startY);
    contextObj.lineTo(endX,endY);
	contextObj.stroke();
}

// just for the test
function redraw() {
	context.clearRect(0, 0, canvas_size.x, canvas_size.y);

	for ( var i = 0; i < m_personData[m_personIndex].m_recordData.length; ++i) {
		if (m_personData[m_personIndex].m_recordType[i].indexID >= 0) {
			if (m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length > 0 ) {
				context.strokeStyle = m_personData[m_personIndex].m_recordType[i].style;
				context.lineWidth = m_personData[m_personIndex].m_recordType[i].lineWidth;
				if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.CURVE && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {
					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {
						for ( var tempCnt = 1; tempCnt < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex].length; ++tempCnt) {
							pDrawLine(
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].x,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].y,
									context);
						}
					}
				}

				else if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.RECT && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {
					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex]
								.drawRect();
					}
				}
				
				else if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.CIRCLE && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {
					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex]
								.drawEll();
					}
				}

				else if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.LINE && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {
					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex].draw();
						//alert("index id" + m_personData[m_personIndex].m_recordType[i].indexID);
					}
				}

				else if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.QUAD && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {

					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {

						for ( var tempCnt = 0; tempCnt < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex].length; ++tempCnt) {
							m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt]
									.setContext(context);
							m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt]
									.drawC();
							if (tempCnt != 0) {
								pDrawLine(
										m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
										m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
										m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].x,
										m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].y,
										context);
							}
						}
					}
				}

				else if (m_personData[m_personIndex].m_recordType[i].shapeID == enumType.IRREGULAR && m_recordType[m_personData[m_personIndex].m_recordType[i].indexID]) {
					for ( var tempIndex = 0; tempIndex < m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID].length; ++tempIndex) {
						var length = m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex].length;
						for ( var tempCnt = 1; tempCnt < length; ++tempCnt) {
							pDrawLine(
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].x,
									m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][tempCnt].y,
									context);
						}
						pDrawLine(
								m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][length - 1].x,
								m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][length - 1].y,
								m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][0].x,
								m_personData[m_personIndex].m_recordData[m_personData[m_personIndex].m_recordType[i].indexID][tempIndex][0].y,
								context);
					}
				}

			}
		}

	}
}