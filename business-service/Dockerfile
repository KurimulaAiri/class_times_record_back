FROM eclipse-temurin:21-jre-alpine

# 安装 curl 用于健康检查
RUN apk add --no-cache curl

WORKDIR /app

# 核心修改：从宿主机本地的 business-service/target 目录复制 Jar 包
COPY business-service/target/business-service-*.jar app.jar

EXPOSE 10001

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:10001/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]