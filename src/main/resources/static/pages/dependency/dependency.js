var app = angular.module('myApp', [
    'ui.bootstrap'
]);

app.controller('menu', function ($scope, $http) {


    var countDOM = $('#count');
    var tableDOM = $('#tabledom');
    countDOM.hide()
    tableDOM.hide()
    // table初始化
    var table = $('#table');

    table.bootstrapTable({
        // data:json.relation,
        striped: true,
        pagination: true, //分页
        pagination: true,
        //是否启用排序
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        // singleSelect: false,
        // data-locale:"zh-US" , //表格汉化
        search: true, //显示搜索框

        columns: [{
            title: '课程名字',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {

                return getCookie("nowclassname");
            },
        },
            {
                title: '知识点名字1',
                field: 'startTermName',
                align: 'center',
                valign: 'middle',
            },
            {
                title: '知识点名字2',
                field: 'endTermName',
                align: 'center',
                valign: 'middle',
            },
            {
                title: '置信度',
                field: 'confidence',
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
                        '<a class="remove ml10"   href="javascript:void(0)" title="Remove">',
                        '<i class="glyphicon glyphicon-remove"></i>删除',
                        '</a>'
                    ].join(' ');
                },
                events: {
                    // 'click .edit': function (e, value, row, index) {
                    //   console.log(index)
                    //   $('#table > tbody > tr:nth-child('+index+') > td:nth-child(1) a');

                    // },
                    'click .remove': function (e, value, row, index) {
                        if (confirm('确定要执行此操作吗?')) {
                            $http.get(ip + "/dependency/delete?temp=" + new Date().getTime() + "&dependencyid=" + row.dependencyID).success(
                                function (json) {
                                    console.log("删除" + row.startTermName + "-" + row.endTermName + "成功")
                                    $scope.getall();
                                });
                        }

                    }
                }
            }
        ]
    });


    $scope.getall = function () {
        $http.get(ip + "/dependency/getAll?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid")).success(
            function (json) {
                // console.log(json)
                $scope.dependencyList = json
                if (json.length != 0) {
                    table.bootstrapTable("load", json)
                    $('.box-footer').text('')
                    tableDOM.show()
                    countDOM.show()
                } else {
                    table.bootstrapTable("load", [])
                    $('.box-footer').text('无数据')
                    countDOM.show()
                    tableDOM.hide()
                }
            }
        );
    }
    $scope.getall();

    $scope.startAlgorithm = function () {
        console.log(getCookie("nowclassname") + "  开始算法分析")
        $("#myModalLabel").text(getCookie("nowclassname"))
        $("#myModalContent").html('</br> <span class="badge" style="background-color:green;font-size: 18px">正在分析，请等待.......</span>  ' +
            '</br> ' +
            '<span class="badge" style="background-color:red;font-size: 18px">时间大约10秒到1分钟.......</span> ' +
            '</br> ' +
            '<span class="badge" style="background-color:red;font-size: 18px">5分钟后如果还没有结果，请重新点击开始算法分析.......</span>');
        $('#myModal').modal('toggle');
        $http.get(ip + "/dependency/startAlgorithm?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid")).success(
            function (data) {
                $("#myModalLabel").text(getCookie("nowclassname"))
                $("#myModalContent").html('</br> <span class="badge" style="background-color:red;font-size: 18px">分析结束.......</span>');
                $('#myModal').modal('show');
                $scope.getall()
            });
    }


    $scope.addfuc = function () {
        if ($scope.addendTermID === undefined || $scope.addstartTermID === undefined) {
            alert('请先在两个输入框中选择已经存在的知识点' + "\n\n （提示：要点击输入提示框中的知识点）")
            return
        }
        console.log($scope.addendTermID)
        $.ajax({
            url: ip + "/dependency/add?temp=" + new Date().getTime() + "&classID=" + getCookie("nowclassid") + "&startTermName=" + $scope.addstartTermName + "&startTermID=" + $scope.addstartTermID + "&endTermName=" + $scope.addendTermName + "&endTermID=" + $scope.addendTermID + "&confidence=1",
            type: 'get',
            dataType: 'json',
            data: {},
        })
            .done(function (data) {
                $("#myModalLabel").text("成功")
                $("#myModalContent").html(data.success);
                $('#myModaladd').modal('hide');
                $('#myModal').modal('show');

                $scope.getall();
                $scope.addendTermID = undefined
                $scope.addstartTermID = undefined
            })
            .error(function (data) {
                console.log(data)
                $("#myModalLabel").text("错误")
                $("#myModalContent").html(data.responseJSON.error);
                $('#myModal').modal('toggle');
            })

    }
    $scope.getClassTerms = function () {
        $http.get(ip + "/dependency/getClassTerms?temp=" + new Date().getTime() + "&classID=" + getCookie("nowclassid")).success(
            function (data) {
                // console.log(data)
                $('#Termname1').typeahead({
                    source: function (query, process) {
                        process(data);
                    },
                    displayText: function (item) {
                        return item.termName
                    },
                    updater: function (item) {
                        console.log(item)
                        $scope.addstartTermName = item.termName
                        $scope.addstartTermID = item.termID
                        return item.termName; //这里一定要return，否则选中不显示
                    },
                    afterSelect: function (item) {
                        //选择项之后的事件 ，item是当前选中的。
                        console.log(item)
                        $("#Termname1").blur()
                    },
                    minLength: 0
                });
                $('#Termname2').typeahead({
                    source: function (query, process) {
                        process(data);
                    },
                    displayText: function (item) {
                        return item.termName
                    },
                    updater: function (item) {
                        console.log(item)
                        $scope.addendTermName = item.termName
                        $scope.addendTermID = item.termID
                        return item.termName; //这里一定要return，否则选中不显示
                    },
                    afterSelect: function (item) {
                        //选择项之后的事件 ，item是当前选中的。
                        console.log(item)
                        $("#Termname2").blur()
                    },
                    minLength: 0
                });
                $('#Termname1').val("");
                $('#Termname2').val("");
                $('#myModaladd').modal('toggle');
            });
    }

});