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

    var chapterDOM = $('#chapter');
    var inputtermsDOM = $('#inputterms');

    inputtermsDOM.hide()
    // var ClassName='测试课程';
    $scope.isCollapsed = true;
    $scope.isCollapsedchildren = true;

    $scope.getClassChapter = function (CourseID) {
        searchTerm();
        console.log(CourseID)
        // CourseID='e63f581c-3eef-4fb9-a120-f57940ef9609'
        $http.get(ip + "/datainput/getChapterListByClassID?temp=" + "&CourseID=" + CourseID).success(
            function (json) {

                $scope.chapter = json;
                chapterDOM.show()
                inputtermsDOM.show()
                chapterDOM.slimscroll({
                    height: '550px'
                });
                console.log("列表加载成功")
            });

    }
    $scope.getClassChapter(getCookie("nowclassid"))

    $scope.setNULL = function () {
        $scope.Termname = "";
    }
    $scope.getdetail = function (x, y, z) {
        $scope.Termname = ""
        parentChapterName = x.parentChapterName;
        parentChapterID = x.parentChapterID;
        chapterID = y.chapterID;
        chapterName = y.chapterName;
        childrenChapterID = z.childrenChapterID;
        childrenChapterName = z.childrenChapterName;

        // console.log(parentChapterName)
        var nowchapter = parentChapterName + ' -> ' + chapterName + ' -> ' + childrenChapterName;
        $('#nowchapter').text(nowchapter)
        searchTerm();
    }
    $scope.addterm = function (TermName, Note) {
        console.log(getCookie("nowclassname"))
        if ($('#nowchapter').text() == '无选中任何章节') {
            $('#status').text('无选中任何章节');
            return
        }

        if ($('#nowchapter').text() == '无选中任何章节' || TermName == null || TermName == undefined || TermName == '') {
            $('#status').text('知识点不准为空');
            return
        }
        $.ajax({
            url: ip + '/datainput/writeKnowledge',
            type: 'get',
            dataType: 'json',
            async: false,
            data: {
                temp: new Date().getTime(),
                ClassID: getCookie("nowclassid"),
                ClassName: getCookie("nowclassname"),
                TermName: TermName,
                ParentChapterID: parentChapterID,
                ParentChapterName: parentChapterName,
                ChapterID: chapterID,
                ChapterName: chapterName,
                ChildrenChapterID: childrenChapterID,
                ChildrenChapterName: childrenChapterName,
                Note: Note
            },
        })
            .done(function (data) {
                console.log("addterm success");
                $('#status').text("录入成功！");

            })
            .fail(function (data) {
                console.log("addterm error");
                for (key in data) $('#status').text(data[key]);
            })
            .always(function () {
                console.log("开始展示知识点列表");
                searchTerm();
            });
    }
    $scope.deleteTerm = function (x) {
        console.log(x.termName + '将要被删除')
        $.ajax({
            url: ip + '/datainput/deleteReletionByRelationID',
            type: 'get',
            async: true,
            dataType: 'json',
            data: {
                relationID: x.relationID
            },
        })
            .done(function (data) {
                var error = 'success'
                console.log("deleteTerm success");
                for (key in data) success = data[key];
                console.log("deleteTerm success:" + success);

            })
            .fail(function (data) {
                var error = ''
                for (key in data) error = data[key];
                console.log("deleteTerm error:" + error);
                alert("删除失败:" + error)
            });
    }

    function searchTerm() {
        $.ajax({
            url: ip + '/datainput/searchTerms',
            type: 'get',
            async: false,
            dataType: 'json',
            data: {
                ParentChapterID: parentChapterID,
                ParentChapterName: parentChapterName,
                ChapterID: chapterID,
                ChapterName: chapterName,
                ChildrenChapterID: childrenChapterID,
                ChildrenChapterName: childrenChapterName
            },
        })
            .done(function (data) {
                console.log("searchTerm success" + data.length);
                $scope.terms = data;
                console.log(data)

            })
            .fail(function (data) {
                var error = ''
                for (key in data) error = data[key];
                console.log("searchTerm error:" + error);
            });
    }
});