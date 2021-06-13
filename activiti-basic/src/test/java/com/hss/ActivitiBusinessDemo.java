package com.hss;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key 到Activiti的表中
     * businessKey主要影响表：
     *  ACT_RU_EXECUTION
     *  ACT_HI_PROCINST
     */
    @Test
    public void addBusinessKey(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.启动流程的过程中，添加businessKey
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey("myEvection", "1002");
//        4.输出
        System.out.println("businessKey==" + instance.getBusinessKey());
    }

    /**
     * 查询流程实例
     * 数据来源：
     * ACT_RU_EXECUTION
     */
    @Test
    public void queryProcessInstance(){
//        1.定义流程key
        String processDefinitionKey = "myEvection";
//        2.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        3.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        4.获取流程实例列表
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
//        5.输出
        for(ProcessInstance processInstance : list){
            System.out.println("--------------------");
            System.out.println("流程实例id:" + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id:" + processInstance.getProcessInstanceId());
            System.out.println("是否执行完成:" + processInstance.isEnded());
            System.out.println("是否暂停:" + processInstance.isSuspended());
            System.out.println("是否活动标识:" + processInstance.getActivityId());
            System.out.println("BusinessKey:" + processInstance.getBusinessKey());
        }
    }

    /**
     * 全部流程实例挂起与激活
     * 操作的表：
     * ACT_RE_PROCDEF
     * ACT_RU_TASK
     * ACT_RU_EXECUTION
     */
    @Test
    public void suspendAllProcessInstance(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取reposityService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.查询流程定义，获取流程定义的查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
//        4.获取当前流程定义的实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
//        5.获取流程定义的id
        String processDefinitionId = processDefinition.getId();
//        判断是否挂起
        if(suspended){
//        6.如果是挂起状态，改为激活状态。参数1：流程定义id  参数2：是否激活  参数3：激活时间
            repositoryService.activateProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义：" + processDefinitionId + "，已激活");
        }else {
//        7.如果是激活状态，改为挂起状态。参数1：流程定义id  参数2：是否暂停  参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义：" + processDefinitionId + "，已挂起");
        }
    }

    /**
     * 挂起、激活单个流程实例
     * 操作表：
     * ACT_RU_TASK
     * ACT_RU_EXECUTION
     * ACT_RU_EXECUTION
     */
    @Test
    public void suspendSingleProcessInstance(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        3.通过RuntimeService获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey("1002", "myEvection")
                .singleResult();
//        4.得到当前流程实例的暂停状态，true-已暂停 false-激活
        boolean suspended = processInstance.isSuspended();
//        5.获取流程实例id
        String instanceId = processInstance.getId();
//        6.判断是否已暂停，如果已经暂停，就执行激活操作
        if(suspended){
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + "已经激活");
        }else{
//        7.如果是激活状态，就执行暂停操作
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + "已经挂起");
        }

    }

    /**
     * 尝试完成挂起的流程实例任务
     */
    @Test
    public void completTask(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取taskService
        TaskService taskService = processEngine.getTaskService();
//        3.使用taskService获取任务，参数 任务实例的id,负责人
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .processInstanceBusinessKey("1002")
                .taskAssignee("zhangsan")
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
