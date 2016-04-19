<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.OrcPictureCheckTarget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String displayMode = request.getParameter("displayMode");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file = "../inc/header.jsp" %>
	<!-- Imported styles on this page -->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap/bootstrap-datetimepicker.min.css"/>
	<!-- Imported scripts on this page -->
	<script type="text/javascript" src="<%=basePath%>assets/js/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>assets/js/selectboxit/jquery.selectBoxIt.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/bootstrap/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/bootstrap/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>assets/js/wookmark/wookmark.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>assets/js/imagesloaded/imagesloaded.pkgd.min.js"></script>

	<!-- JavaScripts initializations and stuff -->
	<script src="<%=basePath%>assets/js/xenon-custom.js"></script>

	<script type="text/javascript">
		var basePath = '<%=basePath%>';
		var pictureId = '${ pictureId }';
		var status = '${ status }';
		var userKey = '${ userKey }';
		var startTime = '${ startTime }';
		var endTime = '${ endTime }';
		var batchId = '${ batchId }';
		var target = '${ target }';
		var search_query = location.href.replace(/[a-zA-z]+:\/\/[^\s]*orcPictureViewSearch\??/,"");

		var currentPage = ${ page };
		var totalpage = ${ totalpage };

		var displayModeVal = <%=displayMode%>; //0：瀑布流，1：列表

		if(search_query.length==0) {
			//search_query = '&status=' + status + '&userKey=' + userKey + '&startTime=' + startTime + '&endTime=' + endTime;
			search_query = '&batchId=&status=&userKey=&startTime=&endTime=';
		} else {
			//search_query = search_query.replace(/\?batchId=[0-9]+/,"");
			//search_query = search_query.replace(/\?batchId=/,"");
			search_query = search_query.replace(/\?page=[0-9]+/,"");
			search_query = search_query.replace(/\&page=/,"");
			//search_query = search_query.replace(/\&batchId=[0-9]+/,"");
			//search_query = search_query.replace(/\&batchId=/,"");
		}
		var urlPre = 'orcPictureViewSearch?';
		function goUrl(targetPageNum) {
			var url = urlPre+'page='+targetPageNum;
			url = url + "&batchId=" + batchId + "&status=" + status + "&target=" + target
			+ "&userKey=" + userKey + "&startTime=" + startTime
			+ "&endTime=" + endTime+'&displayMode='+displayModeVal;
			location.href = url;
		}
		$(function(){

			getComputeRecPercent();

			$("#batchCompute").click(getComputeRecPercent);
				//var resultstring=computeRecPercent($("#target").val(),$("#batchId").val(),$("#status").val(),$("#userKey").val(),$("#startTime").val(),$("#endTime").val());
				//$("#computeResult").val(resultstring);
				//	location.href = "<%=basePath%>batchpicture/computeRecPercent?" 	+ "&target=" + $("#target").val()
				//															   	+ "&batchId=" + $("#batchId").val()
				//															   	+ "&status=" + $("#status").val()
				//															   	+ "&userKey=" + $("#userKey").val()
				//															   	+ "&startTime=" + $("#startTime").val()
				//															   	+ "&endTime=" + $("#endTime").val();

			$("#resultListTable").on("click",".batch-resul-btn",function(){
				var batchId = $(this).data("batchid");
				console.log(batchId);
				$(this).text("识别中...");
				getBatchResult(batchId, {
					displayMode: window.displayModeVal
				});
			});
			
			$("#exportFile").click(function(){
				location.href = "<%=basePath%>batchpicture/downloadFile?" 	+ "&target=" + $("#target").val()
																		   	+ "&pictureId=" + $("#pictureId").val()
																		   	+ "&status=" + $("#status").val()
																		   	+ "&fileName=" + $("#fileName").val()
																		   	+ "&startTime=" + $("#startTime").val()
																		   	+ "&endTime=" + $("#endTime").val();
			});
		});

		function getComputeRecPercent(){
			$.ajax({
				url:'/tiku_ops/batchpicture/computeRecPercent?' 	+ "&target=" + $("#target").val() + "&startTime=" + $("#startTime").val() + "&endTime=" + $("#endTime").val(),
				dataType:'JSON',
				type:'GET',
				success:function(data){
					var elem = '<div class="percent-box" ">'+data.result.percent+'</div>'
					$("#computeResult").html(elem+data.result.msg)
				}
			});
		}
	</script>
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
					<a href="<%=basePath%>picture/orcPictureViewSearch">
						<i class="fa-file-image-o"></i>
						<span class="title">识别结果</span>
					</a>
				</li>
                <li>
                    <a href="<%=basePath%>picture/orcPictureViewResult">
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
				<h1 class="title">识别结果</h1>
			</div>
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="<%=basePath%>index.jsp"><i class="fa-home"></i>首页</a>
					</li>
					<li>
						<a>识别结果</a>
					</li>
				</ol>
			</div>
		</div> --%>
		<!-- Responsive Table -->
		<div class="row">
			<div class="col-md-12">
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">搜索条件</h3>
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
											jQuery(document).ready(function($)
											{
												$("#target").selectBoxIt().on('open', function()
												{
													// Adding Custom Scrollbar
													$(this).data('selectBoxSelectBoxIt').list.perfectScrollbar();
												});
											});
										</script>
									<select class="form-control" name="target" id="target">
										<option value="">全部</option>
										<c:set var="enumTargetSubjects" value="<%=OrcPictureCheckTarget.values()%>"/>
										<c:forEach var="enumTargetSubject" items="${ enumTargetSubjects }">
											<option value="${ enumTargetSubject.id }" <c:if test="${ enumTargetSubject.id == target }">selected = "selected"</c:if>>${ enumTargetSubject.desc }</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">提交时间：</label>
									<input class="form-control form-date" type="text" value="${startTime}" id="startTime"  name="startTime" size="20" data-date-format="yyyy-mm-dd 00:00:00">
									<em class="to">~</em>
									<input class="form-control form-date" type="text" value="${endTime}" id="endTime" name="endTime" size="20" data-date-format="yyyy-mm-dd 23:59:59">
								</div>
								<div class="form-group">
									<label class="control-label">识别结果：</label>
									<script type="text/javascript">
										jQuery(document).ready(function($)
										{
											$("#status").selectBoxIt().on('open', function()
											{
												// Adding Custom Scrollbar
												$(this).data('selectBoxSelectBoxIt').list.perfectScrollbar();
											});
										});
									</script>
									<select name="status" id="status" class="form-control">
										<option value="">全部</option>
										<c:set var="enumSubjects" value="<%=BatchOrcPictureCheckStatus.values()%>"/>
										<c:forEach var="enumSubject" items="${ enumSubjects }">
											<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == status }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">文件名：</label>
									<input class="form-control" type="text" value="${fileName}" id="fileName"  name="fileName" size="10">
								</div>
								<div class="form-group">
									<label class="control-label">ID：</label>
									<input class="form-control" type="text" value="${pictureId}" id="pictureId"  name="pictureId" size="10">
								</div>
								<input type="hidden" name="displayMode">
								<div class="form-group">
									<button class="btn btn-secondary btn-icon btn-icon-standalone" type="submit">
										<i class="fa-search"></i>
										<span>查询</span>
									</button>
								</div>
								<div class="form-group">
									<button class="btn btn-secondary btn-icon btn-icon-standalone" id="exportFile" type="button">
										<i class="fa-file-o"></i>
										<span>导出文件</span>
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
						<h3 class="panel-title">识别结果</h3>
						<div class="panel-options">
							<a href="#" data-toggle="panel">
								<span class="collapse-icon">&ndash;</span>
								<span class="expand-icon">+</span>
							</a>
						</div>
					</div>
					<div class="panel-body">
						<div>
							<i class="fa-question-circle" style="font-size:20px; margin:0 10px; cursor: pointer;" title=".统计对象=根据检索条件中的【测试对象】，【提交时间】产生&#10;.识别准确率=识别正确/(识别正确+识别错误)"></i>识题正确率统计：<button class="btn btn-secondary btn-icon btn-icon-standalone" id="batchCompute"><i class="fa-bar-chart"></i><span>刷新统计</span></button>
							<button class="btn btn-blue btn-icon btn-icon-standalone pull-right" id="changeLayout"><i class="fa-align-justify"></i><span>切换布局</span></button>
						</div>
						<p id="computeResult" style="font-size:18px; margin:20px 15px; height: 125px;"></p>
						<%--<div class="clearfix">
							<h5 class="col-md-12">识题总进度（100/${ totalNum }题）</h5>
							<div class="col-md-12 result-progress-wrapper">
								<div class="progress progress-striped active ">
									<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 20%">
										<span class="sr-only">20% Complete (success)</span>
									</div>
								</div>
							</div>
						</div>--%>					
						<div id="tableLayout" class="col-md-12 result-table-wrapper" style="display: none;">
							<div class="table-responsive">
								<table id="resultListTable" cellspacing="0" class="table table-small-font table-bordered table-striped">
									<thead>
									<tr>
										<th>ID</th>
										<th>测试应用</th>
										<th>文件名</th>
										<th>提交人</th>
										<th>提交时间</th>
										<th>识别结果</th>
										<th>操作</th>
									</tr>
									</thead>
									<tbody>
										<c:forEach var="picture" items="${ orcPictureList }">
											<tr>
												<td><a class="text-blue" href="javascript:;" onclick="toDetail(${ picture.id });">${ picture.id }</a></td>
												<td>${ picture.targetStr}</td>
												<td>${ picture.originalFileName }</td>
												<td>${ picture.userKey }</td>
												<td>${ func:formatDate(picture.createTime) }</td>
												<td>${ picture.statusStr}</td>
												<td width="10%">
													<c:if test="${ 9 == picture.status }">
														<button data-batchid="${ picture.batchId }" class="btn btn-success btn-xs batch-resul-btn">识别</button>
													</c:if>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div class="text-right">
								<%@ include file = "../inc/page.jsp" %>
							</div>
						</div>
						<div id="waterfullLayout" class="col-md-12 waterfall-wrapper">
							<ul id="waterfallContainer" class="waterfall">
								<c:forEach var="picture" items="${ orcPictureList }">
									<li class="tile-loading">
										<div class="photo"><img src="${ picture.orcPictureUrl }" onclick="toDetail(${ picture.id });"></div>
										<div class="info">
											<span>${ picture.id }</span>
											<c:choose>
												<c:when test="${ 1 == picture.status }"><span class="pull-right text-success">${ picture.statusStr}</span></c:when>
												<c:otherwise><span class="pull-right text-danger">${ picture.statusStr}</span></c:otherwise>
											</c:choose>
										</div>
										<div class="info">${ picture.targetStr}<span class="pull-right time">${ func:formatDate(picture.createTime) }</span></div>
									</li>
								</c:forEach>
							</ul>
						</div>
						<script>

						</script>
					</div>
				</div>
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
<script type="text/javascript" src="<%=basePath %>js/global/getBatchResult.js"></script>
<script type="text/javascript">
	$(function(){
		$(".form-date").datetimepicker({
			language: 'zh-CN',/*加载日历语言包，可自定义*/
			weekStart: 1,
			todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			minView: 2,
			showMeridian: 1
		});

		if(displayModeVal==1){
			$('#waterfullLayout').hide();
			$('#tableLayout').show();
			$('#tableLayout').find('i').removeClass('fa-align-justify').addClass('fa-th');
			$('input[name="displayMode"]').val(1);
		}else{
			$('#waterfullLayout').show();
			$('#tableLayout').hide();
			$('#tableLayout').find('i').removeClass('fa-th').addClass('fa-align-justify');
			$('input[name="displayMode"]').val(0);
		}

		$('#changeLayout').click(function(){
			if($('#tableLayout').is(':hidden')){
				$('#waterfullLayout').fadeOut();
				$('#tableLayout').fadeIn();
				$(this).find('i').removeClass('fa-align-justify').addClass('fa-th');
				displayModeVal = 1;
				$('input[name="displayMode"]').val(1);
			}else{
				$('#waterfullLayout').fadeIn();
				$('#tableLayout').fadeOut();
				$(this).find('i').removeClass('fa-th').addClass('fa-align-justify');
				displayModeVal = 0;
				$('input[name="displayMode"]').val(0);
			}
			wookmark.updateOptions();
		});
		/**
		 * 瀑布流布局
		 */
		var container = '#waterfallContainer',
				$container = $(container),
				wookmark,
				$window = $(window),
				$document = $(document);
		wookmark = new Wookmark(container, {
			autoResize: true,
			container: $('.waterfall-wrapper'),
			offset: 20
		});
		imgloaded();

		var targetPageNum = 0;
		function onScroll() {
			if(displayModeVal==1) return;
			var winHeight = window.innerHeight ? window.innerHeight : $window.height(), // iPhone fix
					closeToBottom = ($window.scrollTop() + winHeight > $document.height() - 100);

			if (closeToBottom) {
				if(currentPage>0) return;
				if(targetPageNum>=totalpage-1) return;
				targetPageNum ++;
				asynPicViewList(targetPageNum,function(data){
					var listHtml = createPicViewListElem(data.result);
					$container.append(listHtml);
					wookmark.initItems();
					wookmark.layout(true);
					imgloaded();
				});

			}
		}

		function imgloaded(){
			$container.imagesLoaded()
					.progress(function (instance, image) {
						$(image.img).closest('li').removeClass('tile-loading');
						wookmark.updateOptions();
					});
		}

		$window.bind('scroll.wookmark', onScroll);

		$('[ data-toggle="sidebar"]').click(function(){
			wookmark.updateOptions();
		});
	});
	function toDetail(picId) {
		var pic_id=picId;
		search_query = search_query.replace(/\&pictureId=[0-9]*/,"");
		location.href = "<%=basePath %>batchpicture/viewOrcPictureById?pictureId="+pic_id + "&" + search_query;
	}

	function asynPicViewList(targetPageNum,callback){
		var asynUrlPre = 'orcPicViewAsync?';
		var url = asynUrlPre+'page='+targetPageNum;
		url = url + "&batchId=" + batchId + "&status=" + status + "&target=" + target
		+ "&userKey=" + userKey + "&startTime=" + startTime
		+ "&endTime=" + endTime;
		$.ajax({
			type:"get",
			dataType:"json",
			url:url,
			success:function(data){
				console.log(data)
				callback(data)
			},
			error:function(){

			}
		});
	}

	function formatDate(timestamp) {
		var d = new Date(timestamp);
		var year = d.getFullYear();
		var month = d.getMonth() + 1;
		var date = d.getDate();
		var hour = d.getHours();
		var minute = d.getMinutes();
		var second = d.getSeconds();
		if (month < 10) month = '0' + month;
		if (date < 10) date = '0' + date;
		if (hour < 10)hour = '0' + hour;
		if (minute < 10)minute = '0' + minute;
		if (second < 10)second = '0' + second;
		return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
	}

	function createPicViewListElem(result){
		var elem = '';
		for(var i in result){
			var item = result[i];
			elem +='<li class="tile-loading">'+
			'	<div class="photo"><img src="'+item.orcPictureUrl+'" onclick="toDetail('+item.id+');"></div>'+
			'	<div class="info">'+
			'		<span>'+item.id+'</span>'+
			'		<span class="pull-right '+(item.status==1?'text-success':'text-danger')+'">'+item.statusStr+'</span>'+
			'	</div>'+
			'	<div class="info">'+item.targetStr+'<span class="pull-right time">'+formatDate(item.createTime)+'</span></div>'+
			'</li>';
		}
		return elem;
	}
</script>
</body>
</html>

