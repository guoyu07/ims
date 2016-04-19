define('module/Dialog', ['lib/jquery', 'lib/bootstrap'], function(require) {
	'use strict';
	var Dialog, $;
	$ = require('lib/jquery');
	require('lib/bootstrap');
	Dialog = {
		/**
		 * 初始化对话框
		 * @param  {String|jQuery} id 对话框DOM对象或者id
		 * @param  {Object} config 配置参数
		 * @config {String} sizeClass 尺寸类型 modal-sm modal-lg
		 * @config {String} title 标题
		 * @config {String} content 主体内容
		 * @config {String} [footer] 底部按钮
		 * @config {Boolean} force 强制渲染
		 * @config {String} source 来源绑定用以防止重复渲染
		 * @config {Function} confirm 确认回调函数
		 * @config {Function} cancel 取消回调函数
		 * @config {Function} close 关闭回调函数
		 * @config {Function} initCall 初始化回调函数
		 * @example
		 * 		seajs.use(['module/Dialog'], function(dialog) {
		 * 			dialog.init('modal-dialog', {
		 * 				sizeClass: 'modal-sm',
		 * 				title: '',
		 * 				content: '',
		 * 				source: '',
		 * 				initCall: function() {
		 * 					var Self = this;
		 * 					// 修改确认文案
		 * 					// 绑定内部事件
		 * 					// 防止重复绑定事件
		 * 					// 通过Self._body绑定事件代理'click.bs.custom', 以便于逻辑清除
		 * 					Self._body.on('click.bs.custom', '', function(e) {
		 * 					});
		 * 				},
		 * 				confirm: function() {
		 * 				}
 		 * 			});
		 * 		});
		 */
		init: function(id, config) {
			var Self = Dialog, isRender;
			if($.type(id) === 'string') {
				Self._body = $('#' + id);
			} else {
				Self._body = id;
			}
			config = config || {};
			// 默认浮层底部
			config.footer = config.footer || [
				'<button type="button" class="btn btn-default btn-cancel" data-dismiss="modal">取消</button>',
				'<button type="button" class="btn btn-primary btn-confirm">保存</button>'
			].join('');
			// 扩展参数
			$.extend(Self, config);
			// 防止无效重复渲染
			if(Self.force) {
				Self.source = '';
			}
			Self._dialog = Self._body.find('.modal-dialog');
			Self._title = Self._body.find('.modal-title');
			Self._container = Self._body.find('.modal-body');
			Self._footer = Self._body.find('.modal-footer');
			isRender = Self.force || Self._body.data('source') !== Self.source;
			if(isRender) {
				Self._body.data('source', Self.source)
					.off('click.bs.custom');
				Self._title.html(Self.title);
				Self._container.html(Self.content);
				Self._footer.html(Self.footer);
				Self._dialog.removeClass('modal-lg, modal-sm').addClass(Self.sizeClass);
			}
			Self._confirm = Self._footer.find('.btn-confirm');
			if(isRender) {
				if(config.initCall) {
					config.initCall.call(Self);
				}
				Self.addEvent();
			}
		},
		/**
		 * 绑定事件
		 */
		addEvent: function() {
			var Self = Dialog;
			// 防止重复绑定
			if(!Self._body.data('init')) {
				Self._body.data('init', 1);
				// 展示时激活确认按钮
				Self._body.on('show.bs.modal', function(e) {
					Self.enableConfirm();
				});
				// 确认事件代理
				Self._footer.on('click.bs.modal', '.btn-confirm', function(e) {
					// 防止重复点击
					$(this).prop('disabled', true);
					if(Self.confirm) {
						Self.confirm();
					}
				});
				// 取消事件代理
				Self._footer.on('click.bs.modal', '.btn-cancel', function(e) {
					if(Self.cancel) {
						Self.cancel();
					}
				});
			}
		},
		/**
		 * 展示对话框
		 * @param  {String|jQuery} id 对话框DOM对象或者id
		 * @param  {Object} config 配置参数
		 * @config {String} sizeClass 尺寸类型 modal-sm modal-lg
		 * @config {String} title 标题
		 * @config {String} content 主体内容
		 * @config {String} [footer] 底部按钮
		 * @config {Boolean} force 强制渲染
		 * @config {String} source 来源绑定用以防止重复渲染
		 * @config {Function} confirm 确认回调函数
		 * @config {Function} cancel 取消回调函数
		 * @config {Function} close 关闭回调函数
		 * @config {Function} initCall 初始化回调函数
		 * @example
		 * 		seajs.use(['module/Dialog'], function(dialog) {
		 * 			dialog.show('modal-dialog', {
		 * 				sizeClass: 'modal-sm',
		 * 				title: '',
		 * 				content: '',
		 * 				source: '',
		 * 				initCall: function() {
		 * 					var Self = this;
		 * 					// 修改确认文案
		 * 					// 绑定内部事件
		 * 					// 防止重复绑定事件
		 * 					// 通过Self._body绑定事件代理'click.bs.custom', 以便于逻辑清除
		 * 					Self._body.on('click.bs.custom', '', function(e) {
		 * 					});
		 * 				},
		 * 				confirm: function() {
		 * 				}
 		 * 			});
		 * 		});
		 */
		show: function(id, config) {
			var Self = Dialog;
			Self.init(id, config);
			Self._body.modal('show');
		},
		/**
		 * 隐藏对话框
		 * @example
		 * 		Self.hide();
		 */
		hide: function() {
			var Self = this;
			Self._body.modal('hide');
		},
		/**
		 * 激活确认按钮
		 * @example
		 * 		Self.enableConfirm();
		 */
		enableConfirm: function() {
			var Self = this;
			Self._confirm.prop('disabled', false);
		}
	};
	return Dialog;
});