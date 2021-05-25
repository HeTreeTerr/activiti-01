package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过当前任务设置参数
 */
public class TestVariablesTask {

    /**
     * 启动流程实例
     */
    @Test
    public void startProcess(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> map = new HashMap<>();
//        指定负责人
        map.put("assignee0","zhao_global");
        map.put("assignee1","qian_global");
        map.put("assignee2","sun_global");
        map.put("assignee3","li_global");
//        3.启动流程实例  1011--2D  1012--5D
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("test-global", "1012", map);
//        4.输出
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getProcessDefinitionKey());
    }

    /**
     * 通过当前任务设定参数
     */
    @Test
    public void setGlobalVariableByTaskId(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.查找task
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1012")
                .taskAssignee("zhao_global")
                .singleResult();

//        4.设定参数
        if(null != task){
            String taskId = task.getId();

            Evection evection = new Evection();
            evection.setNum(5D);
//            设定参数
            taskService.setVariable(taskId,"evection",evection);

            taskService.getVariable("","");
//            批量设定参数
            /*Map<String,Object> map = new HashMap<>();
            map.put("evection",evection);
            taskService.setVariables(taskId,null);*/

            System.out.println("成功设定参数！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
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
        String assingnee = "li_global";

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1012")
                .taskAssignee(assingnee)
                .singleResult();
//        4.完成任务
        if(null != task){
            String taskId = task.getId();
            taskService.complete(taskId);
            System.out.println("完成任务！");
        }else{
            System.out.println("没有查询到相关信息！");
        }
    }

    @Test
    public void queryHistory(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取historyService
        HistoryService historyService = processEngine.getHistoryService();
//        3.查询历史
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1012")
                .list();

//        4.输出历史任务信息
        for(HistoricTaskInstance historicTaskInstance : taskInstances){
            System.out.println("--------------------->");
            System.out.println(historicTaskInstance.getName());
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getEndTime());
        }
    }
}
