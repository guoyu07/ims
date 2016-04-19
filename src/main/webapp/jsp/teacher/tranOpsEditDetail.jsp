<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumGradeType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubject" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumSubjectType" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumDiff" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumQuestionCategory" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumTeacherAuditStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta http-equiv="keywords" content=""/>
<meta http-equiv="description" content=""/>
<title>标注试题</title>
<link href="../css/bootstrap/bootstrap.3.3.5.min.css" rel="stylesheet" type="text/css"/>
<link href="../css/merge.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	.page-header {
		padding: 8px 20px;
		margin: 0 0 15px;
	}
	.container {
		width: 98%;
		border: 1px solid #ddd;
		padding-bottom: 5px;
		margin: 0 1%;
	}
	footer.container {
		margin-bottom: 15px;
		padding-top: 10px;
		text-align: center;
	}
	#knowledge-tree-container {
		position: absolute;
		border: 1px solid #ddd;
		background: white;
		min-width: 600px;
		z-index: 10;
	}
	#knowledge-tree {
		height: 300px;
		overflow-y: scroll;
		margin-top: 30px;
	}
	.knowledge-close {
		float: right;
		padding: 5px;
		z-index: 5;
	}
	.knowledge-tag {
		margin-left: 3px;
	}
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
	<header class="page-header">
		<span class="h3">标注试题</span>
	</header>
	<section class="container">
		<header class="h4">题目</header>
		<article>${ tranOps.content }</article>
	</section>
	<section class="container">
		<header class="h4">答案</header>
		<article>${ tranOps.answer }</article>
	</section>
	<section class="container">
		<header class="h4">解题思路</header>
		<article>${ tranOps.solution }</article>
	</section>
	<section class="container">
		<header class="h4">年级、学科、题型</header>
		<article>
			<div class="row form-inline">
				<div class="col-xs-2">
					<label class="control-label" for="grade">年级</label>
					<select class="form-control" id="grade" name="grade">
	                	<option value="">请选择年级</option>
						<c:set var="enumLearnPhases" value="<%= EnumGradeType.values() %>"/>
						<c:forEach var="enumLearnPhase" items="${ enumLearnPhases }">
						<option value="${ enumLearnPhase.id }" <c:if test="${ enumLearnPhase.id == tranOps.realLearnPhase }">selected = "selected"</c:if>>${ enumLearnPhase.chineseName }</option>
						</c:forEach>
	                </select>
				</div>
				<div class="col-xs-2">
					<label class="control-label" for="subject">学科</label>
					<select class="form-control" id="subject" name="subject">
	                	<option value="">请选择学科</option>
						<c:set var="enumSubjects" value="<%= EnumSubject.values() %>"/>
						<c:forEach var="enumSubject" items="${ enumSubjects }">
						<option value="${ enumSubject.id }" <c:if test="${ enumSubject.id == tranOps.realSubject }">selected = "selected"</c:if>>${ enumSubject.chineseName }</option>
						</c:forEach>
	                </select>
				</div>
				<div class="col-xs-2">
					<label class="control-label" for="type">题型</label>
					<select class="form-control" id="type" name="type">
	                	<option value="">请选择题型</option>
						<c:set var="enumSubjectTypes" value="<%= EnumSubjectType.values() %>"/>
						<c:forEach var="enumSubjectType" items="${ enumSubjectTypes }">
						<option value="${ enumSubjectType.id }" <c:if test="${ enumSubjectType.id == tranOps.realType }">selected = "selected"</c:if>>${ enumSubjectType.chineseName }</option>
						</c:forEach>
	                </select>
				</div>
			</div>
		</article>
	</section>
	<section class="container">
		<header class="h4">知识点</header>
		<article class="form-inline" id="knowledge-container" style="display: relative">
			<div class="form-group has-feedback">
				<input type="text" class="form-control search-key" value="" placeholder="最多添加3个知识点">
				<span class="glyphicon glyphicon-search search-btn"></span>
			</div>
			<div class="knowledge-list">
				<c:forEach var="knowledge" items="${ tranOps.knowledgeNodeArray }">
					<span class="label label-default knowledge-tag" data-id="${ knowledge.id }">${ knowledge.text }<span class="glyphicon glyphicon-remove knowledge-remove"></span></span>
				</c:forEach>
			</div>
			<div id="knowledge-tree-container" style="display:none">
				<a href="#" title="关闭" class="glyphicon glyphicon-remove knowledge-close"></a>
				<div id="knowledge-tree"></div>
			</div>
		</article>
	</section>
	<section class="container">
		<header class="h4">难度</header>
		<article>
			<c:set var="enumDiffs" value="<%= EnumDiff.values() %>"/>
			<c:forEach var="enumDiff" items="${ enumDiffs }">
			<label class="radio-inline">
				<input type="radio" <c:if test="${ enumDiff.id == tranOps.realDiff }">checked</c:if> name="difficulty" value="${ enumDiff.id }">${ enumDiff.description }
			</label>
			</c:forEach>
		</article>
	</section>
	<section class="container">
		<header class="h4">题目分类</header>
		<article>
			<c:set var="enumQuestionCategorys" value="<%= EnumQuestionCategory.values() %>"/>
			<c:forEach var="enumQuestionCategory" items="${ enumQuestionCategorys }">
			<label class="checkbox-inline">
				<input type="checkbox" <c:forEach var="categoryArray" items="${ tranOps.questionCategoryArray }">
				<c:if test="${ enumQuestionCategory.id == categoryArray }">checked</c:if>
			</c:forEach> name="category" value="${ enumQuestionCategory.id }">${ enumQuestionCategory.description }
			</label>
			</c:forEach>
		</article>
	</section>
	<c:set var="PENDING"				value="<%= EnumTeacherAuditStatus.PENDING %>"/>
   	<c:set var="COMPLETE"			value="<%= EnumTeacherAuditStatus.COMPLETE %>"/>
   	<c:set var="AUDIT_THROUGH"		value="<%= EnumTeacherAuditStatus.AUDIT_THROUGH %>"/>
   	<c:set var="AUDIT_NOT_THROUGH"	value="<%= EnumTeacherAuditStatus.AUDIT_NOT_THROUGH %>"/>
	<c:choose>
		<c:when test="${tranOps.teacherStatus == AUDIT_THROUGH.id}">
			<section class="container">
				<header class="h4">审核意见</header>
				<article class="text-danger">
					<p>审核结果：审核通过</p>
				</article>
			</section>
		</c:when>
		<c:when test="${tranOps.teacherStatus == AUDIT_NOT_THROUGH.id}">
			<section class="container">
				<header class="h4">审核意见</header>
				<article class="text-danger">
					<p>审核结果：审核未通过</p>
					<p>错误原因：${tranOps.auditReason}</p>
				</article>
			</section>
		</c:when>
	</c:choose>
	<footer class="container">
		<c:if test="${tranOps.teacherStatus != AUDIT_THROUGH.id}">
			<button id="submit" class="btn btn-primary">提交</button>
			<a href="tranOpsEditSearch" class="btn btn-default" style="margin-left:64px">返回</a>
		</c:if>
		<c:if test="${tranOps.teacherStatus == AUDIT_THROUGH.id}">
			<a href="tranOpsEditSearch" class="btn btn-default">返回</a>
		</c:if>
	</footer>
	<script>
		MX.load({
			js: 'lib/sea',
			version: '<%=Version.version %>',
			success: function() {
				seajs.use(['lib/jquery', 'util/bootstrap.treeview'], function($, treeview) {
					var knowledgeContainer = $('#knowledge-container'),
					knowledgeList = knowledgeContainer.find('.knowledge-list'),
						treeContainer = $('#knowledge-tree-container'),
						knowledgeTree = $('#knowledge-tree');
						// 知识点哈希
						knowledgeHash = {
							length: 0
						},
						gradeDrop = $('#grade'),
						subjectDrop = $('#subject');
					// 初始化知识点哈希
					knowledgeList.find('.knowledge-tag').each(function() {
						knowledgeHash[$(this).data('id')] = 1;
						knowledgeHash.length++;
					});
					// 搜索知识点
					knowledgeContainer.find('.search-btn').on('click', function(e) {
						var el = $(this), grade, subject, searchInput;
						e.stopPropagation();
						searchInput = el.siblings('.search-key');
						grade = gradeDrop.find('option:selected').val();
						if(!grade) {
							searchInput.val('');
							alert('请先选择相应的年级');
							return;
						}
						subject = subjectDrop.find('option:selected').val()
						if(!subject) {
							searchInput.val('');
							alert('请先选择相应的学科');
							return;
						}
						if(knowledgeHash.length > 3) {
							searchInput.val('');
							alert("您添加的知识点已达上限");
							return;
						}
						if(!el.data('sending')) {
							el.data('sending', 1);
							$.ajax({
								url: window.basePath + 'teacher/getKnowledgeTree',
								type: 'POST',
								data: {
									keyWord: searchInput.val().trim(),
									learnPhase: grade,
									subject: subject
								},
								dataType: 'json',
								success: function(data) {
									el.data('sending', 0);
									searchInput.val('');
									if(data.status === 0) {
										if(data.result.length) {
											knowledgeTree.treeview({
												color: "#428bca",
												showBorder: false,
												data: data.result,
												levels: 1,
												onNodeSelected: function(e, node) {
													// 如果是叶子节点
													var knowledgeId = node.id;
													if(knowledgeId) {
														//  如果已经添加该知识点 
														if(knowledgeHash[knowledgeId]) {
															alert('该知识点已添加');
														} else {
															if(knowledgeHash.length >= 3) {
																alert("您添加的知识点已达上限");
																return;
															}
															// 添加知识点
															knowledgeHash[knowledgeId] = 1;
															knowledgeHash.length++;
															knowledgeList.append($('<span class="label label-default knowledge-tag" data-id="' + knowledgeId + '">' + node.text + '<span class="glyphicon glyphicon-remove knowledge-remove"></span></span>'));
														}
													}
												}
											});
											treeContainer.show();
										} else {
											alert('请尝试其他关键词');
										}
									} else {
										alert(data.msg);
									}
								},
								error: function() {
									el.data('sending', 0);
									alert('网络情况不佳，请稍后再试');
								}
							});
						}
					});
					// 删除知识点
					knowledgeContainer.on('click', '.knowledge-remove', function(e) {
						var el = $(this).closest('.knowledge-tag'), id;
						id = el.data('id');
						delete knowledgeHash[id];
						knowledgeHash.length--;
						el.remove();
					});
					knowledgeContainer.on('click', '.knowledge-close', function(e) {
						e.preventDefault();
						treeContainer.hide();
					});
					$('#submit').on('click', function(e) {
						var el = $(this), data = {
							questionId: '${tranOps.id}',
							realKnowledge: [],
							knowledgeText: [],
							questionCategory: []
						}, categories;
						// 表单验证
						data.learnPhase = gradeDrop.find('option:selected').val();
						if(!data.learnPhase) {
							alert('请选择相应的年级');
							return;
						}
						data.subject = subjectDrop.find('option:selected').val()
						if(!data.subject) {
							alert('请选择相应的学科');
							return;
						}
						data.realType = $('#type').find('option:selected').val();
						if(!data.realType) {
							alert('请选择相应的题型');
							return;
						}
						if(!knowledgeHash.length) {
							alert('请选择至少一个知识点');
							return;
						}
						data.realDiff = $('input[name=difficulty]:checked').val();
						if(!data.realDiff) {
							alert('请选择难度');
							return;
						}
						categories = $('input[name=category]:checked');
						if(!categories.length) {
							alert('请选择题目分类');
							return;
						}

						// 提交表单
						el.prop('disabled', true);
						knowledgeList.find('.knowledge-tag').each(function() {
							var el = $(this);
							data.realKnowledge.push(el.data('id'));
							data.knowledgeText.push(el.text());
						});
						categories.each(function() {
							data.questionCategory.push($(this).val());
						});
						data.realKnowledge = data.realKnowledge.join();
						data.knowledgeText = data.knowledgeText.join();
						data.questionCategory = data.questionCategory.join();
						$.ajax({
							url: 'updateTranOps',
							type: 'POST',
							data: data,
							dataType: 'json',
							success: function(data) {
								el.prop('disabled', false);
								if(data.status === 0) {
									document.location.href = 'getNext';
								} else {
									alert(data.msg);
								}
							},
							error: function() {
								el.prop('disabled', false);
								alert('网络情况不佳，请稍后再试');
							}
						});
					});
				});
			}
		});
	</script>
</body>
</html>
