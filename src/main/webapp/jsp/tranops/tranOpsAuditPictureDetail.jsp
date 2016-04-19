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
<link href="<%=basePath %>css/moodstrap/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/widgets.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
<style type="text/css">
  .photo-frame{max-height:320px;overflow-y:scroll;}
  #photo{max-width:100%;}
  .check-history li{margin:0 12px;padding:0;}
  .check-history .check-time{width:40%;float:left;text-align:left;height:24px;line-height:24px;}
  .check-history .checkor{width:30%;float:left;text-align:center;height:24px;line-height:24px;}
  .check-history .check-status{width:30%;float:left;text-align:right;height:24px;line-height:24px;}
  .check-history li .clear{clear:both;}
  #body_ocr img,#body_ocr table,#body_ocr div{max-width:100%;}
  #answer_ocr img,#answer_ocr table,#answer_ocr div{max-width:100%;}
  #analysis_ocr img,#analysis_ocr table,#analysis_ocr div{max-width:100%;}
  #body_input img,#body_input table,#body_input div{max-width:100%;}
  #answer_input img,#answer_input table,#answer_input div{max-width:100%;}
  #analysis_input img,#analysis_input table,#analysis_input div{max-width:100%;}
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
	<div class="mainbar">
		<div class="page-head">
			<h2 class="pull-left">试题审核</h2>
			<div class="clearfix"></div>
		</div>
		<div class="matter">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">图片id:${ orcPicture.id }</div>
								<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">${ func:formatDate(orcPicture.createTime) }</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="photo-frame">
									<img src="${ orcPicture.orcPictureUrl}">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">机器识别题目</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="padd">
									<ul class="recent">
										<li>
											<div class="recent-content">
												<div class="recent-meta">问题描述</div>
												<div>${ orcPicture.content }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">题目解答</div>
												<div>${ orcPicture.answer }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">解题思路</div>
												<div>${ orcPicture.solution }</div>
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
					<div class="col-md-6">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">录入题目id：<span id="audioId">${ tranOps.id }</span></div>	
								<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">
									<span id="subject_input">${ tranOps.subject }</span>&nbsp;&nbsp;&nbsp;&nbsp;
									<span id="creat_time">2015-03-16 15:54</span>
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="padd">
									<ul class="recent">
										<li>
											<div class="recent-content">
											<div class="recent-meta">问题描述</div>
											<div id="body_input">${ tranOps.content }</div>
											<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">题目解答</div>
												<div id="answer_input">${ tranOps.answer }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">解题思路</div>
												<div id="analysis_input">${ tranOps.solution }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">知识点</div>
												<div id="tags_input">${ tranOps.knowledge }</div>
												<div class="clearfix"></div>
											</div>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="p2em  mb20">
		<h4>审核意见</h4>
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
			<label for="accept"><input type="radio" name="status" id="accept" value="${ AUDIT_THROUGH.id }"/>${ AUDIT_THROUGH.desc }</label>
			<label for="reject" class="ml10"><input type="radio" name="status" id="reject" value="${ AUDIT_NOT_THROUGH.id }"/>${ AUDIT_NOT_THROUGH.desc }</label>
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
		<c:if test="${ PENDING_AUDIT.id != tranOps.auditStatus }">
		<div  class="center_margin" id="submit-zone">
			<button class="btn btn-primary mr20 reset-btn">修改为未审核</button>
			<a class="btn btn-default" href="<%=basePath %>tranops/tranOpsAuditSearch">返回</a>
		</div>
		</c:if>
		<c:if test="${ PENDING_AUDIT.id == tranOps.auditStatus }">
		<div class="center_margin" id="submit-zone">
			<button class="btn btn-primary mr20 confirm-btn">确定</button>
			<a class="btn btn-default" href="<%=basePath %>tranops/tranOpsAuditSearch">取消</a>
		</div>
		</c:if>
		<div class="clearfix"></div>
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
						document.location.href = window.basePath + 'tranops/tranOpsAuditSearch';
					},
					reset: function() {
						alert('重置完成');
						document.location.href = window.basePath + 'tranops/tranOpsAuditSearch';
					}
				});
			});
		}
	});
	</script>
</body>
</html>
