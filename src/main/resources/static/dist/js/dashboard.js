var app = angular.module('myApp', [
    'ui.bootstrap'
]);

app.controller('menu', function ($scope, $http) {


    $http.get(ip + "/Dashboard/getstatus?temp=" + new Date().getTime()).success(
        function (json) {
            console.log(json)
            $scope.ClassList = json;

        });


    $scope.setClassCookie = function (nowclassid, classname) {
        setCookie("nowclassid", nowclassid, "d999")
        setCookie("nowclassname", classname, "d999")
        $("#myModal1Label").text("提示")
        $("#myModal1Content").html('</br> 您已经选择<span class="badge" style="background-color:green;font-size: 18px">' + classname + '</span>  作为您之后的操作课程！</br></br>请开始 <b>录入知识点</b>');
        $('#myModal1').modal('show');
    }
    $scope.addClassFun = function (addClassName, addClassID) {
        $.ajax({
            url: ip + '/Dashboard/createClass',
            type: 'get',
            dataType: 'json',
            data: {
                className: addClassName,
                classID: addClassID
            },
        })
            .done(function (data) {
                console.log("success");
                for (key in data) $('#status').text(data[key]);
            })
            .fail(function (data) {
                console.log("error");
                for (key in data) $('#status').text(data[key]);
            });

    }


});