<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsAuditStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html lang="zh-CN">
<head>
	<title>学习宝题库编辑器</title>
	<meta charset="UTF-8">
	<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
	<link rel="stylesheet" href="<%=basePath %>css/exam/editor.css" />
	<link rel="stylesheet" href="<%=basePath %>css/exam/problem.css" />
	<link rel="stylesheet" href="<%=basePath %>css/bootstrap/bootstrap.3.3.5.min.css" />
	<script type="text/javascript">
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
</head>
<body class="edit-page">
	<input id="topic_id" type='hidden' value='${ tranOps.id }' />
	<div id="preview-area" class="preview-area">
		<div class="header">
			<div class="photo-frame" id="photo-frame">
				<img src="<%=basePath %>image/default.png">
			</div>
			<div class="photo-footor">
				<select name="subject" class="subject">
					<option value="">学科</option>
					<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
					<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == tranOps.realSubject }">selected</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
				<select name="realType" class="question-type"><option value="">题型</option>
					<c:set var="enumSubjects" value="<%= EnumSubjectType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
					<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == tranOps.realType }">selected</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
				<select name="realLearnPhase" class="grade"><option value="">学段</option>
					<c:set var="enumSubjects" value="<%= EnumGradeType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
					<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == tranOps.realLearnPhase }">selected</c:if>>${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="solution-view">
			<div>
				<img class="sec-icon" src="<%=basePath %>image/exam/icon_wenti.png" alt="">
				<div class="preview-body"></div>
			</div>
			<div>
				<img class="sec-icon" src="<%=basePath %>image/exam/icon_jieda.png" alt="">
				<div class="preview-answer"></div>
			</div>
			<div>
				<img class="sec-icon" src="<%=basePath %>image/exam/icon_silu.png" alt="">
				<div class="preview-analysis"></div>
			</div>
			<span class="update-time">${ tranOps.updateTime }</span>
			<span class="author">${ tranOps.operatorName }</span>
			<br class="edit-clear">
		</div>
		<div class="latex-view">
			<div>
				<img src="<%=basePath %>image/exam/latex_body.png">
				<div class="latex-body"></div>
			</div>
			<div>
				<img src="<%=basePath %>image/exam/latex_answer.png">
				<div class="latex-answer"></div>
			</div>
		</div>
		<span class="sub no-answer" data-answer="1">不录答案</span>
		<span class="sub submit">提交到题库</span>
	</div>
	<div id="edit-area" class="edit-area">
		<c:set var="generalToolConfig" value='[
			[{"name":"blod-","title":"粗体","command":"bold"},
			{"name":"italic-","title":"斜体","command":"italic"},
			{"name":"underline-","title":"下划线","command":"underline"}],
			[{"name":"insert-ul","title":"无序列表","command":"insertunorderedlist"},
			{"name":"insert-ol","title":"有序列表","command":"insertorderedlist"},
			{"name":"insert-img","title":"插入图片","command":""},
			{"name":"insert-formula","title":"插入公式","command":""}],
			[{"name":"delete-style","title":"清除格式","command":""}]
		]'></c:set>
		<c:set var="generalCommands" value="${ func:parseArray(generalToolConfig) }"></c:set>
		<div class="edit-box edit-body">
			<div class="edit-btns">
				<ul class="tr-bar">
					<c:forEach var="commands" items="${ generalCommands }">
					<ul class="tr-group">
						<c:forEach var="command" items="${ func:parseArray(commands) }">
						<li class="tr-btn tool-btn ${ command.name }" title="${ command.title }" data-command="${ command.command }" data-param="${ command.param }">
							<input type="button">
						</li>
						</c:forEach>
					</ul>
					</c:forEach>
					<ul class="tr-group">
						<li class="tr-btn tool-btn add-choice" title="添加选项">
							<input type="button">
						</li>
						<li class="form-inline layout-container">
							<div class="form-group layout-item">
								<label for="choice-num">单行选项个数</label>
								<input type="text" class="form-control choice-num" id="choice-num" size="1" maxlength="1" value="1">
							</div>
						</li>
					</ul>
					
				</ul>
				<div class="submit-btn submit-body" data-title="问题描述">提交问题描述</div>
			</div>
			<div class="edit-border">
				<c:if test="${ tranOps.selectContent == null || tranOps.selectContent == ''}">
				<div class="edittable" contentEditable>${ tranOps.content }</div>
				</c:if>
				<c:if test="${ tranOps.selectContent != null && tranOps.selectContent != ''}">
				<div class="edittable" contentEditable>${ tranOps.selectContent }</div>
				<c:if test="${ SelectOption != null && SelectOption != ''}">
				<ul class="choice-list">
					<c:forEach var="choice" items="${ SelectOption }">
					<li class="choice-item" contentEditable>${ choice }</li>
					</c:forEach>
				</ul>
				</c:if>
				</c:if>
			</div>
		</div>
		<div class="edit-box edit-answer">
			<div class="edit-btns">
				<ul class="tr-bar">
					<c:forEach var="commands" items="${ generalCommands }">
					<ul class="tr-group">
						<c:forEach var="command" items="${ func:parseArray(commands) }">
						<li class="tr-btn tool-btn  ${ command.name }" title="${ command.title }" data-command="${ command.command }" data-param="${ command.param }">
							<input type="button">
						</li>
						</c:forEach>
					</ul>
					</c:forEach>
				</ul>
				<div class="submit-btn submit-answer" data-title="题目解答">提交题目解答</div>
			</div>
			<div class="edit-border">
				<div class="edittable" contentEditable>${ tranOps.answer }</div>
			</div>
		</div>
		<div class="edit-box edit-analysis">
			<div class="edit-btns">
				<ul class="tr-bar">
					<c:forEach var="commands" items="${ generalCommands }">
					<ul class="tr-group">
						<c:forEach var="command" items="${ func:parseArray(commands) }">
						<li class="tr-btn tool-btn ${ command.name }" title="${ command.title }" data-command="${ command.command }" data-param="${ command.param }">
							<input type="button">
						</li>
						</c:forEach>
					</ul>
					</c:forEach>
				</ul>
				<div class="submit-btn submit-analysis" data-title="解题思路">提交解题思路</div>
			</div>
			<div class="edit-border">
				<div class="edittable" contentEditable>${ tranOps.solution }</div>
			</div>
		</div>
		<br class="edit-clear"/>
	</div>
	<div>审核意见</div>
	<div class="p2em  mb20">
		<c:set var="AUDIT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_THROUGH %>"/>
		<c:set var="AUDIT_NOT_THROUGH" value="<%= TranOpsAuditStatus.AUDIT_NOT_THROUGH %>"/>
		<c:set var="PENDING_AUDIT" value="<%= TranOpsAuditStatus.PENDING_AUDIT %>"/>
		<c:set var="HALF_THROUGH" value="<%= TranOpsAuditStatus.HALF_THROUGH %>"/>
		<c:set var="BEST_AUDIT_THROUGH" value="<%= TranOpsAuditStatus.BEST_AUDIT_THROUGH %>"/>
		<c:if test="${ AUDIT_THROUGH.id == tranOps.auditStatus || PENDING_AUDIT.id == tranOps.auditStatus || HALF_THROUGH.id == tranOps.auditStatus || BEST_AUDIT_THROUGH.id == tranOps.auditStatus }">
		<p class="co_red">审核结果：${ tranOps.statusForShow }</p>
		</c:if>
		<c:if test="${ AUDIT_NOT_THROUGH.id == tranOps.auditStatus }">
		<p class="co_red">审核结果：${ tranOps.statusForShow }，原因：${ tranOps.lastTranOpsApprove.reason }</p>
		</c:if>
	</div>
	<div id="image-input" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
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
	<div id="formula-input" class="formula-input modal fade" tabindex="-1" role="dialog" aria-hidden="true">
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
			seajs.use(['lib/jquery', 'page/edit'], function($, edit) {
				edit.init('preview-area', {
					editArea: $('#edit-area'),
					submitUrl: window.basePath + 'tranops/updateTranOps',
					extend: function(data) {
						data.questionId = '${ tranOps.id }';
						return data;
					},
					submitCall: function(data) {
						document.location.href = window.basePath + 'tranops/tranOpsEditSearch';
					}
				});
			});
		}
	});
</script>
</body>
</html>