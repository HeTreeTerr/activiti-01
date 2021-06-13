package com.hss;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestListener {

    /**
     * 流程部署
     */
    @Test
    public void testDeployment(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库中
        Deployment deploy = repositoryService.createDeployment()
                .name("测试监听器")
                .addClasspathResource("bpmn/demo-listener/demo-listener.bpmn")
                .addClasspathResource("bpmn/demo-listener/demo-listener.png")
                .deploy();
        //4.输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startDemoListener(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("testListener", "1004");

        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getName());
    }

    /**
     * 完成任务
     */
    @Test
    public void completTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.使用taskService获取任务，参数 任务实例的id,负责人
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("testListener")
                .processInstanceBusinessKey("1004")
                .taskAssignee("zhao_si")
                .singleResult();

        if(null != task){
            System.out.println("流程实例id==" + task.getProcessDefinitionId());
            System.out.println("流程任务id==" + task.getId());
            System.out.println("负责人==" + task.getAssignee());
            System.out.println("任务名称==" + task.getName());
//            4.根据任务id完成任务
            taskService.complete(task.getId());
        }else{
            System.out.println("没有查找到任务！");
        }
    }
}
