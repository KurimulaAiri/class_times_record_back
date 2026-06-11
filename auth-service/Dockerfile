FROM eclipse-temurin:21-jre-alpine

# 安装 curl 用于健康检查
RUN apk add --no-cache curl

WORKDIR /app

# 核心修改：由于在本地编译，构建上下文在父工程根目录
# 我们直接将宿主机子模块 target 目录下的 Jar 包复制进来
COPY auth-service/target/auth-service-*.jar app.jar

EXPOSE 10002

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:10002/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]