package com.lx.kettle.common.kettle.kettleinit;

import com.lx.kettle.common.tootik.Constant;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.JndiUtil;
import org.pentaho.di.core.KettleVariablesList;
import org.pentaho.di.core.auth.AuthenticationConsumerPluginType;
import org.pentaho.di.core.auth.AuthenticationProviderPluginType;
import org.pentaho.di.core.compress.CompressionPluginType;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.lifecycle.KettleLifecycleSupport;
import org.pentaho.di.core.logging.ConsoleLoggingEventListener;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogTablePluginType;
import org.pentaho.di.core.plugins.*;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.step.RowDistributionPluginType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by chenjiang on 2019/10/25
 * <p>
 * Kettl初始化环境类-自定义
 * </p>
 */
@SuppressWarnings("all")
@Slf4j
public class KettleInit {

    private static Class<?> PKG = Constant.class;
    private static Boolean initialized;

    /**
     * 初始化Kettle环境。 此方法将尝试配置 简单的JNDI， 通过简单地调用init(true)。
     *
     * @throws KettleException
     */
    public static void init() throws KettleException {
        init(true);
    }

    /**
     * @param simpleJndi
     * @throws KettleException
     */
    public static void init(boolean simpleJndi) throws KettleException {
        if (initialized == null) {
            // 创建一个Kettle "home" 的目录 TODO 初始化 kettle.properties 初始化其他属性等
//            createKettleHome();
            //初始化kettle原生的环境
            environmentInit();
            //初始化日志
            KettleLogStore.init();
            KettleLogStore.getAppender().addLoggingEventListener(new ConsoleLoggingEventListener());
            if (simpleJndi) {
                JndiUtil.initJNDI();//单机运行，本地调试的时候用
            }
            // 注册原生类型和各个所需的插件
            PluginRegistry.addPluginType(RowDistributionPluginType.getInstance());
            PluginRegistry.addPluginType(LogTablePluginType.getInstance());
            PluginRegistry.addPluginType(CartePluginType.getInstance());
            PluginRegistry.addPluginType(CompressionPluginType.getInstance());
            PluginRegistry.addPluginType(AuthenticationProviderPluginType.getInstance());
            PluginRegistry.addPluginType(AuthenticationConsumerPluginType.getInstance());
            PluginRegistry.addPluginType(StepPluginType.getInstance());
            PluginRegistry.addPluginType(PartitionerPluginType.getInstance());
            PluginRegistry.addPluginType(JobEntryPluginType.getInstance());
            PluginRegistry.addPluginType(RepositoryPluginType.getInstance());
            PluginRegistry.addPluginType(DatabasePluginType.getInstance());
            PluginRegistry.addPluginType(LifecyclePluginType.getInstance());
            PluginRegistry.addPluginType(KettleLifecyclePluginType.getInstance());
            PluginRegistry.addPluginType(ImportRulePluginType.getInstance());
            PluginRegistry.init();
            KettleVariablesList.init();
            // 初始化生命周期监听器
            initLifecycleListeners();
            initialized = true;
        }
    }

    private static void initLifecycleListeners() throws KettleException {
        final KettleLifecycleSupport s = new KettleLifecycleSupport();
        s.onEnvironmentInit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                s.onEnvironmentShutdown();
            } catch (Exception e) {
                System.err.println(BaseMessages.getString(PKG, ">>>>>>>>>>LifecycleSupport.ErrorInvokingKettleEnvironmentShutdownListeners>>>>>>>>>"));
                e.printStackTrace();
            }
        }));
    }

    /**
     * 创建Kettle home,这是一个目录,其中包含一个默认的 kettle.properties 属性文件等
     */
    public static void createKettleHome() {
        String directory = Constant.getKettleDirectory();
        File dir = new File(directory);
        try {
            dir.mkdirs();
            createDefaultKettleProperties(directory);
        } catch (Exception e) {
        }
    }

    /**
     * 创建默认的 kettle properties 文件
     *
     * @param 目录 目录
     */
    private static void createDefaultKettleProperties(String directory) {
        String kpFile = directory + Constant.FILE_SEPARATOR + Constant.UKETTLE;
        File file = new File(kpFile);
        if (!file.exists()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                out.write(Constant.getKettlePropertiesFileHeader().getBytes());
            } catch (IOException e) {
                System.err.println(BaseMessages.getString(PKG, "Props.Log.Error.UnableToCreateDefaultKettleProperties.Message", Constant.UKETTLE, kpFile));
                System.err.println(e.getStackTrace());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        System.err.println(BaseMessages.getString(PKG, "Props.Log.Error.UnableToCreateDefaultKettleProperties.Message", Constant.UKETTLE, kpFile));
                        System.err.println(e.getStackTrace());
                    }
                }
            }
        }
    }
    /**
     * Checks if the Kettle environment has been initialized.
     *
     * @return true if initialized, false otherwise
     */
    /**
     * 检查Kettle 环境初始化。
     *
     * @return 返回 true or false
     */
    public static boolean isInitialized() {
        if (initialized == null)
            return false;
        else
            return true;
    }

    /**
     * 初始化kettle环境
     *
     * @throws KettleException
     * @See EnvUtil
     */
    public static void environmentInit() throws KettleException {
        if (Thread.currentThread().getContextClassLoader() == null) {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
        }
        Map<?, ?> prop = Constant.readProperties();
        Variables variables = new Variables();
        prop.forEach((k, v) -> {
            variables.setVariable(String.valueOf(k), variables.environmentSubstitute(String.valueOf(v)));
        });
        for (String variable : variables.listVariables()) {
            System.setProperty(variable, variables.getVariable(variable));
        }
        /**
         * this is a system peopertis is kettle environmentInit fucntion set
         */
        System.getProperties().put("KETTLE_HOME", Constant.KETTLE_HOME);
        System.getProperties().put("KETTLE_PLUGIN_BASE_FOLDERS", Constant.KETTLE_PLUGIN);
        System.getProperties().put("KETTLE_JS_HOME", Constant.KETTLE_SCRIPT);
        System.getProperties().put(Constant.INTERNAL_VARIABLE_CLUSTER_SIZE, "1");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_SLAVE_SERVER_NUMBER, "0");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_SLAVE_SERVER_NAME, "slave-trans-name");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_COPYNR, "0");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_NAME, "step-name");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_PARTITION_ID, "partition-id");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_PARTITION_NR, "0");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_UNIQUE_COUNT, "1");
        System.getProperties().put(Constant.INTERNAL_VARIABLE_STEP_UNIQUE_NUMBER, "0");
    }


}
