package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KJobDao;
import com.lx.kettle.core.model.KJob;
import com.lx.kettle.web.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 获取JOb作业
     *
     * @param offset
     * @param limit
     * @param categoryId
     * @param jobName
     * @param integer
     * @return
     */
    @Override
    public BootTablePage getJobListResultBooTablePage(Integer start, Integer size, Integer categoryId, String jobName, Integer uid) {
        KJob template = KJob.builder().addUser(uid).delFlag(1).build();
        if (categoryId != null) {
            template.setCategoryId(categoryId);
        }
        if (StringUtils.isNotBlank(jobName)) {
            template.setJobName(jobName);
        }
        List<KJob> kJobList = kJobDao.pageQuery(template, start, size);
        long allCount = kJobDao.allCount(template);
        return BootTablePage.builder().rows(kJobList).total(allCount).build();
    }
}
