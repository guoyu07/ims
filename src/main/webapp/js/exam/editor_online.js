// 操作元素
// 编辑操作
function blodEr(){
	document.execCommand("bold",false,null);
}
function italicR(){
	document.execCommand("italic",false,null);
}
function underlineR(){
	document.execCommand("underline",false,null);
}
function insertUnorderedlist(){
	document.execCommand("insertunorderedlist",false,null);
}
function insertOrderedlist(){
	document.execCommand("insertorderedlist",false,null);
}

var Part;
// 获取操作板块
function getPart() {
	var e = event.currentTarget;
	if(e.id.indexOf("body") > 0) {
		return 0;
	} else if(e.id.indexOf("answer") > 0) {
		return 1;
	} else if(e.id.indexOf("analysis") > 0) {
		return 2;
	}
}

function insertImage() {
	Part = getPart();
	if(Part === 0) {
		document.getElementById('EditWindow_body').focus();
	} else if(Part === 1) {
		document.getElementById('EditWindow_answer').focus();
	} else if(Part === 2) {
		document.getElementById('EditWindow_analysis').focus();
	}
	document.execCommand("insertimage",false,basePath + "image/exam/img.png");
	var ImgInput = document.getElementById("ImgInput");
	ImgInput.setAttribute("style","display:block;");
	BodyMask.setAttribute("style","display:block;");
}
var cursor;
function insertFomular(){
	Part = getPart();
	if(Part === 0) {
		document.getElementById('EditWindow_body').focus();
	} else if(Part === 1) {
		document.getElementById('EditWindow_answer').focus();
	} else if(Part === 2) {
		document.getElementById('EditWindow_analysis').focus();
	}
	document.execCommand("insertimage",false,basePath + "image/exam/fomular.png");
	var MathFInput = document.getElementById("MathFInput");
	MathFInput.setAttribute("style","display:block;");
	BodyMask.setAttribute("style","display:block;");
}
function delStyle(){

}

var BodyMask = document.getElementsByClassName("body-mask")[0];
var selImgLocal = document.getElementById("selImgLocal");
var selImgNet = document.getElementById("selImgNet");
var inputImgLocal = document.getElementById("inputImgLocal");
var inputImgNet = document.getElementById("inputImgNet");
var isImgLocal = true;
var isImgNet = false;
selImgNet.onclick = function() {
	inputImgLocal.setAttribute("style","display:none;");
	inputImgNet.setAttribute("style","display:block;");
	isImgLocal = false;
	isImgNet = true;
};
selImgLocal.onclick = function() {
	inputImgNet.setAttribute("style","display:none;");
	inputImgLocal.setAttribute("style","display:block;");
	isImgLocal = true;
	isImgNet = false;
};
var closeImgInput = document.getElementById("closeImgInput");
closeImgInput.onclick = function() {
	var ImgInput = document.getElementById("ImgInput");
	ImgInput.setAttribute("style","display:none;");
	BodyMask.setAttribute("style","display:none;");
	var EditWindow;
	if(Part === 0) {
		EditWindow = EditWindow_body;
	} else if(Part === 1) {
		EditWindow = EditWindow_answer;
	} else if(Part === 2) {
		EditWindow = EditWindow_analysis;
	}
	var imgs = EditWindow.getElementsByTagName("img");
	var inmg;
	for(var i=0;i<imgs.length;i++) {
		if(imgs[i].src.indexOf("img.png") > 0) {
			inmg = imgs[i];
			break;
		}
	}
	EditWindow.removeChild(inmg);
	return;
};
var SelectImg = document.getElementById("SelectImg");
var ImgPathLoc = document.getElementById("ImgPathLoc");
var ImgLocPos1 = document.getElementById("ImgLocPos1");
var ImgLocPos2 = document.getElementById("ImgLocPos2");
var ImgLocPos3 = document.getElementById("ImgLocPos3");
var ImgLocDes = document.getElementById("ImgLocDes");

var ImgPathNet = document.getElementById("ImgPathNet");
var ImgNetPos1 = document.getElementById("ImgNetPos1");
var ImgNetPos2 = document.getElementById("ImgNetPos2");
var ImgNetPos3 = document.getElementById("ImgNetPos3");
var ImgNetDes = document.getElementById("ImgNetDes");

function getImg() {
	var xhr = new XMLHttpRequest();
	var xhr_url;
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304 )) {
                alert("提交异常，请稍后重试！");
            } else if(xhr.status === -1) {
            	var obj = eval("("+xhr.responseText+")");
            	alert(obj.msg);
            } else {
            	var obj = eval("("+xhr.responseText+")");
        		var EditWindow;
				if(Part === 0) {
					EditWindow = EditWindow_body;
				} else if(Part === 1) {
					EditWindow = EditWindow_answer;
				} else if(Part === 2) {
					EditWindow = EditWindow_analysis;
				}
				var imgs = EditWindow.getElementsByTagName("img");
				var inmg;
				for(var i=0;i<imgs.length;i++) {
					if(imgs[i].src.indexOf("img.png") > 0) {
						inmg = imgs[i];
						break;
					}
				}
            	if(obj.status === 0) {
					inmg.setAttribute("class","inmg");
					inmg.setAttribute("src",obj.result.imageUrl);
					ImgInput.setAttribute("style","display:none;");
					BodyMask.setAttribute("style","display:none;");
					document.getElementById("ImgPathLoc").setAttribute("value","");
					document.getElementById("ImgPathNet").setAttribute("value","");            		
            	} else {
            		alert(obj.msg);
					EditWindow.removeChild(inmg);
					ImgInput.setAttribute("style","display:none;");
					BodyMask.setAttribute("style","display:none;");
					document.getElementById("ImgPathLoc").setAttribute("value","");
					document.getElementById("ImgPathNet").setAttribute("value","");
            	}
            }
        }
    };

    if(typeof XMLHttpRequest.prototype.sendAsBinary == 'undefined') {
	    XMLHttpRequest.prototype.sendAsBinary = function(text){
	        var data = new ArrayBuffer(text.length);
	        var ui8a = new Uint8Array(data, 0);
	        for (var i = 0; i < text.length; i++) ui8a[i] = (text.charCodeAt(i) & 0xff);
	        this.send(ui8a);
	    };
	}

    if(isImgLocal === true) {
    	if(document.getElementById('SelectImg').files.length>0) {
			var file = document.getElementById('SelectImg').files[0];
			document.getElementById("ImgPathLoc").setAttribute("value",file.name);
			var reader = new FileReader();
			reader.onloadend = function() {
	            if (reader.error) {
	                alert(reader.error);
	            } else {
	            	xhr.open("POST",basePath + "fileupload/uploadLocalImage?localImageFileName=" + file.name);
	                xhr.setRequestHeader('Content-Type','multipart/form-data;');
	                xhr.overrideMimeType("application/octet-stream");
	                xhr.sendAsBinary(reader.result);
	                var localImageFileName = file.name;
	                xhr.send("localImageFileName="+localImageFileName);
	            }
	        };
	        reader.readAsBinaryString(file);    		
    	} else {
    		return;
    	}
	} else if(isImgNet === true) {
		xhr.open("post",basePath + "fileupload/uploadRemoteImage",false);
    	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.send("remoteUrl="+ImgPathNet.value);
	}
}

var submitImg = document.getElementById("submitImg");
submitImg.onclick = function() {
	getImg();
};

