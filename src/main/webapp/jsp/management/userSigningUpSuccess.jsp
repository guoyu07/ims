
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
	<div id="success">
		<p class="title">提交成功！</p>
		<p>提交成功，正在审核您的报名信息，审核通过后将会以短信形式通知您。</p>
	</div>
</div>

</body>
</html>