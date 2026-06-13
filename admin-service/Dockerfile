FROM eclipse-temurin:21-jre-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache curl

WORKDIR /app

# 核心修改：由于在本地编译，构建上下文在父工程根目录
# 我们直接将宿主机子模块 target 目录下的 Jar 包复制进来
COPY admin-service/target/admin-service-[0-9]*.jar app.jar

EXPOSE 10003

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:10003/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
