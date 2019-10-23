package com.lx.kettle.web.controller;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.model.KUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/23
 * <p>
 * 基础Controller
 * </P>
 */
public abstract class BaseController {
    protected KUser getUserSession(HttpServletRequest request) {
        KUser attribute = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return attribute;
    }
}
