 /**
  * @description 选择最佳题目
  * @author sunjian(sunjian@xuexibao.cn)
  */
'use strict';
(function($){
	var Select, singleSelect,
		similarData = window.similarList.slice(0),
		map = {},
		finishBtn = $('#finish'),
		similarElement,
		requestXhr;
	window.similarList.forEach(function(o, i) {
		map[o.questionId] = i;
	});
	/**
	 * @constructor 自定义选择
	 * @param {String|jQuery|DOM} id 选择container id
	 * @param {Object} config 扩展参数
	 * @config {String} item 选项选择器
	 * @config {Function} clickEvent 点击事件处理
	 * @config {String} selected 选中态类名
	 */
	Select = function(id, config) {
		var Self = this;
		config = config || {};
		Self._body = $(id);
		Self.selected = 'selected';
		Self.item = '.item';
		Self.callback = function() {
			Self._items = Self._items || Self._body.find(Self.item);
			return Self._items.filter('.' + Self.selected).map(function(i, o) {
				return $(o).data('id');
			}).toArray();
		};
		Self = $.extend(Self, config);
		Self._body.on('click', Self.item, function() {
			Self.clickEvent(this, Self);
		});
	};
	requestXhr = function(data, config) {
		$.ajax({
			url: window.basePath + 'markDupPic/bestSelect',
			data: data,
			type: 'POST',
			dataType: 'json',
			success: function(data) {
				if(data.status == 0) {
					window.location.reload(1);
				} else {
					alert(data.msg);
					window.location.reload(1);
				}
			}
		});
	};
	// 单选
	singleSelect = new Select('#single-select', {
		clickEvent: function(item, Self) {
			Self._items = Self._items || Self._body.find(Self.item);
			Self._items.removeClass(Self.selected);
			$(item).addClass(Self.selected);
			finishBtn.prop('disabled', false);
		},
		callback: function() {
			var Self = this, markResult = {};
			Self._items = Self._items || Self._body.find(Self.item);
			markResult.groupId = similarData[0].groupId;
			markResult.bestQuestionId = Self._items.filter('.' + Self.selected).data('id');
			return markResult;
		}
	});
	similarElement = $('#select-best-tmpl').tmpl({list: similarData});
	if(similarElement.length) {
		singleSelect._body.empty().append(similarElement);
	}
	// 完成事件处理
	finishBtn.on('click', function() {
		$(this).prop('disabled', true);
		requestXhr(singleSelect.callback());
	});
}(jQuery));