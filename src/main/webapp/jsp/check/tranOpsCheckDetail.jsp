<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCompleteStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCheckStatus" %>
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
<link href="<%=basePath %>css/moodstrap/widgets.css" rel="stylesheet"> 
<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
<style type="text/css">
	.photo-frame {
		max-height: 320px;
		overflow-y: scroll;
	}
	.check-history li {
		margin: 0 12px;
		padding: 0;
	}
	.check-history .check-time, .check-history .checkor, .check-history .check-status {
		width: 40%;
		float: left;
		text-align: left;
		height: 24px;
		line-height: 24px;
	}
	.check-history .checkor, .check-history .check-status {
		width: 30%
	}
	.check-history li .clear{
		clear:both;
	}
	.limit-width img, .limit-width table, .limit-width div{
		max-width:100%;
	}
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
	<div class="mainbar">
		<div class="crumb">当前位置：抽查试题</div>
		<div class="page-head">
			<h2 class="pull-left">识别结果</h2>
			<div class="clearfix"></div>
			<div class="matter">
				<div class="container">
					<div class="row">
						<div class="col-md-4">
							<div class="widget">
								<div class="widget-head">
									<div class="pull-left">图片id:${ orcPicture.id }</div>
									<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">${ func:formatDate(orcPicture.createTime) }</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="photo-frame limit-width">
										<img src=${ orcPicture.orcPictureUrl}>
									</div>
								</div>
							</div>
							<div class="padd">
								<p class="co_red">识别结果：${ orcPicture.statusStr }</p>
							</div>
						</div>
						<div class="col-md-8">
							<div class="widget">
								<div class="widget-head">
									<div class="pull-left">机器识别结果</div>
									<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">${ func:formatDate(orcPicture.createTime) } by Robert</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="padd">
										<ul class="recent">
											<li>
												<div class="recent-content">
													<div class="recent-meta">问题描述</div>
													<div class="limit-width">${ orcPicture.content }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">题目解答</div>
													<div class="limit-width">${ orcPicture.answer }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">解题思路</div>
													<div class="limit-width">${ orcPicture.solution }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">知识点</div>
													<div>${ orcPicture.knowledge }</div>
													<div class="clearfix"></div>
												</div>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="static2_base articleDetail">
						<h2>题目内容</h2>
						<div class="p2em bor_bottom mb20">
							<p>
								<span>题目id：${ checkDetail.tranId }</span>
								<span class="ml10">科目：${ checkDetail.tranOps.subject }</span>
							</p>
							<p>
								<span>题干录入人：${ checkDetail.tranOps.contentOperatorName }</span>
								<span class="ml10">录题时间：${ func:formatDate(checkDetail.tranOps.createTime)}</span>
								<span class="ml10">审核通过时间：${ func:formatDate(checkDetail.tranOps.approveTime) }</span>
							</p>
							<p>
								<span>答案录入人：${ checkDetail.tranOps.operatorName }</span>
								<span class="ml10">录题时间：${ func:formatDate(checkDetail.tranOps.createTime) }</span>
								<span class="ml10">审核通过时间：${ func:formatDate(checkDetail.tranOps.approveTime) }</span>
							</p>
							<div class="articleContent">${ checkDetail.tranOps.content }</div>
						</div>
						<h2>答案</h2>
						<div class="p2em bor_bottom mb20">
							<div class="articleContent">${ checkDetail.tranOps.answer }</div>
						</div>
						<h2>解题思路</h2>
						<div class="p2em bor_bottom mb20">
							<div class="articleContent">${ checkDetail.tranOps.solution }</div>
						</div>
						<h2>知识点</h2>
						<div class="p2em bor_bottom mb20">
							<div class="articleContent">${ checkDetail.tranOps.knowledge }</div>
						</div>
						<h2>审核意见</h2>
						<div class="p2em  mb20">
							<c:set var="AUDIT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_THROUGH %>"/>
							<c:set var="AUDIT_NOT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_NOT_THROUGH %>"/>
							<c:set var="PENDING_AUDIT" value="<%= TranOpsAuditStatus.PENDING_AUDIT %>"/>
							<c:set var="HALF_THROUGH" value="<%= TranOpsAuditStatus.HALF_THROUGH %>"/>
							<c:set var="BEST_AUDIT_THROUGH" value="<%= TranOpsAuditStatus.BEST_AUDIT_THROUGH %>"/>
							<c:if test="${ AUDIT_THROUGH.id == checkDetail.tranOps.auditStatus || HALF_THROUGH.id == checkDetail.tranOps.auditStatus || BEST_AUDIT_THROUGH.id == checkDetail.tranOps.auditStatus}">
							<p class="co_red">审核结果：${ checkDetail.tranOps.statusForShow }</p>
							</c:if>
							<c:if test="${ AUDIT_NOT_THROUGH.id == checkDetail.tranOps.auditStatus }">
							<p class="co_red">审核结果：${ checkDetail.tranOps.statusForShow }，原因：${ checkDetail.lastTranOpsApprove.reason }</p>
							</c:if>
						</div>
						<h2>检查意见</h2>
						<div class="p2em  mb20">
							<c:set var="ELIGIBLE" value="<%= TranOpsCheckStatus.ELIGIBLE %>"/>
							<c:set var="UNELIGIBLE" value="<%= TranOpsCheckStatus.UNELIGIBLE %>"/>
							<c:set var="UCHECK" value="<%= TranOpsCheckStatus.UCHECK %>"/>
							<c:if test="${ ELIGIBLE.id == checkDetail.cstatus }">
							<p id="cresult" class="co_red">检查结果：${ checkDetail.statusForShow }</p>
							</c:if>
							<c:if test="${ UNELIGIBLE.id == checkDetail.cstatus }">
							<p id="cresult" class="co_red">检查结果：${ checkDetail.statusForShow }，原因：${ checkDetail.creason }</p>
							</c:if>
							<c:if test="${ UCHECK.id == checkDetail.cstatus }">
							<p id="audit-zone">检查结果：
								<label for="accept"><input type="radio" name="status" id="accept" value="${ ELIGIBLE.id }"/>${ ELIGIBLE.desc }</label>
								<label for="reject" class="ml10"><input type="radio" name="status" id="reject" value="${ UNELIGIBLE.id }"/>${ UNELIGIBLE.desc }</label>
							</p>
							<div id="result" class="co_red">
								<div class="accept" style="display:none">检查合格</div>
								<div class="reject" style="display:none">
									检查不合格，原因：
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
						<c:if test="${ UCHECK.id != checkDetail.cstatus }">
						<div  class="center_margin">
							<a class="btn btn-default" href="<%=basePath %>check/tranOpsDetailViewSearch?grandParentId=${ grandParentId }">返回</a>
						</div>
						</c:if>
						<c:if test="${ UCHECK.id == checkDetail.cstatus }">
						<div class="center_margin" id="submit-zone">
							<button class="btn btn-primary mr20 confirm-btn">确定</button>
							<a class="btn btn-default" href="<%=basePath %>check/tranOpsDetailViewSearch?grandParentId=${ grandParentId }">取消</a>
						</div>
						</c:if>
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
			seajs.use(['module/Audit'], function(audit) {
				audit.init('${ checkDetail.id }', {
					url: window.basePath + 'check/audit',
					acceptVal: 1,
					confirm: function(data) {
						if(data.result) {
							document.location.href = window.basePath + 'check/auditCheckDetailById?questionId='+ data.result + '&grandParentId=${ grandParentId }';
						} else {
							document.location.href = window.basePath + 'check/tranOpsDetailViewSearch?grandParentId=${ grandParentId }';
						}
					}
				});
			});
		}
	});
</script>
</body>
</html>
