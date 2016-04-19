/**
 * @description  题库编辑器
 * @author lishunping(lishunping@xuexibao.cn)
 */
define('module/Editor', ['lib/jquery', 'util/jquery.tmpl'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		Editor,
		latexContainer = $('<div>'),
		basePath = window.basePath || '',
		doc = document;
	require('util/jquery.tmpl');
	Editor = function(id, config) {
		var Self = this;
		config = config || {};
		if($.type(id) === 'string') {
			Self._body = $('#' + id);
		} else {
			Self._body = id;
		}
		$.extend(Self, config);
		Self.init();
	};
	Editor.prototype = {
		tmpl: [
			'{{html description}}',
			'<table>',
				'<tbody>',
					'{{each(i, choice) choices}}',
					'{{if i % num === 0}}',
					'<tr>',
					'{{/if}}',
						//均匀布局
						'<td style="width:${100 / num}%">${String.fromCharCode(65 + i)}.{{html choice}}</td>',
					'{{if i % num === num - 1 || i === choices.length}}',
					'</tr>',
					'{{/if}}',
					'{{/each}}',
				'</tbody>',
			'</table>'
		].join(''),
		init: function() {
			var Self = this;
			Self.editContainer = Self._body.find('.edit-border');
			Self.area = Self.editContainer.find('.edittable');
			Self.submit = Self._body.find('.submit-btn');
			Self.curEditArea = Self.area;
			$.template('questionTemplate', Self.tmpl);
			Self.addEvent();
		},
		enable: function() {
			var Self = this;
			Self._body.data('status', 'edit');
			Self.editContainer.children().show();
			Self.submit.text('修改' + Self.submit.data('title')).removeClass('submit-preview').data('status', '');
		},
		disable: function(config) {
			var Self = this;
			config = config || {};
			if(config.text === undefined) {
				config.text = '修改' + Self.submit.data('title');
			}
			Self._body.data('status', 'preview');
			Self.editContainer.children().hide();
			Self.submit.text(config.text).addClass('submit-preview');
			if(config.submitStatus) {
				Self.submit.data('status', config.submitStatus);
			}
		},
		getContent: function(callback, config) {
			var Self = this, generateContent;
			config = config || {};
			generateContent = function() {
				var choiceList, data = {};
				data.description = Self.area.html();
				choiceList = Self.editContainer.find('.choice-item');
				if(choiceList[0]) {
					data.choices = choiceList.map(function() {
						return $(this).html();
					}).get();
					data.description = data.description;
					data.content = $.tmpl('questionTemplate', {
						description: data.description,
						choices: data.choices,
						num: +Self._body.find('.choice-num').val().trim() || 1
					});
					data.content = latexContainer.html(data.content).html();
				} else {
					data.content = data.description;
				}
				if(config.withLatex) {
					if(data.content) {
						latexContainer.html(data.content).find('.formula').each(function() {
							var formula = $(this);
							formula.after('<span class="latex">' + formula.data('latex') + '</span>');
						});
						data.latex = latexContainer.text();
					} else {
						data.latex = '';
					}
				}
				callback(data);
			};
			if(config.noRemote) {
				generateContent();
			} else {
				require.async('util/localizeImg', function(localizeImg) {
					localizeImg(Self.editContainer.find('img'), function() {
						generateContent();
					});
				});
			}
		},
		/**
		* 绑定事件
		*/
		addEvent: function() {
			var Self = this, _body;
			_body = Self._body;
			Self.area.on('focus', function(e) {
				Self.curEditArea = $(this);
			});
			_body.find('.choice-item').on('focus', function(e) {
				Self.curEditArea = $(this);
			});
			_body.on('click', '.tool-btn', function(e) {
				var el = $(this), command, param;
				if(_body.data('status') === 'preview') {
					// 阻止后序事件
					e.stopImmediatePropagation();
					alert('当前编辑窗口为不可编辑状态！');
					return;
				}
				command = el.data('command');
				if(command) {
					param = el.data('param');
					param = param || null;
					doc.execCommand(command, false, param);
				}
			}).on('click', '.insert-img', function(e) {
				require.async('module/ImgInput', function(imgInput) {
					imgInput.show('image-input', Self.curEditArea);
				});
			}).on('click', '.insert-formula', function(e) {
				require.async('module/FormulaInput', function(formulaInput) {
					formulaInput.show('formula-input', Self.curEditArea);
				});
			}).on('click', '.add-choice', function(e) {
				// 添加选项
				var choiceList = Self.editContainer.find('.choice-list'), choice, hasList;
				hasList = !!choiceList[0];
				if(!hasList) {
					choiceList = $('<ol class="choice-list"></ol>');
				}
				choice = $('<li class="choice-item" contenteditable></li>').appendTo(choiceList);
				if(!hasList) {
					Self.editContainer.append(choiceList);
				}
				// 自动聚焦
				choice.on('focus', function(e) {
					Self.curEditArea = $(this);
				}).focus();
			}).on('dblclick', '.choice-item', function(e) {
				var el = $(this);
				if(confirm('确定要删除该选项？')) {
					el.off('focus');
					if(el.siblings().length === 0) {
						el.parent().remove();
					} else {
						el.remove();
					}
				}
			}).on('click', '.submit-btn', function(e) {
				var el = $(this),
					title = el.data('title'),
					content;
				if(_body.data('status') === 'preview') {
					Self.enable();
				} else {
					if(Self.submitCall) {
						Self.submitCall();
					}
				}
			});
		}
	};
	return Editor;
});