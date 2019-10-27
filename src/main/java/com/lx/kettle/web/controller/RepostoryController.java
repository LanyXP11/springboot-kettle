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
import java.util.Date;

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
        log.info("获取资源库列表返回参数:{}", JSONUtils.objectToJson(list));
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
        log.info("新增资源库-测试连接开始 参数是:{}", JSON.toJSONString(kRepository));
        //添加判断 TODO
        KRepository repositorys = KRepository.builder().build();
        BeanUtil.copyProperties(kRepository, repositorys);
        if (dataBaseRepositoryService.ckeck(repositorys)) {
            return ResultDto.success("success");
        } else {
            return ResultDto.success("fail");
        }
    }

    /**
     * 新增资源库
     *
     * @param request
     * @param kRepository
     * @return
     */
    @RequestMapping("insert.shtml")
    public String insert(HttpServletRequest request, @ModelAttribute KRepository kRepository) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
//        KRepository repositorys = KRepository.builder().delFlag(1).addUser(kUser.getuId()).editUser(kUser.getuId()).editTime(new Date()).addTime(new Date()).build();
        kRepository.setAddTime(new Date());
        kRepository.setDelFlag(1);
        kRepository.setEditTime(new Date());
        kRepository.setAddUser(kUser.getuId());
        kRepository.setEditUser(kUser.getuId());
        log.info("新增资源库参数:{}", kRepository);
        dataBaseRepositoryService.insert(kRepository);
        return ResultDto.success();
    }

}
