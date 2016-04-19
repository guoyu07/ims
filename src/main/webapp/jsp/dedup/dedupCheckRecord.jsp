<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.DedupParentCheckStatus" %>
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
<title>抽检去重题目列表</title>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()});}}})();
</script>
</head>
<body>
	<div class="crumb">当前位置：抽检去重题目列表</div>
	<div class="static2_base">
		<form>
		<ul class="searchSub">
			<li>检查状态
				<select name="status">
					<option value="">全部</option>
					<c:set var="enumSubjects" value="<%= DedupParentCheckStatus.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
					<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == status }">selected = "selected"</c:if>>${ enumSubject.desc }</option>
					</c:forEach>
				</select>
			</li>
			<li>账号<input type="text" value="${ userKey }" name="userKey" /></li>
			<li>
				<button class="btn btn-primary btn-xs">搜索</button>
			</li>
			<li>
				<a href="#" class="btn btn-success btn-xs ml10" id="generate-sample-btn" data-toggle="modal"  data-target=".modal">生成抽检</a>
			</li>
		</ul>
		</form>
		<table class="table table-hover table-bordered" id="memberListTab">
			<thead>
				<tr class="info">
					<th style="min-width:40px">id</th>
					<th style="min-width:40px">500题去重完成时间</th>
					<th style="min-width:20px">去重人账号</th>
					<th style="min-width:20px">检查状态</th>
					<th style="min-width:20px">抽检数目</th>
					<th style="min-width:20px">正确数目</th>
					<th style="min-width:20px">错误数目</th>
					<th style="min-width:20px">合格率</th>
					<th style="min-width:20px">操作</th>	
				</tr>
			</thead>
			<tbody id="sample-list">
				<c:forEach var="record" items="${ checkRecordList }">
				<tr><td><a href="<%=basePath %>dedup/dedupDetailViewSearch?parentId=${ record.id }">${ record.id }</a></td>
					<td class="record-name">${ record.finshTime }</td>
					<td class="record-userKey">${ record.userKey }</td>
					<td class="record-status">${ record.statusStr }</td>
					<td class="record-num">${ record.num }</td>
					<td class="record-passNum">${ record.passNum }</td>
					<td class="record-unpassNum">${ record.unpassNum }</td>
					<td class="record-passRatio">${ record.ratioStr }</td>
					<td>
					<c:if test="${ record.status != 2  }">
					<a class="btn btn-primary btn-xs btn-back"  data-block="${ record.teamId }">回滚</a>
					</c:if></td>	
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
			seajs.use(['lib/jquery', 'module/Dialog'], function($, dialog) {
				// 绑定生成抽检
				$("#generate-sample-btn").on('click', function(e) {
					var el = $(this);
					e.preventDefault();
					dialog.init('modal-dialog', {
						sizeClass: 'modal-sm',
						title: '生成抽检',
						content: [
							'<p>',
								'<span style="display:inline-block;text-align:right;width:110px">每包抽查：</span>',
								'<input type="text" value="" id="record-length"  maxLength="30"/>',
							'</p>'
						].join(''),
						source: el.attr('id'),
						initCall: function() {
							var Self = this;
							Self._body.find('.btn-confirm').text('生成抽检');
						},
						confirm: function() {
							var Self = this, num;
							num = $('#record-length').val().trim();
							if(/\d+/.test(num)) {
								$.ajax({
									url: window.basePath + 'dedup/generateCheckRecord',
									type: "GET",
									data: {
										num: num
									},
									dataType:"json",
									success: function(obj) {
										if(obj.status === 0){
											window.location.reload();
										} else {
											Self.enableConfirm();
											alert(obj.msg);
										}
									},
									error: function() {
										Self.enableConfirm();
										alert("网络错误，请稍后重试");
									}
								});
							} else {
								Self.enableConfirm();
								alert('请输入合法的长度');
							}
						}
					});
				});
				$('#sample-list').on('click', '.btn-back', function(e) {
					var el = $(this), blockId = el.data('block');
					if(!confirm("确认要回滚吗？")){
						return;
					}
					$.ajax({
						url: window.basePath + 'mark/cancelBlock',
						type: 'GET',
						data: {
							block: blockId
						},
						dataType: 'json',
						success: function(data) {
							var curRow;
							if(data.status === 0) {
								curRow = el.closest('tr');
								curRow.find('.record-status').text('已回滚');
								el.remove();
							} else {
								alert(data.msg);
							}
						}
					});
				});
			});
		}
	});
</script>
</body>
</html>
