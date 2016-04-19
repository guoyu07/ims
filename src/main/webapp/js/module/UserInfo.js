define('module/UserInfo', ['lib/jquery', 'util/jquery.tmpl', 'module/Dialog'], function(require) {
	var $, dialog, userInfo, loadTeam,
		userInfoTmpl, incompleteUserInfoTmpl, loadTeamMember, teamInfoTmpl,
		option;
	$ = require('lib/jquery');
	require('util/jquery.tmpl');
	dialog = require('module/Dialog');
	/**
	 * 加载小组信息
	 * @param  {String}   role  角色
	 * @param  {Function} callback 回调函数
	 */
	loadTeam = function(role, callback) { 
		$.ajax({
			url: window.basePath + 'user/getTikuTeamList', 
			type: 'GET',
			dataType: 'json',
			data: {
				role: role
			},
			success: function(data) {
				if(data.status == 0) {
					teamInfo = data.result;
					teamInfo.unshift({
						id: '',
						name: '请选择'
					});
					callback(teamInfo);
				} else {
					alert(data.msg);
				}
			},
			error: function() {
				alert("网络错误，请稍后重试");
			}
		});
	};
	// 下拉模板
	option = '<option value="{{= id}}">{{= name}}</option>';
	$.template('optionTemplate', option);
	// 用户信息模板
	userInfoTmpl = [
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">手机号：</span>',
			'<input type="text" value="{{= userMobile}}" class="user-mobile"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">密码：</span>',
			'<input type="text" value="" class="password"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">账号：</span>',
			'<input type="text" value="{{= userKey }}" class="user-key" disabled/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">姓名：</span>',
			'<input type="text" value="{{= userName}}" class="user-name"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">身份：</span>',
			'<select class="role" {{if teamRole==="学生组组长" || teamRole==="教师组组长"}}disabled="disabled"{{/if}}>',
				'<option {{if role==="教师"}}selected="selected"{{/if}} value="教师">教师</option>',
				'<option {{if role==="学生"}}selected="selected"{{/if}} value="学生">学生</option>',
			'</select>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">小组：</span>',
			'<select class="team" {{if teamRole==="学生组组长" || teamRole==="教师组组长"}}disabled="disabled"{{/if}}>',
				'{{each(i, item) teamInfo}}',
				'<option {{if teamId===item.id}}selected="selected"{{/if}} value="{{= item.id}}">{{= item.name}}</option>',
				'{{/each}}',
			'</select>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">状态：</span>',
			'<select class="status">',
				'<option {{if status==="0"}}selected="selected"{{/if}} value="0">启用</option>',
				'<option {{if status==="1"}}selected="selected"{{/if}} value="1">禁用</option>',
			'</select>',
		'</p>'
	].join('');
	$.template('userInfoTemplate', userInfoTmpl);
	// 不完整用户信息模板 组长权限
	incompleteUserInfoTmpl = [
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">手机号：</span>',
			'<input type="text" value="{{= userMobile}}" class="user-mobile"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">密码：</span>',
			'<input type="text" value="" class="password"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">姓名：</span>',
			'<input type="text" value="{{= userName}}" class="user-name"  maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">账号：</span>',
			'&nbsp;{{= userKey }}',
		'</p>'
	].join('');
	$.template('incompleteUserInfoTemplate', incompleteUserInfoTmpl);
	// 小组信息模板
	teamInfoTmpl = [
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">小组：</span>',
			'<input class="team-name" type="text" value="{{= teamName}}">',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">当前组长：</span>',
			'<span class="captain" />{{= captainName}}</td>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">新组长：</span>',
			'<select class="new-captain">',
				'{{each(i, item) teamInfo}}',
					'<option {{if +item.key === captain}}selected="selected"{{/if}} value="{{= item.key}}">{{= item.name}}</option>',
				'{{/each}}',
			'</select>',
		'</p>'
	].join('');
	$.template('teamInfoTemplate', teamInfoTmpl);
	// 创建小组模板
	newTeamTmpl = [
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">小组：</span>',
			'<input class="team-name" type="text" value="" maxLength="30"/>',
		'</p>',
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">组长：</span>',
			'<select class="captain">',
				'{{each(i, item) teamInfo}}',
				'<option value="{{= item.key}}">{{= item.name||item.key}}</option>',
				'{{/each}}',
			'</select>',
		'</p>'
	].join('');
	$.template('newTeamTemplate', newTeamTmpl);
	userInfo = {
		/**
		 * 初始化小组
		 * @param  {String|jQuery} container 小组信息容器
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			userInfo.initTeam('');
		 * 		});
		 */
		initTeam: function(container) {
			if($.type(container) === 'string') {
				container = $('#' + container);
			}
			loadTeam('', function(teamInfo) {
				container.append($.tmpl('optionTemplate', teamInfo));
			});
		},
		/**
		 * 添加成员
		 * @param {String} source 来源id
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			userInfo.addMember('');
		 * 		});
		 */
		addMember: function(source) {
			// 默认加载学生分组
			loadTeam('学生', function(teamInfo) {
				dialog.show('modal-dialog', {
					sizeClass: 'modal-sm',
					title: '创建用户',
					content: [
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">手机号：</span>',
							'<input type="text" value="" class="user-mobile"  maxLength="30"/>',
						'</p>',
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">密码：</span>',
							'<input type="text" value="" class="password"  maxLength="30"/>',
						'</p>',
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">账号：</span>',
							'<input type="text" value="" class="user-key"  maxLength="30"/>',
						'</p>',
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">姓名：</span>',
							'<input type="text" value="" class="user-name"  maxLength="30"/>',
						'</p>',
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">身份：</span>',
							'<select class="role">',
								'<option value="教师">教师</option>',
								'<option selected="selected" value="学生">学生</option>',
							'</select>',
						'</p>',
						'<p>',
							'<span style="display:inline-block;text-align:right;width:110px">小组：</span>',
							'<select class="team"></select>',
						'</p>'
					].join(''),
					source: source,
					initCall: function() {
						var Self = this, option, team;
						team = Self._container.find('.team');
						team.html($.tmpl('optionTemplate', teamInfo));
						// 切换角色，加载分组
						Self._container.find('.role').on('change', function(e) {
							var role = $(this).find('option:selected').val();
							loadTeam(role, function(teamInfo) {
								team.html($.tmpl('optionTemplate', teamInfo));
							});
						});
					},
					confirm: function() {
						var Self = this, container, userMobile, role;
						container = Self._container;
						userMobile = container.find('.user-mobile').val().trim();
						if(!/^1\d{10}$/.test(userMobile)) {
							alert("手机号码不正确");
							Self.enableConfirm();
							return;
						}
						role = container.find('.role').val();
						$.ajax({
							url: window.basePath + 'user/addUserInfo',
							type: 'GET',
							data: {
								userKey: container.find('.user-key').val().trim(),
								userName: container.find('.user-name').val().trim(),
								userMobile: userMobile,
								password: container.find('.password').val(),
								role: role,
								groupName: role + '组员',
								status: 0,
								teamId: container.find('.team').val()
							},
							dataType: 'json',
							success: function(data) {
								if(data.status === 0) {
									window.location.reload();
								} else {
									Self.enableConfirm();
									alert(data.msg);
								}
							},
							error: function() {
								Self.enableConfirm();
								alert("网络错误，请稍后重试");
							}
						});
					}
				});
			});
		},
		/**
		 * 编辑成员
		 * @param {Object} data 成员数据
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			// 成员数据取自dataset
		 * 			userInfo.editMember({
		 * 				role: '',
		 * 				userMobile: '',
		 * 				userKey: '',
		 * 				userName: '',
		 * 				teamRole: '',
		 * 				status: ''
		 * 			});
		 * 		});
		 */
		editMember: function(data) {
			loadTeam(data.role, function(teamInfo) {
				data.teamInfo = teamInfo;
				dialog.show('modal-dialog', {
					sizeClass: 'modal-sm',
					title: '编辑用户',
					content: $.tmpl('userInfoTemplate', data),
					force: 1,
					initCall: function() {
						var Self = this;
						// 切换角色，加载分组
						Self._container.find('.role').on('change', function(e) {
							var role = $(this).find('option:selected').val();
							loadTeam(role, function(teamInfo) {
								Self._container.find('.team').html($.tmpl('optionTemplate', teamInfo));
							});
						});
					},
					confirm: function() {
						var Self = this, container, userMobile;
						container = Self._container;
						userMobile = container.find('.user-mobile').val().trim();
						if(!/^1\d{10}$/.test(userMobile)) {
							alert("手机号码不正确");
							Self.enableConfirm();
							return;
						}
						$.ajax({
							url: window.basePath + 'user/updateUserInfo',
							type: 'GET',
							data: {
								userKey: container.find('.user-key').val().trim(),
								id: data.userId,
								userName: container.find('.user-name').val().trim(),
								userMobile: userMobile,
								password: container.find('.password').val(),
								role: container.find('.role').val(),
								status: container.find('.status').val(),
								teamId: container.find('.team').val()
							},
							dataType: 'json',
							success: function(data) {
								if(data.status === 0) {
									window.location.reload();
								} else {
									Self.enableConfirm();
									alert(data.msg);
								}
							},
							error: function() {
								Self.enableConfirm();
								alert("网络错误，请稍后重试");
							}
						});
					}
				});
			});
		},
		/**
		 * 受限添加成员
		 * @param {String} source 来源id
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			userInfo.addMemberIncomplete('');
		 * 		});
		 */
		addMemberIncomplete: function(source) {
			dialog.show('modal-dialog', {
				sizeClass: 'modal-sm',
				title: '创建用户',
				content: [
					'<p>',
						'<span style="display:inline-block;text-align:right;width:110px">手机号：</span>',
						'<input type="text" value="" class="user-mobile"  maxLength="30"/>',
					'</p>',
					'<p>',
						'<span style="display:inline-block;text-align:right;width:110px">密码：</span>',
						'<input type="text" value="" class="password"  maxLength="30"/>',
					'</p>',
					'<p>',
						'<span style="display:inline-block;text-align:right;width:110px">账号：</span>',
						'<input type="text" value="" class="user-key"  maxLength="30"/>',
					'</p>',
					'<p>',
						'<span style="display:inline-block;text-align:right;width:110px">姓名：</span>',
						'<input type="text" value="" class="user-name"  maxLength="30"/>',
					'</p>'
				].join(''),
				source: source,
				confirm: function() {
					var Self = this, container, userMobile, role;
					container = Self._container;
					userMobile = container.find('.user-mobile').val().trim();
					if(!/^1\d{10}$/.test(userMobile)) {
						alert("手机号码不正确");
						Self._confirm.prop('disabled', false);
						return;
					}
					role = container.find('.role').val();
					$.ajax({
						url: window.basePath + 'user/addUserInfo',
						type: 'GET',
						data: {
							userKey: container.find('.user-key').val().trim(),
							userName: container.find('.user-name').val().trim(),
							userMobile: userMobile,
							password: container.find('.password').val(),
							status: 0
						},
						dataType: 'json',
						success: function(data) {
							Self._confirm.prop('disabled', false);
							if(data.status === 0) {
								window.location.reload();
							} else {
								alert(data.msg);
							}
						},
						error: function() {
							Self._confirm.prop('disabled', false);
							alert("网络错误，请稍后重试");
						}
					});
				}
			});
		},
		/**
		 * 受限编辑成员
		 * @param  {Object} data 成员信息
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			// 成员数据取自dataset
		 * 			userInfo.editMemberIncomplete({
		 * 				userMobile: '',
		 * 				userKey: '',
		 * 				userName: ''
		 * 			});
		 * 		});
		 */
		editMemberIncomplete: function(data) {
			dialog.show('modal-dialog', {
				sizeClass: 'modal-sm',
				title: '编辑用户',
				content: $.tmpl('incompleteUserInfoTemplate', data),
				force: 1,
				confirm: function() {
					var Self = this, container, userMobile;
					container = Self._container;
					userMobile = container.find('.user-mobile').val().trim();
					if(!/^1\d{10}$/.test(userMobile)) {
						alert("手机号码不正确");
						Self._confirm.prop('disabled', false);
						return;
					}
					$.ajax({
						url: window.basePath + 'user/updateUserInfo',
						type: 'GET',
						data: {
							id: data.userId,
							userName: container.find('.user-name').val().trim(),
							userMobile: userMobile,
							password: container.find('.password').val(),
							status: 0
						},
						dataType: 'json',
						success: function(data) {
							Self.enableConfirm();
							if(data.status === 0) {
								window.location.reload();
							} else {
								alert(data.msg);
							}
						},
						error: function() {
							Self.enableConfirm();
							alert("网络错误，请稍后重试");
						}
					});
				}
			});
		},
		/**
		 * 添加小组
		 * @param {String} source 来源id
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			userInfo.addTeam('');
		 * 		});
		 */
		addTeam: function(source) {
			$.ajax({
				url: window.basePath + 'user/getCanCaptainUsersList', 
				type: 'GET',
				dataType: 'json',
				success: function(data) {
					if(data.status == 0) {
						if(data.result.length === 0) {
							alert('暂时没有可以任命的组长');
							return;
						}
						dialog.show('modal-dialog', {
							sizeClass: 'modal-sm',
							title: '编辑用户',
							content: $.tmpl('newTeamTemplate', {
								teamInfo: data.result
							}),
							source: source,
							confirm: function() {
								var Self = this, container, captain;
								container = Self._container;
								captain = container.find('.captain :selected');
								$.ajax({
									url: window.basePath + 'tranops/newTeam',
									type: 'GET',
									data: {
										teamName: container.find('.team-name').val(),
										captain: captain.val(),
										captainName: captain.text()
									},
									dataType: 'json',
									success: function(data) {
										Self.enableConfirm();
										if(data.status === 0) {
											window.location.reload();
										} else {
											alert(data.msg);
										}
									},
									error: function() {
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
				error: function() {
					alert('网络错误，请稍后重试');
				}
			});
		},
		/**
		 * 编辑小组
		 * @param  {Object} teamData 小组信息
		 * @example
		 * 		seajs.use(['module/UserInfo'], function(userInfo) {
		 * 			// 成员数据取自dataset
		 * 			userInfo.editTeam({
		 * 				teamId: '',
		 * 				captain: '',
		 * 				captainName: '',
		 * 				teamName: ''
		 * 			});
		 * 		});
		 */
		editTeam: function(teamData) {
			$.ajax({
				url: window.basePath + 'user/getUsersListByTeamId', 
				type: 'GET',
				data: {
					teamId: teamData.teamId
				},
				dataType: 'json',
				success: function(data) {
					if(data.status == 0) {
						teamInfo = data.result;
						teamData.teamInfo = teamInfo;
						dialog.show('modal-dialog', {
							sizeClass: 'modal-sm',
							title: '编辑用户',
							content: $.tmpl('teamInfoTemplate', teamData),
							force: 1,
							confirm: function() {
								var Self = this, container, newCaptain;
								container = Self._container;
								newCaptain = container.find('.new-captain :selected');
								$.ajax({
									url: window.basePath + 'tranops/updateTeam',
									type: 'GET',
									data: {
										teamId: teamData.teamId,
										oldCaptain: teamData.captain,
										newCaptain: newCaptain.val(),
										newTeamName: container.find('.team-name').val()
									},
									dataType: 'json',
									success: function(data) {
										Self._confirm.prop('disabled', false);
										if(data.status === 0) {
											window.location.reload();
										} else {
											alert(data.msg);
										}
									},
									error: function() {
										alert("网络错误，请稍后重试");
									}
								});
							}
						});
					} else {
						alert(data.msg);
					}
				},
				error: function() {
					alert("网络错误，请稍后重试");
				}
			});
		}
	};
	return userInfo;
});
