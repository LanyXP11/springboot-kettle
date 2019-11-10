package com.lx.kettle.web.service.impl.biz;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.mapper.KRepositoryDao;
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
        log.info("获取任务调度的基础信息结束jobName={},jobGroupName={},triggerName={},triggerGroupName={}", jobName, jobGroupName, triggerName, triggerGroupName);
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
        return parameter;
    }

    /**
     * 添加监控
     *
     * @param userId          用户ID
     * @param transId         转换ID
     * @param nextExecuteTime
     */
    private void addMonitor(Integer userId, Integer transId, Date nextExecuteTime) {
        KTransMonitor build = KTransMonitor.builder().addUser(userId).monitorTrans(transId).build();
    }


}
