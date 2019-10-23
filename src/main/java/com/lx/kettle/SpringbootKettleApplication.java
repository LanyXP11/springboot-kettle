package com.lx.kettle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


/**
 * 项目启动入口
 */
@SpringBootApplication
//@ComponentScan({"com.lx.kettle.web.*", "com.lx.kettle.core.*"})
public class SpringbootKettleApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringbootKettleApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKettleApplication.class, args);
        System.err.println("启动成功");
    }


}
