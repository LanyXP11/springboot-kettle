package com.lx.kettle.core.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Created by chenjiang on 2019/10/21
 * <p>
 *      druidDataSource 数据源配置
 * </p>
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@SuppressWarnings("all")
public class DataSourceConfig {

    @Value("${datasource.exam.url}")
    private String examUrl;
    @Value("${datasource.exam.username}")
    private String examUserName;
    @Value("${datasource.exam.password}")
    private String examPassword;
    //最大连接池数量
    @Value("${datasource.maxActive}")
    private int maxActive;
    //初始化时建立物理连接的个数,初始化发生在显示调用init方法，或者第一次getConnection时
    @Value("${datasource.initialSize}")
    private int initialSize;
    //获取连接时最大等待时间，单位毫秒
    @Value("${datasource.maxWaitMillis}")
    private long maxWaitMillis;
    //最小连接池数量
    @Value("${datasource.minIdle}")
    private int minIdle;

    @Value("${datasource.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${datasource.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;
    @Value("${datasource.validationQuery}")
    private String validationQuery;
    @Value("${datasource.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${datasource.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${datasource.testOnReturn}")
    private boolean testOnReturn;

    /**
     * 设置数据源
     *
     * @return DataSource
     */
    @Bean(value = "dataSource")
    @Primary
    public DataSource getExamDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName("dataSource");
        druidDataSource.setUrl(examUrl);
        druidDataSource.setUsername(examUserName);
        druidDataSource.setPassword(examPassword);
        //druidDataSource配置
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxWait(maxWaitMillis);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        return druidDataSource;

    }
}
