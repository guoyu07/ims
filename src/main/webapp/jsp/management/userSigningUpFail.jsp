
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" id="viewport" content="width=360,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<link href="<%=basePath %>css/bootstrap/sign_style.css" rel="stylesheet" type="text/css"/>

<title></title>

</head>
<body>
<div id="result">
	<div id="fail">
		<p class="title">提交失败！</p>
		<p>提交失败：提交失败，服务器去做作业了~等会儿再找他玩吧</p>
	</div>
</div>

</body>
</html>