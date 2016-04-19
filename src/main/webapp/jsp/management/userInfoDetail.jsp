<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<title>用户详情</title>
<meta http-equiv="keywords" content="">
<meta http-equiv="description" content="">
<link type="image/x-icon" rel="shortcut icon" href="<%=basePath %>image/exam/favicon.ico">
<link href="<%=basePath %>css/bootstrap/static2.0.css" rel="stylesheet" type="text/css"/>
  <link href="<%=basePath %>css/moodstrap/bootstrap.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=basePath %>css/moodstrap/font-awesome.css"> 
  <link href="<%=basePath %>css/moodstrap/frame.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=basePath %>css/moodstrap/widgets.css">
<!-- <link href="<%=basePath %>css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/> -->
<link href="<%=basePath %>css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=basePath %>js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap/bootstrap.min.js"/></script>
<style type="text/css">
	/*.user-pic{width:100px;height:100px;}*/
	.section-left{float:left;width:28%;}
	.section-right{float:left;width:60%;}
</style>
</head>
<body>

<div class="content">

  	  	<!-- Main bar -->
  	<div class="mainbar">
      
	    <!-- Page heading -->
	    <div class="page-head">
	      <h2 class="pull-left">用户详情</h2>
        <div class="clearfix"></div>
        <!-- Breadcrumb -->
        <div class="bread-crumb">
                        用户详情
        </div>
        
        <div class="clearfix"></div>

	    </div>

	    <div class="matter">
        <div class="container">
          <div class="row">
            <div class="col-md-12">

			  <div class="widget">
                <div class="widget-head">
                  <div class="pull-left"><s:property value="teacher.name" /></div>
                  <div class="widget-icons pull-right">
                    <a href="#" class="wminimize"><i class="icon-chevron-up"></i></a> 
                    <a href="#" class="wclose"><i class="icon-remove"></i></a>
                  </div>  
                  <div class="clearfix"></div>
                </div>
                <div class="widget-content">
                  <div class="padd">

                    <div class="user">
                      <div class="user-pic">
                        <!-- User pic -->
                        <a href="#"><img src="<s:property value="teacher.idCardImageUrl" />"></a>
                      </div>

                      <div class="user-details">
                        <h5 id="teacher_name" title=""><s:property value="teacher.name" /></h5>
                        <p><span id="province"><s:property value="teacher.city.province.name" /></span>&nbsp<span id="city"><s:property value="teacher.city.name" /></span></p>
                        <p><span>身份证号：</span><span id="id_card_num"><s:property value="teacher.idNumber" /></span></p>
                        <p><span>注册时间：</span><span id="creat_time"><s:property value="teacher.createTime" /></span></p>
                      </div>
                      <div class="clearfix"></div>
                    </div>

                    <hr>

                    <div class="">
                      <div class="user-details">
                        <h5>联系方式</h5>
                        <p><span>电话：</span><span id="tel"><s:property value="teacher.phone" /></span></p>
                        <p><span>微信：</span><span id="weichat"><s:property value="teacher.weixin" /></span></p>
                        <p><span>QQ：</span><span id="qq"><s:property value="teacher.qq" /></span></p>
                      </div>
                      <div class="clearfix"></div>
                    </div>

                    <hr>

                    <div class="">
                      <div class="user-details">
                        <h5>学费信息</h5>
                        <div class="section-left">
                        	<s:if test="vipTeacherPay == null">
	                        	<p><span>累计：￥</span><span id="count">0.00</span></p>
		                        <p><span>已发：￥</span><span id="pay">0.00</span></p>
		                        <p><span>待发：￥</span><span id="nopay">0.00</span></p>
                        	</s:if>
                        	<s:if test="vipTeacherPay != null">
	                        	<p><span>累计：￥</span><span id="count"><s:property value="vipTeacherPay.totalMoney" /></span></p>
		                        <p><span>已发：￥</span><span id="pay"><s:property value="vipTeacherPay.paidMoney" /></span></p>
		                        <p><span>待发：￥</span><span id="nopay"><s:property value="vipTeacherPay.waitingMone" /></span></p>
                        	</s:if>
                        </div>
                        <div class="section-right">
                        	<p><span>支付宝账号：</span><span id="zhifubao"><s:property value="teacher.alipay" /></span></p>
	                        <p><span>银行卡号：</span><span id="bank_num"><s:property value="teacher.bankCard" /></span></p>
	                        <p><span>开户行：</span><span id="creat_bank"><s:property value="teacher.bank" /></span></p>
                        </div>
                        
                      </div>
                      <div class="clearfix"></div>
                    </div>

                    <hr>

                    <div class="">
                      <div class="user-details">
                        <h5>辅导信息</h5>
                        <div class="section-left">
                        	<p><span>授课学科：</span><span id="subject"><s:property value="teacher.subjects" /></span></p>
	                        <p><span>可授年级：</span><span id="grade"><s:property value="teacher.grades" /></span></p>
	                        <p><span>上课时间：</span><span id="time"><s:property value="teacher.courseTime" /></span></p>
	                        <p><span>授课地区：</span><span id="area"><s:property value="teacher.courseArea" /></span></p>
                        </div>
                        <div class="section-right">
                        	<p><span>教龄：</span><span id="teach_year"><s:property value="teacher.courseYear" /></span>年</p>
	                        <p><span>教师简介：</span><span id="teach_intro"><s:property value="teacher.selfDescription" /></span></p>
                        </div>
                        
                      </div>
                      <div class="clearfix"></div>
                    </div>

                    <hr>

                    <div>
                      <div class="user-details">
                      	<s:if test="teacher.status==1">
                      		<!-- <button type="submit" class="btn btn-danger">禁用老师资格</button> -->
                      		<div class="btn btn-danger" onclick="location.href='/report3/vip/getDeleTeacher.do?teacherId=<s:property value="teacher.id" />'">禁用老师资格</div>
						</s:if>
						<s:if test="teacher.status==0">
							<!-- <button type="submit" class="btn btn-danger">启用老师资格</button> -->
							<div class="btn btn-danger" onclick="location.href='/report3/vip/getNdeleTeacher.do?teacherId=<s:property value="teacher.id" />'">启用老师资格</div>
						</s:if>
						<!-- <button type="submit" class="btn btn-primary">操作记录</button> -->
						<!-- <button type="reset" class="btn btn-default">返回</button> -->
						<div class="btn btn-default" onclick="location.href='/report3/vip/vipTeacherSearch.do'">返回</div>
                      </div>
                      <div class="clearfix"></div>
                    </div>                                       

                    
                  </div>
                </div>
              </div>

              
            </div>
          </div>
        </div>
		  </div>

    </div>    
   <div class="clearfix"></div>

<script type="text/javascript">
</script>
</body>
</html>
