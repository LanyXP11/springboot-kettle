package com.lx.kettle.web.service.impl;

import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.CommonUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KTransDao;
import com.lx.kettle.core.mapper.KTransMonitorDao;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.core.model.KTransMonitor;
import com.lx.kettle.web.service.TransMonitorService;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/***
 * create by chenjiang on 2019/10/19 0019
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class TransMonitorServiceImpl implements TransMonitorService {
    @Autowired
    private KTransMonitorDao kTransMonitorDao;
    @Autowired
    private KTransDao kTransDao;

    /**
     * @param uId
     * @return
     */
    @Override
    public Integer getAllMonitorTrans(Integer uId) {
        final KTransMonitor kTransMonitor = KTransMonitor.builder().addUser(uId).monitorStatus(1).build();
        return kTransMonitorDao.template(kTransMonitor).size();
    }

    /**
     * 获取用户7 天内的转换折现图
     *
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> getTransLine(Integer uid) {
        KTransMonitor template = new KTransMonitor();
        template.setAddUser(uid);
        List<KTransMonitor> kTransMonitorList = kTransMonitorDao.template(template);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            resultList.add(i, 0);
        }
        if (!kTransMonitorList.isEmpty()) {
            for (KTransMonitor KTransMonitor : kTransMonitorList) {
                String runStatus = KTransMonitor.getRunStatus();
                if (runStatus != null && runStatus.contains(",")) {
                    String[] startList = runStatus.split(",");
                    for (String startOnce : startList) {
                        String[] startAndStopTime = startOnce.split(Constant.RUNSTATUS_SEPARATE);
                        if (startAndStopTime.length != 2) {
                            continue;
                        }
                        //得到一次任务的起始时间和结束时间的毫秒值
                        resultList = CommonUtils.getEveryDayData(Long.parseLong(startAndStopTime[0]), Long.parseLong(startAndStopTime[1]), resultList);
                    }
                }
            }
        }
        resultMap.put("name", "转换");
        resultMap.put("data", resultList);
        return resultMap;
    }

    /**
     * 获取转换的Top5 不满足5条数据的按照当前数据计算
     *
     * @param uid 用户ID
     * @return BootTablePage
     */
    @Override
    public BootTablePage getListTop5(Integer userId) {
        KTransMonitor template = new KTransMonitor();
        template.setAddUser(userId);
        template.setMonitorStatus(1);
        List<KTransMonitor> kTransMonitorResultList = this.kTransMonitorDao.template(template);
        Collections.sort(kTransMonitorResultList);//对数据进行排序操作
        List resultLists = new ArrayList();
        if(!kTransMonitorResultList.isEmpty()){
            if(kTransMonitorResultList.size()>=5){
                List<KTransMonitor> kTransMonitors = kTransMonitorResultList.subList(0, 5);
                for (KTransMonitor kTransMonitor : kTransMonitors) {
                    KTrans kTrans = this.kTransDao.template(KTrans.builder().transId(kTransMonitor.getMonitorTrans()).build()).get(0);
                    kTransMonitor.setTransName(kTrans.getTransName());
                    resultLists.add(kTransMonitor);
                }
            }else{
                for (KTransMonitor kTransMonitor : kTransMonitorResultList) {
                    KTrans kTrans = this.kTransDao.template(KTrans.builder().transId(kTransMonitor.getMonitorTrans()).build()).get(0);
                    kTransMonitor.setTransName(kTrans.getTransName());
                    resultLists.add(kTransMonitor);
                }
            }
        }
        BootTablePage page = new BootTablePage();
        page.setRows(resultLists);
        page.setTotal(5);
        return page;
    }


}
