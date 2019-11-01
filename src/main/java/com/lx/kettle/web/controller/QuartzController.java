package com.lx.kettle.web.controller;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/24
 */
@RestController
@RequestMapping("/quartz/")
@Slf4j
@SuppressWarnings("all")
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    @RequestMapping("getQuartz.shtml")
    public String getQuartz(@RequestParam("quartzId") Integer quartzId) {
        return ResultDto.success(quartzService.getQuartz(quartzId));
    }

    /**
     * 获取定时调度策越
     *
     * @param request
     * @return
     */
    @RequestMapping("getSimpleList.shtml")
    public String simpleList(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JSONUtils.objectToJson(quartzService.getList(kUser.getuId()));
    }


}
