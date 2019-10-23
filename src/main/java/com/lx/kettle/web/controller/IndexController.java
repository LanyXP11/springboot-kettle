package com.lx.kettle.web.controller;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjiang on 2019/10/21
 * <p>
 *      首页
 * </p>
 */
@Controller
@RequestMapping("/index/")
public class IndexController {
    @Autowired
    private UserService userService;
    /**
     * 判断是否为管理员
     * 当前 登录比较简单 只支持单用户即admin用户一人登录
     * @param request
     * @return
     */
    @PostMapping("isAdmin.shtml")
    @ResponseBody
    public String isAdmin(HttpServletRequest request) {
        final KUser user = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        if (user != null && userService.isAdmin(user.getuId())) {
            return JSONUtils.objectToJson(true);
        }
        return JSONUtils.objectToJson(false);
    }

    /**
     * 登录页面
     * @param uAccount
     * @param uPassword
     * @param attr
     * @param request
     * @return
     */
    @RequestMapping("login.shtml")
    public String login(@RequestParam(value = "uAccount") String uAccount,
                        @RequestParam(value = "uPassword") String uPassword,
                        RedirectAttributes attr, HttpServletRequest request) {
        if (StringUtils.isBlank(uAccount) || StringUtils.isBlank(uPassword)) {
            attr.addFlashAttribute("errorMsg", "账号或密码不能为空");
            return "redirect:/view/loginUI.shtml";
        }
        KUser user = KUser.builder().uAccount(uAccount).uPassword(uPassword).build();
        KUser u = userService.login(user);
        if (null != u) {
            request.getSession().setAttribute(Constant.SESSION_ID, u);
            return "redirect:/view/indexUI.shtml";
        }
        attr.addFlashAttribute("errorMsg", "账号或密码错误");
        return "redirect:/view/loginUI.shtml";
    }
}
