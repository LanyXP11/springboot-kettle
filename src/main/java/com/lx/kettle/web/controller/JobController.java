package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.core.model.KUser;
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

    /**
     * 获取JOb列表
     *
     * @return
     */
    @RequestMapping("getList.shtml")
    public String getJobList(HttpServletRequest request, @RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset,
                             @RequestParam("categoryId") Integer categoryId, @RequestParam("jobName") String jobName) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        log.info("查询Job列表开始 参数limit={},offset={},categoryId={},jobName={}user={}:", limit, offset, categoryId, jobName, JSON.toJSONString(user));
        BootTablePage pageList=jobService.getJobListResultBooTablePage(offset,limit,categoryId,jobName,user.getuId());
        log.info("查询Job列表最终返回值：{}",JSON.toJSONString(pageList));
        return JSONUtils.objectToJson(pageList);
    }

    /**
     *
     * @param categoryId
     * @param jobName
     * @param request
     * @return
     */
    @RequestMapping("getStartTaskCount.shtml")
    public String getStartTaskCount(@RequestParam("categoryId") Integer categoryId, @RequestParam("jobName") String jobName, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JSONUtils.objectToJson(jobService.getStartTaskCount(categoryId, jobName, kUser.getuId()));
    }
    /**
     *
     * @param categoryId
     * @param jobName
     * @param request
     * @return
     */
    @RequestMapping("getStopTaskCount.shtml")
    public String getStopTaskCount(@RequestParam("categoryId") Integer categoryId, @RequestParam("jobName") String jobName, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JSONUtils.objectToJson(jobService.getStopTaskCount(categoryId, jobName, kUser.getuId()));
    }

}
