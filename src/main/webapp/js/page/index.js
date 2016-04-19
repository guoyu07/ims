/**
 * @description  录题后台首页
 * @author jansesun(sunjian@xuexibao.cn)
 */
define('page/index', ['lib/jquery', 'util/jquery.tmpl', 'module/TreeList'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		TreeList = require('module/TreeList'),
		page;
	// 引入jquery.tmpl
	require('util/jquery.tmpl');
	page = {
		/**
		 * 页面初始化
		 * @param  {String} id     树形导航id
		 * @param  {Object} [config] 配置参数
		 * @config {String} tmpl 	模板id
		 * @example
		 * 		seajs.use(['page/index'], function(page) {
		 * 			page.init('nav', {
		 * 				tmpl: 'tree-list-tmpl'
		 * 			});
		 * 		});
		 */
		init: function(id, config) {
			var Self = page, width, treeList;
			config = config || {
				tmpl: 'tree-list-tmpl',
				container: 'nav-container'
			};
			Self.container = $('#' + config.container);
			Self.nav = $('#' + id);
			width = $(window).width();
			$('body').toggleClass('mobilescreen',  width < 765);
			if(width < 765) {
				Self.nav.slideUp(350);
			} else {
				Self.nav.slideDown(350);
			}
			treeList = new TreeList(id);
			Self.addEvent();
		},
		/**
		 * [addEvent description]
		 */
		addEvent: function() {
			var Self = page;
			// 登录信息展开收起
			$(document.body).on('click', '.dropdown-toggle', function(e) {
				var el = $(this);
				el.closest('.' + el.data('toggle')).toggleClass('open');
				e.preventDefault();
			});
			// 列表收起展开
			$("#menu-btn").click(function(e) {
				/**
				 * .enlarged .fa-chevron-left:before, .active .fa-chevron-left:before {
				 * 	content: '\f078'
				 * }
				 * .enlarged .sub-nav {
				 * 	display: block!important;
				 * }
				 */
				e.preventDefault();
				Self.container.toggleClass('enlarged');
				Self.nav.find('.has-sub').toggleClass('expanded');
			});
		}
	};
	return {
		init: page.init
	};
});