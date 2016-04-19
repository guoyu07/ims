//运营录题-审核列表
function transhengheList(modalPop,audio_ids,whichTds,url){
	var audioStatus,urlx,shenheStatus;
	var Stch=false;
	modalPop.off().on('show.bs.modal',function(){
		modalPop.find(".modal-title").html("批量审核");
		modalPop.find(".modal-body").empty().append('<p id="pingfen">审核结果：<label for="passed"><input type="radio" name="shenhe" id="passed" value="2"/>审核通过</label>'+
				'<label for="unpassed" class="ml10"><input type="radio" name="shenhe" id="unpassed" value="3"/>审核不通过</label></p>'+
				'<p id="result" class="co_red"></p>');
		modalPop.find(".modal-footer").html('<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
        +'<button type="button" class="btn btn-primary" id="popSure">保存</button>');
	});
	
	modalPop.on('click','#pingfen input',function(){
		if($(this).is(":checked")){
			shenheStatus=$(this).val();
			if(shenheStatus==2){
				auditStatus="审核通过";
				urlx=basePath + "/tranops/audit?questionIds="+audio_ids+"&status=2";
			}else if(shenheStatus==3){
				auditStatus="审核未通过";
				$("#result").html('原因：<select id="why"><option value="0">识别结果正确，不该录</option><option value="1">文字格式问题</option><option value="2">图片有问题</option>'+
						'<option value="3">学科选择错误</option>'+
						'<option value="4">请补充解题思路&知识点</option>'+
						'<option value="5">其他</option></select>');
				var reasonVal=$("#why").val();
				$("#why").change(function(){
					reasonVal=$("#why").val();
					urlx=basePath + "/tranops/audit.do?questionIds="+audio_ids+"&status=3&reason="+reasonVal;
					if(reasonVal==5){
						$("#result").append("<input type='text' class='ml10' id='reasonStr' maxlength='50' style='width:194px;margin-left:0;margin-top:10px;'>");
					}else{
						$("#result").find("input").remove();
					}
				});
				urlx=basePath + "/tranops/audit?questionIds="+audio_ids+"&status=3&reason="+reasonVal;
			}
		}	
	});	
	modalPop.on("click","#popSure",function(){
		//$(this).slideUp();
		if($("#reasonStr")&&$.trim($("#reasonStr").val())!=""){
			urlx+="&reasonStr="+$.trim($("#reasonStr").val());
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status == 200){
					location.href = url;
					// $.each(whichTds,function(){
					// 	$(this).html(auditStatus+'<input type="hidden" value="'+shenheStatus+'">');
					// 	$(this).parent().find('td input[name="checkbox"]').removeAttr("checked");
					// });
					// modalPop.modal('hide');
				}else{alert(obj.msg);}
			},error:function(){
				alert("网络错误请稍后再试！");
			}
		});
	});
}
//创建用户
function addMember(modalPop,roles,statuss,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getTikuTeamList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx, 
		async:false,

		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				if(roles=="教师"){//正常
					roleSelect='<option selected="selected" value="教师">教师</option><option value="学生">学生</option>';
					}else{
						roleSelect='<option value="教师">教师</option><option selected="selected" value="学生">学生</option>';
				  }
				
				var statusSelect;
				if(statuss=="0"){//正常
					statusSelect='<option selected="selected" value="0">启用</option><option value="1">禁用</option>';
				}else{
					statusSelect='<option value="0">启用</option><option selected="selected" value="1">禁用</option>';
				}
				$.each(obj.result,function(i,n){
					selectHtml+='<option value="'+n.id+'">'+n.name+'</option>';
				});		
				var option = "<option value=''>请选择</option>";
				
				selectHtml="<select id='provName'>"+option+selectHtml+"</select>";
				modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><input type="text" value="" id="userMobile"  maxLength="30"/></p>'+
						'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><input type="text" value="" id="userKey"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><input type="text" value="" id="userName"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">身份：</span><select id="role">'+roleSelect+'</select></p>'
						//+'<p><span style="display:inline-block;text-align:right;width:110px">状态：</span><select id="status">'+statusSelect+'</select></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">小组：</span>'+selectHtml+'</p>'
					    );
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});

	$("#popSure").off().click(function(){
		var userMobile=$("#userMobile").val();
		var reg = /^1[0-9]{10}$/;
		if(reg.test(userMobile) == false){
			alert("手机号码不正确");	
		
		}else{
		var password=$("#password").val();
		var userKey=$("#userKey").val();
		var userName=$("#userName").val();
		var teamId=$("#provName").val();
		var teamname=$("#provName").find("option:selected").text();
		var roleId=$("#role").val();
		var role_name=$("#role").find("option:selected").text();
		var statusId=$("#status").val();
		var status_name=$("#status").find("option:selected").text();
		// var status=0;
		var groupName="组员";
		var urlx;
		urlx="/tiku_ops/user/addUserInfo?userKey="+userKey+"&userName="+userName+"&userMobile="+userMobile+"&password="+password+"&role="+roleId+"&groupName="+roleId+groupName+"&status="+0+"&teamId="+teamId;
			
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					if(status=="0"){//正常
						status='启用';
					}else{
						status='禁止';
					}
					datas.tikuTeam.name = datas.tikuTeam.name || '';
					if(obj.status==0){
						modalPop.modal('hide');
						location.href=basePath + "user/userInfoList";
					}else if(obj.status==-1){
						alert(obj.msg);
					}
					
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});}
	});
}

