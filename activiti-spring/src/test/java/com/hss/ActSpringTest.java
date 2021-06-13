package com.hss;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:activiti-spring.xml")
public class ActSpringTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Test
    public void testRep(){
        System.out.println(repositoryService);
    }

    @Test
    public void testTask(){
//        1.查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myEvectionUel")
                .processInstanceBusinessKey("1003")
                .list();
//        2.遍历输出
        for(Task task : list){
            System.out.println("任务:id=" + task.getId());
            System.out.println("任务:name=" + task.getName());
            System.out.println("任务:assignee=" + task.getAssignee());
        }
    }
}
