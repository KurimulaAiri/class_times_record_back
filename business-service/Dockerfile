FROM eclipse-temurin:21-jre-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache curl

WORKDIR /app

# 核心修改：从宿主机本地的 business-service/target 目录复制 Jar 包
COPY business-service/target/business-service-[0-9]*.jar app.jar

EXPOSE 10001

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:10001/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]