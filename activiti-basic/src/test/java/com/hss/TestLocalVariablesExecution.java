package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务办理时设定loacl参数
 */
public class TestLocalVariablesExecution {

    /**
     * 启动流程实例
     */
    @Test
    public void startProcess(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> map = new HashMap<>();
//        指定负责人
        map.put("assignee0","zhao_global");
        map.put("assignee1","qian_global");
        map.put("assignee2","sun_global");
        map.put("assignee3","li_global");

//        3.启动流程实例
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("test-global", "1013", map);

//        4.输出
        System.out.println(processInstance.getProcessDefinitionKey());
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getProcessVariables());
    }

    /**
     * 设定local参数
     */
    @Test
    public void setLocalVariables(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

//        3.设定请假天数
        Map<String,Object> map = new HashMap<>();

        Evection evection = new Evection();
        evection.setNum(2D);
        map.put("evection",evection);

        Execution execution = runtimeService.createExecutionQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1013")
                .singleResult();

        if(null != execution){
            System.out.println(execution.getId());
//            4.设定local变量
            runtimeService.setVariableLocal(execution.getId(),"evection",evection);
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
//        3.查询任务信息
        String assignee = "li_global";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1013")
                .taskAssignee(assignee)
                .singleResult();

//        4.输出任务
        if(null != task){
            System.out.println(task.getId());
            System.out.println(task.getName());
//            5.完成任务
            taskService.complete(task.getId());
        }else{
            System.out.println("没有查找到相关信息！");
        }
    }

    /**
     * 创建历史任务查询对象
     */
    @Test
    public void queryHistory(){
//        1.获取规则引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取historyService
        HistoryService historyService = processEngine.getHistoryService();
//        3.查询
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1013")
                .includeTaskLocalVariables().list();
//        4.输出
        for(HistoricTaskInstance historicTaskInstance : list){
            System.out.println("-----------------------");
            System.out.println("任务id:" + historicTaskInstance.getId());
            System.out.println("任务名称：" + historicTaskInstance.getName());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务local变量：" + historicTaskInstance.getTaskLocalVariables());
        }
    }
}
