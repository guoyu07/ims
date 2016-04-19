<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumDiff" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumQuestionCategory" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumTeacherAuditStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
<title>标注试题审核</title>
<link href="../css/bootstrap/bootstrap.3.3.5.min.css" rel="stylesheet" type="text/css"/>
<link href="../css/merge.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	.page-header {
		padding: 8px 20px;
		margin: 0 0 15px;
	}
	.container {
		width: 98%;
		border: 1px solid #ddd;
		padding-bottom: 5px;
		margin: 0 1%;
	}
	footer.container {
		margin-bottom: 15px;
		padding-top: 10px;
		text-align: center;
	}
	#knowledge-tree-container {
		position: absolute;
		border: 1px solid #ddd;
		background: white;
		min-width: 600px;
		height: 300px;
		overflow-y: scroll;  
		z-index: 10;
	}
	.knowledge-close {
		float: right;
		padding: 5px;
		z-index: 5;
	}
	.knowledge-tag {
		margin-left: 3px;
	}
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '../';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
	<header class="page-header">
		<span class="h3">标注试题审核</span>
	</header>
	<section class="container">
		<header class="h4">题目</header>
		<article>${ tranOps.content }</article>
	</section>
	<section class="container">
		<header class="h4">答案</header>
		<article>${ tranOps.answer }</article>
	</section>
	<section class="container">
		<header class="h4">解题思路</header>
		<article>${ tranOps.solution }</article>
	</section>
	<section class="container">
		<header class="h4">年级、学科、题型</header>
		<article>
			<div class="row form-inline">
				<div class="col-xs-2">
					年级：<c:set var="enumLearnPhases" value="<%= EnumGradeType.values() %>"/>
						<c:forEach var="enumLearnPhase" items="${ enumLearnPhases }">
						<c:if test="${ enumLearnPhase.id == tranOps.realLearnPhase }">${ enumLearnPhase.chineseName }</c:if>
						</c:forEach>
				</div>
				<div class="col-xs-2">
					学科：<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<c:if test="${ enumSubject.id == tranOps.realSubject }">${ enumSubject.chineseName }</c:if>
						</c:forEach>
				</div>
				<div class="col-xs-2">
					题型：<c:set var="enumSubjectTypes" value="<%= EnumSubjectType.values() %>"/>
						<c:forEach var="enumSubjectType" items="${ enumSubjectTypes }">
						<c:if test="${ enumSubjectType.id == tranOps.realType }">${ enumSubjectType.chineseName }</c:if>
						</c:forEach>
				</div>
			</div>
		</article>
	</section>
	<section class="container">
		<header class="h4">知识点</header>
		<article class="form-inline">
			<div class="knowledge-list">
				<c:forEach var="knowledge" items="${ tranOps.knowledgeNodeArray }">
				<span class="label label-default knowledge-tag" data-id="${ knowledge.id }">${ knowledge.text }</span>
				</c:forEach>
			</div>
		</article>
	</section>
	<section class="container">
		<header class="h4">难度</header>
		<article>
			<c:set var="enumDiffs" value="<%= EnumDiff.values() %>"/>
			<c:forEach var="enumDiff" items="${ enumDiffs }">
			<c:if test="${ enumDiff.id == tranOps.realDiff }">${ enumDiff.description }</c:if>
			</c:forEach>
		</article>
	</section>
	<section class="container">
		<header class="h4">题目分类</header>
		<article class="row">
			<c:set var="enumQuestionCategorys" value="<%= EnumQuestionCategory.values() %>"/>
			<c:forEach var="categoryArray" items="${ tranOps.questionCategoryArray }">
				<c:forEach var="enumQuestionCategory" items="${ enumQuestionCategorys }">
				<c:if test="${ enumQuestionCategory.id == categoryArray }"><div class="col-xs-2">${ enumQuestionCategory.description }</div></c:if>
				</c:forEach>
			</c:forEach>
		</article>
	</section>
	<section class="container">
		<header class="h4">审核意见</header>
		<c:set var="PENDING"				value="<%= EnumTeacherAuditStatus.PENDING %>"/>
	   	<c:set var="COMPLETE"			value="<%= EnumTeacherAuditStatus.COMPLETE %>"/>
	   	<c:set var="AUDIT_THROUGH"		value="<%= EnumTeacherAuditStatus.AUDIT_THROUGH %>"/>
	   	<c:set var="AUDIT_NOT_THROUGH"	value="<%= EnumTeacherAuditStatus.AUDIT_NOT_THROUGH %>"/>
		<c:choose>
			<c:when test="${tranOps.teacherStatus == AUDIT_THROUGH.id}">
				<article class="text-success">
					<p>审核结果：审核通过</p>
				</article>
			</c:when>
			<c:when test="${tranOps.teacherStatus == AUDIT_NOT_THROUGH.id}">
				<article class="text-danger">
					<p>审核结果：审核未通过</p>
					<p>错误原因：${tranOps.auditReason }</p>
				</article>
			</c:when>
			<c:otherwise>
				<article id="audit-result">
					<label class="radio-inline">
						<input type="radio" name="result" value="${ AUDIT_THROUGH.id }"> 审核通过
					</label>
					<label class="radio-inline">
						<input type="radio" name="result" value="${ AUDIT_NOT_THROUGH.id }"> 审核未通过
					</label>
					<div class="form-inline reject-reason" style="display:none">
						<input type="text" class="form-control" value="" placeholder="请填写错误原因">
					<div>
				</article>
			</c:otherwise>
		</c:choose>
		
	</section>
	<footer class="container">
		<c:if test="${tranOps.teacherStatus == COMPLETE.id}">
			<button id="submit" class="btn btn-primary">提交</button>
			<a href="tranOpsAuditSearch" class="btn btn-default" style="margin-left:64px">返回</a>
		</c:if>
		<c:if test="${tranOps.teacherStatus != COMPLETE.id}">
			<a href="tranOpsAuditSearch" class="btn btn-default">返回</a>
		</c:if>
	</footer>
	<script>
		MX.load({
			js: 'lib/sea',
			version: '150528',
			success: function() {
				seajs.use(['lib/jquery'], function($) {
					var auditResult = $('#audit-result'),
						rejectReason = auditResult.find('.reject-reason');
					auditResult.on('click', 'input[name=result]', function(e) {
						rejectReason.toggle(+$(this).val() === ${ AUDIT_NOT_THROUGH.id });
					});
					$('#submit').on('click', function(e) {
						var el = $(this), data = {
							questionId: '${ tranOps.id }'
						};
						data.status = +auditResult.find('input[name=result]:checked').val();
						if(!data.status) {
							alert('请选择审核结果');
							return;
						}
						if(data.status === ${ AUDIT_NOT_THROUGH.id }) {
							data.reason = rejectReason.find('input').val().trim();
							if(!data.reason) {
								alert('请填写错误原因');
								return;
							}
						}
						el.prop('disabled', true);
						$.ajax({
							url: 'audit',
							type: 'POST',
							data: data,
							dataType: 'json',
							success: function(data) {
								el.prop('disabled', false);
								if(data.status === 0) {
									document.location.href = 'auditNext';
								} else {
									alert(data.msg);
								}
							},
							error: function() {
								el.prop('disabled', false);
								alert('网络情况不佳，请稍后再试');
							}
						});
					});
				});
			}
		});
	</script>
</body>
</html>
