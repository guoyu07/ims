<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumRole" %>
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
<title>小组列表</title>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
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
<c:set var="preUrl" value="tikuTeamInfoSearch
							?teamId=${ teamId }
							&captain=${ captain }
							&role=${ role }&" />
	<div class="crumb">当前位置：小组管理</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>小组
					<select name="teamId"  id="team-id"></select>
				</li>
				<li>角色
					<select name="role">
						<option value="">全部</option>
						<c:set var="enumRoles" value="<%= EnumRole.values() %>"/>
						<c:forEach var="enumRole" items="${ enumRoles }">
						<option value="${ enumRole.id }" <c:if test="${ enumRole.id == role }">selected = "selected"</c:if>>${ enumRole.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>组长<input type="text" value="${ captain }" name="captain" /></li>  
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
				<li>
					<a class="btn btn-primary btn-xs"  href="<%=basePath %>tranops/tikuTeamExport2Excel">导出</a>
				</li>
				<li>
					<a href="#" class="btn btn-success btn-xs ml10" id="add-team-btn">创建小组</a>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr class="info">
					<th style="min-width:40px">小组名称</th>
					<th style="min-width:40px">组长</th>
					<th style="min-width:40px">角色</th>
					<th style="min-width:30px">操作</th>
				</tr>
			</thead>
			<tbody id="team-list">
				<c:forEach var="team" items="${ tikuTeamList }">
				<tr data-team-name="${ team.name }" data-team-id="${ team.id }" data-captain="${ team.captain }" data-captain-name="${ team.captainName }">
					<td>${ team.name }</td>
					<td title=${ team.captain}>${ team.userInfo.userName }</td>
					<td title=${ team.captain}>${ team.userInfo.role }</td>
					<td>
						<button class="btn btn-primary btn-xs ml10 edit-btn">编辑</button>
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
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
						<span class="sr-only">Close</span>
					</button>
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
				var teamList;
				userInfo.initTeam('team-id');
				$('#add-team-btn').on('click', function(e) {
					userInfo.addTeam($(this).attr('id'));
				});
				teamList = $('#team-list');
				// 编辑信息
				teamList.on('click', '.edit-btn', function(e) {
					var el = $(this), data;
					data = el.closest('tr').data();
					userInfo.editTeam(data);
				});
			});
		}
	});
</script>
</body>
</html>
