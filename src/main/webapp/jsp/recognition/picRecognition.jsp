<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.constant.Version" %>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCaptainCheckStatus"%>
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
<title>图片识别</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath%>image/exam/favicon.ico">
<link href="<%=basePath%>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>css/moodstrap/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>css/moodstrap/font-awesome.css">
<link href="<%=basePath%>css/moodstrap/frame.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>css/moodstrap/prettyPhoto.css">
<link href="<%=basePath%>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!--<link href="<%=basePath%>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap/bootstrap.min.js" /></script>-->
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath%>';
	(function(){MX=window.MX||{};var g=function(a,c){for(var b in c)a.setAttribute(b,c[b])};MX.load=function(a){var c=a.js,b=c?".js":".css",d=-1==location.search.indexOf("jsDebug"),e=a.js||a.css;-1==e.indexOf("http://")?(e=(a.path||window.basePath)+((c?"js/":"css/")+e)+(!d&&!c?".source":""),b=e+(a.version?"_"+a.version:"")+b):b=e;d||(d=b.split("#"),b=d[0],b=b+(-1!=b.indexOf("?")?"&":"?")+"r="+Math.random(),d[1]&&(b=b+"#"+d[1]));if(c){var c=b,h=a.success,f=document.createElement("script");f.onload=function(){h&&h();f=null};g(f,{type:"text/javascript",src:c,async:"true"});document.getElementsByTagName("head")[0].appendChild(f)}else{var c=b,i=a.success,a=document.createElement("link");g(a,{rel:"stylesheet"});document.getElementsByTagName("head")[0].appendChild(a);g(a,{href:c});i&&(a.onload=function(){i()})}}})();
</script>
<style type="text/css">
.font-unit {
	display: inline-block;
	min-height: 210px;
}

.photo-frame {
	width: 100%;
	height: 240px;
	overflow: hidden;
}

.photo-frame img {
	width: 100%;
	max-height: 100%;
	position: relative;
	bottom: 0;
}

img.no-chinese {
	width: 36px;
	height: 36px;
	border: none;
	display: inline-block;
}

