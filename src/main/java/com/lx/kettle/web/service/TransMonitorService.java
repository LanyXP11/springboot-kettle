package com.lx.kettle.web.service;

import com.lx.kettle.core.dto.BootTablePage;

import java.util.Map;

/***
 * create by chenjiang on 2019/10/19 0019
 */
public interface TransMonitorService {
    /**
     * 根据用户ID查询全部被监控的转换
     *
     * @param uId
     * @return
     */
    Integer getAllMonitorTrans(Integer uId);

    /**
     * 通过用户ID查询用户7天内的转换图
     *
     * @param uid
     * @return
     */
    Map<String, Object> getTransLine(Integer uid);

    /**
     * @param integer
     * @return
     */
    BootTablePage getListTop5(Integer integer);
}
