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
	<title>公告</title>
	<meta charset="UTF-8">
	<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
	<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>css/moodstrap/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=basePath %>css/moodstrap/font-awesome.css">
	<link rel="stylesheet" href="<%=basePath %>css/moodstrap/widgets.css">
	<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
	<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
	<script type="text/javascript">
		var path = '<%=path %>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
	<style type="text/css">
	#notice-container .notice-tips {
		cursor: pointer;
		color: #8a6d3b;
	}
	.widget-foot {
		text-align: center;
	}
	</style>
</head>
<body>
	<c:set var="preUrl" value="announcementList?" />
	<div class="content">
		<div class="mainbar">
			<div class="page-head" id="notice-container">
				<h2 class="pull-left">公告</h2>
				<div class="clearfix"></div>
				<c:choose>
				<c:when test="${isread == false}">
				<div class="bread-crumb text-warning notice-tips" data-alt="公告" data-user-key="${userKey}" data-read-id="${firstid}">您有新公告未阅读</div>
				</c:when>
				<c:otherwise>
				<div class="bread-crumb">公告</div>
				</c:otherwise>
				</c:choose>
				<div class="clearfix"></div>
			</div>
			<div class="matter">
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-head">
									<div class="pull-left">公告栏</div>
									<div class="widget-icons pull-right"></div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="padd">
										<ul class="recent">
											<c:forEach var="announcement" items="${ announcementList }">
											<li class="recent-list">
												<div class="recent-content">
													<div class="recent-meta"><span class="creat-time">${ func:formatDate(announcement.createTime) }</span> by <span class="author">${ announcement.operator }</span>
													</div>
													<div class="notice-content">${ announcement.text }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											</c:forEach>
										</ul>
									</div>
									<div class="widget-foot">
										<%@ include file = "../inc/newpage.jsp" %>
									</div>
								</div>
							</div> 
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br class="clear"/>
	<script>
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				$(function() {
					$('#notice-container').on('click', '.notice-tips', function(e) {
						var el = $(this);
						el.removeClass('notice-tips text-warning')
						.text(el.data('alt'));
						$.ajax({
							url: window.basePath + 'user/updateUserReadId',
							type: 'POST',
							data: {
								userkey: el.data('user-key'),
								readid: el.data('read-id')
							},
							success: function(data) {
								if(data.status !== 0) {
									alert(data.msg);
								}
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