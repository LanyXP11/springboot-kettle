package com.lx.kettle.core.conf;

import com.lx.kettle.common.tootik.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chenjiang on 2019/10/21
 * <p>
 *      用户登录拦截器
 * </p>
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LonginInterceptor()).addPathPatterns("/**");
    }
    private class LonginInterceptor implements HandlerInterceptor {
        /**
         * 在业务处理器处理请求之前被调用
         *
         * @param request
         * @param response
         * @param handler
         * @return
         * @throws Exception
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            Object sessionObje = request.getSession().getAttribute(Constant.SESSION_ID);
//            log.info("[拦截器开始作用用户请求路径URL={},SESSION_ID={}]", request.getRequestURI(), JSON.toJSONString(sessionObje));
            String url = request.getRequestURI();
            if(url.contains("view/indexUI.shtml")){
                if (sessionObje == null) {
                    response.sendRedirect(request.getContextPath() + "/view/loginUI.shtml");
                    return false;
                }
            }
            if (!url.contains("view/loginUI.shtml") && url.contains("index/login.shtml")) {
                return true;
            }
            return true;

        }

        /**
         * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
         * 可在modelAndView中加入数据，比如当前时间
         *
         * @param request
         * @param response
         * @param handler
         * @param modelAndView
         * @throws Exception
         */
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
            //TODO
        }

        /**
         * DispatcherServlet完全处理完请求后被调用,可用于清理资源等
         *
         * @param request
         * @param response
         * @param handler
         * @param ex
         * @throws Exception
         */
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
            //TODO
        }
    }


}