//创建用户
function editMember(modalPop,userId,userKey,userName,roles,team,userMobile,statuss,memberListTab,role_team,clickBtn){
	urlx="/tiku_ops/user/getTikuTeamList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx, 
		async:false, 
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				if(roles=="教师"){//正常
					roleSelect='<option selected="selected" value="教师">教师</option><option value="学生">学生</option>';
				} else {
					roleSelect='<option value="教师">教师</option><option selected="selected" value="学生">学生</option>';
				}
				var statusSelect;
				if(statuss=="启用"){//正常
					statusSelect='<option selected="selected" value="0">启用</option><option value="1">禁用</option>';
				} else {
					statusSelect='<option value="0">启用</option><option selected="selected" value="1">禁用</option>';
				}
				$.each(obj.result,function(i,n){
					if(n.name == team) {
						selectHtml+='<option selected="selected" value="'+n.id+'">'+n.name+'</option>';
					} else {
						selectHtml+='<option value="'+n.id+'">'+n.name+'</option>';
					}
				});
				
				if(clickBtn=="0"){
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><input type="text" value="" id="userMobile"  maxLength="30"/></p>'+
						'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><input type="text" value="" id="userKey"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><input type="text" value="" id="userName"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">身份：</span><select id="role">'+roleSelect+'</select></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">小组：</span>'+selectHtml+'</p>'
					    );
				} else if(clickBtn!="0"){
					if(role_team == "组长") {
						selectHtml="<select id='provName' disabled='disabled'>"+selectHtml+"</select>";
					} else {
						selectHtml="<select id='provName'>"+selectHtml+"</select>";
					}
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><td  class="userMobile"><input type="text" value="'+userMobile+'" id="userMobile"  maxLength="30"/></p></td></p>'+
							'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><input type="text" value="'+userKey+'" id="userKey" disabled/></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><td class="userName"> <input type="text" value="'+userName+'" id="userName"  maxLength="30"/></p></td></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">身份：</span><select id="role">'+roleSelect+'</select></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">小组：</span>'+selectHtml+'</p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">状态：</span><select id="status">'+statusSelect+'</select></p>');
				}
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	$("#popSure").off().click(function(){
		var userMobile=$("#userMobile").val();
		var reg = /^1[0-9]{10}$/;
		if(reg.test(userMobile) == false){
			alert("手机号码不正确！");	
		}else{
		var password=$("#password").val();
		var userKey=$("#userKey").val();
		var userName=$("#userName").val();
		var teamId=$("#provName").val();
		var teamname=$("#provName").find("option:selected").text();
		var roleId=$("#role").val();
		var role_name=$("#role").find("option:selected").text();
		var statusId=$("#status").val();
		var status_name=$("#status").find("option:selected").text();
		var groupName="组员";
		var userId=clickBtn.siblings("input").val();
		var urlx;
		if(clickBtn=="0"){
			urlx="/tiku_ops/user/addUserInfo?userKey="+userKey+"&userName="+userName+"&userMobile="+userMobile+"&password="+password+"&role="+roleId+"&groupName="+roleId+groupName+"&status="+0+"&teamId="+teamId;
		}else if(clickBtn!="0"){
			urlx="/tiku_ops/user/updateUserInfo?userKey="+userKey+"&id="+userId+"&userName="+userName+"&userMobile="+userMobile+"&password="+password+"&role="+roleId+"&status="+statusId+"&teamId="+teamId;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					
					if(status=="0"){//正常
						status='启用';
						}else
					  {
						status='禁止';
					  }
					if(obj.status==0){//添加
						// datas.mobile = datas.mobile || '';
						if(clickBtn=="0"){//添加学校
							memberListTab.prepend('<tr><td class="name">'+datas.tikuTeam.name+'<input type="hidden" value="'+ datas.teamId +'" class="teamId" /></td>'
									+'<td class="groupname">'+datas.groupname+'</td>'
									+'<td class="userKey">'+datas.userKey+'</td>'
									+'<td class="userName">'+datas.userName+'</td>'
									+'<td class="userMobile">'+datas.userMobile+'</td>'
									+'<td class="role">'+datas.role+'</td>'
									+'<td class="status">'+datas.statusForShow+'<input type="hidden" value="'+datas.status +'" /></td>'
									+'<td><button class="btn btn-primary btn-xs ml10 editBtn"  data-toggle="modal"  data-target=".modal">编辑</button><input type="hidden" value="'+ datas.id +'" class="id" /></td>'
							
									+'</tr>');
							}else if(clickBtn!="0"){
									var whichTr=clickBtn.parents("tr");
									whichTr.find(".userName").html(datas.userName);
									whichTr.find(".userMobile").html(datas.userMobile);
									whichTr.find(".name").html(datas.tikuTeam.name);
									whichTr.find(".role").html(datas.role);
									whichTr.find(".status").html(datas.statusForShow);
								}
						// alert(obj.result);
					}else if(obj.status==-1){
					}
					modalPop.modal('hide');
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});}
	});
}



