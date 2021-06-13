package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 包含网关特点：
 *  既可以像排它网关那样设定分支条件
 *  又可以向并行网关那样无脑执行无条件的所有分支
 */
public class ActivitiGatewayInclusive {

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
                .name("出差流程审批-包含网关")
                .addClasspathResource("bpmn/evection-inclusive/evection-inclusive.bpmn")
                .addClasspathResource("bpmn/evection-inclusive/evection-inclusive.png")
                .deploy();
//        4.输出
        System.out.println("流程部署:id=" + deploy.getId());
        System.out.println("流程部署:name=" + deploy.getName());
        System.out.println("流程部署:key=" + deploy.getKey());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstall(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.设定参数
        Map<String,Object> map = new HashMap<>();
        Evection evection = new Evection();
//        1020--4D  1021--2D
        evection.setNum(2D);
        map.put("evection",evection);
//        4.启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("evection-inclusive", "1021", map);
//        5.输出
        System.out.println("流程实例:id=" + processInstance.getId());
        System.out.println("流程定义:key=" + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例:businessKey=" + processInstance.getBusinessKey());
    }

    /**
     * 办理任务
     */
    @Test
    public void completeTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查询任务
        String assignee = "Mr.li";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-inclusive")
                .processInstanceBusinessKey("1021")
                .taskAssignee(assignee)
                .singleResult();
//        4.校验并办理
        if(null != task){
            System.out.println("任务:id=" + task.getId());
            System.out.println("任务:name=" + task.getName());
            System.out.println("任务:assignee=" + task.getAssignee());
            taskService.complete(task.getId());
            System.out.println(task.getAssignee() + "成功办理任务！");
        }else{
            System.out.println("没有查找到相关信息！");
        }
    }

    /**
     * 删除流程定义
     * 批量删除流程实例
     */
    @Test
    public void deleteProcess(){
//        1.流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.获取流程部署对象
        Deployment deployment = repositoryService.createDeploymentQuery()
                .processDefinitionKey("evection-candidate")
                .singleResult();

        if(null != deployment){
            System.out.println("流程部署id=" + deployment.getId());
//          4.删除流程
            repositoryService.deleteDeployment(deployment.getId(),true);
        }else{
            System.out.println("未找到流程定义");
        }

    }
}
