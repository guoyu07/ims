/*
 * @description 编辑器
 * @author lishunping(lishunping@xuexibao.cn)
 */
define('page/edit', ['lib/jquery', 'module/Editor', 'util/jquery.tmpl'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		Editor = require('module/Editor'),
		page;
	require('util/jquery.tmpl');
	page = {
		data: {
			choices: []
		},
		/**
		 * 编辑页面初始化
		 * @param  {String|jQuery} id 预览区id或者dom对象
		 * @param  {Object} config 扩展参数
		 * @config {jQuery} editArea 编辑区dom
		 * @config {String} submitUrl 提交url
		 * @config {Number} [complete] 题目是否完整
		 * @config {Function} [extend] 扩展请求参数
		 * @config {Function} [submitCall] 提交回调
		 */
		init: function(id, config) {
			var Self = page, previewArea;
			$.extend(Self, config);
			if($.type(id) === 'string') {
				Self.previewArea = $('#' + id);
			} else {
				Self.previewArea = id;
			}
			previewArea = Self.previewArea;
			Self._previewBody = previewArea.find('.preview-body');
			Self._previewAnswer = previewArea.find('.preview-answer');
			Self._previewAnalysis = previewArea.find('.preview-analysis');
			Self._latexBody = previewArea.find('.latex-body');
			Self._latexAnswer = previewArea.find('.latex-answer');
			Self._subject = previewArea.find('.subject');
			Self._questionType = previewArea.find('.question-type');
			Self._grade = previewArea.find('.grade');
			Self._noAnswer = previewArea.find('.no-answer');
			$.template('questionTemplate', Self.tmpl);
			Self.editorBody = new Editor(Self.editArea.find('.edit-body'), {
				submitCall: function() {
					var me = this;
					me.getContent(function(data) {
						if(data.content) {
							me.disable();
							if(data.choices) {
								Self.data.choices = data.choices;
							}
							Self.data.description = data.description;
							Self._previewBody.html(data.content);
							Self._latexBody.html(data.latex);
						} else {
							alert(me.submit.data('title') + '不能为空');
						}
					}, {
						withLatex: 1
					});
				}
			});
			Self.editorAnswer = new Editor(Self.editArea.find('.edit-answer'), {
				submitCall: function(content) {
					var me = this;
					me.getContent(function(data) {
						if(data.content) {
							me.disable();
							Self._previewAnswer.html(data.content);
							Self._latexAnswer.html(data.latex);
						} else {
							alert(me.submit.data('title') + '不能为空');
						}
					}, {
						withLatex: 1
					});
				}
			});
			Self.editorAnalysis = new Editor(Self.editArea.find('.edit-analysis'), {
				submitCall: function(content) {
					var me = this;
					me.getContent(function(data) {
						if(data.content) {
							me.disable();
							Self._previewAnalysis.html(data.content);
						} else {
							alert(me.submit.data('title') + '不能为空');
						}
					});
				}
			});
			Self.addEvent();
		},
		addEvent: function() {
			var Self = page;
			Self.previewArea.on('click', '.photo-frame img', function(e) {
				window.open($(this).attr('src'), 'newwindow', 'alwaysRaised=yes,z-look=yes');
			}).on('click', '.submit', function(e) {
				var data = {}, choiceNum, answerReg, endChar, answers, prevAnswer, i, answerLen;
				// 提交验证
				data.content = Self._previewBody.html();
				if(!data.content) {
					alert('请输入问题描述后再提交');
					return;
				}
				data.realType = Self._questionType.val();
				if(!data.realType) {
					alert("请选择题型后再提交");
					return;
				}
				if(data.realType === '1' || data.realType === '2') {
					choiceNum = Self.data.choices.length;
					if(choiceNum === 0) {
						alert('选择题必须添加选项');
						return;
					}
					data.selectOptionArray = Self.data.choices.map(function(o) {
						return window.encodeURIComponent(o);
					}).join();
					data.selectContent = Self.data.description;
					if(!data.selectContent) {
						alert('选择题必须输入题干');
						return;
					}
				}
				if(Self.complete !== undefined) {
					data.complete = 1;
				} else {
					data.complete = Self._noAnswer.data('answer');
				}
				if(data.complete === 1) {
					data.answer = Self._previewAnswer.html().trim();
					// 答案非空验证
					if(!data.answer) {
						alert('请输入题目解答后再提交');
						return;
					}
					if(data.realType === '1' || data.realType === '2') {
						data.answer = Self._previewAnswer.text().trim();
						// 单选答案验证
						if(data.realType === '1') {
							endChar = String.fromCharCode(64 + choiceNum);
							answerReg = new RegExp('^[A-' + endChar + ']$');
							if(!answerReg.test(data.answer)) {
								alert('单选题只能录入一个答案');
								return;
							}
						} else {
							// 多选答案验证
							endChar = String.fromCharCode(64 + choiceNum);
							answers = data.answer.split('');
							answerLen = answers.length;
							if(answerLen > choiceNum) {
								alert('该多选题最多只能有' + choiceNum + '个答案');
								return;
							}
							prevAnswer = 'A';
							for(i = 0; i < answerLen; ++i) {
								if(prevAnswer > endChar) {
									alert('请输入合法的多选题答案');
									return;
								}
								answerReg = new RegExp('^[' + prevAnswer + '-' + endChar + ']$');
								if(!answerReg.test(answers[i])) {
									alert('请输入合法的多选题答案');
									return;
								}
								prevAnswer = String.fromCharCode(answers[i].charCodeAt(0) + 1);
							}
						}
					}
					// 讲解验证
					data.solution = Self._previewAnalysis.html();
					if(!data.solution) {
						alert('请输入解题思路后再提交');
						return;
					}
					data.answerLatex = Self._latexAnswer.html();
				} else {
					data.answer = '';
					data.answerLatex = '';
					data.solution = '';
				}
				data.subject = Self._subject.val();
				if(!data.subject) {
					alert('请选择学科后再提交');
					return;
				}
				data.realLearnPhase = Self._grade.val();
				if(!data.realLearnPhase) {
					alert('请选择学段后再提交');
					return;
				}
				data.latex = Self._latexBody.html();
				data.knowledge = '知识点';
				data.target = '';
				if(Self.extend) {
					data = Self.extend(data);
				}
				// 额外参数
				$.ajax({
					url: Self.submitUrl,
					type: 'POST',
					data: data,
					dataType: 'json',
					success: function(data) {
						alert(data.msg);
						if(data.status === 0) {
							if(Self.submitCall) {
								Self.submitCall(data);
							}
						}
					},
					error: function() {
						alert("提交异常，请稍后重试！");
					}
				});
			}).on('click', '.no-answer', function(e) {
				var el = $(this);
				if(el.data('answer') === 1) {
					Self._previewAnswer.empty();
					Self.editorAnswer.disable({
						text: '无需题目解答',
						submitStatus: 'disabled'
					});
					Self._previewAnalysis.empty();
					Self.editorAnalysis.disable({
						text: '无需解题思路',
						submitStatus: 'disabled'
					});
					el.text('录入答案').data('answer', 0);
				} else {
					Self.editorAnswer.enable();
					Self.editorAnalysis.enable();
					el.text('不录答案').data('answer', 1);
				}
			});
		}
	};
	return {
		init: page.init
	};
});