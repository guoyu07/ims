/**
 * Created by Administrator on 2015/2/12.
 */
$(function () {
    $("#startTime").focus(function () {
        var endTime = $dp.$('endTime');
        WdatePicker({
            onpicked: function () {
                endTime.focus();
            },
            dateFmt: 'yyyy-MM-dd',
            minDate: '%y-%M-{%d}',
            maxDate: '#F{$dp.$D(\'endTime\')}'
        })
    });
    $("#endTime").focus(function () {
        WdatePicker({
            dateFmt: 'yyyy-MM-dd',
            minDate: '#F{$dp.$D(\'startTime\')}'
        });
    });

    $("#chk_all").click(function(){
        var chk = $(this).prop("checked");
        $("input[name='chk_list']").each(function() {
            $(this).prop("checked",chk);
        });
        var $chk_list = $("input[name='chk_list']");
        $chk_list.click(function(){
            $("#chk_all").prop("checked" , $chk_list.length == $chk_list.filter(":checked").length ? true :false);
        });
    });

    //获取列表
    getList('js/list.json',1);


    //翻页
    var currentPage = 1;
    $("#prev").click(function(){
        if(currentPage==1){
            alert("已经是第一页");
            return;
        }
        getList('js/list.json',currentPage-1)
    });
    //下一页
    $("#next").click(function(){
        getList('js/list.json',currentPage+1)
    });

    //生成
    $("#generateSubject").click(function(){
        submit("/url");
    });

});

//获取列表
function getList(url,page) {
    $.ajax({
        type: "POST",
        url: url,
        data: {page: page},
        dataType: "json",
        success: function (data) {
            console.log(data);
            $('#listTable tr:gt(0)').remove();
            var html = '';
            $.each(data, function (i) {
                html += '<tr>' +
                '<td><input id="' + data[i].id + '" type="checkbox" name="chk_list"></td>' +
                '<td>' + data[i].id + '</td>' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + data[i].nums + '</td>' +
                '</tr>';
            });
            $('#listTable').append(html)
        }
    });
}

//获取时间范围
function getTimeRange(){
    var timeRange = {};
    timeRange.startTime = $("#startTime").val();
    timeRange.endTime = $("#startTime").val();
    if(timeRange.startTime == ''){
        alert("请选择开始时间");
        return;
    }
    if(timeRange.endTime == ''){
        alert("请选择结束时间");
        return;
    }
    return timeRange;
}

//获取每人抽查数
function getEveryoneNums(){
    var nums = $("#everyoneNums").val();
    if(nums==''){
        alert("请输入每组抽查数");
        return;
    }
    return nums;
}

//获取已选中的条目id
function getSetectedItemById(){
    var items = [];
    $("input[name='chk_list']").filter(":checked").each(function(){
        var id = $(this).attr('id');
        items.push(id);
    });
    return items;
}

//生成抽检题目
function generateSubject(){
    var subject ={};
    subject.timeRange =getTimeRange();
    subject.nums = getEveryoneNums();
    subject.items = getSetectedItemById();
    console.log(subject);
    return subject;
}

function num(obj){
    if(obj.value.length==1){
        obj.value=obj.value.replace(/[^1-9]/g,'');
    }
    else{
        obj.value=obj.value.replace(/\D/g,'')
    }
}

function submit(url){
    var data = generateSubject();
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        success: function (data) {
            $('#listTable tr:gt(0)').remove();
            var html = '';
            $.each(data, function (i) {
                html += '<tr>' +
                '<td><input id="' + data[i].id + '" type="checkbox" name="chk_list"></td>' +
                '<td>' + data[i].id + '</td>' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + data[i].nums + '</td>' +
                '</tr>';
            });
            $('#listTable').append(html)
        }
    });
}