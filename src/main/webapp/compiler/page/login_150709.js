define("page/login",["lib/jquery"],function(a){var d=a("lib/jquery"),a=function(b,a){var c=this;c._body=d("#"+b);c=d.extend(c,a);c.init();c.addEvent()};a.prototype={alertTip:"用户名和密码不能为空",init:function(){this._account=this._body.find(".account").focus();this._password=this._body.find(".password")},addEvent:function(){var b=this;d(document).on("keydown",function(a){13===a.keyCode&&b.submit()});b._body.on("click",".confirm",function(){b.submit()}).on("click",".reset",function(){b._account.val("").focus();b._password.val("")})},submit:function(){this.validate()&&this._body[0].submit()},validate:function(){if(this._account.val()&&this._password.val())return!0;alert(this.alertTip)}};return a});