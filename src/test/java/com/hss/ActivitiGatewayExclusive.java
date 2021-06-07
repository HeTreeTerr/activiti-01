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
 * 排它网关测试（存在多个分支，只能且必须执行一个分支）
 * 分支结果：
 * 都是false会抛异常
 * 都是true换选择编号较小的分支
 * 一个fals一个true会选择true分支
 */
public class ActivitiGatewayExclusive {

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
                .name("排它网关测试")
                .addClasspathResource("bpmn/evection-exclusive/evection-exclusive.bpmn")
                .addClasspathResource("bpmn/evection-exclusive/evection-exclusive.png")
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
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.指定出差基本信息
        Evection evection = new Evection();
        evection.setNum(4D);

        Map<String, Object> map = new HashMap<>();
//        指定负责人
        map.put("assignee0","zhao_global");
        map.put("assignee1","qian_global");
        map.put("assignee2","sun_global");
        map.put("assignee3","li_global");

        map.put("evection",evection);

//        4.启动实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("evection-exclusive", "1014", map);

//        5.输出
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程定义key=" + processInstance.getProcessDefinitionKey());
        System.out.println("流程实例商业key=" + processInstance.getBusinessKey());
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查询任务
        String assignee = "li_global";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-exclusive")
                .processInstanceBusinessKey("1014")
                .taskAssignee(assignee)
                .singleResult();
//        4.判断并完成任务
        if(null != task){
            String taskId = task.getId();
            taskService.complete(taskId);
            System.out.println("办理成功！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
    }
}
