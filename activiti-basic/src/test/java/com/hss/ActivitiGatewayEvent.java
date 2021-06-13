package com.hss;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * 事件网关
 */
public class ActivitiGatewayEvent {

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
                .name("出差流程审批-事件网关")
                .addClasspathResource("bpmn/evection-event-gateway/evection-event-gateway.bpmn")
                .addClasspathResource("bpmn/evection-event-gateway/evection-event-gateway.png")
                .deploy();
//        4.输出
        System.out.println("流程定义:id=" + deploy.getId());
        System.out.println("流程定义:name=" + deploy.getName());
        System.out.println("流程定义:key=" + deploy.getKey());
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
//        3.启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("evection-event-gateway", "1024");
//        4.输出
        System.out.println("流程实例:name=" + processInstance.getId());
        System.out.println("流程定义:key=" + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例:商务key=" + processInstance.getBusinessKey());
        System.out.println("流程实例:name=" + processInstance.getName());
    }

    /**
     * 办理任务
     */
    @Test
    public void completeTask() throws InterruptedException, IOException {
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查询任务
        String assignee = "Mr.zhao";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-event-gateway")
                .processInstanceBusinessKey("1024")
                .taskAssignee(assignee)
                .singleResult();
//        4.输出
        if(null != task){
            System.out.println("任务:name=" + task.getName());
            System.out.println("任务:assignee=" + task.getAssignee());
            taskService.complete(task.getId());
            System.out.println("任务办理成功！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
        System.in.read();
    }

    /**
     * 触发信号事件
    */
    @Test
    public void completeSingal(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.查询执行器
        List<Execution> executions =  processEngine.getRuntimeService().createExecutionQuery()
                .signalEventSubscriptionName("Singal")
                .list();
//        3.遍历
        for(Execution e:executions){
//            4.触发事件
            processEngine.getRuntimeService().signalEventReceived("Singal", e.getId());

        }
    }

    /**
     * 触发消息事件
     */
    @Test
    public void completeMessage(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.查询执行器
        List<Execution> executions =  processEngine.getRuntimeService().createExecutionQuery()
                .messageEventSubscriptionName("Message")
                .list();
//        3.遍历
        for(Execution e:executions){
//            4.触发事件
            processEngine.getRuntimeService().messageEventReceived("Message", e.getId());

        }

    }
}
