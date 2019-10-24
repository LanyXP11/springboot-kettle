package com.lx.kettle.web.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * Created by chenjiang on 2019/10/24
 * <p>
 *     @DisallowConcurrentExecution 该注解的作用不允许并发执行多个Job
 *     这里的并发作用于jobDetail 如一个JOB类下面有多个jobDetail的时候添加次注解不会并执行jobDetail
 * </p>
 */
@DisallowConcurrentExecution
@SuppressWarnings("all")
@Slf4j
public class JobQuartz implements InterruptableJob {
    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    /**
     * 
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
