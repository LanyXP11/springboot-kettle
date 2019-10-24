package com.lx.kettle.web.service;

import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.model.KJob;

public interface JobService {

    KJob getJobNamebyJobId(Integer jobId);

    BootTablePage getJobListResultBooTablePage(Integer offset, Integer limit, Integer categoryId, String jobName, Integer integer);
}
