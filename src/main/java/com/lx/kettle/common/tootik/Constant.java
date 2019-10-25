package com.lx.kettle.common.tootik;

/***
 * create by chenjiang on 2019/10/19 0019
 * <p>
 *     全局通用常量类设置
 * </p>
 */

public final class Constant {
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

}
