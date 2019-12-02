package com.lx.kettle.core.init;

import com.lx.kettle.common.kettle.kettleinit.KettleInit;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.KettleEnvironment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * create by chenjiang on 2019/10/26 0026
 * <p>
 *      该方法用于启动springboot容器后  加载Kettle环境初始化和自定义的Kettle环境初始化
 * </p>
 */
@Component
@Order(1)
@Slf4j
public class KettleCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        long startLog = System.currentTimeMillis();
        log.info("初始化kettle环境开始");
        KettleEnvironment.init();
        log.info("初始化自定义kettle环境开始");
//        KettleInit.init();//TODO
        log.info("初始化kettle环境结束 总耗时:{}:毫秒", (System.currentTimeMillis() - startLog));
    }
}
