/**
 * @description  公式输入框
 * @author lishunping(lishunping@xuexibao.cn)
 */
define('module/FormulaInput', ['lib/jquery', 'util/jquery.markitup', 'module/Dialog'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		dialog = require('module/Dialog'),
		FormulaInput,
		instance,
		basePath = window.basePath || '';
	FormulaInput = function(id) {
		var Self = this;
		if(instance) {
			return instance;
		}
		Self.id = id;
		Self._body = $('#' + Self.id);
		instance = Self;
	};
	FormulaInput.prototype = {
		show: function() {
			var Self = this;
			dialog.show(Self.id, {
				sizeClass: 'modal-lg',
				title: '输入公式',
				content: [
					'<textarea class="latex-editor"></textarea>',
					'<div class="formula-view-frame">',
						'<span class="formula-preview">预览：</span>',
						'<div class="formula-display"></div>',
					'</div>'
				].join(''),
				footer: '<button type="button" class="btn btn-primary btn-confirm">确定</button>',
				source: Self.id,
				initCall: function() {
					var me = this, container;
					container = me._body;
					Self.latexEditor = container.find('.latex-editor');
					Self.markitup();
					Self.formularDisplay = container.find('.formula-display');
					container.on('show.bs.modal', function(e) {
						Self.src = '';
						Self.latex = '';
						Self.timeView = null;
					});
					container.on('hide.bs.modal', function(e) {
						Self.latexEditor.val('');
						Self.formularDisplay.empty();
						$(document).trigger('FormulaInput:hide');
					});
					Self.addEvent();
				},
				confirm: function() {
					if(Self.formularDisplay.html()) {
						$.ajax({
							url: basePath + 'fileupload/uploadRemoteImage',
							type: 'POST',
							data: {
								remoteUrl: Self.src
							},
							dataType: 'json',
							success: function(data) {
								if(data.status === 0) {
									$(document).trigger('FormulaInput:insert', [data.result, Self]);
									Self.latexEditor.val('');
								} else {
									alert(data.msg);
								}
								Self.hide();
							},
							error: function() {
								alert("提交异常，请稍后重试！");
								Self.hide();
							}
						});
					}
				}
			});
		},
		hide: function() {
			dialog.hide();
		},
		viewFormula: function() {
			var Self = this,
				formulaImg;
			Self.latex = Self.latexEditor.val().trim();
			if(Self.latex) {
				formulaImg = '<img alt="预览生成中..." src="http://math.zuoyebao.net/latex/png?tex=' + window.encodeURIComponent(Self.latex) + '" />';
				Self.formularDisplay.html(formulaImg);
				Self.src = Self.formularDisplay.find('img').attr('src');
			}
		},
		markitup: function() {
			var Self = this;
			Self.latexEditor.markItUp({
				onShiftEnter: {
					keepDefault: false,
					replaceWith:'<br />\n'
				},
				onCtrlEnter: {
					keepDefault: false,
					openWith: '\n<p>',
					closeWith: '</p>'
				},
				onTab: {
					keepDefault: false,
					replaceWith: '   '
				},
				afterInsert: function() {
					Self.viewFormula();
				},
				markupSet:  [
					{name:'分数', replaceWith: '\\frac{b}{a} '},
					{name:'平方根', replaceWith: '\\sqrt{ab} '},
					{name:'根号', replaceWith: '\\sqrt[n]{ab} '},
					{name:'上标', replaceWith: 'x^{a} '},
					{name:'下标', replaceWith: 'x_{a} '},
					{name:'上下标', replaceWith: 'x_{a}^{b} '},
					{name:'倒数', replaceWith: '\\dot{x} '},
					{name:'下划线', replaceWith: '\\underline{ab} '},
					{name:'上划线', replaceWith: '\\overline{ab} '},
					{name:'矢量(右)', replaceWith: '\\overrightarrow{ab} '},
					{name:'矢量(左)', replaceWith: '\\overleftarrow{ab} '},
					{name:'矢量', replaceWith: '\\widehat{ab} '},
					{name:'矩阵', replaceWith: '\\begin{bmatrix}a & b \\\\c & d \\end{bmatrix} '},
					{name:'\'', replaceWith: '\\prime '},
					{name:'求和', replaceWith: '\\sum_{x}^{y}{z} '},
					{name:'+', replaceWith: ' + '},
					{name:'-', replaceWith: ' - '},
					{name:'×', replaceWith: '\\times '},
					{name:'÷', replaceWith: '\\div '},
					{name:'不等于', replaceWith: '\\neq '},
					{name:'因为', replaceWith: '\\because '},
					{name:'所以', replaceWith: '\\therefore '},
					{name:'正负', replaceWith: '\\pm '},
					{name:'并集', replaceWith: '\\cup '},
					{name:'交集', replaceWith: '\\cap '},
					{name:'垂直', replaceWith: '\\perp '},
					{name:'无穷', replaceWith: '\\infty '},
					{name:'方程组', replaceWith: '\\begin{cases} {a} \\\\ {b} \\end{cases} '},
					{name:'化学反应方程式', replaceWith: '\\mathop{=\\!=\\!=}^{a}_{b} '},
					{name:'极限', replaceWith: '\\lim_{n \\to \\infty} '},
					{name:'小于等于', replaceWith: '\\leq '},
					{name:'大于等于', replaceWith: '\\geq '},
					{name:'子集', replaceWith: '\\subset '},
					{name:'父集', replaceWith: '\\supset '},
					{name:'子集或等于', replaceWith: '\\subseteq '},
					{name:'父集或等于', replaceWith: '\\supseteq '},
					{name:'包含于', replaceWith: '\\in '},
					{name:'不包含于', replaceWith: '\\ni '},
					{name:'大于', replaceWith: ' > '},
					{name:'小于', replaceWith: ' < '},
					{name:'约等于', replaceWith: '\\approx '},
					{name:'相似', replaceWith: '\\sim '},
					{name:'全等', replaceWith: '\\cong '},
					{name:'上箭头', replaceWith: '\\uparrow '},
					{name:'下箭头', replaceWith: '\\downarrow '},
					{name:'α', replaceWith: '\\alpha '},
					{name:'β', replaceWith: '\\beta '},
					{name:'γ', replaceWith: '\\gamma '},
					{name:'θ', replaceWith: '\\theta '},
					{name:'λ', replaceWith: '\\lambda '},
					{name:'π', replaceWith: '\\pi '},
					{name:'μ', replaceWith: '\\mu '},
					{name:'ρ', replaceWith: '\\rho '},
					{name:'∑', replaceWith: '\\Sigma '},
					{name:'Ω', replaceWith: '\\Omega '},
					{name:'Δ', replaceWith: '\\Delta '},
					{name:'空集', replaceWith: '\\phi '},
					{name:'三角形', replaceWith: '\\triangle '},
					{name:'角', replaceWith: '\\angle '},
					{name:'圆', replaceWith: '\\odot '}
				]
			});
		},
		/**
		* 绑定事件
		*/
		addEvent: function() {
			var Self = this;
			Self._body.on('click', '.formula-preview', function(e) {
				Self.viewFormula();
			}).on('keyup', 'textarea', function(e) {
				if (Self.timeView) {
					clearTimeout(Self.timeView);
				}
				Self.timeView = setTimeout(function() {
					Self.viewFormula();
				}, 500);
			});
		}
	};
	return {
		show: function(id, editorArea) {
			var placeholder;
			editorArea.focus();
			document.execCommand('insertimage', false, basePath + 'image/exam/formula.png');
			placeholder = editorArea.find('img[src$="image/exam/formula.png"]').addClass('formula');
			$(document).one('FormulaInput:insert', function(e, imageInfo, Self) {
				placeholder.attr({
					'src': imageInfo.imageUrl,
					'data-width': imageInfo.width,
					'data-height': imageInfo.height,
					'data-latex': Self.latex
				}).data({
					img: 1
				});
			}).one('FormulaInput:hide', function(e) {
				if(!placeholder.data('img')) {
					placeholder.remove();
					// 手动释放内存
					placeholder = null;
				}
			});
			(new FormulaInput(id)).show();
		}
	};
});