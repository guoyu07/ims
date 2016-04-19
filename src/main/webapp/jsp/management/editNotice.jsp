<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<title>编辑公告</title>
	<meta charset="UTF-8">
	<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
	<link rel="stylesheet" href="<%=basePath %>css/exam/editor.css" />
	<link rel="stylesheet" href="<%=basePath %>css/bootstrap/bootstrap.3.3.5.min.css" />
	<link rel="stylesheet" href="<%=basePath %>css/moodstrap/font-awesome.css"> 
	<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=basePath %>css/moodstrap/widgets.css">
	<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
	<script type="text/javascript">
		var path = '<%=path %>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
</head>
<body>
	<div class="content">
		<div class="mainbar">
			<div class="page-head">
				<h2 class="pull-left">编辑公告</h2>
				<div class="clearfix"></div>
				<div class="bread-crumb">编辑公告</div>
				<div class="clearfix"></div>
			</div>
			<div class="matter">
				<div class="container">
					<c:set var="generalToolConfig" value='[
						[{"name":"ForeColor-","title":"醒目色","command":"ForeColor","param":"#d9534f"},
						{"name":"FontSize-","title":"大号字","command":"FontSize","param":"4"},{"name":"blod-","title":"粗体","command":"bold"},
						{"name":"italic-","title":"斜体","command":"italic"},
						{"name":"underline-","title":"下划线","command":"underline"}],
						[{"name":"insert-ul","title":"无序列表","command":"insertunorderedlist"},
						{"name":"insert-ol","title":"有序列表","command":"insertorderedlist"},
						{"name":"insert-img","title":"插入图片","command":""}]
					]'></c:set>
					<c:set var="generalCommands" value="${ func:parseArray(generalToolConfig) }"></c:set>
					<div id="edit-notice" class="edit-box">
						<div class="edit-btns">
							<ul class="tr-bar">
								<c:forEach var="commands" items="${ generalCommands }">
								<ul class="tr-group">
									<c:forEach var="command" items="${ func:parseArray(commands) }">
									<li class="tr-btn tool-btn ${ command.name }" title="${ command.title }" data-command="${ command.command }" data-param="${ command.param }">
										<input type="button">
									</li>
									</c:forEach>
								</ul>
								</c:forEach>
							</ul>
						</div>
						<div class="edit-border">
							<div class="edittable" contentEditable></div>
						</div>
					</div>
					<button id="submit-notice" type="submit" class="btn btn-primary btn-block">提交</button>
				</div>
			</div>
		</div>
	</div>
	<div id="image-input" class="modal fade"  tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer"></div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'module/Editor'], function($, Editor) {
				var noticeEditor = new Editor('edit-notice');
				$('#submit-notice').on('click', function(e) {
					var el = $(this);
					el.prop('disabled', true);
					noticeEditor.getContent(function(data) {
						$.ajax({
							url: window.basePath + 'notice/addAnnouncement',
							type: 'GET',
							data: {
								text: data.content
							},
							dataType: 'json',
							success: function(data) {
								alert(data.msg);
								el.prop('disabled', false);
								if(data.status === 0) {
									document.location.href = window.basePath + 'notice/announcementList'
								}
							},
							error: function() {
								alert('网络错误，请稍后重试');
							}
						});
					});
				});
			});
		}
	});
</script>
</body>
</html>