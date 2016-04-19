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
	<title>二次识别</title>
	<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
	<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>css/moodstrap/widgets.css" rel="stylesheet">
	<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
	<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=basePath %>css/exam/problem.css" />
	<style type="text/css">
		#photo{width:100%;max-width:100%;margin:0;}
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
		.widget-content{cursor:pointer;}
		.widget-content.select-result{background-color:#a0c0e8;color:#fff;}
	</style>
	<script type="text/javascript">
		var path = '<%=path %>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
</head>
<body class="edit-body">
	<div class="mainbar">
		<div class="matter">
			<div class="container" id="orc-container">
				<div class="col-md-4">
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">图片id:${ orcPicture.id }</span></div>
							<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;">
								<span id="creat_time">${ func:formatDate(orcPicture.createTime) }</span>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="photo-frame">
								<img src="${ orcPicture.orcPictureUrl}">
							</div>
						</div>
					</div>
					<div class="padd">
					<c:set var="ERROR_RECORD" value="<%= OrcPictureCheckStatus.ERROR_RECORD %>"/>
					<c:set var="ERROR_UNRECORD" value="<%= OrcPictureCheckStatus.ERROR_UNRECORD %>"/>
					<c:set var="SOLVE_RIGHT" value="<%= OrcPictureCheckStatus.SOLVE_RIGHT %>"/>
					<c:set var="USOLVE" value="<%= OrcPictureCheckStatus.USOLVE %>"/>
					<c:set var="USOLVE_ERROR" value="<%= OrcPictureCheckStatus.USOLVE_ERROR %>"/>
					</div>
					<a class="btn btn-danger" href="<%=basePath %>picture/eidtOrcPictureById?pictureId=${ orcPicture.id }&noSelection=1" style="width:48%;float:;">无匹配选项</a>
					<button class="btn btn-success with-result" style="width:48%;float:right;">已选最佳</button>
				</div>
				<c:forEach var="otherReco" items="${ otherRecos }">
				<div class="col-md-4 result">
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">其他识别结果</div>
							<div class="recent-meta" style="display:inline-block;float:right;font-weight:normal;"> ${ func:formatDate(orcPicture.createTime) } by Robert</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content" data-target="${ otherReco.target }">
							<div class="padd">
								<ul class="recent">
									<li>
										<div class="recent-content">
											<div class="recent-meta">问题描述</div>
											<div class='body-input'>${ otherReco.content }</div>
											<div class="clearfix"></div>
										</div>
									</li>
									<li>
										<div class="recent-content">
											<div class="recent-meta">题目解答</div>
											<div class='answer-input'>${ otherReco.answer }</div>
											<div class="clearfix"></div>
										</div>
									</li>
									<li>
										<div class="recent-content">
											<div class="recent-meta">解题思路</div>
											<div class="analysis-input">${ otherReco.solution }</div>
											<div class="clearfix"></div>
										</div>
									</li>
									<li>
										<div class="recent-content">
											<div class="recent-meta">知识点</div>
											<div class="tags-input">${ otherReco.knowledge }</div>
											<div class="clearfix"></div>
										</div>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<div class="clearfix"></div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				var data = {}, orcContainer, headerReg, dividerReg;
				orcContainer = $('#orc-container');
				selectBest = orcContainer.find('.with-result');
				headerReg = /<h4>【(?:解析|答案|点评)】<\/h4>/g;
				dividerReg = /(?:<br>)?<hr class="divider">/g
				orcContainer.on('click', '.result', function(e) {
					var el = $(this),
						container = el.find('.widget-content');
					container.find('.select-result').removeClass('select-result');
					container.addClass('select-result');
					// 清除样式表
					container.find('link').remove();
					data.body = container.find('.body-input').html().replace(dividerReg, '');
					data.answer = container.find('.answer-input').html().replace(headerReg, '').replace(dividerReg, '');
					data.analysis = container.find('.analysis-input').html().replace(headerReg, '').replace(dividerReg, '');
					selectBest.data('selected', 1);
					data.pictureId = '${ orcPicture.id }';
					data.picUrl = '${ orcPicture.orcPictureUrl}';
					data.target = container.data('target');
				}).on('click', '.photo-frame', function(e) {
					window.open($(this).attr('src'), '', 'width=800,scrollbars=yes,status=yes');
				}).on('click', '.with-result', function(e) {
					if(!$(this).data('selected')){
						alert('请点选最佳！');
					} else {
						window.localStorage.result = JSON.stringify(data);
						document.location.href = '<%=basePath %>jsp/picture/orcOtherEditDetail.jsp';
					}
				});
			});
		}
	});
</script>
</body>
</html>
