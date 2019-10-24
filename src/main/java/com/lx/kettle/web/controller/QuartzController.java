package com.lx.kettle.web.controller;

import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.web.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String getQuartz(@RequestParam("quartzId") Integer quartzId){
        return ResultDto.success(quartzService.getQuartz(quartzId));
    }


}
