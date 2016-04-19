<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
</head>
<body>
	<c:set var="preUrl" value="checkDetailRecordList
							?month=${ month }
							&questionId=${ questionId }
							&subject=${ subject }
							&teacher=${ teacher }
							&status=${ status }
							&transStartDate=${ transStartDate }
							&transEndDate=${ transEndDate }
							&checkStartDate=${ checkStartDate }
							&checkEndDate=${ checkEndDate }&" />
	<div class="crumb">当前位置：抽检列表</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>抽检月份<select name="month">
					<option value="">全部</option>
					<c:set var="enumMonths" value="<%= EnumMonth.values() %>"/>
					<c:forEach var="enumMonth" items="${ enumMonths }">
						<option value="${ enumMonth.id }" <c:if test="${ enumMonth.id == (month)}">selected = "selected"</c:if>>${ enumMonth.month }</option>
					</c:forEach></select>
				</li>
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:40px">抽检时间</th>
					<th style="min-width:40px">抽检日期范围</th>
					<th style="min-width:20px">抽检题目数</th>
					<th style="min-width:20px">合格数</th>
					<th style="min-width:30px">不合格数</th>
					<th style="min-width:30px">抽检合格率</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="record" items="${ checkDetailRecordList }">
				<tr>
					<td>
						<a href="<%=basePath %>piccheck/tranCaptainOpsDetailViewSearch?parentId=${ record.id }">${ func:formatDate(record.createTime) }</a>
					</td>
					<td>${ func:formatDate(record.startTime) } -- ${ func:formatDate(record.endTime) }</td>
					<td>${ record.num }</td>
					<td>${ record.passNum }</td>
					<td>${ record.unpassNum }</td>
					<td>${ record.ratioStr }</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
</body>
</html>
