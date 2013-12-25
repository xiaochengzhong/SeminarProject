
$(document).ready(function(){
	$("#drawPanel").css({"top": $("#picShow")[0].offsetTop , "left": $("#picShow")[0].offsetLeft});
	$("#draftPanel").css({"top": $("#picShow")[0].offsetTop , "left": $("#picShow")[0].offsetLeft});
    imgSWidth = $("#picShow").css("width").replace("px" , "");
    imgStHeight = $("#picShow").css("height").replace("px" , "");

    //$("#tasklistShow").jscroll({W:"12px",Btn:{btn:false}});

	document.ondragstart=function(){return false;};// not allow the drag move
	document.onselectstart=function(){return false;};//

	canvas_size={x:$("#canvas_drawPanel").width(),y:$("#canvas_drawPanel").height()};  //draw panel size
	origin={x:0,y:0};
	end={x:0,y:0};
	enumType = {CURVE:1 , LINE:2 ,  CIRCLE:3 , RECT:4 , QUAD:5 , IRREGULAR: 6};
    drawable=false;

	quadPSize = 3;
	quad_PrePoint = {x:0 , y:0};
	
	//建立映射表
	m_mapindex = new Array();

	
	//use the object to define the quad point
	pCircle = function( x , y , rad , contextObj){
    	this.x=x;
    	this.y=y;
    	this.r=rad;
		this.ctx = contextObj;
		this.hasrepeat = false;
		this.id= null;
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
		//set the id
		this.setID = function(idNumber)
		{
			this.id = idNumber;
		};
    };
	m_MarkPCpoint = false;                       //mark the circle point

	//define the rectangle object
	pRectangle = function(startX , startY , endX , endY , contextObj)
	{
		//define the data
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.context = contextObj;
		this.id = null;
		
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
		//set the id
		this.setID = function(idNumber)
		{
			this.id = idNumber;
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
		this.id = null;
		
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
		//set the id
		this.setID = function(idNumber)
		{
			this.id = idNumber;
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
		this.id = null;
		
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
		//set the id
		this.setID = function(idNumber)
		{
			this.id = idNumber;
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
	
	//set the default data for the draw type
	type=enumType.CURVE;
	
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

	m_recordData = new Array();                     //use to record the use information
	m_recordType = new Array();
	m_recordIndex = 0;
	
	canvas=document.getElementById("canvas_drawPanel");         //draw on the image(the finally data)
	canvas2=document.getElementById("canvas_draftPanel");       //draft panel


	context=canvas.getContext("2d");
	context.strokeStyle="#00aeef";                       //set the pencil style
	fill_canvas("#ffffff");                              //clean the canvas
	context.lineWidth = 1;                               //define the line width
	context2=canvas2.getContext("2d");
	context2.strokeStyle="#00aeef";
	context2.lineWidth = 1;

	$("#canvas_draftPanel").bind('mousedown',function(event){
		drawable=true;
		if(type == enumType.QUAD)
			return;
		origin.x=event.offsetX;
		origin.y=event.offsetY;
		if(type == enumType.CURVE)                      //storage the point
		{
			m_recordData[m_recordIndex].push(new Array());
			m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(new pCurvedLine(origin.x , origin.y));
			addMapkey(m_recordIndex);
		}
		
		else if(type == enumType.IRREGULAR)
		{
			m_recordData[m_recordIndex].push(new Array());
			m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(new pCurvedLine(origin.x , origin.y));
			addMapkey(m_recordIndex);
		}
	});
	$("#canvas_draftPanel").bind('mouseup',function(event){
		if(type == enumType.QUAD)           //only do when the model is QUAD
		{
			if(m_MarkPCpoint)     //no drawing
			{
				if(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0].getRepeat())
				{
					origin.x = m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length -1].x;
					origin.y = m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length -1].y;
					quadDrawPoint(context);
					m_MarkPCpoint = false;
				}
			}
			else                  //add new point as normal
			{
				origin.x=event.offsetX;
				origin.y=event.offsetY;
				quadDrawPoint(context);
				quad_PrePoint.x=origin.x;
				quad_PrePoint.y=origin.y;
			}
		}
		else if(type == enumType.IRREGULAR)
		{
			//end the drawing
			pclosePath(m_recordData[m_recordIndex].length-1);
			drawable = false;
			//clean the context2
			context2.clearRect(0,0,canvas_size.x,canvas_size.y);
		}
		canvas_backup=context.getImageData(0, 0, canvas.width, canvas.height);
	});
	
	//bind the canvas movemove event
	$("#canvas_draftPanel").bind("mousemove" , function(event){
		if(drawable)
		{
			if(type == enumType.CURVE)
			{
				end.x=event.offsetX;
				end.y=event.offsetY;
				m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(new pCurvedLine(end.x , end.y));
				draw(context);
				origin.x=end.x;
				origin.y=end.y;
			}
			else if (type == enumType.IRREGULAR)
			{
				end.x=event.offsetX;
				end.y=event.offsetY;
				m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(new pCurvedLine(end.x , end.y));
				draw(context2);
				origin.x=end.x;
				origin.y=end.y;
			}
		}
		
	});
	
	$(document).bind('mouseup',function(event){
		if((type==enumType.LINE||type==enumType.CIRCLE||type==enumType.RECT)&&drawable==true){
			drawable=false;
			context2.clearRect(0,0,canvas_size.x,canvas_size.y);
			end.x=event.offsetX;
	        end.y=event.offsetY;
			draw(context , true);
		}
		else if (type == enumType.QUAD)
		{
		}
		else
			drawable=false;
	});
    $(document).bind("mousemove",function(event){
        if(drawable==false)
			return;
        if(type==enumType.CURVE){                 //don't need to remember the point data
            //end.x=event.clientX-canvas_offset.x;
            //end.y=event.clientY-canvas_offset.y;
	    	//draw(context);
	    	//origin.x=end.x;
	    	//origin.y=end.y;
			return;
        }
		else if(type==enumType.LINE||type==enumType.CIRCLE||type==enumType.RECT){
				end.x=event.offsetX;
				end.y=event.offsetY;
            	context2.clearRect(0,0,canvas_size.x,canvas_size.y);
	    	draw(context2);
        }
		else if(type == enumType.QUAD)
		{
			end.x=event.offsetX;
			end.y=event.offsetY;
			context2.clearRect(0,0,canvas_size.x,canvas_size.y);
			draw(context2);
			for(var tempIndex = 0 ; tempIndex < m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length ; ++tempIndex)
			{
				if(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][tempIndex].isInPCircle(end.x , end.y))
				{
					//in the point area
					m_MarkPCpoint = true;
					return;
				}
			}
			m_MarkPCpoint = false;
			
			
		}
    });
    //init the current data
	initData();
    getCurrentPicData();       //update current data
});


