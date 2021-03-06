package com.lx.kettle.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by chenjiang on 2019/10/18
 * <p>
 * 页面转发
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
    //****************************************************************************//
    @RequestMapping("user/listUI.shtml")
    public Object userList() {
        return "user/list";
    }
    @RequestMapping("user/addUI.shtml")
    public String userAddUI(){
        return "user/add";
    }
    @RequestMapping("user/editUI.shtml")
    public String userEditUI(Integer uId, Model model){
        model.addAttribute("uId", uId);
        return "user/edit";
    }
}
