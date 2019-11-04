package com.lx.kettle.web.quartz;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.kettle.repository.RepositoryUtils;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.model.KJobMonitor;
import com.lx.kettle.core.model.KJobRecord;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.web.quartz.model.DBConnectionModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.quartz.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
    private Job job;

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
                    runRepositoryJob(KRepositoryObject, DbConnectionObject, jobId, jobPath, jobName, userId, logLevel, logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (Exception e) {
                    log.error("运行资源库DB:{},出现异常,异常详情msg:{}", JSON.toJSONString(KRepositoryObject), e);
                }
            } else {
                try {
                    runFileJob(DbConnectionObject, jobId, jobPath, jobName, userId, logLevel, logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (Exception e) {
                    log.error("运行资源库File:{},出现异常,异常详情msg:{}", JSON.toJSONString(KRepositoryObject), e);
                }
            }
        }
    }

    /**
     * 运行文件库job
     *
     * @param DbConnectionObject
     * @param jobId
     * @param jobPath
     * @param jobName
     * @param userId
     * @param logLevel
     * @param logFilePath
     * @param lastExecuteTime
     * @param nexExecuteTime
     * @throws KettleXMLException
     */
    private void runFileJob(Object DbConnectionObject, String jobId, String jobPath, String jobName, String userId, String logLevel, String logFilePath, Date lastExecuteTime, Date nexExecuteTime) throws KettleXMLException {
        JobMeta jobMeta = new JobMeta(jobPath, null);
        job = new org.pentaho.di.job.Job(null, jobMeta);
        job.setDaemon(true);
        job.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(logLevel)) {
            job.setLogLevel(Constant.logger(logLevel));
        }
        String exception = null;
        Integer recordStatus = 1;
        Date jobStartDate = null;
        Date jobStopDate = null;
        String logText = null;
        try {
            jobStartDate = new Date();
            job.run();
            job.waitUntilFinished();
            jobStopDate = new Date();
        } catch (Exception e) {
            exception = e.getMessage();
            recordStatus = 2;
        } finally {
            if (null != job && job.isFinished()) {
                if (job.getErrors() > 0 && (StringUtils.isBlank(job.getResult().getLogText()))) {
                    logText = exception;
                }
                // 写入作业执行结果
                StringBuilder allLogFilePath = new StringBuilder();
                allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                        .append(StringUtils.remove(jobPath, "/")).append("@").append(jobName).append("-log").append("/")
                        .append(new Date().getTime()).append(".").append("txt");
                String logChannelId = job.getLogChannelId();
                LoggingBuffer appender = KettleLogStore.getAppender();
                logText = appender.getBuffer(logChannelId, true).toString();
                try {
                    KJobRecord kJobRecord = new KJobRecord();
                    kJobRecord.setRecordJob(Integer.parseInt(jobId));
                    kJobRecord.setAddUser(Integer.parseInt(userId));
                    kJobRecord.setLogFilePath(allLogFilePath.toString());
                    kJobRecord.setRecordStatus(recordStatus);
                    kJobRecord.setStartTime(jobStartDate);
                    kJobRecord.setStopTime(jobStopDate);
                    writeToDBAndFile(DbConnectionObject, kJobRecord, logText, lastExecuteTime, nexExecuteTime);
                } catch (IOException | SQLException e) {
                    log.info("资源库File job执行记录结果写入数据库异常 info:{}", e);
                }
            }
        }
    }

    /**
     * 运行资源库的作业
     *
     * @param KRepositoryObject  数据库连接对象
     * @param DbConnectionObject 资源库对象
     * @param jobId              作业ID
     * @param jobPath            作业在资源库中的路径信息
     * @param jobName            作业名称
     * @param userId             作业归属者ID
     * @param logLevel           作业的日志等级
     * @param logFilePath        作业日志保存的根路径
     * @param executeTime
     * @param nexExecuteTim
     */
    private void runRepositoryJob(Object KRepositoryObject, Object DbConnectionObject, String jobId,
                                  String jobPath, String jobName, String userId, String logLevel, String logFilePath, Date lastExecuteTime, Date nexExecuteTime) throws Exception {
        //1.获取资源库的ID查询关联的kettleDatabaseRepository
        KRepository kRepository = (KRepository) KRepositoryObject;
        Integer repositoryId = kRepository.getRepositoryId();
        KettleDatabaseRepository kettleDatabaseRepository = null;
        if (RepositoryUtils.KettleDatabaseRepositoryCatch.containsKey(repositoryId)) {
            kettleDatabaseRepository = RepositoryUtils.KettleDatabaseRepositoryCatch.get(repositoryId);
        } else {
            kettleDatabaseRepository = RepositoryUtils.connectionRepository(kRepository);
        }
        if (kettleDatabaseRepository != null) {
            RepositoryDirectoryInterface directory = kettleDatabaseRepository.loadRepositoryDirectoryTree().findDirectory(jobPath);
            JobMeta jobMeta = kettleDatabaseRepository.loadJob(jobName, directory, new ProgressNullMonitorListener(), null);
            log.info("元Job资源库信息详情:{}", JSON.toJSONString(jobMeta));
            job = new org.pentaho.di.job.Job(kettleDatabaseRepository, jobMeta);
            job.setDaemon(true);//  开启多线程
            job.setLogLevel(LogLevel.DEBUG);
            if (StringUtils.isNotEmpty(logLevel)) {
                job.setLogLevel(Constant.logger(logLevel));
            }
            String exception = null;
            Integer recordStatus = 1;
            Date jobStartDate = null;
            Date jobStopDate = null;
            String logText = null;
            try {
                jobStartDate = new Date();
                job.run();
                job.waitUntilFinished();
                jobStopDate = new Date();
            } catch (Exception e) {
                log.info("运行资源库作业出现异常:{}", e);
                recordStatus = 2;
                exception = e.getMessage();//TODO 异常详情截取前100个字符
            } finally {
                if (job != null && job.isFinished()) {
                    //判断job执行的异常信息
                    if (job.getErrors() > 0 && (StringUtils.isBlank(job.getResult().getLogText()))) {
                        logText = exception;
                    }
                    //作业运行的结果详情写入到数据库中
                    StringBuilder allLogFilePath = new StringBuilder();
                    allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                            .append(StringUtils.remove(jobPath, "/")).append("@").append(jobName).append("-log").append("/")
                            .append(new Date().getTime()).append(".").append("txt");
                    String logChannelId = job.getLogChannelId();
                    LoggingBuffer appender = KettleLogStore.getAppender();
                    logText = appender.getBuffer(logChannelId, true).toString();
                    try {
                        KJobRecord kJobRecord = new KJobRecord();
                        kJobRecord.setRecordJob(Integer.parseInt(jobId));
                        kJobRecord.setAddUser(Integer.parseInt(userId));
                        kJobRecord.setLogFilePath(allLogFilePath.toString());
                        kJobRecord.setRecordStatus(recordStatus);
                        kJobRecord.setStartTime(jobStartDate);
                        kJobRecord.setStopTime(jobStopDate);
                        writeToDBAndFile(DbConnectionObject, kJobRecord, logText, lastExecuteTime, nexExecuteTime);
                    } catch (Exception e) {
                        log.info("资源库DB job执行记录结果写入数据库异常 info:{}", e);
                        throw new RuntimeException("资源库获取异常运行作业终止");
                    }
                }
            }
        } else {
            throw new RuntimeException("资源库DB获取异常运行作业终止");
        }
    }

    /**
     * @param dbConnectionObject
     * @param kJobRecord
     * @param logText
     * @param lastExecuteTime
     * @param nexExecuteTime
     */
    private void writeToDBAndFile(Object DbConnectionObject, KJobRecord kJobRecord, String logText, Date lastExecuteTime, Date nextExecuteTime) throws IOException, SQLException {
        // 将日志信息写入文件
        FileUtils.writeStringToFile(new File(kJobRecord.getLogFilePath()), logText, Constant.DEFAULT_ENCODING, false);
        DBConnectionModel DBConnectionModel = (DBConnectionModel) DbConnectionObject;
        ConnectionSource source = ConnectionSourceHelper.getSimple(DBConnectionModel.getConnectionDriveClassName(), DBConnectionModel.getConnectionUrl(), DBConnectionModel.getConnectionUser(), DBConnectionModel.getConnectionPassword());
        DBStyle mysql = new MySqlStyle();
        SQLLoader loader = new ClasspathLoader("/");
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, new Interceptor[]{new DebugInterceptor()});
        //开启事务
        DSTransactionManager.start();

        sqlManager.insert(kJobRecord);

        KJobMonitor template = KJobMonitor.builder().addUser(kJobRecord.getAddUser()).monitorJob(kJobRecord.getRecordJob()).build();
        KJobMonitor templateOne = sqlManager.templateOne(template);
        templateOne.setLastExecuteTime(lastExecuteTime);
        //在监控表中增加下一次执行时间
        templateOne.setNextExecuteTime(nextExecuteTime);
        if (kJobRecord.getRecordStatus() == 1) {// 证明成功
            //成功次数加1
            templateOne.setMonitorSuccess(templateOne.getMonitorSuccess() + 1);
            sqlManager.updateById(templateOne);
        } else if (kJobRecord.getRecordStatus() == 2) {// 证明失败
            //失败次数加1
            templateOne.setMonitorFail(templateOne.getMonitorFail() + 1);
            sqlManager.updateById(templateOne);
        }
        //提交事务
        DSTransactionManager.commit();
    }
}
