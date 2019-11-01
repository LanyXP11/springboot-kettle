package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.mapper.KQuartzDao;
import com.lx.kettle.core.model.KQuartz;
import com.lx.kettle.web.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiang on 2019/10/24
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private KQuartzDao kQuartzDao;

    @Override
    public KQuartz getQuartz(Integer quartzId) {
        return kQuartzDao.single(quartzId);
    }

    /**
     * 获取定时策略列表
     *
     * @param uId
     * @return
     */
    @Override
    public Object getList(Integer uId) {
        List<KQuartz> resultList = new ArrayList<KQuartz>();
        resultList.addAll(kQuartzDao.template(KQuartz.builder().delFlag(1).addUser(uId).build()));
        return resultList;
    }
}
