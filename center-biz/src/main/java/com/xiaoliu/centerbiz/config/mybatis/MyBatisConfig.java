package com.xiaoliu.centerbiz.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis基础配置
 *
 * @author xiaoliu
 */
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "center.datasource")
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Value( "${oc.datasource.jdbc_url}" )
    private String jdbcUrl;

    @Value( "${oc.datasource.username}" )
    private String username;

    @Value( "${oc.datasource.password}" )
    private String password;

    @Value( "${oc.datasource.driver_class_name}" )
    private String driverClassName;

    private DataSource dataSource;

    @Bean( name = "ocAssistDataSource" )
    public DataSource datasource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl( jdbcUrl );
        dataSource.setUsername( username );
        dataSource.setPassword( password );
        dataSource.setDriverClassName( driverClassName );
        this.dataSource = dataSource;
        return this.dataSource;
    }

    @Bean( name = "sqlSessionFactory" )
    @DependsOn( "ocAssistDataSource" )
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource( dataSource );
        bean.setTypeAliasesPackage( "com.yuouhui.oc.assist.domain" );

        //分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty( "supportMethodsArguments", "true" );
        properties.setProperty( "helperDialect", "mysql" );
        properties.setProperty( "params", "pageNum=pageable.pageNumber;pageSize=pageable.pageSize;" );
        pageInterceptor.setProperties( properties );

        //添加插件
        bean.setPlugins( new Interceptor[]{pageInterceptor} );

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations( resolver.getResources( "classpath:mapper/**/**.xml" ) );
            return bean.getObject();
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory ) {
        return new SqlSessionTemplate( sqlSessionFactory );
    }

    @Bean
    @DependsOn( "ocAssistDataSource" )
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager( dataSource );
    }
}
