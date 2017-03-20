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
        // singleSelect: false,
        // data-locale:"zh-US" , //表格汉化
        search: true, //显示搜索框

        columns: [
            {
                title: '课程名字',
                field: 'ClassName',
                align: 'center',
                valign: 'middle'
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
            }
            ,
            {
                title: '置信度',
                field: 'confidence',
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
                        '<a class="remove ml10"   href="javascript:void(0)" title="Remove">',
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
                    $http.get("http://" + ip + "/KG/dependencyModify/delete?temp=" + new Date().getTime() + "&startTermID=" + row.startTermID + "&className=" + row.ClassName + "&endTermID=" + row.endTermID).success(
                        function (json) {
                            console.log("删除" + row.startTermName + "-" + row.endTermName + "成功")
                            $scope.getClass(row.ClassName);
                        });
                }
            }
            }
        ]
    });

    $http.get("http://" + ip + "/KG/dependencyModify/getClassState?temp=" + new Date().getTime()).success(
        function (json) {
            console.log(json)
            $scope.ClassList = json;
        });

    $scope.getClass = function (ClassName) {

        $http.get("http://" + ip + "/KG/dependencyModify/get?temp=" + new Date().getTime() + "&className=" + ClassName).success(
            function (json) {
                console.log(json)
                $scope.dependencyList = json
                if (json.length != 0) {
                    table.bootstrapTable("load", json)
                    $('.box-footer').text('')
                    tableDOM.show()
                    countDOM.show()
                }
                else {
                    table.bootstrapTable("load", [])
                    $('.box-footer').text('无数据')
                    countDOM.show()
                    tableDOM.hide()
                }
            }
        );
    }

    $scope.startAlgorithm = function (ClassName) {
        console.log(ClassName + "  开始算法分析")
        $("#myModalLabel").text(ClassName)
        $("#myModalContent").html('</br> <span class="badge" style="background-color:green;font-size: 18px">正在分析，请等待.......</span>  </br> <span class="badge" style="background-color:red;font-size: 18px">时间大约30秒到3分钟.......</span> </br> <span class="badge" style="background-color:red;font-size: 18px">10分钟后如果还没有结果，请重新点击开始算法分析.......</span>');
        $('#myModal').modal('toggle');
        $http.get("http://" + ip + "/KG/dependencyModify/startAlgorithm?temp=" + new Date().getTime() + "&className=" + ClassName).success(
            function (data) {
                $("#myModalLabel").text(ClassName)
                $("#myModalContent").html('</br> <span class="badge" style="background-color:red;font-size: 18px">分析结束.......</span>');
                $('#myModal').modal('show');
                $scope.getClass(ClassName)
            });
    }

    $scope.submitClass = function (ClassName) {
        console.log(ClassName)
        $http.get("http://" + ip + "/KG/dependencyModify/changeSystemStatus?temp=" + new Date().getTime() + "&className=" + ClassName).success(
            function (data) {
                $("#myModalLabel").text(ClassName)
                $("#myModalContent").html(data.success + "</br> 请进行下一步.......");
                $('#myModal').modal('toggle');
            });
    }

});