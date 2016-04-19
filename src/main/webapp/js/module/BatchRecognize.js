define('module/BatchRecognize', ['lib/jquery', 'module/Dialog', 'util/jquery.tmpl', 'util/uploadFile'], function(require) {
	'use strict';
	var bookList,
		$ = require('lib/jquery'),
		dialog = require('module/Dialog'),
		uploader = require('util/uploadFile');
	require('util/jquery.tmpl');
	// 图书列表模板
	bookList = [
		'<div class="col-lg-6">',
			'<div class="input-group">',
				'<span class="input-group-addon">',
					'<input type="radio" name="book" data-id="{{= id}}">',
				'</span>',
				'<span class="form-control modal-mutil-line">书名: {{= name}}<br/>出版:{{= publishingHouse}}<br/>ISBN:{{= isbn}}</span>',
			'</div>',
		'</div>'
	].join('');
	$.template('bookListTemplate', bookList);
	/**
	 * 批量识别
	 * @param  {id} source 点击id
	 * @example
	 * 		seajs.use(['module/BatchRecognize'], function(batchRecognize) {
	 * 			batchRecognize('');
	 * 		});
	 */
	return function(source) {
		dialog.show('modal-dialog', {
			sizeClass: 'modal-lg',
			title: '识别图片',
			content: [
				'<div class="modal-add-book">',
					'<div class="modal-guide">选择要识别的书籍:</div>',
					'<div class="input-group modal-nav">',
						'<input type="text" class="form-control query-name" placeholder="输入要书名或ISBN查询" style="width:87%;margin-right:9px">',
						'<button type="button" class="btn btn-primary query-btn">查询</button>',
					'</div>',
					'<div class="row query-result"></div>',
					'<div class="modal-guide">上传书籍图片:</div>',
					'<div class="input-group">',
						'<input type="file" accept="image/*" multiple=“multiple” class="btn btn-primary select-img">',
					'</div>',
				'</div>'
			].join(''),
			force: 1,
			initCall: function() {
				var Self = this, body;
				body = Self._body;
				body.find('.btn-confirm').text('开始识别');
				body.on('click.bs.custom', '.query-btn', function(e) {
					var name = body.find('.query-name').val().trim();
					if(!name) {
						alert('请输入有效的书名或ISBN');
						return false;
					}
					$.ajax({
						url: window.basePath + 'book/booksList',
						type: 'GET',
						data: {
							nameisbn: name
						},
						dataType: 'json',
						success: function(data) {
							if(data.status === 0) {
								body.find('.query-result').html($.tmpl('bookListTemplate', data.result));
							} else {
								alert(data.msg);
							}
						}
					});
				});
				// 图片上传张数限制
				body.find('.select-img').on('change', function(e) {
					if(this.files.length > 500) {
						alert('请不要同时上传超过500张图片，并控制每张图片的数据量大小!');
					}
				});
			},
			confirm: function() {
				var Self = this,
					body = Self._body,
					loadingImg = window.basePath + 'image/loading.gif',
					files = body.find('.select-img')[0].files || [],
					fileLen = files.length,
					bookId = body.find('.query-result :checked').data('id');
				if(!bookId) {
					alert('请选择图书');
					Self.enableConfirm();
					return;
				}
				if(!fileLen) {
					alert('请上传图片');
					Self.enableConfirm();
					return;
				}
				Self._container.html('<div style="text-align:center;">正在上传，请等待…</div><br><img src=' + loadingImg + ' style="display:block;width:24px;margin:0 auto;">');
				// 上传图片
				uploader(files, {
					url: window.basePath + 'fileupload/batchRecognitionBook',
					data: {
						bookId: bookId
					},
					end: function(results) {
						var i, len, errorResults, error, container;
						errorResults = results.filter(function(o) {
							return o.status !== 0;
						});
						len = errorResults.length;
						container = Self._container;
						if(len > 0) {
							error = [
								'<div style="color:#c00;">本组图片处理完毕，以下压缩包上传失败：</div>',
								'<ul>',
									'{{each(i, item) data}}',
									'<li>',
										'${item.name}-${item.msg}',
									'</li>',
									'{{/each}}',
								'</ul>',
								'<div style="color:#c00;">请修复问题文件重新上传！</div>'
							].join('');
							$.template('errorTemplate', error);
							container.html($.tmpl('errorTemplate', {data: errorResults}));
						} else {
							container.text('本组图片处理完毕，全部上传成功！');
						}
					}
				});
			}
		});
	};
});