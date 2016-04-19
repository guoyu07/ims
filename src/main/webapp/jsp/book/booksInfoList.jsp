<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumRole" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumTiKuPersonStatus" %>
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
<title>书籍管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
    .modal-add-book>* {
        margin-top: 13px;
    }
    .modal-add-book>*:first-child {
        margin-top: 0;
    }
    .modal-add-book .row {
        max-height: 68px;
        overflow-y: scroll; 
        margin-left: 37px;
    }
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="booksInfoList
							?isbn=${ isbn }
							&sourceName=${ sourceName }
							&status=${ status }
							&name=${ name }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
	<div class="crumb">当前位置：书籍管理</div>
	<div class="static2_base">
		<form>
		<ul class="searchSub">
			<li>书名<input type="text" value="${ booksInfo.name }" name="name"/></li>
			<li>IBSN<input type="text" value="${ booksInfo.isbn }" name="isbn"/></li>
			<li>来源<input type="text" value="${ booksInfo.sourceId }" name="sourceName"/></li>
			<li>添加时间<input class="form-control form-date" type="text" value="${ startTime }" data-date-format="yyyy-mm-dd 00:00:00" name="startTime">~<input class="form-control form-date" type="text" value="${ endTime }" data-date-format="yyyy-mm-dd 23:59:59" name="endTime"/></li>
			<li><button class="btn btn-primary btn-xs">搜索</button></li>
			<li><a href="#" class="btn btn-success btn-xs ml10" id="add-book-btn" data-toggle="modal"  data-target=".modal">添加书籍</a></li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">书名</th>
					<th style="min-width:50px">ISBN</th>
					<th style="min-width:90px">年级</th>
					<th style="min-width:90px">科目</th>
					<th style="min-width:40px">出版社</th>
					<th style="min-width:40px">来源</th>
					<th style="min-width:90px">添加时间</th>
					<th style="min-width:90px">精品书籍状态</th>
					<th style="min-width:90px">操作</th>
				</tr>
			</thead>
			<tbody id="book-list">
				<c:forEach var="booksInfo" items="${ booksInfoList }">
				<tr data-id="${ booksInfo.id}" data-name="${ booksInfo.name}" data-isbn="${ booksInfo.isbn}" data-grade="${ booksInfo.grade}" data-subject="${ booksInfo.subject}" data-publishing-house="${ booksInfo.publishingHouse}" data-source-id="${ booksInfo.sourceId }" data-create-time-Forshow="${ func:formatDate(booksInfo.createTime) }">
					<td>${ booksInfo.name}</td>
			 		<td>${ booksInfo.isbn }</td>
					<td>${ booksInfo.grade }</td>
					<td>${ booksInfo.subject }</td>
					<td>${ booksInfo.publishingHouse } </td>
					<td class="sourceId">${ booksInfo.sourceId }</td>
					<td>${ func:formatDate(booksInfo.createTime) }</td>
					<td>
						<button class="btn btn-primary btn-xs ml10 btn-best" data-best="${ booksInfo.best }" ><c:if test="${ booksInfo.best == 0 }">置为精品</c:if><c:if test="${ booksInfo.best == 1 }">置为非精品</c:if></button>
					</td>
					<td>
						<button class="btn btn-primary btn-xs ml10 add-source" data-toggle="modal" data-target=".modal">增加来源</button>
					</td>
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
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer"></div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
<script id="book-item-tmpl" type="text/x-jquery-tmpl">
	<tr data-id="{{= id}}" data-name="{{= name}}" data-isbn="{{= isbn}}" data-grade="{{= grade}}" data-subject="{{= subject}}" data-publishing-house="{{= publishingHouse}}" data-source-id="{{= sourceId}}" data-create-time-Forshow="{{= createTimeForShow}}">
		<td>{{= name}}</td>
		<td>{{= isbn}}</td>
		<td>{{= grade}}</td>
		<td>{{= subject}}</td>
		<td>{{= publishingHouse}}</td>
		<td class="sourceId">{{= sourceId }}</td>
		<td>{{= createTimeForShow}}</td>
		<td>
			<button class="btn btn-primary btn-xs ml10 add-source" data-toggle="modal" data-target=".modal">增加来源</button>
		</td>
	</tr>
</script>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN', 'module/BookInfo'], function($, datetimepicker, bookInfo) {
				var bookList = $('#book-list');
				$('.form-date').datetimepicker({
					language: 'zh-CN',/*加载日历语言包，可自定义*/
					weekStart: 1,
					todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					forceParse: 0,
					minView: 2,
					showMeridian: 1
				});
				$('#add-book-btn').on('click', function(e) {
					e.preventDefault();
					bookInfo.addBook($(this).attr('id'), {
						callback: function(data) {
							bookList.prepend($('#book-item-tmpl').tmpl(data));
						}
					});
				});
				bookList.on('click', '.add-source', function(e) {
					var row = $(this).closest('tr');
					bookInfo.addSource2Book(row.data(), {
						callback: function(data) {
							row.find('.sourceId').text(data.sourceId);
							// 修改dom上的属性值
							row[0].dataset.sourceId = data.sourceId;
						}
					});
				}).on('click', '.btn-best', function(e) {
					var el = $(this),
						row = el.closest('tr');
					$.ajax({
						url: window.basePath + 'book/editBest',
						type: 'POST',
						data: {
							id: row.data('id'),
							best: el.data('best') ^ 1
						},
						dataType: 'json',
						success: function(data) {
							if(data.status === 0) {
								document.location.reload();
							} else {
								alert(data.msg);
							}
						}
					});
				});
			});
		}
	});
</script>
</body>
</html>

