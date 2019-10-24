package com.lx.kettle.web.service.impl;

import com.lx.kettle.core.mapper.CategoryDao;
import com.lx.kettle.core.model.KCategory;
import com.lx.kettle.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiang on 2019/10/24
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * @param integer
     * @return
     */
    @Override
    public List getList(Integer userId) {
        ArrayList resultList = new ArrayList();
        List<KCategory> categoryList = categoryDao.template(KCategory.builder().addUser(userId).delFlag(1).build());
        if (!categoryList.isEmpty()) {
            resultList.addAll(categoryList);
        }
        return resultList;
    }
}
