package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.mapper.KJobDao;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.web.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenjiang on 2019/10/23
 */
@Service(value = "jobServiceImpl")
@Slf4j
@SuppressWarnings("all")
public class JobServiceImpl implements JobService {
    @Autowired
    private KJobDao kJobDao;

    /**
     * 根据JobId获取Job详情
     *
     * @param jobId
     * @return
     */
    @Override
    public KJob getJobNamebyJobId(Integer jobId) {
        return kJobDao.single(jobId);
    }
}
