FROM williamyeh/java8:latest
MAINTAINER wuhao<wuhao.cosmo@haier.com>
WORKDIR /work
COPY target/springframe-0.0.1-SNAPSHOT.jar ./springframe.jar
COPY src/main/resources/license/ ./resources/license/
ENV LANG=zh_CN.UTF-8 LANGUAGE=zh_CN:zh LC_ALL=zh_CN.UTF-8
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 启动命令
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8 -jar /work/springframe.jar" ]
CMD ["-Xmx512M","-Xms256M","-Xmn192M","-XX:MaxMetaspaceSize=192M","-XX:MetaspaceSize=192M"]