// 取消公式编辑
function cancelFomular() {
	var MathFInput = document.getElementById("MathFInput");
	MathFInput.setAttribute("style","display:none;");
	BodyMask.setAttribute("style","display:none;");
	var EditWindow;
	if(Part === 0) {
		EditWindow = EditWindow_body;
	} else if(Part === 1) {
		EditWindow = EditWindow_answer;
	} else if(Part === 2) {
		EditWindow = EditWindow_analysis;
	}
	var f_imgs = EditWindow.getElementsByTagName("img");
	var f;
	for(var i=0;i<f_imgs.length;i++) {
		if(f_imgs[i].src.indexOf("fomular.png") > 0) {
			f = f_imgs[i];
			break;
		}
	}
	EditWindow.removeChild(f);
	return;
}
// 插入公式
function getFomular() {
	var matheq_latex = document.getElementById("matheq_latex");
	// 图片格式公式,最好将生成和图片的服务写在本地或者将图片批量下载到本地进行上传
	var matheq_preview = document.getElementById("matheq_preview");
	var FomularImgLink = matheq_preview.getElementsByTagName("img")[0];
	var src;
	var latex;
	if(typeof matheq_preview.getElementsByTagName("img")[0] !== "undefined") {
		src = matheq_preview.getElementsByTagName("img")[0].src;
		src = encodeURI(src);
		latex = matheq_latex.value;
	} else {
		src = "";
		latex = "";
	}
	var EditWindow;
	if(Part === 0) {
		EditWindow = EditWindow_body;
	} else if(Part === 1) {
		EditWindow = EditWindow_answer;
	} else if(Part === 2) {
		EditWindow = EditWindow_analysis;
	}
	var f_imgs = EditWindow.getElementsByTagName("img");
	var f;
	for(var i=0;i<f_imgs.length;i++) {
		if(f_imgs[i].src.indexOf("fomular.png") > 0) {
			f = f_imgs[i];
			break;
		}
	}
	var xhr = new XMLHttpRequest();
	var xhr_url;
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status ==304 )) {
                alert("提交异常，请稍后重试！");
            } else {
                var obj = eval("("+xhr.responseText+")");
                if(obj.status === 0) {
                	if(src === "") {
					EditWindow.removeChild(f);
					} else {
						f.setAttribute("class","fomular");
						f.setAttribute("src",obj.result.imageUrl);
						f.setAttribute("alt",latex);
					}
					MathFInput.setAttribute("style","display:none;");
					BodyMask.setAttribute("style","display:none;");
					matheq_latex.setAttribute("value","");
                } else {
                	MathFInput.setAttribute("style","display:none;");
					BodyMask.setAttribute("style","display:none;");
					matheq_latex.setAttribute("value","");
                	EditWindow.removeChild(f);
                	alert(obj.msg);
                }
            }
        }
    };
    xhr.open("post",basePath + "fileupload/uploadRemoteImage",false);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("remoteUrl="+src);
	// EditWindow.focus();
}
var submitFomular = document.getElementById("submitFomular");
submitFomular.onclick = function() {
	getFomular();
};
var closeFomularInput = document.getElementById("closeFomularInput");
closeFomularInput.onclick = function() {
	cancelFomular();
};

// 获取Latex
function getLatex() {
	var doc = document;
	var LaTex;
	var edit_content;
	if(Part === 0) {
		LaTex = doc.getElementById("latex_body");
		edit_content = doc.getElementById("EditWindow_body");
		if(edit_content.innerHTML.length === 0) {
			alert("问题描述不能为空！");
			return;
		}
	} else if(Part === 1) {
		LaTex = doc.getElementById("latex_answer");
		edit_content = doc.getElementById("EditWindow_answer");
		if(edit_content.innerHTML.toString().length === 0) {
			alert("题目解答不能为空！");
			return;
		}
	} else if(Part === 2) {
		// LaTex = latex_analysis;
		return;
	}
	if(LaTex.innerHTML.length > 0) {
		var fomulars = LaTex.getElementsByClassName("fomular");
		var f;
		var latex;
		for(var i=0;i<fomulars.length;i++) {
			f = fomulars[i];
			latex = f.getAttribute("alt");
			var latex_ele = document.createElement("span");
			latex_ele.setAttribute("class","latex");
			latex_ele.innerHTML = latex;
			f.parentNode.insertBefore(latex_ele,f);
		}
		var text = LaTex.innerText;
		LaTex.innerHTML = text;
	} else {
		setTimeout("getLatex();",100);
	}
}

// 问题描述编辑区
var EditWindow_body = document.getElementById("EditWindow_body");
var blodEr_body = document.getElementById("blodEr_body");
var italicR_body = document.getElementById("italicR_body");
var underlineR_body = document.getElementById("underlineR_body");
var insertUnorderedlist_body = document.getElementById("insertUnorderedlist_body");
var insertOrderedlist_body = document.getElementById("insertOrderedlist_body");
var insertImage_body = document.getElementById("insertImage_body");
var insertFomular_body = document.getElementById("insertFomular_body");
var delStyle_body = document.getElementById("delStyle_body");

