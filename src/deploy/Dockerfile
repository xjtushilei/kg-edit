# knowledge graph;
# docker build -t 10.100.135.125:5000/knowledgegraph:20170420-01 .

# tomcat版本8; jdk版本8;
FROM 10.100.135.125:5000/sys_tomcat:8_jdk8
# ddd

# 固定不变的; 清除原 tomcat 的 webapps 目录中的数据;
#RUN rm -fr /data/server/tomcat/default/webapps/*

# 可选; 更改系统的时区和时间为 shanghai;
RUN cp -f /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone



# 可选; 处理中文编码;
RUN yum install -y kde-l10n-Chinese
#RUN yum reinstall -y glibc-common
RUN yum install -y net-tools
ENV LANG "zh_CN.UTF-8"
ENV TERM linux



# 可选; 安装 scp 命令;
RUN yum install -y openssh-clients



#
RUN mkdir /data/moocknowledgegraph




# 可选; 如果使用了 disconf 管理配置文件, 需要添加以下3行代码;
#ENV DISCONF_ENV pre     # 配置文件 的使用环境,3选一:local; pre; ops;
#ENV DISCONF_DEFAULT_VERSION 1_0_0_0     # 配置文件 的版本号, 根据实际情况变更;
#ENV DISCONF_CONF_SERVER_HOST http://disconf-openpre.myalauda.cn   # 本行基本不变;



# 可选; 如果catalina.sh 文件更改了, 需要将该文件放在当前目录中, 并添加如下2行代码;
#COPY ./catalina.sh /data/server/tomcat/default/bin/
#RUN chmod +x /data/server/tomcat/default/bin/catalina.sh



# 必须; 对外暴露80端口;
EXPOSE 80



# 必须; 将当前目录中的 war 包, 复制到 tomcat 的 webapps 目录中;
ADD ./ROOT.war /data/server/tomcat/default/webapps/



# 必须; 本 Docker 镜像的启动命令;
ENTRYPOINT /data/server/tomcat/default/bin/catalina.sh start && /usr/sbin/sshd -D
