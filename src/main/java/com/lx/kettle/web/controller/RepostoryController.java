package com.lx.kettle.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.common.utils.JSONUtils;
import com.lx.kettle.core.dto.BootTablePage;
import com.lx.kettle.core.dto.ResultDto;
import com.lx.kettle.core.dto.kettle.RepositoryTree;
import com.lx.kettle.core.model.KRepository;
import com.lx.kettle.core.model.KUser;
import com.lx.kettle.web.service.DataBaseRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/***
 * create by chenjiang on 2019/10/26 0026
 */
@RestController
@RequestMapping("/repository/database/")
@Slf4j
@SuppressWarnings("all")
public class RepostoryController {
    @Autowired
    private DataBaseRepositoryService dataBaseRepositoryService;

    /**
     * 获取资源库中的job
     *
     * @param request
     * @param repositoryId
     * @return
     */
    @RequestMapping("getJobTree.shtml")
    public String getJobTreeByRepositoryId(HttpServletRequest request, @RequestParam("repositoryId") Integer repositoryId) {
        try {
            List<RepositoryTree> repositoryTreeList = dataBaseRepositoryService.getTreeList(repositoryId);
            List<RepositoryTree> newRepositoryTreeList = new ArrayList();
            if (CollectionUtil.isEmpty(repositoryTreeList)) {
                log.info("获取资源库JOBTree 为空 返回数据");
                return null;
            }
            for (RepositoryTree repositoryTree : repositoryTreeList) {
                if ("0".equals(repositoryTree.getParent())) {
                    repositoryTree.setParent("#");
                }
                if (repositoryTree.getId().indexOf("@") > 0) {
                    repositoryTree.setIcon("none");
                }
                if (Constant.TYPE_TRANS.equals(repositoryTree.getType())) {
                    Map<String, String> stateMap = new HashMap<String, String>();
                    stateMap.put("disabled", "false");
                    repositoryTree.setState(stateMap);
                }
                newRepositoryTreeList.add(repositoryTree);
            }
            log.info("获取资源库JOBTree返回结果集:{}", JSONUtils.objectToJson(newRepositoryTreeList));
            return JSONUtils.objectToJson(newRepositoryTreeList);
        } catch (Exception e) {
            log.info("获取资源库JOBTree出现异常:{}", e);
            return null;
        }

    }

    /**
     * 获取资源库中的转换
     *
     * @param repositoryId
     * @return
     */
    @RequestMapping("getTransTree.shtml")
    public String getTransTree(Integer repositoryId) {
        try {
            List<RepositoryTree> repositoryTreeList = dataBaseRepositoryService.getTreeList(repositoryId);
            List<RepositoryTree> newRepositoryTreeList = new ArrayList<RepositoryTree>();
            for (RepositoryTree repositoryTree : repositoryTreeList) {
                if ("0".equals(repositoryTree.getParent())) {
                    repositoryTree.setParent("#");
                }
                if (repositoryTree.getId().indexOf("@") > 0) {
                    repositoryTree.setIcon("none");
                }
                if (Constant.TYPE_JOB.equals(repositoryTree.getType())) {
                    Map<String, String> stateMap = new HashMap<String, String>();
                    stateMap.put("disabled", "false");
                    repositoryTree.setState(stateMap);
                }
                newRepositoryTreeList.add(repositoryTree);
            }
            log.info("获取资源库TransTree返回结果集:{}", JSONUtils.objectToJson(newRepositoryTreeList));
            return JSONUtils.objectToJson(newRepositoryTreeList);
        } catch (Exception e) {
            log.info("获取资源库Trans出现异常:{}", e);
        }
        return null;
    }


    /**
     * 获取资源库列表
     *
     * @param offset
     * @param limit
     * @param request
     * @return
     */
    @RequestMapping("getList.shtml")
    public String getList(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = null;
        list = dataBaseRepositoryService.getList(offset, limit, kUser.getuId());
        log.info("获取资源库列表返回参数:{}", JSONUtils.objectToJson(list));
        return JSONUtils.objectToJson(list);
    }

    @RequestMapping("getType.shtml")
    public String getType() {
        return JSONUtils.objectToJson(dataBaseRepositoryService.getRepositoryTypeList());
    }

    @RequestMapping("getAccess.shtml")
    public String getAccess() {
        return JSONUtils.objectToJson(dataBaseRepositoryService.getAccess());
    }

    /**
     * 新增资源库测试连接
     *
     * @param kRepository
     * @param request
     * @return
     */
    @RequestMapping("ckeck.shtml")
    public String check(@ModelAttribute KRepository kRepository, HttpServletRequest request) {
        log.info("新增资源库-测试连接开始 参数是:{}", JSON.toJSONString(kRepository));
        //添加判断 TODO
        KRepository repositorys = KRepository.builder().build();
        BeanUtil.copyProperties(kRepository, repositorys);
        if (dataBaseRepositoryService.ckeck(repositorys)) {
            return ResultDto.success("success");
        } else {
            return ResultDto.success("fail");
        }
    }

    /**
     * 新增资源库
     *
     * @param request
     * @param kRepository
     * @return
     */
    @RequestMapping("insert.shtml")
    public String insert(HttpServletRequest request, @ModelAttribute KRepository kRepository) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        //        KRepository repositorys = KRepository.builder().delFlag(1).addUser(kUser.getuId()).editUser(kUser.getuId()).editTime(new Date()).addTime(new Date()).build();
        kRepository.setAddTime(new Date());
        kRepository.setDelFlag(1);
        kRepository.setEditTime(new Date());
        kRepository.setAddUser(kUser.getuId());
        kRepository.setEditUser(kUser.getuId());
        log.info("新增资源库参数:{}", kRepository);
        dataBaseRepositoryService.insert(kRepository);
        return ResultDto.success();
    }

    /**
     * 根据repositoryId查询
     *
     * @param repositoryId
     * @return
     */
    @RequestMapping("getKRepository.shtml")
    public String getKRepository(@RequestParam("repositoryId") Integer repositoryId) {
        return ResultDto.success(dataBaseRepositoryService.getKRepositoryById(repositoryId));
    }

    @RequestMapping("delete.shtml")
    public String delete(@RequestParam("repositoryId") Integer repositoryId) {
        dataBaseRepositoryService.delete(repositoryId);
        return ResultDto.success();
    }

    @RequestMapping("getSimpleList.shtml")
    public String getSimpleList(HttpServletRequest request) {
        try {
            KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
            return JSONUtils.objectToJson(dataBaseRepositoryService.getListByUid(kUser.getuId()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
