var api_host = ip;

function getclass(ClassID, ClassName) {
    var dom = document.getElementById("kg");
    var myChart = echarts.init(dom);
    myChart.showLoading();
    var url = api_host + "/visualization/get?temp=" + new Date().getTime() + "&ClassID=" + ClassID;
    $.getJSON(url, function (json) {
        myChart.hideLoading();
        myChart.setOption(option = {
            title: {
                text: ClassName,
            },
            animationDurationUpdate: 100,
            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    // progressiveThreshold: 700,
                    data: json.nodes.map(function (node) {
                        return {

                            id: node.id,
                            name: node.label,
                            value: node.id,
                            symbolSize: node.size * 0.9 + 19,

                            itemStyle: {
                                normal: {
                                    color: node.color
                                }
                            }
                        };
                    }),
                    edges: json.edges.map(function (edge) {
                        return {
                            source: edge.sourceID,
                            target: edge.targetID
                        };
                    }),
                    force: {
                        initLayout: 'circular',
                        gravity: 1,
                        // repulsion: 20,
                        edgeLength: 120,
                        repulsion: 1300
                    },
                    label: {
                        //normal:{
                        //  if()
                        //}
                        emphasis: {
                            position: 'right',
                            show: true
                        }
                    },
                    // draggable: true,
                    roam: true,
                    focusNodeAdjacency: true,
                    lineStyle: {
                        normal: {
                            width: 0.6,
                            curveness: 0.3,
                            opacity: 0.7
                        }
                    }
                }
            ]
        }, true);
        myChart.on('click', function (node) {
            if (node.dataType == 'node') {
                //node.value is id
                getDependency(node.value, node.name)
            }

        });

    });
    ;
}

function getfacet(TermID, TermName) {
    var dom = document.getElementById("kg");
    var myChart = echarts.init(dom);

    var url = api_host + "/visualization/getfacet?temp=" + new Date().getTime() + "&TermID=" + TermID + "&TermName=" + TermName;

    $.getJSON(decodeURI(url), function (json) {

        myChart.hideLoading();
        myChart.setOption(option = {
            title: {
                text: TermName,
            },
            animationDurationUpdate: 150,


            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    // progressiveThreshold: 700,
                    data: json.nodes.map(function (node) {
                        return {
                            id: node.id,
                            name: node.label,
                            value: node.id,
                            symbolSize: (node.size + 1) * 3 + 9,
                            itemStyle: {
                                normal: {
                                    color: node.color
                                }
                            }
                        };
                    }),
                    edges: json.edges.map(function (edge) {
                        return {
                            source: edge.sourceID,
                            target: edge.targetID
                        };
                    }),
                    force: {
                        //initLayout: 'circular',
                        gravity: 1,

                        edgeLength: 90,
                        repulsion: 2000,
                    },
                    label: {
                        normal: {
                            position: 'right',
                            show: true,
                            textStyle: {
                                // color:"#000",
                                fontSize: 20,
                            }
                        }
                    },
                    //draggable: true,
                    roam: true,
                    // focusNodeAdjacency: true,
                    lineStyle: {
                        normal: {
                            width: 0.9,
                            curveness: 0.3,
                            opacity: 0.7,
                        }
                    }
                }
            ]
        }, true);

        myChart.on('click', function (node) {
            if (node.dataType == 'node') {
                if (node.value == TermID) {

                }
                else if (node.value != null) {

                    function getComputerWithAndHeigth() {
                        var width = window.screen.width; //获取屏幕宽
                        var height = window.screen.height; //获取屏幕高度
                        var thisWidth = getRandom(width); //随机位置x
                        var thisHeigth = getRandom(height); //随机位置y
                        document.getElementById('modal-data').style.left = thisWidth + 'px';
                        document.getElementById('modal-data').style.top = thisHeigth + 'px';
                    }

                    //生成0~max之间的随机数
                    function getRandom(max) {
                        var value = parseInt(Math.random() * (max + 1), 10);
                        return value;
                    }

                    var facet = api_host + "/visualization/getdetailed?temp=" + new Date().getTime() + "&TermID=" + TermID + "&FacetName=" + node.value;
                    $.ajax({
                        type: "get",
                        timeout: 10000,
                        dataType: "json",
                        error: function (errorMsg) {
                            alert("不好意思，数据请求失败啦!");
                        },
                        url: facet,
                        cache: false,
                        success: function (data) {
                            console.log(data);
                            //
                            $("#myModalLabel").text(node.name)
                            var photo = "";
                            var text = "";
                            if (data.text.length != 0) {
                                var color = ["sucess", "info", "warning", "danger"];
                                console.log(data);
                                for (var i = 0; i < data.text.length; i++) {
                                    text = text + '<div class="alert alert-' + color[getRandom(3)] + '">' + data.text[i].replace(/\n/g, '<br/>') + '</div>';
                                }
                            }
                            if (data.photo.length != 0) {

                                for (var i = 0; i < data.photo.length; i++) {

                                    photo = photo + '<div class="modal-data"><img style="width:50%" src=' + data.photo[i] + '></img></div>';

                                }
                            }

                            var htmlstr = text + photo;
                            $("#myModalContent").html(htmlstr);
                            if (htmlstr === "") $("#myModalContent").html("暂无内容");
                            $('#myModal').modal('toggle');
                        }
                    });
                }   //end  else if
            } //end  if判断nodetype
        })
    });

}


function getDependency(TermID, TermName) {

    var dom = document.getElementById("kg");
    var myChart = echarts.init(dom);

    var url = api_host + "/visualization/getDependency?temp=" + new Date().getTime() + "&TermID=" + TermID + "&TermName=" + TermName;

    $.getJSON(url, function (json) {

        myChart.hideLoading();
        myChart.setOption(option = {
            title: {
                text: TermName + " 的相关知识点",
            },
            animationDurationUpdate: 150,
            // animationEasingUpdate: 'quinticInOut',
            //animationEasingUpdate: 'linear',
            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    // progressiveThreshold: 700,
                    data: json.nodes.map(function (node) {
                        return {
                            id: node.id,
                            name: node.label,
                            value: node.id,
                            symbolSize: node.size * 3 + 19,
                            itemStyle: {
                                normal: {
                                    color: node.color
                                }
                            }
                        };
                    }),
                    edges: json.edges.map(function (edge) {
                        return {
                            source: edge.sourceID,
                            target: edge.targetID
                        };
                    }),
                    force: {
                        //initLayout: 'circular',
                        gravity: 1,

                        edgeLength: 150,
                        repulsion: 2000,
                    },
                    label: {
                        normal: {
                            position: 'right',
                            show: true,
                            textStyle: {
                                // color:"#000",
                                fontSize: 20,
                            }
                        }
                    },
                    //draggable: true,
                    roam: true,
                    // focusNodeAdjacency: true,
                    lineStyle: {
                        normal: {
                            width: 0.8,
                            curveness: 0.3,
                            opacity: 0.7,

                        }

                    }

                }
            ]
        }, true);

        myChart.on('click', function (node) {
            if (node.dataType == 'node') {
                if (node.value == TermID) {
                    getfacet(node.value, node.name);
                }
                else {
                    getDependency(node.value, node.name);
                }
            }
        });
    });

};