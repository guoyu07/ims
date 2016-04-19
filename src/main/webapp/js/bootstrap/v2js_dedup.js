function modalSize(modal,size,cSize){
	modal.removeClass(size).addClass(cSize);
};

function generateCheckRecord(modalPop) {
	modalPop.find(".modal-title").html("生成抽检");
	modalPop.find(".modal-body").html([
		'<p>',
			'<span style="display:inline-block;text-align:right;width:110px">每包抽查：</span>',
			'<input type="text" value="" id="record-length"  maxLength="30"/>',
		'</p>'
	].join(''));
	$("#popSure").text('生成抽检').off().on('click', function(e) {
		var num = $('#record-length').val().trim();
		if(/\d+/.test(num)) {
			$.ajax({
				url: '/tiku_ops/dedup/generateCheckRecord',
				type: "GET",
				data: {
					num: num
				},
				dataType:"json",
				success: function(obj) {
					if(obj.status == 0){
						location.reload();
					} else {
						alert(obj.msg);
					}
				},
				error: function() {
					alert("网络错误，请稍后重试");
				}
			});
		} else {
			alert('请输入合法的长度');
		}
	});
}
function closeCheck() {
	document.getElementById("check_mask").setAttribute("style","display:none;");
	document.getElementById("check_form").setAttribute("style","display:none;");
}
//导入字符压缩包
function importImgs(modalPop,memberListTab,clickBtn) {
	var uploads = [],
		files,
		reader = [];
	modalPop.find(".modal-title").html("选择图片压缩包");
	modalPop.find(".modal-body").html('<p><div id="getImgUrlLoc"><input id="SelectImg" class="img-select" type="file" multiple="multiple"></div></p>');

	if(XMLHttpRequest.prototype.sendAsBinary === undefined) {
		XMLHttpRequest.prototype.sendAsBinary = function(text) {
			var i, len = text.length,
				data = new window.ArrayBuffer(len),
				ui8a = new window.Uint8Array(data, 0);
			for (i = 0; i < len; i++) {
				ui8a[i] = text.charCodeAt(i) & 0xff;
			}
			this.send(ui8a);
		};
	}

	// var ii = 0;
	function sendFile(files, i, n) {
		var j;
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4) {
				var obj = eval("("+xhr.responseText+")");
				var res = {status:obj.status,msg:obj.msg};
				uploads.push(res);
				sendFile(files,i+1,n);
			}
		};
		if(i<n) {
			xhr.open("POST", window.basePath + "recognition/addPic?fileName="+files[i].name);
			xhr.setRequestHeader('Content-Type','multipart/form-data;');
			xhr.overrideMimeType("application/octet-stream");
			xhr.sendAsBinary(reader[i].result);
		} else {
			var has_err=false;
			var errs="<ul>";
			for(j = 0; j < n; j++) {
				if(uploads[j].status==0) {
					uploads[j].err="";
				} else {
					uploads[j].err=files[j].name+"-"+uploads[j].msg;
					has_err=true;
					errs+="<li>"+uploads[j].err+"</li>";
				}
			}
			errs+="</ul>";
			var upload_log;
			if(has_err==true) {
				upload_log = "<div style='color:#c00;'>本组图片处理完毕，以下压缩包上传失败：</div>"+errs+"<div style='color:#c00;'>请修复问题文件重新上传！</div>";
				// console.info("本组图片处理完毕，以下压缩包上传失败："+errs+"请修复问题文件重新上传！");
			} else {
				upload_log = "本组图片处理完毕，全部上传成功！";
				// console.info("本组图片处理完毕，全部上传成功！");
			}
			modalPop.find(".modal-body").html(upload_log);
			$("#popSure").off().click(function(){
				modalPop.modal('hide');
			});
		}
	}
	$("#SelectImg").change(function() {
		var i;
		files = document.getElementById('SelectImg').files;
		var files_length = files.length;
		for(i = 0; i < files_length; i++) {
			reader[i] = new window.FileReader();
			reader[i].readAsBinaryString(files[i]);
		}
	});

	$("#popSure").off().click(function(){
		var img_src = window.basePath + "image/loading.gif";
		modalPop.find(".modal-body").html('<div style="text-align:center;">正在上传，请等待…</div><br><img src='+img_src+' style="display:block;width:24px;margin:0 auto;">');
		sendFile(files,0,files.length);
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
 * 导入批量识别图片
 * @param  {jQuery} modalPop modal浮层对象
 * @example
 * 		batchRec(modalPop);
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
	XMLHttpRequest.prototype.sendAsBinary = XMLHttpRequest.prototype.sendAsBinary || function(text){
		var len = text.length,
			data = new window.ArrayBuffer(len),
			ui8a = new window.Uint8Array(data, 0),
			i;
		for (i = 0; i < len; i++) {
			ui8a[i] = text.charCodeAt(i) & 0xff;
		}
		this.send(ui8a);
	};
	uploads = [];
	function sendFile(files, i, n) {
		var xhr = new XMLHttpRequest(),
			hasErr, errs, j, uploadLog;

		xhr.onreadystatechange = function() {
			var obj, res;
			if(xhr.readyState == 4) {
				obj = JSON.parse(xhr.responseText);
				res = {
					status: obj.status,
					msg: obj.msg
				};
				uploads.push(res);
				sendFile(files, i+1, n);
			}
		};
		if(i < n) {
			xhr.open("POST", window.basePath + "fileupload/batchRecognitionBook?fileName=" + files[i].name + '&bookId=' + bookId);
			xhr.setRequestHeader('Content-Type', 'multipart/form-data;');
			xhr.overrideMimeType("application/octet-stream");
			xhr.sendAsBinary(reader[i].result);
		} else {
			hasErr = false;
			errs = [];
			for(j = 0; j < n; j++) {
				if(uploads[j].status == 0) {
					uploads[j].err = "";
				} else {
					uploads[j].err = files[j].name + "-" + uploads[j].msg;
					hasErr = true;
					errs.push(uploads[j].err);
				}
			}
			errs = '<ul><li>' + errs.join('</li><li>') + '</li></ul>';
			if(hasErr) {
				uploadLog = "<div style='color:#c00;'>本组图片处理完毕，以下文件识别失败：</div>"+errs+"<div style='color:#c00;'>请修复问题文件重新上传！</div>";
				// console.info("本组图片处理完毕，以下压缩包上传失败："+errs+"请修复问题文件重新上传！");
			} else {
				uploadLog = "本组图片处理完毕，全部上传成功！";
				// console.info("本组图片处理完毕，全部上传成功！");
			}
			_body.html(uploadLog);
			_confirm.off().click(function(){
				modalPop.modal('hide');
				location.href = window.basePath + "/picture/orcPictureViewSearch";
			});
		}
	}
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
		sendFile(files, 0, fileLen);
	});
	

}