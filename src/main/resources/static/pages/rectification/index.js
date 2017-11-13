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

    $scope.urlBase = ip;
    var termsDOM = $('#terms');
    var checktermsDOM = $('#checkterms');
    var fragmentDOM = $('#fragmentDIV');
    termsDOM.hide()
    checktermsDOM.hide()
    fragmentDOM.hide()


    $scope.getSpiderStatus = function () {

        $.ajax({
            url: ip + "/spider/getClassStatus?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid"),
            // url:"a.json",
            type: 'get',
            dataType: 'json',
        })
            .done(function (json) {
                // console.log(json)
                console.log("加载课程列表 success");

                ///计算两个整数的百分比值
                function GetPercent(num, total) {
                    num = parseFloat(num);
                    total = parseFloat(total);
                    if (isNaN(num) || isNaN(total)) {
                        return "-";
                    }
                    return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%");
                }

                row = json[0];
                nowPercent = GetPercent(row.alreadyTermNum, row.termSum);
                console.log(nowPercent)
                if (nowPercent != "100%") {
                    $('#myModalInfoLabel').html("目前数据抓取仅完成<span style='color:blue'>" + nowPercent + "</span> <br>" +
                        "请在 <span style='color:blue'>数据抓取</span> 页面点击 <span style='color:blue'>开始爬取</span><br>" +
                        "如果确定不能爬取，请在该页面人工增加数据！")
                    $('#myModalInfo').modal('toggle')
                }

            })
            .fail(function () {
                console.log("error");
            })
            .always(function (json) {
                console.log("complete");
            });
    }
    $scope.getSpiderStatus();

    $scope.getClassList = function () {
        $http.get(ip + "/rectification/getClassTerm?temp=" + new Date().getTime() + "&ClassID=" + getCookie("nowclassid")).success(
            function (json) {
                $scope.classTermList = json;
                $scope.nowTerm = "无选中任何知识点";
                termsDOM.show()
                termsDOM.slimscroll({
                    height: '550px'
                });
            });
    }
    $scope.getClassList();
    $scope.getClassFacetList = function (term) {
        $http.get(ip + "/rectification/getTermFacet?temp=" + new Date().getTime() + "&TermID=" + term.termID).success(
            function (json) {
                console.log(json)
                checktermsDOM.show()
                fragmentDOM.hide()
                $scope.nowTerm = term;
                $scope.facets = json
            });
    }
    $scope.getFacetFragmentList = function (nowFacet) {
        $http.get(ip + "/rectification/getFacetFragment?temp=" + new Date().getTime() + "&TermID=" + $scope.nowTerm.termID + "&FacetName=" + nowFacet).success(
            function (json) {
                fragmentDOM.show()
                $scope.nowFacet = nowFacet
                $scope.textOfFacetFragment = json.text
                $scope.imageOfFacetFragment = json.image

            });
    }

    $scope.renderFinish = function () {
        // console.log('渲染完之后的操作')
        $(".textclass").slimscroll({
            height: '150px',
            size: '4px',
            alwaysVisible: true,
            railOpacity: 0.1
        });
    }

    $scope.changeText = function (fragmentID, fragmentContent) {

        $('#textarea').val(fragmentContent)

        $scope.nowfragmentID = fragmentID
        $('#myModal1Label').text($scope.nowFacet)
        $('#myModal1').modal('toggle');

    }

    $scope.updateText = function () {
        // console.log($('#textarea').val())
        $.ajax({
            url: ip + "/rectification/updateText",
            type: 'POST',
            data: {
                TermID: $scope.nowTerm.termID,
                FragmentID: $scope.nowfragmentID,
                FragmentContent: $('#textarea').val()
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json"
        })
            .done(function (json) {
                console.log(json.success);
                $scope.getFacetFragmentList($scope.nowFacet)

            })
    }

    $scope.deleteText = function (fragmentID) {
        // console.log($('#textarea').val())
        if (confirm('确定要执行此操作吗?')) {
            $.ajax({
                url: ip + "/rectification/deleteText",
                type: 'get',
                data: {
                    FragmentID: fragmentID,
                    TermID: $scope.nowTerm.termID
                },
                dataType: "json"
            })
                .done(function (json) {
                    console.log(json.success);
                    $scope.getFacetFragmentList($scope.nowFacet)
                })
        }
    }
    $scope.lookImage = function (ImageAPI, ImageID) {
        $scope.nowImageID = ImageID
        $('#myModalImageContent').html('<img  style="max-height: 500px;width: auto;" src="' + ImageAPI + '"  alt="' + ImageID + '">')
        $('#myModalImage').modal('toggle');
    }

    $scope.deleteImage = function (imageID) {
        if (confirm('确定要执行此操作吗?')) {
            // console.log($('#textarea').val())
            $.ajax({
                url: ip + "/rectification/deleteImage",
                type: 'get',
                data: {
                    TermID: $scope.nowTerm.termID,
                    ImageID: imageID
                },
                dataType: "json"
            })
                .done(function (json) {
                    console.log(json.success);
                    $scope.getFacetFragmentList($scope.nowFacet)
                })
        }

    }


    $scope.addfenmianfun = function (className) {
        console.log(className)
        console.log($scope.addfenmian)

        if ($scope.nowTerm == undefined) {
            $('#myModalInfoLabel').html("请先输入分面属性名字！")
            $('#myModalInfo').modal('toggle')
            return
        }
        if ($scope.addfenmian == undefined) {
            $('#myModalInfoLabel').html("请先输入分面属性名字！")
            $('#myModalInfo').modal('toggle')
            return
        }
        $.ajax({
            url: ip + "/rectification/addFacet",
            type: 'get',
            data: {
                TermID: $scope.nowTerm.termID,
                FacetName: $scope.addfenmian
            },
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded'
        })
            .done(function (data) {
                $('#myModalInfoLabel').html("增加成功！")
                $('#myModalInfo').modal('toggle');
                $scope.getClassFacetList($scope.nowTerm)
            })
            .fail(function (data) {
                var heretemp = ''
                console.log(data)
                for (key in data.responseJSON) heretemp = data.responseJSON[key];
                console.log(heretemp)
                $('#myModalInfoLabel').html("</br>" + heretemp)
                $('#myModalInfo').modal('toggle');
                $scope.getClassFacetList($scope.nowTerm)
            })
    }
    $scope.addtextfun = function () {
        $.ajax({
            url: ip + "/rectification/addText",
            type: 'post',
            data: {
                TermID: $scope.nowTerm.termID,
                TermName: $scope.nowTerm.termName,
                FacetName: $scope.nowFacet,
                FragmentContent: $('#textareaAddText').val()
            },
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded'
        })
            .done(function (data) {
                $('#myModalInfoLabel').html("增加成功！")
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.nowFacet)

            })
            .fail(function (data) {
                var heretemp = ''
                // console.log(data)
                for (key in data.responseJSON) heretemp = data.responseJSON[key];
                $('#myModalInfoLabel').html("</br>" + heretemp)
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.nowFacet)
            })
    }
    $scope.addphotofun = function () {
        $("#uploadPhoto input[name='TermID']").val($scope.nowTerm.termID)
        $("#uploadPhoto input[name='TermName']").val($scope.nowTerm.termName)
        $("#uploadPhoto input[name='FacetName']").val($scope.nowFacet)
        // console.log($("#uploadPhoto input[name='facetName']").val())
        var hideForm = $('#uploadPhoto');
        hideForm.attr("action", ip + "/rectification/addImage")
        var options = {
            dataType: "json",
            beforeSubmit: function () {
                console.log("正在上传");

            },
            success: function (result) {
                console.log('成功上传！');
                var heretemp = ''
                // console.log(data)
                for (key in result) heretemp = result[key];
                $('#myModalInfoLabel').html("成功上传！" + "</br>" + heretemp)
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.nowFacet)
            },
            error: function (result) {
                console.log(result)
                console.log('上传失败！');
                $('#myModalInfoLabel').html("上传失败！请重试" + "</br>")
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.nowFacet)
            }
        };
        hideForm.ajaxSubmit(options);
    }
    $scope.updateFacetfun = function () {
        $.ajax({
            url: ip + "/rectification/updateFacet",
            type: 'get',
            data: {
                TermID: $scope.nowTerm.termID,
                OldFacetName: $scope.nowFacet,
                NewFacetName: $scope.updatefenmian,
            },
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded'
        })
            .done(function (data) {
                $('#myModalInfoLabel').html("修改分面成功！")
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.updatefenmian)
                $scope.getClassFacetList($scope.nowTerm)
            })
            .fail(function (data) {
                var heretemp = ''
                // console.log(data)
                for (key in data.responseJSON) heretemp = data.responseJSON[key];
                $('#myModalInfoLabel').html("</br>" + heretemp)
                $('#myModalInfo').modal('toggle');
                $scope.getFacetFragmentList($scope.updatefenmian)
                $scope.getClassFacetList($scope.nowTerm)
            })
    }
    $scope.deleteFacetfun = function (x) {
        if (confirm('确定要执行此操作吗?')) {
            $.ajax({
                url: ip + "/rectification/deleteFacet",
                type: 'get',
                data: {
                    TermID: $scope.nowTerm.termID,
                    FacetName: x
                },
                dataType: "json",
                contentType: 'application/x-www-form-urlencoded'
            })
                .done(function (data) {
                    $('#myModalInfoLabel').html("删除分面成功！")
                    $('#myModalInfo').modal('toggle');
                    $scope.getClassFacetList($scope.nowTerm)
                })
                .fail(function (data) {
                    var heretemp = ''
                    // console.log(data)
                    for (key in data.responseJSON) heretemp = data.responseJSON[key];
                    $('#myModalInfoLabel').html("</br>" + heretemp)
                    $('#myModalInfo').modal('toggle');
                    $scope.getClassFacetList($scope.nowTerm)
                })
        }

    }


});