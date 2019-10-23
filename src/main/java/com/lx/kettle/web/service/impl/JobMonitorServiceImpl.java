package com.lx.kettle.web.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.CommonUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KJobMonitorDao;
import com.lx.kettle.core.model.KJobMonitor;
import com.lx.kettle.web.service.JobMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/***
 * create by chenjiang on 2019/10/19 0019
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class JobMonitorServiceImpl implements JobMonitorService {
    @Autowired
    private KJobMonitorDao kJobMonitorDao;

    /**
     * @param uId 用户ID
     * @return 获取所有的监控作业
     */
    @Override
    public Integer getAllMonitorJob(Integer uId) {
        final KJobMonitor kJobMonitor = KJobMonitor.builder().addUser(uId).monitorStatus(1).build();
        return kJobMonitorDao.template(kJobMonitor).size();

    }

    /**
     * 获取用户7天内用户作业折线图
     *
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> getJobLine(Integer uid) {
        KJobMonitor kJobMonitor = KJobMonitor.builder().addUser(uid).build();
        List<KJobMonitor> kJobMonitorResultList = kJobMonitorDao.template(kJobMonitor);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            resultList.add(i, 0);
        }
        if (!kJobMonitorResultList.isEmpty()) {
            for (KJobMonitor t : kJobMonitorResultList) {
                String runStatus = t.getRunStatus();
                if (StringUtils.isNotBlank(runStatus) && runStatus.contains(",")) {
                    String[] startList = runStatus.split(",");
                    for (String s : startList) {
                        String[] startAndStopTime = s.split(Constant.RUNSTATUS_SEPARATE);
                        if (startAndStopTime.length == 2) {
                            resultList = CommonUtils.getEveryDayData(Long.parseLong(startAndStopTime[0]), Long.parseLong(startAndStopTime[1]), resultList);
                        }
                    }
                }
            }
        }
        resultMap.put("name", "作业");
        resultMap.put("data", resultList);
        return resultMap;
    }

    /**
     * 获取作业的Top5数据
     *
     * @param uId
     * @return
     */
    @Override
    public BootTablePage getJobListTop5(Integer uId) {
        KJobMonitor template = new KJobMonitor();
        template.setAddUser(uId);
        template.setMonitorStatus(1);
        List<KJobMonitor> kJobMonitorList = kJobMonitorDao.template(template);
        Collections.sort(kJobMonitorList);
        List<KJobMonitor> newKJobMonitorList = new ArrayList<KJobMonitor>();
        if (!kJobMonitorList.isEmpty() && kJobMonitorList.size() >= 5) {
            newKJobMonitorList = kJobMonitorList.subList(0, 5);
        } else {
            newKJobMonitorList = kJobMonitorList;
        }
        BootTablePage bootTablePage = new BootTablePage();
        bootTablePage.setRows(newKJobMonitorList);
        bootTablePage.setTotal(5);
        return bootTablePage;
    }
}
