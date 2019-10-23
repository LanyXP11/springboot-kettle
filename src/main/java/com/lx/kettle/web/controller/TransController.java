package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.web.service.TransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/23
 */
@RestController
@RequestMapping("/trans/")
@Slf4j
@SuppressWarnings("all")
public class TransController {
    @Autowired
    private TransService transService;

    /**
     * 根据transId查询TransName
     *
     * @param request
     * @param transId
     * @return
     */
    @PostMapping(value = "getTrans.shtml/{transId}")
    public String getTransByTransId(HttpServletRequest request, @PathVariable("transId") Integer transId) {
        log.info("根据TransId查询TransName开始参数TransId={}", transId);
        KTrans kTrans = this.transService.getTransByTransId(transId);
        log.info("根据TransId查询TransName最终返回值:{}", JSON.toJSONString(ResultDto.success(kTrans)));
        return ResultDto.success(kTrans);
    }
}
