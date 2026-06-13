FROM eclipse-temurin:21-jre-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache curl

WORKDIR /app

# 从宿主机本地的 gateway/target 目录复制 Jar 包
COPY gateway/target/gateway-[0-9]*.jar app.jar

EXPOSE 9999

HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD curl -f http://localhost:9999/actuator/health || exit 1

# JVM 内存优化参数:
# - 初始堆 64MB，最大堆 128MB（网关仅做路由+JWT校验，无需大堆）
# - 启用虚拟线程（Java 21），大幅减少线程栈内存占用
# - G1GC 平衡吞吐与延迟，Alpine 兼容性最好
# - 限制元空间和代码缓存大小
ENV JAVA_OPTS="-Xms64m -Xmx128m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -Dspring.threads.virtual.enabled=true \
  -Djava.security.egd=file:/dev/./urandom \
  -XX:MaxMetaspaceSize=128m \
  -XX:ReservedCodeCacheSize=64m"

ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar