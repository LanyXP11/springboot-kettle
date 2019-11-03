package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.TransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

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
     * 首页查询
     *
     * @param offset
     * @param limit
     * @param categoryId
     * @param transName
     * @param request
     * @return
     */
    @RequestMapping("getList.shtml")
    public String getList(@RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset,
                          @RequestParam("categoryId") Integer categoryId, @RequestParam("transName") String transName, HttpServletRequest request) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = transService.getList(offset, limit, categoryId, transName, user.getuId());
        log.info("转换首页加载数据返回结果集合:{}", JSON.toJSONString(list));
        return JSONUtils.objectToJson(list);
    }

    /**
     * 获取运行中和停止的转换
     *
     * @param categoryId
     * @param transName
     * @param request
     * @return
     */
    @RequestMapping("getStartTaskCount.shtml")
    public String getStartTaskCount(@RequestParam("categoryId") Integer categoryId, @RequestParam("transName") String transName, HttpServletRequest request) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        Long startTaskCount = transService.getStartTaskCount(categoryId, transName, user.getuId());
        log.info("查询正在运行的转换条数：{}", startTaskCount);
        return JSONUtils.objectToJson(startTaskCount);
    }

    @RequestMapping("getStopTaskCount.shtml")
    public String getStopTaskCount(@RequestParam("categoryId") Integer categoryId, @RequestParam("transName") String transName, HttpServletRequest request) {
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        Long stopTaskCount = transService.getStartTaskCount(categoryId, transName, user.getuId());
        log.info("查询停止的的转换条数：{}", stopTaskCount);
        return JSONUtils.objectToJson(stopTaskCount);
    }

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

    /**
     * 添加转换操作
     *
     * @param kTrans
     * @param customerQuarz
     * @param request
     * @return
     */
    @RequestMapping("insert.shtml")
    public String insert(@ModelAttribute KTrans kTrans, String customerQuarz, HttpServletRequest request) {
        log.info("添加转换开始参数KTrans={},customerQuarz={}", JSON.toJSONString(kTrans), customerQuarz);
        KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        if (transService.check(kTrans.getTransRepositoryId(), kTrans.getTransPath(), user)) {
            try {
                transService.insert(kTrans, user, customerQuarz);
                return ResultDto.success();
            } catch (Exception e) {
                log.info("添加转换失败 msg:{}", e);
                return ResultDto.fail("添加失败,请联系管理员");
            }
        } else {
            return ResultDto.fail("该作业已经添加过了,不可以重复添加");
        }
    }

    /**
     * 查询转换的定时任务状态
     *
     * @param transId
     * @return
     */
    @RequestMapping({"getTransRunState.shtml"})
    public String getTransRunState(@RequestParam("transId") Integer transId) {
        return JSONUtils.objectToJson(this.transService.getTransRunState(transId));
    }
    /*@RequestMapping("startAll.shtml")
    public String startAll(Integer categoryId, String transName, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        transService.startAll(categoryId, transName, kUser.getuId());
        return ResultDto.success();
    }

    @RequestMapping("stopAll.shtml")
    public String stopAll(Integer categoryId, String transName, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        transService.stopAll(categoryId, transName, kUser.getuId());
        return ResultDto.success();
    }*/
}