//创建用户
function zuaddMember(modalPop,userId,userKey,roles,userMobile,statuss,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getTikuTeamList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx, 
		async:false, 
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				if(roles=="教师"){//正常
					roleSelect='<option selected="selected" value="教师">教师</option><option value="学生">学生</option>';
					}else{
						roleSelect='<option value="教师">教师</option><option selected="selected" value="学生">学生</option>';
				  }
				
				var statusSelect;
				if(status=="0"){//正常
					statusSelect='<option selected="selected" value="0">启用</option><option value="1">禁用</option>';
					}else{
						statusSelect='<option value="0">启用</option><option selected="selected" value="1">禁用</option>';
				  }
				
				$.each(obj.result,function(i,n){
					selectHtml+='<option value="'+n.id+'">'+n.name+'</option>';
				});		
				
				selectHtml="<select id='provName'>"+selectHtml+"</select>";
				modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><input type="text" value="" id="userMobile"  maxLength="30"/></p>'+
						'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><input type="text" value="" id="userKey"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><input type="text" value="" id="userName"  maxLength="30"/></p>'
					    );
				
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
		$("#popSure").off().click(function(){
		var userMobile=$("#userMobile").val();
		var reg = /^1[0-9]{10}$/;
		if(reg.test(userMobile) == false){
			alert("手机号码不正确");
		}else{
		var password=$("#password").val();
		var userKey=$("#userKey").val();
		var userName=$("#userName").val();
		var teamId=$("#provName").val();
		var teamname=$("#provName").find("option:selected").text();
		var roleId=$("#role").val();
		var role_name=$("#role").find("option:selected").text();
		var statusId=$("#status").val();
		var status_name=$("#status").find("option:selected").text();
		// var status=0;
		var groupName="组员";
		
		var urlx;

		urlx="/tiku_ops/user/addUserInfo?userKey="+userKey+"&userName="+userName+"&userMobile="+userMobile+"&password="+password+"&groupName="+roleId+groupName+"&status="+0;
			
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					
					if(status=="0"){//正常
						status='启用';
						}else
					  {
						status='禁止';
					  }
					if(obj.status==0){//添加
						// datas.mobile = datas.mobile || '';
						modalPop.modal('hide');
						location.href=basePath + "user/userTeamInfoList";
						// alert(obj.result);
					}else if(obj.status==-1){
						alert(obj.msg);
					}
					
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});}
	});
}