blodEr_body.onclick = function() {if(isSubmitBody == true){return;} else {blodEr();}};
italicR_body.onclick = function() {if(isSubmitBody == true){return;} else {italicR();}};
underlineR_body.onclick = function() {if(isSubmitBody == true){return;} else {underlineR();}};
insertUnorderedlist_body.onclick = function() {if(isSubmitBody == true){return;} else {insertUnorderedlist();}};
insertOrderedlist_body.onclick = function() {if(isSubmitBody == true){return;} else {insertOrderedlist();}};
insertImage_body.onclick = function() {if(isSubmitBody == true){return;} else {insertImage();}};
insertFomular_body.onclick = function() {if(isSubmitBody == true){return;} else {insertFomular();}};
delStyle_body.onclick = function() {};

// 问题解答编辑区
var EditWindow_answer = document.getElementById("EditWindow_answer");
var blodEr_answer = document.getElementById("blodEr_answer");
var italicR_answer = document.getElementById("italicR_answer");
var underlineR_answer = document.getElementById("underlineR_answer");
var insertUnorderedlist_answer = document.getElementById("insertUnorderedlist_answer");
var insertOrderedlist_answer = document.getElementById("insertOrderedlist_answer");
var insertImage_answer = document.getElementById("insertImage_answer");
var insertFomular_answer = document.getElementById("insertFomular_answer");
var delStyle_answer = document.getElementById("delStyle_answer");

blodEr_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {blodEr();}};
italicR_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {italicR();}};
underlineR_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {underlineR();}};
insertUnorderedlist_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {insertUnorderedlist();}};
insertOrderedlist_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {insertOrderedlist();}};
insertImage_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {insertImage();}};
insertFomular_answer.onclick = function() {if(isSubmitAnswer == true){return;} else {insertFomular();}};
delStyle_answer.onclick = function() {};

// 问题解析编辑区
var EditWindow_analysis = document.getElementById("EditWindow_analysis");
var blodEr_analysis = document.getElementById("blodEr_analysis");
var italicR_analysis = document.getElementById("italicR_analysis");
var underlineR_analysis = document.getElementById("underlineR_analysis");
var insertUnorderedlist_analysis = document.getElementById("insertUnorderedlist_analysis");
var insertOrderedlist_analysis = document.getElementById("insertOrderedlist_analysis");
var insertImage_analysis = document.getElementById("insertImage_analysis");
var insertFomular_analysis = document.getElementById("insertFomular_analysis");
var delStyle_analysis = document.getElementById("delStyle_analysis");

blodEr_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {blodEr();}}
italicR_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {italicR();}}
underlineR_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {underlineR();}}
insertUnorderedlist_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {insertUnorderedlist();}}
insertOrderedlist_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {insertOrderedlist();}}
insertImage_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {insertImage();}}
insertFomular_analysis.onclick = function() {if(isSubmitAnalysis == true){return;} else {insertFomular();}}
delStyle_analysis.onclick = function() {}

// 知识点编辑区
var EditWindow_tags = document.getElementById("EditWindow_tags");

// 提交各个板块的内容
var body = document.getElementById("body");
var answer = document.getElementById("answer");
var analysis = document.getElementById("analysis");
var tags = document.getElementById("tags");

var submitBodyBtn = document.getElementById("submitBodyBtn");
var submitAnswerBtn = document.getElementById("submitAnswerBtn");
var submitAnalysisBtn = document.getElementById("submitAnalysisBtn");
var submitTagsBtn = document.getElementById("submitTagsBtn");

var isSubmitBody = false;
var isSubmitAnswer = false;
var isSubmitAnalysis = false;
var isSubmitTags = false;
var input_answer = true;

function localizeImg(container, callback) {
	var internalReg, imgs, errorImg, remoteUrls = [];
	internalReg = /^https?:\/\/(?:(?:\d+\.){3}\d+:\d+|.*\.91xuexibao\.com)\/.*/;
	errorImg = basePath + 'image/default.png';
	imgs = $(container).find('img');
	imgs = [].filter.call(imgs, function(img) {
		var imgUrl = $(img).attr('src');
		if(!internalReg.test(imgUrl)) {
			remoteUrls.push(encodeURIComponent(imgUrl));
			return true;
		}
	});
	if(remoteUrls[0]) {
		$.ajax({
			url: basePath + 'fileupload/uploadRemoteImages',
			method: 'get',
			dataType: 'json',
			data: {
				remoteUrls: remoteUrls.join()
			},
			success: function(data) {
				var errorFlag = false;
				if(data.status == 0) {
					$.each(imgs, function(i, img) {
						var imgUrl = data.result[i];
						if(!imgUrl) {
							errorFlag = true;
							imgUrl = errorImg;
						}
						$(img).attr('src', imgUrl);
					});
					if(!errorFlag && callback) {
						callback();
					}
				} else {
					alert(data.msg);
				}
			}
		});
	} else {
		if(callback) {
			callback();
		}
	}
}

