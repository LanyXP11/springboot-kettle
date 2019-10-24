package com.lx.kettle.web.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * Created by chenjiang on 2019/10/24
 * <p>
 *     @DisallowConcurrentExecution该注解的作用
 * </p>
 */
@DisallowConcurrentExecution
@SuppressWarnings("all")
@Slf4j
public class JobQuartz implements InterruptableJob {
    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
