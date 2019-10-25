package com.lx.kettle.web.service.impl;

import com.lx.kettle.common.tootik.JobStatusEnum;
import com.lx.kettle.common.tootik.TriggerStateEnum;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KJobDao;
import com.lx.kettle.core.mapper.KQuartzDao;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.core.model.KQuartz;
import com.lx.kettle.web.quartz.JobQuartz;
import com.lx.kettle.web.quartz.QuartzManager;
import com.lx.kettle.web.service.JobService;
import com.lx.kettle.web.service.impl.biz.JobServiceBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/23
 */
@Service(value = "jobServiceImpl")
@Slf4j
@SuppressWarnings("all")
public class JobServiceImpl implements JobService {
    @Autowired
    private JobServiceBiz jobServiceBiz;
    @Autowired
    private KJobDao kJobDao;
    @Autowired
    private KQuartzDao kQuartzDao;

    /**
     * 根据JobId获取Job详情
     *
     * @param jobId
     * @return
     */
    @Override
    public KJob getJobNamebyJobId(Integer jobId) {
        return kJobDao.single(jobId);
    }

    /**
     * 获取JOb作业
     *
     * @param offset
     * @param limit
     * @param categoryId
     * @param jobName
     * @param integer
     * @return
     */
    @Override
    public BootTablePage getJobListResultBooTablePage(Integer start, Integer size, Integer categoryId, String jobName, Integer uid) {
        KJob template = KJob.builder().addUser(uid).delFlag(1).build();
        if (categoryId != null) {
            template.setCategoryId(categoryId);
        }
        if (StringUtils.isNotBlank(jobName)) {
            template.setJobName(jobName);
        }
        List<KJob> kJobList = kJobDao.pageQuery(template, start, size);
        long allCount = kJobDao.allCount(template);
        return BootTablePage.builder().rows(kJobList).total(allCount).build();
    }


    /**
     * 根据JobId查询Job状态
     *
     * @param jobId
     * @return
     */
    @Override
    public Object getJobRunState(Integer jobId) {
        KJob kJob = this.kJobDao.unique(jobId);
        Map getQuartzBasicMap = this.jobServiceBiz.getQuartzBasic(kJob);
        String triggerState = QuartzManager.getTriggerState((String) getQuartzBasicMap.get("triggerName"), (String) getQuartzBasicMap.get("triggerGroupName"));
        return TriggerStateEnum.getTriggerStateDescByCode(triggerState).equals("null") ? "未定义" : TriggerStateEnum.getTriggerStateDescByCode(triggerState);
    }

    /**
     * @param jobId 作业ID
     *              启动作业
     */
    @Override
    public void start(Integer jobId) {
        // 获取到作业对象
        KJob kJob = kJobDao.unique(jobId);
        // 获取到定时策略对象
        KQuartz kQuartz = kQuartzDao.unique(kJob.getJobQuartz());
        // 定时策略
        String quartzCron = kQuartz.getQuartzCron();
        // 用户ID
        Integer userId = kJob.getAddUser();
        //分别获取调度任务基础信息和执行定时任务Quartz的参数
        Map<String, String> quartzBasic = this.jobServiceBiz.getQuartzBasic(kJob);
        Map<String, Object> quartzParameter = this.jobServiceBiz.getQuartzParameter(kJob);
        Date nextExecuteTime = null;
        try {
            //如果当前的任务就是执行一次的话 即用户在页面上是手动执行的
            if (new Integer(1).equals(kJob.getJobQuartz())) {
                nextExecuteTime = nextExecuteTime = QuartzManager.addOneJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                        quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"), JobQuartz.class, quartzParameter);
            } else {// 如果是按照策略执行
                nextExecuteTime = nextExecuteTime = QuartzManager.addJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                        quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"),
                        JobQuartz.class, quartzCron, quartzParameter);
            }
        } catch (Exception e) {
            log.error("执行任务出现异常：异常信息:{}", e);
            kJob.setJobStatus(JobStatusEnum.STOP.getCode());
            kJobDao.updateTemplateById(kJob);
            return;
        }
        //添加监控信息 以方便前端页面查询
        this.jobServiceBiz.addMonitor(userId,jobId,nextExecuteTime);
        //TODO 优化
        kJob.setJobStatus(JobStatusEnum.RUNING.getCode());
        kJobDao.updateTemplateById(kJob);
    }

    /**
     * @param categoryId
     * @param jobName
     * @param userId
     * @return
     */
    @Override
    public Long getStartTaskCount(Integer categoryId, String jobName, Integer userId) {
        return getTaskCount(categoryId, jobName, userId, "1");
    }

    @Override
    public Long getStopTaskCount(Integer categoryId, String jobName, Integer userId) {
        return getTaskCount(categoryId, jobName, userId, "2");
    }

    private Long getTaskCount(Integer categoryId, String jobName, Integer userId, String type) {
        long result = 0l;
        KJob template = KJob.builder().addUser(userId).delFlag(1).build();
        if (categoryId != null) {
            template.setCategoryId(categoryId);
        }
        if (StringUtils.isNotBlank(jobName)) {
            template.setJobName(jobName);
        }
        switch (type) {
            case "1":
                template.setJobStatus(JobStatusEnum.RUNING.getCode());
                result = kJobDao.allCount(template);
                break;
            case "2":
                template.setJobStatus(JobStatusEnum.STOP.getCode());
                result = kJobDao.allCount(template);
        }
        return result;
    }
}
