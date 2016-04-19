<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumTeacherAuditStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
<title>题目加标签</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b]);};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="tranOpsAuditSearch
							?questionId=${ questionId }
							&subject=${ subject }
							&status=${ status }
							&startTime=${ startTime }
							&endTime=${ endTime }&" />
<div class="crumb">当前位置：题目加标签</div>
<div class="static2_base">
	<form>
		<ul class="searchSub">
			<li>题目ID<input type="text" value="${ question.id }" name="questionId" /></li>
			<li>学科
				<select name="subject">
                	<option value="">全部</option>
					<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
					<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == subject }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
                </select>
			</li>
			<li>标注状态
				<select name="status">
                	<option value="">全部</option>
					<c:set var="enumStatuses" value="<%= EnumTeacherAuditStatus.values() %>"/>
					<c:forEach var="enumStatus" items="${ enumStatuses }">
					<option value="${ enumStatus.id }" <c:if test="${ enumStatus.id == status }">selected = "selected"</c:if>>${ enumStatus.description }</option>
					</c:forEach>
                </select>
			</li>
			<li>标注时间<input class="form-control form-date" type="text" value="${ startTime }" name="startTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form-date" type="text" value="${ endTime }" name="endTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
			<li>
				<button class="btn btn-primary btn-xs">查询</button>
			</li>
		</ul>
	</form>

	<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="12">
						<button class="btn btn-primary btn-xs" id="batch-audit-btn">批量审核</button>
					</th>
				</tr>
				<tr class="info">
					<th width="20px"><input type="checkbox" value=""
						id="all-checkbox" /></th>
					<th style="min-width:50px">题目ID</th>
					<th style="min-width:40px">年级</th>
					<th style="min-width:40px">学科</th>
					<th style="min-width:40px">题型</th>
					<th style="min-width:90px">标注人</th>
					<th style="min-width:90px">审核状态</th>
					<th style="min-width:150px">审核时间</th>
				</tr>
			</thead>
			<tbody id="question-list">
				<c:forEach var="question" items="${ tranOpsList }">
					<tr>
						<td><input type="checkbox" value="" name="checkbox" data-audit-status="${ question.teacherStatus }" data-question-id="${ question.id }"/></td>
						<td class="questionId"><a href="<%=basePath %>teacher/auditDetail?questionId=${ question.id }">${ question.id }</a></td>
						<td><c:set var="enumLearnPhases" value="<%= EnumGradeType.values() %>"/>
						<c:forEach var="enumLearnPhase" items="${ enumLearnPhases }">
						<c:if test="${ enumLearnPhase.id == question.realLearnPhase }">${ enumLearnPhase.chineseName }</c:if>
						</c:forEach></td>
						<td><c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<c:if test="${ enumSubject.id == question.realSubject }">${ enumSubject.chineseName }</c:if>
						</c:forEach></td>
						<td><c:set var="enumSubjectTypes" value="<%= EnumSubjectType.values() %>"/>
						<c:forEach var="enumSubjectType" items="${ enumSubjectTypes }">
						<c:if test="${ enumSubjectType.id == question.realType }">${ enumSubjectType.chineseName }</c:if>
						</c:forEach></td>
						<td>${ question.teacher }</td>
						<td><c:set var="enumTeacherAuditStatuss" value="<%= EnumTeacherAuditStatus.values() %>"/>
						<c:forEach var="enumTeacherAuditStatus" items="${ enumTeacherAuditStatuss }">
						<c:if test="${ enumTeacherAuditStatus.id == question.teacherStatus }">${ enumTeacherAuditStatus.description }</c:if>
						</c:forEach></td>
						<td>${ func:formatDate(question.teacherAuditTime) }</td>
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
			seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN', 'module/Dialog'], function($, datetimepicker, dialog) {
				// 全选按钮
				var allCheck = $('#all-checkbox'),
					questionList = $('#question-list'),
					checkList = questionList.find('tr input').filter('[name="checkbox"]');
				// 绑定datetimepicker插件
				$(".form-date").datetimepicker({
					language: 'zh-CN',/*加载日历语言包，可自定义*/
					weekStart: 1,
					todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					forceParse: 0,
					minView: 2,
					showMeridian: 1
				});
				// 绑定全选
				allCheck.on('click', function(e) {
					var el = $(this);
					if(el.prop('checked')) {
						checkList.filter(function(i, o) {
							return $(o).data('audit-status') === 1;
						}).prop('checked', true);
					} else {
						checkList.prop('checked', false);
					}
				});
				// 取消全选
				questionList.on('click', 'tr input[name=checkbox]', function(e) {
					if(!$(this).prop('checked')) {
						allCheck.prop('checked', false);
					}
				});
				// 批量审核
				$("#batch-audit-btn").on('click', function(e) {
					var el = $(this),
						questionId, data;
					questionId = Array.prototype.map.call(checkList.filter(':checked'), function(o) {
						return $(o).data('question-id');
					}).join();
					if(questionId.length === 0) {
						alert('请选择待审核题目！');
						return;
					}
					dialog.show('modal-dialog', {
						sizeClass: 'modal-sm',
						title: '批量审核',
						content: [
							'<p class="audit">',
								'审核结果：<label for="passed"><input type="radio" name="shenhe" id="passed" value="2"/>审核通过</label>',
								'<label for="unpassed" class="ml10"><input type="radio" name="shenhe" id="unpassed" value="3"/>审核不通过</label>',
							'</p>',
							'<p class="result" style="display:none">',
								'<input type="text" class="ml10 reason-txt" maxlength="50" style="width:194px;margin-left:0;" value= "" placeholder="请填写错误原因"/>',
							'</p>'
						].join(''),
						source: el.attr('id'),
						initCall: function() {
							var Self = this, result, reason, reasonTxt;
							result = Self._body.find('.result');
							reason = result.find('.why');
							reasonTxt = result.find('.reason-txt');
							reason.on('change', function(e) {
								var val = +$(this).val();
								data.reason = val;
								reasonTxt.toggle(val === 5);
								if(val !== 5) {
									reasonTxt.val('');
								}
							});
							Self._body.on('click.bs.custom', '.audit input', function(e) {
								var el = $(this), auditStatus;
								data = {
									questionId: questionId
								};
								if(el.prop('checked')) {
									auditStatus = +el.val();
									data.status = auditStatus;
									if(auditStatus === 2) {
										result.hide();
										delete data.reason;
									} else {
										result.show();
									}
								}
							});
						},
						confirm: function() {
							var Self = this, reasonTxt;
							reasonTxt = Self._body.find('.reason-txt').val().trim();
							if(reasonTxt) {
								data.reason = reasonTxt;
							}
							$.ajax({
								url: window.basePath + 'teacher/audit',
								type:'POST',
								data: data,
								dataType: 'json',
								success: function(data) {
									if(data.status === 0) {
										window.location.reload();
									} else {
										Self.enableConfirm();
										alert(data.msg);
									}
								},
								error: function() {
									Self.enableConfirm();
									alert('网络错误请稍后重试！');
								}
							});
						}
					});
				});
			});
		}
	});
</script>
</body>
</html>

