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
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.启动流程实例
        Map<String,Object> map = new HashMap<>();
        Evection evection = new Evection();
//        1018--4D  1019--2D
        evection.setNum(2D);
        map.put("evection",evection);
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("evection-parallel", "1019", map);
//        4.输出
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程实例key=" + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例商业key=" + processInstance.getBusinessKey());
    }

    /**
     * 任务办理
     */
    @Test
    public void completeTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查询任务
        String assignee = "Mr.qian";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-parallel")
                .processInstanceBusinessKey("1019")
                .taskAssignee(assignee)
                .singleResult();
        if(null != task){
            System.out.println(task.getName());
            taskService.complete(task.getId());
            System.out.println("办理成功！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
    }
}
