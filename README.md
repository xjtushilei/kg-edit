# knowledgegraph 工程简介

知识图谱的后台管理系统，同时提供给奥鹏mooc2u平台一些api进行可视化展示图谱数据



# 与mooc2u的对接说明

本系统相当于一个工具。主要有三个步骤。

1. 从mooc2u的教师课程管理平台过来，只携带一个CourseID过来，当携带CourseID进来`knowledgegraph`系统的时候，把该课程写入到 `knowledgegraph`系统的表里，相当于表示我们的系统里有该门课的记录，并在前端记录cookie。
    - 该过程在前端进行了优化，如果存在cookie，则肯定我们系统里有该门课记录，则不进行上面的请求。
    - 该过程是针对一门课程的，每次从其他平台过来进行构建的时候，只构建一门课，因为一般一个老师只有一门课。需要切换的时候，重新从之前的系统里进来该系统就可以。
    - **例如：** 
        - 测试课程：http://p-knowledgegraph-openops.myalauda.cn/?CourseID=a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5
        - 光电子学：http://p-knowledgegraph-openops.myalauda.cn/?CourseID=14db49f9-69a3-4b34-8316-1d942f238dd9
        - 计算机实用技术：http://p-knowledgegraph-openops.myalauda.cn/?CourseID=3227b430-4b96-439f-babe-3ab6bedb4b2b
1. 在该系统里进行对该课程的知识谱图的构建
1. 构建结束，mooc2u使用 `http://host/visualization/*`一系列接口，进行前端的知识图谱的展示
    - 接口说明 在`http://p-knowledgegraph-openops.myalauda.cn/swagger-ui.html` ，其中域名若有修改请使用最新的。






## 其他
目前项目主要在维护gradle，若有jar包修改，请修改该配置文件（奥鹏服务器使用gradle进行自动化构建）。


maven的pom文件已经停止了维护，请忽略。

windows下部署的话，phantomjs.exe需要人工添加，github不支持上传exe。linux下使用不影响。