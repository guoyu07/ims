/**
 * @description  录题后台登录
 * @author jansesun(sunjian@xuexibao.cn)
 */
define('page/login', ['lib/jquery'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		Login;
	/**
	* @constructor 登陆
	* @param {String} id 登陆表单id
	* @param {Object} [config] 配置参数
	* @config {String} alertTip 提示信息
	* @example
	*   seajs.use('page/login', function(Login) {
	*       new Login('login_form', {
	*           alertTip: '亲，用户名和密码都得要哦'
	*       });
	*   })
	*/
	Login = function(id, config) {
		var Self = this;
		Self._body = $('#' + id);
		Self = $.extend(Self, config);
		Self.init();
		Self.addEvent();
	};
	Login.prototype = {
		// 提示文案
		alertTip: '用户名和密码不能为空',
		/**
		* 初始化入口
		*/
		init: function() {
			this._account = this._body.find('.account').focus();
			this._password = this._body.find('.password');
		},
		/**
		* 绑定事件
		*/
		addEvent: function() {
			var Self = this;
			$(document).on('keydown', function(e) {
				if(13 === e.keyCode) {
					Self.submit();
				}
			});
			Self._body.on('click', '.confirm', function(e) {
				e.preventDefault();
				Self.submit();
			}).on('click', '.reset', function(e) {
				Self._account.val('').focus();
				Self._password.val('');
			});
		},
		/**
		* 表单提交
		*/
		submit: function() {
			var Self = this;
			if(Self.validate()) {
				$.ajax({
					url: window.basePath + 'login/userLogin',
					type: 'POST',
					data: {
						loginId: Self._account.val().trim(),
						password: Self._password.val()
					},
					dataType: 'json',
					success: function(data) {
						if(data.status === 0) {
							document.location.href = data.result[0].url;
						} else {
							alert(data.msg);
						}
					}
				});
			}
		},
		/**
		* 表单验证
		*/
		validate: function() {
			// 用户名和密码不能为空
			if(this._account.val() && this._password.val()) {
				return true;
			}
			alert(this.alertTip);
		}
	};
	return Login;
});