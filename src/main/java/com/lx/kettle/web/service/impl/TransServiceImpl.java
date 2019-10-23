package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.mapper.KTransDao;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.web.service.TransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenjiang on 2019/10/23
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class TransServiceImpl implements TransService {
    @Autowired
    private KTransDao kTransDao;

    /**
     * @param transId 主键ID
     * @return Krans
     */
    @Override
    public KTrans getTransByTransId(Integer transId) {
        return kTransDao.single(transId);
    }
}
