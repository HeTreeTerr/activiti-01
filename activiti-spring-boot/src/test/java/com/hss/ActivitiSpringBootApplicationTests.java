package com.hss;

import com.hss.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.GetTasksPayloadBuilder;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.GetTasksPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActivitiSpringBootApplicationTests {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void testActBoot(){
        log.info(taskRuntime.toString());
    }

    /**
     * 获取所有可用的流程定义信息
     */
    @Test
    public void getProcessDefinition() {
//        1.模拟用户登录
        securityUtil.logInAs("system");
//        2.查询可用的流程定义信息
        Page<ProcessDefinition> processDefinitionPage = processRuntime
                .processDefinitions(Pageable.of(0, 10));
//        3.输出
        log.info("可用流程定义数量:" + processDefinitionPage.getTotalItems());
        for(ProcessDefinition pd : processDefinitionPage.getContent()){
            log.info("流程定义:{}", pd);
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
//        1.模拟用户登录
        securityUtil.logInAs("system");
//        2.启动流程实例
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("activiti-demo")
                .withBusinessKey("2001")
                .build());
//        3.输出
        log.info("流程实例:id={}",processInstance.getId());
        log.info("流程实例:key={}",processInstance.getProcessDefinitionKey());
        log.info("流程实例:businessKey={}",processInstance.getBusinessKey());
    }

    /**
     * 查询任务并完成自己的任务
     */
    @Test
    public void testTask(){
//        1.模拟用户登录
        securityUtil.logInAs("jack");
//        2.分页查询待办任务
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
//        3.判断并输出
        if(taskPage.getTotalItems() > 0){
            for(Task task : taskPage.getContent()){
//                输出
                log.info("task:id={}",task.getId());
                log.info("task:name={}",task.getName());
//                拾取任务
                taskRuntime.claim(TaskPayloadBuilder
                        .claim()
                        .withTaskId(task.getId())
                        .build()
                );
//                办理任务
                taskRuntime.complete(TaskPayloadBuilder
                        .complete()
                        .withTaskId(task.getId())
                        .build()
                );
                log.info("");
            }
        }else{
            log.info("没有查找到相关信息！");
        }
    }
}
