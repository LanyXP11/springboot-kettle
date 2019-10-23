package com.lx.kettle.web.controller;

import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.web.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/23
 */
@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping(value = "/job/")
public class JobController {

    @Resource(name = "jobServiceImpl")
    private JobService jobService;

    /**
     * @param jobId   jobId获取Job详情
     * @param request
     * @return
     */
    @PostMapping(value = "getJob.shtml")
    public String getJobNamebyJobId(@RequestParam(value = "jobId") Integer jobId, HttpServletRequest request) {
        KJob kjob = jobService.getJobNamebyJobId(jobId);
        return ResultDto.success(kjob);

    }

}
