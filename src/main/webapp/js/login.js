
function checkFormInput() {
	var flag = true;
	var loginId = $("#loginId").val();
	var password = $('#password').val();
	if (null == loginId || '' == loginId || null == password || '' == password) {
		alert("用户名和密码不能为空");
		flag = false;
	}
	return flag;
}

$(document).ready(function () {
	document.onkeydown = function() {
		if (event.keyCode == 13) {
			if (checkFormInput()) {
				$('form')[0].submit();
			}
		}
	};

	$('#loginId').focus(function() {
		this.focus();
		this.select();
	}).focus();

	$('#password').focus(function() {
		this.focus();
		this.select();
	});

	$('#submit-btn').click(function() {
		if (checkFormInput()) {
			$('form')[0].submit();
		}
	});

	$('#reset-btn').click(function() {
		$('#loginId').val('').focus();
		$('#password').val('');
	});

});