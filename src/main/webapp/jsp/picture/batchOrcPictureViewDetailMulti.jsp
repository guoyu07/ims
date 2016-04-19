<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String pictureId = request.getParameter("pictureId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file = "../inc/header.jsp" %>
	<!-- Imported scripts on this page -->
	<script src="<%=basePath %>assets/js/jquery-ui/jquery-ui.min.js"></script>
	<script src="<%=basePath %>assets/js/selectboxit/jquery.selectBoxIt.min.js"></script>

	<style type="text/css">
		/*.photo-frame{max-height:60px;}*/
		#photo{width:100%;max-width:100%; cursor: pointer;}
		.check-history li{margin:0 12px;padding:0;}
		.check-history .check-time{width:40%;float:left;text-align:left;height:24px;line-height:24px;}
		.check-history .checkor{width:30%;float:left;text-align:center;height:24px;line-height:24px;}
		.check-history .check-status{width:30%;float:left;text-align:right;height:24px;line-height:24px;}
		.check-history li .clear{clear:both;}
		#body_ocr img,#body_ocr table,#body_ocr div{max-width:100%;}
		#answer_ocr img,#answer_ocr table,#answer_ocr div{max-width:100%;}
		#analysis_ocr img,#analysis_ocr table,#analysis_ocr div{max-width:100%;}
		#body_input img,#body_input table,#body_input div{max-width:100%;}
		#answer_input img,#answer_input table,#answer_input div{max-width:100%;}
		#analysis_input img,#analysis_input table,#analysis_input div{max-width:100%;}
		#recoAddInfo {width:100%;}

		/*jyeoo*/
		.pt0{ padding:2px 0 5px 0; font-size:14px; font-weight:700; }
		.pt2{ margin:0; padding:10px 5px; }
		.pt3, .pt4, .pt5, .pt6, .pt7{ clear:both; overflow:hidden; zoom:1; position:relative; }
		.pt3{ margin-top:10px; }
		.pt8 a:link, .pt8 a:visited{ margin-right:10px; padding:2px 5px; }
		.pt8 a:hover{ background:#fc0; }
		.pt9{ text-align:right; border:0 none; }
		.pt1{ overflow:hidden; zoom:1; clear:both; line-height:25px; }
		.pt1 img{ position:relative; }
		.MathXxb{ border:0 none; direction:ltr; line-height:normal; display:inline-block; float:none; font-size:15px; font-style:normal; font-weight:normal; letter-spacing:1px; margin:0; padding:0; text-align:left; text-indent:0; text-transform:none; white-space:nowrap; word-spacing:normal; word-wrap:normal; -webkit-text-size-adjust:none; }
		.MathXxb div, .MathJye span{ border:0 none; margin:0; padding:0; line-height:normal; text-align:left; height:auto; _height:auto; white-space:normal; }
		.MathXxb table{ border-collapse:collapse; margin:0; padding:0; text-align:center; vertical-align:middle; line-height:normal; font-size:inherit; *font-size:100%; _font-size:100%; font-style:normal; font-weight:normal; border:0; float:none; display:inline-block; *display:inline; zoom:0; }
		.MathXxb table td{ padding:0; font-size:inherit; line-height:normal; white-space:nowrap; border:0 none; width:auto; _height:auto; }
		.MathXxb_mi{ font-style:italic; }
		/*ks5u*/
		.deText{ word-break:break-all; }
		/*manfen5*/
		.xxb_manfen{ word-break:break-all; }
		/*mofangge*/
		.ke-zeroborder{ width:100%; }
		table[width='650']{ width:auto; }
	</style>
</head>
<body class="page-body" style="margin:0;">
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
				<li class="active">
					<a href="<%=basePath %>batchpicture/orcPictureViewSearch">
						<i class="fa-file-image-o"></i>
						<span class="title">识别结果</span>
					</a>
				</li>
                <li>
                    <a href="<%=basePath %>batchpicture/orcPictureViewResult">
                        <i class="fa-file-image-o"></i>
                        <span class="title">识别率统计</span>
                    </a>
                </li> 				
			</ul>
		</div>
	</div> --%>
	<div class="main-content">
		<!-- User Info, Notifications and Menu Bar -->
		<%-- <%@ include file = "../inc/nav.jsp" %> --%>
		<%-- <div class="page-title">
			<div class="title-env">
				<h1 class="title">对比详情</h1>
			</div>
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="<%=basePath %>index.jsp"><i class="fa-home"></i>首页</a>
					</li>
					<li>
						<a href="<%=basePath %>batchpicture/orcPictureViewSearch">识别结果</a>
					</li>
					<li>
						<a href="<%=basePath %>batchpicture/viewOrcPictureById?pictureId=<%=pictureId%>">识别详情</a>
					</li>
					<li>
						<a>对比详情</a>
					</li>
				</ol>
			</div>
		</div> --%>
		<!-- Responsive Table -->
			<div class="tabs-vertical-env app-contrast-tabs">
				<ul class="nav tabs-vertical">
					<c:forEach var="orcPicture" items="${ orcPictureList }" varStatus="status">
					<li <c:if test="${status.index==0}">class="active"</c:if>>
						<a href="#v-tab-${orcPicture.target}" data-toggle="tab">${orcPicture.targetStr}</a>
					</li>
					</c:forEach>
				</ul>
				<div class="tab-content">
					<c:forEach var="orcPicture" items="${ orcPictureList }" varStatus="status">
					<div class="tab-pane <c:if test="${status.index==0}">active</c:if>" id="v-tab-${orcPicture.target}">
						<div class="row">
							<div class="col-md-4">
								<div class="panel panel-default">
									<div class="panel-heading">
										<div class="pull-left">图片id:<span id="pic_id">${ orcPicture.id }</span></div>
										<div class="pull-right recent-meta"><span id="creat_time">${ func:formatDate(orcPicture.createTime) }</span></div>
									</div>

									<div class="panel-body">
										<div class="photo-frame"><img id="photo" src=${ orcPicture.orcPictureUrl} ></div>
									</div>
								</div>
							</div>
							<div class="col-md-8">
								<div class="panel panel-default">
									<div class="panel-heading">
										<div class="pull-left">机器识别结果</div>
									<!-- 	<div class="pull-right recent-meta"><span id="rec_time">2015-03-30 18:38:10</span> by <span id="author">Robert</span></div>  -->
									</div>

									<div class="panel-body">
										<c:if test="${orcPicture.target==0}">
										<ul class="nav nav-tabs">
											<c:if test="${fn:length(orcPictureRecolist)!=0}">
												<li class="active">
													<a href="#tab_0" data-toggle="tab">
														<span class="visible-xs"><i class="fa-home"></i></span>
														<span class="hidden-xs">机器识别结果1</span>
													</a>
												</li>
											</c:if>
											<c:forEach var="picture" items="${ orcPictureRecolist }" varStatus="statusRec">
												<li>
													<a href="#tab_${statusRec.index+1}" data-toggle="tab">
														<span class="visible-xs"><i class="fa-user"></i></span>
														<span class="hidden-xs">机器识别结果${statusRec.index+2}</span>
													</a>
												</li>
											</c:forEach>
										</ul>
										</c:if>
										<div class="tab-content">
											<div class="tab-pane active" id="tab_0">
												<div class="padd">
													<ul class="recent">
														<c:if test="${orcPicture.target==0}">
															<li>
																<div class="recent-content" style="font-size:20px;font-weight:bold">
																	<div class="recent-meta">Raw Text</div>
																	<div id="tags_input_2">${ orcPicture.rawText }</div>
																	<div class="clearfix"></div>
																</div>
															</li>
														</c:if>
														<li>
															<div class="recent-content">
																<div class="recent-meta">问题描述</div>
																<div id="body_input">${ orcPicture.content }
																</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">题目解答</div>
																<div id="answer_input">${ orcPicture.answer }
																</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">解题思路</div>
																<div id="analysis_input">${ orcPicture.solution }
																</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">知识点</div>
																<div id="tags_input">${ orcPicture.knowledge }</div>
																<div class="clearfix"></div>
															</div>
														</li>
													</ul>
												</div>
											</div>
											<c:if test="${orcPicture.target==0}">
											<c:forEach var="pictureRec" items="${ orcPictureRecolist }" varStatus="statusRec">
											<div class="tab-pane" id="tab_${statusRec.index+1}">
												<div class="padd">
													<ul class="recent">
														<li>
															<div class="recent-content" style="font-size:20px;font-weight:bold">
																<div class="recent-meta">Raw Text</div>
																<div>${ pictureRec.rawText }</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">问题描述</div>
																<div>${ pictureRec.content }</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">题目解答</div>
																<div>${ pictureRec.answer }</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">解题思路</div>
																<div>${ pictureRec.solution }</div>
																<div class="clearfix"></div>
															</div>
														</li>
														<li>
															<div class="recent-content">
																<div class="recent-meta">知识点</div>
																<div>${ pictureRec.knowledge }</div>
																<div class="clearfix"></div>
															</div>
														</li>
													</ul>
												</div>
											</div>
											</c:forEach>
											</c:if>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		<%-- <%@ include file = "../inc/footer.jsp" %> --%>
	</div>
</div>

<!-- Modal 1 (Basic)-->
<div class="modal fade" id="modal-1">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">消息</h4>
			</div>

			<div class="modal-body">
				程序猿GG正在攻关中，敬请期待！
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-info" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
</body>
</html>
