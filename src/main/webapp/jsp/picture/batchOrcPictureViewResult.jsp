<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.OrcPictureCheckTarget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file = "../inc/header.jsp" %>
	<!-- Imported styles on this page -->
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css"/>
	<!-- Imported scripts on this page -->
	<script type="text/javascript" src="<%=basePath %>assets/js/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>assets/js/selectboxit/jquery.selectBoxIt.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/bootstrap/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/bootstrap/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath %>assets/js/wookmark/wookmark.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>assets/js/imagesloaded/imagesloaded.pkgd.min.js"></script>

	<!-- JavaScripts initializations and stuff -->
	<script src="<%=basePath %>assets/js/xenon-custom.js"></script>

	<script type="text/javascript">
		var basePath = '<%=basePath%>';
		var userKey = '${ userKey }';
		var searchMonth = '${ searchMonth }';
		var target = '${ target }';
		var resultCountList = '${resultCountList}';
	</script>
	<style type="text/css">
		.ui-datepicker-calendar { 
			display: none; 
		} 	
	</style>
</head>
<body class="page-body">
<div class="page-container"><!-- add class "sidebar-collapsed" to close sidebar by default, "chat-visible" to make chat appear always -->
	<!-- Add "fixed" class to make the sidebar fixed always to the browser viewport. -->
	<!-- Adding class "toggle-others" will keep only one menu item open at a time. -->
	<!-- Adding class "collapsed" collapse sidebar root elements and show only icons. -->
	<%-- <div class="sidebar-menu toggle-others fixed">
		<div class="sidebar-menu-inner">
			<header class="logo-env">
				<!-- logo -->
				<div class="logo">
					<span class="logo-expanded"><img src="<%=basePath %>assets/images/logo.png"><span>学习宝识题自动化测试平台</span></span>
					<span class="logo-collapsed"><img src="<%=basePath %>assets/images/logo.png"></span>
				</div>
				<!-- This will toggle the mobile menu and will be visible only on mobile devices -->
				<div class="mobile-menu-toggle visible-xs">
					<a href="#" data-toggle="mobile-menu">
						<i class="fa-bars"></i>
					</a>
				</div>
			</header>
			<ul id="main-menu" class="main-menu">
				<!-- add class "multiple-expanded" to allow multiple submenus to open -->
				<!-- class "auto-inherit-active-class" will automatically add "active" class for parent elements who are marked already with class "active" -->
				<li>
					<a href="<%=basePath %>">
						<i class="fa-upload"></i>
						<span class="title">图片上传</span>
					</a>
				</li>
				<li>
					<a href="<%=basePath %>picture/orcPictureViewSearch">
						<i class="fa-file-image-o"></i>
						<span class="title">识别结果</span>
					</a>
				</li>
                <li class="active">
                    <a href="<%=basePath %>picture/orcPictureViewResult">
                        <i class="fa-file-image-o"></i>
                        <span class="title">识别率统计</span>
                    </a>
                </li> 				
			</ul>
		</div>
	</div> --%>
	<div class="main-content">
		<%-- <%@ include file = "../inc/nav.jsp" %> --%>
		<%-- <div class="page-title">
			<div class="title-env">
				<h1 class="title">识别率统计</h1>
			</div>
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="<%=basePath %>index.jsp"><i class="fa-home"></i>首页</a>
					</li>
					<li>
						<a>识别率统计</a>
					</li>
				</ol>
			</div>
		</div> --%>
		<!-- Responsive Table -->
		<div class="row">
			<div class="col-md-12">
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">统计对象</h3>
						<div class="panel-options">
							<a href="#" data-toggle="panel">
								<span class="collapse-icon">&ndash;</span>
								<span class="expand-icon">+</span>
							</a>
						</div>
					</div>
					<div class="panel-body">
						<form id="search_form" role="form" class="form-inline search-reslut">
							<div class="col-sm-12">
								<div class="form-group">
									<label class="control-label">测试对象：</label>
										<script type="text/javascript">
											$(function($) {
												$("#target").selectBoxIt().on('open', function() {
													// Adding Custom Scrollbar
													$(this).data('selectBoxSelectBoxIt').list.perfectScrollbar();
												});
											});
										</script>
									<select class="form-control" name="target" id="target">
										<c:set var="enumTargetSubjects" value="<%= OrcPictureCheckTarget.values() %>"/>
										<c:forEach var="enumTargetSubject" items="${ enumTargetSubjects }">
											<option value="${ enumTargetSubject.id }" <c:if test="${ enumTargetSubject.id == target }">selected = "selected"</c:if>>${ enumTargetSubject.desc }</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">统计月份：</label>
									<input class="form-control" type="text" value="${searchMonth}" id="search-month"  name="searchMonth" size="10">
								</div>
								<div class="form-group">
									<button class="btn btn-secondary btn-icon btn-icon-standalone">
										<i class="fa-search"></i>
										<span>统计</span>
									</button>
								</div>							
							</div>
						</form>
					</div>
				
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">统计结果</h3>
						<div class="panel-options">
							<a href="#" data-toggle="panel">
								<span class="collapse-icon">&ndash;</span>
								<span class="expand-icon">+</span>
							</a>
						</div>
					</div>
					<div class="panel-body">
						<div id="tableLayout" class="col-md-12 result-table-wrapper" style="">
							<div class="table-responsive">
								<table id="resultListTable" cellspacing="0" class="table table-small-font table-bordered table-striped">
									<thead>
									<tr>
										<th>日期</th>
										<th>总件数</th>
										<th>未处理件数</th>
										<th>正确件数</th>
										<th>错误件数</th>
										<th>正确率(%)</th>
									</tr>
									</thead>
									<tbody>
										<c:forEach var="picture" items="${ resultCountList }">
											<tr>
												<td>${ picture.create_ymd}</td>
												<td>${ picture.total_count }</td>
												<td>${ picture.unjudged_count }</td>
												<td>${ picture.success_count }</td>
												<td>${ picture.failure_count }</td>
												<td>${ picture.success_rate}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- <%@ include file = "../inc/footer.jsp" %> --%>
	</div>
</div>
</body>
</html>

