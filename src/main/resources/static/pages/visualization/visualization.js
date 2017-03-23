var app = angular.module('myApp', [
    'ui.bootstrap'
]);

app.controller('menu', function ($scope, $http) {


    $http.get("http://" + ip + "/KG/Dashboard/getstatus?temp=" + new Date().getTime()).success(
        function (json) {
            console.log(json)
            $scope.ClassList = json;
        });
    $scope.getClass = function (name) {
        $scope.nowClassName = name;
        console.log('即将显示 ' + name + ' 的图谱')
        getclass(name)

    }
});