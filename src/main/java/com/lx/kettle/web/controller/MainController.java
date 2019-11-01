package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.JobMonitorService;
import com.lx.kettle.web.service.TransMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 中转Controller
 */
@RestController
@RequestMapping("/main/")
@SuppressWarnings("all")
@Slf4j
public class MainController {
    @Autowired
    private TransMonitorService transMonitorService;
    @Autowired
    private JobMonitorService jobMonitorService;

    /**
     * 获取全部运行的任务
     *
     * @param request
     * @return
     */
    @PostMapping("allRuning.shtml")
    public String allRuning(HttpServletRequest request) {
        log.info("[查询正在kettle跑的任务开始 SESSION_ID={}]", JSON.toJSONString(request.getSession().getAttribute(Constant.SESSION_ID)));
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        Integer uId = user.getuId();
        Integer allMonitorTrans = transMonitorService.getAllMonitorTrans(uId);
        Integer allMonitorJob = jobMonitorService.getAllMonitorJob(uId);
        Integer allRuning = allMonitorTrans + allMonitorJob;
        log.info("[查询一共运行的任务条数:{}]", allRuning);
        return JSONUtils.objectToJson(allRuning);
    }

    /**
     * 获取用户7 天内的转换和作业编辑前端页面
     *
     * @param request
     * @return
     */
    @PostMapping("getKettleLine.shtml")
    public String getKettleLine(HttpServletRequest request) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        final HashMap<String, Object> resultMap = new HashMap<>();
        List<String> dateList = new ArrayList<String>();
        for (int i = -6; i < 1; i++) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateFormat = simpleDateFormat.format(instance.getTime());
            dateList.add(dateFormat);
        }
        resultMap.put("legend", dateList);
        Map<String, Object> transLine = transMonitorService.getTransLine(user.getuId());
        resultMap.put("trans", transLine);
        Map<String, Object> jobLine = jobMonitorService.getJobLine(user.getuId());
        resultMap.put("job", jobLine);
        log.info("[查询用户UserID:{},前7天内的作业和转换的数据:{}]", user.getuId(), JSON.toJSONString(resultMap));
        return ResultDto.success(resultMap);
    }

    /**
     * 获取转换的Top5 查询前5的转换数
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getTransList.shtml", method = RequestMethod.GET)
    public String getTransList(@RequestParam(value = "limit") Integer limit, @RequestParam(value = "offset") Integer offset, HttpServletRequest request) {
        log.info("[获取转换的Top5开始参数 limit={},offest={}]", limit, offset);
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage resultPage = transMonitorService.getListTop5(user.getuId());
        log.info("获取转换的Top5的详情:{}", JSON.toJSONString(resultPage.getRows()));
        return JSONUtils.objectToJson(resultPage);
    }

    /**
     * 获取作业的Top5 查询前5的作业数
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getJobList.shtml", method = RequestMethod.GET)
    public String getJobListTop5(@RequestParam(value = "limit") Integer limit, @RequestParam(value = "offset") Integer offset, HttpServletRequest request) {
        log.info("[获取作业的Top5开始 参数limit={},offest={}]", limit, offset);
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage jobPage = jobMonitorService.getJobListTop5(user.getuId());
        return JSONUtils.objectToJson(jobPage);
    }
}
