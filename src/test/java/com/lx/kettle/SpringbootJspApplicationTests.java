package com.lx.kettle;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.mapper.KJobDao;
import com.lx.kettle.core.mapper.KRepositoryTypeDao;
import com.lx.kettle.core.mapper.KUserDao;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.core.model.KRepositoryType;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.quartz.QuartzManager;
import com.lx.kettle.web.quartz.TetsQuartz;
import com.lx.kettle.web.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class SpringbootJspApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    KUserDao kUserDao;
    @Autowired
    KJobDao kJobDao;
    @Autowired
    KRepositoryTypeDao kRepositoryTypeDao;

    @Test
    public void contextLoads() {
        KUser admin = KUser.builder().uAccount("admin").build();
        KUser user = userService.login(admin);
        System.out.println(user);
    }

    @Test
    public void userfind() {
        List<KUser> resultUserList = this.kUserDao.template(KUser.builder().delFlag(1).build(), 2, 10);
        System.out.println(JSON.toJSONString(resultUserList));
    }

    @Test
    public void kjobDaoTest() {
        List<KJob> kJobList = kJobDao.pageQuery(KJob.builder().addUser(1).delFlag(1).build(), 0, 10);
        System.out.println("JOSN=====>" + JSON.toJSONString(kJobList));
    }

    @Test
    public void kRepositoryTypeDao() {
        KRepositoryType kRepositoryType = KRepositoryType.builder().repositoryTypeId(1).build();
//        KRepositoryType repositoryType = kRepositoryTypeDao.single(1);
        List<KRepositoryType> all = kRepositoryTypeDao.all();
        System.out.println(JSON.toJSON(all));
    }


    /**
     * jobName 任务名:work1
     * jobGroupName 任务组名:/
     * triggerName 触发器名:qqq1111
     * triggerGroupName 触发器组名:aaa
     */
    public static void main(String[] args) {
        System.out.println("任务启动............................");
        String cron = "0 */1 * * * ?";
        Map<String, Object> parameter = new HashMap<String, Object>();
        QuartzManager.addJob("work1", "/", "qqq1111", "aaa", TetsQuartz.class, cron, parameter);
//        System.out.println(StringUtils.replace("readfile_job", Constant.JOB_PREFIX, Constant.TRIGGER_PREFIX));

    }

}
