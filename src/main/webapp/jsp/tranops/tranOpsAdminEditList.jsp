<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCompleteStatus" %>
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
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>老师录答案列表</title>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
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
<c:set var="preUrl" value="tranOpsAdminEditSearch
							?questionId=${ questionId }
							&subject=${ subject }
							&realType=${ realType }
							&realLearnPhase=${ realLearnPhase }
							&teacher=${ teacher }
							&status=${ status }
							&complete=${ complete }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：录题列表</div>
	<div class="static2_base">
		<form id="search-form">
		<ul class="searchSub">
			<li>题目ID<input type="text" value="${ questionId }" name="questionId" /></li>
			<li>学科
				<select name="subject">
					<option value="">全部</option>
					<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == subject }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>题型
				<select name="realType">
					<option value="">全部</option>
					<c:set var="enumSubjects" value="<%= EnumSubjectType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == realType }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>年级
				<select name="realLearnPhase">
					<option value="">全部</option>
					<c:set var="enumSubjects" value="<%= EnumGradeType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == realLearnPhase }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>审核状态
				<select name="status">
					<option value="">全部</option>
					<c:set var="enumStatuses" value="<%= TranOpsAuditStatus.values() %>"/>
					<c:forEach var="enumStatus" items="${ enumStatuses }">
						<option value="${ enumStatus.id }" <c:if test="${ enumStatus.id == status }">selected = "selected"</c:if>>${ enumStatus.desc }</option>
					</c:forEach>
				</select>
			</li>
			<li>完整程度
				<select name="complete">
					<option value="">全部</option>
					<c:set var="enumCompletes" value="<%= TranOpsCompleteStatus.values() %>"/>
					<c:forEach var="enumComplete" items="${ enumCompletes }">
						<option value="${ enumComplete.id }" <c:if test="${ enumComplete.id == complete }">selected = "selected"</c:if>>${ enumComplete.desc }</option>
					</c:forEach>
				</select>
			</li>
			<li>录制日期<input class="form-control form_date" type="text" value="${ startTime }" name="startTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form_date" type="text" value="${ endTime }" name="endTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
			<li>
				<button class="btn btn-primary btn-xs">搜索</button>
			</li>
			<li>
				<a class="btn btn-primary btn-xs" href="<%=basePath %>jsp/tranops/addTranOps.jsp">新增</a>
			</li>
		</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">题目ID</th>
					<th style="min-width:40px">完整程度</th>
					<th style="min-width:40px">学科</th>
					<th style="min-width:40px">题型</th>
					<th style="min-width:40px">年级</th>
					<th style="min-width:100px">题干</th>
					<th style="min-width:90px">录题时间</th>
					<th style="min-width:80px">审核结果</th>
					<th style="min-width:90px">审核未通过原因</th>
				</tr>
			</thead>
			<tbody id="question-list">
			<c:set var="AUDIT_NOT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_NOT_THROUGH %>"/>
				<c:forEach var="audio" items="${ tranOpsList }">
				<tr>
					<td class="questionId"><a href="<%=basePath %>tranops/adminEditTranOpsById?questionId=${ audio.id }">${ audio.id }</a></td>
					<td>${ audio.completeForShow }</td>
					<td>${ audio.subject }</td>
					<td>${ audio.type }</td>
					<td>${ audio.learnphase }</td>
					<td>${ audio.content }</td>
					<td>${ func:formatDate(audio.createTime) }</td>
					<td>${ audio.statusForShow }</td>
					<td><c:if test="${ AUDIT_NOT_THROUGH.id == audio.auditStatus }">${ audio.lastTranOpsApprove.reason }</c:if></td>
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
				$(".form_date").datetimepicker({
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
