package com.lx.kettle.web.service;

import com.lx.kettle.core.model.KUser;

public interface UserService {
    /**
     * 登陆
     *
     * @param kUser
     * @return
     */
    KUser login(KUser kUser);

    /**
     * 判断是不是管理员
     *
     * @param uid
     * @return
     */
    Boolean isAdmin(Integer uid);

    Object getListByPage(Integer offset, Integer limit);

    void insert(KUser insertUser, Integer integer);
}
