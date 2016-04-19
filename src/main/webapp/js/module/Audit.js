define('module/Audit', ['lib/jquery'], function(require) {
	'use strict';
	var Audit,
		$ = require('lib/jquery');
	Audit = {
		/**
		 * 默认参数
		 * @type {Object}
		 */
		defaults: {
			result: 'result',
			auditZone: 'audit-zone',
			submitZone: 'submit-zone',
			acceptVal: 2,
			noReason: false
		},
		/**
		 * 审核初始化
		 * @param {String} questionId 题目id
		 * @param {Object} config 扩展参数
		 * @config {String} [result='result'] 结果展示区id
		 * @config {String} [auditZone='audit-zone'] 审核区id
		 * @config {String} [submitZone='submit-zone'] 提交区id
		 * @config {Number} [acceptVal=2] 通过值
		 * @config {Boolean} [noReason=false] 是否需要原因
		 * @config {String} url 请求地址
		 * @config {Function} confirm 确认回调函数
		 * @config {Function} [reset] 重置回调函数
		 * @example
		 * 		seajs.use('module/Audit', function(audit) {
		 * 			audit.init('1234', {
		 * 				acceptVal: 3,
		 * 				url: '',
		 * 				confirm: function(data) {
		 * 				}
		 * 			});
		 * 		});
		 */
		init: function(questionId, config) {
			var Self = Audit;
			config = config || {};
			config = $.extend(Self.defaults, config);
			Self.questionId = questionId;
			Self.confirm = config.confirm;
			Self.reset = config.reset;
			Self.url = config.url;
			Self.acceptVal = config.acceptVal;
			Self.noReason = config.noReason;
			Self._result = $('#' + config.result);
			Self._auditZone = $('#' + config.auditZone);
			Self._submitZone = $('#' + config.submitZone);
			Self.addEvent();
		},
		/**
		 * 绑定事件
		 */
		addEvent: function() {
			var Self = Audit,
				result = Self._result,
				_accept = result.find('.accept'),
				_reject = result.find('.reject'),
				_reason = result.find('.reason'),
				_reasonTxt = result.find('.reason-txt');
			// 绑定审核单选点击事件
			Self._auditZone.on('click', 'input', function(e) {
				var el = $(this), hasAccept;
				if(el.prop('checked')) {
					hasAccept = +el.val() === Self.acceptVal;
					// 展示结果
					_accept.toggle(hasAccept);
					_reject.toggle(!hasAccept);
					// 收起原因输入框
					if(!Self.noReason) {
						_reasonTxt.val('').hide();
					}
				}
			});
			// 绑定原因改变事件
			_reason.on('change', function(e) {
				var el = $(this), val;
				val = +el.find('option:selected').val();
				// 隐藏显示原因输入框
				_reasonTxt.val('').toggle(val === 5);
			});
			// 绑定提交事件
			Self._submitZone.on('click', '.confirm-btn', function(e) {
				var data = {
					questionIds: Self.questionId
				}, el;
				el = $(this).prop('disabled', true);
				data.status = +Self._auditZone.find('input:checked').val();
				if(!Self.noReason && data.status !== Self.acceptVal) {
					data.reason = +_reason.find('option:selected').val();
					if(data.reason === 5) {
						data.reasonStr = _reasonTxt.val().trim();
					}
				}
				$.ajax({
					url: Self.url,
					type: 'GET',
					data: data,
					dataType: 'json',
					success: function(data) {
						if(data.status === 0) {
							if(Self.confirm) {
								Self.confirm(data);
							}
						} else {
							alert(data.msg);
							if(Self.error) {
								Self.error();
							}
							el.prop('disabled', false);
						}
					},
					error: function() {
						alert('网络错误请稍后再试！');
						el.prop('disabled', false);
					}
				});
				// 绑定重置事件
			}).on('click', '.reset-btn', function(e) {
				var el = $(this).prop('disabled', true);
				$.ajax({
					url: Self.url,
					type: 'GET',
					data: {
						questionIds: Self.questionId,
						status: 0
					},
					dataType: 'json',
					success: function(data) {
						if(data.status === 0) {
							if(Self.reset) {
								Self.reset();
							}
						} else {
							el.prop('disabled', false);
							alert(data.msg);
						}
					},
					error: function() {
						alert('网络错误请稍后再试！');
						el.prop('disabled', false);
					}
				});
			});
		}
	};
	return Audit;
});