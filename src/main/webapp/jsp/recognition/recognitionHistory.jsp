<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
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
<title>识别结果</title>
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
<c:set var="preUrl" value="recognitionHistory
							?fileId=${ fileId }
							&operator=${ operator }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：识别结果流水</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>识别人<input type="text" value="${ operator }" name="operator" /></li>
				<li>图片ID<input type="text" value="${ fileId }" name="fileId" /></li>
				<li>识别日期<input class="form-control form-date" type="text" value="${ startTime }" data-date-format="yyyy-mm-dd 00:00:00" name="startTime">~<input class="form-control form-date" type="text" value="${ endTime }" data-date-format="yyyy-mm-dd 23:59:59" name="endTime"/></li>
				<li>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>
				<li>
					<a href="#" class="btn btn-success btn-xs ml10" id="import-imgs" data-toggle="modal" data-target=".modal">导入图片</a>
				</li>
			</ul>
		</form>
		<div style="font-size: 10pt">待识别图片：${remainRecognitionAmount}&nbsp;&nbsp;&nbsp;&nbsp;今日识别图片：${todayRecognitionAmount}&nbsp;&nbsp;&nbsp;&nbsp;总识别图片：${totalRecognizedNum}</div>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">识别人</th>
					<th style="min-width:40px">识别时间</th>
					<th style="min-width:40px">图片ID</th>
					<th>识别图片</th>
					<th>识别结果</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="question" items="${ recognitionList }">
					<tr>
						<td>${ question.operator }</td>
						<td>${ func:formatDate(question.recognitionTime) }</td>
						<td>${ question.fileId }</td>
						<td><image src="<%=basePath%>${ question.recognitionPicture.filePath }"  /></td>
						<td>${ question.result }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="../inc/newpage.jsp"%>
	</div>
	<div id="modal-dialog" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
						<span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		MX.load({
			js: 'lib/sea',
			version: '<%=Version.version %>',
			success: function() {
				seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN', 'module/Dialog', 'util/uploadFile', 'util/jquery.tmpl'], function($, datetimepicker, dialog, uploader, tmpl) {
					$('.form-date').datetimepicker({
						language: 'zh-CN',/*加载日历语言包，可自定义*/
						weekStart: 1,
						todayBtn: 1,
						autoclose: 1,
						todayHighlight: 1,
						minView: 2,
						forceParse: 0,
						showMeridian: 1
					});
					$('#import-imgs').on('click', function(e) {
						var el = $(this);
						e.preventDefault();
						dialog.init('modal-dialog', {
							sizeClass: 'modal-sm',
							title: '选择图片压缩包',
							content: [
								'<p>',
									'<input class="img-select" type="file" multiple="multiple">',
								'</p>'
							].join(''),
							force: 1,
							confirm: function() {
								var Self = this, container;
								container = Self._container;
								uploader(container.find('.img-select')[0].files, {
									url: window.basePath + 'recognition/addPic',
									end: function(results) {
										var len, errorResults, error;
										errorResults = results.filter(function(o) {
											return o.status !== 0;
										});
										len = errorResults.length;
										if(len > 0) {
											error = [
												'<div style="color:#c00;">本组图片处理完毕，以下压缩包上传失败：</div>',
												'<ul>',
													'{{each(i, item) data}}',
													'<li>',
														'{{= item.name}}-{{= item.msg}}',
													'</li>',
													'{{/each}}',
												'</ul>',
												'<div style="color:#c00;">请修复问题文件重新上传！</div>'
											].join('');
											$.template('errorTemplate', error);
											container.html($.tmpl('errorTemplate', {data: errorResults}));
										} else {
											container.text('本组图片处理完毕，全部上传成功！');
										}
									}
								});
								container.html([
									'<div style="text-align:center;">正在上传，请等待…</div>',
									'<img src="' + window.basePath + 'image/loading.gif" style="display:block;width:24px;margin:0 auto;">'
								].join(''));
							}
						});
					});
				});
			}
		});
	</script>
</body>
</html>
