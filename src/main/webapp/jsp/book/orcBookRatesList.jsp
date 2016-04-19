<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumRole" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumOrcResult" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<title>识别率统计管理</title>
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
<c:set var="preUrl" value="orcBookRatesList
							?isbn=${ isbn }
							&operator=${ operator }
							&teamName=${ teamName }
							&status=${ status }
							&name=${ name }
							&startTime = ${ startTime }
							&endTime = ${ endTime }&" />
	<div class="crumb">当前位置：识别率统计管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>书名<input type="text" value="${ booksInfo.name }" name="name" /></li>
				<li>IBSN<input type="text" value="${ booksInfo.isbn }" name="isbn" /></li>
				<li>录题人<input type="text" value="${ orcbooksInfo.operatorName }" name="operator" /></li>
				<li>录题组<input type="text" value="${ orcbooksInfo.tikuTeam.name }" name="teamName" /></li>
				<li>结束识别时间<input class="form-control form-date" type="text" value="${ startTime }" data-date-format="yyyy-mm-dd 00:00:00" name="startTime">~<input class="form-control form-date" type="text" value="${ endTime }" data-date-format="yyyy-mm-dd 23:59:59" name="endTime"/></li>
				<li><button class="btn btn-primary btn-xs">搜索</button></li>
			</ul>
		</form>

		<table class="table table-hover table-bordered table-condensed" id="memberListTab">
			<thead>
				<tr class="info">
					<th style="min-width:50px">书名</th>
					<th style="min-width:90px">年级</th>
					<th style="min-width:90px">科目</th>
					<th style="min-width:50px">ISBN</th>
					<th style="min-width:40px">出版社</th>
					<th style="min-width:50px">录题人</th>
					<th style="min-width:50px">所在小组</th>
					<th style="min-width:90px">开始识别时间</th>
					<th style="min-width:90px">完成识别时间</th>
					<th style="min-width:50px">识别率（%）</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="orcbooksInfo" items="${ orcBookRatesInfoList }">
					<tr>
						<td class="name"><span class="name_show">${ orcbooksInfo.books.name}</span><input  type="hidden" value="${ orcbooksInfo.id }" class="Id" /></td>
							<td class="grade">${ orcbooksInfo.books.grade } </td>
							<td class="subject">${ orcbooksInfo.books.subject } </td>
							<td class="ISBN">${ orcbooksInfo.books.isbn }</td>
						<td class="publishingHouse">${ orcbooksInfo.books.publishingHouse } </td>
						<td class="operatorName">${ orcbooksInfo.orcBooks.operatorName }</td>
						<td class="teamId">${ orcbooksInfo.tikuTeam.name }</td>
						<td class="operatorStartime">${ func:formatDate(orcbooksInfo.orcBooks.operatorStartime) }</td>
						<td class="operatorEndtime">${ func:formatDate(orcbooksInfo.orcBooks.operatorEndtime) }</td>
						<td class="rate">${ orcbooksInfo.orcRate }</td>
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

