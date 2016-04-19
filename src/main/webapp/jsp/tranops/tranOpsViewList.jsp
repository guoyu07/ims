<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
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
<title>录题管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()});}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="tranOpsViewSearch
							?questionId=${ question.id }
							&subject=${ question.subject }
							&teacher=${ question.teacher }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：录题管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>题目ID<input type="text" value="${ question.id }" name="questionId" /></li>
				<li>学科
					<select name="subject">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == subject }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
						</c:forEach>
					</select>
				<li>录制人<input type="text" value="${ teacher }" name="teacher" /></li>
				<li>录制时间<input class="form-control form-date" type="text" value="${ startTime }" name="startTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form-date" type="text" value="${ endTime }" name="endTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
				<li>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">题目ID</th>
					<th style="min-width:40px">年级</th>
					<th style="min-width:40px">学科</th>
					<th>内容</th>
					<th>答案</th>
					<th style="min-width:90px">录制人</th>
					<th style="min-width:90px">录制时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="question" items="${ tranOpsList }">
				<tr>
					<td class="questionId"><a href="<%=basePath %>tranops/viewTranOpsById?questionId=${ question.id }">${ question.id }</a></td>
					<td>${ question.learnphase }</td>
					<td>${ question.subject }</td>
					<td>${ question.content }</td>
					<td>${ question.answer }</td>
					<td>${ question.operatorName }</td>
					<td>${ func:formatDate(question.createTime) }</td>
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
				// 绑定datetimepicker插件
				$(".form-date").datetimepicker({
					language: 'zh-CN',/*加载日历语言包，可自定义*/
					weekStart: 1,
					todayBtn: 1,
					autoclose: 1,
					todayHighlight: 1,
					minView: 2,
					forceParse: 0,
					showMeridian: 1
				});
			});
		}
	});
</script>
</body>
</html>

