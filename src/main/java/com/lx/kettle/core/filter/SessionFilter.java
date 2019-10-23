//package com.lx.kettle.core.filter;
//
//import com.lx.kettle.common.tootik.Constant;
//import com.lx.kettle.core.model.KUser;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * Created by chenjiang on 2019/10/21
// * <p>
// *      该Filer链路主要的作用在于服务端频繁获取Session值所做
// * </p>
// */
//@Slf4j
//public class SessionFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        //TODO
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            request = new SeessionWapper((HttpServletRequest) request);
//        } catch (Exception e) {
//            log.info("[获取Session出现异常],e", e);
//            writeMsg((HttpServletResponse) response, "系统异常请您联系管理员");
//        }
//        chain.doFilter(request, response);
//    }
//    @Override
//    public void destroy() {
//        //TODO
//    }
//    static class SeessionWapper extends HttpServletRequestWrapper {
//        String Body = "";
//        /**
//         * Constructs a request object wrapping the given request.
//         */
//        public SeessionWapper(HttpServletRequest request) {
//            super(request);
//            Object attribute = request.getSession().getAttribute(Constant.SESSION_ID);
//            if (attribute != null) {
//                KUser sessionUser = (KUser) attribute;
//                Body = sessionUser.toString();
//            }
//        }
//    }
//    public static void writeMsg(HttpServletResponse response, String msg) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charest=utf-8");
//        PrintWriter out = response.getWriter();
//        out.write(msg);
//        out.flush();
//
//    }
//}
