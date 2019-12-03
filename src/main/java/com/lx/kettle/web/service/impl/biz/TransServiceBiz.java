package com.lx.kettle.web.service.impl.biz;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.mapper.KRepositoryDao;
import com.lx.kettle.core.mapper.KTransMonitorDao;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.core.model.KTransMonitor;
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

/***
 * create by LanyXP on 2019/11/3 0003
 */
@Component
@Slf4j
@SuppressWarnings("all")
@PropertySource({"classpath:application.properties"})
public class TransServiceBiz {
    @Autowired
    private KRepositoryDao kRepositoryDao;

    @Autowired
    private KTransMonitorDao kTransMonitorDao;

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

        return quartzBasic;
    }

    public Map<String, Object> getQuartzParameter(KTrans kTrans) {
        // Quartz执行时的参数
        Map<String, Object> parameter = new HashMap();
        Integer transRepositoryId = kTrans.getTransRepositoryId();
        KRepository kRepository = null;
        if (transRepositoryId != null) {// 这里是判断是否为资源库中的转换还是文件类型的转换的关键点
            kRepository = this.kRepositoryDao.single(transRepositoryId);
        }
        // 资源库对象
        parameter.put(Constant.REPOSITORYOBJECT, kRepository);

        // 数据库连接对象
        DBConnectionModel dbmodel = DBConnectionModel.builder().connectionUrl(url).connectionPassword(password).connectionUser(userName).connectionDriveClassName(dbDriverClassName).build();
        // 转换ID
        parameter.put(Constant.DBCONNECTIONOBJECT, dbmodel);

        parameter.put(Constant.TRANSID, kTrans.getTransId());
        parameter.put(Constant.JOBTYPE, 2);
        String transPath = kTrans.getTransPath();
        if (transPath.contains("/")) {
            int lastIndexOf = StringUtils.lastIndexOf(transPath, "/");
            String path = transPath.substring(0, lastIndexOf);
            // 转换在资源库中的路径
            parameter.put(Constant.TRANSPATH, StringUtils.isEmpty(path) ? "/" : path);
            // 转换名称
            parameter.put(Constant.TRANSNAME, transPath.substring(lastIndexOf + 1, transPath.length()));
        }
        // 用户ID
        parameter.put(Constant.USERID, kTrans.getAddUser());
        // 转换日志等级
        parameter.put(Constant.LOGLEVEL, kTrans.getTransLogLevel());
        // 转换日志的保存位置
        parameter.put(Constant.LOGFILEPATH, kettleLogFilePath);
        log.info("获取执行转换的参数:{}", parameter);
        return parameter;
    }

    /**
     * 添加监控
     *
     * @param userId          用户ID
     * @param transId         转换ID
     * @param nextExecuteTime
     */
    public void addMonitor(Integer userId, Integer transId, Date time) {
        KTransMonitor build = KTransMonitor.builder().addUser(userId).monitorTrans(transId).build();
        KTransMonitor kTransMonitorOne = this.kTransMonitorDao.templateOne(build);
        //如果不为空的话 仅仅更新状态 为空初始化数据
        if (kTransMonitorOne != null) {
            kTransMonitorOne.setMonitorStatus(1);
            kTransMonitorOne.setRunStatus(new StringBuilder().append(kTransMonitorOne.getRunStatus()).append(",").append(new Date().getTime()).append(Constant.RUNSTATUS_SEPARATE).toString());
            kTransMonitorOne.setNextExecuteTime(time);
            kTransMonitorDao.updateTemplateById(kTransMonitorOne);
        } else {
            KTransMonitor bean = KTransMonitor.builder()
                    .monitorTrans(transId)
                    .addUser(userId)
                    .monitorSuccess(0)
                    .monitorFail(0)
                    .runStatus(new StringBuilder().append(new Date().getTime()).append(Constant.RUNSTATUS_SEPARATE).toString())
                    .monitorStatus(1)
                    .nextExecuteTime(time).build();
            kTransMonitorDao.insert(bean);
        }
    }

    /**
     * 移除任务
     *
     * @param userId
     * @param transId
     */
    public void removeMonitor(Integer userId, Integer transId) {
        KTransMonitor transMonitor = KTransMonitor.builder().addUser(userId).monitorTrans(transId).build();
        KTransMonitor resultBean = this.kTransMonitorDao.templateOne(transMonitor);
        resultBean.setMonitorStatus(2);
        StringBuilder runStatusBuilder = new StringBuilder();
        runStatusBuilder.append(resultBean.getRunStatus()).append(new Date().getTime());
        resultBean.setRunStatus(runStatusBuilder.toString());
        int i = kTransMonitorDao.updateTemplateById(resultBean);
        if (i < 0) {
            log.error("移除任务失败Userid={},transId={}", userId, transId);
            throw new RuntimeException("移除任务失败");
        }
    }
}
