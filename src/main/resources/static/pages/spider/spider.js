var app = angular.module('myApp', [
    'ui.bootstrap'
]);
var tabledata
var table = $('#table');
var error_table = $('#error_table');

function getSpiderStatus() {

    $.ajax({
        url: ip + "/spider/getClassStatus?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid"),
        // url:"a.json",
        type: 'get',
        dataType: 'json',
    })
        .done(function (json) {
            console.log("加载课程列表 success");
            tabledata = json
            table.bootstrapTable("load", tabledata)
        })
        .fail(function () {
            console.log("error");

        })
        .always(function (json) {
            console.log("complete");
            tabledata = json
            table.bootstrapTable("load", tabledata)
        });
}

var nowclassname;
app.controller('menu', function ($scope, $http) {

    error_table.bootstrapTable({
        // data:tabledata,
        striped: true,
        // singleSelect: false,
        // data-locale:"zh-US" , //表格汉化
        // search: true, 
        search: true, //显示搜索框
        searchAlign: 'left',
        showToggle: true, //名片格式
        // cardView: true,
        rowStyle: function (row, index) {
            // var classes = ['active', 'success', 'info', 'warning', 'danger'];
            var classes = ['success', 'info', 'active'];
            return {
                classes: classes[index % 3]
            };

        },
        onEditableSave: function (field, row, oldValue, $el) {
            row.newTermName = row.termName;
            row.termName = oldValue;
            $.ajax({
                type: "get",
                url: ip + "/datainput/modifyRelation",
                data: row,
                cache: false,
                dataType: 'JSON',
                success: function (data, status) {
                    // if (status == "success") {
                    //     $("#myModalLabel").html(row.NewTermName)
                    //     $("#myModalContent").html("保存成功！");            
                    //     $('#myModal').modal('toggle');
                    // }
                    getErrorTerm()
                },
                error: function () {
                    alert('编辑保存失败!请重试或者联系管理员！');
                },
                complete: function () {

                }

            })
            $.ajax({
                type: "get",
                url: ip + "/spider/updateErrorTerm",
                data: row,
                cache: false,
                dataType: 'JSON',
                success: function (data, status) {
                    getErrorTerm()
                    if (status == "success") {
                        $("#myModalLabel").html(row.NewTermName)
                        $("#myModalContent").html("保存成功！");
                        $('#myModal').modal('toggle');
                    }

                },
                error: function () {
                    alert('编辑保存失败!请重试或者联系管理员！');
                },
                complete: function () {

                }

            })
        },
        columns: [{
            title: '知识点名字',
            field: 'termName',
            align: 'center',
            valign: 'middle',
            editable: {
                type: 'text',
                title: '输入新的知识点名字',
                validate: function (v) {
                    if (!v) return '不能为空';
                }
            }
        },
            {
                title: '章',
                field: 'parentChapterName',
                align: 'center',
                valign: 'middle',
            },
            {
                title: '节',
                field: 'chapterName',
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
                        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
                        '<i class="glyphicon glyphicon-remove"></i>删除',
                        '</a>'
                    ].join(' ');
                },
                events: {
                    // 'click .edit': function (e, value, row, index) {
                    //   // console.log(row)
                    //   $('#table > tbody > tr:nth-child('+index+') > td:nth-child(1) a');
                    // },
                    'click .remove': function (e, value, row, index) {
                        // console.log(row)
                        $http.get(ip + "/datainput/deleteReletionByRelationID?temp=" + new Date().getTime() + "&relationID=" + row.relationID).success(
                            function (json) {

                                $.ajax({
                                    type: "get",
                                    url: ip + "/spider/deleteErrorTerm?TermID=" + row.termID,
                                    cache: false,
                                    dataType: 'JSON',
                                    success: function (data, status) {
                                        getErrorTerm(nowclassname)
                                        if (status == "success") {
                                            $("#myModalLabel").html(row.NewTermName)
                                            $("#myModalContent").html("删除成功！");
                                            $('#myModal').modal('toggle');
                                        }

                                    },
                                    error: function () {
                                        alert('删除失败!请重试或者联系管理员！');
                                    },
                                    complete: function () {

                                    }

                                })
                                console.log("删除" + row.TermName + "成功")
                                getErrorTerm(nowclassname);
                                // getSpiderStatus()
                            });
                    }
                }
            }
        ]
    });
    // table初始化
    $.ajax({
        url: ip + "/spider/getClassStatus?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid"),
        // url:"a.json",
        type: 'get',
        // async:false,
        dataType: 'json',
    })
        .done(function (json) {
            console.log("加载课程 success");
            console.log(json[0].termSum)
            if (json[0].alreadyTermNum < json[0].termSum) {
                $("#myModalLabel").html("《" + json[0].className + "》")
                $("#myModalContent").html("存在尚未爬取得知识点，请点击 开始爬取 ！</br>如果您刚刚点击，请稍后再试！</br>如果您等待很久没有响应，请再次点击！");
                $('#myModal').modal('toggle');
            }

            tabledata = json
            table.bootstrapTable({
                data: tabledata,
                striped: true,
                // singleSelect: false,
                // data-locale:"zh-US" , //表格汉化
                // search: true, 
                // search: true, //显示搜索框
                searchAlign: 'left',
                showToggle: true, //名片格式
                // cardView: true,
                rowStyle: function (row, index) {

                    // var classes = ['active', 'success', 'info', 'warning', 'danger'];
                    var classes = ['success', 'info', 'active'];
                    return {
                        classes: classes[index % 3]
                    };

                },

                columns: [{
                    title: '课程名字',
                    field: 'className',
                    align: 'center',
                    valign: 'middle'
                },
                    {
                        title: '知识点总个数',
                        field: 'termSum',
                        align: 'center',
                        valign: 'middle',
                    },
                    {
                        title: '已完成个数',
                        field: 'alreadyTermNum',
                        align: 'center',
                        valign: 'middle',
                    },
                    {
                        title: '知识点出错个数',
                        field: 'errorTermNum',
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {

                            return '<span style="color:red">' + row.errorTermNum + '</span>'
                        }
                    },
                    {
                        title: '完成',
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {
                            ///计算两个整数的百分比值 
                            function GetPercent(num, total) {
                                num = parseFloat(num);
                                total = parseFloat(total);
                                if (isNaN(num) || isNaN(total)) {
                                    return "-";
                                }
                                return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%");
                            }

                            return '<span style="color:green">' + GetPercent(row.alreadyTermNum, row.termSum) + '</span>'
                        }
                    },
                    {
                        title: '操作',
                        field: 'action',
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {
                            return [
                                '<a class="respider ml10" href="javascript:void(0)" title="submit" style="color:red">',
                                '<i class="glyphicon glyphicon-download-alt"></i>开始爬取',
                                '</a> &nbsp',
                                '<a class="edit ml10" href="javascript:void(0)" title="Edit" >',
                                '<i class="glyphicon glyphicon-edit"></i>修改无法爬取知识点',


                                //   '</a>',
                                //   '<a class="submit ml10" href="javascript:void(0)" title="submit" style="color:red">',
                                //   '<i class="glyphicon glyphicon-ok"></i>提交',
                                '</a>'
                            ].join(' ');
                        },
                        events: {
                            'click .edit': function (e, value, row, index) {
                                nowclassname = row.className
                                console.log(row.className + "  修改无法爬取知识点")
                                if (row.alreadyTermNum != row.termSum) {
                                    // $("#myModalLabel").html("《" + nowclassname + "》")
                                    // $("#myModalContent").html("正在爬取数据！请等待结果!");
                                    // $('#myModal').modal('toggle');
                                    // getSpiderStatus();
                                    // return
                                } else if (row.errorTermNum == 0) {
                                    $("#myModalLabel").text(row.className)
                                    $("#myModalContent").html("该课程没有无法爬取的知识点！请提交");
                                    $('#myModal').modal('toggle');
                                    return
                                }
                                getErrorTerm(row.className)

                            },
                            'click .respider': function (e, value, row, index) {

                                nowclassname = row.className
                                $http.get(ip + "/spider/storeAllTermByClassID?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid"))
                                    .error(function () {
                                        $("#myModalLabel").html("《" + nowclassname + "》")
                                        $("#myModalContent").html("服务器网络问题，请点击 开始爬取 ！" + "</br> ");
                                        $('#myModal').modal('toggle');
                                    });
                                $("#myModalLabel").html("《" + nowclassname + "》")
                                $("#myModalContent").html(row.className +
                                    "</br> 开始爬取数据......." + "</br>" +
                                    " 请点击关闭，注意表格中的完成率！");
                                $('#myModal').modal('toggle');
                                getSpiderStatus();

                            }
                            //   ,'click .submit': function (e, value, row, index) {
                            //     nowclassname=row.className
                            //     if (row.alreadyTermNum!=row.termSum) {
                            //       $("#myModalLabel").html("《"+nowclassname+"》")
                            //       $("#myModalContent").html("正在爬取数据！请等待结果!");            
                            //       $('#myModal').modal('toggle');
                            //       getSpiderStatus();
                            //       return
                            //     } else if (row.errorTermNum!=0) {
                            //       $("#myModalLabel").text( row.className) 
                            //       $("#myModalContent").html("该课程存在无法爬取的知识点！请修改");
                            //       $('#myModal').modal('toggle');
                            //       return
                            //     }
                            //     $http.get(ip+"/SpiderAPI/changeSystemStatus?temp="+new Date().getTime()+"&className="+row.className).success(
                            //           function(json)
                            //            { 
                            //               console.log("提交 "+nowclassname+" 成功! "+ json.success )
                            //               $("#myModalLabel").text( nowclassname) 
                            //               $("#myModalContent").html("提交成功！请进行下一步!");
                            //               $('#myModal').modal('toggle');
                            //               // alert("提交成功！请进行下一步!")
                            //               // location.reload()

                            //           });

                            //   }
                        }
                    }
                ]
            });
        })
        .fail(function () {
            console.log("error");
            $('#spiderBoxTitle').text('没有正在爬取的课程')
        });

    // 摸态框关闭刷新事件
    $("#ModalClose").click(function (event) {
        /* Act on the event */
        $http.get(ip + "/spider/storeAllTermByClassID?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid"));
        getSpiderStatus();
    });

});


window.setInterval(getSpiderStatus, 5000);
//得到错误的知识点
function getErrorTerm() {
    $.get(ip + "/spider/getErrorTermInfo?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid")).success(
        function (json) {
            console.log('从新加载 error term')
            $("#error_myModalLabel").text("《" + getCookie("nowclassname") + "》")
            error_table.bootstrapTable("load", json)
            // $('#error_myModall').modal('toggle');
            $('#error_myModal').modal('show');
        });
}