.font-unit input {
	width: 100px;
	margin-left: 24px;
	display: inline-block;
	text-align: center
}
</style>
</head>
<body>
	<div class="content">
		<div class="mainbar">
			<div class="page-head">
				<h2 class="pull-left">
					<i class="fa fa-picture-o"></i> 图片识别
				</h2>
				<div class="clearfix"></div>
				<div class="bread-crumb" style="font-size: 16px;font-weight: bold;color: black">
					标记未识别——“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/no_chinese.png">”(快捷键：'Ctrl+M')。<span style="color: red;font-size: 24px">以下为识别说明示例图，具体按实际情况填写</span><br> 数字标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/digit.png">”&nbsp;&nbsp;英文字母标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/letter.png">”&nbsp;&nbsp;标点符号标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/punctuation.png">”&nbsp;&nbsp;汉字偏旁部首标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/chineseCharacterComponent1.png">”&nbsp; “<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/chineseCharacterComponent2.png">”&nbsp;&nbsp;字符切取有留白的标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/blank.png">”&nbsp;&nbsp;字符不完整标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/imperfect1.png">”&nbsp;“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/imperfect2.png">”<br>一个以上字符标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/multiCharacter.png">”&nbsp;&nbsp;字符不清晰标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/notClear.png">”&nbsp;&nbsp;每张图片只能识别填写一个汉字“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/perfect.png">”&nbsp;&nbsp;含非本字符的其他偏旁部首标记为未识别“<img style="width:32px;height:32px;" src="<%=basePath%>image/recognition/otherChineseCharacterComponent.png">”<br>有些字符图片高度过高但已经在方框内自适应显示，所以对于高度发生畸变的图片可以执行以下操作：右击-在新标签页中打开图片，从而查看原图
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="matter">
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-head">当前图片组</div>
								<div class="widget-content">
									<div class="padd">
										<div id="gallery" data-order="0">
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
											<div class="font-unit" style="display:none">
												<div class="photo-frame"></div>
												<input type="text" maxlength="1" class="form-control placeholder">
												<img class="no-chinese" src="<%=basePath%>image/recognition/no_chinese.png">
											</div>
										</div>
									</div>
									<div class="widget-foot" id="remind" style="color:#c00;"></div>
								</div>
							</div>
							<button id="update-pics" type="button" class="btn btn-primary btn-lg btn-block" style="width:40%;margin:0 auto;">提交识别结果</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	<script type="text/javascript">
	MX.load({
		js: 'lib/sea',
		version: '<%=Version.version %>',
		success: function() {
			seajs.use(['lib/jquery'], function($) {
				$(function() {
					var initPics, gallery, fontUnits, charactors, placeholders, photoFrames, remind, picNum = 0;
					gallery = $('#gallery');
					remind = $('#remind');
					fontUnits = gallery.find('.font-unit');
					placeholders = gallery.find('.placeholder');
					photoFrames = gallery.find('.photo-frame');
					gallery.on('click', '.no-chinese', function(e) {
						var el = $(this), fontUnit;
						fontUnit = el.closest('.font-unit');
						fontUnit.find('.placeholder').val('未识别');
						fontUnit.find('.charactor').data('status', -1);
						// 聚焦下一个
						fontUnit.next().find('.placeholder').focus();
					});
					gallery.on('keydown', function(e) {
						var order, el = $(this);
						// Ctrl + m
						if(e.ctrlKey && e.which === 77) {
							order = el.data('order');
							if(order < 10) {
								placeholders.eq(order).val('未识别');
								charactors.eq(order).data('status', -1);
								order += 1;
								placeholders.eq(order).focus();
								el.data('order', order);
							}
						}
					});
					initPics = function() {
						var i;
						// 置为第一个
						gallery.data('order', 0);
						// 聚焦第一个
						placeholders.first().focus();
						// 清空提醒
						remind.empty();
						fontUnits.hide();
						photoFrames.empty();
						placeholders.val('');
						$.ajax({
							url: window.basePath + 'recognition/obtainRemainPictureList',
							type: 'GET',
							dataType: 'json',
							success: function(data) {
								if(data.status === 1 || data.status === 0) {
									picNum = data.result.length;
									$.each(data.result, function(i) {
										photoFrames.eq(i).html('<img class="charactor" src="../' + this.filePath +'" data-file-id="'+ this.id + '" data-status="' + this.status + '">');
									});
									fontUnits.slice(0, picNum).show();
									charactors = gallery.find('.charactor');
								} else {
									alert(data.msg);
								}
							},
							error: function() {
								alert('网络错误，请稍后重试');
							}
						});
					};
					initPics();
					$('#update-pics').on('click', function(e) {
						var el = $(this).prop('disabled', true), data, i, flag;
						data = {
							fileIds: [],
							results: [],
							status: []
						};
						charactors.each(function(i) {
							var el = $(this), status, fileId, result;
							result = placeholders.eq(i).val();
							status = result.length === 1 ? 1 : el.data('status');
							if(status === 0) {
								flag = true;
								return false;
							}
							if(status === -1) {
								result = '';
							}
							data.fileIds.push(el.data('fileId'));
							data.results.push(result);
							data.status.push(status);
						});
						if(flag) {
							alert('有未处理图片，请将本组图片完全处理后提交！');
							return;
						}
						if(data.fileIds.length === 0) {
							alert('无须提交数据');
							return;
						}
						$.ajax({
							url: window.basePath + 'recognition/updateRecognitionList',
							type: 'GET',
							data: {
								fileIds: data.fileIds.join(),
								results: data.results.join(),
								status: data.status.join()
							},
							dataType: 'json',
							success: function(data) {
								remind.text(data.msg);
								if(data.status === 0) {
									initPics();
									el.prop('disabled', false);
								} else {
									alert(data.msg);
								}
							}
						});
					});
				});
			});
		}
	});
	</script>
</body>
</html>
