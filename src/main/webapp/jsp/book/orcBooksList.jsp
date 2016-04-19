<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumRole" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumOrcResult" %>
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
<title>识别状态管理</title>
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
<c:set var="preUrl" value="orcBooksList
							?isbn=${ isbn }
							&operator=${ operator }
							&teamName=${ teamName }
							&status=${ status }
							&name=${ name }
							&startTime = ${ startTime }
							&endTime = ${ endTime }&" />
	<div class="crumb">当前位置：识别状态管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>识别结果
					<select name="status">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= EnumOrcResult.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
							<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == status }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>书名<input type="text" value="${ booksInfo.name }" name="name" /></li>
				<li>IBSN<input type="text" value="${ booksInfo.isbn }" name="isbn" /></li>
				<li>录题人<input type="text" value="${ orcbooksInfo.operatorName }" name="operator" /></li>
				<li>录题组<input type="text" value="${ orcbooksInfo.tikuTeam.name }" name="teamName" /></li>
				<li>识别完成时间<input class="form-control form-date" type="text" value="${ startTime }" data-date-format="yyyy-mm-dd 00:00:00" name="startTime">~<input class="form-control form-date" type="text" value="${ endTime }" data-date-format="yyyy-mm-dd 23:59:59" name="endTime"/></li> 
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
				<li>
					<a href="<%=basePath%>book/orcBookExport2Excel?&isbn=${ isbn }&operator=${ operator }&teamName=${ teamName }&status=${ status }&name=${ name }&startTime=${ startTime }&endTime=${ endTime }" class="btn btn-success btn-xs ml10">导出结果</a>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">书名</th>
					<th style="min-width:50px">ISBN</th>
					<th style="min-width:40px">出版社</th>
					<th style="min-width:40px">来源</th>
					<th style="min-width:50px">录题人</th>
					<th style="min-width:50px">所在小组</th>
					<th style="min-width:90px">开始识别时间</th>
					<th style="min-width:90px">完成识别时间</th>
					<th style="min-width:50px">状态</th>
					<th style="min-width:90px">操作</th>
				</tr>
			</thead>
			<tbody id="recognize-list">
				<c:forEach var="orcbooksInfo" items="${ orcBooksInfoList }">
					<tr>
						<td class="name"><span class="name_show">${ orcbooksInfo.books.name}</span></td>
				 		<td class="ISBN">${ orcbooksInfo.books.isbn }</td>
						<td class="publishingHouse">${ orcbooksInfo.books.publishingHouse } </td>
						<td class="sourceId">${ orcbooksInfo.books.sourceId } </td>
						<td class="operatorName">${ orcbooksInfo.operatorName }</td>
						<td class="teamId">${ orcbooksInfo.tikuTeam.name }</td>
						<td class="operatorStartime">${ func:formatDate(orcbooksInfo.operatorStartime) }</td>
						<td class="operatorEndtime">${ func:formatDate(orcbooksInfo.operatorEndtime) }</td>
						<td class="status">${ orcbooksInfo.statusStr }</td>
						<td>
							<c:if test="${ 0 == orcbooksInfo.status }">			      
							<button class="btn btn-primary btn-xs ml10 edit-btn" data-id="${ orcbooksInfo.id }">识别完成</button>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN'], function($, datetimepicker) {
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
				$('#recognize-list').on('click', '.edit-btn', function(e) {
					var el = $(this);
					if(confirm('确认完成识别？')) {
						$.ajax({
							url: window.basePath + 'book/updateOrcBoosResult',
							type: 'get',
							dataType: 'json',
							data: {
								id: el.data('id')
							},
							success: function(data) {
								var tr;
								if(data.status == 0) {
									tr = el.closest('tr');
									tr.find('.operatorEndtime').text(data.result.operatorEndtime);
									tr.find('.status').text('识别完成');
									el.remove();
								} else {
									alert(data.msg);
								}
							}
						});
					}
				});
			});
		}
	});
</script>
</body>
</html>

