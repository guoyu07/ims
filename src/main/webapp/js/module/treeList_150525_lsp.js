/**
 * @description  录题后台首页导航栏
 * @author jansesun(sunjian@xuexibao.cn)
 */
define('module/treeList', ['lib/jquery'], function(require) {
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
	TreeList = function(id,iframe) {
		var Self = this;
		Self.nav = $('#' + id);
		Self.iframe = $('#' + iframe);
		Self.init();
	};
	TreeList.prototype = {
		/**
		* 导航栏初始化
		*/
		init: function() {
			var Self = this;
			Self.nav.on('click', 'a', function(e){
		        if(!$(this).parents(".content:first").hasClass("enlarged")){
					if($(this).parent().hasClass("has_sub")) {
				    	e.preventDefault();
				    }   

			        if(!$(this).hasClass("subdrop")) {
			        	// hide any open menus and remove all other classes
			        	$("ul",$(this).parents("ul:first")).slideUp(350);
			        	$("a",$(this).parents("ul:first")).removeClass("subdrop");
			        	Self.nav.find(".pull-right i").removeClass("fa-chevron-down").addClass("fa-chevron-left");
			        	// open our new menu and add the open class
			        	$(this).next("ul").slideDown(350);
			        	$(this).addClass("subdrop");
			        	$(".pull-right i",$(this).parents(".has_sub:last")).removeClass("fa-chevron-left").addClass("fa-chevron-down");
			        	$(".pull-right i",$(this).siblings("ul")).removeClass("fa-chevron-down").addClass("fa-chevron-left");
			        } else if($(this).hasClass("subdrop")) {
			        	$(this).removeClass("subdrop");
			        	$(this).next("ul").slideUp(350);
			        	$(".pull-right i",$(this).parent()).removeClass("fa-chevron-down").addClass("fa-chevron-left");
			        }
		        }
		        var url = $(this).data("url");
				Self.iframe.attr("src",url);
				e.preventDefault();
		    });
		},
	};
	return TreeList;
});
