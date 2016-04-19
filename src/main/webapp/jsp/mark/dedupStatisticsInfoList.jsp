<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<title>去重统计</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="userTeamInfoList
							?startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：去重统计</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>去重时间<input class="form-control form-date" type="text" value="${ startTime }" name="startTime"/>~<input class="form-control form-date" type="text" value="${ endTime }" name="endTime"/></li>
				<li>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>
				<li>
					<a class="btn btn-primary btn-xs"  href="<%=basePath %>markDupPic/exportDedupStatisticsInfo?startTime=${ startTime }&endTime=${ endTime }">导出</a>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th>去重人</th>
					<th>去重题组数</th>
					<th>去重题备选题数</th>
					<th>重复题目选择数</th>
					<th>选择无重复组数</th>
					<th>有效去重题总数</th>
					<th>去重时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="question" items="${ dedupStatisticsList }">
					<tr>
						<td>${ question.userKey }</td>
						<td>${ question.operatCnt }</td>
						<td>${ question.analyzeCnt }</td>
						<td>${ question.clickCnt }</td>
						<td>${ question.nodupClickCnt }</td>
						<td>${ question.validCnt }</td>
						<td>${ question.dateStr }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
	<script type="text/javascript">
		MX.load({
			js: 'lib/sea',
			version: '<%=Version.version %>',
			success: function() {
				seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN'], function($, datetimepicker) {
					$('.form-date').datetimepicker({
						language: 'zh-CN',/*加载日历语言包，可自定义*/
						weekStart: 1,
						todayBtn:  1,
						autoclose: 1,
						todayHighlight: 1,
						forceParse: 0,
						showMeridian: 1
					});
				});
			}
		});
	</script>
</body>
</html>

