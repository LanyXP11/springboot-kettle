package com.lx.kettle.web.controller;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.TransMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/21
 */
@RequestMapping("/trans/monitor/")
@RestController
@Slf4j
@SuppressWarnings("all")
public class TransMonitorController {
    @Autowired
    private TransMonitorService transMonitorService;

    /***
     * 获取在监控的转换数
     * @param request
     * @return
     */
    @PostMapping("getAllMonitorTrans.shtml")
    public String getAllMonitorTrans(HttpServletRequest request) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        final Integer allMonitorTransSize = transMonitorService.getAllMonitorTrans(user.getuId());
        return JSONUtils.objectToJson(allMonitorTransSize);

    }
}
