
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
<script type="text/javascript">

function verifyForm() {
	if ($("#teachername").val().length < 2) {
		alert("请输入您的姓名！");
		$("#teachername").addClass("error");
		return false;
	} else if (!/^1[0-9]{10}$/.test($("#phonenumber").val())) {
		alert("您输入的手机号不符合要求，请重新输入！");
		$("#phonenumber").addClass("error");
		return false;
	} else if (!/^[0-9]{15}$/.test($("#IDnumber").val()) && !/^[0-9]{18}$/.test($("#IDnumber").val()) ) {
		alert("您输入的身份证号有问题，请重新输入！");
		$("#IDnumber").addClass("error");
		return false;
	} else if ($("#province").val() == "" || $("#city").val() == "") {
		alert("请选择省市！");
		return false;
	} else {
		return true;
	}
}


</script>
</head>
<body>
<div id="page">
	<div id="description">
		<p>只要你具备解题能力和表达能力，能流畅的讲解初高中试题，科目不限，就是我们需要的人才!！</p>
		<p><span>极大的工作弹性</span><br />工作时间、空间都由你掌握！</p>
		<p><span>薪酬看得到</span><br />每录制一个音频可获得1-5元收入，月入过万不是梦！</p>
		<section>请填写如下信息报名</section>
	</div>
	<div id="survey">
		<form action="userSigningUpFunc" method="post" onsubmit="return verifyForm();">
			<section>
				<label>姓名<span class="necessary">*</span></label>
				<p><input type="text" id="teachername" name="teachername" placeholder="请填写真实姓名"></p>
			</section>
			<section>
				<label>手机号<span class="necessary">*</span></label>
				<p><input type="text" id="phonenumber" name="phonenumber" placeholder="请输入11位手机号"></p>
			</section>
			<section>
				<label>身份证号<span class="necessary">*</span></label>
				<p class="intro">身份证号是您提取收入的依据，请准确填写</p>
				<p><input type="text" id="IDnumber" name="IDnumber" placeholder="请输入15位或18位身份证号"></p>
			</section>
			<section id="city_1">
				<label>地区<span class="necessary">*</span></label>
				<p><select id="province" name="province" class="prov"></select></p>
				<p><select id="city" name="city" class="city"><option>请选择</option></select></p>
			</section>
			<section>
				<p><button type="submit">提交</button></p>
			</section>
		</form>
	</div>
</div>

<script type="text/javascript" src="<%=basePath %>js/bootstrap/jquery.js"/></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap/city.min.js"/></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap/jquery.cityselect.js"/></script>
<script type="text/javascript">
$("#city_1").citySelect({
	nodata:"none",
	required:false
});

</script>
</body>
</html>