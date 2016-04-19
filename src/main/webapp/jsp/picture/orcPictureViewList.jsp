<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.OrcPictureCheckStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
<title>图片批量识别管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
    .modal-nav {
        display: block;
    }
    .modal-add-book>* {
        margin-top: 13px;
    }
    .modal-add-book>*:first-child {
        margin-top: 0;
    }
    .form-control.modal-mutil-line {
        height: auto;
    }
    .modal-add-book .row {
        max-height: 148px;
        overflow-y: scroll;
    }
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="orcPictureViewSearch
							?pictureId=${ pictureId }
							&status=${ status }
							&bookName=${ bookName }
							&userKey=${ userKey }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：图片批量识别管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>图片ID<input type="text" value="${ pictureId }" name="pictureId" /></li>
				<li>书名<input type="text" value="${ bookName }" name="bookName" /></li>
				<li>识别结果
					<select name="status">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= OrcPictureCheckStatus.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == status }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>提交人<input type="text" value="${ userKey }" name="userKey" /></li>
				<li>提交时间<input class="form-control form-date" type="text" value="${ startTime}" name="startTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form-date" type="text" value="${endTime}" name="endTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
				<li>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>
				<li>
					<a href="#" class="btn btn-success btn-xs ml10" id="batch-recognize">批量识别</a>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">图片ID</th>
					<th style="min-width:50px">书名</th>
					<th style="min-width:90px">提交人</th>
					<th style="min-width:90px">提交时间</th>
					<th style="min-width:90px">识别时间</th>
					<th style="min-width:40px">识别结果</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="picture" items="${ orcPictureList }">
				<tr>
					<td>
						<a href="<%=basePath %>picture/viewOrcPictureById?pictureId=${ picture.id }&status=${ status }&bookName=${ bookName }&userKey=${ userKey }&startTime=${ startTime }&endTime=${ endTime }">${ picture.id }</a>
					</td>
					<td>${ picture.books.name }</td>
					<td>${ picture.userKey }</td>
					<td>${ func:formatDate(picture.createTime) }</td>
					<td>${ func:formatDate(picture.approveTime) }</td>
					<td>${ picture.statusStr}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
	<div id="modal-dialog" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="popSure">确定</button>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN', 'module/BatchRecognize'], function($, datetimepicker, batchRecognize) {
				// 绑定datetimepicker插件
				$('.form-date').datetimepicker({
					language: 'zh-CN',/*加载日历语言包，可自定义*/
					weekStart: 1,
					todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					minView: 2,
					forceParse: 0,
					showMeridian: 1
				});
				// 绑定批量识别
				$('#batch-recognize').on('click', function(e) {
					var el = $(this);
					e.preventDefault();
					batchRecognize(el.attr('id'));
				});
			});
		}
	});
</script>
</body>
</html>

