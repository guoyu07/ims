var bone = require('bone'),
	proxy = require('bone-cli-proxy'),
	connect = require('bone-connect'),
	less = require('bone-act-less'),
	sass = require('bone-act-sass'),
	concat = require('bone-act-concat'),
	// 混淆
	uglify = require('bone-act-uglify'),
	build = require('bone-cli-build'),
	// 定义生成文件夹dist， 用来存放最终的文件
	dist, buildPath,
	seajs;
dist = bone.dest('dist');
buildPath = bone.dest('build');

buildPath.cwd('~')
	.src('js/bootstrap/v2js.js');
var page = buildPath.dest('js/page')
	.src('~/js/page/!(index).js');
var module = buildPath.dest('js/module')
	.src('~/js/module/*.js');
var index = buildPath.dest('js/page')
	.src('~/js/page/index.js')
	.act(concat({
		files: [
			'js/util/jquery.tmpl.min.js',
			'js/module/TreeList.js',
			'js/page/index.js'
		]
	}));
var seajs = buildPath.dest('js/lib')
	.src('~/js/lib/sea.js')
	.act(concat({
		files: [
			'js/lib/sea.js',
			'js/lib/sea.config.js'
		]
	}));
var sass = buildPath.dest('css')
	.src('~/scss/*.scss')
	.act(sass)
	.rename(function(filename) {
		return filename.replace(/\.scss$/, '.css');
	});

bone.cli(build(), {
	alias: 'my_build'
});
if(bone.cli.argv.command === 'my_build') {
	seajs.act(uglify);
	index.act(uglify);
	page.act(uglify);
	module.act(uglify);
}
// 加载代理
bone.cli(proxy({
	pac: true,
	replaceRules: [
		[
			/http:\/\/(?:\d+\.){3}\d+:\d+\/tiku_ops\/js\/page\/index([\w_]+)?\.js/,
			'~/build/js/page/index.js'
		],
		[
			/http:\/\/(?:\d+\.){3}\d+:\d+\/tiku_ops\/js\/page\/login([\w_]+)?\.js/,
			'~/build/js/page/login.js'
		],
		[
			/http:\/\/(?:\d+\.){3}\d+:\d+\/tiku_ops\/js\/lib\/sea([\w_]+)?\.js/,
			'~/build/js/lib/sea.js'
		],
	]
}));
// 加载支持connect的插件
bone.cli(connect({
	base: './dist',
	port: 8085
}));
