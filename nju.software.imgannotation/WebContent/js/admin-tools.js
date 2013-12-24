$(document).ready(function(){
	$("#taskListContent").jscroll({W:"12px",Btn:{btn:false}});
	$("#FVContent").jscroll({W:"12px",Btn:{btn:false}});
	document.ondragstart=function(){return false;};
	document.onselectstart=function(){return false;};
	color_changeable=false;
    var img = new Image();
	img.src="./images/color.bmp";
	$(document).bind('mouseup',function(event){
		color_changeable=false;
	});
	
	$(img).bind("load",function(){
		canvas_color=document.getElementById("canvas_color");
		context3=canvas_color.getContext('2d');
		context3.drawImage(this, 0, 0,this.width,this.height);
		canvas_color_data = context3.getImageData(0, 0, canvas_color.width, canvas_color.height);
		$(canvas_color).bind("mousedown",function(event){

			var idx = ((event.offsetX-1) + (event.offsetY-1) * canvas_color_data.width) * 4;
            var r = canvas_color_data.data[idx + 0];
            var g = canvas_color_data.data[idx + 1];
            var b = canvas_color_data.data[idx + 2];
            $("#color_span").css("background-color","rgb("+r+","+g+","+b+")");
			m_colorSet = "rgb("+r+","+g+","+b+")";
            //change_attr(-1,-1,"rgb("+r+","+g+","+b+")");
            color_changeable=true;
		});
		$(canvas_color).bind("mousemove",function(event){
			if(color_changeable==false)
				return;
			var x=event.offsetX-1;
			if(x>=canvas_color_data.width||x<0)
				return;
			var y=event.offsetY-1;
			if(y>=canvas_color_data.height||y<0)
				return;
			var idx = (x + y * canvas_color_data.width) * 4;
            var r = canvas_color_data.data[idx + 0];
            var g = canvas_color_data.data[idx + 1];
            var b = canvas_color_data.data[idx + 2];
            $("#color_span").css("background-color","rgb("+r+","+g+","+b+")");
			m_colorSet = "rgb("+r+","+g+","+b+")";
            //change_attr(-1,-1,"rgb("+r+","+g+","+b+")");
		});
	});
	
	$("#size_bar").bind("mousedown",function(event){
		var thumb=$("#size_thumb");
		var main_w=$(this).width();
		var mainLeft=$(this).offset().left;
		thumb.css("left",event.clientX-mainLeft-thumb.width()/2+"px");
		$("#size_span").html(Math.ceil($(thumb).position().left/main_w*5)+1);
		//change_attr(-1,$("#size_span").html(),-1);
		m_lineWidth = $("#size_span").html();
		$(document).bind("mousemove",size_bar_move);
		$(document).bind("mouseup",function unbind(event){
			$(document).unbind("mousemove",size_bar_move);
			$(document).unbind("mouseup",unbind);
		});
	});
	
});