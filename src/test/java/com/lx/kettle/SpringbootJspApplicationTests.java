package com.lx.kettle;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.core.mapper.KUserDao;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class SpringbootJspApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    KUserDao kUserDao;
    @Test
    public void contextLoads() {
        KUser admin = KUser.builder().uAccount("admin").build();
        KUser user = userService.login(admin);
        System.out.println(user);
    }
    @Test
    public void  userfind(){
        List<KUser> resultUserList = this.kUserDao.template(KUser.builder().delFlag(1).build(), 2, 10);
        System.out.println(JSON.toJSONString(resultUserList));
    }

}
