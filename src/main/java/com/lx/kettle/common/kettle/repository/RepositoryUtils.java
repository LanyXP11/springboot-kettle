package com.lx.kettle.common.kettle.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.dto.kettle.RepositoryTree;
import com.lx.kettle.core.model.KRepository;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectory;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by chenjiang on 2019/10/25
 * <p>
 * 该工具类主要用于的是Kettle链接资源库的全局通用工具类
 * </p>
 */
@Slf4j
@SuppressWarnings("all")
public class RepositoryUtils {
    public final static Map<Integer, KettleDatabaseRepository> KettleDatabaseRepositoryCatch = new HashMap<>();

    /**
     * 链接方式
     * { "Native", "ODBC", "OCI", "Plugin", "JNDI", ",", }
     *
     * @return
     */
    public static String[] getDataBaseAccess() {
        String[] dataBaseAccess = DatabaseMeta.dbAccessTypeCode;
        return dataBaseAccess;
    }

    /**
     * @param kettleDatabaseRepository
     * @param path
     * @param allRepositoryTreeList
     * @return
     */
    public static List<RepositoryTree> getAllDirectoryTreeList(KettleDatabaseRepository kdRepository, String path, List<RepositoryTree> allRepositoryTreeList) throws Exception {
        //获取Job和Transformation和Directory的信息
        List<RepositoryTree> repositoryTreeList = getJobAndTrans(kdRepository, path);

        if (!CollectionUtil.isEmpty(repositoryTreeList)) {
            if (repositoryTreeList.size() != 0) {
                for (RepositoryTree repositoryTree : repositoryTreeList) {
                    if (!repositoryTree.isLasted()) {
                        getAllDirectoryTreeList(kdRepository, repositoryTree.getPath(), allRepositoryTreeList);  //如果有子Directory或者Job和Transformation。那么递归遍历
                        allRepositoryTreeList.add(repositoryTree);
                    } else {
                        allRepositoryTreeList.add(repositoryTree);
                    }
                }
            }
        }
        return allRepositoryTreeList;
    }

    /**
     * 获取Job和Transformation和Directory的信息
     *
     * @param kdRepository
     * @param path
     * @return
     */

    private static List<RepositoryTree> getJobAndTrans(KettleDatabaseRepository repository, String path) throws KettleException {
        //获取到当前路径
        RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory(path);
        List<RepositoryTree> repositoryTreeList = getDirectory(repository, rDirectory);
        List<RepositoryElementMetaInterface> li = repository.getJobAndTransformationObjects(rDirectory.getObjectId(), false);
        if (null != li) {
            for (RepositoryElementMetaInterface repel : li) {
                if (Constant.TYPE_JOB.equals(repel.getObjectType().toString())) {
                    RepositoryTree repositoryTree = new RepositoryTree();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Constant.TYPE_JOB).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    repositoryTree.setType(Constant.TYPE_JOB);
                    repositoryTree.setLasted(true);
                    repositoryTreeList.add(repositoryTree);
                } else if (Constant.TYPE_TRANS.equals(repel.getObjectType().toString())) {
                    RepositoryTree repositoryTree = new RepositoryTree();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Constant.TYPE_TRANS).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    repositoryTree.setType(Constant.TYPE_TRANS);
                    repositoryTree.setLasted(true);
                    repositoryTreeList.add(repositoryTree);
                }
            }
        }
        return repositoryTreeList;
    }

    /**
     * 连接资源库对象
     * <p>
     * 1.获取数据库元对象 KettleDatabaseRepositoryMeta
     * 2.通过数据库元对象获取资源库 KettleDatabaseRepository
     * 3.设置缓存
     * 4.返回数据库链接信息
     * </P>
     *
     * @param kRepository 连接资源库对象
     * @return 资源库连接信息
     */
    public static KettleDatabaseRepository connectionRepository(KRepository kRepository) {
        log.info("新增资源库开始参数:{}", JSON.toJSONString(kRepository));
        if (kRepository != null) {
            String repositoryType = kRepository.getRepositoryType();
            String databaseAccess = kRepository.getDatabaseAccess();
            String databaseHost = kRepository.getDatabaseHost();
            String databaseName = kRepository.getDatabaseName();
            String databasePort = kRepository.getDatabasePort();
            String databaseUsername = kRepository.getDatabaseUsername();
            String databasePassword = kRepository.getDatabasePassword();
            try {
                DatabaseMeta databaseMeta = new DatabaseMeta(null, repositoryType, databaseAccess, databaseHost, databaseName, databasePort, databaseUsername, databasePassword);
                KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
                repositoryInfo.setConnection(databaseMeta);
                KettleDatabaseRepository repository = new KettleDatabaseRepository();
                repository.init(repositoryInfo);
                repository.connect(kRepository.getRepositoryUsername(), kRepository.getRepositoryPassword());
                if (null != kRepository.getRepositoryId()) {
                    KettleDatabaseRepositoryCatch.put(kRepository.getRepositoryId(), repository);
                }
                return repository;
            } catch (KettleException e) {
                log.error("资源库链接出现异常info:{}", e);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param repository
     * @param rDirectory
     * @return List<RepositoryTree>
     * @throws KettleException
     * @Title getDirectory
     * @Description 获取Directory信息
     */
    private static List<RepositoryTree> getDirectory(KettleDatabaseRepository repository,
                                                     RepositoryDirectoryInterface rDirectory) throws KettleException {
        List<RepositoryTree> repositoryTreeList = new ArrayList<RepositoryTree>();
        if (null != repository && null != rDirectory) {
            RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree().findDirectory(rDirectory.getObjectId());
            if (rDirectory.getNrSubdirectories() > 0) {
                for (int i = 0; i < rDirectory.getNrSubdirectories(); i++) {
                    RepositoryDirectory subTree = tree.getSubdirectory(i);
                    RepositoryTree repositoryTree = new RepositoryTree();
                    repositoryTree.setId(subTree.getObjectId().toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(subTree.getName());
                    repositoryTree.setPath(subTree.getPath());
                    //判断是否还有子Directory或者Job和Transformation
                    List<RepositoryElementMetaInterface> RepositoryElementMetaInterfaceList =
                            repository.getJobAndTransformationObjects(subTree.getObjectId(), false);
                    if (subTree.getNrSubdirectories() > 0 || RepositoryElementMetaInterfaceList.size() > 0) {
                        repositoryTree.setLasted(false);
                    } else {
                        repositoryTree.setLasted(true);
                    }
                    repositoryTreeList.add(repositoryTree);
                }
            }
        }
        return repositoryTreeList;
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        KettleEnvironment.init();
        DatabaseMeta databaseMeta = new DatabaseMeta(null, "MYSQL", "Native",
                "127.0.0.1", "kettle",
                "3306", "root", "root");
        KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
        repositoryInfo.setConnection(databaseMeta);
        KettleDatabaseRepository repository = new KettleDatabaseRepository();
        repository.init(repositoryInfo);
        repository.connect("admin", "admin");
        //判断是否连接成功
        if (repository.isConnected()) {
            System.out.println("链接成功!!!!!!!!!!!!!");
        } else {
            System.out.println("链接失败");
        }
        List<RepositoryTree> allDirectoryTreeList = getAllDirectoryTreeList(repository, "/", new ArrayList<>());
        log.info("返回信息:{}", JSON.toJSONString(allDirectoryTreeList));


    }

}
