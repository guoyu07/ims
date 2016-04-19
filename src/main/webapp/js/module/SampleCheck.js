define('module/SampleCheck', ['lib/jquery', 'module/Dialog', 'util/jquery.tmpl', 'util/bootstrap.datetimepicker.zh-CN'], function(require) {
	'use strict';
	var $, dialog, sampleCheck;
	$ = require('lib/jquery');
	dialog = require('module/Dialog');
	require('util/jquery.tmpl');
	require('util/bootstrap.datetimepicker.zh-CN');
	/**
	 * 生成抽检
	 * @param  {String} url    抽检接口
	 * @param  {Object} config 扩展参数
	 * @config {String} source 来源id
	 * @example
	 * 		seajs.use('module/SampleCheck', function(sampleCheck) {
	 * 			sampleCheck('', {
	 * 				source: ''
	 * 			});
	 * 		});
	 */
	sampleCheck = function(url, config) {
		config = config || {};
		// 请求小组信息
		$.ajax({
			url: window.basePath + 'tranops/getAllTikuTeam',
			type: 'GET',
			dataType: 'json',
			success: function(data) {
				if(data.status === 0) {
					var content, tmpl;
					// 小组信息模板
					tmpl = [
						'{{each(i, item) items}}',
							'<tr>',
								'<td>',
									'<input class="check-item" name="checkbox" type="checkbox" value="${item.id}">',
								'</td>',
								'<td>${i + 1}</td>',
								'<td>${item.captainName}</td>',
								'<td>${item.usersNum}</td>',
							'</tr>',
						'{{/each}}'
					].join('');
					$.template('listTemplate', tmpl);
					dialog.show('modal-dialog', {
						sizeClass: 'modal-lg',
						title: '生成抽检',
						content: [
							'<p>',
								'<ul class="record-data">',
									'<li>',
										'抽检日期:',
										'<input style="margin-left:16px;" class="form-control form-date start-time" type="text" value="" name="startTime" data-date-format="yyyy-mm-dd 00:00:00"/>',
										'~',
										'<input class="form-control form-date end-time" type="text" value="" name="endTime"  data-date-format="yyyy-mm-dd 23:59:59"/>',
									'</li>',
								'</ul>',
							'</p>',
							'<p>',
								'<span style="display:inline-block;">每组抽查：</span>',
								'<input class="team_name" type="text" value="" maxLength="30"/>',
							'</p>',
							'<p>',
								'<span style="display:inline-block;">抽查小组：</span>',
							'</p>',
							'<div class="table-frame">',
								'<table class="table table-bordered check-team" border="1" cellpadding="0" cellspacing="0">',
									'<tr>',
										'<th>',
											'<input class="check-all" type="checkbox">',
										'</th>',
										'<th>序号</th>',
										'<th>组长</th>',
										'<th>小组人数</th>',
									'</tr>',
								'</table>',
							'</div>'
						].join(''),
						source: config.source,
						initCall: function() {
							var Self = this, allCheck, checkList;
							Self._body.find('.check-team').append($.tmpl('listTemplate', {items: data.result}));
							Self._body.find('.btn-confirm').text('生成抽检');
							allCheck = Self._body.find('.check-all');
							checkList = Self._body.find('.check-item');
							// 绑定全选
							Self._body.on('click.bs.custom', '.check-all', function(e) {
								checkList.prop('checked', $(this).prop('checked'));
							});
							// 取消全选
							Self._body.on('click', '.check-item', function(e) {
								if(!$(this).prop('checked')) {
									allCheck.prop('checked', false);
								}
							});
							// 绑定datetimepicker插件
							Self._body.find(".form-date").datetimepicker({
								language: 'zh-CN',/*加载日历语言包，可自定义*/
								weekStart: 1,
								todayBtn:  1,
								autoclose: 1,
								todayHighlight: 1,
								minView: 2,
								forceParse: 0,
								showMeridian: 1
							});
						},
						confirm: function() {
							var Self = this, teamIds, body, checkList;
							body = Self._body;
							checkList = body.find('.check-item');
							teamIds = Array.prototype.map.call(checkList.filter(':checked'), function(o) {
									return $(o).val();
							}).join();
							$.ajax({
								url: url,
								type: 'GET',
								dataType: 'json',
								data: {
									startTime: body.find('.start-time').val(),
									endTime: body.find('.end-time').val(),
									n: body.find('.team_name').val(),
									teamIds: teamIds
								},
								success: function(data) {
									if(data.status === 0){
										window.location.reload();
									} else {
										Self.enableConfirm();
										alert(data.msg);
									}
								},
								error:function() {
									Self.enableConfirm();
									alert("网络错误，请稍后重试");
								}
							});
						}
					});
				} else {
					alert(data.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	};
	return sampleCheck;
});