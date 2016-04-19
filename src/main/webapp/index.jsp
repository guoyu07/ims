<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html lang="zh-CN" style="height:100%;">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>学习宝题库运营平台</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=basePath%>css/moodstrap/bootstrap.css" rel="stylesheet">
<link rel="stylesheet"
	href="<%=basePath%>css/moodstrap/font-awesome.css">
<!-- 自定义优先级最高的样式表，保证选择器最高优先级，并且这个样式表始终排在最后面 -->
<link href="<%=basePath%>css/moodstrap/frame.css" rel="stylesheet">
<link href="<%=basePath%>css/moodstrap/supreme.css" rel="stylesheet">
<link rel="shortcut icon" href="<%=basePath%>image/favicon/favicon.ico">
<script type="text/javascript">
		var path = '<%=path%>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
<style type="text/css">
.enlarged .fa-chevron-left:before,.active .fa-chevron-left:before {
	content: '\f078'
}
</style>
</head>

<body class="index">
	<header class="navbar navbar-fixed-top bs-docs-nav" role="banner">
		<div class="container">
			<div class="navbar-header">
				<button class="navbar-toggle btn-navbar" type="button"
					data-toggle="collapse" data-target=".bs-navbar-collapse">
					<span>Menu</span>
				</button>
				<a href="#" id="menu-btn" class="pull-left menubutton hidden-xs"><i
					class="fa fa-bars"></i></a> <a href="<%=basePath%>index.jsp"
					class="navbar-brand">学习宝<span class="bold">题库运营</span>平台
				</a>
			</div>
			<nav class="collapse navbar-collapse bs-navbar-collapse"
				role="navigation">
				<ul class="nav navbar-nav pull-right">
					<li class="dropdown pull-right user-data">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<img src="<%=basePath%>image/teacher.png" width="24" height="24">
							<span class="bold"><shiro:principal /></span>
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="<%=basePath%>login/userLogout"><i class="fa fa-key"></i> Logout</a>
							</li>
						</ul>
					</li>
				</ul>
			</nav>
		</div>
	</header>

	<div class="content content-index" id="nav-container">
		<div class="sidebar">
			<div class="sidebar-dropdown">
				<a href="#">Navigation</a>
			</div>
			<ul id="nav">
				<!-- 超级管理员 -->
				<shiro:hasRole name="admin">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>1.录题列表</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#"  data-url="<%=basePath%>tranops/tranOpsAdminEditSearch"
								class="nav-link">试题列表</a></li>
						</ul>
                    </li>					
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>2.题目审核</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>tranops/tranOpsAdminAuditSearch">试题列表</a>
							</li>
						</ul>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>/teacher/tranOpsAuditSearch">题目加标签审核列表</a>
							</li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>3.抽检管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>dedup/checkRecordList">抽检去重题目列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>piccheck/checkRecordList">抽检图片列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>check/checkRecordList">抽检题目列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>4.批量识别管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>picture/orcPictureViewSearch">批量识别图片列表</a>
							</li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>picture/orcPictureCount">识别录题统计列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/orcBooksList">识别状态列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/orcBookRatesList">识别率统计</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>5.用户管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/userInfoList">用户列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/userBankInfoList">用户详细列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>6.小组管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>tranops/tikuTeamSearch">小组管理</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>tranops/tikuTeamInfoSearch">新增小组</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>7.用户信息</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/getUserInfoByUserKey">个人信息</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>8.公告管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>notice/announcementList">公告列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>9.书籍管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/booksInfoList">书籍列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/organizationList">来源列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>看图识字</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/unRecognitionPicture">未识别图片库</a>
							</li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/recognitionAnalysis">用户识别统计</a>
							</li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/recognitionList">识别列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/recognitionHistory">识别结果流水</a>
							</li>
						</ul></li>
					<li class="has-sub expanded"><a
						href="<%=basePath%>mark/getOneDupGroup" class="nav-has-sub"> <i
							class="fa fa-list-alt"></i> <span>题目去重</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>mark/getOneDupGroup">1.重复题目标注</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>markDupPic/dedupStatisticsInfo">3.去重题目统计</a>
							</li>
						</ul></li>
				</shiro:hasRole>
				<!--组员  -->
				<shiro:hasRole name="teamMember">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>1.录题列表</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>tranops/tranOpsEditSearch">试题列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>2.看图识字</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/recognitionList">识别列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>3.批量识别</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>picture/orcPictureMemberViewSearch">批量识别图片列表</a>
							</li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>4.用户信息</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/getUserInfoByUserKey">个人信息</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>题目去重</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>mark/getOneDupGroup">1.重复题目标注</a></li>
						</ul></li>
				</shiro:hasRole>
				<!--  组长-->
				<shiro:hasRole name="teamLeader">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>1.题目审核</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>tranops/tranOpsAuditSearch">试题列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>2.抽检管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>check/checkDetailRecordList">抽检题目列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>3.看图识字</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>recognition/recognitionList">识别列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>4.批量识别</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>piccheck/checkDetailRecordList">抽检图片列表</a>
							</li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>5.成员管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/userTeamInfoList">成员管理</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>6.用户信息</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/getUserInfoByUserKey">个人信息</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>7.识别状态管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/orcBooksCaptainList">识别状态列表</a></li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>book/orcBookRatesCaptainList">识别率统计</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>题目去重</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>mark/getOneDupGroup">1.重复题目标注</a></li>
						</ul></li>
				</shiro:hasRole>
				<!-- 去重 -->
				<shiro:hasRole name="mark">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>抽检管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>dedup/checkRecordList">抽检去重题目列表</a></li>
						</ul></li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>题目去重</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>mark/getOneDupGroup">1.重复题目标注</a></li>
						</ul></li>
				</shiro:hasRole>
				<!-- 自动化测试 -->
				<shiro:hasRole name="autoTest">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>自动化测试</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>batchpicture/orcPictureUpload">1.图片上传</a>
							</li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>batchpicture/orcPictureViewSearch">2.识别结果</a>
							</li>
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>batchpicture/orcPictureViewResult">3.识别率统计</a>
							</li>
						</ul></li>
				</shiro:hasRole>
				<!-- 教师组员 -->
				<shiro:hasRole name="teacherMember">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>1.题目加标签</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>teacher/tranOpsEditSearch">题目列表</a>
							</li>
						</ul>
					</li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>2.用户信息</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/getUserInfoByUserKey">个人信息</a></li>
						</ul>
					</li>						
				</shiro:hasRole>
				<!-- 教师组长-->
				<shiro:hasRole name="teacherTeamLeader">
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>1.题目加标签</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>teacher/tranOpsEditSearch">题目列表</a>
							</li>
						</ul>
					</li>				
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>2.题目加标签</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>teacher/tranOpsAuditSearch">题目审核列表</a>
							</li>
						</ul>
					</li>					
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>3.成员管理</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/userTeamInfoList">成员管理</a></li>
						</ul>
					</li>
					<li class="has-sub expanded"><a href="#" class="nav-has-sub">
							<i class="fa fa-list-alt"></i> <span>4.用户信息</span> <span
							class="pull-right"> <i class="fa fa-chevron-left"></i>
						</span>
					</a>
						<ul class="sub-nav" style="display:none">
							<li><a href="#" class="nav-link"
								data-url="<%=basePath%>user/getUserInfoByUserKey">个人信息</a></li>
						</ul>
					</li>
					
				</shiro:hasRole>
			</ul>
		</div>

		<div class="mainbar">
			<iframe id="i-frame" src="<%=basePath%>notice/announcementList"></iframe>
		</div>
		<div class="clear"></div>
	</div>

	<footer>
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<p class="copy">
						学习宝 &copy; 2015 | <a href="http://www.xuexibao.cn/">学习宝，一拍就知道</a>
					</p>
				</div>
			</div>
		</div>
	</footer>
	<script type="text/x-jquery-tmpl" id="tree-list-tmpl">
	{{each(key1, val1) item}}
	<li class="has-sub expanded">
		{{if $.type(val1) === "object"}}
		<a href="#" class="nav-has-sub">
			<i class="fa fa-list-alt"></i>
			<span>{{= key1}}</span>
			<span class="pull-right">
				<i class="fa fa-chevron-left"></i>
			</span>
		</a>
		<ul class="sub-nav" style="display:none">
			{{each(key2, val2) val1}}
				<li>
					<a href="#" class="nav-link" data-url="{{= val2}}">{{= key2}}</a>
				</li>
			{{/each}}
		</ul>
		{{else}}
		<a href="#" class="nav-link" data-url="{{= val1}}">
			<i class="fa fa-list-alt"></i>
			<span>{{= key1}}</span>
		</a>
		{{/if}}
	</li>
	{{/each}}
</script>
	<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				seajs.use(['page/index'], function(page) {
					page.init('nav');
				});
			});
		}
	});
</script>
</body>
</html>
