<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%    
String path = request.getContextPath();    
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
pageContext.setAttribute("basePath",basePath);
Cookie[] pCookie = request.getCookies();
String[] pInformation = new String[]{"" , ""};
for(short i=0; i < pCookie.length ; ++i)
{
	if(pCookie[i].getName().contentEquals("pID"))
		pInformation[0] = pCookie[i].getValue();
	if(pCookie[i].getName().contentEquals("pName"))
		pInformation[1] = pCookie[i].getValue();
}
%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录</title>
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="css/bootstrap-responsive.min.css" type="text/css" rel="stylesheet" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style type="text/css">
body {
	margin-top: 100px;
	padding: 40px 0px;
	background: #f6f7f1 url(images/login-bg.png) repeat 0 0;
}

.btn-reset,.btn-submit {
	font-family: Helvetica, Arial;
	font-weight: normal;
	line-height: normal;
	background: #ffffff;
	border: 1px solid #cccccc;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	-webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
	box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
	font-size: 14px;
	text-decoration: none;
}

.btn-submit {
	background-color: #a6bbb6;
	color: #fff;
}

.form-signin {
	max-width: 510px;
	padding: 19px 29px 0px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="container">
		<div style="color: red">
			<s:property value="failInfo" />
		</div>
		<form class="form-horizontal form-signin" action="loginClServlet"
			method="post">
			<div class="control-group">
				<label class="control-label" for="inputEmail">用户名：</label>
				<div class="controls">
					<input type="text" id="email" name="email" placeholder="邮箱前缀" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputPassword">密码 ：</label>
				<div class="controls">
					<input type="password" id="password" name="password"
						placeholder="密码" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button class="btn btn-default btn-reset" type="reset">重置</button>
					<button class="btn btn-default btn-submit" type="submit">登录</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>