var body_image = document.getElementById("body_image");
function submitBody() {
	if(isSubmitBody === false) {
        localizeImg(EditWindow_body, function() {
            var content = EditWindow_body.innerHTML.toString();    
            EditWindow_body.setAttribute("style","display:none;");
            submitBodyBtn.innerHTML = "修改问题描述";
            submitBodyBtn.setAttribute("style","background-color:#ccc;");
            // 可以根据页面是否被更新完毕使用submitBody.setAttribute("style","cursor:pointer;")语句更换鼠标状态为wait或者其他
            // 在这种状态更改后，按钮的onclick事件被锁定
            isSubmitBody = true;
            body.innerHTML = content;
            document.getElementById("latex_body").innerHTML = EditWindow_body.innerHTML;
        });
	} else {
		EditWindow_body.setAttribute("style","display:block;");
		submitBodyBtn.innerHTML = "提交问题描述";
		submitBodyBtn.setAttribute("style","background-color:#74c5ed;");
		isSubmitBody = false;
	}
}
submitBodyBtn.onclick = function() {submitBody();Part = 0;getLatex();};

function submitAnswer() {
	if(isSubmitAnswer === false) {
        localizeImg(EditWindow_answer, function() {
            var content = EditWindow_answer.innerHTML.toString();
            EditWindow_answer.setAttribute("style","display:none;");
            submitAnswerBtn.innerHTML = "修改题目解答";
            submitAnswerBtn.setAttribute("style","background-color:#ccc;");
            // 可以根据页面是否被更新完毕使用submitAnswer.setAttribute("style","cursor:pointer;")语句更换鼠标状态为wait或者其他
            // 在这种状态更改后，按钮的onclick事件被锁定
            isSubmitAnswer = true;
            answer.innerHTML = content;
            document.getElementById("latex_answer").innerHTML = EditWindow_answer.innerHTML;
        });
	} else {
		EditWindow_answer.setAttribute("style","display:block;");
		submitAnswerBtn.innerHTML = "提交题目解答";
		submitAnswerBtn.setAttribute("style","background-color:#9ded74;");
		isSubmitAnswer = false;
	}
}
submitAnswerBtn.onclick = function() {submitAnswer();Part = 1;getLatex();};

function submitAnalysis() {
	if(isSubmitAnalysis === false) {
        localizeImg(EditWindow_analysis, function() {
            var content = EditWindow_analysis.innerHTML.toString();        
            EditWindow_analysis.setAttribute("style","display:none;");
            submitAnalysisBtn.innerHTML = "修改解题思路";
            submitAnalysisBtn.setAttribute("style","background-color:#ccc;");
            // 可以根据页面是否被更新完毕使用submitAnalysis.setAttribute("style","cursor:pointer;")语句更换鼠标状态为wait或者其他
            // 在这种状态更改后，按钮的onclick事件被锁定
            isSubmitAnalysis = true;
            analysis.innerHTML = EditWindow_analysis.innerHTML;
            // document.getElementById("latex_analysis").innerHTML = EditWindow_analysis.innerHTML;
        });
	} else {
		EditWindow_analysis.setAttribute("style","display:block;");
		submitAnalysisBtn.innerHTML = "提交解题思路";
		submitAnalysisBtn.setAttribute("style","background-color:#74c5ed;");
		isSubmitAnalysis = false;
	}
}
submitAnalysisBtn.onclick = function() {submitAnalysis();Part = 2;getLatex();};

function submitTags() {
	var tag_arr = [];
	var tag_str = "";
	for(var i=0;i<3;i++) {
		if(EditWindow_tags.getElementsByClassName("tag")[i].innerText !== "") {
			tag_arr.push(EditWindow_tags.getElementsByClassName("tag")[i].innerText);
		}
	}
	for(var j=0;j<tag_arr.length;j++) {
		tag_str = tag_str + tag_arr[j];
		if(j !== tag_arr.length-1) {
			tag_str = tag_str + ",";
		}
	}
	tags.innerHTML = tag_str;
}
submitTagsBtn.onclick = function() {submitTags();};

