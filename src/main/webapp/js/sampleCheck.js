'use strict';
(function($) {
	var	bestData = window.base_list,
		repeatData = window.dup_list,
		noRepeatData = window.undup_list,
		checkId = window.checkId,
		parentId = window.parentId,
		container = $('<div>'),
		sendResult;
	$.each(bestData, function(i, o) {
		o.content = container.html(o.content).html();
	});
	$.each(repeatData, function(i, o) {
		o.content = container.html(o.content).html();
	});
	$.each(noRepeatData, function(i, o) {
		o.content = container.html(o.content).html();
	});
	$('#best-result').html($('#detail-tmpl').tmpl(bestData));
	$('#repeat-result').html($('#question-tmpl').tmpl(repeatData));
	$('#no-repeat-result').html($('#question-tmpl').tmpl(noRepeatData));
	sendResult = function(data, config) {
		config = config || {};
		$.ajax({
			url: window.basePath + 'dedup/audit',
			data: data,
			type: 'POST',
			dataType: 'json',
			success: function(data) {
				if(data.status == 0) {
					checkId = data.result;
					if(checkId != '' && checkId != undefined){
						window.location.assign(window.basePath + 'mark/getCheckOneDupGroup?checkId=' + checkId);
					}else{
						window.location.assign(window.basePath + 'dedup/checkRecordList');
					}
				} else {
					alert(data.msg);
				}
			}
		});
		if(config.callback) {
			config.callback();
		}
	};
	$('#confirm').on('click', function(e) {
		sendResult({
			status: 1,
			id:checkId
		});
	});
	$('#error').on('click', function(e) {
		sendResult({
			status: 2,
			id:checkId
		});			
	});
	$('#back').on('click', function(e) {
		window.location.assign(window.basePath + 'dedup/dedupDetailViewSearch?parentId=' + parentId);
	});	
}($));