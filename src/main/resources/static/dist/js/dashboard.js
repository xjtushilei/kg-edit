var app = angular.module('myApp', [
    'ui.bootstrap'
]);

app.controller('menu', function ($scope, $http) {


    $http.get("http://" + ip + "/KG/Dashboard/getstatus?temp=" + new Date().getTime()).success(
        function (json) {
            console.log(json)
            $scope.ClassStatusList = json;

        });
    $scope.addClassFun = function (addClassName, addClassID) {
        $.ajax({
            url: 'http://' + ip + '/KG/Dashboard/createClass',
            type: 'get',
            dataType: 'json',
            data: {ClassName: addClassName, ClassID: addClassID},
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

    $scope.updateClass = function (ClassName, ClassID) {
        $.ajax({
            url: 'http://' + ip + '/KG/Dashboard/updateClass',
            type: 'get',
            dataType: 'json',
            data: {ClassName: ClassName, ClassID: ClassID},
        })
            .done(function (data) {
                console.log("success");
                $http.get("http://" + ip + "/KG/Dashboard/getstatus?temp=" + new Date().getTime()).success(
                    function (json) {
                        console.log(json)
                        $scope.ClassStatusList = json;
                    });
            })
            .fail(function (data) {
                console.log("error");
                $http.get("http://" + ip + "/KG/Dashboard/getstatus?temp=" + new Date().getTime()).success(
                    function (json) {
                        console.log(json)
                        $scope.ClassStatusList = json;
                    });
            });

    }

});