//��������
newArray = new Array();
myVertex = new Array();
startPoint = {x:0 , y:0};
rectSize = {w:0 , h:0};

//���ɵ����������
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

//��ǰ����X������
function arrayXDeal(arrayData , grap)
{
	for(var XDeali = 0 ; XDeali < grap ; ++XDeali)
		arrayData.splice(0 , 0 , 0);
	if(arrayData.length > rectSize.w)
		rectSize.w = arrayData.length;
}

//�������X������
function arrayExpDeal(arrayData , grap)
{
	for(var ExpDeali = 0 ; ExpDeali < grap ; ++ExpDeali)
		arrayData.push(0);
	if(arrayData.length > rectSize.w)
	rectSize.w = arrayData.length;
}

//��ǵ�ǰ��
function markVertex(arrayData , mPos)
{
	var vergrap = mPos -  arrayData.length;
	if(vergrap > 0)
	{
		arrayExpDeal(arrayData , vergrap);
	}
	arrayData[mPos] = 1;
}

//������ת���ɵ���
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
			//��Ҫ�޸�ԭ�е�myvertex��Ԫ�ص�X����
			var grap = startPoint.x - newArray[2*count];
			for(var index = 0 ; index < myVertex.length; ++index)
			{
				arrayXDeal(myVertex[index] , grap);
			}

			//�޸�startPoint��x����
			//rectSize.w = rectSize.w + (startPoint.x - newArray[2*count]);
			startPoint.x = newArray[2*count];
			
		}
 
		if(newArray[2*count+1] < startPoint.y)
		{
			//��Ҫ�޸�ԭ�е�myvertex��Ԫ�ص�Y����
			var grap = startPoint.y - newArray[2*count+1];
			for(var index = 0 ; index < grap; ++index)
			{
				myVertex.splice(0 , 0 , new Array(0,0));
			}

			//�޸�startPoint��Y����
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
					myVertex.push(new Array(0,0));  //��ĩβ��չY����
				}
				rectSize.h = rectSize.h + grap;
			}
		}
		
		//���µ�ǰ������
		var xPos = newArray[2*count] - startPoint.x;
		var yPos = newArray[2*count+1] - startPoint.y;
		markVertex(myVertex[yPos] , xPos);
	}
}
