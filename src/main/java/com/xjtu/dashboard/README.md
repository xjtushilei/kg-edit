# 欢迎界面模块

- 从mooc2u的教师课程管理平台过来，携带一个CourseID过来，然后再本项目里，对该课程进行知识图谱的构建

# 基本介绍
- **domain**  或者叫做entity或javabean。定义了一些数据结构，其中@table的是数据库中的表。
- **repository** 持久化层，使用Spring-Data-JPA进行持久化操作（兼容主流数据库，需在gradle和maven中补充掉相应的jar包）。
- **service** 服务层，主要实现一些算法或者业务逻辑（由于部分代码逻辑简单，相应的实现在**web**层）
- **web** 控制层。主要就是RestController，定义rest接口，和外界（前台）打交道