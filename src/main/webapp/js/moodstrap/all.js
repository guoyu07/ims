
var ops = {
	domain:basePath,
	opt_list:[],
}

function initPage() {
	var doc = document;
	loadList();
	var nav = doc.getElementById('nav');
	var as = nav.getElementsByTagName('a');
	for(var i=0;i<as.length;i++) {
	     as[i].setAttribute("onclick","showOpt();");
	}
}

function showOpt_list() {
	var doc = document;
	// ops.opt_list = {"录题列表":{"试题列表":"http://127.0.0.1:8080/tiku_ops/tranops/tranOpsAuditSearch"},"题目审核":{"试题列表":"http://127.0.0.1:8080/tiku_ops/tranops/tranOpsAuditSearch"}};
	for(var p in ops.opt_list){
		var li_p = doc.createElement("li");
		if(typeof(ops.opt_list[p])==="object") {
			li_p.innerHTML = '<a href="#"><i class="fa fa-list-alt"></i><span></span><span class="pull-right"><i class="fa fa-chevron-left"></i></span></a><ul style="display: none;"></ul>';
			li_p.className = "has_sub";
			li_p.getElementsByTagName("span")[0].innerHTML = p;
			for(var q in ops.opt_list[p]){
				var li_q = doc.createElement("li");
				li_q.innerHTML = '<a></a>';
				li_q.getElementsByTagName("a")[0].innerHTML = q;
				li_q.getElementsByTagName("a")[0].setAttribute("title",ops.opt_list[p][q]);
				li_p.getElementsByTagName("ul")[0].appendChild(li_q);
			}
		} else {
			li_p.innerHTML = '<a><i class="fa fa-list-alt"></i><span></span></a>';
			li_p.getElementsByTagName("span")[0].innerHTML = p;
			li_p.getElementsByTagName("a")[0].setAttribute("title",ops.opt_list[p]);
		}
		doc.getElementById("nav").appendChild(li_p);
	}
}
function loadList() {
      var xhr = new XMLHttpRequest();
      xhr.onreadystatechange = function() {
        if(xhr.readyState === 4) {
            var obj = eval("("+xhr.responseText+")");
            if(obj.status === 0) {
                  ops.opt_list = obj.result;
            	showOpt_list();
            } else if(obj.status !== 0) {
                
            } else if(!((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304)) {
                
            }
        }
    }
    xhr.open("get",ops.domain+"user/getUrlsByGroupname",false);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("");
}
function showOpt() {
      var doc = document;
      var tar = event.currentTarget;
      if(tar.title.length > 0) {
            doc.getElementById("i-frame").setAttribute("src",tar.title);
      }
}