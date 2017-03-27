var app = angular.module('myApp', [
    'ui.bootstrap'
]);

app.controller('menu', function ($scope, $http) {


    $scope.getClass = function () {
        getclass(getCookie("nowclassid"), getCookie("nowclassname"))
    }
    $scope.getClass()
});