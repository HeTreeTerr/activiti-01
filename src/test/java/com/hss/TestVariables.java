package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动流程时设置变量
 */
public class TestVariables {

    /**
     * 部署流程
     */
    @Test
    public void testDeploy(){
//        获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取repositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        流程部署
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-global-test")
                .addClasspathResource("bpmn/evection-global/evection-global.bpmn")
                .addClasspathResource("bpmn/evection-global/evection-global.png")
                .deploy();
//        部署信息
        System.out.println(deploy.getId());
        System.out.println(deploy.getKey());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程
     * 使用uel表达式指定任务负责人
     * 和出差天数
     */
    @Test
    public void testStartProcess(){
//        获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

//        指定出差基本信息
        Evection evection = new Evection();
        evection.setNum(3D);

        Map<String, Object> map = new HashMap<>();
//        指定负责人
        map.put("assignee0","zhao_global");
        map.put("assignee1","qian_global");
        map.put("assignee2","sun_global");
        map.put("assignee3","li_global");

        map.put("evection",evection);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test-global", "1006", map);

        System.out.println("实例Id=" + processInstance.getId());
        System.out.println("流程定义key=" + processInstance.getProcessDefinitionKey());
        System.out.println("商业key=" + processInstance.getBusinessKey());
    }

    /**
     * 删除流程定义
     * 批量删除流程实例
     */
    @Test
    public void deleteProcess(){
//        流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        获取流程部署对象
        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentName("出差申请流程-global-test")
                .singleResult();

        if(null != deployment){
            System.out.println("流程部署id=" + deployment.getId());
//          删除流程
          repositoryService.deleteDeployment(deployment.getId(),true);
        }else{
            System.out.println("未找到流程定义");
        }

    }

    /**
     * 完成个人任务
     */
    @Test
    public void complateTask(){
//        获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取taskService
        TaskService taskService = processEngine.getTaskService();
//        获取待办任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1006")
                .taskAssignee("li_global")
                .singleResult();

        if(null != task){
            System.out.println(task.getId());
            System.out.println(task.getName());
//            完成任务
            taskService.complete(task.getId());
            System.out.println("完成任务！");
        }else{
            System.out.println("没有找到任务！");
        }
    }

    /**
     * 查找历史信息
     */
    @Test
    public void queryHistory(){
//        获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取historyService
        HistoryService historyService = processEngine.getHistoryService();

//      流程实例编号：  62501（两天）  72501（三天）
        List<HistoricTaskInstance> instanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("72501")
                .list();

        if(null != instanceList && !instanceList.isEmpty()){
            System.out.println(instanceList.size());
            for(HistoricTaskInstance historicTaskInstance : instanceList){
                System.out.println("任务名称："+ historicTaskInstance.getName());
                System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
                System.out.println("任务完成时间：" + historicTaskInstance.getEndTime());
            }
        }else{
            System.out.println("查询信息为空！");
        }
    }
}
