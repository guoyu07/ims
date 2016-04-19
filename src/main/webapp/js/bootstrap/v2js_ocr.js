function generateCheckRecord() {
	var urlx;
	$('#check_mask').css('display', 'block');
	$('#check_form').css('display', 'block');
	urlx = '/tiku_ops/tranops/getAllTikuTeam?page=0';
	$.ajax({
		type: 'get',
		dataType: 'json',
		url: urlx,
		async: false,
		success: function(obj) {
			if(obj.status == 0){
				var selectHtml = "";
				$.each(obj.result,function(i,n){
					selectHtml += '<tr><td><input class="chk" name="checkbox" type="checkbox" value=' + n.id + '></td><td>' + (i + 1) + '</td><td>' + n.captainName + '</td><td>' + n.usersNum + '</td></tr>';
				});
				$('#table_frame').html('<table id="checkTeam" border="1" cellpadding="0" cellspacing="0"><tr><th><input id="chk_all" type="checkbox"></th><th>序号</th><th>组长</th><th>小组人数</th></tr>' + selectHtml + '</table>');
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
		}
	});
	$("#popSure").off().click(function(){
		var start_time = $('#startTime').val(),
			end_time= $('#endTime').val(),
			num = $('#check_num').val(),
			team_id = '';
			team_id = $.map($('.chk').filter(':checked'), function(o) {
				return $(o).val();
			}).join();
		urlx="/tiku_ops/piccheck/generateCheckRecord?startTime="+start_time+"&endTime="+end_time+"&n="+num+"&teamIds="+team_id;
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
//导入字符压缩包
function importImgs(modalPop,memberListTab,clickBtn) {
	var uploads = [],
		files,
		reader = [];
	modalPop.find(".modal-title").html("选择图片压缩包");
	modalPop.find(".modal-body").html('<p><div id="getImgUrlLoc"><input id="SelectImg" class="img-select" type="file" multiple="multiple"></div></p>');
	$("#SelectImg").change(function() {
		var i;
		files = document.getElementById('SelectImg').files;
		var files_length = files.length;
		for(i = 0; i < files_length; i++) {
			reader[i] = new window.FileReader();
			reader[i].readAsBinaryString(files[i]);
		}
	});

	$("#popSure").prop('disabled', false).off().on('click', function(){
		var img_src = window.basePath + "image/loading.gif", _body;
		_body = modalPop.find(".modal-body").html('<div style="text-align:center;">正在上传，请等待…</div><br><img src='+img_src+' style="display:block;width:24px;margin:0 auto;">');
		$(this).prop('disabled', true);
		MX.load({
			js: 'lib/sea',
			version: '150528',
			success: function() {
				seajs.use(['util/uploadFile', 'util/jquery.tmpl'], function(uploader) {
					uploader(files, {
						url: window.basePath + 'recognition/addPic',
						end: function(results) {
							var i, len, errorResults, error, container;
							errorResults = results.filter(function(o) {
								return o.status !== 0;
							});
							len = errorResults.length;
							container = _body;
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
				});
			}
		});
	});
}


//创建识别人
function addMemberRec(modalPop,roles,statuss,memberListTab,clickBtn){
	var urlx = "/tiku_ops/user/getTikuTeamList";
	$.ajax({
		type:"get",
		dataType:"json",
		url:urlx, 
		async:false,
		success:function(obj){
			if(obj.status==0){
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
		var reg = /^1\d{10}$/;
		if(reg.test(userMobile) == false){
			alert("手机号码不正确");
		}else{
		var password=$("#password").val();
		var userKey=$("#userKey").val();
		var userName=$("#userName").val();
		urlx="/tiku_ops/user/addRecognitionUserInfo?userKey="+userKey+"&userName="+userName+"&userMobile="+userMobile+"&password="+password+"&status=0";
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
					if(obj.status==0){//添加
						modalPop.modal('hide');
						location.href = window.basePath + "user/recognitionUserInfoList";
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

function importPicsBef() {}

/**
导入批量识别图片
@param  {jQuery} modalPop modal浮层对象
@example
		batchRec(modalPop);
 */
function batchRec(modalPop) {
	var _title = modalPop.find('.modal-title'),
		_body = modalPop.find('.modal-body'),
		_footer = modalPop.find('.modal-footer'),
		_confirm = $('#popSure'),
		_queryName, _queryBtn, _queryResult,
		bookId,
		files, reader, uploads;
	_title.text('识别图片');
	_body.html([
		'<div class="modal-add-book">',
			'<div class="modal-guide">选择要识别的书籍:</div>',
			'<div class="input-group modal-nav">',
				'<input type="text" class="form-control query-name" placeholder="输入要书名或ISBN查询" style="width:87%;margin-right:9px">',
				'<button type="button" class="btn btn-primary query-btn">查询</button>',
			'</div>',
			'<div class="row query-result"></div>',
			'<div class="modal-guide">上传书籍图片:</div>',
			'<div class="input-group">',
				'<input id="select-img" type="file" accept="image/*" multiple=“multiple” class="btn btn-primary">',
			'</div>',
		'</div>'
	].join(''));
	_confirm.text('开始识别');
	_footer.find('btn-default').text('取消');
	_queryName = _body.find('.query-name');
	_queryBtn = _body.find('.query-btn');
	_queryResult = _body.find('.query-result');
	_queryBtn.on('click', function(e) {
		var name = $.trim(_queryName.val());
		if(!name) {
			alert('请输入有效的书名或ISBN');
			return false;
		}
		window.querySource({
			url: 'book/booksList',
			data: {
				nameisbn: name
			},
			callback: function(data) {
				var html = [];
				$.each(data.result, function(i, o) {
					html.push([
						'<div class="col-lg-6">',
							'<div class="input-group">',
								'<span class="input-group-addon">',
									'<input type="radio" name="book" data-id="' + o.id + '">',
								'</span>',
								'<span class="form-control modal-mutil-line">书名:' + o.name + '<br/>出版:' + o.publishingHouse + '<br/>ISBN:' + o.isbn + '</span>',
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
	uploads = [];
	reader = [];
	$('#select-img').change(function(e){
		var filesLength, i;
		files = this.files;
		filesLength = files.length;
		if(filesLength <= 500) {
			for(i = 0;i < filesLength; i++) {
				reader[i] = new window.FileReader();
				reader[i].readAsBinaryString(files[i]);
			}
		} else {
			alert('请不要同时上传超过500张图片，并控制每张图片的数据量大小!');
			modalPop.modal('hide');
			return;
		}
	});

	_confirm.off().click(function(){
		var imgSrc = window.basePath + 'image/loading.gif',
			fileLen = (files || []).length;
		bookId = _queryResult.find(':checked').data('id');
		if(!bookId) {
			alert('请选择图书');
			return;
		}
		if(!fileLen) {
			alert('请上传图片');
			return;
		}
		_body.html('<div style="text-align:center;">正在上传，请等待…</div><br><img src=' + imgSrc + ' style="display:block;width:24px;margin:0 auto;">');
		$(this).prop('disabled', true);
		//sendFile(files, 0, fileLen);
		MX.load({
			js: 'lib/sea',
			version: '150528',
			success: function() {
				seajs.use(['util/uploadFile', 'util/jquery.tmpl'], function(uploader) {
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
							container = _body;
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
				});
			}
		});
	});
}
function computeRecPercent(target,batchId,status,userKey,startTime,endTime){
	var xhr = new XMLHttpRequest();
    xhr.open("GET",basePath + "picture/computeRecPercent?target="+target + "&batchId="+ batchId+"&status="+ status+"&userKey="+ userKey+"&startTime="+ startTime+"&endTime="+ endTime);
    xhr.setRequestHeader('Content-Type','multipart/form-data;');
    xhr.overrideMimeType("application/octet-stream");
    xhr.send(null);
    
	xhr.onreadystatechange = function() {
    	if(xhr.readyState == 4) {
	    	var obj = eval("("+xhr.responseText+")");
	    	var res = {status:obj.status,msg:obj.msg,result:obj.result.msg};
	    	$("#computeResult").text(obj.result.msg);
        }
    };
}

function getBatchResult(batchid,hasFilter,page){
	var url,targetPage;
	if(page){
		targetPage=page;
	}else{
		targetPage=0;
	}
	if(hasFilter){
		url ="/tiku_ops/batchpicture/orcPictureViewSearch?batchId=" + batchid + "&status=" + status + "&target=" + target
			+ "&userKey=" + userKey + "&startTime=" + startTime
			+ "&endTime=" + endTime+"&page="+targetPage+"&displayMode="+displayModeVal;
	}else{
		url ="/tiku_ops/batchpicture/orcPictureViewSearch?&page="+targetPage+"&displayMode="+displayModeVal;
	}
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: '/tiku_ops/batchfileupload/getBatchResult?batchId='+batchid,
		success: function (data) {
			if(data.msg=='ok'){
				location.href=url;
			}
		},
		error: function (xhr, status, error) {

		}
	})
}