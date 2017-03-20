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

    function f5Getclass(ClassName) {
        $http.get("http://" + ip + "/KG/datainput/getRelationAndTermCount?temp=" + new Date().getTime() + "&ClassName=" + ClassName).success(
            function (json) {

                $scope.termcount = json.termCount.length;
                $scope.relationcount = json.termCount.length;
                countDOM.show()
                if (json.status == '已完成') {
                    console.log(ClassName + '已完成.无法提交')
                    $('#submit').attr("disabled", "disabled");
                    $("#myModalLabel").text(ClassName)
                    $("#myModalContent").html("该课程已经提交！请勿重复提交！");
                    $('#myModal').modal('toggle');
                } else {
                    $('#submit').removeAttr("disabled");
                }

                if (json.relation.length != 0) {
                    table.bootstrapTable("load", json.relation)
                    $('.box-footer').text('')
                    tableDOM.show()
                }
                else {
                    table.bootstrapTable("load", json.relation)
                    $('.box-footer').text('无数据')
                    tableDOM.show()
                }
            });
    }

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
            row.NewTermName = row.TermName;
            row.TermName = oldValue;
            $.ajax({
                type: "get",
                url: "http://" + ip + "/KG/datainput/modifyRelation",
                data: row,
                cache: false,
                dataType: 'JSON',
                success: function (data, status) {
                    if (status == "success") {
                        $("#myModalLabel").text(row.ClassName)
                        $("#myModalContent").html("修改成功！");
                        $('#myModal').modal('toggle');
                    }
                    f5Getclass(row.ClassName)
                },
                error: function () {
                    alert('编辑失败');
                },
                complete: function () {

                }

            })
        },
        columns: [
            {
                title: '知识点',
                field: 'TermName',
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
                field: 'ClassName',
                align: 'center',
                valign: 'middle'
            },
            {
                title: '章',
                field: 'ParentChapterName',
                align: 'center',
                valign: 'middle',
            },
            {
                title: '节',
                field: 'ChapterName',
                align: 'center',
                valign: 'middle',
            }
            ,
            {
                title: '小节',
                field: 'ChildrenChapterName',
                align: 'center',
                valign: 'middle',
            }
            ,
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
                        '<small>修改请直接点击左侧知识点名字</small>',
                        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
                        '<i class="glyphicon glyphicon-remove"></i>删除',
                        '</a>'
                    ].join(' ');
                }
                , events: {
                // 'click .edit': function (e, value, row, index) {
                //   console.log(index)
                //   $('#table > tbody > tr:nth-child('+index+') > td:nth-child(1) a');

                // },
                'click .remove': function (e, value, row, index) {
                    $http.get("http://" + ip + "/KG/datainput/deleteReletionByCatalogID?temp=" + new Date().getTime() + "&TermName=" + row.TermName + "&ClassName=" + row.ClassName + "&CatalogID=" + row.CatalogID).success(
                        function (json) {
                            console.log("删除" + row.TermName + "成功")
                            f5Getclass(row.ClassName);
                        });
                }
            }
            }
        ]
    });

    $http.get("http://" + ip + "/KG/datainput/getClassState?temp=" + new Date().getTime()).success(
        function (json) {
            console.log(json)
            $scope.ClassList = json;
        });

    $scope.getClassStatus = function (ClassName) {
        f5Getclass(ClassName)
    }

    $scope.submitClass = function (ClassName) {
        console.log(ClassName)
        $http.get("http://" + ip + "/KG/datainput/submitClass?temp=" + new Date().getTime() + "&ClassName=" + ClassName).success(
            function (data) {
                $("#myModalLabel").text(ClassName)
                $http.get("http://" + ip + "/KG/SpiderAPI/storeAllTermByClassName?temp=" + new Date().getTime() + "&className=" + ClassName);
                for (key in data) {
                    $("#myModalContent").html(data[key] + "</br> 开始爬取数据.......");
                    $('#myModal').modal('toggle');
                }
                ;
            });
    }

});