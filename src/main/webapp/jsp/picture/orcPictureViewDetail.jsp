<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.OrcPictureCheckStatus" %>
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
	#photo {
		width: 100%;
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
								<div class="pull-left">图片id:${ orcPicture.id }</div>
								<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">${ func:formatDate(orcPicture.createTime) }</div>
								<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="photo-frame">
									<img id="photo" src=${ orcPicture.orcPictureUrl}>
								</div>
							</div>
						</div>
						<div class="padd" id="ocr-container">
							<c:set var="ERROR_RECORD" value="<%= OrcPictureCheckStatus.ERROR_RECORD %>"/>
							<c:set var="BEST_RECORD" value="<%= OrcPictureCheckStatus.BEST_RECORD %>"/>
							<c:set var="ERROR_UNRECORD" value="<%= OrcPictureCheckStatus.ERROR_UNRECORD %>"/>
							<c:set var="SOLVE_RIGHT" value="<%= OrcPictureCheckStatus.SOLVE_RIGHT %>"/>
							<c:set var="USOLVE" value="<%= OrcPictureCheckStatus.USOLVE %>"/>
							<c:set var="USOLVE_ERROR" value="<%= OrcPictureCheckStatus.USOLVE_ERROR %>"/>
							<c:set var="BEST_SOLVE_RIGHT" value="<%= OrcPictureCheckStatus.BEST_SOLVE_RIGHT %>"/>
							<c:if test="${ USOLVE.id == orcPicture.status }">
							<button class="btn btn-success" style="width:48%;">识别正确</button>
							<button class="btn btn-danger btn-error" style="width:48%;float:right;">识别错误</button>
							<button class="btn btn-primary btn-input" style="width:100%;display:none;">手动录入</button>
							</c:if>
							<c:if test="${ SOLVE_RIGHT.id == orcPicture.status }">
							<p class="co_red">识别结果：${ orcPicture.statusStr }</p>
							</c:if>
							<c:if test="${ ERROR_RECORD.id == orcPicture.status }">
							<p class="co_red">识别结果： ${ orcPicture.statusStr }<a class="btn btn-primary" href="<%=basePath %>tranops/viewTranOpsByPictureId?pictureId=${ orcPicture.id }" style="width:100%;">查看录入结果</a></p>
							</c:if>
							<c:if test="${ BEST_RECORD.id == orcPicture.status }">
							<p class="co_red">识别结果： ${ orcPicture.statusStr }<a class="btn btn-primary" href="<%=basePath %>tranops/viewTranOpsByPictureId?pictureId=${ orcPicture.id }" style="width:100%;">查看录入结果</a></p>
							</c:if>
							<c:if test="${ ERROR_UNRECORD.id == orcPicture.status }">
							<p class="co_red">识别结果：${ orcPicture.statusStr }<button class="btn btn-primary btn-input" style="width:100%;">手动录入</button></p>
							</c:if>
							<c:if test="${ USOLVE_ERROR.id == orcPicture.status }">
							<p class="co_red">识别结果：${ orcPicture.statusStr }<button class="btn btn-primary btn-input" style="width:100%;">手动录入</button></p>
							</c:if>
							<c:if test="${ BEST_SOLVE_RIGHT.id == orcPicture.status }">
							<p class="co_red">识别结果：${ orcPicture.statusStr }<button class="btn btn-primary btn-input" style="width:100%;">手动录入</button></p>
							</c:if>
						</div>
						<div style="width:172px;margin:16px auto;">
							<ul class="pagination">
								<c:set var="searchUrl" value="&status=${ status }&bookName=${ bookName }&userKey=${ userKey }&startTime=${ startTime }&endTime=${ endTime }"/>
								<li>
									<c:if test="${ lastpictureId != null }">
									<a href="<%=basePath%>picture/viewOrcPictureById?pictureId=${ lastpictureId }${ searchUrl }">Prev</a>
									</c:if>
									<c:if test="${ lastpictureId == null }">
									<span>Prev</span>
									</c:if>
								</li>
								<li>
									<span>当前页</span>
								</li>
								<li>
									<c:if test="${ nextpictureId != null }">
									<a href="<%=basePath%>picture/viewOrcPictureById?pictureId=${ nextpictureId }${ searchUrl }">Next</a>
									</c:if>
									<c:if test="${ nextpictureId == null }">
									<span>Next</span>
									</c:if>
								</li>
							</ul>
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
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				var ocrContainer = $('#ocr-container'),
					btnSuccess = ocrContainer.find('.btn-success'),
					btnInput = ocrContainer.find('.btn-input');
				$('#photo').on('click', function(e) {
					window.open($(this).attr('src'), '', 'width=800,scrollbars=yes,status=yes');
				});
				ocrContainer.on('click', '.btn-success', function(e) {
					var el = $(this).prop('disabled', true);
					$.ajax({
						url: window.basePath + 'picture/edit',
						type: 'GET',
						data: {
							pictureId: '${ orcPicture.id }',
							status: 1
						},
						dataType: 'json',
						success: function(data) {
							if(data.status === 0) {
								document.location.reload();
							} else {
								el.prop('disabled', false);
								alert(data.msg);
							}
						}
					});
				}).on('click', '.btn-error', function(e) {
					var el = $(this);
					btnSuccess.hide();
					el.hide();
					btnInput.show();
					$.ajax({
						url: window.basePath + 'picture/edit',
						type: 'GET',
						data: {
							pictureId: '${ orcPicture.id }',
							status: 2
						},
						dataType: 'json',
						success: function(data) {
							if(data.status !== 0) {
								alert(data.msg);
							}
						}
					});
				}).on('click', '.btn-input', function(e) {
					$(this).removeClass('btn-primary');
					document.location.href = window.basePath +'picture/eidtOrcPictureById?pictureId=${ orcPicture.id }'
				});
			});
		}
	});
</script>
</body>
</html>
