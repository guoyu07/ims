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
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file = "../inc/header.jsp" %>
	<!-- Imported scripts on this page -->
	<script src="<%=basePath%>assets/js/jquery-ui/jquery-ui.min.js"></script>
	<script src="<%=basePath%>assets/js/selectboxit/jquery.selectBoxIt.min.js"></script>

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
					<span class="logo-expanded"><img src="<%=basePath%>assets/images/logo.png"><span>学习宝识题自动化测试平台</span></span>
					<span class="logo-collapsed"><img src="<%=basePath%>assets/images/logo.png"></span>
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
					<a href="<%=basePath%>">
						<i class="fa-upload"></i>
						<span class="title">图片上传</span>
					</a>
				</li>
				<li class="active">
					<a href="<%=basePath%>batchpicture/orcPictureViewSearch">
						<i class="fa-file-image-o"></i>
						<span class="title">识别结果</span>
					</a>
				</li>
                <li>
                    <a href="<%=basePath%>batchpicture/orcPictureViewResult">
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
				<h1 class="title">识别结果</h1>
			</div>
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="<%=basePath%>index.jsp"><i class="fa-home"></i>首页</a>
					</li>
					<li>
						<a href="<%=basePath%>batchpicture/orcPictureViewSearch">识别结果</a>
					</li>
					<li>
						<a>详细</a>
					</li>
				</ol>
			</div>
		</div> --%>
		<!-- Responsive Table -->
		<div class="row">
			<div class="col-md-7">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="pull-left">图片id:<span>${ orcPicture.id }</span>（${ orcPicture.targetStr }）</div>
						<div class="pull-right recent-meta"><span id="creat_time">${ func:formatDate(orcPicture.createTime) }</span></div>
					</div>

					<div class="panel-body">
						<div class="photo-frame"><img id="photo" src=${ orcPicture.orcPictureUrl} ></div>
						<div class="padd">
							<c:set var="URECO" value="<%=BatchOrcPictureCheckStatus.URECO%>"/>
							<c:set var="USOLVE" value="<%=BatchOrcPictureCheckStatus.USOLVE%>"/>
							<c:set var="SOLVE_RIGHT" value="<%=BatchOrcPictureCheckStatus.SOLVE_RIGHT%>"/>
							<c:set var="ERROR_RECO_FAILURE" value="<%=BatchOrcPictureCheckStatus.ERROR_RECO_FAILURE%>"/>
							<c:set var="ERROR_SEARCH" value="<%=BatchOrcPictureCheckStatus.ERROR_SEARCH%>"/>
							<c:set var="ERROR_RECO_NORESULT" value="<%=BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT%>"/>
							<c:set var="ERROR_PICTUREBAD" value="<%=BatchOrcPictureCheckStatus.ERROR_PICTUREBAD%>"/>
							<c:set var="ERROR_OTHER" value="<%=BatchOrcPictureCheckStatus.ERROR_OTHER%>"/>
							<c:set var="ERROR_UPLOAD_FAILURE" value="<%=BatchOrcPictureCheckStatus.ERROR_UPLOAD_FAILURE%>"/>

							<div  style="text-align: center; margin-top: 0px;weight:100%">
								<ul class="pagination" style="display:block;width:100%;padding:0;margin:0;list-type-style:none;" id="ocr-container">
									<li><button class="btn btn-success" data-status="1" style="width:30%;text-align:center;margin-right:4%;" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>识别正确</button></li>
									<li><button class="btn btn-danger" data-status="2" style="width:30%;text-align:center" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>识别失败</button></li>
									<li><button class="btn btn-danger" data-status="3" style="width:30%;text-align:center;margin-left:4%;" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>搜索失败</button></li>
								</ul>
							</div>

							<div style="margin-top: 5px;">
								<div class="recent-meta" style="margin-bottom:0px;font-weight:bold">识别结果&nbsp;</div>
								<div class="selectboxit-block">
									
									<select name="status" id="recognize-result" style="font-size:15px;width:100%;height:30px;font-weight:bold" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>
		               					<c:set var="enumSubjects" value="<%=BatchOrcPictureCheckStatus.values()%>"/>
					                	<c:forEach var="enumSubject" items="${ enumSubjects }">
					                   		<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == orcPicture.status }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
					                	</c:forEach>
					                </select>
				                </div>
	        				</div>
							<div style="width:100%;margin-top: 5px;">
								<div class="recent-meta" style="margin-bottom:0px;font-weight:bold">备注</div>
								<textarea id="recoAddInfo" name="recoAddInfo" class="form-control" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>${ orcPicture.recoAdditionalInfo }</textarea>
							</div>
							<div  style="text-align: center; margin-top: 15px;">
								<button class="btn btn-primary"  id="ocr-submit"  style="width:48%;text-align:center" <c:if test="${ ERROR_UPLOAD_FAILURE.id == orcPicture.status }">disabled</c:if>>提交</button>
							</div>

						</div>
						<div style="text-align: center; margin-top: 0px;">
							<ul class="pagination">
								<c:set var="searchUrl" value="&batchId=${ batchId }status=${ status }&&userKey=${ userKey }&startTime=${ startTime }&endTime=${ endTime }"/>
								<li>
									<c:if test="${ lastpictureId != null }">
									<a href="<%=basePath%>batchpicture/viewOrcPictureById?pictureId=${ lastpictureId }${ searchUrl }">上一条</a>
									</c:if>
									<c:if test="${ lastpictureId == null }">
									<span>上一条</span>
									</c:if>
								</li>
								<li>
									<c:if test="${ nextpictureId != null }">
									<a href="<%=basePath%>batchpicture/viewOrcPictureById?pictureId=${ nextpictureId }${ searchUrl }">下一条</a>
									</c:if>
									<c:if test="${ nextpictureId == null }">
									<span>下一条</span>
									</c:if>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">竞品对比:</div>
					</div>
					<div class="panel-body select-app" id="rival-container">
						<div class="clearfix">
							<c:if test="${orcPicture.target!=0}">
								<div class="col-md-4">
									<div class="form-block">
										<label>
											<div class="s-logo"><img src="<%=basePath %>assets/images/logo.png"></div>
											<input type="checkbox" name="checkbox-app" class="cbr cbr-secondary" value="0">学习宝
										</label>
									</div>
								</div>
							</c:if>
                            <c:if test="${orcPicture.target!=1}">
							<div class="col-md-4">
								<div class="form-block">
									<label>
										<div class="s-logo"><img src="<%=basePath %>assets/images/logo_xuebajun.png"></div>
										<input type="checkbox" name="checkbox-app" class="cbr cbr-secondary" value="1">学霸君
									</label>
								</div>
							</div>
	                        </c:if>
                            <c:if test="${orcPicture.target!=2}">
							<div class="col-md-4">
								<div class="form-block">
									<label>
										<div class="s-logo"><img src="<%=basePath %>assets/images/logo_xiaoyuansouti.png"></div>
										<input type="checkbox" name="checkbox-app" class="cbr cbr-secondary" value="2" >小猿搜题
									</label>
								</div>
							</div>
	                        </c:if>
                            <c:if test="${orcPicture.target!=3}">
							<div class="col-md-4">
								<div class="form-block">
									<label>
										<div class="s-logo"><img src="<%=basePath %>assets/images/logo_zuoyebang_disabled.png"></div>
										<input type="checkbox" name="checkbox-app" class="cbr cbr-secondary" value="3" disabled>作业帮
									</label>
								</div>
							</div>
	                        </c:if>
						</div>
						<div class="contrast-btn">
							<button id="btn-compare" class="btn btn-primary">开始对比</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-5">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="pull-left">机器识别结果</div>
				<!-- 		<div class="pull-right recent-meta"><span id="rec_time">2015-03-30 18:38:10</span> by <span id="author">Robert</span></div>  -->
					</div>

					<div class="panel-body">
						<ul class="nav nav-tabs">
							<c:if test="${fn:length(orcPictureRecolist)!=0}">
								<li class="active">
									<a href="#tab_0" data-toggle="tab">
										<span class="visible-xs"><i class="fa-home"></i></span>
										<span class="hidden-xs">机器识别结果1</span>
									</a>
								</li>
							</c:if>
							<c:forEach var="picture" items="${ orcPictureRecolist }" varStatus="status">
								<li>
									<a href="#tab_${status.index+1}" data-toggle="tab">
										<span class="visible-xs"><i class="fa-user"></i></span>
										<span class="hidden-xs">机器识别结果${status.index+2}</span>
									</a>
								</li>
							</c:forEach>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_0">
								<div class="padd">
									<ul class="recent">
										<c:if test="${orcPicture.target==0}">
										<li>
											<div class="recent-content" style="font-size:20px;font-weight:bold">
												<div class="recent-meta">Raw Text</div>
												<div>${ orcPicture.rawText }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										</c:if>
										<li>
											<div class="recent-content">
												<div class="recent-meta">问题描述</div>
												<div>${ orcPicture.content }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">题目解答</div>
												<div>${ orcPicture.answer }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">解题思路</div>
												<div>${ orcPicture.solution }</div>
												<div class="clearfix"></div>
											</div>
										</li>
										<li>
											<div class="recent-content">
												<div class="recent-meta">知识点</div>
												<div>${ orcPicture.knowledge }</div>
												<div class="clearfix"></div>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<c:forEach var="picture" items="${ orcPictureRecolist }" varStatus="status">
								<div class="tab-pane" id="tab_${status.index+1}">
									<div class="padd">
										<ul class="recent">
											<li>
												<div class="recent-content" style="font-size:20px;font-weight:bold">
													<div class="recent-meta">Raw Text</div>
													<div>${ picture.rawText }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">问题描述</div>
													<div>${ picture.content }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">题目解答</div>
													<div>${ picture.answer }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">解题思路</div>
													<div>${ picture.solution }</div>
													<div class="clearfix"></div>
												</div>
											</li>
											<li>
												<div class="recent-content">
													<div class="recent-meta">知识点</div>
													<div>${ picture.knowledge }</div>
													<div class="clearfix"></div>
												</div>
											</li>
										</ul>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- <%@ include file = "../inc/footer.jsp" %> --%>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		var rivalContainer = $('#rival-container'), 
			rivals = rivalContainer.find('input[name="checkbox-app"]'),
			pictureId = ${ orcPicture.id },
			basePath = '<%=basePath%>',
			recognizeResult = $('#recognize-result'),
			doCommit;
		recognizeResult.selectBoxIt().on('open', function() {
			$(this).data('selectBoxSelectBoxIt').list.perfectScrollbar();
		});
		rivalContainer.on('click', 'label', function(e) {
			if($(this).find('input:checkbox').prop('disabled')){
				$.dialog.tips('程序猿GG正在攻关中，敬请期待！', 1, false);
			}
		});
		$('#btn-compare').on('click', function(e) {
			var targets = Array.prototype.map.call(rivals.filter(':checked'), function(o) {
				return $(o).val();
			}).join(';')
			document.location.href = basePath + 'batchpicture/batchRecognition?pictureId=' + pictureId + '&targets=' + targets;
		});
		$('#photo').on('click', function(e) {
			window.open($(this).attr('src'), '', 'width=800,scrollbars=yes,status=yes');
		});
		doCommit = function(code) {
			$.ajax({
				url: basePath + 'batchpicture/edit',
				type: 'post',
				data: {
					status: code,
					pictureId: pictureId,
					recoAddInfo: $('#recoAddInfo').val()
				},
				dataType: 'json',
				success: function(data) {
					var nextpictureId = '${ nextpictureId }',
						curUrl = document.location.href;
					if(data.status === 0) {
						if(nextpictureId) {
							document.location.href = curUrl.replace(/([?&]pictureId\=)\d*/, '$1' + nextpictureId);
						} else {
							alert('已经是最后一题');
						}
					} else {
						alert(data.msg);
					}
				}
			});
		};
		$(document).on('keydown', function(e) {
			if(e.keyCode==13 && e.ctrlKey) {
				doCommit(recognizeResult.val());
			}
		});
		$('#ocr-submit').on('click', function() {
			doCommit(recognizeResult.val());
		});
		$('#ocr-container').on('click', '.btn', function(e) {
			doCommit($(this).data('status'));
		});
	});
</script>

</body>
</html>
