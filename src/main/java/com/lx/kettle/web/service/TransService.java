package com.lx.kettle.web.service;

import com.lx.kettle.core.model.KTrans;

public interface TransService {
    /**
     * 根据TransId查询KTrans
     * @param transId
     * @return
     */
    KTrans getTransByTransId(Integer transId);
}