function draw(context){
	var judgement=arguments[1]?arguments[1]:false;           //set the second default argument
	if(type==enumType.CURVE||type==enumType.LINE){               //to judgement the kind
		pDrawLine(origin.x,origin.y,end.x,end.y,context);
		if(judgement)
		{
			m_recordData[m_recordIndex].push(new pLine(origin.x,origin.y,end.x,end.y,context));
			addMapkey(m_recordIndex);
		}
    }
	else if(type==enumType.CIRCLE){
	   	//define the object
		var tempEll = new pEllipse(origin.x , origin.y , end.x , end.y , context);
		tempEll.drawEll();
		// weather need record the data
		if(judgement)
		{
			m_recordData[m_recordIndex].push(tempEll);
			addMapkey(m_recordIndex);
		}
    }
	else if(type==enumType.RECT){
		//draw the rectangle
		var tempRect = new pRectangle(origin.x , origin.y , end.x , end.y , context);
		tempRect.drawRect();
		if(judgement)
		{
			m_recordData[m_recordIndex].push(tempRect);
			addMapkey(m_recordIndex);
		}
    }
	else if(type == enumType.QUAD)
	{
		context.beginPath();
	    context.moveTo(origin.x,origin.y);
	    context.lineTo(end.x,end.y);
		context.stroke();
	}
	else if(type == enumType.IRREGULAR)
	{
		//draw the irregular shape
		context.beginPath();
	    context.moveTo(origin.x,origin.y);
	    context.lineTo(end.x,end.y);
		context.stroke();
	}
}

