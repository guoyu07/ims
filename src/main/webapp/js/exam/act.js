
window.onload = function() {
	var win_width = document.documentElement.clientWidth - 384;
	document.getElementById("EditArea").setAttribute("style","width:"+win_width+"px;");
}
window.onresize = function() {
	var win_width = document.documentElement.clientWidth - 384;
	document.getElementById("EditArea").setAttribute("style","width:"+win_width+"px;");
}