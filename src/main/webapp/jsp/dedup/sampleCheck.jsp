<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.xuexibao.ops.enumeration.DedupCheckStatus" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>选择重复题目</title>
	<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>css/merge.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		.mark-repeat {
			margin: 20px 20px 0;
			padding-bottom: 15px;
		}
		.mark-repeat .panel-heading span{
			font-size: 18px;
		}
		.mark-repeat .btn {
			min-width: 100px;
		}
		.mark-repeat .panel-body {
			padding-top: 8px;
			margin-bottom: 15px;
		}
		.mark-repeat table.contrast {
			margin-top: 15px;
			width: 100%
		}
		.mark-repeat th.title {
			height: 35px;
			color: #fff;
			background: #3399ff;
			vertical-align: middle;
			text-align: center;
			font-size: 14px;
		}
		.mark-repeat .contrast .container {
			padding: 0;
		}
		.mark-repeat .similar:nth-child(n+1) {
			margin-top: 13px;
			max-height: 485px;
			overflow-y: auto;
			overflow-x: hidden;
		}
		.mark-repeat ul.similar {
			margin-top: 0;
		}
		.mark-repeat ul.similar>li {
			padding: 8px 15px ;
			border-top: 1px solid #c51114;
		}
		.mark-repeat ul.similar>li:first-child {
			border-top: 0;
		}
		.mark-repeat .contrast tr {
			vertical-align: top;
		}
		.mark-repeat .panel>.contrast>tbody>tr>*:nth-child(n){
			border: 1px solid #c51114;
		}
		.mark-repeat .content {
			word-break: break-all;
		}
		.mark-repeat .btn-container {
			margin-top: -5px;
			float: right;
			display: inline-block;
			padding-right: 35px;
			height: 34px;
			line-height: 34px;
		}
		.mark-repeat .btn-container span {
			font-size: 16px;
		}
		.solution{width:100%; overflow:hidden; background-color:#fff; }
		.solution .solve > h1{
			font-size:16px;
			font-weight:bold;
			color:#0e7aff;
			margin-bottom:10px;
		}
		.solution .solve .detail{ font-size:14px; line-height:24px; }
		.solution .solve .detail img{ max-width:100%; }
		.solution .solve .detail table{ max-width:100%; }
		.solution .content * {
			width: auto !important;
		}
	</style>
	<script type="text/javascript">
		var path = '<%=path %>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
</head>
<body>
	<section class="mark-repeat">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span>去重结果检查</span>
				<c:set var="UCHECK" value="<%= DedupCheckStatus.UCHECK %>"/>
				<c:set var="ELIGIBLE" value="<%= DedupCheckStatus.ELIGIBLE %>"/>
				<c:set var="UNELIGIBLE" value="<%= DedupCheckStatus.UNELIGIBLE %>"/>
				<c:if test="${ result.cstatus == UCHECK.id }">
				<div class="btn-container">
					<button id="confirm" class="btn btn-success">√正确</button>
					<button id="error" class="btn btn-danger" data-toggle="modal" data-target=".modal" style="margin-left:35px">x错误</button>
					<a style="margin-left:35px" href="<%=basePath%>dedup/dedupDetailViewSearch?parentId=${ result.parentId }">&lt;&lt;返回</a>
				</div>
				</c:if>
				<c:if test="${ result.cstatus == ELIGIBLE.id }">
				<div class="btn-container">
					<span class="text-success">√正确</span>
					<a style="margin-left:35px" href="<%=basePath%>dedup/dedupDetailViewSearch?parentId=${ result.parentId }">&lt;&lt;返回</a>
				</div>
				</c:if>
				<c:if test="${ result.cstatus == UNELIGIBLE.id }">
				<div class="btn-container">
					<span class="text-danger">x错误</span>
					<a style="margin-left:35px" href="<%=basePath%>dedup/dedupDetailViewSearch?parentId=${ result.parentId }">&lt;&lt;返回</a>
				</div>
				</c:if> 
			</div>
			<table class="panel-body contrast table-bordered">
				<tr class="row">
					<th class="title col-md-4">最佳题目</th>
					<th class="title col-md-4">重复题目</th>
					<th class="title col-md-4">不重复题目</th>
				</tr>
				<tr class="row">
					<td class="container col-md-4">
						<ul id="best-result" class="solution similar"></ul>
					</td>
					<td class="container col-md-4">
						<ul id="repeat-result" class="solution similar"></ul>
					</td>
					<td class="container col-md-4">
						<ul id="no-repeat-result" class="solution similar"></ul>
					</td>
				</tr>
			</table>
		</div>
	</section>
	<!--错误原因浮层begin-->
	<!-- 
	<div class="modal fade"  tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title">错误原因</h4>
				</div>
				<div class="modal-body">
					<ul class="radio">
						<li>
							<label><input type="radio" name="error" value="最佳题目缺少图片、乱码">最佳题目缺少图片、乱码</label>
						</li>
						<li>
							<label><input type="radio" name="error" value="重复题目中存在不重复题目">重复题目中存在不重复题目</label>
						</li>
						<li>
							<label><input type="radio" name="error" value="不重复题目中存在重复题目">不重复题目中存在重复题目</label>
						</li>
					</ul>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="popSure">确认</button>
				</div>
			</div>
		</div>
	</div>
	 -->
	<!--错误原因浮层end-->
	<script type="text/x-jquery-tmpl" id="question-tmpl">
		<li class="solve item" data-id="{{= $data.questionId}}">
			<div class="content">
				<div class="detail">
					{{html $data.content || $data.latex}}
				</div>
			</div>
		</li>
	</script>
	<script type="text/x-jquery-tmpl" id="detail-tmpl">
		<li class="item" data-id="{{= $data.questionId}}">
			<div class="content solve">
				<div class="detail">
					{{html $data.content || $data.latex}}
				</div>
			</div>
		</li>
	</script>
	<script type="text/javascript">
		MX.load({
			js: 'lib/sea',
			version: '<%=Version.version %>',
			success: function() {
				seajs.use(['lib/jquery', 'util/jquery.tmpl'], function($, tmpl) {
					var container = $('<div>'),
						result = ${ result },
						sendResult;
					if(+result.status !== 0) {
						alert(result.msg);
						result.base_list = [];
						result.dup_list = [];
						result.undup_list = [];
					}
					$.each(result.base_list, function(i, o) {
						o.content = container.html(o.content).html();
					});
					$.each(result.dup_list, function(i, o) {
						o.content = container.html(o.content).html();
					});
					$.each(result.undup_list, function(i, o) {
						o.content = container.html(o.content).html();
					});
					$('#best-result').html($('#detail-tmpl').tmpl(result.base_list));
					$('#repeat-result').html($('#question-tmpl').tmpl(result.dup_list));
					$('#no-repeat-result').html($('#question-tmpl').tmpl(result.undup_list));
					sendResult = function(data, config) {
						config = config || {};
						$.ajax({
							url: window.basePath + 'dedup/audit',
							data: data,
							type: 'POST',
							dataType: 'json',
							success: function(data) {
								var checkId;
								if(data.status === 0) {
									checkId = data.result;
									if(checkId != '' && checkId != undefined) {
										window.location.href = window.basePath + 'mark/getCheckOneDupGroup?checkId=' + checkId;
									} else {
										window.location.href = window.basePath + 'dedup/checkRecordList';
									}
								} else {
									alert(data.msg);
								}
							}
						});
						if(config.callback) {
							config.callback();
						}
					};
					$('#confirm').on('click', function(e) {
						e.preventDefault();
						sendResult({
							status: 1,
							id: result.checkId
						});
					});
					$('#error').on('click', function(e) {
						e.preventDefault();
						sendResult({
							status: 2,
							id: result.checkId
						});
					});
					// $('#back').on('click', function(e) {
					// 	window.location.assign(window.basePath + 'dedup/dedupDetailViewSearch?parentId=' + result.parentId);
					// });
				});
			}
		});
	</script>
</body>
</html>
