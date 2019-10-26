package com.lx.kettle.common.kettle.repository;

import com.lx.kettle.core.model.KRepository;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/25
 * <p>
 *      该工具类主要用于的是Kettle链接资源库的全局通用工具类
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
     *      1.获取数据库元对象 KettleDatabaseRepositoryMeta
     *      2.通过数据库元对象获取资源库 KettleDatabaseRepository
     *      3.设置缓存
     *      4.返回数据库链接信息
     * </P>
     *
     * @param kRepository 连接资源库对象
     * @return 资源库连接信息
     */
    public static KettleDatabaseRepository connectionRepository(KRepository kRepository) {
        if (kRepository != null) {
            DatabaseMeta databaseMeta = new DatabaseMeta(null, kRepository.getRepositoryType(), kRepository.getDatabaseAccess(), kRepository.getDatabaseHost(), kRepository.getDatabaseName(), kRepository.getDatabasePort(), kRepository.getDatabaseUsername(), kRepository.getDatabasePassword());
            KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
            repositoryInfo.setConnection(databaseMeta);
            KettleDatabaseRepository repository = new KettleDatabaseRepository();
            repository.init(repositoryInfo);
            try {
                repository.connect(kRepository.getRepositoryUsername(), kRepository.getRepositoryPassword());
            } catch (KettleException e) {
                log.error("资源库链接出现异常info:{}", e);
                return null;
            }
            if (null != kRepository.getRepositoryId()) {
                KettleDatabaseRepositoryCatch.put(kRepository.getRepositoryId(), repository);
            }
            return repository;
        }
        return null;
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

    }

}