//创建用户
function zueditMember(modalPop,userId,userKey,userName,roles,userMobile,statuss,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getTikuTeamList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx, 
		async:false, 
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				if(roles=="教师"){//正常
					roleSelect='<option selected="selected" value="教师">教师</option><option value="学生">学生</option>';
					}else{
						roleSelect='<option value="教师">教师</option><option selected="selected" value="学生">学生</option>';
				  }
				
				var statusSelect;
				if(status=="0"){//正常
					statusSelect='<option selected="selected" value="0">启用</option><option value="1">禁用</option>';
					}else{
						statusSelect='<option value="0">启用</option><option selected="selected" value="1">禁用</option>';
				  }
				
				$.each(obj.result,function(i,n){
					selectHtml+='<option value="'+n.id+'">'+n.name+'</option>';
				});		
				if(clickBtn=="0"){
				selectHtml="<select id='provName'>"+selectHtml+"</select>";
				modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><input type="text" value="" id="userMobile"  maxLength="30"/></p>'+
						'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><input type="text" value="" id="userName"  maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><input type="text" value="" id="userKey"  maxLength="30"/></p>'
					    );
				}else if(clickBtn!="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">手机号：</span><td  class="userMobile"  /><input type="text" value="'+userMobile+'" id="userMobile"  maxLength="30"/></td></p>'+
							'<p><span style="display:inline-block;text-align:right;width:110px">密码：</span><input type="text" value="" id="password"  maxLength="30"/></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">姓名：</span><td class="userName" /><input type="text" value="'+userName+'" id="userName"  maxLength="30"/></td></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">账号：</span><td class="userKey" /> '+userKey+'</td></p>'
						//	+'<p><span style="display:inline-block;text-align:right;width:110px">状态：</span><select id="status">'+statusSelect+'</select></p>'
							);
				}
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
		$("#popSure").off().click(function(){
		var userMobile=$("#userMobile").val();
		var reg = /^1[0-9]{10}$/;
		if(reg.test(userMobile) == false){
			alert("手机号码不正确");	
		}else{
		var password=$("#password").val();
		var userKey=$("#userKey").val();
		var userName=$("#userName").val();
		var teamId=$("#provName").val();
		var teamname=$("#provName").find("option:selected").text();
		var roleId=$("#role").val();
		var role_name=$("#role").find("option:selected").text();
		var statusId=$("#status").val();
		var status_name=$("#status").find("option:selected").text();
		// var status=0;
		var groupName="组员";
		var userId=clickBtn.siblings("input").val();
		
		var urlx;
			if(clickBtn=="0"){
				//addUserInfo?userKey=zhengjun1&userMobile=15201116102&password=1234&role=&groupName=超级管理员&status=0&teamId=1
				urlx="/tiku_ops/user/addUserInfo?userKey="+userKey+"&userName="+userName+ "&userMobile="+userMobile+"&password="+password+"&groupName="+roleId+groupName+"&status="+0;
			}else if(clickBtn!="0"){
				// var id=clickBtn.siblings("input").val();
				//updateUserInfo?id=606&userKey=zhengjun&userMobile=15201116104&password=&role=学生&groupName=审题组&status=1&teamId=
				// urlx="/tiku_ops/user/updateUserInfo?id="+id+"&password="+password+"&role="+role+"&groupName="+groupName+"&status="+status+"&teamId="+teamId;
				urlx="/tiku_ops/user/updateUserInfo?id="+userId+"&userName="+userName+ "&userMobile="+userMobile+"&password="+password+"&status="+0;
			}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					
					if(status=="0"){//正常
						status='启用';
						}else
					  {
						status='禁止';
					  }
					if(obj.status==0){//添加
						// datas.mobile = datas.mobile || '';
						if(clickBtn=="0"){//添加学校
							memberListTab.prepend('<tr><td class="name">'+datas.tikuTeam.name+'<input value="'+ datas.teamId +'" class="teamId" /></td>'
									+'<td class="groupname">'+datas.groupname+'</td>'
									+'<td class="userKey">'+datas.userKey+'</td>'
									+'<td class="userName">'+datas.userName+'</td>'
									+'<td class="userMobile">'+datas.userMobile+'</td>'
									+'<td class="role">'+datas.role+'</td>'
									+'<td class="status">'+datas.statusForShow+'<input value="'+datas.status +'" /></td>'
									+'<td><button class="btn btn-primary btn-xs ml10 editBtn"  data-toggle="modal"  data-target=".modal">编辑</button><input type="hidden" value="'+ datas.id +'" class="id" /></td>'
							
									+'</tr>');
							}else if(clickBtn!="0"){
									var whichTr=clickBtn.parents("tr");
									whichTr.find(".userName").html(datas.userName);
									whichTr.find(".userMobile").html(datas.userMobile);
									whichTr.find(".name").html(datas.tikuTeam.name);
									whichTr.find(".role").html(datas.role);
									whichTr.find(".status").html(datas.statusForShow);
								}
					}else if(obj.status==-1){
					}
					modalPop.modal('hide');
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});}
	});
}





//重置密码

function resetPassword(whichBtn,modalPop,Id){
	modalPop.find(".modal-title").html("提示");
	modalPop.find(".modal-body").html('确定要重置密码吗?');
	modalPop.find(".modal-footer .btn-default").html("取消");
	modalPop.find(".modal-footer .btn-primary").html("确定");
	$("#popSure").off().click(function(){
		$.ajax({
			type:"get",
			dataType:"json",
			url:"/tiku_ops/user/updateUserPassword?id="+Id,
			success:function(obj){
				if(obj.status==0){
					alert("重置成功");
					modalPop.modal('hide');
				
				}else{
					alert(obj.msg);
				}
			},
			error:function(){alert("网络错误请稍后再试！");}
		});
	});
}




