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
<title>用户管理</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="userTeamInfoList
							?status=${ status }
							&userKey=${ userKey }&" />
	<div class="crumb">当前位置：用户管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>账号状态
					<select name="status">
						<option value="">全部</option>
						<c:set var="enumTiKuPersonStatus" value="<%= EnumTiKuPersonStatus.values() %>"/>
						<c:forEach var="enumTiKuPersonStatu" items="${ enumTiKuPersonStatus }">
						<option value="${ enumTiKuPersonStatu.id }" <c:if test="${ enumTiKuPersonStatu.id == status }">selected = "selected"</c:if>>${ enumTiKuPersonStatu.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>账号<input type="text" value="${ userInfo.userKey }" name="userKey"/></li>
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
				<li>
					<a href="#" class="btn btn-success btn-xs ml10" id="add-member-btn">创建用户</a>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed" id="memberListTab">
			<thead>
				<tr class="info">
					<th style="min-width:50px">小组</th>
					<th style="min-width:50px">角色组</th>
					<th style="min-width:90px">账号</th>
					<th style="min-width:90px">姓名</th>
					<th style="min-width:40px">手机号</th>
					<th style="min-width:40px">身份</th>
					<th style="min-width:90px">账号状态</th>
					<th style="min-width:90px">操作</th>
				</tr>
			</thead>
			<tbody id="user-list">
				<c:forEach var="userInfo" items="${ userInfoList }">
					<tr data-user-id="${ userInfo.id }" data-user-key="${ userInfo.userKey }" data-user-name="${ userInfo.userName }" data-role="${ userInfo.role }" data-team-id="${ userInfo.teamId }" data-user-mobile="${ userInfo.userMobile }" data-status="${ userInfo.status }" data-team-role="${ userInfo.groupname }">
						<td>${ userInfo.tikuTeam.name}</td>
				 		<td>${ userInfo.groupname }</td>
						<td>${ userInfo.userKey }</td>
						<td>${ userInfo.userName }</td>
						<td>${ userInfo.userMobile } </td>
						<td>${ userInfo.role }</td>
						<td>${ userInfo.statusForShow }</td>
						<td>
						<c:if test="${ userInfo.status == 0}">
							<button class="btn btn-primary btn-xs ml10 edit-btn">编辑</button> 
						</c:if>
						</td>
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
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery', 'module/UserInfo'], function($, userInfo) {
				// 加载小组列表
				var userList;
				// 创建用户
				$('#add-member-btn').on('click', function(e) {
					var el = $(this);
					e.preventDefault();
					userInfo.addMemberIncomplete(el.attr('id'));
				});
				userList = $('#user-list');
				// 编辑信息
				userList.on('click', '.edit-btn', function(e) {
					var el = $(this), data;
					data = el.closest('tr').data();
					userInfo.editMemberIncomplete(data);
				});
			});
		}
	});
</script>
</body>
</html>

