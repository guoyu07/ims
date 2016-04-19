<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.PictureCheckStatus" %>
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
<title>识图抽检详情</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/moodstrap/widgets.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet"> 
<style type="text/css">
	/*.photo-frame{max-height:60px;}*/
  #photo{width:100%;max-width:100%;}
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
			<h2 class="pull-left">识别结果</h2>
			<div class="clearfix"></div>
			<div class="bread-crumb">识别结果</div>
			<div class="clearfix"></div>
		</div>
		<div class="matter">
			<div class="container">
				<div class="row">
					<div class="col-md-4">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">图片id:<span id="pic_id">${ checkDetail.tranId }</span></div>
								<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">
									<span id="creat_time">${ func:formatDate(checkDetail.orcPicture.createTime) }</span>
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="photo-frame">
									<img id="photo" src="${ checkDetail.orcPicture.orcPictureUrl}" >
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-8">
						<div class="widget">
							<div class="widget-head">
								<div class="pull-left">机器识别结果</div>
								<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">${ func:formatDate(checkDetail.orcPicture.createTime) } by Robert</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="padd">
									<ul class="recent">
										<li>
											<div class="recent-content">
												<div class="recent-meta">问题描述</div>
												<div>${ checkDetail.orcPicture.content }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">题目解答</div>
												<div>${ checkDetail.orcPicture.answer }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">解题思路</div>
												<div>${ checkDetail.orcPicture.solution }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">知识点</div>
												<div>${ checkDetail.orcPicture.knowledge }</div>
												<div class="clearfix"></div>
											</div>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</div>
				<h2>检查意见</h2>
				<div class="p2em  mb20">
					<c:set var="ELIGIBLE" value="<%= PictureCheckStatus.ELIGIBLE %>"/>
					<c:set var="UNELIGIBLE" value="<%= PictureCheckStatus.UNELIGIBLE %>"/>
					<c:set var="UCHECK" value="<%= PictureCheckStatus.UCHECK %>"/>
					<c:if test="${ UCHECK.id != checkDetail.cstatus }">
					<p class="co_red">检查结果：${ checkDetail.statusForShow }</p>
					</c:if>
					<c:if test="${ UCHECK.id == checkDetail.cstatus }">
					<p id="audit-zone">检查结果：
						<label for="accept"><input type="radio" name="status" id="accept" value="${ ELIGIBLE.id }"/>${ ELIGIBLE.desc }</label>
						<label for="reject" class="ml10"><input type="radio" name="status" id="reject" value="${ UNELIGIBLE.id }"/>${ UNELIGIBLE.desc }</label>
					</p>
					<div id="result" class="co_red">
						<div class="accept" style="display:none">检查合格</div>
						<div class="reject" style="display:none">检查不合格</div>
					</div>
					</c:if>
				</div>
				<div class="center_margin" id="submit-zone">
					<c:if test="${ UCHECK.id == checkDetail.cstatus }">
					<button class="btn btn-primary mr20 confirm-btn">确定</button>
					</c:if>
					<a class="btn btn-default" href="<%=basePath %>piccheck/tranOpsDetailViewSearch?grandParentId=${ grandParentId }">取消</a>
				</div>
			</div>
		</div>
	</div>
	<div class="clearfix"></div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['module/Audit'], function(audit) {
				audit.init('${ checkDetail.id }', {
					url: window.basePath + 'piccheck/audit',
					noReason: true,
					acceptVal: 1,
					confirm: function(data) {
						if(data.result) {
							document.location.href = window.basePath + 'piccheck/auditCheckDetailById?questionId='+ data.result + '&grandParentId=${ grandParentId }';
						} else {
							document.location.href = window.basePath + 'piccheck/tranOpsDetailViewSearch?grandParentId=${ grandParentId }';
						}
					},
					error: function() {
						document.location.href = window.basePath + 'piccheck/tranOpsDetailViewSearch?grandParentId=${ grandParentId }';
					}
				});
			});
		}
	});
</script>
</body>
</html>
