package com.hss;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
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

    /**
     * 全部流程实例挂起与激活
     * 操作的表：
     * ACT_RE_PROCDEF
     * ACT_RU_TASK
     * ACT_RU_EXECUTION
     */
    @Test
    public void suspendAllProcessInstance(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取reposityService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.查询流程定义，获取流程定义的查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
//        4.获取当前流程定义的实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
//        5.获取流程定义的id
        String processDefinitionId = processDefinition.getId();
//        判断是否挂起
        if(suspended){
//        6.如果是挂起状态，改为激活状态。参数1：流程定义id  参数2：是否激活  参数3：激活时间
            repositoryService.activateProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义：" + processDefinitionId + "，已激活");
        }else {
//        7.如果是激活状态，改为挂起状态。参数1：流程定义id  参数2：是否暂停  参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义：" + processDefinitionId + "，已挂起");
        }
    }
}
