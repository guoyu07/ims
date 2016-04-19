<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.DedupCheckStatus" %>
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
<title>抽检去重题目列表</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:set var="preUrl" value="dedupDetailViewSearch
							?questionId=${ questionId }
							&grandParentId=${ grandParentId }
							&parentId=${ parentId }
							&teacher=${ teacher }
							&cstatus=${ cstatus }&" />
	<div class="crumb">当前位置：抽检去重题目列表</div>
	<div class="static2_base">
		<form id="search-form">
			<ul class="searchSub">
				<li>检查结果
					<select name="cstatus">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= DedupCheckStatus.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == cstatus }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>
					<input type="hidden" value="${ grandParentId }" name="grandParentId"/>
					<input type="hidden" value="${ parentId }" name="parentId"/>
				</li>
				<li>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">重复题目组id</th>
					<th style="min-width:40px">去重时间</th>
					<th>检查结果</th>
					<th style="min-width:90px">检查时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="question" items="${ tranOpsDetailList }">
					<tr>
						<td class="questionId"><a href="<%=basePath %>mark/getCheckOneDupGroup?checkId=${ question.id }">${ question.id }</a></td>
						<td>${ func:formatDate(question.approveTime) }</td>
						<td>${ question.statusForShow }</td>
						<td>${ func:formatDate( question.checkTime ) }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
</body>
</html>

