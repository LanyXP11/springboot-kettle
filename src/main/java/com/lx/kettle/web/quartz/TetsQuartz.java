package com.lx.kettle.web.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * Created by chenjiang on 2019/10/25
 */
@DisallowConcurrentExecution
@Slf4j
public class TetsQuartz implements InterruptableJob {
    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("测试集成quartz...........................");

    }
}
