package com.lx.kettle.web.service;

import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.model.KRepository;

/***
 * create by chenjiang on 2019/10/26 0026
 */
public interface DataBaseRepositoryService {
    BootTablePage getList(Integer offset, Integer limit, Integer integer);

    Object getRepositoryTypeList();

    Object getAccess();

    boolean ckeck(KRepository kRepository);

    void insert(KRepository repositorys);

    Object getKRepositoryById(Integer repositoryId);

    void delete(Integer repositoryId);
}
