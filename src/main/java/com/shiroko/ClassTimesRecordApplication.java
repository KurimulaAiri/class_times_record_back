package com.shiroko;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.shiroko.mapper")
public class ClassTimesRecordApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClassTimesRecordApplication.class, args);
    }
}
