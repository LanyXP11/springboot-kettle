package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/23
 * <p>
 * 用户
 * </p>
 */

@RestController
@RequestMapping("/user/")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping("getList.shtml")
    public String getList(HttpServletRequest request, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        log.info("查询用户列表开始 参数offset={},limit={}", offset, limit);
        return JSONUtils.objectToJson(userService.getListByPage(offset, limit));
    }

    /**
     * 添加用户
     *
     * @param request
     * @param kUser
     * @return
     */
    @RequestMapping("insert.shtml")
    public String insertUser(HttpServletRequest request, @RequestBody KUser insertUser) {
        log.info("添加用户开始 参数:{}", JSON.toJSONString(insertUser));
        KUser uu = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        userService.insert(insertUser, uu.getuId());
        return ResultDto.success();
    }
}
