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
@MapperScan(basePackages = "com.xiaoliu.center.sc.dao", sqlSessionTemplateRef = "bizSCSqlSessionTemplate")
public class SCMybatisConfig {

    @Value("${center_sc.datasource.jdbc_url}")
    private String jdbcUrl;
    @Value("${center_sc.datasource.username}")
    private String username;
    @Value("${center_sc.datasource.password}")
    private String password;
    @Value("${center_sc.datasource.driver_class_name}")
    private String driverClassName;

    @Value("${center_sc.datasource.minPoolSize}")
    private int minPoolSize;
    @Value("${center_sc.datasource.maxPoolSize}")
    private int maxPoolSize;
    @Value("${center_sc.datasource.maxLifetime}")
    private int maxLifetime;
    @Value("${center_sc.datasource.borrowConnectionTimeout}")
    private int borrowConnectionTimeout;
    @Value("${center_sc.datasource.loginTimeout}")
    private int loginTimeout;
    @Value("${center_sc.datasource.maintenanceInterval}")
    private int maintenanceInterval;
    @Value("${center_sc.datasource.maxIdleTime}")
    private int maxIdleTime;
    @Value("${center_sc.datasource.testQuery}")
    private String testQuery;

    // 配置数据源
    @Bean(name = "bizSCDataSource")
    public DataSource dataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(jdbcUrl);
        mysqlXaDataSource.setUser(username);
        mysqlXaDataSource.setPassword(password);
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        // 将本地事务注册到创 Atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("bizSCDataSource");

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

    @Bean(name = "bizSCSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("bizSCDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "bizSCSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("bizSCSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
