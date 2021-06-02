package com.hss;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 测试候选人
 */
public class TestCandidate {

    /**
     * 流程部署
     */
    @Test
    public void testDeployment(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.流程部署
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-candidate")
                .addClasspathResource("bpmn/evection-candidate/evection-candidate.bpmn")
                .addClasspathResource("bpmn/evection-candidate/evection-candidate.png")
                .deploy();
//        4.输出
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection-candidate", "1011");
//        4.输出
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程定义key=" + processInstance.getProcessDefinitionKey());
        System.out.println("商业key=" + processInstance.getBusinessKey());
        System.out.println("流程实例name=" + processInstance.getName());
    }
}
