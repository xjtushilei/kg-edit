# 知识点录入模块

- 从`com.xjtu.common.domain.Config.MOOC2U_API_GET_COURSE`（一个奥鹏提供的api，可以获得到相应的章节的信息）中获取章节信息进行前台展示。该功能可能需要修改，因为api一点修改，这里需要进行json解析的修改。json解析使用的JsonPath
- 将对应章节有的知识点录入进去。

# 基本介绍
- **domain**  或者叫做entity或javabean。定义了一些数据结构，其中@table的是数据库中的表。
- **repository** 持久化层，使用Spring-Data-JPA进行持久化操作（兼容主流数据库，需在gradle和maven中补充掉相应的jar包）。
- **service** 服务层，主要实现一些算法或者业务逻辑（由于部分代码逻辑简单，相应的实现在**web**层）
- **web** 控制层。主要就是RestController，定义rest接口，和外界（前台）打交道