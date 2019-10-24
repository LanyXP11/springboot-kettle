package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.mapper.KQuartzDao;
import com.lx.kettle.core.model.KQuartz;
import com.lx.kettle.web.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
