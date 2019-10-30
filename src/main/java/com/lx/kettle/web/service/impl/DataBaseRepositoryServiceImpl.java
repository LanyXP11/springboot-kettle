package com.lx.kettle.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.kettle.repository.RepositoryUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.mapper.KRepositoryDao;
import com.lx.kettle.core.mapper.KRepositoryTypeDao;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.core.model.KRepositoryType;
import com.lx.kettle.web.service.DataBaseRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * create by chenjiang on 2019/10/26 0026
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class DataBaseRepositoryServiceImpl implements DataBaseRepositoryService {
    @Autowired
    private KRepositoryDao kRepositoryDao;
    @Autowired
    private KRepositoryTypeDao kRepositoryTypeDao;

    /**
     * 获取资源库列表分页
     *
     * @param offset
     * @param limit
     * @param uId
     * @return
     */
    @Override
    public BootTablePage getList(Integer offset, Integer limit, Integer uId) {
        log.info("");
        KRepository kRepository = KRepository.builder().addUser(uId).delFlag(1).build();
        List<KRepository> kRepositoryList = kRepositoryDao.template(kRepository);
        long allCount = kRepositoryDao.templateCount(kRepository);
        BootTablePage bootTablePage = new BootTablePage();
        bootTablePage.setRows(kRepositoryList);
        bootTablePage.setTotal(allCount);
        return bootTablePage;
    }

    /**
     * 获取资源库列表
     *
     * @return
     */
    @Override
    public List<KRepositoryType> getRepositoryTypeList() {
        return kRepositoryTypeDao.all();
    }

    /**
     * @return String[]
     * @Title getAccess
     * @Description 获取资源库访问类型
     */
    public String[] getAccess() {
        return RepositoryUtils.getDataBaseAccess();
    }

    /**
     * 新增资源库测试校验
     *
     * @param kRepository
     * @return
     */
    @Override
    public boolean ckeck(KRepository kRepository) {
        KettleDatabaseRepository kettleDatabaseRepository = RepositoryUtils.connectionRepository(kRepository);
        if (kettleDatabaseRepository != null) {
            if (kettleDatabaseRepository.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void insert(KRepository repositorys) {
        log.info("新增资源库参数:{}", JSON.toJSONString(repositorys));
        kRepositoryDao.insert(repositorys);
    }

    /**
     * 没有返回null
     *
     * @param repositoryId
     * @return
     */
    @Override
    public KRepository getKRepositoryById(Integer repositoryId) {
        return kRepositoryDao.single(repositoryId);
    }

    /**
     * 根据ID删除 逻辑删除
     *
     * @param repositoryId
     */
    @Override
    public void delete(Integer repositoryId) {
        KRepository kRepository = kRepositoryDao.unique(repositoryId);
        kRepository.setDelFlag(0);
        kRepositoryDao.updateById(kRepository);
    }

    @Override
    public Object getListByUid(Integer uId) {
        KRepository kRepository = new KRepository();
        kRepository.setAddUser(uId);
        kRepository.setDelFlag(1);
        return kRepositoryDao.template(kRepository);
    }
}