var noinput_answer = document.getElementById("noinput_answer");
noinput_answer.onclick = function() {
	if(input_answer === true) {
		// submitAnalysis
		answer.innerHTML = "";
		document.getElementById("latex_answer").innerHTML = "";
		EditWindow_answer.innerHTML = "";
		isSubmitAnswer = false;
		submitAnswerBtn.innerHTML = "提交题目解答";
		EditWindow_answer.setAttribute("style","display:none;");
		submitAnswerBtn.setAttribute("style","background-color:#ccc;");
		submitAnswerBtn.setAttribute("onclick","return;");

		analysis.innerHTML = "";
		EditWindow_analysis.innerHTML = "";
		isSubmitAnalysis = false;
		submitAnalysisBtn.innerHTML = "提交解题思路";
		EditWindow_analysis.setAttribute("style","display:none;");
		submitAnalysisBtn.setAttribute("style","background-color:#ccc;");
		submitAnalysisBtn.setAttribute("onclick","return;");

		tags.innerHTML = "";
		for(var i=0;i<3;i++) {
			EditWindow_tags.getElementsByClassName("tag")[i].innerHTML = "";
		}
		submitTagsBtn.setAttribute("onclick","return;");

		noinput_answer.innerHTML = "录入答案";
		noinput_answer.setAttribute("style","background-color:#ccc;");
		input_answer = false;
	} else {
		EditWindow_answer.setAttribute("style","display:block;");
		submitAnswerBtn.setAttribute("style","background-color:#9ded74;");
		submitAnswerBtn.setAttribute("onclick","submitAnswer();Part = 1;getLatex();");

		EditWindow_analysis.setAttribute("style","display:block;");
		submitAnalysisBtn.setAttribute("style","background-color:#74c5ed;");
		submitAnalysisBtn.setAttribute("onclick","submitAnalysis();Part = 2;getLatex();");

		submitTagsBtn.setAttribute("onclick","submitTags();");

		noinput_answer.innerHTML = "不录答案";
		noinput_answer.setAttribute("style","background-color:#1D80CF;");
		input_answer = true;
	}
}