//创建小组
function addTeam(modalPop,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getCanCaptainUsersList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx,
		async:false,
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				$.each(obj.result,function(i,n){
					selectHtml+='<option value="'+n.key+'">'+(n.name||n.key)+'</option>';
				});
				if(clickBtn=="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input class="team_name" type="text" value="" maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>'
					    );
				} else if(clickBtn!="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><td/></td></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>');
				}
			} else {
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	$("#popSure").off().click(function(){
		var team_name=$(".team_name").val();
		var team_captain=$("#provName").find("option:selected").text();
		var team_key=$("#provName").find("option:selected").val();
		var urlx;
		if(clickBtn=="0"){
			urlx="/tiku_ops/tranops/newTeam?teamName="+team_name+"&captain="+team_key+"&captainName="+team_captain;
		}else if(clickBtn!="0"){
			// urlx="/tiku_ops/user/updateUserInfo?id="+userId;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					if(obj.status==0){
						memberListTab.prepend('<tr><td><span class="team-name">'+datas.name+'</span><span style="display:none;" class="teamId">'+ datas.id +'</span></td>'
									+'<td class="team-captain">'+datas.captainName+'</td>'
									+'<td class="team-usersNum">'+datas.usersNum+'</td>'
									+'<td class="team-transContentNum">'+datas.transContentNum+'</td>'
									+'<td class="team-transCompleteNum">'+datas.transCompleteNum+'</td>'
									+'<td class="team-transUnCheckNum">'+datas.transUnCheckNum+'</td>'
									+'<td class="team-transCheckedNum">'+datas.transCheckedNum+'</td>'
									+'<td><button class="btn btn-primary btn-xs ml10 editBtn"  data-toggle="modal"  data-target=".modal">编辑</button><input type="hidden" value="'+ datas.id +'" class="id" /></td>'
									+'</tr>');
					} else if(obj.status==-1){
					}
					modalPop.modal('hide');
				} else {
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	});
}


//创建小组
function addTeamInfo(modalPop,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getCanCaptainUsersList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx,
		async:false,
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				$.each(obj.result,function(i,n){
					selectHtml+='<option value="'+n.key+'">'+(n.name||n.key)+'</option>';
				});
				if(clickBtn=="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input class="team_name" type="text" value="" maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>'
					    );
				} else if(clickBtn!="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><td/></td></p>'
							+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>');
				}
			} else {
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	$("#popSure").off().click(function(){
		var team_name=$(".team_name").val();
		var team_captain=$("#provName").find("option:selected").text();
		var team_key=$("#provName").find("option:selected").val();
		var urlx;
		if(clickBtn=="0"){
			urlx="/tiku_ops/tranops/newTeam?teamName="+team_name+"&captain="+team_key+"&captainName="+team_captain;
		}else if(clickBtn!="0"){
			// urlx="/tiku_ops/user/updateUserInfo?id="+userId;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					var datas=obj.result;
					var status=datas.status;
					if(obj.status==0){
						memberListTab.prepend('<tr><td><span class="team-name">'+datas.name+'</span><span style="display:none;" class="teamId">'+ datas.id +'</span></td>'
									+'<td class="team-captain">'+datas.captainName+'</td>'
									+'<td><button class="btn btn-primary btn-xs ml10 editBtn"  data-toggle="modal"  data-target=".modal">编辑</button><input type="hidden" value="'+ datas.id +'" class="id" /></td>'
									+'</tr>');
					} else if(obj.status==-1){
					}
					modalPop.modal('hide');
				} else {
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	});
}

// 编辑小组
function editTeam(modalPop,team,teamId,captain,captain_name,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getUsersListByTeamId?teamId="+teamId;
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx,
		async:false, 
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				$.each(obj.result,function(i,n){
					if(n.name == captain) {
						selectHtml+='<option selected="selected" value="'+n.id+'">'+n.name+'</option>';
					} else {
						selectHtml+='<option value="'+n.key+'">'+n.name+'</option>';
					}
				});
				if(clickBtn=="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input type="text" value="" maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>'
					    );
				} else if(clickBtn!="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input class="team_name" type="text" value='+team+'></p>'
                    + '<p><span style="display:inline-block;text-align:right;width:110px">当前组长：</span><td class="captain" /> '+captain_name+'</td></p>'
					+'<p><span style="display:inline-block;text-align:right;width:110px">新组长：</span>'+selectHtml+'</p>');
				}
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	$("#popSure").off().click(function(){
		var team_name=$(".team_name").val();
		var oldCaptain = captain;
		var newCaptain = $("#provName").find("option:selected").text();
		var new_phone = $("#provName").find("option:selected").val();
		var urlx;
		if(clickBtn=="0"){
		}else if(clickBtn!="0"){
			urlx="/tiku_ops/tranops/updateTeam?teamId="+teamId+"&oldCaptain="+oldCaptain+"&newCaptain="+new_phone+"&newTeamName="+team_name;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
						if(clickBtn=="0"){
						}else if(clickBtn!="0"){
							var whichTr=clickBtn.parents("tr");	
							whichTr.find(".team-captain").html(newCaptain);
							whichTr.find(".team-name").html(team_name);
						}
					modalPop.modal('hide');
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	});
}

