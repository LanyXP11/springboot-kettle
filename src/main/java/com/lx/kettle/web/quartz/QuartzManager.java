package com.lx.kettle.web.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/24
 */
@SuppressWarnings("all")
@Slf4j
public class QuartzManager {
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * @param jobName          任务名
     *                         以作业为例：  JOB@1(资源库ID)@/job/mysql-mysql(JOB全路径)
     * @param jobGroupName     任务组名
     *                         以作业为例：  JOB_GROUP@1(用户ID)@1(资源库ID)@/job/mysql-mysql(JOB全路径)
     * @param triggerName      触发器名
     *                         以作业为例：  TRIGGER@1(资源库ID)@/job/mysql-mysql(JOB全路径)
     * @param triggerGroupName 触发器组名
     *                         以作业为例：  TRIGGER_GROUP@1(用户ID)@1(资源库ID)@/job/mysql-mysql(JOB全路径)
     * @param jobClass         任务对象实例
     * @param cron             时间设置，参考quartz说明文档
     * @param parameter        传入的参数
     * @return void
     * @Title addJob
     * @Description 添加一个定时任务
     */
    public static Date addJob(String jobName, String jobGroupName,
                              String triggerName,
                              String triggerGroupName,
                              Class<? extends Job> jobClass,
                              String cron, Map<String, Object> parameter) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 添加任务执行的参数
            parameter.forEach((k, v) -> {
                jobDetail.getJobDataMap().put(k, v);
            });
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
            return trigger.getNextFireTime();
        } catch (Exception e) {
            log.error("添加定时任务出现异常异常信息message:{}", e);
            return null;
        }
    }

    /**
     * 添加一个Job  针对手动执行的时候添加次方法
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @param jobClass
     * @param parameter
     */
    public static Date addOneJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName, Class<? extends Job> jobClass, Map<String, Object> parameter) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 添加任务执行的参数
            parameter.forEach((k, v) -> {
                jobDetail.getJobDataMap().put(k, v);
            });
            //3秒后立即执行，重复次数设为0，表示只执行一次
            SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(0))
                    .build();
            sched.scheduleJob(jobDetail, simpleTrigger);
            if (!sched.isShutdown()) {
                sched.start();
            }
            return simpleTrigger.getNextFireTime();
        } catch (Exception e) {
            log.error("添加一个一次性定时任务出现异常 异常详情Msg:{}", e);
            return null;
        }
    }

    /**
     * 获取任务状态
     * NONE: 不存在
     * NORMAL: 正常
     * PAUSED: 暂停
     * COMPLETE:完成
     * ERROR : 错误
     * BLOCKED : 阻塞
     *
     * @param triggerName
     * @param triggerGroupName
     * @return name
     */
    public static String getTriggerState(String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        String name = null;
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            name = triggerState.name();
        } catch (Exception e) {
            log.error("获取任务状态出现异常:{}", e);
            return null;
        }
        return name;
    }
}
