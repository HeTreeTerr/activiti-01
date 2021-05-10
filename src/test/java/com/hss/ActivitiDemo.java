package com.hss;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

    /**
     * 测试部署流程
     * 踩坑记录：
     * 执行流程部署报错，提示开始事件必须有执行器监听
     * 为StartEvent配置ExampleExecutionListenerOne后，成功部署
     * 操作表：
     *  act_re_deployment 部署表
     *  act_re_procdef 流程定义表
     *  act_ge_bytearray 资源表
     */
    @Test
    public void testDeployment(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库中
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        //4.输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    /**
     * 启动流程实例
     * act_hi_actinst 流程实例执行历史
     * act_hi_identitylink 流程参与者的历史信息
     * act_hi_procinst 流程实例的历史信息
     * act_hi_taskinst 任务的历史信息
     * act_ru_execution 流程执行的信息
     * act_ru_identitylink 流程参与者信息
     * act_ru_task 任务信息
     */
    @Test
    public void testSterProcess(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        //4.输出内容
        System.out.println("流程定义ID" + instance.getProcessDefinitionId());
        System.out.println("流程实例ID" + instance.getId());
        System.out.println("当前活动的ID" + instance.getActivityId());
    }

    /**
     * 查询个人待办任务列表
     *
     * SELECT DISTINCT
     * 	RES.*
     * FROM
     * 	ACT_RU_TASK RES
     * INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_ = D.ID_
     * WHERE
     * 	RES.ASSIGNEE_ = 'zhangsan'
     * AND D.KEY_ = 'myEvection'
     * ORDER BY
     * 	RES.ID_ ASC
     * LIMIT 2147483647 OFFSET 0;
     * 列表数据来自于：ACT_RU_TASK
     */
    @Test
    public void testFindPersonalTaskList(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务的负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") //要查询的负责人
                .list();
        //4.输出
        for(Task task : taskList){
            System.out.println("----------------------");
            System.out.println("流程实例id=" + task.getProcessDefinitionId());
            System.out.println("任务id=" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
        }
    }

    /**
     * 完成个人任务
     * 操作表：
     * ACT_HI_TASKINST 历史的任务实例
     * ACT_HI_ACTINST 历史的流程实例
     * ACT_HI_IDENTITYLINK 历史的流程运行过程中用户关系
     * ACT_RU_TASK 运行时任务
     * ACT_RU_IDENTITYLINK 运行时用户关系信息，存储任务节点与参与者的相关信息
     * ACT_HI_TASKINST(update) END_TIME_赋值 完成操作
     * ACT_HI_ACTINST(update) END_TIME_赋值 完成操作
     * ACT_RU_EXECUTION(update)
     * ACT_RU_EXECUTION(update)
     * ACT_RU_TASK(delete) 删除上一步的任务
     */
    @Test
    public void completTask(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取操作任务的服务 TaskService
        TaskService taskService = processEngine.getTaskService();
        //3.完成任务，参数：任务id
        //taskService.complete("22507");

        //优化，由条件获取任务信息。人后完成任务
        //张三体提交申请
        /*Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") //要查询的负责人
                .singleResult();*/

        //经理审批
        /*Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("jerry") //要查询的负责人
                .singleResult();*/

        //总经理审批
        /*Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("jack") //要查询的负责人
                .singleResult();*/

        //财务审批
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("rose") //要查询的负责人
                .singleResult();

        if(null != task && null != task.getId() && !"".equals(task.getId())){
            System.out.println("流程实例id=" + task.getProcessDefinitionId());
            System.out.println("任务id=" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
            //完成任务
            taskService.complete(task.getId());
        }else{
            System.out.println("没有待办任务！");
        }

    }

    /**
     * 使用zip包进行批量的部署
     */
    @Test
    public void deployProcessByZip(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.流程部署
        //读取资源文件，构成inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.zip");
        //用inputStream 构造ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //使用压缩包的流进行流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        //4.输出结果
        System.out.println("流程部署的id=" + deploy.getId());
        System.out.println("流程部署的名称" + deploy.getName());

    }

    /**
     * 查询流程定义
     * ACT_RE_PROCDEF
     */
    @Test
    public void queryProcessDefinition(){
//      1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//      2.获取 repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//      3.获取ProcessDifinitionQuery对象
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
//      4.查询当前所有的流程定义
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion() //定义排序的字段
                .desc() //定义排序规则
                .list(); //返回所有结果
//      5.输出信息
        for(ProcessDefinition processDefinition : definitionList){
            System.out.println("流程定义ID:" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义key：" + processDefinition.getKey());
            System.out.println("流程定义版本:" + processDefinition.getVersion());

            System.out.println("资源名:" + processDefinition.getResourceName());
        }
    }

    /**
     * 删除流程部署信息
     * 操作表：
     * ACT_RE_DEPLOYMENT
     * ACT_RE_PROCDEF
     * ACT_RU_EVENT_SUBSCR
     * ACT_RU_IDENTITYLINK
     * ACT_GE_BYTEARRAY
     * 1.不会删除历史表信息
     * 2.当前的流程如果没有完成，想要删除的话需要使用特殊方式。
     *  原理就是 级联删除
     */
    @Test
    public void deleteDeployMent(){
//        1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取 repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.通过部署id来删除流程部署信息
        String deploymentId = "2501";

//        repositoryService.deleteDeployment(deploymentId);
//        参数1：部署编号  参数2：级联删除（可以清理未完成的流程）
        repositoryService.deleteDeployment(deploymentId,true);
    }

    /**
     * 下载资源文件
     * 方案1： 使用Activiti提供的api，来下载资源文件，保存到文件目录
     * 方案2： 自己写代码从数据库中下载文件下来，使用jdbc对blob类型，clob类型数据
     *  读出来，保存到文件目录
     * 解决Io操作，comm-io.jar
     * 这里使用我们方案1，Activiti提供的api:RespositoryService
     */
    @Test
    public void getDeployment() throws IOException {
//        1.得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2.获取api,RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3.获取查询对象 ProcessDefinitionQuery查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
//        4.通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
//        5.通过RepositoryService.传递部署id参数，读取资源信息(png和bpmn)
        String pngName = processDefinition.getDiagramResourceName();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);

        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
//        6.构造OutPutStream流
        File pngFile = new File("e:/evection01.png");
        File bpmnFile = new File("e:/evection01.bpmn");

        FileOutputStream pngOutPutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutPutStream = new FileOutputStream(bpmnFile);
//        7.输入流，输出流的转换
        IOUtils.copy(pngInput,pngOutPutStream);
        IOUtils.copy(bpmnInput,bpmnOutPutStream);
//        8.关闭流
        pngOutPutStream.close();
        bpmnOutPutStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
//        获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
//        获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
//        查询 actinst表
        List<HistoricActivityInstance> activityInstances = instanceQuery
                .processInstanceId("5001")
                .orderByHistoricActivityInstanceStartTime().desc()
                .list();
//        输出
        for(HistoricActivityInstance historicActivityInstance : activityInstances){
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getProcessDefinitionId());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("------------------------------->");
        }
    }
}
