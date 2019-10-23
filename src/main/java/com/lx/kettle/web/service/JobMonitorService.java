package com.lx.kettle.web.service;

import com.lx.kettle.core.dto.BootTablePage;

import java.util.Map;

/***
 * create by chenjiang on 2019/10/19 0019
 */
public interface JobMonitorService {
    /**
     * 根据用户ID获取所有的监控作业
     * @param uId
     * @return
     */
    Integer getAllMonitorJob(Integer uId);

    Map<String,Object> getJobLine(Integer uid);

    BootTablePage getJobListTop5(Integer integer);
}
