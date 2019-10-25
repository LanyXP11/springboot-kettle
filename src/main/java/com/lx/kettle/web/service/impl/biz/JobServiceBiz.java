package com.lx.kettle.web.service.impl.biz;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.tootik.JobTypeEnum;
import com.lx.kettle.core.mapper.KRepositoryDao;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.web.quartz.model.DBConnectionModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/25
 */
@Component
@Slf4j
@SuppressWarnings("all")
@PropertySource({"classpath:application.properties"})
public class JobServiceBiz {
    @Autowired
    private KRepositoryDao kRepositoryDao;

    @Value("${datasource.exam.url}")
    private String url;

    @Value("${datasource.exam.username}")
    private String userName;
    @Value("${datasource.exam.password}")

    private String password;
    @Value("${datasource.dbconnection.driverClassName}")
    private String dbDriverClassName;

    @Value("${kettle.log.file.path}")
    private String kettleLogFilePath;


    /**
     * @param 转换对象
     * @return Map<String,Object>
     * @Title getQuartzParameter
     * @Description 获取任务调度的参数
     */
    public Map<String, Object> getQuartzParameter(KJob kJob) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        Integer repositoryId = kJob.getJobRepositoryId();//获取资源库对象
        KRepository kRepository = null;
        if (repositoryId != null) {
            kRepository = this.kRepositoryDao.single(repositoryId);
        }
        //资源库对象
        parameter.put(Constant.REPOSITORYOBJECT, kRepository);
        //数据库连接对象
        DBConnectionModel dbmodel = DBConnectionModel.builder().connectionUrl(url).connectionPassword(password).connectionUser(userName).connectionDriveClassName(dbDriverClassName).build();
        parameter.put(Constant.DBCONNECTIONOBJECT, dbmodel);
        //转换ID
        parameter.put(Constant.JOBID, kJob.getJobId());
        parameter.put(Constant.JOBTYPE, JobTypeEnum.DB.getCode());
        String jobPath = kJob.getJobPath();
        if (StringUtils.isNotBlank(jobPath) && jobPath.contains("/")) {
            int lastIndexOf = StringUtils.lastIndexOf(jobPath, "/");
            String path = jobPath.substring(0, lastIndexOf);
            // 转换在资源库中的路径
            parameter.put(Constant.JOBPATH, StringUtils.isEmpty(path) ? "/" : path);
            // 转换名称
            parameter.put(Constant.JOBNAME, jobPath.substring(lastIndexOf + 1, jobPath.length()));
        }
        // 用户ID
        parameter.put(Constant.USERID, kJob.getAddUser());
        // 转换日志等级
        parameter.put(Constant.LOGLEVEL, kJob.getJobLogLevel());
        // 转换日志的保存位置
        parameter.put(Constant.LOGFILEPATH, kettleLogFilePath);
        log.info("获取任务调度的参数 结束 详情:{}", JSON.toJSONString(parameter));
        return parameter;

    }

    /**
     * @param kJob 转换对象
     * @return Map<String,String> 任务调度的基础信息
     * @Title getQuartzBasic
     * @Description 获取任务调度的基础信息
     */
    public Map<String, String> getQuartzBasic(KJob kJob) {
        Integer userId = kJob.getAddUser();
        Integer transRepositoryId = kJob.getJobRepositoryId();
        String jobPath = kJob.getJobPath();
        Map<String, String> quartzBasic = new HashMap<String, String>();
        // 拼接Quartz的任务名称 任务组名称
        StringBuilder jobName = new StringBuilder();
        jobName.append(Constant.JOB_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(jobPath);
        StringBuilder jobGroupName = new StringBuilder();
        jobGroupName.append(Constant.JOB_GROUP_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(userId).append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(jobPath);
        // 拼接Quartz的触发器名称 触发器组名称
        String triggerName = StringUtils.replace(jobName.toString(), Constant.JOB_PREFIX, Constant.TRIGGER_PREFIX);
        String triggerGroupName = StringUtils.replace(jobGroupName.toString(), Constant.JOB_GROUP_PREFIX, Constant.TRIGGER_GROUP_PREFIX);
        quartzBasic.put("jobName", jobName.toString());
        quartzBasic.put("jobGroupName", jobGroupName.toString());
        quartzBasic.put("triggerName", triggerName);
        quartzBasic.put("triggerGroupName", triggerGroupName);
        log.info("获取任务调度的基础信息结束jobName={},jobGroupName={},triggerName={},triggerGroupName={}", jobName, jobGroupName, triggerName, triggerGroupName);
        return quartzBasic;
    }

    /**
     * 添加监控
     * 
     * @param userId 任务Id
     * @param jobId  用户ID
     * @param nextExecuteTime 时间
     */
    public void  addMonitor(Integer userId, Integer jobId, Date nextExecuteTime){

    }
}
