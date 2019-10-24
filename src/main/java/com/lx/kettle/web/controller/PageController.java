package com.lx.kettle.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by chenjiang on 2019/10/18
 * <p>
 * 路由转发
 * </P>
 */
@RequestMapping("/view/")
@Controller
public class PageController {
    /**
     * 首页转发
     *
     * @return
     */
    @RequestMapping("indexUI.shtml")
    public String IndexUI() {
        return "index";
    }

    @RequestMapping("mainUI.shtml")
    public String mainUI() {
        return "main";
    }

    /**
     * 跳转登录页面
     *
     * @param errorMsg
     * @param session
     * @return
     */
    @RequestMapping("loginUI.shtml")
    public String loginUI(@ModelAttribute("errorMsg") String errorMsg, HttpSession session) {
        session.setAttribute("errorMsg", errorMsg);
        return "login";
    }

    /*************************user************************/
    @RequestMapping("user/listUI.shtml")
    public Object userList() {
        return "user/list";
    }

    @RequestMapping("user/addUI.shtml")
    public String userAddUI() {
        return "user/add";
    }

    @RequestMapping("user/editUI.shtml")
    public String userEditUI(Integer uId, Model model) {
        model.addAttribute("uId", uId);
        return "user/edit";
    }

    /*************************job************************/
    @RequestMapping("job/listUI.shtml")
    public String jobListUI() {
        return "job/list";
    }

    @RequestMapping("job/rAddUI.shtml")
    public String jobRAddUI() {
        return "job/r-add";
    }

    @RequestMapping("job/fAddUI.shtml")
    public String jobFAddUI() {
        return "job/f-add";
    }

    @RequestMapping("job/editUI.shtml")
    public String jobEditUI(Integer jobId, Model model) {
        model.addAttribute("jobId", jobId);
        return "job/edit";
    }

    /*************************category************************/
    @RequestMapping("category/listUI.shtml")
    public String categoryListUI() {
        return "category/list";
    }

    @RequestMapping("category/addUI.shtml")
    public String categoryAddUI() {
        return "category/add";
    }

    @RequestMapping("category/editUI.shtml")
    public String categoryEditUI(Integer categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return "category/edit";
    }
}
