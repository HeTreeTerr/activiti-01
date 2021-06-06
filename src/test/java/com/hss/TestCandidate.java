package com.hss;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

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

    /**
     * 查询组任务
     */
    @Test
    public void findGroupTaskList(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取
        TaskService taskService = processEngine.getTaskService();
//        3.查询组任务 lisi,wangwu
        String candidateUser = "wangwu";
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("evection-candidate")
                .processInstanceBusinessKey("1011")
                .taskCandidateUser(candidateUser)
                .list();
//        4.遍历输出
        for(Task task : taskList){
            System.out.println("----------------------------");
            System.out.println("流程实例ID=" + task.getProcessInstanceId());
            System.out.println("任务id=" + task.getId());
            System.out.println("任务负责人" + task.getAssignee());
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查询待办任务
        String assignee = "zhangsan";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-candidate")
                .processInstanceBusinessKey("1011")
                .taskAssignee(assignee)
                .singleResult();

        if(null != task){
            taskService.complete(task.getId());
            System.out.println("任务处理成功！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
    }
}