function cleancanvase()
{
	context2.clearRect(0,0,canvas_size.x,canvas_size.y);
}

function quadDrawPoint(context)
{
	//recordData[enumType.QUAD].push(origin.x , origin.y);
	var newCir=new pCircle(origin.x,origin.y,quadPSize , context);
	newCir.drawC();
	m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(newCir);
	//if(recordData[enumType.QUAD].length == 0)
	//	return;
	if(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length == 0 || m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length == 1)
	{
		return;
	}
	context.beginPath();
	var tempData = m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].length-2];

	context.moveTo(tempData.x,tempData.y);
	//context.moveTo(quad_PrePoint.x,quad_PrePoint.y);
	if(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0].getRepeat())
	{
		addMapkey(m_recordIndex);
		m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].pop();
		m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0]);
		context.lineTo(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0].x,m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0].y);
		m_recordData[m_recordIndex].push(new Array());
		drawable = false;		
	}
	else
		context.lineTo(end.x,end.y);
	context.stroke();
//	if(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0].getRepeat())
//	{
//		addMapkey();
//		m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].pop();
//		m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1].push(m_recordData[m_recordIndex][m_recordData[m_recordIndex].length-1][0]);
//		m_recordData[m_recordIndex].push(new Array());
//		drawable = false;
//	}
}



function change_attr(tp,sz,clr){      //change the attribute of the canvas
	if(tp!=-1)
		type=tp;                     //pencil
	if(clr!=-1){
		context.strokeStyle=clr;
		context2.strokeStyle=clr;
	}
	if(sz!=-1){
		context.lineWidth = sz;
		context2.lineWidth = sz;
	}
}

function clear_canvas(){
	context.clearRect(0,0,canvas_size.x,canvas_size.y);
}

function fill_canvas(col,orix,oriy,w,h){
	context.fillStyle=col;
	context.fillRect(orix,oriy,w,h);
}

function open_img(url){
	var img = new Image();
	img.src=url;
	$(img).bind("load",function(){
		fill_canvas('#ffffff',0,0,canvas_size.x,canvas_size.y);
		context.drawImage(this, 0, 0,this.width,this.height);
		canvas_backup=context.getImageData(0, 0, canvas.width, canvas.height);
	});
}

function changeAttr(btnID)
{
	//hide the last time information
	$("#act_"+m_recordIndex).css('display' , 'none');
	m_recordIndex = btnID;
	$("#act_"+m_recordIndex).css('display' , 'inline');
	context.strokeStyle=$("#color_span_"+btnID).css("background-color");
	context2.strokeStyle=$("#color_span_"+btnID).css("background-color");
	context.lineWidth = $("#color_span_"+btnID).attr("value");
	context2.lineWidth = $("#color_span_"+btnID).attr("value");
	type = $("#color_span_"+btnID).attr("class");
	//btnID is the task ID
	if(type == enumType.QUAD)
		m_recordData[m_recordIndex].push(new Array());
	m_recordType[m_recordIndex].setShapeID(type);
	m_recordType[m_recordIndex].setIndexID(m_recordIndex);
	m_recordType[m_recordIndex].setStyle($("#color_span_"+btnID).css("background-color"));
	m_recordType[m_recordIndex].setLineWidth($("#color_span_"+btnID).attr("value"));
}

function initData()
{
	//return recordData[enumType.CURVE];
	initRecordData($("#task_list_size").attr("class"));
	$("#task_list_size li:first input").click();
	
}

//init the record data array
function initRecordData(taskLength)
{
	for(var initIndex = 0 ; initIndex < taskLength ; ++initIndex )
	{
		m_recordData.push(new Array());
		m_recordType.push(new pRecordInfo());
		m_mapindex.push(new Array());
	}
}

//draw line
function pDrawLine(startX , startY , endX , endY , contextObj)
{
	contextObj.beginPath();
    contextObj.moveTo(startX,startY);
    contextObj.lineTo(endX,endY);
	contextObj.stroke();
}

