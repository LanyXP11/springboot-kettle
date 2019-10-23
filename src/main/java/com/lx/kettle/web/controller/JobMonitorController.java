package com.lx.kettle.web.controller;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.JobMonitorService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenjiang on 2019/10/21
 */
@RequestMapping("/job/monitor/")
@RestController
public class JobMonitorController {
    @Autowired
    private JobMonitorService jobMonitorService;
    /**
     * 获取在监控的作业数
     *
     * @param request
     * @return
     */
    @PostMapping(value = "getAllMonitorJob.shtml")
    @ResponseBody
    public Object getAllMonitorJob(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JSONUtils.objectToJson(jobMonitorService.getAllMonitorJob(kUser.getuId()));
    }

}
