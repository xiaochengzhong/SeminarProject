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
    		  	  	  		+ "><a><img name='img' src='" 
    				  		+ images[i].path + "' onload='javascript:resizeImg(this, 150, 150);' /></a></div>" 
    				  		+ "<div class='rank'>排名" + (i+1) + "<div style='float:right'>" 
    				  		+ Math.round((images[i].matchRate)*10000)/100 + "%</div></div></div>";
    	  	  	  }
    		  	  else{
    			      imagehtml = imagehtml + "<div class='imageDiv'>" 
		  	  	  			+ "<div class='imgUnseleted match' onclick='javascript:chooseImage(this);'"
		  	  	  			+ "><a><img name='img' src='" 
				  			+ images[i].path + "' onload='javascript:resizeImg(this, 150, 150);' /></a></div>" 
				  			+ "<div class='rank'>排名" + (i+1) + "<div style='float:right'>" 
				  			+ Math.round((images[i].matchRate)*10000)/100 + "%</div></div></div>";
    		  	  }
    	      }
    		  else{
    			  corrNumber++;
    			  imagehtml2 = imagehtml2 + "<div class='imageDiv'>" 
	  	  				+ "<div class='imgSeleted miss' onclick='javascript:chooseImage(this);'"
	  	  				+ "><a><img name='img' src='" 
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
  	<div id="originalPanel">&nbsp;&nbsp;原图:
  		<br/>
  		<div class="imageDiv">
    		<a href="<%=request.getParameter("queryPath")%>" target=_blank>
    	    	<img class="img" src="<%=request.getParameter("queryPath")%>" style='border:solid 3px #FFFFFF;' height=height*200/width width=200 />
    		</a>
  		</div>
  		<div class="viewBigImage">
  			<a href="<%=request.getParameter("queryPath")%>" target=_blank>查看大图</a>
  		</div>
  		<br/>
  		<div class="matchInfo">
  			测试图集中有<span id="correspondingNumber">5</span>张图片与原图对应，匹配结果包含了其中的<span id="matchNumber">2</span>张图片。
  		</div>
  		<br/>
  		<br/>
  		<div class="renew">
  			<button id="renewButton">更新</button>
  		</div>
  		<br/>
  		<br/>
  	</div>
  	<div id="matchPanel">&nbsp;&nbsp;匹配结果:
  		<br/>
  		<div id="imagesDiv"></div>
  	</div>
  	<br/>
  	<div id="missPanel">&nbsp;&nbsp;未匹配到的对应图片:
  		<br/>
  		<div id="imagesDiv2"></div>
  	</div>
  	<script src="js/portamento-min.js"></script>
  </body>
</html>