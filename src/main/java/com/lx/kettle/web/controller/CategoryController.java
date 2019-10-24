package com.lx.kettle.web.controller;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by chenjiang on 2019/10/24
 */
@RestController
@RequestMapping("/category/")
@Slf4j
@SuppressWarnings("all")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表 主要是分成转换和作业
     *
     * @param request
     * @return
     */
    @RequestMapping("getSimpleList.shtml")
    public String simpleList(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        List list = categoryService.getList(kUser.getuId());
        log.info("查询当前用户userID={},分类详情为", kUser.getuId(), JSON.toJSONString(list));
        return JSONUtils.objectToJson(list);
    }
}
