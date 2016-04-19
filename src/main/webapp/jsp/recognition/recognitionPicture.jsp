<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.xuexibao.ops.enumeration.TranOpsCaptainCheckStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<base href="<%=basePath%>">
<title>当前位置：图片识别</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<title>图片识别</title>
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
<link href="../../css/moodstrap/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="../../css/moodstrap/font-awesome.css"> 
<link href="../../css/moodstrap/frame.css" rel="stylesheet">
<link rel="stylesheet" href="../../css/moodstrap/prettyPhoto.css"> 
<link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	.font-unit{display:inline-block;min-height:210px;}
	.photo-frame{width:100%;height:160px;overflow:hidden;}
	.photo-frame img{width:100%;}
	img.no-chinese{width:36px;height:36px;border:none;display:inline-block;}
	.font-unit input{width:100px;margin-left:24px;display:inline-block;text-align:center}
</style>
</head>
<body>
	<div class="content">
		<div class="mainbar">
			<div class="page-head">
				<h2 class="pull-left"><i class="fa fa-picture-o"></i> 图片识别</h2>
				<div class="clearfix"></div>
				<div class="bread-crumb">
					请将图片中的文字输入到下方对应的输入框内，每张图片只能识别为一个汉字，如果图片中出现数字、引文字母、符号等均标记为未识别——
					“<img style="width:24px;height:24px;" src="../../image/recognition/no_chinese.png">”。
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="matter">
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-head">
									<div class="pull-left">当前图片组</div>
									<div class="widget-icons pull-right">
										<a href="#" class="wminimize">
											<i class="icon-chevron-up"></i>
										</a>
										<a href="#" class="wclose">
											<i class="icon-remove"></i>
										</a>
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="padd">
										<div class="gallery">
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t1.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t2.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t3.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t4.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t5.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t6.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t7.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t8.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t9.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
											<div class="font-unit">
												<div class="photo-frame">
													<img src="img/photos/t10.jpg" alt="">
												</div>
												<input type="text" class="form-control placeholder">
												<img class="no-chinese" src="../../image/recognition/no_chinese.png">
											</div>
										</div>
									</div>
									<div class="widget-foot"></div>
									<button type="button" class="btn btn-primary btn-lg btn-block">Block level button</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
</body>
</html>
