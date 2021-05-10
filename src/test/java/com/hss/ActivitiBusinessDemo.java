package com.hss;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key 到Activiti的表中
     * businessKey主要影响表：
     *  ACT_RU_EXECUTION
     *  ACT_HI_PROCINST
     */
    @Test
    public void addBusinessKey(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.启动流程的过程中，添加businessKey
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey("myEvection", "1002");
//        4.输出
        System.out.println("businessKey==" + instance.getBusinessKey());
    }
}