if(document.getElementById("Sub"))
document.getElementById("Sub").onclick = function() {
	var doc = document;
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status ==304 )) {
                alert("提交异常，请稍后重试！");
            } else {
            	try {
	                var obj = eval("("+xhr.responseText+")");
	                if(obj.status === 0) {
	                	alert(obj.msg);
	                	location.href=basePath + "tranops/tranOpsEditSearch";
	                } else {
	                	alert(obj.msg);
	                }
            	} catch (e) {
            		location.href = basePath + 'login.jsp';
            	}
            }
        }
    };
    var result = {
    	questionId: $('#topic_id').val(),
    	subject: $('select[name=subject]').val(),
    	content:doc.getElementById("body").innerHTML.replace(/&nbsp/g, "\'\&nbsp\'"),
    	answer:doc.getElementById("answer").innerHTML,
    	solution:doc.getElementById("analysis").innerHTML,
    	knowledge:doc.getElementById("tags").innerHTML,
		latex:doc.getElementById("latex_body").innerHTML,
		answerLatex:doc.getElementById("latex_answer").innerHTML,
		complete:1,
    };

    if(input_answer === true) {
    	result.complete = 1;
    } else {
    	result.complete = 0;
    }
    // 输入判定
  	if(result.complete === 1) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		} else if(result.answer.length === 0) {
  			alert("请输入题目解答后再提交");
  			return;
  		} else if(result.solution.length === 0) {
  			alert("请输入解题思路后再提交");
  			return;
  		} else if(result.knowledge.length === 0) {
  			alert("请输入知识点后再提交");
  			return;
  		}
  	} else if(result.complete === 0) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		}
  	}
  	if(result.subject.length === 0) {
  		alert("请选择学科后再提交");
  		return;
  	}
	doc.getElementById("questionId_input").setAttribute("value",result.questionId);
	doc.getElementById("subject_input").setAttribute("value",result.subject);
	doc.getElementById("content_input").setAttribute("value",result.content);
	doc.getElementById("answer_input").setAttribute("value",result.answer);
	doc.getElementById("solution_input").setAttribute("value",result.solution);
	doc.getElementById("knowledge_input").setAttribute("value",result.knowledge);
	doc.getElementById("latex_input").setAttribute("value",result.latex);
	doc.getElementById("answerLatex_input").setAttribute("value",result.answerLatex);
	doc.getElementById("complete_input").setAttribute("value",result.complete);
	xhr.open("post",basePath + "tranops/updateTranOps",false);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send($("#results").serialize());
};
if(document.getElementById("adminSub"))
	document.getElementById("adminSub").onclick = function() {
	var doc = document;
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4) {
			if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status ==304 )) {
				alert("提交异常，请稍后重试！");
			} else {
				try {
					var obj = eval("("+xhr.responseText+")");
					if(obj.status === 0) {
						alert(obj.msg);
						location.href=basePath + "tranops/tranOpsAdminEditSearch";
					} else {
						alert(obj.msg);
					}					
				} catch (e) {
					location.href = basePath + 'login.jsp';
				}
			}
		}
	};
	var result = {
			questionId: $('#topic_id').val(),
			subject: $('select[name=subject]').val(),
			content:doc.getElementById("body").innerHTML.replace(/&nbsp/g, "\'\&nbsp\'"),
			answer:doc.getElementById("answer").innerHTML,
			solution:doc.getElementById("analysis").innerHTML,
			knowledge:doc.getElementById("tags").innerHTML,
			latex:doc.getElementById("latex_body").innerHTML,
			answerLatex:doc.getElementById("latex_answer").innerHTML,
			complete:1,
	};
	
	if(input_answer === true) {
		result.complete = 1;
	} else {
		result.complete = 0;
	}
	// 输入判定
	if(result.complete === 1) {
		if(result.content.length === 0) {
			alert("请输入问题描述后再提交");
			return;
		} else if(result.answer.length === 0) {
			alert("请输入题目解答后再提交");
			return;
		} else if(result.solution.length === 0) {
			alert("请输入解题思路后再提交");
			return;
		} else if(result.knowledge.length === 0) {
			alert("请输入知识点后再提交");
			return;
		}
	} else if(result.complete === 0) {
		if(result.content.length === 0) {
			alert("请输入问题描述后再提交");
			return;
		}
	}
	if(result.subject.length === 0) {
		alert("请选择学科后再提交");
		return;
	}
	doc.getElementById("questionId_input").setAttribute("value",result.questionId);
	doc.getElementById("subject_input").setAttribute("value",result.subject);
	doc.getElementById("content_input").setAttribute("value",result.content);
	doc.getElementById("answer_input").setAttribute("value",result.answer);
	doc.getElementById("solution_input").setAttribute("value",result.solution);
	doc.getElementById("knowledge_input").setAttribute("value",result.knowledge);
	doc.getElementById("latex_input").setAttribute("value",result.latex);
	doc.getElementById("answerLatex_input").setAttribute("value",result.answerLatex);
	doc.getElementById("complete_input").setAttribute("value",result.complete);
	xhr.open("post",basePath + "tranops/updateTranOps",false);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send($("#results").serialize());
};
if(document.getElementById("Creat_Sub"))
	document.getElementById("Creat_Sub").onclick = function() {
	var doc = document;
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status ==304 )) {
                alert("提交异常，请稍后重试！");
            } else {
            	try {
            		var obj = eval("("+xhr.responseText+")");
            		if(obj.status === 0) {
            			alert(obj.msg);
            			location.href=basePath + "tranops/tranOpsEditSearch";
            		} else {
            			alert(obj.msg);
            		}
            	} catch (e) {
            		location.href = basePath + 'login.jsp';
            	}
            }
        }
    };
    var result = {
    	subject: $('select[name=subject]').val(),
    	content:doc.getElementById("body").innerHTML,
    	answer:doc.getElementById("answer").innerHTML,
    	solution:doc.getElementById("analysis").innerHTML,
    	knowledge:doc.getElementById("tags").innerHTML,
		latex:doc.getElementById("latex_body").innerHTML,
		answerLatex:doc.getElementById("latex_answer").innerHTML,
		complete:1,
    }
	// doc.getElementById("questionId_input").setAttribute("value","");
	doc.getElementById("subject_input").setAttribute("value",result.subject);
	doc.getElementById("content_input").setAttribute("value",result.content);
	doc.getElementById("answer_input").setAttribute("value",result.answer);
	doc.getElementById("solution_input").setAttribute("value",result.solution);
	doc.getElementById("knowledge_input").setAttribute("value",result.knowledge);
	doc.getElementById("latex_input").setAttribute("value",result.latex);
	doc.getElementById("answerLatex_input").setAttribute("value",result.answerLatex);
	
	if(input_answer === true) {
    	result.complete = 1;
    } else {
    	result.complete = 0;
    }
    doc.getElementById("complete_input").setAttribute("value",result.complete);
  	if(result.complete === 1) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		} else if(result.answer.length === 0) {
  			alert("请输入题目解答后再提交");
  			return;
  		} else if(result.solution.length === 0) {
  			alert("请输入解题思路后再提交");
  			return;
  		} else if(result.knowledge.length === 0) {
  			alert("请输入知识点后再提交");
  			return;
  		}
  	} else if(result.complete === 0) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		}
  	}
  	if(result.subject.length === 0) {
  		alert("请选择学科后再提交");
  		return;
  	}
	xhr.open("post",basePath + "tranops/addTranOps",false);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send($("#results").serialize());
};

