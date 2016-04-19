 /**
  * @description 标记重复，选择最佳题目
  * @author sunjian(sunjian@xuexibao.cn)
  */
'use strict';
(function($){
	var Select, multiSelect,
		reference,
		similarList,
		element, map = {},
		noSimilarBtn = $('#no-similar'),
		nextBtn = $('#next'),
		nextGroupBtn = $('#next-group'),
		block = window.block,
		container = $('<div>'),
		requestXhr;
	$.each(window.similarList, function(i, o) {
		o.content = container.html(o.content).html();
	});
	reference = window.similarList[0],
	similarList = window.similarList.slice(1),
	window.similarList.forEach(function(o, i) {
		map[o.questionId] = i;
	});
	
	//下一组
	nextGroupBtn.on('click', function() {
		$.ajax({
			url: window.basePath + 'mark/assignNewBlock',
			type: 'GET',
			dataType: 'json',
			success: function(data) {
				if(data.status == 0) {
					window.location.reload();
				} else {
					alert(data.msg);
//					window.location.reload(1);
				}
			}
		});
	});	
	
	if(!reference) {
		noSimilarBtn.prop('disabled', true);
		return;
	}
	$('#reference').html(reference.content);
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
		data = data || {};
		data.baseId = reference.base_id;
		data.block  = block;
		$.ajax({
			url: window.basePath + 'mark/updateOneDupGroup',
			data: data,
			type: 'POST',
			dataType: 'json',
			success: function(data) {
				if(data.status == 0) {
					window.location.reload();
				} else {
					alert(data.msg);
//					window.location.reload();
				}
			}
		});
	};
	// 复选
	multiSelect = new Select('#multi-select', {
		clickEvent: function(item, Self) {
			$(item).toggleClass(Self.selected);
			nextBtn.prop('disabled', !Self._body.find('.' + Self.selected)[0]);
		}
	});
	// 初始化重复标记页面
	element = $('#mark-repeat-tmpl').tmpl({list: similarList});
	if(element.length) {
		multiSelect._body.empty().append(element);
	}
	// 下一步事件处理
	nextBtn.on('click', function() {
		$(this).prop('disabled', true);
		// 通过回调函数获得选中节点对应的题目id
		requestXhr({
			dupQuestionId: multiSelect.callback().join()
		});
	});
	// 无重复结果事件处理
	$('#no-similar').on('click', function() {
		if(window.confirm('确认没有与样题重复题目？')) {
			requestXhr({
				dupQuestionId: ''
			});
		}
	});
}(jQuery));