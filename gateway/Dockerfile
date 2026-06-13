FROM eclipse-temurin:21-jre-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache curl

WORKDIR /app

# 从宿主机本地的 gateway/target 目录复制 Jar 包
COPY gateway/target/gateway-[0-9]*.jar app.jar

EXPOSE 9999

HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD curl -f http://localhost:9999/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]