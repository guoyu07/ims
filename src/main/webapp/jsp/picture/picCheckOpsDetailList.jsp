<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCheckStatus" %>
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
<title>检查管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<c:set var="preUrl" value="tranOpsDetailViewSearch
							?questionId=${ questionId }
							&teacher=${ teacher }
							&cstatus=${ cstatus }
							&grandParentId=${ grandParentId }&" />
	<div class="crumb">当前位置：检查管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>检查结果
					<select name="cstatus">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= TranOpsCheckStatus.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == cstatus }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>识别人<input type="text" value="${ teacher }" name="teacher" /></li>
				<li>
					<input type="hidden" value="${ grandParentId }" name="grandParentId"/>
					<button class="btn btn-primary btn-xs">查询</button>
				</li>  
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:50px">题目ID</th>
					<th style="min-width:40px">上传人</th>
					<th style="min-width:40px">识别人</th>
					<th>上传时间</th>
					<th>识别时间</th>
					<th style="min-width:90px">检查结果</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="question" items="${ tranOpsDetailList }">
				<tr>
					<td class="questionId"><a href="<%=basePath %>piccheck/auditCheckDetailById?questionId=${ question.id }&grandParentId=${ question.grandParentId }">${ question.tranId }</a></td>
					<td>${ question.orcPicture.userKey }</td>
					<td>${ question.orcPicture.operatorName }</td>
					<td> ${ func:formatDate(question.orcPicture.createTime) }</td>
					<td>${ func:formatDate(question.orcPicture.approveTime) }</td>
					<td>${ question.statusForShow }</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
</body>
</html>