//编辑小组
function editTeamInfo(modalPop,team,teamId,captain,captain_name,memberListTab,clickBtn){
	urlx="/tiku_ops/user/getUsersListByTeamId?teamId="+teamId;
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx,
		async:false, 
		success:function(obj){
			if(obj.status==0){
				var selectHtml;
				var bodyHtml,titleHtml;
				var roleSelect;
				$.each(obj.result,function(i,n){
					if(n.name == captain) {
						selectHtml+='<option selected="selected" value="'+n.id+'">'+n.name+'</option>';
					} else {
						selectHtml+='<option value="'+n.key+'">'+n.name+'</option>';
					}
				});
				if(clickBtn=="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input type="text" value="" maxLength="30"/></p>'
						+'<p><span style="display:inline-block;text-align:right;width:110px">组长：</span>'+selectHtml+'</p>'
					    );
				} else if(clickBtn!="0"){
					selectHtml="<select id='provName'>"+selectHtml+"</select>";
					modalPop.find(".modal-body").html('<p><span style="display:inline-block;text-align:right;width:110px">小组：</span><input class="team_name" type="text" value='+team+'></p>'
                    + '<p><span style="display:inline-block;text-align:right;width:110px">当前组长：</span><td class="captain" /> '+captain_name+'</td></p>'
					+'<p><span style="display:inline-block;text-align:right;width:110px">新组长：</span>'+selectHtml+'</p>');
				}
			}else{
				alert(obj.msg);
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	$("#popSure").off().click(function(){
		var team_name=$(".team_name").val();
		var oldCaptain = captain;
		var newCaptain = $("#provName").find("option:selected").text();
		var new_phone = $("#provName").find("option:selected").val();
		var urlx;
		if(clickBtn=="0"){
		}else if(clickBtn!="0"){
			urlx="/tiku_ops/tranops/updateTeam?teamId="+teamId+"&oldCaptain="+oldCaptain+"&newCaptain="+new_phone+"&newTeamName="+team_name;
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
						if(clickBtn=="0"){
						}else if(clickBtn!="0"){
							var whichTr=clickBtn.parents("tr");	
							whichTr.find(".team-captain").html(newCaptain);
							whichTr.find(".team-name").html(team_name);
						}
					modalPop.modal('hide');
				}else{
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	});
}
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
function querySource(config) {
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
}
/**
 * 添加书籍
 * @param {jQuery} modalPop modal浮层对象
 * @param {Object} config 配置参数
 * @config {Function} callback 回调函数
 * @example
 *		addBook(modalPop, {
 *			callback: function(data) {
 *				//prepend content
 *				$('#memberListTab').find('tbody').prepend([
 *					'<tr>',
 *					 	'<td class="name">',
 *					 		'<span class="name_show">' + data.name + '</span>',
 *					 		'<input type="hidden" value="' + data.id + '" class="Id">',
 *					 	'</td>',
 *				 	 	'<td class="ISBN">' + data.isbn + '</td>',
 *						'<td class="grade">' + data.grade + '</td>',
 *						'<td class="subject">' + data.subject + '</td>',
 *						'<td class="publishingHouse">' + data.publishingHouse + '</td>',
 *						'<td class="sourceId">' + data.sourceId + '</td>',
 *						'<td class="createTime">' + data.createTime + '</td>',
 *						'<td>',
 *							'<button class="btn btn-primary btn-xs ml10 editBtn" data-toggle="modal" data-target=".modal">增加来源</button>',
 *						'</td>',
 *					'</tr>''
 *				].join(''));
 *			}
 *		});
 */
function addBook(modalPop, config) {
	var _body = modalPop.find('.modal-body'),
		_title = modalPop.find('.modal-title'),
		_footer = modalPop.find('.modal-footer'),
		_queryName,
		_queryBtn,
		_queryResult,
		_confirm = $('#popSure'),
		validate;
	config = config || {};
	_title.text('添加书籍');
	_body.html([
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
	].join(''));
	_confirm.text('添加书籍');
	_footer.find('.btn-default').text('取消');
	_queryName = _body.find('.query-name');
	_queryBtn = _body.find('.query-btn');
	_queryResult = _body.find('.query-result');
	_queryBtn.on('click', function(e) {
		var name = $.trim(_queryName.val());
		if(!name) {
			alert('请输入有效的来源名称');
			return false;
		}
		querySource({
			data: {
				name: name
			},
			callback: function(data) {
				var html = [];
				$.each(data.result, function(i, o) {
					html.push([
						'<div class="col-lg-6">',
							'<div class="input-group">',
								'<span class="input-group-addon">',
									'<input type="radio" name="source" data-id="', o.id ,'">',
								'</span>',
								'<span class="form-control">', o.name,'</span>',
							'</div>',
						'</div>'
					].join(''));
				});
				_queryResult.html(html.join(''));
			}
		});
	});
	modalPop.on('hide', function(e) {
		_confirm.off();
	});
	_confirm.off().on('click', function(e) {
		// 	书籍新增--\book\booksInfoList.jsp
		// http://127.0.0.1:8080/tiku_ops/book/addBookInfo?name=高中3年级数学&isbn
		// 978700000&subject=数学&grade=高三&publishing_house=清华出版社&source_id=1
		validate(_body, function(data) {
			$.ajax({
				url: basePath + 'book/addBookInfo',
				type: 'get',
				dataType: 'json',
				data: data,
				success: function(data) {
					var html = [];
					if(data.status == 0) {
						if(config.callback) {
							config.callback(data.result);
						}
						modalPop.modal('hide');
					} else {
						alert(data.msg);
					}
				}
			});
		});
	});
	validate = function(container, callback) {
		var _name, _isbn, _publishingHouse,
			data = {};
		// 书名验证
		_name = container.find('.book-name');
		data.name = $.trim(_name.val());
		if(!data.name) {
			alert('书名不能为空');
			_name.val('').focus();
			return;
		}
		_isbn = container.find('.book-isbn');
		data.isbn = $.trim(_isbn.val());
		// 国内图书ISBN编号前四位为9787
		data.isbn = /^9787\d+$/.test(data.isbn) ? data.isbn : '';
		data.subject = container.find(".book-subject :selected").text();
		data.grade = container.find(".book-grade :selected").text();
		// 出版社验证
		_publishingHouse = container.find('.book-publishing-house');
		data.publishing_house = $.trim(_publishingHouse.val());
		if(!data.publishing_house) {
			alert('出版社不能为空');
			_publishingHouse.val('').focus();
			return;
		}
		// ISBN与来源验证
		data.source_id = _queryResult.find(':checked').data('id');
		if(!(data.isbn || data.source_id)) {
			alert('ISBN或者来源至少填写其中一项');
			_isbn.val('9787').focus();
			return;
		}
		if(callback) {
			callback(data);
		}
	};
}
/**
 * 添加来源
 * @param {jQuery} modalPop modal浮层对象
 * @param {Object} config 配置参数
 * @config {String} name 书名
 * @config {String} isbn 书籍ISBN编号
 * @config {String} grade 年级
 * @config {String} subject 科目
 * @config {String} publishingHouse 出版社
 * @config {String} id 书籍id
 * @config {Function} callback 回调函数
 * @example
 * 		addSource2Book(modalPop, {
 * 			name: '高考必刷题',
 * 			isbn: '1234567890',
 * 			grade: '高中',
 * 			subject: '数学',
 * 			publishingHouse: '外语教学与研究出版社',
 * 			id: '12345',
 * 			callback: function(data) {
 * 				$(target).closest('tr').find('.sourceId').text(data.sourceId);
 * 			}
 * 		});
 */
function addSource2Book(modalPop, config) {
	var _body = modalPop.find('.modal-body'),
		_title = modalPop.find('.modal-title'),
		_footer = modalPop.find('.modal-footer'),
		_queryName,
		_queryBtn,
		_queryResult,
		_confirm = $('#popSure'),
		validate;
	config = config || {};
	_title.text('添加来源');
	_body.html([
		'<div class="modal-add-book">',
			'<div class="input-group">',
				'<span class="input-group-addon">书名</span>',
				'<span class="form-control">' + config.name + '</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">ISBN</span>',
				'<span class="form-control">' + config.isbn + '</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">年级</span>',
				'<span class="form-control">' + config.grade + '</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">科目</span>',
				'<span class="form-control">' + config.subject + '</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">出版社</span>',
				'<span class="form-control">' + config.publishingHouse + '</span>',
			'</div>',
			'<div class="input-group">',
				'<span class="input-group-addon">来源</span>',
				'<input type="text" class="form-control query-name" placeholder="输入要查询的来源名称" style="width:87%;margin-right:9px">',
				'<button type="button" class="btn btn-primary query-btn">查询</button>',
			'</div>',
			'<div class="row query-result"></div>',
		'</div>'
	].join(''));
	_confirm.text('添加来源');
	_footer.find('.btn-default').text('取消');
	_queryName = _body.find('.query-name');
	_queryBtn = _body.find('.query-btn');
	_queryResult = _body.find('.query-result');
	_queryBtn.on('click', function(e) {
		var name = $.trim(_queryName.val());
		if(!name) {
			alert('请输入有效的来源名称');
			return false;
		}
		querySource({
			data: {
				name: name
			},
			callback: function(data) {
				var html = [];
				$.each(data.result, function(i, o) {
					html.push([
						'<div class="col-lg-6">',
							'<div class="input-group">',
								'<span class="input-group-addon">',
									'<input type="radio" name="source" data-id="', o.id ,'">',
								'</span>',
								'<span class="form-control">', o.name,'</span>',
							'</div>',
						'</div>'
					].join(''));
				});
				_queryResult.html(html.join(''));
			}
		});
	});
	modalPop.on('hide', function(e) {
		_confirm.off();
	});
	_confirm.off().on('click', function(e) {
		// 书籍列表    增加来源---\book\addBookSourceInfo.jsp
		// http://127.0.0.1:8080/tiku_ops/book/addBookSourceInfo?id=8&&source_id=1
		validate(_body, function(data) {
			data.id = config.id || '';
			$.ajax({
				url: basePath + 'book/addBookSourceInfo',
				type: 'get',
				dataType: 'json',
				data: data,
				success: function(data) {
					var html = [];
					if(data.status == 0) {
						if(config.callback) {
							config.callback(data.result);
						}
						modalPop.modal('hide');
					} else {
						alert(data.msg);
					}
				}
			});
		});
	});
	validate = function(container, callback) {
		var data = {};
		data.source_id = _queryResult.find(':checked').data('id');
		// 来源验证
		if(!data.source_id) {
			alert('请选择要添加的来源');
			return;
		}
		if(callback) {
			callback(data);
		}
	};
}
function addCheck() {
	document.getElementById("check_mask").setAttribute("style","display:block;");
	document.getElementById("check_form").setAttribute("style","display:block;");
	urlx="/tiku_ops/tranops/getAllTikuTeam?page=0";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx,
		async:false,
		success:function(obj){
			if(obj.status==0){
				var selectHtml="";
				$.each(obj.result,function(i,n){
					selectHtml+='<tr><td><input class="chk" name="checkbox" type="checkbox" value='+n.id+'></td><td>'+(i+1)+'</td><td>'+n.captainName+'</td><td>'+n.usersNum+'</td></tr>'
				});
				document.getElementById("table_frame").innerHTML = '<table id="checkTeam" border="1" cellpadding="0" cellspacing="0"><tr><th><input id="chk_all" type="checkbox"></th><th>序号</th><th>组长</th><th>小组人数</th></tr>'+selectHtml+'</table>';
			}
		},
		error:function(){
			alert("网络错误，请稍后重试");
		}
	});
	var checkList = $("tbody tr input[name='checkbox']");
	$("#chk_all").off().click(function(){
		if($(this).is(":checked")){
			checkList.each(function(){
				$(this).prop("checked",true);
			});
		}else{
			checkList.each(function(){
				$(this).prop("checked",false);
			});
		};
	});
	$("#popSure").off().click(function(){
		var now="";
		var start_time=document.getElementById("startTime").value;
		var end_time=document.getElementById("endTime").value;
		var num=$("#check_num").val();
		var team_id="";
		for(var i=0;i<$(".chk").length;i++) {
			if(document.getElementsByClassName("chk")[i].checked == false) {

			} else if(document.getElementsByClassName("chk")[i].checked == true) {
				team_id+=document.getElementsByClassName("chk")[i].value+",";
			}
		}
		var urlx="/tiku_ops/check/generateCheckRecord?startTime="+start_time+"&endTime="+end_time+"&n="+num+"&teamIds="+team_id;
		$.ajax({
			type:"get",
			dataType:"json",
			url:urlx,
			async:false, 
			success:function(obj){
				if(obj.status==0){
					location.href = 'checkRecordList?';
				} else {
					alert(obj.msg);
				}
			},
			error:function(){
				alert("网络错误，请稍后重试");
			}
		});
	});
}
function closeCheck() {
	document.getElementById("check_mask").setAttribute("style","display:none;");
	document.getElementById("check_form").setAttribute("style","display:none;");
}


function modalSize(modal,size,cSize){
	modal.removeClass(size).addClass(cSize);
}

function openPhoto() {
	var src = event.target.src;
	window.open(src,'newwindow','alwaysRaised=yes,z-look=yes');
}