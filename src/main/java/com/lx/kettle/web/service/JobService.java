package com.lx.kettle.web.service;

import com.lx.kettle.core.model.KJob;

public interface JobService {

    KJob getJobNamebyJobId(Integer jobId);
}
