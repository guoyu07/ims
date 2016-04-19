define("module/SampleCheck",["lib/jquery","module/Dialog","util/jquery.tmpl","util/bootstrap.datetimepicker.zh-CN"],function(t){"use strict";var e,a,i;return e=t("lib/jquery"),a=t("module/Dialog"),t("util/jquery.tmpl"),t("util/bootstrap.datetimepicker.zh-CN"),i=function(t,i){i=i||{},e.ajax({url:window.basePath+"tranops/getAllTikuTeam",type:"GET",dataType:"json",success:function(l){if(0===l.status){var n;n=["{{each(i, item) items}}","<tr>","<td>",'<input class="check-item" name="checkbox" type="checkbox" value="${item.id}">',"</td>","<td>${i + 1}</td>","<td>${item.captainName}</td>","<td>${item.usersNum}</td>","</tr>","{{/each}}"].join(""),e.template("listTemplate",n),a.show("modal-dialog",{sizeClass:"modal-lg",title:"生成抽检",content:["<p>",'<ul class="record-data">',"<li>","抽检日期:",'<input style="margin-left:16px;" class="form-control form-date start-time" type="text" value="" name="startTime" data-date-format="yyyy-mm-dd 00:00:00"/>',"~",'<input class="form-control form-date end-time" type="text" value="" name="endTime"  data-date-format="yyyy-mm-dd 23:59:59"/>',"</li>","</ul>","</p>","<p>",'<span style="display:inline-block;">每组抽查：</span>','<input class="team_name" type="text" value="" maxLength="30"/>',"</p>","<p>",'<span style="display:inline-block;">抽查小组：</span>',"</p>",'<div class="table-frame">','<table class="table table-bordered check-team" border="1" cellpadding="0" cellspacing="0">',"<tr>","<th>",'<input class="check-all" type="checkbox">',"</th>","<th>序号</th>","<th>组长</th>","<th>小组人数</th>","</tr>","</table>","</div>"].join(""),source:i.source,initCall:function(){var t,a,i=this;i._body.find(".check-team").append(e.tmpl("listTemplate",{items:l.result})),i._body.find(".btn-confirm").text("生成抽检"),t=i._body.find(".check-all"),a=i._body.find(".check-item"),i._body.on("click.bs.custom",".check-all",function(t){a.prop("checked",e(this).prop("checked"))}),i._body.on("click",".check-item",function(a){e(this).prop("checked")||t.prop("checked",!1)}),i._body.find(".form-date").datetimepicker({language:"zh-CN",weekStart:1,todayBtn:1,autoclose:1,todayHighlight:1,minView:2,forceParse:0,showMeridian:1})},confirm:function(){var a,i,l,n=this;i=n._body,l=i.find(".check-item"),a=Array.prototype.map.call(l.filter(":checked"),function(t){return e(t).val()}).join(),e.ajax({url:t,type:"GET",dataType:"json",data:{startTime:i.find(".start-time").val(),endTime:i.find(".end-time").val(),n:i.find(".team_name").val(),teamIds:a},success:function(t){0===t.status?window.location.reload():(n.enableConfirm(),alert(t.msg))},error:function(){n.enableConfirm(),alert("网络错误，请稍后重试")}})}})}else alert(l.msg)},error:function(){alert("网络错误，请稍后重试")}})}});