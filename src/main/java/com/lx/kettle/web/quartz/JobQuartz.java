package com.lx.kettle.web.quartz;

import com.lx.kettle.common.tootik.Constant;
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
        //获取文件资源库对象   和    数据资源库对象
        Object KRepositoryObject = transDataMap.get(Constant.REPOSITORYOBJECT);
        Object DbConnectionObject = transDataMap.get(Constant.DBCONNECTIONOBJECT);

        String transId = String.valueOf(transDataMap.get(Constant.TRANSID));//转换ID
        String transPath = String.valueOf(transDataMap.get(Constant.TRANSPATH));//路径地址
        String transName = String.valueOf(transDataMap.get(Constant.TRANSNAME));//转换名称
        String userId = String.valueOf(transDataMap.get(Constant.USERID));//用户ID
        String logLevel = String.valueOf(transDataMap.get(Constant.LOGLEVEL));//日志级别
        String logFilePath = String.valueOf(transDataMap.get(Constant.LOGFILEPATH));//日志存放路径地址

        Date lastExecuteTime = context.getFireTime();
        Date nexExecuteTime = context.getNextFireTime();
        //判断数据库连接对象是否正确
        if (DbConnectionObject != null && DbConnectionObject instanceof DBConnectionModel) {

        }


    }
}
