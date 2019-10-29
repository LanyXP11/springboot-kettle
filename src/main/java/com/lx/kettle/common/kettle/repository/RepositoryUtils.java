package com.lx.kettle.common.kettle.repository;

import com.alibaba.fastjson.JSON;
import com.lx.kettle.core.model.KRepository;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/25
 * <p>
 * 该工具类主要用于的是Kettle链接资源库的全局通用工具类
 * </p>
 */
@Slf4j
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
//                KettleEnvironment.init();//TODO放到后面 项目启动初始化kettle环境
                DatabaseMeta databaseMeta = new DatabaseMeta(null,
                        repositoryType, databaseAccess, databaseHost,
                        databaseName, databasePort, databaseUsername, databasePassword);
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
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) throws KettleException {
//        KRepository kRepository = new KRepository();
//        kRepository.setRepositoryId(1);
//        kRepository.setRepositoryName("repository");
//        kRepository.setRepositoryUsername("admin");
//        kRepository.setRepositoryPassword("admin");
//        kRepository.setRepositoryType("MYSQL");
//        kRepository.setDatabaseAccess("Native");
//        kRepository.setDatabaseHost("localhost");
//        kRepository.setDatabasePort("3306");
//        kRepository.setDatabaseName("kettle");
//        kRepository.setDatabaseUsername("root");
//        kRepository.setDatabasePassword("123456");
//        KettleEnvironment.init();
//        KettleDatabaseRepository kettleDatabaseRepository = connectionRepository(kRepository);
//        List<RepositoryTree> allRepositoryTreeList = new ArrayList<RepositoryTree>();
//        List<RepositoryTree> repositoryTreeList = getAllDirectoryTreeList(kettleDatabaseRepository, "/", allRepositoryTreeList);
//        for (RepositoryTree repositoryTree : repositoryTreeList){
//            System.out.println(repositoryTree);
//        }
        KettleEnvironment.init();
        DatabaseMeta databaseMeta = new DatabaseMeta(null,
                "MYSQL", "Native",
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
    }

}
