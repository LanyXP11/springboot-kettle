package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KQuartzDao;
import com.lx.kettle.core.mapper.KTransDao;
import com.lx.kettle.core.model.KQuartz;
import com.lx.kettle.core.model.KTrans;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.quartz.QuartzManager;
import com.lx.kettle.web.service.TransService;
import com.lx.kettle.web.service.impl.biz.JobServiceBiz;
import com.lx.kettle.web.service.impl.biz.TransServiceBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.beetl.sql.core.DSTransactionManager;
import org.beetl.sql.core.db.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/23
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class TransServiceImpl implements TransService {
    @Autowired
    private KTransDao kTransDao;
    @Autowired
    private KQuartzDao kQuartzDao;
    @Autowired
    private TransServiceBiz transServiceBiz;

    /**
     * 获取转换列表
     *
     * @param start
     * @param size
     * @param categoryId
     * @param transName
     * @param userId
     * @return
     */
    @Override
    public BootTablePage getList(Integer start, Integer size, Integer categoryId, String transName, Integer userId) {
        KTrans template = KTrans.builder().addUser(userId).delFlag(1).build();
        if (categoryId != null) {
            template.setCategoryId(categoryId);
        }
        if (StringUtils.isNotEmpty(transName)) {
            template.setTransName(transName);
        }
        List<KTrans> kTransList = kTransDao.pageQuery(template, start, size);
        Long allCount = kTransDao.allCount(template);
        BootTablePage bootTablePage = new BootTablePage();
        bootTablePage.setRows(kTransList);
        bootTablePage.setTotal(allCount);
        return bootTablePage;
    }

    @Override
    public boolean check(Integer repositoryId, String transPath, KUser user) {
        KTrans template = KTrans.builder().addUser(user.getuId()).delFlag(1).transRepositoryId(repositoryId).transPath(transPath).build();
        List<KTrans> kTransList = kTransDao.template(template);
        if (null != kTransList && kTransList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void insert(KTrans kTrans, KUser user, String customerQuarz) throws SQLException {
        DSTransactionManager.start();
        kTrans.setAddUser(user.getuId());
        kTrans.setAddTime(new Date());
        kTrans.setEditUser(user.getuId());
        kTrans.setEditTime(new Date());
        kTrans.setDelFlag(1);
        kTrans.setTransStatus(2);//第一次添加默认是停止状态
        if (StringUtils.isNotBlank(customerQuarz)) {
            KQuartz kQuartz = KQuartz.builder()
                    .addUser(user.getuId())
                    .addTime(new Date())
                    .editTime(new Date())
                    .editUser(user.getuId())
                    .delFlag(1)
                    .quartzCron(customerQuarz)
                    .quartzDescription(kTrans.getTransName() + "的定时策略").build();
            KeyHolder kQuartzKey = kQuartzDao.insertReturnKey(kQuartz);
            kTrans.setTransQuartz(kQuartzKey.getInt());
        } else if (StringUtils.isBlank(customerQuarz) && new Integer(0).equals(kTrans.getTransQuartz())) {
            kTrans.setTransQuartz(1);
        } else if (StringUtils.isBlank(customerQuarz) && kTrans.getTransQuartz() == null) {
            kTrans.setTransQuartz(1);
        }
        kTransDao.insert(kTrans);
        DSTransactionManager.commit();
    }

    /**
     * @param transId
     * @return
     */
    @Override
    public Object getTransRunState(Integer transId) {
        KTrans kTrans = kTransDao.unique(transId);
        Map<String, String> quartzBasic = transServiceBiz.getQuartzBasic(kTrans);
        return QuartzManager.getTriggerState(quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"));
    }

    /**
     * @param transId 主键ID
     * @return Krans
     */
    @Override
    public KTrans getTransByTransId(Integer transId) {
        return kTransDao.single(transId);
    }

    @Override
    public Long getStartTaskCount(Integer categoryId, String transName, Integer uid) {
        KTrans template = KTrans.builder().addUser(uid).delFlag(1).transStatus(1).build();
        return taskCount(template, categoryId, transName);
    }

    @Override
    public Long getStopTaskCount(Integer categoryId, String transName, Integer uid) {
        KTrans template = KTrans.builder().addUser(uid).delFlag(1).transStatus(2).build();
        return taskCount(template, categoryId, transName);
    }


    private Long taskCount(KTrans trans, Integer categoryId, String transName) {
        if (categoryId != null) {
            trans.setCategoryId(categoryId);
        }
        if (StringUtils.isNotEmpty(transName)) {
            trans.setTransName(transName);
        }
        Long taskCount = kTransDao.allCount(trans);
        return taskCount;
    }
}
