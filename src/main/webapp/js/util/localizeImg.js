define('util/localizeImg', ['lib/jquery'], function(require) {
	var $ = require('lib/jquery');
	 /**
	 * 图片本地化校验，没有被本地化的图片使用默认图片替代
	 * @author jansesun(sunjian@xuexibao.cn)
	 */
	return function(imgs, callback) {
		var internalReg, errorImg, remoteUrls = [];
		internalReg = /^https?:\/\/(?:(?:\d+\.){3}\d+:\d+|.*\.91xuexibao\.com)\/.*/;
		errorImg = window.basePath + 'image/default.png';
		imgs = [].filter.call(imgs, function(img) {
			var $img = $(img),
				imgUrl = $img .attr('src');
			if(!internalReg.test(imgUrl) || !$img.data('width')) {
				remoteUrls.push(encodeURIComponent(imgUrl));
				return true;
			}
		});
		if(remoteUrls[0]) {
			$.ajax({
				url: window.basePath + 'fileupload/uploadRemoteImages',
				type: 'GET',
				dataType: 'json',
				data: {
					remoteUrls: remoteUrls.join()
				},
				success: function(data) {
					var errorFlag = false;
					if(data.status === 0) {
						$.each(imgs, function(i) {
							var imgInfo = data.result[i];
							if(!imgInfo.imageUrl) {
								errorFlag = true;
								$(this).attr('src',errorImg);
							} else {
								$(this).attr({
									'src': imgInfo.imageUrl,
									'data-width': imgInfo.width,
									'data-height': imgInfo.height
								});
							}
						});
						if(!errorFlag && callback) {
							callback();
						}
					} else {
						alert(data.msg);
					}
				}
			});
		} else {
			if(callback) {
				callback();
			}
		}
	};
});