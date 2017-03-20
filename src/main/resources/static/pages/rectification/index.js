// 初始化 编辑器
// var editor
// $(function () {
//     //bootstrap WYSIHTML5 - text editor
//     editor = new wysihtml5.Editor("textarea", { });

// });


var app = angular.module('myApp', [
    'ui.bootstrap'
]);
//ng-repeat结束执行指令
app.directive('repeatFinish', function () {
    return {
        link: function (scope, element, attr) {
            // console.table(scope.x)
            // scope.x.FragmentContent=scope.x.FragmentContent.replace(/\n/g,'<br/>');

            // console.log(element)
            // console.log(attr)
            if (scope.$last == true) {
                // console.log('ng-repeat执行完毕')
                scope.$eval(attr.repeatFinish)
            }
        }
    }
})
app.controller('menu', function ($scope, $http) {

    var termsDOM = $('#terms');
    var checktermsDOM = $('#checkterms');
    var fragmentDOM = $('#fragmentDIV');
    termsDOM.hide()
    checktermsDOM.hide()
    fragmentDOM.hide()


    $http.get("http://" + ip + "/KG/Rectification/getClassState?temp=" + new Date().getTime()).success(
        function (json) {
            // console.log(json)
            $scope.ClassList = json;
        });
    $scope.getClassList = function (className) {
        $http.get("http://" + ip + "/KG/Rectification/getClassTerm?temp=" + new Date().getTime() + "&className=" + className).success(
            function (json) {
                $scope.classTermList = json;
                $scope.nowTerm = "无选中任何知识点";
                termsDOM.show()
                termsDOM.slimscroll({
                    height: '550px'
                });
            });
    }
    $scope.getClassFacetList = function (termName, className) {
        $http.get("http://" + ip + "/KG/Rectification/getTermFacet?temp=" + new Date().getTime() + "&termName=" + termName + "&className=" + className).success(
            function (json) {
                checktermsDOM.show()
                fragmentDOM.hide()
                $scope.nowTerm = termName;
                $scope.facets = json
            });
    }
    $scope.getFacetFragmentList = function (className, termName, facetName) {
        $http.get("http://" + ip + "/KG/Rectification/getFacetFragment?temp=" + new Date().getTime() + "&className=" + className + "&termName=" + termName + "&facetName=" + facetName).success(
            function (json) {
                fragmentDOM.show()
                $scope.nowFacet = facetName
                $scope.textOfFacetFragment = json.text
                $scope.imageOfFacetFragment = json.image

            });
    }

    $scope.renderFinish = function () {
        // console.log('渲染完之后的操作')
        $(".textclass").slimscroll({
            height: '100px',
            size: '4px',
            alwaysVisible: true,
            railOpacity: 0.1
        });
    }

    $scope.changeText = function (className, termName, facet, fragmentID, fragmentContent) {
        console.log(className)
        console.log(termName)
        console.log(fragmentID)

        $('#textarea').val(fragmentContent)

        $scope.nowfragmentID = fragmentID
        $('#myModalLabel').text(facet)
        $('#myModal').modal('toggle');

    }

    $scope.updateText = function (className, termName, fragmentID) {
        // console.log($('#textarea').val())
        $.ajax({
            url: "http://" + ip + "/KG/Rectification/updateText",
            type: 'POST',
            data: {
                className: className,
                termName: termName,
                fragmentID: fragmentID,
                fragmentContent: $('#textarea').val()
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json"
        })
            .done(function (json) {
                console.log(json.success);
                $scope.getFacetFragmentList(className, termName, $scope.nowFacet)

            })
    }

    $scope.deleteText = function (className, termName, fragmentID) {
        // console.log($('#textarea').val())
        $.ajax({
            url: "http://" + ip + "/KG/Rectification/deleteText",
            type: 'get',
            data: {
                className: className,
                termName: termName,
                fragmentID: fragmentID
            },
            dataType: "json"
        })
            .done(function (json) {
                console.log(json.success);
                $scope.getFacetFragmentList(className, termName, $scope.nowFacet)
            })
    }
    $scope.lookImage = function (ImageAPI, ImageID) {
        $scope.nowImageID = ImageID
        $('#myModalImageContent').html('<img  style="max-height: 500px;width: auto;" src="' + ImageAPI + '"  alt="' + ImageID + '">')
        $('#myModalImage').modal('toggle');
    }

    $scope.deleteImage = function (className, termName, imageID) {
        // console.log($('#textarea').val())
        $.ajax({
            url: "http://" + ip + "/KG/Rectification/deleteImage",
            type: 'get',
            data: {
                className: className,
                termName: termName,
                imageID: imageID
            },
            dataType: "json"
        })
            .done(function (json) {
                console.log(json.success);
                $scope.getFacetFragmentList(className, termName, $scope.nowFacet)
            })
    }


    $scope.submitClass = function (className) {
        console.log(className)
        if (className === "" || className == null || className == undefined) {
            $('#myModalInfoLabel').html("请先选择课程！")
            $('#myModalInfo').modal('toggle')
            return
        }
        // console.log($('#textarea').val())
        $.ajax({
            url: "http://" + ip + "/KG/Rectification/changeClassStatus",
            type: 'get',
            data: {
                className: className
            },
            dataType: "json"
        })
            .done(function (json) {
                console.log(json.success);
                $('#myModalInfoLabel').html("提交成功！")
                $('#myModalInfo').modal('toggle');

            })
    }
});




