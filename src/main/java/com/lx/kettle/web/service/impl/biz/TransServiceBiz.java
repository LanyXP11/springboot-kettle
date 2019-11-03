package com.lx.kettle.web.service.impl.biz;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.model.KTrans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/***
 * create by LanyXP on 2019/11/3 0003
 */
@Component
@Slf4j
@SuppressWarnings("all")
@PropertySource({"classpath:application.properties"})
public class TransServiceBiz {

    public Map<String, String> getQuartzBasic(KTrans kTrans) {
        Integer userId = kTrans.getAddUser();
        Integer transRepositoryId = kTrans.getTransRepositoryId();
        String transPath = kTrans.getTransPath();
        Map<String, String> quartzBasic = new HashMap<String, String>();
        // 拼接Quartz的任务名称
        StringBuilder jobName = new StringBuilder();
        jobName.append(Constant.JOB_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(transPath);
        // 拼接Quartz的任务组名称
        StringBuilder jobGroupName = new StringBuilder();
        jobGroupName.append(Constant.JOB_GROUP_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(userId).append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(transPath);
        // 拼接Quartz的触发器名称
        String triggerName = StringUtils.replace(jobName.toString(), Constant.JOB_PREFIX, Constant.TRIGGER_PREFIX);
        // 拼接Quartz的触发器组名称
        String triggerGroupName = StringUtils.replace(jobGroupName.toString(), Constant.JOB_GROUP_PREFIX, Constant.TRIGGER_GROUP_PREFIX);
        quartzBasic.put("jobName", jobName.toString());
        quartzBasic.put("jobGroupName", jobGroupName.toString());
        quartzBasic.put("triggerName", triggerName);
        quartzBasic.put("triggerGroupName", triggerGroupName);
        // 拼接Quartz的任务名称
        log.info("获取任务调度的基础信息结束jobName={},jobGroupName={},triggerName={},triggerGroupName={}", jobName, jobGroupName, triggerName, triggerGroupName);
        return quartzBasic;
    }

}
