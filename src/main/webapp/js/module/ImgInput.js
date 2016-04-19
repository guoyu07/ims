/**
 * @description  图片输入框
 * @author lishunping(lishunping@xuexibao.cn)
 */
define('module/ImgInput', ['lib/jquery', 'module/Dialog'], function(require) {
	'use strict';
	var $ = require('lib/jquery'),
		dialog = require('module/Dialog'),
		ImgInput,
		instance,
		basePath = window.basePath || '';
	ImgInput = function(id) {
		var Self = this;
		if(instance) {
			return instance;
		}
		Self.id = id;
		instance = Self;
	};
	ImgInput.prototype = {
		show: function() {
			var Self = this;
			dialog.show(Self.id, {
				sizeClass: 'modal-lg',
				title: '上传图片',
				content: [
					'<ul class="nav nav-tabs" role="tablist">',
						'<li role="presentation" class="active">',
							'<a href="#local-image" role="tab" data-toggle="tab" data-image-type="1">本地上传</a>',
						'</li>',
						'<li role="presentation" class="">',
							'<a href="#remote-image" role="tab" data-toggle="tab" data-image-type="2">网络图片</a>',
						'</li>',
					'</ul>',
					'<div class="tab-content" style="margin-top:15px">',
						'<div role="tabpanel" class="tab-pane fade active in" id="local-image">',
							'<input class="img-url form-control" type="file" multiple="multiple" accept="image/*">',
						'</div>',
						'<div role="tabpanel" class="tab-pane fade" id="remote-image">',
							'<input class="img-url form-control" type="text" placeholder="请填写图片地址">',
						'</div>',
					'</div>'
				].join(''),
				footer: '<button type="button" class="btn btn-primary btn-confirm">确定</button>',
				source: Self.id,
				initCall: function() {
					var me = this, container, nav;
					container = me._body;
					// 绑定切换事件
					container.find('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
						var el = $(e.target);
						Self.imageType = el.data('image-type');
					});
					nav = container.find('.nav-tabs');
					container.on('show.bs.modal', function(e) {
						Self.imageType = 1;
						$('a[href="#local-image"]').tab('show');
					});
					// 绑定隐藏事件
					container.on('hide.bs.modal', function(e) {
						container.find('.img-url').val('');
						$(document).trigger('ImgInput:hide');
					});
				},
				confirm: function() {
					var me = this;
					if(Self.imageType === 1) {
						require.async(['util/uploadFile'], function(uploader) {
							uploader($('#local-image').find('.img-url')[0].files[0], {
								end: function(results) {
									var data;
									data = results[0];
									Self.uploadCall(data);
								},
								error: function() {
									alert("提交异常，请稍后重试！");
								}
							});
						});
					} else {
						$.ajax({
							url: basePath + 'fileupload/uploadRemoteImage',
							type: 'POST',
							data: {
								remoteUrl: $('#remote-image').find('.img-url').val()
							},
							dataType: 'json',
							success: function(data) {
								Self.uploadCall(data);
							},
							error: function() {
								alert("提交异常，请稍后重试！");
							}
						});
					}
				}
			});
		},
		hide: function() {
			var Self = this;
			dialog.hide();
		},
		uploadCall: function(data) {
			var Self = this;
			if(data.status === 0) {
				$(document).trigger('ImgInput:insert', [data.result]);
			} else {
				alert(data.msg);
			}
			Self.hide();
		}
	};
	return {
		show: function(id, editorArea) {
			var placeholder;
			editorArea.focus();
			document.execCommand('insertimage', false, basePath + 'image/exam/img.png');
			placeholder = editorArea.find('img[src$="image/exam/img.png"]');
			$(document).one('ImgInput:insert', function(e, imageInfo) {
				placeholder.attr({
					'src': imageInfo.imageUrl,
					'data-width': imageInfo.width,
					'data-height': imageInfo.height
				}).data('img', 1);
			}).one('ImgInput:hide', function(e) {
				var flag = placeholder.data('img');
				if(!flag) {
					placeholder.remove();
					// 手动释放内存
					placeholder = null;
				}
			});
			(new ImgInput(id)).show();
		},
		hide: function(id) {
			(new ImgInput(id)).hide();
		}
	};
});