<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.OrcPictureCheckStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	<link href="<%=basePath %>/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>/css/merge.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		.mark-repeat {
			margin: 20px 20px 0;
			padding-bottom: 15px;
		}
		.mark-repeat .panel-heading span{
			font-size: 18px;
		}
		.mark-repeat .btn-primary {
			min-width: 100px;
		}
		.mark-repeat .panel-body {
			padding-top: 8px;
			margin-bottom: 15px;
		}
		.mark-repeat .heading {
			font-size: 16px;
			color: #0e7aff;
		}
		.mark-repeat table.contrast {
			margin-top: 15px;
			width: 100%
		}
		.mark-repeat .similar-container {
			padding: 0;
		}
		.mark-repeat .similar {
			margin-top: 13px;
			max-height: 520px;
			overflow-y: scroll;
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
		.mark-repeat .table-bordered {
			border: none;
		}
		.mark-repeat .contrast tr, .mark-repeat .similar tr {
			vertical-align: top;
		}
		.mark-repeat .contrast>tbody>tr>td.container, .mark-repeat tbody>tr>td.item {
			border: 1px solid #c51114;
		}
		.mark-repeat .content {
			word-break: break-all;
		}
		.mark-repeat .item {
			cursor: pointer;
		}
		.mark-repeat td.item {
			padding: 8px 15px 0;
		}
		.mark-repeat .selected {
			background-color: #3399ff;
		}
		.mark-repeat .selected * {
			background-color: transparent !important;
			border-color: #3399ff !important;
		}
		.mark-repeat .btn-container {
			margin-top: -5px;
			float: right;
			display: inline-block;
			padding-right: 60px;
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
				<span>请选择与样题重复的题目</span>
				<div class="btn-container">
					<c:if test="${ result.remainCNT != null }"> <span>当前组还有${ result.remainCNT }题</span> </c:if>
					<button id="next" class="btn btn-primary" style="margin-left:34px" disabled>已选择重复题目</button>
					<button id="no-similar" class="btn btn-primary" style="margin-left:34px">没有与样题重复题目</button>
					<button id="next-group" class="btn btn-primary" style="margin-left:64px">下一组</button>
				</div>
			</div>
			<table class="panel-body contrast table-bordered">
				<tr class="row">
					<td class="container col-md-5 col-md-offset-1">
						<div class="heading">样&nbsp;题</div>
						<div id="reference"></div>
					</td>
					<td class="container similar-container col-md-5">
						<ul id="multi-select" class="solution similar"></ul>
					</td>
				</tr>
			</table>
		</div>
	</section>
	<script type="text/x-jquery-tmpl" id="mark-repeat-tmpl">
		{{each(i, item) list}}
			<li class="solve item" data-id="{{= item.question_id}}">
				<div class="content">
					<div class="detail">
						{{html item.content||item.latex}}
					</div>
				</div>
			</li>
		{{/each}}
	</script>
	<script type="text/javascript">
		MX.load({
			js: 'lib/sea',
			version: '<%=Version.version %>',
			success: function() {
				seajs.use(['lib/jquery', 'util/jquery.tmpl'], function($, tmpl) {
					var result = ${ result },
						reference, similarList, map = {},
						element, btnContainer, nextBtn, container,
						requestXhr, Select;
					/**
					 * @constructor 自定义选择
					 * @param {String|jQuery|DOM} id 选择container id
					 * @param {Object} config 扩展参数
					 * @config {String} item 选项选择器
					 * @config {Function} clickEvent 点击事件处理
					 * @config {String} selected 选中态类名
					 */
					Select = function(id, config) {
						var Self = this;
						config = config || {};
						Self._body = $(id);
						Self.selected = 'selected';
						Self.item = '.item';
						Self.callback = function() {
							Self._items = Self._items || Self._body.find(Self.item);
							return Self._items.filter('.' + Self.selected).map(function(i, o) {
								return $(o).data('id');
							}).toArray();
						};
						Self = $.extend(Self, config);
						Self._body.on('click', Self.item, function() {
							Self.clickEvent(this, Self);
						});
					};
					if(+result.status !== 0) {
						alert(result.msg);
						result.candidate_list = [];
					}
					requestXhr = function(data, config) {
						data = data || {};
						data.baseId = reference.base_id;
						data.block  = result.block;
						$.ajax({
							url: window.basePath + 'mark/updateOneDupGroup',
							data: data,
							type: 'POST',
							dataType: 'json',
							success: function(data) {
								if(data.status == 0) {
									window.location.reload();
								} else {
									alert(data.msg);
								}
							}
						});
					};
					btnContainer = $('#btn-container');
					nextBtn = btnContainer.find('.btn-next');
					container = $('<div>');
					// 非法html容错
					$.each(result.candidate_list, function(i, o) {
						o.content = container.html(o.content).html();
					});
					reference = result.candidate_list[0],
					similarList = result.candidate_list.slice(1),
					result.candidate_list.forEach(function(o, i) {
						map[o.questionId] = i;
					});
					// 下一组
					btnContainer.on('click', '.next-group', function(e) {
						$.ajax({
							url: window.basePath + 'mark/assignNewBlock',
							type: 'GET',
							dataType: 'json',
							success: function(data) {
								if(data.status == 0) {
									window.location.reload();
								} else {
									alert(data.msg);
								}
							}
						});
					});
					if(!reference) {
						btnContainer.find('.no-similar').prop('disabled', true);
						return;
					}
					// 样题内容
					$('#reference').html(reference.content);
					// 实例化复选框
					multiSelect = new Select('#multi-select', {
						clickEvent: function(item, Self) {
							$(item).toggleClass(Self.selected);
							nextBtn.prop('disabled', !Self._body.find('.' + Self.selected)[0]);
						}
					});
					// 初始化重复标记页面
					element = $('#mark-repeat-tmpl').tmpl({list: similarList});
					if(element.length) {
						multiSelect._body.empty().append(element);
					}
					// 下一步事件处理
					btnContainer.on('click', '.btn-next', function(e) {
						$(this).prop('disabled', true);
						// 通过回调函数获得选中节点对应的题目id
						requestXhr({
							dupQuestionId: multiSelect.callback().join()
						});
					});
					// 无重复结果事件处理
					btnContainer.on('click', '.no-similar', function(e) {
						if(window.confirm('确认没有与样题重复题目？')) {
							requestXhr({
								dupQuestionId: ''
							});
						}
					});
				});
			}
		});
	</script>
</body>
</html>
