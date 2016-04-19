define('util/uploadFile', [], function(require) {
	'use strict';
	var uploadFile;
	/**
	 * 批量上传文件
	 * @param  {Array} files  文件队列
	 * @param  {Object} config 配置参数
	 * @config {String} url 请求链接
	 * @config {String} name 请求参数名
	 * @config {Object} data 其他请求参数
	 * @config {Function} end 全部处理完成回调
	 * @config {Function} error 请求错误回调
	 * @example
	 * 		seajs.use(['lib/jquery', util/uploadFile', 'util/jquery.tmpl'], function($, uploader, null) {
	 * 			uploader(files, {
	 * 				url: window.basePath + '',
	 * 				name: 'file',
	 * 				end: function(results) {
	 * 					var i, len, errorResults, error, container;
	 * 					errorResults = results.filter(function(o) {
	 * 						return o.status !== 0;
	 * 					});
	 * 					len = errorResults.length;
	 * 					container = modalPop.find(".modal-body");
	 * 					if(len > 0) {
	 * 						error = [
	 * 							'<div style="color:#c00;">本组图片处理完毕，以下压缩包上传失败：</div>',
	 * 							'<ul>',
	 * 								'{{each(data) item}}',
	 * 								'<li>',
	 * 									'${item.name}-${item.msg}',
	 * 								'</li>',
	 * 								'{{/each}}',
	 * 							'</ul>',
	 * 							'<div style="color:#c00;">请修复问题文件重新上传！</div>'
	 * 						].join('');
	 * 						$.template('errorTemplate', error);
	 * 						container.html($.tmpl('errorTemplate', errorResults));
	 * 					} else {
	 * 						container.text('本组图片处理完毕，全部上传成功！');
	 * 					}
	 * 				}
	 * 			});
	 * 		});
	 */
	uploadFile = function(files, config) {
		var defaultConfig, i, len, formData, file, results = [];
		files = files || [];
		if(files.constructor === File) {
			files = [files];
		}
		len = files.length;
		if(len === 0) {
			return;
		}
		defaultConfig = {
			name: 'file',
			url: window.basePath + 'fileupload/uploadLocalImage'
		};
		config = $.extend(defaultConfig, config);
		config.data = config.data || {};
		for(i = 0; i < len; ++i) {
			file = files[i];
			formData = new window.FormData();
			$.each(config.data, function(i, o) {
				formData.append(i, o);
			});
			formData.append(config.name, file, file.name);
			(function(i) {
				$.ajax({
					url: config.url,
					method: 'POST',
					data: formData,
					// 自动生成boundray 提高文件传输效率
					contentType: false,
					processData: false,
					dataType: 'json',
					success: function(data) {
						data.name = files[i].name;
						results.push(data);
						if(i === len - 1) {
							if(config.end) {
								config.end(results);
							}
						}
					},
					error: function() {
						if(config.error) {
							config.error();
						}
					}
				});
			}(i));
		}
	};
	window.uploadFile = uploadFile;
	return uploadFile;
});