//generate the closed interval
function pclosePath(index)
{
	var length = m_recordData[m_recordIndex][index].length;
	for(var tempCnt = 1 ; tempCnt < length ; ++tempCnt)
	{
		pDrawLine(m_recordData[m_recordIndex][index][tempCnt-1].x , m_recordData[m_recordIndex][index][tempCnt-1].y ,
			m_recordData[m_recordIndex][index][tempCnt].x , m_recordData[m_recordIndex][index][tempCnt].y , context);
	}
	pDrawLine(m_recordData[m_recordIndex][index][length-1].x , m_recordData[m_recordIndex][index][length-1].y , 
			m_recordData[m_recordIndex][index][0].x , m_recordData[m_recordIndex][index][0].y , context);
	return;  //just do the cut behind
}

//show the data
function showTheData()
{
	commitTasksData();
	return;
	
}


//use to generate the xml data to the server
function genPostData(taskindex)
{
	var time = getNowFormatDate();
	var formatData="";
	//rectangle and the the oval has the same data structure, put together
	if(m_recordType[taskindex].shapeID == enumType.RECT || m_recordType[taskindex].shapeID == enumType.CIRCLE)
	{
		for(var tempIndex=0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData + "<record><author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><Extend>" + 
				"<start X=\"" + m_recordData[taskindex][tempIndex].startX + "\" Y=\"" + m_recordData[taskindex][tempIndex].startY +"\"></start><end X=\"" + m_recordData[taskindex][tempIndex].endX+
				"\" Y=\"" + m_recordData[taskindex][tempIndex].endY +"\"></end></Extend></record>";
		}
		
	}
	else if(m_recordType[taskindex].shapeID == enumType.LINE)
	{
		for(var tempIndex=0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData + "<record><author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><points>" + "<point X=\"" + m_recordData[taskindex][tempIndex].startX + "\" Y=\"" +
			m_recordData[taskindex][tempIndex].startY +"\"></point>" + 
				"<point X=\"" + m_recordData[taskindex][tempIndex].endX+ "\" Y=\"" + m_recordData[taskindex][tempIndex].endY +"\"></point>" + "</points></record>";
		}
	}
	else
	{
		
		for(var tempIndex = 0 ; tempIndex < m_recordData[taskindex].length ; ++tempIndex)
		{
			formatData = formatData + "<record><author>" + $("#user_name").attr("value") +"</author><time>"+ time + "</time><points>";
			for(var tempCnt = 0  ; tempCnt < m_recordData[taskindex][tempIndex].length ; ++tempCnt)
			{
				formatData = formatData + "<point X=\"" + m_recordData[taskindex][tempIndex][tempCnt].x +"\" Y=\""+
					m_recordData[taskindex][tempIndex][tempCnt].y +"\"></point>";
			}
			formatData = formatData + "</points></record>";
		}
	}
	return formatData;
}

//yyyy-mm-dd hh:mm:ss
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
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
	
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + hours + seperator2 + minutes + seperator2 + second;
    return currentdate;
}

//use to commit the tasks to the serer	
function commitTasksData()
{
	var taskDataInfo = new Array();
	var taskDataID = new Array();
	for(var tempIndex = 0 ; tempIndex < m_recordType.length ; ++tempIndex)
	{
		taskDataID.push(m_recordType[tempIndex].indexID);        //当前任务中该任务的编号
		taskDataInfo.push(genPostData(tempIndex));
	}
	taskDataID = toJSON(taskDataID);
	taskDataInfo = toJSON(taskDataInfo);
//	alert(taskDataID);
	var taskSetID = $("#current_taskset_ID").attr("value");
	var picID = $("#current_pic_ID").attr("value");
	$.ajax({
		type : "POST",
		url : "taskdataControl",
		data : {'tasksetID' : taskSetID , 'picID' : picID , 'taskDataID' : taskDataID , 'taskDataInfo' : taskDataInfo},
		success : function(msg) {
			alert(msg);
		}
	});
}

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

