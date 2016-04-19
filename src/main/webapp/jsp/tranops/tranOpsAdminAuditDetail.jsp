<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
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
<title>审核试题</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
<body>
	<div class="crumb">当前位置：审核试题</div>
	<div class="static2_base articleDetail">
		<h2>题目内容</h2>
		<div class="p2em bor_bottom mb20">
			<p>
				<span>题目id：${ tranOps.id }</span>
				<span class="ml10">科目：${ tranOps.subject }</span>
			</p>
			<div class="articleContent">${ tranOps.content }</div>
		</div>
		<h2>答案</h2>
		<div class="p2em bor_bottom mb20">
			<div class="articleContent">${ tranOps.answer }</div>
		</div>
		<h2>解题思路</h2>
		<div class="p2em bor_bottom mb20">
			<div class="articleContent">${ tranOps.solution }</div>
		</div>
		<h2>知识点</h2>
		<div class="p2em bor_bottom mb20">
			<div class="articleContent">${ tranOps.knowledge }</div>
		</div>
		<h2>审核意见</h2>
		<div class="p2em  mb20">
			<c:set var="AUDIT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_THROUGH %>"/>
			<c:set var="AUDIT_NOT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_NOT_THROUGH %>"/>
			<c:set var="PENDING_AUDIT" value="<%= TranOpsAuditStatus.PENDING_AUDIT %>"/>
			<c:set var="HALF_THROUGH" value="<%= TranOpsAuditStatus.HALF_THROUGH %>"/>
			<c:set var="BEST_AUDIT_THROUGH" value="<%= TranOpsAuditStatus.BEST_AUDIT_THROUGH %>"/>
			<c:if test="${ AUDIT_THROUGH.id == tranOps.auditStatus || HALF_THROUGH.id == tranOps.auditStatus || BEST_AUDIT_THROUGH.id == tranOps.auditStatus }">
			<p class="co_red">审核结果：${ tranOps.statusForShow }</p>
			</c:if>
			<c:if test="${ AUDIT_NOT_THROUGH.id == tranOps.auditStatus }">
			<p class="co_red">审核结果：${ tranOps.statusForShow }，原因：${ tranOps.lastTranOpsApprove.reason }</p>
			</c:if>
			<c:if test="${ PENDING_AUDIT.id == tranOps.auditStatus }">
			<p id="audit-zone">审核结果：
				<label for="accept"><input type="radio" name="status" value="${ AUDIT_THROUGH.id }" id="accept"/>${ AUDIT_THROUGH.desc }</label>
				<label for="reject" class="ml10"><input type="radio" name="status" value="${ AUDIT_NOT_THROUGH.id }" id="reject"/>${ AUDIT_NOT_THROUGH.desc }</label>
			</p>
			<div id="result" class="co_red">
				<div class="accept" style="display:none">审核通过</div>
				<div class="reject" style="display:none">
					审核未通过，原因：
					<select class="reason">
						<option value="0">识别结果正确，不该录</option>
						<option value="1">文字格式问题</option>
						<option value="2">图片有问题</option>
						<option value="3">学科选择错误</option>
						<option value="4">请补充解题思路&知识点</option>
						<option value="5">其他（可填写）</option>
					</select>
					<input type="text" class="form-control reason-txt" style="margin-top:6px;width:280px;height:20px;line-height:20px;display:none" maxlength="30" val=""/>
				</div>
			</div>
			</c:if>
		</div>
		<div class="center_margin" id="submit-zone">
			<c:if test="${ PENDING_AUDIT.id == tranOps.auditStatus }">
			<button class="btn btn-primary mr20 confirm-btn">确定</button>
			</c:if>
			<c:if test="${ PENDING_AUDIT.id != tranOps.auditStatus }">
			<button class="btn btn-primary mr20 reset-btn">修改为未审核</button>
			</c:if>
			<a class="btn btn-default" href="<%=basePath %>tranops/tranOpsAdminAuditSearch">取消</a>
		</div>
	</div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['module/Audit'], function(audit) {
				audit.init('${ tranOps.id }', {
					url: window.basePath + 'tranops/audit',
					confirm: function() {
						alert('审核完成');
						document.location.href = window.basePath + 'tranops/tranOpsAdminAuditSearch';
					},
					reset: function() {
						alert('重置完成');
						document.location.href = window.basePath + 'tranops/tranOpsAdminAuditSearch';
					}
				});
			});
		}
	});
</script>
</body>
</html>
