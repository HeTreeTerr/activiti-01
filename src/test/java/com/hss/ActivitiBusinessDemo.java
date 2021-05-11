package com.hss;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.List;

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

    /**
     * 查询流程实例
     * 数据来源：
     * ACT_RU_EXECUTION
     */
    @Test
    public void queryProcessInstance(){
//        1.定义流程key
        String processDefinitionKey = "myEvection";
//        2.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        3.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        4.获取流程实例列表
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
//        5.输出
        for(ProcessInstance processInstance : list){
            System.out.println("--------------------");
            System.out.println("流程实例id:" + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id:" + processInstance.getProcessInstanceId());
            System.out.println("是否执行完成:" + processInstance.isEnded());
            System.out.println("是否暂停:" + processInstance.isSuspended());
            System.out.println("是否活动标识:" + processInstance.getActivityId());
            System.out.println("BusinessKey:" + processInstance.getBusinessKey());
        }
    }
}
