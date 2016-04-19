<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.EnumBank" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<title>编辑用户</title>
<meta http-equiv="keywords" content="">
<meta http-equiv="description" content="">
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath %>css/moodstrap/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath %>css/moodstrap/font-awesome.css"> 
<link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath %>css/moodstrap/widgets.css">
<link href="<%=basePath %>css/moodstrap/supreme.css" rel="stylesheet">
<style type="text/css">
	.section-left{float:left;width:28%;}
	.section-right{float:left;width:60%;}
</style>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
</head>
<body>
	<div class="content">
		<!-- Main bar -->
		<div class="mainbar">
			<!-- Page heading -->
			<div class="page-head">
				<h2>填写个人信息</h2>
				<!-- Breadcrumb -->
				<div class="bread-crumb">填写个人信息</div>
			</div>
			<div class="matter">
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-head">
									<div class="pull-left">个人信息</div>
									<div class="pull-right" style="font-weight:normal;">
										账号：<span id="user-key">${ userinfo.userKey }</span>&nbsp;&nbsp;
										手机号：${ userinfo.userMobile }
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="padd">
										<form>
											<fieldset>
												<div class="form-group" style="width:30%;float:left;">
													<label for="user-name">姓名</label>
													<input type="text" class="form-control" value="${ userinfo.userName }" id="user-name" placeholder="姓名">
												</div>
												<div class="form-group" style="width:68%;float:right;">
													<label for="id-card-num">身份证号</label>
													<input  type="text"  class="form-control" value="${ userinfo.idNumber }" id="id-card-num" <c:if test="${ userinfo.idNumber != null }">disabled</c:if> placeholder="身份证号">
												</div>
												<div class="clearfix"></div>
												<div>身份证号为18位有效字符，身份证号有x，请输入x，其他字符不允许输入。一经填写，永久不可更改。</div>
											</fieldset>
										</form>
									</div>
								</div>
							</div>
							<div class="widget" style="width:49%;float:left;">
								<div class="widget-head">
									<div>收款信息</div>
								</div>
								<div class="widget-content">
									<div class="padd">
										<form>
											<fieldset>
												<div class="form-group">
													<label for="bank">开户行</label>
														<select class="form-control" id="bank" name="bank">
															<option class="banks" value="">请选择开户行</option>
															<c:set var="enumBanks" value="<%= EnumBank.values() %>"/>
															<c:forEach var="enumBank" items="${ enumBanks }">
															<option value="${ enumBank.id }" <c:if test="${ enumBank.id == userinfo.bank }">selected = "selected"</c:if>>${ enumBank.name }</option>
															</c:forEach>
														</select>
													</div>
													<div class="form-group">
														<label for="bank-subbranch">支行名称</label>
														<input type="text" class="form-control" id="bank-subbranch" value="${ userinfo.bankSubbranch }"placeholder="开户支行名称，如：中关村支行营业室">
													</div>
													<div class="form-group">
														<label for="account">银行账号</label>
														<input type="text" class="form-control" id="account" value="${ userinfo.bankCard }" placeholder="银行账号">
													</div>
													<div class="form-group">
														<label for="province">开户地</label>
														<div class="clearfix"></div>
														<input type="text" style="width:32%;float:left;" class="form-control" id="province" value="${ userinfo.province }" placeholder="省/直辖市/自治区">
														<input type="text" style="width:32%;float:left;margin:0 2%;" class="form-control" id="city" value="${ userinfo.city }" placeholder="地级市">
														<input type="text" style="width:32%;float:left;" class="form-control" id="county" value="${ userinfo.county }" name="district" placeholder="县/区/县级市/旗…">
														<div class="clearfix"></div>
													</div>
												</div>
											</fieldset>
										</form>
										<div class="clearfix"></div>
									</div>
								</div>
							</div>
							<div class="widget" style="width:49%;float:right;">
								<div class="widget-head">
									<div class="pull-left">修改密码</div>
									<button class="btn btn-xs btn-default" style="float:right;" data-enable="true" id="active-password">
										<i class="fa fa-wrench"></i>
									</button>
									<div class="clearfix"></div>
							</div>
							<div class="widget-content">
								<div class="padd">
									<form>
										<fieldset>
											<div class="form-group">
												<label for="old-password">原密码</label>
												<input type="password" class="form-control" id="old-password" disabled placeholder="原密码">
											</div>
											<div class="form-group">
												<label for="new-password">新密码</label>
												<input type="password" class="form-control" id="new-password" disabled placeholder="新密码">
											</div>
											<div class="form-group">
												<label for="new-password2">确认新密码</label>
												<input type="password" class="form-control" id="new-password2" disabled placeholder="确认新密码">
											</div>
										</fieldset>
									</form>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
						<button id="edit-user" type="submit" class="btn btn-primary btn-block">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				var activePassword = $('#active-password'),
					oldPassword = $('#old-password'),
					newPassword = $('#new-password'),
					newPassword2 = $('#new-password2'),
					editUser = $('#edit-user');
				// 修改密码
				activePassword.on('click', function(e) {
					var el = $(this), enable = !el.data('enable');
					oldPassword.prop('disabled', enable);
					newPassword.prop('disabled', enable);
					newPassword2.prop('disabled', enable);
					el.data('enable', enable);
				});
				editUser.on('click', function(e) {
					var userName = $('#user-name'),
						idCardNum,
						bankSubbranch,
						account,
						province,
						city,
						county,
						data = {};
					// 表单验证
					data.userName = userName.val().trim();
					if(!data.userName) {
						alert('姓名不能为空');
						userName.focus();
						return false;
					}
					idCardNum = $('#id-card-num');
					data.idNumber = idCardNum.val().trim();
					if(!/\d{14,17}(?:\d|x)/i.test(data.idNumber)) {
						alert('身份证号不合法');
						idCardNum.focus();
						return false;
					}
					data.bank = $('#bank').find(':selected').val();
					if(!data.bank) {
						alert('请选择开户行');
						return false;
					}
					bankSubbranch = $('#bank-subbranch');
					data.bankSubbranch = bankSubbranch.val().trim();
					if(!data.bankSubbranch) {
						alert('请填写具体的开户支行信息后提交');
						bankSubbranch.focus();
						return false;
					}
					account = $('#account');
					data.bankCard = account.val().trim();
					if(!data.bankCard) {
						alert('银行账号不能为空');
						account.focus();
						return false;
					}
					province = $('#province');
					data.province = province.val().trim();
					if(!data.province) {
						alert('请填写完整的开户地信息后提交');
						province.focus();
						return false;
					}
					city = $('#city');
					data.city = city.val().trim();
					if(!data.city) {
						alert('请填写完整的开户地信息后提交');
						city.focus();
						return false;
					}
					county = $('#county');
					data.county = county.val().trim();
					if(!data.county) {
						alert('请填写完整的开户地信息后提交');
						county.focus();
						return false;
					}
					if(!activePassword.data('enable')) {
						data.password = oldPassword.val();
						if(data.password.length < 4) {
							alert('请输入正确的密码');
							oldPassword.focus();
							return false;
						}
						data.passwordNew = newPassword.val();
						if(data.passwordNew.length < 4) {
							alert('请输入正确的密码');
							newPassword.focus();
							return false;
						}
						data.passwordNew2 = newPassword2.val();
						if(data.passwordNew2.length < 4) {
							alert('请输入正确的密码');
							newPassword2.focus();
							return false;
						}
						if(data.passwordNew !== data.passwordNew2) {
							alert('两次输入新密码不一致');
							newPassword.val('').focus();
							newPassword2.val('');
							return false;
						}
					}
					data.userKey = $('#user-key').text();
					// 提交表单
					$.ajax({
						url: window.basePath + 'user/updateBankUserInfo',
						contentType: 'application/x-www-form-urlencoded;charset=utf-8',
						type: 'GET',
						data: data,
						dataType: 'json',
						success: function(data) {
							alert(data.msg);
							if(data.status === 0 || data.status === 2) {
								top.location.href = window.basePath + 'index.jsp';
							}
						},
						error: function() {
							alert("网络错误，请稍后重试");
						}
					});
				});
			});
		}
	});
</script>
</body>
</html>
