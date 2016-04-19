<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumMonth" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
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
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>录题列表</title>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
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
	<c:set var="preUrl" value="checkRecordList
							?month=${ month }&" />
	<div class="crumb">当前位置：抽检列表</div>
	<div class="static2_base">
		<form>
		<ul class="searchSub">	
			<li>抽检月份
				<select name="month">
					<option value="">全部</option>
					<c:set var="enumMonths" value="<%= EnumMonth.values() %>"/>
					<c:forEach var="enumMonth" items="${ enumMonths }">
						<option value="${ enumMonth.id }" <c:if test="${ enumMonth.id == (month)}">selected = "selected"</c:if>>${ enumMonth.month }</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<button class="btn btn-primary btn-xs">搜索</button>
			</li>
			<li>
				<a href="#" class="btn btn-success btn-xs ml10" id="generate-sample-btn">生成抽检</a>
			</li>
		</ul>
		</form>
		<table class="table table-hover table-bordered" id="memberListTab">
			<thead>
				<tr class="info">
					<th style="min-width:40px">抽检时间</th>
					<th style="min-width:40px">抽检日期范围</th>
					<th style="min-width:20px">抽检小组数量</th>
					<th style="min-width:20px">检查状态</th>		
					<th style="min-width:20px">操作</th>		
				</tr>
			</thead>
			<tbody>
				<c:forEach var="record" items="${ checkRecordList }">
				<tr><td><a href="<%=basePath %>check/tranOpsDetailViewSearch?grandParentId=${ record.id }">${ func:formatDate(record.createTime) }</a></td>
					<td class="record-time" >${ func:formatDate( record.startTime) } -- ${ func:formatDate(record.endTime) }</td>
					<td class="record-count">${ record.teamCount }</td>
					<td class="record-status">${ record.statusString }</td>
					<td><a class="btn btn-primary btn-xs"  href="<%=basePath %>check/checkDetailRecordExport2Excel?parentId=${ record.id }">导出抽检结果</a></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>

    </div>
<div id="modal-dialog" class="modal fade"  tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
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
			seajs.use(['lib/jquery', 'module/SampleCheck'], function($, sampleCheck) {
				// 绑定生成抽检
				$("#generate-sample-btn").on('click', function(e) {
					var el = $(this);
					e.preventDefault();
					sampleCheck(window.basePath + 'check/generateCheckRecord', {
						source: el.attr('id')
					});
				});
			});
		}
	});
</script>
</body>
</html>
