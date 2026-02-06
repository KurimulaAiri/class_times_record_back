package com.shiroko.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Description: MyBatis-Plus 核心配置类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午12:58
 */
@Configuration
public class MyBatisPlusConfig {

    // ====================== 1. 分页插件（必配，否则分页无效） ======================
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件（适配 MySQL，其他数据库需修改 DbType）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件（可选，需实体类加 @Version 注解）
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    // ====================== 2. 字段自动填充（可选，如创建时间/更新时间） ======================
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            // 插入时自动填充
            @Override
            public void insertFill(MetaObject metaObject) {
                // 填充 createTime 字段（实体类需加 @TableField(fill = FieldFill.INSERT)）
                strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                // 填充 updateTime 字段（实体类需加 @TableField(fill = FieldFill.INSERT_UPDATE)）
                strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }

            // 更新时自动填充
            @Override
            public void updateFill(MetaObject metaObject) {
                // 填充 updateTime 字段
                strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}