define('module/BookInfo', ['lib/jquery', 'util/jquery.tmpl', 'module/Dialog'], function(require) {
	'use strict';
	var querySource, BookInfo, $, dialog, sourceTmpl, bookInfoTmpl;
	$ = require('lib/jquery');
	require('util/jquery.tmpl');
	dialog = require('module/Dialog');
	/**
	 * 查询来源
	 * @param  {Object} config 配置参数
	 * @config {String} url 相对请求地址，默认为'book/organizationSourcesList'
	 * @config {Object} data 请求参数
	 * @config {Function} callback 回调函数
	 * @example
	 * 		querySource({
	 * 			data: {
	 * 				name: '真金教育'
	 * 			},
	 * 			callback: function(data) {
	 * 				//codes go here
	 * 			}
	 * 		});
	 */
	querySource = function(config) {
		$.ajax({
			url: basePath + (config.url || 'book/organizationSourcesList'),
			type: 'get',
			dataType: 'json',
			data: config.data,
			success: function(data) {
				var html = [];
				if(data.status == 0) {
					if(config.callback) {
						config.callback(data);
					}
				} else {
					alert(data.msg);
				}
			}
		});
	};
	// 来源模板
	sourceTmpl = [
		'<div class="col-lg-6">',
			'<div class="input-group">',
				'<span class="input-group-addon">',
					'<input type="radio" name="source" data-id="${id}">',
				'</span>',
				'<span class="form-control">${name}</span>',
			'</div>',
		'</div>'
	].join('');
	$.template('sourceTemplate', sourceTmpl);
	// 书籍模板
	bookInfoTmpl = [
		'<div class="modal-add-book">',
			'<div class="input-group">',
				'<span class="input-group-addon">书名</span>',
				'<span class="form-control">${name}</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">ISBN</span>',
				'<span class="form-control">${isbn}</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">年级</span>',
				'<span class="form-control">${grade}</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">科目</span>',
				'<span class="form-control">${subject}</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">出版社</span>',
				'<span class="form-control">${publishingHouse}</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">来源</span>',
				'<input type="text" class="form-control query-name" placeholder="输入要查询的来源名称" style="width:87%;margin-right:9px">',
				'<button type="button" class="btn btn-primary query-btn">查询</button>',
			'</div>',
			'<div class="row query-result"></div>',
		'</div>'
	].join('');
	$.template('bookInfoTemplate', bookInfoTmpl);
	BookInfo = {
		/**
		 * 添加书籍
		 * @param {String} source 来源id
		 * @param {Object} config 配置参数
		 * @config {Function} callback 回调函数
		 * @example
		 * 		seajs.use(['module/BookInfo'], function(bookInfo) {
		 * 			bookInfo.addBook(source, {
		 * 				callback: function(data) {
		 * 				}
		 * 			});
		 * 		});
		 */
		addBook: function(source, config) {
			dialog.init('modal-dialog', {
				sizeClass: 'modal-lg',
				title: '添加书籍',
				content: [
					'<div class="modal-add-book">',
						'<div class="input-group">',
							'<span class="input-group-addon">书名</span>',
							'<input type="text" class="form-control book-name" placeholder="请填写书名，必填，最多30个字" maxlength="30" required="required">',
						'</div>',
						'<div class="input-group">',
							'<span class="input-group-addon">ISBN</span>',
							'<input type="text" class="form-control book-isbn" placeholder="仅输入数字" value="9787">',
						'</div>',
						'<div class="input-group">',
							'<span class="input-group-addon">年级</span>',
							'<select class="form-control book-grade">',
								'<option>小学</option>',
								'<option>初中</option>',
								'<option>高中</option>',
								'<option>其他</option>',
							'</select>',
						'</div>',
						'<div class="input-group">',
							'<span class="input-group-addon">科目</span>',
							'<select class="form-control book-subject">',
								'<option>数学</option>',
								'<option>语文</option>',
								'<option>英语</option>',
								'<option>物理</option>',
								'<option>化学</option>',
								'<option>生物</option>',
								'<option>政治</option>',
								'<option>历史</option>',
								'<option>地理</option>',
								'<option>文综</option>',
								'<option>理综</option>',
								'<option>其他</option>',
							'</select>',
						'</div>',
						'<div class="input-group">',
							'<span class="input-group-addon">出版社</span>',
							'<input type="text" class="form-control book-publishing-house" placeholder="请填写书籍出版社，必填，最多30个字" maxlength="30" required="required">',
						'</div>',
						'<div class="input-group">',
							'<span class="input-group-addon">来源</span>',
							'<input type="text" class="form-control query-name" placeholder="输入要查询的来源名称" style="width:87%;margin-right:9px">',
							'<button type="button" class="btn btn-primary query-btn">查询</button>',
						'</div>',
						'<div class="row query-result"></div>',
					'</div>'
				].join(''),
				source: source,
				initCall: function() {
					var Self = this, container, queryName, queryResult;
					container = Self._container;
					queryName = container.find('.query-name');
					queryResult = container.find('.query-result');
					Self._confirm.text('添加书籍');
					Self._body.on('click.bs.custom', '.query-btn', function(e) {
						var el = $(this), data = {};
						data.name = queryName.val().trim();
						if(!data.name) {
							queryName.val('').focus();
							alert('请输入有效的来源名称');
							return;
						}
						querySource({
							data: data,
							callback: function(data) {
								queryResult.html($.tmpl('sourceTemplate', data.result));
							}
						});
					});
				},
				confirm: function(e) {
					var Self = this,
						container = Self._container, data = {},
						name, isbn, publishingHouse;
					name = container.find('.book-name');
					data.name = name.val().trim();
					if(!data.name) {
						alert('书名不能为空');
						Self.enableConfirm();
						name.val('').focus();
						return;
					}
					publishingHouse = container.find('.book-publishing-house');
					data.publishing_house = $.trim(publishingHouse.val());
					if(!data.publishing_house) {
						alert('出版社不能为空');
						Self.enableConfirm();
						publishingHouse.val('').focus();
						return;
					}
					isbn = container.find('.book-isbn');
					data.isbn = isbn.val().trim();
					// 国内图书ISBN编号前四位为9787
					data.isbn = /^9787\d+$/.test(data.isbn) ? data.isbn : '';
					data.source_id = container.find('.query-result :checked').data('id');
					if(!(data.isbn || data.source_id)) {
						Self.enableConfirm();
						alert('ISBN或者来源至少填写其中一项');
						isbn.val('9787').focus();
						return;
					}
					data.subject = container.find('.book-subject :selected').text();
					data.grade = container.find('.book-grade :selected').text();
					$.ajax({
						url: window.basePath + 'book/addBookInfo',
						type: 'GET',
						dataType: 'json',
						data: data,
						success: function(data) {
							if(data.status === 0) {
								if(config.callback) {
									config.callback(data.result);
								}
								Self.hide();
							} else {
								Self._confirm.prop('disabled', false);
								alert(data.msg);
							}
						}
					});
				}
			});
		},
		/**
		 * 添加来源
		 * @param {Object} bookData 书籍数据
		 * @param {Object} config 配置参数
		 * @config {Function} callback 回调函数
		 * @example
		 * 		seajs.use(['module/BookInfo'], function(bookInfo) {
		 * 			bookInfo.addSource2Book({
		 * 				id: '123'
		 * 			}, {
		 * 				callback: function(data) {
		 * 				}
		 * 			});
		 * 		});
		 */
		addSource2Book: function(bookData, config) {
			dialog.init('modal-dialog', {
				sizeClass: 'modal-lg',
				title: '添加来源',
				content: $.tmpl('bookInfoTemplate', bookData),
				force: 1,
				initCall: function() {
					var Self = this, container, queryName, queryResult;
					container = Self._container;
					queryName = container.find('.query-name');
					queryResult = container.find('.query-result');
					Self._confirm.text('添加来源');
					Self._body.on('click.bs.custom', '.query-btn', function(e) {
						var el = $(this), data = {};
						data.name = queryName.val().trim();
						if(!data.name) {
							alert('请输入有效的来源名称');
							return;
						}
						querySource({
							data: data,
							callback: function(data) {
								queryResult.html($.tmpl('sourceTemplate', data.result));
							}
						});
					});
				},
				confirm: function(e) {
					var Self = this,
						container = Self._container, data = {};
					data.source_id = container.find('.query-result :checked').data('id');
					if(!data.source_id) {
						alert('请选择要添加的来源');
						Self.enableConfirm();
						return;
					}
					data.id = bookData.id || ''
					$.ajax({
						url: window.basePath + 'book/addBookSourceInfo',
						type: 'GET',
						dataType: 'json',
						data: data,
						success: function(data) {
							if(data.status === 0) {
								if(config.callback) {
									config.callback(data.result);
								}
								Self.hide();
							} else {
								Self.enableConfirm();
								alert(data.msg);
							}
						}
					});
				}
			});
		}
	};
	return BookInfo;
});