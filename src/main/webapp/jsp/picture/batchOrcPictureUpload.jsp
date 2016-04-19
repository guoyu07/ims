<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file = "../inc/header.jsp" %>
    <!-- Imported scripts on this page -->
    <script type="text/javascript" src="<%=basePath %>assets/js/rwd-table/js/rwd-table.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>assets/js/dropzone/dropzone.js"></script>
    <script>
        var displayModeVal = 0;
    </script>
    <script type="text/javascript" src="<%=basePath %>assets/js/upload-img.js"></script>
</head>
<body class="page-body">
<div class="page-container"><!-- add class "sidebar-collapsed" to close sidebar by default, "chat-visible" to make chat appear always -->
    <!-- Add "fixed" class to make the sidebar fixed always to the browser viewport. -->
    <!-- Adding class "toggle-others" will keep only one menu item open at a time. -->
    <!-- Adding class "collapsed" collapse sidebar root elements and show only icons. -->
   <%--  <div class="sidebar-menu toggle-others fixed">
        <div class="sidebar-menu-inner">
            <header class="logo-env">
                <!-- logo -->
                <div class="logo">
                    <span class="logo-expanded"><img src="<%=basePath %>assets/images/logo.png"><span>学习宝识题自动化测试平台</span></span>
                    <span class="logo-collapsed"><img src="<%=basePath %>assets/images/logo.png"></span>
                </div>
                <!-- This will toggle the mobile menu and will be visible only on mobile devices -->
                <div class="mobile-menu-toggle visible-xs">
                    <a href="#" data-toggle="mobile-menu">
                        <i class="fa-bars"></i>
                    </a>
                </div>
            </header>
            <ul id="main-menu" class="main-menu">
                <!-- add class "multiple-expanded" to allow multiple submenus to open -->
                <!-- class "auto-inherit-active-class" will automatically add "active" class for parent elements who are marked already with class "active" -->
                <li class="active">
                    <a href="<%=basePath %>">
                        <i class="fa-upload"></i>
                        <span class="title">图片上传</span>
                    </a>
                </li>
                <li>
                    <a href="<%=basePath %>picture/orcPictureViewSearch">
                        <i class="fa-file-image-o"></i>
                        <span class="title">识别结果</span>
                    </a>
                </li>
                <li>
                    <a href="<%=basePath %>picture/orcPictureViewResult">
                        <i class="fa-file-image-o"></i>
                        <span class="title">识别率统计</span>
                    </a>
                </li>                
            </ul>
        </div>
    </div> --%>
    <div class="main-content">
		<%-- <%@ include file = "../inc/nav.jsp" %> --%>
        <%-- <div class="page-title">
            <div class="title-env">
                <h1 class="title">图片上传</h1>
            </div>
            <div class="breadcrumb-env">
                <ol class="breadcrumb bc-1">
                    <li>
                        <a href="<%=basePath %>index.jsp"><i class="fa-home"></i>首页</a>
                    </li>
                    <li>
                        <a>图片上传</a>
                    </li>
                </ol>
            </div>
        </div> --%>
        <!-- Responsive Table -->
        <div class="row select-app">
            <div class="col-md-12">
                <div class="panel">
                    <div class="panel-heading">
                        <h3 class="panel-title">测试应用选择</h3>
                        <div class="panel-options">
                            <a href="#" data-toggle="panel">
                                <span class="collapse-icon">&ndash;</span>
                                <span class="expand-icon">+</span>
                            </a>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="col-md-3">

                            <div class="form-block">
                                <label>
                                    <div class="s-logo"><img src="<%=basePath %>assets/images/logo.png"></div>
                                    <input type="radio" name="radio-3"class="cbr cbr-secondary" value="0" checked>学习宝
                                </label>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-block">
                                <label>
                                    <div class="s-logo"><img src="<%=basePath %>assets/images/logo_xuebajun.png"></div>
                                    <input type="radio" name="radio-3"class="cbr cbr-secondary" value="1" >学霸君
                                </label>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-block">
                                <label>
                                    <div class="s-logo"><img src="<%=basePath %>assets/images/logo_xiaoyuansouti.png"></div>
                                    <input type="radio" name="radio-3"class="cbr cbr-secondary" value="2" >小猿搜题
                                </label>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-block">
                                <label>
                                    <div class="s-logo"><img src="<%=basePath %>assets/images/logo_zuoyebang_disabled.png"></div>
                                    <input type="radio" name="radio-3"class="cbr cbr-secondary" value="3" disabled>作业帮
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">图片上传</h3>
                        <div class="panel-options">
                            <a href="#" data-toggle="panel">
                                <span class="collapse-icon">&ndash;</span>
                                <span class="expand-icon">+</span>
                            </a>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="upload-area">
                            <div class="row">
                                <div class="col-sm-3 text-center">

                                    <div id="advancedDropzone" class="droppable-area" title="">
                                        点击选择文件<br>
                                        或<br>
                                        拖放文件至此处
                                    </div>
                                    <div class="submit-btn">
                                        <button class="btn btn-secondary btn-icon btn-icon-standalone noupload" id="batchResultBtn"><i class="fa-check"></i><span>开始识别</span></button>
                                    </div>
                                </div>
                                <div class="col-sm-9">
                                    <div class="table-wrapper">
                                        <table class="table table-bordered table-striped" id="dropzone-filetable">
                                            <thead>
                                            <tr>
                                                <th width="8%" class="text-center">#</th>
                                                <th width="40%">文件名</th>
                                                <th width="20%">进度</th>
                                                <th>大小</th>
                                                <th>状态</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td colspan="5">当前没有上传任务</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%-- <%@ include file = "../inc/footer.jsp" %> --%>
    </div>
</div>

<!-- Modal 1 (Basic)-->
<div class="modal fade" id="modal-1">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">消息</h4>
            </div>

            <div class="modal-body">
                程序猿GG正在攻关中，敬请期待！
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-dismiss="modal">确定</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
	window.basePath = '<%=basePath%>';
</script>
<script type="text/javascript" src="<%=basePath %>js/global/getBatchResult.js"></script>
</body>
</html>