package com.hss.service;

import com.hss.entity.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //@Autowired
    //private EvectionService evectionService;

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
     * 参数流程定义
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
}
