<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>匹配结果</title>
  	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <script language="javascript" src="js/jquery-1.9.1.min.js"></script> 
    <script language="javascript" src="js/bootstrap.min.js"></script>
    <link href="css/bootstrap.min.css" type="text/css"  rel="stylesheet"/>   
  	<link href="css/queryInfo.css" type="text/css" rel="stylesheet"/>
  	
    <script language="javascript">
      var executor = '<%=request.getParameter("executor")%>';
      var taskId = '<%=request.getParameter("taskId")%>';
      var queryPath = '<%=request.getParameter("queryPath")%>';
      //var imagesStr; /* = '<s:property escapeJavaScript="false" escape="false" value="imagesStr" />'; */
      var images;
      var corrNumber = 0;
      var corrmatchNumber = 0;
      $(document).ready(function(){
      	$("#username").html("欢迎，"+executor+"  <a href='logOut.action'>退出</a>");
      	var localUrl = document.location.href;
        var argsArr = localUrl.split("&");
        var url = argsArr[0] + "&" + argsArr[1];
        url = url.replace("queryInfo" , "taskInfo");
        $("#taskInfo").attr("href", url);
      $.ajax({ 
        type: "post", 
        url: "fetchQueryInfo.action",
        data:"executor="+executor+"&taskId="+taskId+"&queryPath="+queryPath,
        dataType: "json",
        success: function (data) { 
       		images = data.images;
       		showMatchedImages();
        }, 
        error: function (XMLHttpRequest, textStatus, errorThrown) {
       	     alert(errorThrown);
        } 
      }); 
	  });
      
      function showMatchedImages(){      
      $('#originalPanel').portamento();
    	  var i = 0;
    	  var imagehtml = "";
    	  var imagehtml2 = "";
    	  for(; i < images.length; i++){
    		  if(images[i].match){
    		  	  if(images[i].correspond){
    		  		  corrNumber++;
    		  		  corrmatchNumber++;
    		  	  	  imagehtml = imagehtml + "<div class='imageDiv'>" 
    		  	  	  		+ "<div class='imgSeleted match' onclick='javascript:chooseImage(this);'"
    		  	  	  		+ "><a><img class='img img-polaroid' name='img' src='" 
    				  		+ images[i].path + "' onload='javascript:resizeImg(this, 150, 150);' /></a></div>" 
    				  		+ "<div class='rank'>排名" + (i+1) + "<div style='float:right'>" 
    				  		+ Math.round((images[i].matchRate)*10000)/100 + "%</div></div></div>";
    	  	  	  }
    		  	  else{
    			      imagehtml = imagehtml + "<div class='imageDiv'>" 
		  	  	  			+ "<div class='imgUnseleted match' onclick='javascript:chooseImage(this);'"
		  	  	  			+ "><a><img class='img img-polaroid' name='img' src='" 
				  			+ images[i].path + "' onload='javascript:resizeImg(this, 150, 150);' /></a></div>" 
				  			+ "<div class='rank'>排名" + (i+1) + "<div style='float:right'>" 
				  			+ Math.round((images[i].matchRate)*10000)/100 + "%</div></div></div>";
    		  	  }
    	      }
    		  else{
    			  corrNumber++;
    			  imagehtml2 = imagehtml2 + "<div class='imageDiv'>" 
	  	  				+ "<div class='imgSeleted miss' onclick='javascript:chooseImage(this);'"
	  	  				+ "><a><img class='img img-polaroid' name='img' src='" 
			  			+ images[i].path + "' onload='javascript:resizeImg(this, 150, 150);' /></a></div>" 
			  			+ "<div>"  + "<div style='float:right'>"
			  			+ Math.round((images[i].matchRate)*10000)/100 + "%</div></div></div>";
    		  }
    	  }
    	  if(imagehtml == "") imagehtml = "<div class='noImage'>" + "无" + "</div>";
    	  if(imagehtml2 == "") imagehtml2 = "<div class='noImage'>" + "无" + "</div>";
    	  document.getElementById("imagesDiv").innerHTML = imagehtml;
    	  document.getElementById("imagesDiv2").innerHTML = imagehtml2;
    	  document.getElementById("correspondingNumber").innerHTML = corrNumber;
    	  document.getElementById("matchNumber").innerHTML = corrmatchNumber;
    	  //resizeImg();
      }
      function resizeImg(ImgD, kw, kh){ 
    	  var image = new Image();
    	  image.src = ImgD.src;
    	  if(image.height < image.width){
    	      if(image.width > kw){
    	          ImgD.width = kw;
    	          ImgD.height = (image.height * kw) / image.width;
    	      }
    	      else{
    	          ImgD.width = image.width; 
    	          ImgD.height = image.height;
    	      }
    	  }
    	  else{
    	      if(image.height > kh){
    	          ImgD.height = kh;
    	          ImgD.width = (image.width * kh) / image.height;
    	      }
    	      else{
    	          ImgD.width = image.width; 
    	          ImgD.height = image.height;
    	      }
    	  }
      }
      function chooseImage(element){
    	  if(element.className == "imgUnseleted match"){
    		  element.className = "imgSeleted match";
    		  document.getElementById("correspondingNumber").innerHTML = ++corrNumber;
    		  document.getElementById("matchNumber").innerHTML = ++corrmatchNumber;
    	  }
    	  else if(element.className == "imgUnseleted miss"){
    		  element.className = "imgSeleted miss";
    		  document.getElementById("correspondingNumber").innerHTML = ++corrNumber;
    	  }
    	  else if(element.className == "imgSeleted match"){
    	  	  element.className = "imgUnseleted match";
		      document.getElementById("correspondingNumber").innerHTML = --corrNumber;
		      document.getElementById("matchNumber").innerHTML = --corrmatchNumber;
    	  }
    	  else{
    		  element.className = "imgUnseleted miss";
    		  document.getElementById("correspondingNumber").innerHTML = --corrNumber;
    	  }
      }
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
								<li>
									<a href="#" id="taskInfo">任务信息</a>
								</li>
								<li class="active">
									<a href="#">匹配结果</a>
								</li>
								<div id="username"></div>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
  	<div class="body-div container">
	<div class="row-fluid" id="userInfoDiv">
		<div class="span6" id="matchedTasks">
			<label>匹配结果</label> 
		</div>
	</div>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span4 div-border" id="originalPanel">
		<label class="title">原图</label> 
		<div class="imageDiv">
    		<a href="<%=request.getParameter("queryPath")%>" target=_blank>
    	    	<img class="img img-polaroid" src="<%=request.getParameter("queryPath")%>"  height=height*200/width width=200 />
    		</a>
  		</div>
  		<div class="matchInfo">
  			测试图集中有&nbsp;&nbsp;
  			<span id="correspondingNumber">5</span>
  			&nbsp;&nbsp;张图片与原图对应，匹配结果包含了其中的&nbsp;&nbsp;
  			<span id="matchNumber">2</span>&nbsp;&nbsp;张图片。
  		</div>
  		<div class="renew">
  			<button id="renewButton" class="btn btn-default btn-submit">更新</button>
  		</div>
    </div>
    <div class="span8 div-border" id="choiceDiv">
		<div id="matchPanel">
  			<label class="title">匹配结果</label>
  		<div id="imagesDiv"></div>
  		</div>
  		<div id="missPanel">
			<label class="title">未匹配到的对应图片</label>
  		<div id="imagesDiv2"></div>
  	</div>
    </div>

  </div>
</div>
</div>
  	<script src="js/portamento-min.js"></script>
  </body>
</html>