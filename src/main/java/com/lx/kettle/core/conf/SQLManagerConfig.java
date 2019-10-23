package com.lx.kettle.core.conf;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Created by chenjiang on 2019/10/21
 * <p>
 *         BeetlSql相关配置
 * </p>
 */
@Configuration
@SuppressWarnings("all")
public class SQLManagerConfig {
    /**
     * @param master
     * @return SqlManagerFactoryBean
     */
    @Bean(name = "sqlManagerFactoryBean")
    @Primary
    public SqlManagerFactoryBean getSqlManagerFactoryBean(@Qualifier("dataSource") DataSource master) {
        SqlManagerFactoryBean factoryBean = new SqlManagerFactoryBean();
        BeetlSqlDataSource source = new BeetlSqlDataSource();
        source.setMasterSource(master);
        factoryBean.setCs(source);
        factoryBean.setDbStyle(new MySqlStyle());
        factoryBean.setNc(new UnderlinedNameConversion());//开启驼峰 数据库k_user ==>
        factoryBean.setInterceptors(new DebugInterceptor[]{new DebugInterceptor()});
        factoryBean.setSqlLoader(new ClasspathLoader("/sql"));//sql文件路径
        return factoryBean;
    }

    /**
     * @return BeetlSqlScannerConfigurer
     */
    @Bean(name = "beetlSqlScannerConfigurer")
    public BeetlSqlScannerConfigurer getBeetlSqlScannerConfigurer() {
        BeetlSqlScannerConfigurer conf = new BeetlSqlScannerConfigurer();
        conf.setBasePackage("com.lx.kettle.core.mapper");
        conf.setDaoSuffix("Dao");
        conf.setSqlManagerFactoryBeanName("sqlManagerFactoryBean");
        return conf;
    }


}
