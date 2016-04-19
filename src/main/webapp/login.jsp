<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	<title>党员信息管理平台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta name="author" content="">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<link rel="shortcut icon" href="<%=basePath %>image/favicon/favicon.ico">
	<script type="text/javascript">
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/moodstrap/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/moodstrap/font-awesome.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/moodstrap/style.css">
	<style type="text/css">
		.sign-window{max-width:422px;max-height:288px;position:absolute;top:50%;left:50%;margin-left:-211px;margin-top:-180px;}
		.sign-window .widget{width:420px;}
		img.pull-right{width:22px;height:22px;}
		.sign-btns{padding:0 15px;}
		.sign-btns .btn-danger{width:48%;max-width:48%;float:left;}
		.sign-btns .btn-default{width:48%;max-width:48%;float:right;}
	</style>
</head>

<body>

<!-- Form area -->
<div class="admin-form">
	<div class="container">

    <div class="row">
      	<div class="sign-window">
            <div class="widget">
	            <div class="widget-head">
	            	党员信息管理平台
	            	<%-- <img class="pull-right" src="<%=basePath %>image/favicon/logo.png"> --%>
	            </div>

              	<div class="widget-content">
                	<div class="padd">
	                	<form id="login_form" class="form-horizontal">
		                    <div class="form-group">
		                      <div class="col-lg-12">
		                        <input type="text" class="form-control account" name="loginId" placeholder="请输入账号">
		                      </div>
		                    </div>
		                    <div class="form-group">
		                      <div class="col-lg-12">
		                        <input type="password" class="form-control password" name="password" placeholder="请输入密码">
		                      </div>
		                    </div>
							
							<div class="form-group sign-btns">
								<button type="submit" class="btn btn-danger inputBt confirm">登录</button>
								<button type="reset" class="btn btn-default inputBt reset">清除</button>
								<br class="clear-both">
							</div>
	                  	</form>
	                </div>
                </div>
              
                <div class="widget-foot">
                	无法登录？请联系运营团队
                	<!-- 运营团队信息在后面考虑加入小弹层或者外链 -->
                </div>
            </div>  
      </div>
    </div>
  </div> 
</div>

<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'page/login'], function($, Login) {
				var login = new Login('login_form');
			});
		}
	});
</script>
</body>
</html>

