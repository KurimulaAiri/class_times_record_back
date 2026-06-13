FROM eclipse-temurin:21-jre-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache curl

WORKDIR /app

# 核心修改：由于在本地编译，构建上下文在父工程根目录
# 我们直接将宿主机子模块 target 目录下的 Jar 包复制进来
COPY auth-service/target/auth-service-[0-9]*.jar app.jar

EXPOSE 10002

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:10002/actuator/health || exit 1

# JVM 内存优化参数:
# - 初始堆 128MB，最大堆 256MB（认证服务，轻量业务逻辑）
# - 启用虚拟线程（Java 21），大幅减少线程栈内存占用
# - ZGC 低延迟垃圾回收
# - 限制元空间和代码缓存大小
ENV JAVA_OPTS="-Xms128m -Xmx256m \
  -XX:+UseZGC \
  -Dspring.threads.virtual.enabled=true \
  -Djava.security.egd=file:/dev/./urandom \
  -XX:MaxMetaspaceSize=128m \
  -XX:ReservedCodeCacheSize=64m \
  -XX:+TieredCompilation \
  -XX:TieredStopAtLevel=1"

ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar