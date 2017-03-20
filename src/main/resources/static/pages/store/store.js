var app = angular.module('myApp', [
    'ui.bootstrap'
]);
var tabledata
var table = $('#table');
function getSpiderStatus() {

    $.ajax({
        url: "http://" + ip + "/KG/RDFTDB/getClassState?temp=" + new Date().getTime(),
        // url:"a.json",
        type: 'get',
        async: false,
        dataType: 'json',
    })
        .done(function (json) {
            console.log("加载课程列表 刷新success");
            tabledata = json
            table.bootstrapTable("load", tabledata)
        })
        .fail(function () {
            console.log("error");

        })
        .always(function () {
            console.log("complete");
            // tabledata=json
            // table.bootstrapTable("load",tabledata)
        });
}

var nowclassname;
app.controller('menu', function ($scope, $http) {


    // table初始化
    $.ajax({
        url: "http://" + ip + "/KG/RDFTDB/getClassState?temp=" + new Date().getTime(),
        // url:"a.json",
        type: 'get',
        cache: false,
        dataType: 'json',
    })
        .done(function (json) {
            console.log("加载课程列表 success");
            tabledata = json
            table.bootstrapTable({
                data: tabledata,
                striped: true,
                // singleSelect: false,
                // data-locale:"zh-US" , //表格汉化
                // search: true,
                search: true, //显示搜索框
                searchAlign: 'left',
                showToggle: true,   //名片格式
                // cardView: true,
                rowStyle: function (row, index) {

                    // var classes = ['active', 'success', 'info', 'warning', 'danger'];
                    var classes = ['success', 'info', 'active'];
                    return {
                        classes: classes[index % 3]
                    };

                },
                columns: [
                    {
                        title: '课程名字',
                        field: 'ClassName',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: '数据库记录条数',
                        field: 'number',
                        align: 'center',
                        valign: 'middle',
                    },
                    {
                        title: '状态',
                        field: 'Store',
                        align: 'center',
                        valign: 'middle',
                    },
                    {
                        title: '操作',
                        field: 'action',
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {
                            return [

                                '<a class="start ml10" href="javascript:void(0)" title="Edit" >',
                                '<i class="glyphicon glyphicon-edit"></i>开始存储数据',
                                '</a> &nbsp',
                                '<a class="restart ml10" href="javascript:void(0)" title="submit" style="color:blue">',
                                '<i class="glyphicon glyphicon-download-alt"></i>强制重新存储',
                                '</a>',
                                '<a class="submit ml10" href="javascript:void(0)" title="submit" style="color:red">',
                                '<i class="glyphicon glyphicon-ok"></i>提交',
                                '</a>'
                            ].join(' ');
                        }
                        , events: {
                        'click .start': function (e, value, row, index) {
                            nowclassname = row.ClassName
                            console.log(row.ClassName + "  存储开始!")
                            if (row.Store == '未开始') {
                                $("#myModalLabel").html("《" + nowclassname + "》")
                                $("#myModalContent").html("正在存储！请等待结果!");
                                $('#myModal').modal('toggle');
                                $http.get("http://" + ip + "/KG/RDFTDB/store?temp=" + new Date().getTime() + "&ClassName=" + row.ClassName).success(function () {
                                    getSpiderStatus();
                                    $("#myModalContent").html("该课程已完成！请提交！");
                                })
                                getSpiderStatus();
                                return
                            }
                            else if (row.Store == '正在执行') {
                                $("#myModalLabel").text(row.className)
                                $("#myModalContent").html("该课程正在存储！请不要重复存储！");
                                $('#myModal').modal('toggle');
                                getSpiderStatus();
                                return
                            } else if (row.Store == '已完成') {
                                $("#myModalLabel").text(row.className)
                                $("#myModalContent").html("该课程已完成！请提交！");
                                $('#myModal').modal('toggle');
                                getSpiderStatus();
                                return
                            }
                        },
                        'click .restart': function (e, value, row, index) {
                            nowclassname = row.ClassName
                            $("#myModalLabel").html("《" + nowclassname + "》")
                            $("#myModalContent").html("正在存储！请等待结果!");
                            $('#myModal').modal('toggle');
                            $http.get("http://" + ip + "/KG/RDFTDB/store?temp=" + new Date().getTime() + "&ClassName=" + row.ClassName).success(function () {
                                getSpiderStatus();
                                $("#myModalContent").html("该课程已完成！请提交！");
                            })
                        },
                        'click .submit': function (e, value, row, index) {
                            nowclassname = row.ClassName
                            if (row.Store == '已完成') {
                                $("#myModalLabel").html("《" + nowclassname + "》")
                                $("#myModalContent").html("提交成功!");
                                $('#myModal').modal('toggle');
                                getSpiderStatus();
                                return
                            } else {
                                $("#myModalLabel").text(row.className)
                                $("#myModalContent").html("提交失败，请存储!");
                                $('#myModal').modal('toggle');
                                return
                            }
                        }
                    }
                    }
                ]
            });
        })
        .fail(function () {
            console.log("error");
            $('#storeBoxTitle').text('没有需要存储的课程')
        });

});


