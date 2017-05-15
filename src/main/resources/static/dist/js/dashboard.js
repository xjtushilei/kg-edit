var message = {Course: ''}
app = new Vue({
    el: ".content",
    data: message
});
nowclassid = getCookie('nowclassid')
//cookie中不存在记录
if (nowclassid == "") {

    nowclassid = getUrlParam('CourseID')
    console.log(nowclassid)
    //判断有无calssid，没有则无法展开后面的行动
    if (nowclassid == '') {
        $("#myModal1Label").text("提示")
        $("#myModal1Content").html('</br> 由于无法获得您想要操作的课程</br></br> <b>请您重新从教师管理系统里进入该知识图谱构建系统！</b>');
        $('#myModal1').modal('show');
    }
    //判断本地有没有classname，没有的话，去后台调取，并写cookie
    nowclassname = getCookie('nowclassname')
    if (nowclassname == '') {
        $.ajax({
            url: ip + '/Dashboard/RecordCourse',
            type: 'get',
            dataType: 'json',
            data: {classID: nowclassid},
        })
            .done(function (data) {

                nowclassname = data.classname
                setCookie("nowclassid", data.classid, "d9999")
                setCookie("nowclassname", data.classname, "d9999")
                // $("#nowclassname").text(nowclassname)
                message.Course = getCookie('nowclassname')
            })
            .fail(function (data) {
                for (key in data) console.log(data[key]);
            })
    }
    else {
        message.Course = getCookie('nowclassname')
        // $("#nowclassname").text(getCookie('nowclassname'))
    }
}
//cookie中存在记录
else {

    url_nowclassid = getUrlParam('CourseID')
    //检查cookie和现在的一样吗，不一样的话
    if (url_nowclassid != nowclassid) {

        //不一样，但是url里id是空的
        if (url_nowclassid == '') {
            message.Course = getCookie('nowclassname')
        }
        //不一样，以url里的为新的
        else {
            nowclassid = url_nowclassid

            //判断本地有没有classname，没有的话，去后台调取，并写cookie
            $.ajax({
                url: ip + '/Dashboard/RecordCourse',
                type: 'get',
                dataType: 'json',
                data: {classID: nowclassid},
            })
                .done(function (data) {
                    nowclassname = data.classname
                    setCookie("nowclassid", data.classid, "d9999")
                    setCookie("nowclassname", data.classname, "d9999")
                    // $("#nowclassname").text(nowclassname)
                    message.Course = getCookie('nowclassname')
                })
                .fail(function (data) {
                    for (key in data) console.log(data[key]);
                })
        }
    }
    else {
        // 一样的话直接显示cookie里的
        message.Course = getCookie('nowclassname')
    }
}