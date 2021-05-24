package com.hss;

import com.hss.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 完成任务时设置变量
 */
public class TestVariablesComplete {

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

//        3.启动流程实例 1007--2D 1008--5D
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("test-global", "1008", map);
//        4.输出流程定义信息
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getBusinessKey());
    }

    /**
     * 完成任务时指定参数
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
                .processDefinitionKey("test-global")
                .processInstanceBusinessKey("1008")
                .taskAssignee(assignee)
                .singleResult();
//        4.判断任务是否存在
        if(null != task){
            String taskName = task.getName();
            if("部门经理审核".equals(taskName)){
                Evection evection = new Evection();
                evection.setNum(5D);
                Map<String,Object> map = new HashMap<>();
                map.put("evection",evection);
//                5.完成任务
                taskService.complete(task.getId(),map);
            }else{
                taskService.complete(task.getId());
            }
            System.out.println("完成任务");
        }else{
            System.out.println("没有查询到相关任务");
        }
    }
}
