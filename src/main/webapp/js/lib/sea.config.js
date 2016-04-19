(function() {
	// to be modified
	var base = window.basePath + 'js/';
	seajs.config({
		base: base,
		alias: {
			'lib/jquery': 'lib/jquery.min.js',
			'util/bootstrap.datetimepicker': 'util/bootstrap-datetimepicker.min.js',
			'util/bootstrap.datetimepicker.zh-CN': 'util/bootstrap-datetimepicker.zh-CN.js',
			'util/jquery.tmpl': 'util/jquery.tmpl.min.js',
			'util/jquery.markitup': 'util/jquery.markitup-1.1.14.min.js',
			'page/login': 'page/login_150709.js',
			'page/index': 'page/index_150629.js',
			'page/edit': 'page/edit_150727.js',
			'module/BatchRecognize': 'module/BatchRecognize_150715.js',
			'module/SampleCheck': 'module/SampleCheck_150713.js',
			'module/Dialog': 'module/Dialog_150728.js'
		}
	});
	if(window.MX && MX.load) {
		var module = seajs.Module, fetch = module.prototype.fetch, fetchingList = {};
		module.prototype.fetch = function(requestCache) {
			var mod = this;
			seajs.emit('fetch', mod);
			var uri = mod.uri, info, file, version, load;
			if(uri.indexOf(base) !== -1) {
				info = uri.split(base)[1].split('_');
				file = info[0];
				version = info[1];

				if(fetchingList[file]) {
					fetchingList[file].push(mod);
					return;
				}

				fetchingList[file] = [mod];

				load = {
					js: file,
					success: function() {
						var mods = fetchingList[file], m;
						delete fetchingList[file];
						m = mods.shift();
						while(m) {
							m.load();
							m = mods.shift();
						}
					}
				};
				if(version) {
					load.version = version.slice(0, -3);
				} else {
					load.js = file.slice(0, -3);
				}
				MX.load(load);
			} else {
				fetch.call(mod, requestCache);
			}
		};
	}
}());
