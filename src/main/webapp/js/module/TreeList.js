/**
 * @description  录题后台首页导航栏
 * @author jansesun(sunjian@xuexibao.cn)
 */
define('module/TreeList', ['lib/jquery'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		TreeList;
	/**
	* @constructor 导航栏
	* @param {String} id 导航栏id
	* @param {String} iframe 展示具体内容的iframe
	* @example
	*   seajs.use('page/login', function(Login) {
	*       new TreeList('nav', 'i-frame');
	*   })
	*/
	TreeList = function(id) {
		var Self = this;
		Self._body = $('#' + id);
		Self.init();
	};
	TreeList.prototype = {
		/**
		* 导航栏初始化
		*/
		init: function() {
			var Self = this;
			Self.addEvent();
		},
		/**
		* 绑定事件
		*/
		addEvent: function() {
			var Self = this;
			Self._body.on('click', '.nav-link', function(e) {
				var url = $(this).data('url');
				$('#i-frame').attr('src', url);
				e.preventDefault();
			});
			Self._body.on('click', '.expanded .nav-has-sub', function(e) {
				var isSelf = true, item = $(this).closest('li'), activeItem;
				e.preventDefault();
				if(!item.hasClass('active')) {
					isSelf = false;
				}
				activeItem = Self._body.find('.active');
				activeItem.find('.sub-nav').slideUp(350);
				activeItem.removeClass('active');
				if(!isSelf) {
					item.addClass('active');
					item.find('.sub-nav').slideDown(350);
				}
			});
		},
	};
	return TreeList;
});
