package com.lx.kettle.web.quartz;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.web.quartz.model.DBConnectionModel;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.trans.Trans;
import org.quartz.*;

import java.util.Date;

/**
 * Created by chenjiang on 2019/10/24
 * <p>
 *
 * @DisallowConcurrentExecution 该注解的作用不允许并发执行多个Job
 * 这里的并发作用于jobDetail 如一个JOB类下面有多个jobDetail的时候添加次注解不会并执行jobDetail
 * </p>
 */
@DisallowConcurrentExecution
@SuppressWarnings("all")
@Slf4j
public class JobQuartz implements InterruptableJob {

    private Trans trans;

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        this.trans.stopAll();
    }

    /**
     * 调度执行入口
     *
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap transDataMap = context.getJobDetail().getJobDataMap();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        Object KRepositoryObject = jobDataMap.get(Constant.REPOSITORYOBJECT);
        Object DbConnectionObject = jobDataMap.get(Constant.DBCONNECTIONOBJECT);

        String jobName_str = context.getJobDetail().getKey().getName();
        String[] names = jobName_str.split("@");

        String jobId = String.valueOf(jobDataMap.get(Constant.JOBID));
        String jobPath = String.valueOf(jobDataMap.get(Constant.JOBPATH));
        String jobName = String.valueOf(jobDataMap.get(Constant.JOBNAME));
        String userId = String.valueOf(jobDataMap.get(Constant.USERID));
        String logLevel = String.valueOf(jobDataMap.get(Constant.LOGLEVEL));
        String logFilePath = String.valueOf(jobDataMap.get(Constant.LOGFILEPATH));
        Date lastExecuteTime = context.getFireTime();
        Date nexExecuteTime = context.getNextFireTime();

        //判断数据库连接对象是否正确
        if (DbConnectionObject != null && DbConnectionObject instanceof DBConnectionModel) {
            //证明是从资源库中获取的
            if (KRepositoryObject != null && KRepositoryObject instanceof KRepository) {
                try {
                    runRepositoryJob(KRepositoryObject,DbConnectionObject,jobId,jobPath,jobName,userId,logLevel,logFilePath,lastExecuteTime,nexExecuteTime);
                } catch (Exception e) {
                    log.error("运行资源库DB:{},出现异常,异常详情msg:{}", JSON.toJSONString(KRepositoryObject),e);
                }
            }else{

            }

        }


    }

    /**
     * 运行资源库的作业
     *
     * @param KRepositoryObject 数据库连接对象
     * @param DbConnectionObject 资源库对象
     * @param jobId             作业ID
     * @param jobPath          作业在资源库中的路径信息
     * @param jobName          作业名称
     * @param userId           作业归属者ID
     * @param logLevel         作业的日志等级
     * @param logFilePath      作业日志保存的根路径
     * @param executeTime
     * @param nexExecuteTim
     */
    private void runRepositoryJob(Object KRepositoryObject, Object DbConnectionObject, String jobId,
                                  String jobPath, String jobName, String userId, String logLevel, String logFilePath, Date executeTime, Date nexExecuteTim) {
    }
}
