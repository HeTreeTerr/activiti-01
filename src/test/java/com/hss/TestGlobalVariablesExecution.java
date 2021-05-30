package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 在执行流程实例中指定变量
 */
public class TestGlobalVariablesExecution {

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
//        3.启动流程实例 10009--2D 1010--3D
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("test-global", "1010", map);

//        4.输出
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getBusinessKey());
    }

    /**
     * 设置global变量由执行实例编号
     */
    @Test
    public void setGlobalVariableByExecutionId(){

//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.获取执行实例
        Execution execution = runtimeService.createExecutionQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1010")
                .singleResult();
        if(null != execution){
//            设置单个变量
            Evection evection = new Evection();
            evection.setNum(3D);
//            通过执行实例设置变量
//            runtimeService.setVariable(execution.getId(),"evection",evection);

//            设定多个变量
            Map<String,Object> map = new HashMap<>();
            map.put("evection",evection);
            runtimeService.setVariables(execution.getId(),map);

            System.out.println("设置变量成功！");
        }else{
            System.out.println("没有找到执行流程实例");
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
//        3.完成任务
        String assignee = "li_global";

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1010")
                .taskAssignee(assignee)
                .singleResult();
//        4.判断是否存在任务，有则完成任务
        if(null != task){
            System.out.println(task.getId());
            taskService.complete(task.getId());
            System.out.println("完成任务！");
        }else{
            System.out.println("没有查询到相关信息");
        }
    }
}
