package com.hss.service;

import com.hss.entity.FlowInfo;
import com.hss.entity.User;
import com.hss.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ActFlowCommService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private EvectionService evectionService;

    /**
     * 部署流程定义
     */
    public void saveNewDeploy(FlowInfo flowInfo) {
//        1.流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(flowInfo.getFilepath()) // 添加bpmn资源
                .name(flowInfo.getFlowkey())
                .deploy();
//        2.输出部署信息
        log.info("流程部署id：{}", deployment.getId());
        log.info("流程部署名称：{}", deployment.getName());
    }

    /**
     * 删除流程定义
     */
    public void deleteDeploy(String processDefinitionKey,Boolean cascade){
//        1.获取流程部署对象
        Deployment deployment = repositoryService.createDeploymentQuery()
                .processDefinitionKey(processDefinitionKey)
                .singleResult();
//        2.校验流程定义信息
        if(null != deployment){
            log.info("流程部署id={}", deployment.getId());
//            3.删除流程
            repositoryService.deleteDeployment(deployment.getId(),cascade);
        }else{
            log.info("未找到流程定义");
        }
    }

    /**
     * 启动流程实例
     */
    public ProcessInstance startProcess(String formKey, String beanName, String bussinessKey, Long id){
        IActFlowCustomService customService = (IActFlowCustomService) SpringContextUtil.getBean(beanName);
//        1.修改业务的状态
        customService.startRunTask(id);
        Map<String, Object> variables = customService.setvariables(id);
//        2.启动流程
        log.info("【启动流程】，formKey ：{},bussinessKey:{}", formKey, bussinessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(formKey, bussinessKey, variables);
//        3.流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("【启动流程】- 成功，processDefinitionId：{}", processDefinitionId);
        return processInstance;
    }

    /**
     * 完成提交任务
     */
    public void completeProcess(String remark, String taskId, String userId, String beanName, Long id) {
        IActFlowCustomService customService = (IActFlowCustomService) SpringContextUtil.getBean(beanName);

        //任务Id 查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            log.error("completeProcess - task is null!!");
            return;
        }

        //任务对象  获取流程实例Id
        String processInstanceId = task.getProcessInstanceId();

        //设置审批人的userId
        Authentication.setAuthenticatedUserId(userId);

        //添加记录
        taskService.addComment(taskId, processInstanceId, remark);
        System.out.println("-----------完成任务操作 开始----------");
        System.out.println("任务Id=" + taskId);
        System.out.println("负责人id=" + userId);
        System.out.println("流程实例id=" + processInstanceId);
        //完成办理
        taskService.complete(taskId);
        System.out.println("-----------完成任务操作 结束----------");
        ProcessInstance processInstance = runtimeService    //与正在的任务相关的Service
                .createProcessInstanceQuery()    //创建流程实例查询对象
                .processInstanceId(processInstanceId)     //查询条件 -- 流程的实例id(流程的实例id在流程启动后的整个流程中是不改变的)
                .singleResult();
        if(null == processInstance){
            //修改业务的状态
            customService.endRunTask(id);
        }

    }

    /**
     * 查看个人任务列表
     */
    public List<Map<String, Object>> myTaskList(String userid, String bussinessKey) {

        /**
         * 根据负责人id  查询任务
         */
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(userid);

        if(!StringUtils.isEmpty(bussinessKey)){
            taskQuery.processInstanceBusinessKey(bussinessKey);
        }

        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (Task task : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", task.getId());
            map.put("taskname", task.getName());
            map.put("description", task.getDescription());
            map.put("priority", task.getPriority());
            map.put("owner", task.getOwner());
            map.put("assignee", task.getAssignee());
            map.put("delegationState", task.getDelegationState());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("createTime", task.getCreateTime());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("dueDate", task.getDueDate());
            map.put("category", task.getCategory());
            map.put("parentTaskId", task.getParentTaskId());
            map.put("tenantId", task.getTenantId());

            User userInfo = userService.findOneUserById(Long.valueOf(task.getAssignee()));
            map.put("assigneeUser", userInfo.getUsername());
            listmap.add(map);
        }

        return listmap;
    }

}