// just for the test
function redraw() {
	context.clearRect(0, 0, canvas_size.x, canvas_size.y);

	for ( var i = 0; i < m_recordData.length; ++i) {
		if (m_recordType[i].indexID >= 0) {
			if (m_recordData[m_recordType[i].indexID].length > 0) {
				context.strokeStyle = m_recordType[i].style;
				context.lineWidth = m_recordType[i].lineWidth;
				if (m_recordType[i].shapeID == enumType.CURVE) {
					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {
						for ( var tempCnt = 1; tempCnt < m_recordData[m_recordType[i].indexID][tempIndex].length; ++tempCnt) {
							pDrawLine(
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].x,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].y,
									context);
						}
					}
				}

				else if (m_recordType[i].shapeID == enumType.RECT) {
					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {
						m_recordData[m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_recordData[m_recordType[i].indexID][tempIndex]
								.drawRect();
					}
				}
				
				else if (m_recordType[i].shapeID == enumType.CIRCLE) {
					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {
						m_recordData[m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_recordData[m_recordType[i].indexID][tempIndex]
								.drawEll();
					}
				}

				else if (m_recordType[i].shapeID == enumType.LINE) {
					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {
						m_recordData[m_recordType[i].indexID][tempIndex]
								.setContext(context);
						m_recordData[m_recordType[i].indexID][tempIndex].draw();
						//alert("index id" + m_recordType[i].indexID);
					}
				}

				else if (m_recordType[i].shapeID == enumType.QUAD) {

					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {

						for ( var tempCnt = 0; tempCnt < m_recordData[m_recordType[i].indexID][tempIndex].length; ++tempCnt) {
							m_recordData[m_recordType[i].indexID][tempIndex][tempCnt]
									.setContext(context);
							m_recordData[m_recordType[i].indexID][tempIndex][tempCnt]
									.drawC();
							if (tempCnt != 0) {
								pDrawLine(
										m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
										m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
										m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].x,
										m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].y,
										context);
							}
						}
					}
				}

				else if (m_recordType[i].shapeID == enumType.IRREGULAR) {
					for ( var tempIndex = 0; tempIndex < m_recordData[m_recordType[i].indexID].length; ++tempIndex) {
						var length = m_recordData[m_recordType[i].indexID][tempIndex].length;
						for ( var tempCnt = 1; tempCnt < length; ++tempCnt) {
							pDrawLine(
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].x,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt - 1].y,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].x,
									m_recordData[m_recordType[i].indexID][tempIndex][tempCnt].y,
									context);
						}
						pDrawLine(
								m_recordData[m_recordType[i].indexID][tempIndex][length - 1].x,
								m_recordData[m_recordType[i].indexID][tempIndex][length - 1].y,
								m_recordData[m_recordType[i].indexID][tempIndex][0].x,
								m_recordData[m_recordType[i].indexID][tempIndex][0].y,
								context);
					}
				}

			}
		}

	}
	
	//set the default color and line-width
	context.strokeStyle = m_recordType[m_recordIndex].style;
	context.lineWidth = m_recordType[m_recordIndex].lineWidth;

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

