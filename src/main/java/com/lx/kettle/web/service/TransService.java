package com.lx.kettle.web.service;

import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.core.model.KUser;

import java.sql.SQLException;

public interface TransService {
    /**
     * 根据TransId查询KTrans
     * @param transId
     * @return
     */
    KTrans getTransByTransId(Integer transId);

    Long getStartTaskCount(Integer categoryId, String transName, Integer integer);

    Long getStopTaskCount(Integer categoryId, String transName, Integer integer);

    BootTablePage getList(Integer offset, Integer limit, Integer categoryId, String transName, Integer userId);

    boolean check(Integer transRepositoryId, String transPath, KUser user);

    void insert(KTrans kTrans, KUser user, String customerQuarz) throws SQLException;

    Object getTransRunState(Integer transId);

    void start(Integer transId);

    void stop(Integer transId);
}
