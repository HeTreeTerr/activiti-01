package com.hss;

import org.activiti.engine.*;
import org.junit.Test;

public class TestCreate {

    /**
     * 使用activiti提供的默认方式
     * 来创建mysql的表
     */
    @Test
    public void testCreateDbTable(){
        //需要使用activiti提供的工具类 ProcessEngines,使用方法 getDefaultProcessEngine
        //getDefaultProcessEngine 会默认从resource下读取名字为activiti.cfg.xml的文件
        //创建processEngine时，就会创建mysql的表

        //默认方式
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("processEngine--" + processEngine);

        //部署对应的服务
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //任务对应的服务
        TaskService taskService = processEngine.getTaskService();
        //运行时的服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //
        ManagementService managementService = processEngine.getManagementService();
        //历史数据对应的服务
        HistoryService historyService = processEngine.getHistoryService();

    }

    /**
     * 一般创建方式创建ProcessEngine对象
     */
    @Test
    public void testCreateGeneralProcessEngine(){
        /*
            自定义方式
            优点:1.自定义配置文件名 2.可以自定义bean的名字
         */
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        //获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        System.out.println("processEngine--" + processEngine);
    }
}