//has select the child
function outline(indexID)
{
	context2.clearRect(0, 0, canvas_size.x, canvas_size.y);
	colorstorage = context2.strokeStyle;
	context2.strokeStyle = colorOff(colorstorage);
	arrayIndex = m_mapindex[m_recordIndex][indexID];
	if (m_recordType[m_recordIndex].shapeID == enumType.CURVE) {
		for ( var tempCnt = 1; tempCnt < m_recordData[m_recordIndex][arrayIndex].length; ++tempCnt) {
			pDrawLine(
					m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].x,
					m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].y,
					m_recordData[m_recordIndex][arrayIndex][tempCnt].x,
					m_recordData[m_recordIndex][arrayIndex][tempCnt].y,
					context2);
		}
	}

	else if (m_recordType[m_recordIndex].shapeID == enumType.RECT) {
		m_recordData[m_recordIndex][arrayIndex].setContext(context2);
		m_recordData[m_recordIndex][arrayIndex].drawRect();
	}
	
	else if (m_recordType[m_recordIndex].shapeID == enumType.CIRCLE) {
		m_recordData[m_recordIndex][arrayIndex].setContext(context2);
		m_recordData[m_recordIndex][arrayIndex].drawEll();
	}

	else if (m_recordType[m_recordIndex].shapeID == enumType.LINE) {
			m_recordData[m_recordIndex][arrayIndex].setContext(context2);
			m_recordData[m_recordIndex][arrayIndex].draw();
	}

	else if (m_recordType[m_recordIndex].shapeID == enumType.QUAD) {
		for ( var tempCnt = 0; tempCnt < m_recordData[m_recordIndex][arrayIndex].length; ++tempCnt) {
			m_recordData[m_recordIndex][arrayIndex][tempCnt].setContext(context2);
			m_recordData[m_recordIndex][arrayIndex][tempCnt].drawC();
			if (tempCnt != 0) {
				pDrawLine(
						m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].x,
						m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].y,
						m_recordData[m_recordIndex][arrayIndex][tempCnt].x,
						m_recordData[m_recordIndex][arrayIndex][tempCnt].y,
						context2);
			}
		}
	}

	else if (m_recordType[m_recordIndex].shapeID == enumType.IRREGULAR) {
		var length = m_recordData[m_recordIndex][arrayIndex].length;
		for ( var tempCnt = 1; tempCnt < length; ++tempCnt) {
			pDrawLine(
					m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].x,
					m_recordData[m_recordIndex][arrayIndex][tempCnt - 1].y,
					m_recordData[m_recordIndex][arrayIndex][tempCnt].x,
					m_recordData[m_recordIndex][arrayIndex][tempCnt].y,
					context2);
		}
		pDrawLine(
				m_recordData[m_recordIndex][arrayIndex][length - 1].x,
				m_recordData[m_recordIndex][arrayIndex][length - 1].y,
				m_recordData[m_recordIndex][arrayIndex][0].x,
				m_recordData[m_recordIndex][arrayIndex][0].y,
				context2);
	}
	context2.strokeStyle = colorstorage;
}

//反转颜色
function colorOff(hex) {
	var pattern = "#";
	hex = hex.replace(new RegExp(pattern), "");
    var bigint = parseInt(hex, 16);
    var r = 255-(bigint >> 16) & 255;
    var g = 255 - (bigint >> 8) & 255;
    var b = 255 - bigint & 255;
    return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}

//delete the childIndex
function deleteAction(indexID)
{
	arrayIndex = m_mapindex[m_recordIndex][indexID];
	m_recordData[m_recordIndex].splice(arrayIndex , 1);
	//delete the div
	var divCon = document.getElementById("div_"+indexID);
    if (divCon != null)
    	divCon.parentNode.removeChild(divCon);
    //adjust the key-value
    for(var key in m_mapindex[m_recordIndex]){  
    	  if(m_mapindex[m_recordIndex][key] > arrayIndex)
    	  {
    		  m_mapindex[m_recordIndex][key] = m_mapindex[m_recordIndex][key] - 1;
    	  }
    }
//    for(var key in m_mapindex[m_recordIndex]){
//    	if(m_mapindex[m_recordIndex][key] == arrayIndex)
//  		  m_mapindex[m_recordIndex].splice(key, 1);
//    }
	
	context2.clearRect(0, 0, canvas_size.x, canvas_size.y);
	redraw();
}

function addMapkey(recordIndex)
{
	id = getStringDate() +""+recordIndex+""+ m_recordData[recordIndex].length;
	m_mapindex[recordIndex][id] = m_recordData[recordIndex].length-1;    //create the key-value
	//add the index of the actions
	data = "<div style=\"width:80%;margin-left: 50px; margin-bottom: 10px;\" id=\"div_"+id+"\"><input class=\"button glow button-rounded button-flat\" type=\"button\" id=\""+id+"\" value=\"删除\" style=\"position:relative;padding-left: 10px;padding-right: 10px;\" onclick=\"deleteAction(this.id)\" class=\"button\">";
	data += "<input type=\"radio\" id=\""+id+"\" style=\"margin-top: 0px;margin-right: 20px; margin-left: 10px;\" name=\"childCheck\" class=\"childAction\" onclick=\"outline(this.id)\"><span>画图动作</span></div>";
	$("#act_"+recordIndex).html($("#act_"+recordIndex).html()+data);
    //adjust the key-value
//    for(var key in m_mapindex[recordIndex]){  
//    	alert(key);
//    }
}



