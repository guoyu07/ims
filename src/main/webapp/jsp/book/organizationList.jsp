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
<title>机构来源管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:set var="preUrl" value="organizationList
							?name=${ name }&" />
	<div class="crumb">当前位置：机构来源管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>机构来源名称<input type="text" value="${ booksInfo.name }" name="name" id="name"/></li>
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:90px">Id</th>
					<th style="min-width:50px">机构来源</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="booksInfo" items="${ organizationList }">
				<tr>
					<td>${booksInfo.id}</td>
					<td>${ booksInfo.organizationName}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
</body>
</html>

