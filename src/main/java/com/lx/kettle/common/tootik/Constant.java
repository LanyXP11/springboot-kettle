package com.lx.kettle.common.tootik;

import org.pentaho.di.core.logging.LogLevel;

/***
 * create by chenjiang on 2019/10/19 0019
 * <p>
 *     全局通用常量类设置
 * </p>
 */

public final class Constant {
    /**
     * encoding
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /***
     * kettle LogLevel
     */
    public static LogLevel KETTLE_LOGLEVEL;
    /**
     * Session_id
     */
    public static final String SESSION_ID = "SESSION_ID";
    /**
     * 时间格式化
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 管理员用户唯一标识
     */
    public static final String ADMIN = "admin";
    /**
     * 任务切分割符号
     */
    public static final String RUNSTATUS_SEPARATE = "-";
    /**
     * quartz parameter
     **/
    public static final String REPOSITORYOBJECT = "REPOSITORYOBJECT";//资源库对象
    public static final String DBCONNECTIONOBJECT = "DBCONNECTIONOBJECT";//连接库对象
    public static final String JOBID = "JOBID";
    public static final String TRANSID = "TRANSID";
    public static final String JOBTYPE = "JOBTYPE";
    public static final String JOBPATH = "JOBPATH";
    public static final String TRANSPATH = "TRANSPATH";
    public static final String JOBNAME = "JOBNAME";
    public static final String TRANSNAME = "TRANSNAME";
    public static final String USERID = "USERID";
    public static final String LOGLEVEL = "LOGLEVEL";
    public static final String LOGFILEPATH = "LOGFILEPATH";
    /**
     * quartz
     */
    public static final String JOB_PREFIX = "JOB";
    public static final String JOB_GROUP_PREFIX = "JOB_GROUP";
    public static final String TRIGGER_PREFIX = "TRIGGER";
    public static final String TRIGGER_GROUP_PREFIX = "TRIGGER_GROUP";
    public static final String QUARTZ_SEPARATE = "@";
    /**
     * Kettle
     */
    public static final String TYPE_JOB = "job";
    public static final String TYPE_TRANS = "transformation";
    public static final String TYPE_JOB_SUFFIX = ".kjb";
    public static final String TYPE_TRANS_SUFFIX = ".ktr";
    public static final String TYPE_TESTING = "TESTING";
    public static final String TYPE_RUNNING = "RUNNING";
    public static final String TYPE_USER_KETTLE = "KETTLE";
    public static final String TYPE_AGAIN = "AGAIN";

    public static final String STARTS_WITH_USD = "$";
    public static final String STARTS_WITH_PARAM = "-param:";
    public static final String SPLIT_PARAM = "-param:";
    public static final String SPLIT_EQUAL = "=";
    public static final String SPLIT_USD = "$";
    public static final String KETTLE_REPO = "repo";

    /**
     * 根据日志级别显示日志信息
     *
     * @param level
     * @return
     */
    public static LogLevel logger(String level) {
        LogLevel logLevel = null;
        if ("basic".equals(level)) {
            logLevel = LogLevel.BASIC;
        } else if ("detail".equals(level)) {
            logLevel = LogLevel.DETAILED;
        } else if ("error".equals(level)) {
            logLevel = LogLevel.ERROR;
        } else if ("debug".equals(level)) {
            logLevel = LogLevel.DEBUG;
        } else if ("minimal".equals(level)) {
            logLevel = LogLevel.MINIMAL;
        } else if ("rowlevel".equals(level)) {
            logLevel = LogLevel.ROWLEVEL;
        } else if ("Nothing".endsWith(level)) {
            logLevel = LogLevel.NOTHING;
        } else {
            logLevel = KETTLE_LOGLEVEL;
        }
        return logLevel;
    }


}
