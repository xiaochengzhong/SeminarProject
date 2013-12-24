//定义数据
newArray = new Array();
myVertex = new Array();
startPoint = {x:0 , y:0};
rectSize = {w:0 , h:0};

//生成点矩阵并输出结果
function getmatrix()
{
	newArray = getThedata();
	$("#tempValue").val(newArray.toString());
	//alert(tempInput);// = newArray.toString();
	//$("#tempValue").setValue(newArray.toString());
	arrayToVertex();
	stringData = "startPoint X: " + startPoint.x + " Y: " + startPoint.y + " Width: " + rectSize.w + "  Height: " + rectSize.h + "<br/>";
	for(var xxi = 0 ; xxi < myVertex.length ; ++xxi)
	{
		for(j = 0 ; j < myVertex[xxi].length ; ++j)
			stringData += myVertex[xxi][j];
		stringData += "<br/>";
	}
	document.write(stringData);
}

//向前扩充X轴数组
function arrayXDeal(arrayData , grap)
{
	for(var XDeali = 0 ; XDeali < grap ; ++XDeali)
		arrayData.splice(0 , 0 , 0);
	if(arrayData.length > rectSize.w)
		rectSize.w = arrayData.length;
}

//向后扩充X轴数组
function arrayExpDeal(arrayData , grap)
{
	for(var ExpDeali = 0 ; ExpDeali < grap ; ++ExpDeali)
		arrayData.push(0);
	if(arrayData.length > rectSize.w)
	rectSize.w = arrayData.length;
}

//标记当前点
function markVertex(arrayData , mPos)
{
	var vergrap = mPos -  arrayData.length;
	if(vergrap > 0)
	{
		arrayExpDeal(arrayData , vergrap);
	}
	arrayData[mPos] = 1;
}

//将坐标转换成点阵
function arrayToVertex()
{
	startPoint.x = newArray[0];
	startPoint.y = newArray[1];
	rectSize.w = 1;
	rectSize.h = 1;
	myVertex.push( new Array(1));
	record = 0;
	for (var count =0; count <newArray.length/2;++count)
	{
		if(newArray[2*count] < startPoint.x)
		{
			//需要修改原有的myvertex中元素的X坐标
			var grap = startPoint.x - newArray[2*count];
			for(var index = 0 ; index < myVertex.length; ++index)
			{
				arrayXDeal(myVertex[index] , grap);
			}

			//修改startPoint的x坐标
			//rectSize.w = rectSize.w + (startPoint.x - newArray[2*count]);
			startPoint.x = newArray[2*count];
			
		}
 
		if(newArray[2*count+1] < startPoint.y)
		{
			//需要修改原有的myvertex中元素的Y坐标
			var grap = startPoint.y - newArray[2*count+1];
			for(var index = 0 ; index < grap; ++index)
			{
				myVertex.splice(0 , 0 , new Array(0,0));
			}

			//修改startPoint的Y坐标
			rectSize.h = rectSize.h + (startPoint.y - newArray[2*count+1]);
		    startPoint.y = newArray[2*count+1];
		}
		else
		{
			var grap = newArray[2*count+1] - startPoint.y;
			if(grap >= rectSize.h)
			{
				grap = grap - rectSize.h + 1;
				for(index = 0 ; index < grap ; ++index)
				{
					myVertex.push(new Array(0,0));  //在末尾扩展Y坐标
				}
				rectSize.h = rectSize.h + grap;
			}
		}
		
		//更新当前点属性
		var xPos = newArray[2*count] - startPoint.x;
		var yPos = newArray[2*count+1] - startPoint.y;
		markVertex(myVertex[yPos] , xPos);
	}
}
