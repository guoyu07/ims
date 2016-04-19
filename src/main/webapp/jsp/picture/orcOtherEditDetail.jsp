<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
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
	<title>学习宝识别结果</title>
	<meta charset="UTF-8">
	<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
	<link rel="stylesheet" href="<%=basePath %>css/exam/editor.css" />
	<link rel="stylesheet" href="<%=basePath %>css/exam/problem.css" />
	<link rel="stylesheet" href="<%=basePath %>css/bootstrap/bootstrap.3.3.5.min.css" />
	<script type="text/javascript">
		var path = '<%=path %>';
		var basePath = '<%=basePath%>';
		(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
	</script>
</head>
<body class="edit-page">
	<div id="preview-area" class="preview-area">
		<div class="header">
			<div class="pic-id">图片id:  <span id="pic_id"></span></div>
			<div class="photo-frame">
				<img src="<%=basePath %>image/default.png">
			</div>
			<div class="photo-footor">
				<select name="subject" class="subject"><option value="">学科</option>
					<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }">${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
				<select name="realType" class="question-type"><option value="">题型</option>
					<c:set var="enumSubjects" value="<%= EnumSubjectType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }">${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
				<select name="realLearnPhase" class="grade"><option value="">学段</option>
					<c:set var="enumSubjects" value="<%= EnumGradeType.values() %>"/>
					<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }">${ enumSubject.chineseName }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="solution-view">
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
			<span class="update-time"></span>
			<span class="author"></span>
			<br class="edit-clear">
		</div>
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
		<div class="photo-big"></div>
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
				<div class="edittable" contentEditable></div>
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
				<div class="edittable" contentEditable></div>
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
				<div class="edittable" contentEditable></div>
			</div>
		</div>
		<br class="edit-clear"/>
	</div>
	<br class="edit-clear"/>
	<div id="image-input" class="modal fade"  tabindex="-1" role="dialog" aria-hidden="true">
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
	<div id="formula-input" class="formula-input modal fade"  tabindex="-1" role="dialog" aria-hidden="true">
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
				var previewArea = $('#preview-area'), data, editArea = $('#edit-area');
				data = JSON.parse(window.localStorage.result);
				window.localStorage.removeItem('result');
				previewArea.find('.photo-frame img').attr('src', data.picUrl);
				editArea.find('.photo-big').html('<img src="' + data.picUrl + '">');
				editArea.find('.edit-body .edittable').html(data.body);
				editArea.find('.edit-answer .edittable').html(data.answer);
				editArea.find('.edit-analysis .edittable').html(data.analysis);
				editArea.on('click', '.photo-big img', function(e) {
					window.open($(this).attr('src'), '', 'width=800,scrollbars=yes,status=yes')
				});
				edit.init(previewArea, {
					editArea: editArea,
					complete: 1,
					submitUrl: window.basePath + 'picture/addTranOpsOrc',
					extend: function(config) {
						config.pictureId = data.pictureId;
						config.pictureUrl = data.picUrl;
						config.target = data.target;
						config.status = 3;
						return config;
					},
					submitCall: function(config) {
						document.location.href = window.basePath + 'picture/viewOrcPictureById?pictureId=' + data.pictureId;
					}
				});
			});
		}
	});
</script>
</body>
</html>