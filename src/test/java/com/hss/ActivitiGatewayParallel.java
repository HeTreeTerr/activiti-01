package com.hss;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

/**
 * 并行网关测试(无视自定义的规则，要求所有的分支都必须执行完毕，才会执行下一步任务)
 */
public class ActivitiGatewayParallel {

    /**
     * 流程部署
     */
    @Test
    public void testDeploy(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.流程部署
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请-evection-parallel")
                .addClasspathResource("bpmn/evection-parallel/evection-parallel.bpmn")
                .addClasspathResource("bpmn/evection-parallel/evection-parallel.png")
                .deploy();
//        4.输出
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署key=" + deploy.getKey());
        System.out.println("流程部署name=" + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    }
}