if(document.getElementById("PicRecSub"))
	document.getElementById("PicRecSub").onclick = function() {
	var doc = document;
	var xhr = new XMLHttpRequest();
	var pictureId = doc.getElementById("pic_id").innerText;
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status ==304 )) {
                alert("提交异常，请稍后重试！");
            } else {
            	try {
            		var obj = eval("("+xhr.responseText+")");
            		if(obj.status === 0) {
            			alert(obj.msg);
            			location.href=basePath + "picture/viewOrcPictureById?pictureId="+pictureId;
            		} else {
            			alert(obj.msg);
            		}
            	} catch (e) {
            		location.href = basePath + 'login.jsp';
            	}
            }
        }
    };
    var result = {
    	subject: $('select[name=subject]').val(),
    	content:doc.getElementById("body").innerHTML,
    	answer:doc.getElementById("answer").innerHTML,
    	solution:doc.getElementById("analysis").innerHTML,
    	knowledge:doc.getElementById("tags").innerHTML,
		latex:doc.getElementById("latex_body").innerHTML,
		answerLatex:doc.getElementById("latex_answer").innerHTML,
		complete:1,
		status:3,
		pictureId:doc.getElementById("pic_id").innerHTML,
		pictureUrl:doc.getElementById("photo").src,
    }
    // Long pictureId, Integer status, String pictureUrl
	// doc.getElementById("questionId_input").setAttribute("value",result.questionId);
	doc.getElementById("subject_input").setAttribute("value",result.subject);
	doc.getElementById("content_input").setAttribute("value",result.content);
	doc.getElementById("answer_input").setAttribute("value",result.answer);
	doc.getElementById("solution_input").setAttribute("value",result.solution);
	doc.getElementById("knowledge_input").setAttribute("value",result.knowledge);
	doc.getElementById("latex_input").setAttribute("value",result.latex);
	doc.getElementById("answerLatex_input").setAttribute("value",result.answerLatex);
	doc.getElementById("status_input").setAttribute("value",result.status);
	doc.getElementById("pictureId_input").setAttribute("value",result.pictureId);
	doc.getElementById("pictureUrl_input").setAttribute("value",result.pictureUrl);

	
	if(input_answer === true) {
    	result.complete = 1;
    } else {
    	result.complete = 0;
    }
    doc.getElementById("complete_input").setAttribute("value",result.complete);
  	if(result.complete === 1) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		} else if(result.answer.length === 0) {
  			alert("请输入题目解答后再提交");
  			return;
  		} else if(result.solution.length === 0) {
  			alert("请输入解题思路后再提交");
  			return;
  		} else if(result.knowledge.length === 0) {
  			alert("请输入知识点后再提交");
  			return;
  		}
  	} else if(result.complete === 0) {
  		if(result.content.length === 0) {
  			alert("请输入问题描述后再提交");
  			return;
  		}
  	}
  	if(result.subject.length === 0) {
  		alert("请选择学科后再提交");
  		return;
  	}
	xhr.open("post",basePath + "picture/addTranOpsOrc",false);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send($("#results").serialize());
};
function openPhoto() {
	var src = event.target.src;
	window.open(src,'newwindow','alwaysRaised=yes,z-look=yes');
}