package com.xiaoliu.centerbiz.config.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis扫描接口
 * 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
 *
 * @author xiaoliu
 */
@Configuration
@AutoConfigureAfter( MyBatisConfig.class )
public class MyBatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName( "sqlSessionFactory" );
        mapperScannerConfigurer.setBasePackage( "com.xiaoliu.centerbiz.dao" );
        return mapperScannerConfigurer;
    }

}