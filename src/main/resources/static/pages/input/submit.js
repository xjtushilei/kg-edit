var app = angular.module('myApp', [
    'ui.bootstrap'
]);
var parentChapterName = '';
var parentChapterID = '';
var chapterID = '';
var chapterName = '';
var childrenChapterID = '';
var childrenChapterName = '';

app.controller('menu', function ($scope, $http) {

    $scope.nowClassName = getCookie("nowclassname")

    function f5Getclass() {
        $http.get(ip + "/datainput/getRelationAndTermCount?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid")).success(
            function (json) {

                $scope.termcount = json.termCount.length;
                $scope.relationcount = json.relation.length;
                countDOM.show()
                if (json.relation.length != 0) {
                    table.bootstrapTable("load", json.relation)
                    $('.box-footer').text('')
                    tableDOM.show()
                } else {
                    table.bootstrapTable("load", json.relation)
                    $('.box-footer').text('无数据')
                    tableDOM.show()
                }
            });
    }

    f5Getclass()
    var countDOM = $('#count');
    var tableDOM = $('#tabledom');
    countDOM.hide()
    tableDOM.hide()
    // table初始化
    var table = $('#table');

    table.bootstrapTable({
        // data:json.relation,
        striped: true,
        // singleSelect: false,
        // data-locale:"zh-US" , //表格汉化
        search: true, //显示搜索框
        onEditableSave: function (field, row, oldValue, $el) {
            row.newTermName = row.termName;
            row.termName = oldValue;
            console.log(row)
            $.ajax({
                type: "get",
                url: ip + "/datainput/modifyRelation",
                data: row,
                cache: false,
                dataType: 'JSON',
                success: function (data, status) {
                    if (status == "success") {
                        $("#myModalLabel").text(row.ClassName)
                        $("#myModalContent").html("修改成功！");
                        $('#myModal').modal('toggle');
                    }
                    f5Getclass()
                },
                error: function () {
                    alert('编辑失败');
                },
                complete: function () {

                }

            })
        },
        columns: [{
            title: '知识点',
            field: 'termName',
            align: 'center',
            valign: 'middle',
            editable: {
                type: 'text',
                title: '输入新的知识点',
                validate: function (v) {
                    if (!v) return '不能为空';
                }
            }
        },
            {
                title: '课程名字',
                field: 'className',
                align: 'center',
                valign: 'middle'
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
                title: '小节',
                field: 'childrenChapterName',
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
                        // '<a class="edit ml10" href="javascript:void(0)" title="Edit">',
                        // '<i class="glyphicon glyphicon-edit"></i>修改',
                        // '</a>',
                        '<small><span style="color: blue">修改</span>请直接点击左侧知识点名字</small>',
                        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
                        '<span style="color: red"><i class="glyphicon glyphicon-remove"></i>删除</span>',
                        '</a>'
                    ].join(' ');
                },
                events: {
                    // 'click .edit': function (e, value, row, index) {
                    //   console.log(index)
                    //   $('#table > tbody > tr:nth-child('+index+') > td:nth-child(1) a');

                    // },
                    'click .remove': function (e, value, row, index) {
                        $http.get(ip + "/datainput/deleteReletionByRelationID?temp=" + new Date().getTime() + "&relationID=" + row.relationID).success(
                            function (json) {
                                console.log("删除" + row.TermName + "成功")
                                f5Getclass(row.ClassName);
                            });
                    }
                }
            }
        ]
    });


    $scope.getClassStatus = function (ClassName) {
        f5Getclass(ClassName)
    }


});