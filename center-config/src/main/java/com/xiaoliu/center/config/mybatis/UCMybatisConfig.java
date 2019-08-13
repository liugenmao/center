package com.xiaoliu.center.config.mybatis;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootConfiguration
@MapperScan(basePackages = "com.xiaoliu.center.uc.dao", sqlSessionTemplateRef = "bizUCSqlSessionTemplate")
public class UCMybatisConfig {

    @Value("${center_uc.datasource.jdbc_url}")
    private String jdbcUrl;
    @Value("${center_uc.datasource.username}")
    private String username;
    @Value("${center_uc.datasource.password}")
    private String password;
    @Value("${center_uc.datasource.driver_class_name}")
    private String driverClassName;

    @Value("${center_uc.datasource.minPoolSize}")
    private int minPoolSize;
    @Value("${center_uc.datasource.maxPoolSize}")
    private int maxPoolSize;
    @Value("${center_uc.datasource.maxLifetime}")
    private int maxLifetime;
    @Value("${center_uc.datasource.borrowConnectionTimeout}")
    private int borrowConnectionTimeout;
    @Value("${center_uc.datasource.loginTimeout}")
    private int loginTimeout;
    @Value("${center_uc.datasource.maintenanceInterval}")
    private int maintenanceInterval;
    @Value("${center_uc.datasource.maxIdleTime}")
    private int maxIdleTime;
    @Value("${center_uc.datasource.testQuery}")
    private String testQuery;

    // 配置数据源
    @Bean(name = "bizUCDataSource")
    public DataSource testDataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(jdbcUrl);
        mysqlXaDataSource.setUser(username);
        mysqlXaDataSource.setPassword(password);
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        // 将本地事务注册到创 Atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("bizUCDataSource");

        xaDataSource.setMinPoolSize(minPoolSize);
        xaDataSource.setMaxPoolSize(maxPoolSize);
        xaDataSource.setMaxLifetime(maxLifetime);
        xaDataSource.setBorrowConnectionTimeout(borrowConnectionTimeout);
        xaDataSource.setLoginTimeout(loginTimeout);
        xaDataSource.setMaintenanceInterval(maintenanceInterval);
        xaDataSource.setMaxIdleTime(maxIdleTime);
        xaDataSource.setTestQuery(testQuery);
        return xaDataSource;
    }

    @Bean(name = "bizUCSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("bizUCDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "bizUCSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("bizUCSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
