<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCompleteStatus" %>
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
<title>审核列表</title>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
<c:set var="preUrl" value="tranOpsAdminAuditSearch
							?questionId=${ questionId }
							&subject=${ subject }
							&realType=${ realType }
							&realLearnPhase=${ realLearnPhase }
							&teacher=${ teacher }
							&status=${ status }
							&complete=${ complete }
							&startTime=${ startTime }
							&endTime=${ endTime }
							&approvor=${ approvor }
							&auditStartTime=${ auditStartTime }
							&auditEndTime=${ auditEndTime }&" />
	<div class="crumb">当前位置：审核列表</div>
	<div class="static2_base">
		<form>
			<ul class="searchSub">
				<li>题目ID<input type="text" value="${ questionId }" name="questionId" /></li>
				<li>学科
					<select name="subject">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
							<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == subject }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
						</c:forEach>
					</select>
				</li>
				<li>题型
					<select name="realType">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= EnumSubjectType.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
							<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == realType }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
						</c:forEach>
					</select>
				</li>
				<li>年级
					<select name="realLearnPhase">
						<option value="">全部</option>
						<c:set var="enumSubjects" value="<%= EnumGradeType.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
							<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == realLearnPhase }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
				</li>
				<li>审核状态
					<select name="status">
						<option value="">全部</option>
						<c:set var="enumStatuses" value="<%= TranOpsAuditStatus.values() %>"/>
						<c:forEach var="enumStatus" items="${ enumStatuses }">
							<option value="${ enumStatus.id }" <c:if test="${ enumStatus.id == status }">selected = "selected"</c:if>>${ enumStatus.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>完整程度
					<select name="complete">
						<option value="">全部</option>
						<c:set var="enumCompletes" value="<%= TranOpsCompleteStatus.values() %>"/>
						<c:forEach var="enumComplete" items="${ enumCompletes }">
							<option value="${ enumComplete.id }" <c:if test="${ enumComplete.id == complete }">selected = "selected"</c:if>>${ enumComplete.desc }</option>
						</c:forEach>
					</select>
				</li>
				<li>录制人<input type="text" value="${ teacher }" name="teacher" /></li>
				<li>审核人<input type="text" value="${ approvor }" name="approvor" /></li>
				<li>录制时间<input class="form-control form-date" type="text" value="${ startTime }" name="startTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form-date" type="text" value="${ endTime }" name="endTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
				<li>审核日期<input class="form-control form-date" type="text" value="${ auditStartTime }" name="auditStartTime" data-date-format="yyyy-mm-dd 00:00:00">~<input class="form-control form-date" type="text" value="${ auditEndTime }" name="auditEndTime" data-date-format="yyyy-mm-dd 23:59:59"/></li>
				<li>
					<button class="btn btn-primary btn-xs">搜索</button>
				</li>
			</ul>
		</form>
		<table class="table table-hover table-bordered table-condensed">
			<thead>
				<tr><th colspan="12"><button class="btn btn-primary btn-xs" id="batchAuditBtn">批量审核</button></th></tr>
				<tr class="info">
					<th width="20px"><input type="checkbox" id="allCheckbox"/></th>
					<th style="min-width:50px">题目ID</th>
					<th style="min-width:40px">完整程度</th>
					<th style="min-width:40px">学科</th>
					<th style="min-width:40px">题型</th>
					<th style="min-width:40px">年级</th>
					<th style="min-width:100px">题干</th>
					<th style="min-width:90px">录制人</th>
					<th style="min-width:90px">小组</th>
					<th style="min-width:90px">录题时间</th>
					<th style="min-width:90px">审核时间</th>
					<th style="min-width:80px">审核人</th>
					<th style="min-width:80px">审核结果</th>
					<th style="min-width:90px">审核未通过原因</th>
					<th style="min-width:40px">重新录制</th>
				</tr>
			</thead>
			<tbody id="question-list">
			<c:set var="AUDIT_NOT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_NOT_THROUGH %>"/>
			<c:set var="PENDING_AUDIT" value="<%= TranOpsAuditStatus.PENDING_AUDIT %>"/>
			<c:set var="NOT_COMPLETE" value="<%= TranOpsCompleteStatus.NOT_COMPLETE %>"/>
			<c:set var="COMPLETE" value="<%= TranOpsCompleteStatus.COMPLETE %>"/>
				<c:forEach var="audio" items="${ tranOpsList }">
				<tr>
					<td><input type="checkbox" name="checkbox" data-audited="${ audio.auditStatus }" data-question-id="${ audio.id }"/></td>  
					<td><a href="<%=basePath %>tranops/auditTranOpsById?questionId=${ audio.id }">${ audio.id }</a></td>
					<td>${ audio.completeForShow }</td>
					<td>${ audio.subject }</td>
					<td>${ audio.type }</td>
					<td>${ audio.learnphase }</td>
					<td>${ audio.content }</td>
					<td><c:if test="${ COMPLETE.id == audio.complete }">${ audio.operatorName }</c:if><c:if test="${ NOT_COMPLETE.id == audio.complete }">${ audio.contentOperatorName }</c:if></td>
					<td>${ audio.teamNameForShow }</td>
					<td>${ func:formatDate(audio.createTime) }</td>
					<td><c:if test="${ PENDING_AUDIT.id != audio.auditStatus }">${ func:formatDate(audio.approveTime) }</c:if></td>
					<td><c:if test="${ PENDING_AUDIT.id != audio.auditStatus }">${ audio.lastTranOpsApprove.approvor }</c:if></td>
					<td class="shengheResult">${ audio.statusForShow }<input type="hidden" value="${ audio.auditStatus }" /></td>
					<td><c:if test="${ AUDIT_NOT_THROUGH.id == audio.auditStatus }">
						${ audio.lastTranOpsApprove.reason }
						</c:if></td>
					<td>${ audio.isRerecordForShow }</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file = "../inc/newpage.jsp" %>
	</div>
	<div id="modal-dialog" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
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
			seajs.use(['lib/jquery', 'util/bootstrap.datetimepicker.zh-CN', 'module/Dialog'], function($, datetimepicker, dialog) {
				// 全选按钮
				var allCheck = $('#allCheckbox'),
					questionList = $('#question-list'),
					checkList = questionList.find('tr input').filter('[name="checkbox"]');
				// 绑定datetimepicker插件
				$(".form-date").datetimepicker({
					language: 'zh-CN',/*加载日历语言包，可自定义*/
					weekStart: 1,
					todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					minView: 2,
					forceParse: 0,
					showMeridian: 1
				});
				// 绑定全选
				allCheck.on('click', function(e) {
					var el = $(this);
					if(el.prop('checked')) {
						checkList.filter(function(i, o) {
							return $(o).data('audited') === 0;
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
				$("#batchAuditBtn").on('click', function(e) {
					var el = $(this),
						questionIds, data;
					questionIds = Array.prototype.map.call(checkList.filter(':checked'), function(o) {
						return $(o).data('question-id');
					}).join();
					if(questionIds.length === 0) {
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
							'<p class="result co_red" style="display:none">',
								'原因：',
								'<select class="why">',
									'<option value="0">识别结果正确，不该录</option>',
									'<option value="1">文字格式问题</option>',
									'<option value="2">图片有问题</option>',
									'<option value="3">学科选择错误</option>',
									'<option value="4">请补充解题思路&知识点</option>',
									'<option value="5">其他</option>',
								'</select>',
								'<input type="text" class="ml10 reason-txt" maxlength="50" style="width:194px;margin-left:0;margin-top:10px;display:none">',
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
									questionIds: questionIds
								};
								if(el.prop('checked')) {
									auditStatus = +el.val();
									data.status = auditStatus;
									if(auditStatus === 2) {
										result.hide();
										delete data.reason;
									} else {
										result.show();
										data.reason = reason.val();
									}
								}
							});
						},
						confirm: function() {
							var Self = this, reasonTxt;
							reasonTxt = Self._body.find('.reason-txt').val().trim();
							if(reasonTxt) {
								data.reasonStr = reasonTxt;
							}
							$.ajax({
								url: window.basePath + 'tranops/audit',
								type:'GET',
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
