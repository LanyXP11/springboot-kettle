package com.lx.kettle.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.DataBaseRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/***
 * create by chenjiang on 2019/10/26 0026
 */
@RestController
@RequestMapping("/repository/database/")
@Slf4j
@SuppressWarnings("all")
public class RepostoryController {
    @Autowired
    private DataBaseRepositoryService dataBaseRepositoryService;

    /**
     * 获取资源库列表
     *
     * @param offset
     * @param limit
     * @param request
     * @return
     */
    @RequestMapping("getList.shtml")
    public String getList(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = null;
        list = dataBaseRepositoryService.getList(offset, limit, kUser.getuId());
        return JSONUtils.objectToJson(list);
    }

    @RequestMapping("getType.shtml")
    public String getType() {
        return JSONUtils.objectToJson(dataBaseRepositoryService.getRepositoryTypeList());
    }

    @RequestMapping("getAccess.shtml")
    public String getAccess() {
        return JSONUtils.objectToJson(dataBaseRepositoryService.getAccess());
    }

    /**
     * 新增资源库测试连接
     *
     * @param kRepository
     * @param request
     * @return
     */
    @RequestMapping("ckeck.shtml")
    public String check(@ModelAttribute KRepository kRepository, HttpServletRequest request) {
        log.info("新增资源库测试连接开始 参数是:{}", JSON.toJSONString(kRepository));
        //添加判断 TODO
        KRepository repositorys = KRepository.builder().build();
        BeanUtil.copyProperties(kRepository,repositorys);
        if (dataBaseRepositoryService.ckeck(repositorys)){
            return ResultDto.success("success");
        }else {
            return ResultDto.success("fail");
        }
    }

}
