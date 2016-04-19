<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tlds/Functions" prefix="func"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>选择重复题目</title>
	<link href="<%=basePath %>/css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=basePath %>/css/merge.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		.mark-repeat {
			margin: 20px 20px 0;
			padding-bottom: 15px;
		}
		.mark-repeat .panel-heading span{
			font-size: 18px;
		}
		.mark-repeat .btn-primary {
			min-width: 100px;
		}
		.mark-repeat .panel-body {
			padding-top: 8px;
			margin-bottom: 15px;
		}
		.mark-repeat .heading {
			font-size: 16px;
			color: #0e7aff;
		}
		.mark-repeat table.contrast {
			margin-top: 15px;
			width: 100%
		}
		.mark-repeat .similar-container {
			padding: 0;
		}
		.mark-repeat .similar {
			margin-top: 13px;
			max-height: 520px;
			overflow-y: auto;
			overflow-x: hidden;
		}
		.mark-repeat ul.similar {
			margin-top: 0;
		}
		.mark-repeat ul.similar>li {
			padding: 8px 15px ;
			border-top: 1px solid #c51114;
		}
		.mark-repeat ul.similar>li:first-child {
			border-top: 0;
		}
		.mark-repeat .table-bordered {
			border: none;
		}
		.mark-repeat .contrast tr, .mark-repeat .similar tr {
			vertical-align: top;
		}
		.mark-repeat .contrast>tbody>tr>td.container, .mark-repeat tbody>tr>td.item {
			border: 1px solid #c51114;
		}
		.mark-repeat .content {
			word-break: break-all;
		}
		.mark-repeat .item {
			cursor: pointer;
		}
		.mark-repeat td.item {
			padding: 8px 15px 0;
		}
		.mark-repeat .selected {
			background-color: #3399ff;
		}
		.mark-repeat .selected * {
			background-color: transparent !important;
			border-color: #3399ff !important;
		}
		.mark-repeat .btn-container {
			margin-top: -5px;
			float: right;
			display: inline-block;
			padding-right: 60px;
		}
		.solution{width:100%; overflow:hidden; background-color:#fff; }
		.solution .solve > h1{
			font-size:16px;
			font-weight:bold;
			color:#0e7aff;
			margin-bottom:10px;
		}
		.solution .solve .detail{ font-size:14px; line-height:24px; }
		.solution .solve .detail img{ max-width:100%; }
		.solution .solve .detail table{ max-width:100%; }
		.solution .content * {
			width: auto !important;
		}
	</style>
</head>
<body>
	<section class="mark-repeat">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span>请在重复题目中选择一道答案、解析完整详细的题目<br><span class="text-danger">（请不要选择乱码、图片缺失的题目）</span></span>
				<div class="btn-container">
					<button id="finish" class="btn btn-primary" disabled style="margin-left:64px">完成</button>
				</div>
			</div>
			<div class="panel-body">
				<div class="similar">
					<table id="single-select" class="table-bordered solution" cellspacing="20"></table>
				</div>
			</div>
		</div>
	</section>
	<script src="<%=basePath %>/js/moodstrap/jquery.js"></script>
	<script src="<%=basePath %>/js/jquery/jquery.tmpl.min.js"></script>
	<script type="text/x-jquery-tmpl" id="select-best-tmpl">
		{{each(i, item) list}}
			{{if i%3==0}}
				<tr class="row">
			{{/if}}
			<td class="col-md-4 item" data-id="{{= item.questionId}}">
				<div class="content solve">
					<h1>题目</h1>
					<div class="detail">
						{{html item.content}}
					</div>
					<h1>答案</h1>
					<div class="detail">
						{{html item.answer}}
					</div>
					<h1>解析</h1>
					<div class="detail">
						{{html item.solution}}
					</div>
				</div>
			</td>
			{{if i%3==2}}
				</tr>
			{{/if}}
		{{/each}}
	</script>
	<script type="text/javascript">
		//window.similarList = {"candidate_list":[{"answer":"<div>【答案】A</div>","content":"<div>【题文】<br />在组成植物体的化学元素中，占鲜重最多的是（&nbsp;&nbsp;&nbsp;&nbsp;）<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"25%\">A．氧元素 </td><td width=\"25%\">B．碳元素 </td><td width=\"25%\">C．氢元素 </td><td width=\"25%\">D．氮元素</td></tr></table></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"","qualityOrder":17,"questionId":2928767,"SimOrder":17,"solution":"<div>【解析】略</div>"}, {"answer":"<div>【答案】A</div>","content":"<div>在组成植物体的化学元素中，质量分数最多的是（&nbsp;&nbsp;&nbsp;）<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"25%\">A．O元素</td><td width=\"25%\">B．C元素</td><td width=\"25%\">C．H元素</td><td width=\"25%\">D．N元素</td></tr></table></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"组成细胞的分子 第2章组成细胞的分子","qualityOrder":13,"questionId":36023651,"SimOrder":16,"solution":"<div>【解析】植物体的组成元素中，有65%为氧元素，18%为碳元素，10%为氢元素，3%为氮元素。</div>"}, {"answer":"<div class=\"doc_answer\"><div>  <p class=\"MsoNormal\">A</p>  </div></div>","content":"<div class=\"doc_question\"><div>  <p class=\"MsoNormal\">在组成植物体的化学元素中，质量分数最多的是</p>  <table class=\"MsoNormalTable\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100.0%;border:solid white 1.0pt\" name=\"optionsTable\">   <tbody><tr>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">A．O元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">B．C元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">C．H元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">D．N元素</p>    </td>   </tr>  </tbody></table>  </div></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"<div class=\"doc_theme\">高中生物,细胞的分子组成与结构,组成细胞的分子</div>","qualityOrder":12,"questionId":34161362,"SimOrder":15,"solution":"<div class=\"doc_analysis\"><div>  <p class=\"MsoNormal\">植物体的组成元素中，有65%为氧元素，18%为碳元素，10%为氢元素，3%为氮元素。  </p>  </div></div>"}, {"answer":"<p><i>A</i></p>","content":"<dt><p><br />在组成植物体的化学元素中，占鲜重最多的是（&nbsp;&nbsp;&nbsp;&nbsp;）</p><table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td width=\"25%\">A．氧元素 </td><td width=\"25%\">B．碳元素 </td><td width=\"25%\">C．氢元素 </td><td width=\"25%\">D．氮元素</td></tr></tbody></table><p></p></dt>","CreateTime":1430796865000,"groupId":1,"Knowledge":"NULL","qualityOrder":11,"questionId":32408406,"SimOrder":14,"solution":"<p><span class=\"parsing\">解析</span><i></i></p>"}, {"answer":"<div class=\"doc_answer\"><p>A</p></div>","content":"<div class=\"doc_question\"><div>  <p>在组成植物体的化学元素中，占鲜重最多的是（ ）</p>  <p>A．氧元素 B．碳元素 C．氢元素 D．氮元素</p>  </div></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"<div class=\"doc_theme\"></div>","qualityOrder":10,"questionId":34334114,"SimOrder":13,"solution":"<div class=\"doc_analysis\"><p class=\"MsoNormal\">略</p></div>"},{"answer":"<div>【答案】A</div>","content":"<div>【题文】在组成人体的化学元素中，占鲜重最多的是（&nbsp;&nbsp;&nbsp;&nbsp;）<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"25%\">A．氧元素</td><td width=\"25%\">B．碳元素</td><td width=\"25%\">C．氢元素</td><td width=\"25%\">D．氮元素]</td></tr></table></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"","qualityOrder":16,"questionId":2928734,"SimOrder":12,"solution":"<div>【解析】略</div>"},{"answer":"<div>【答案】A</div>","content":"<div>【题文】在组成植物体的化学元素中，质量分数最多的是<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"25%\">A．氧元素</td><td width=\"25%\">B．碳元素</td><td width=\"25%\">C．氢元素</td><td width=\"25%\">D．氮元素</td></tr></table></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"","qualityOrder":15,"questionId":3090812,"SimOrder":11,"solution":"<div>【解析】略</div>"},{"answer":"<p><i>A</i></p>","content":"<dt><p>在组成人体的化学元素中，占鲜重最多的是<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p><table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td width=\"25%\">A．氧元素</td><td width=\"25%\">B．碳元素</td><td width=\"25%\">C．氢元素</td><td width=\"25%\">D．氮元素</td></tr></tbody></table><p></p></dt>","CreateTime":1430796865000,"groupId":1,"Knowledge":"NULL","qualityOrder":9,"questionId":32389843,"SimOrder":10,"solution":"<p><span class=\"parsing\">解析</span><i></i></p>"},{"answer":"<div class=\"doc_answer\"><p>A</p></div>","content":"<div class=\"doc_question\"><div>  <p>在组成人体的化学元素中，占鲜重最多的是（ ）</p>  <p>A．氧元素 B．碳元素 C．氢元素 D．氮元素]</p>  </div></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"<div class=\"doc_theme\"></div>","qualityOrder":8,"questionId":34322778,"SimOrder":9,"solution":"<div class=\"doc_analysis\"><p class=\"MsoNormal\">略</p></div>"},{"answer":"<div class=\"pt6\"><!--B6--><em></em>解：人体活细胞故为鲜重，则占细胞鲜重比例最大的元素是氧．<br/>故选：A．<!--E6--></div>","content":"<div class=\"pt1\"><!--B1--><span class=\"qseq\"></span>在组成人体的化学元素中，占鲜重最多的是（　　）<!--E1--></div><div class=\"pt2\"><!--B2--><table class=\"ques quesborder\" style=\"width:100%\"><tr><td class=\"selectoption\" style=\"width:23%\"><label class=\" s\">A．氧元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">B．碳元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">C．氢元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">D．氮元素</label></td></tr></table><!--E2--></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"碳原子的结构特点","qualityOrder":1,"questionId":34464630,"SimOrder":8,"solution":"<div class=\"pt5\"><!--B5--><em></em>1、C、H、O、N、P、S这六种元素的含量占到了细胞总量的97%，称为主要元素．<b></b><br/>2、C、H、O、N基本元素占鲜重的比例从大到小的顺序是：O＞C＞H＞N；<br/>3、C、H、O、N基本元素占干重的比例从大到小的顺序是：C＞O＞N＞H．<b></b><!--E5--></div>"},{"answer":"<div>【答案】A</div>","content":"<div>在组成人体的化学元素中，占鲜重最多的是<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"25%\">A．氧元素</td><td width=\"25%\">B．碳元素</td><td width=\"25%\">C．氢元素</td><td width=\"25%\">D．氮元素</td></tr></table></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"组成细胞的分子 第2章组成细胞的分子","qualityOrder":7,"questionId":36142530,"SimOrder":7,"solution":"<div>【解析】本题考查的是组成人体的化学元素的有关内容。在组成人体的化学元素中，占鲜重最多的是氧元素。A正确。故本题选A。</div>"},{"answer":"<div class=\"doc_answer\"><p>A</p></div>","content":"<div class=\"doc_question\"><div>  <p>在组成人体的化学元素中，占鲜重最多的是<u> </u></p>  <p>　　A．氧元素 B．碳元素 C．氢元素 D．氮元素</p>  </div></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"<div class=\"doc_theme\">考点：细胞的分子组成</div>","qualityOrder":6,"questionId":34336545,"SimOrder":6,"solution":"<div class=\"doc_analysis\"><p class=\"MsoNormal\">略</p></div>"},{"answer":"<div class=\"doc_answer\"><div>  <p class=\"MsoNormal\">A</p>  </div></div>","content":"<div class=\"doc_question\"><div>  <p class=\"MsoNormal\">在组成人体的化学元素中，占鲜重最多的是<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>  <table class=\"MsoNormalTable\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100.0%;border:solid white 1.0pt\" name=\"optionsTable\">   <tbody><tr>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">A．氧元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">B．碳元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">C．氢元素</p>    </td>    <td width=\"25%\" style=\"width:25.0%;border:solid white 1.0pt;padding:0cm 0cm 0cm 0cm\">    <p class=\"MsoNormal\">D．氮元素</p>    </td>   </tr>  </tbody></table>  </div></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"<div class=\"doc_theme\">高中生物,细胞的分子组成与结构,组成细胞的分子</div>","qualityOrder":5,"questionId":34069621,"SimOrder":5,"solution":"<div class=\"doc_analysis\"><div>  <p class=\"MsoNormal\">本题考查的是组成人体的化学元素的有关内容。在组成人体的化学元素中，占鲜重最多的是氧元素。A正确。故本题选A。 </p>  </div></div>"},{"answer":"解析：在组成生物体的大量元素中，C、H、O、N、S、P六种元素是组成细胞的主要元素，大约占细胞总量的97%；由组成细胞的主要元素示意图可知，氧元素占65%，碳元素占18%，氢元素占10%，氮元素占3%，磷元素占1.4%,硫元素占0.3%。所以组成生物体的化学元素中，质量分数最多的是氧元素。</P><P>答案：A","content":"在组成植物体的化学元素中，质量分数最多的是（　　）</P><P>A.氧元素</P><P>B.碳元素 </P><P>C.氢元素</P><P>D.氮元素</P><P>","CreateTime":1430796865000,"groupId":1,"Knowledge":"组成生物体的化学元素","qualityOrder":4,"questionId":35632900,"SimOrder":4,"solution":"NULL"},{"answer":"<div class=\"pt6\"><!--B6--><em></em>解：人体活细胞故为鲜重，则占细胞鲜重比例最大的元素是氧．<br/>故选：A．<!--E6--></div>","content":"<div class=\"pt1\"><!--B1--><span class=\"qseq\"></span>在组成人体活细胞的化学元素中，质量最多的是（　　）<!--E1--></div><div class=\"pt2\"><!--B2--><table class=\"ques quesborder\" style=\"width:100%\"><tr><td class=\"selectoption\" style=\"width:23%\"><label class=\" s\">A．氧元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">B．碳元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">C．氢元素</label></td><td class=\"selectoption\" style=\"width:23%\"><label class=\"\">D．氮元素</label></td></tr></table><!--E2--></div>","CreateTime":1430796865000,"groupId":1,"Knowledge":"碳原子的结构特点","qualityOrder":0,"questionId":34419968,"SimOrder":3,"solution":"<div class=\"pt5\"><!--B5--><em></em>1、C、H、O、N、P、S这六种元素的含量占到了细胞总量的97%，称为主要元素．<b></b><br/>2、C、H、O、N基本元素占鲜重的比例从大到小的顺序是：O＞C＞H＞N；<br/>3、C、H、O、N基本元素占干重的比例从大到小的顺序是：C＞O＞N＞H．<b></b><!--E5--></div>"}], "msg":"ok","status":"0"}.candidate_list || [];
		var basePath = '<%=basePath%>';
		var local_result = ${ result };
	    if(local_result.status ==0 ){
	    	window.similarList = local_result.candidate_list;
	    }else{
	   	 	window.similarList = [];
	   	 	alert(local_result.msg);
	    }
	</script>
	<script src="<%=basePath %>js/selectBest.js"></script>
</body>
</html>
