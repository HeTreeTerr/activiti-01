package com.hss.controller;

import com.hss.entity.FlowInfo;
import com.hss.service.ActFlowCommService;
import com.hss.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 流程管理
 */
@RestController
public class FlowController {

    @Autowired
    private FlowService flowService;

    @Autowired
    private ActFlowCommService actFlowCommService;

    /**
     * 查询所有流程
     * @return
     */
    @GetMapping("/flow/findAll")
    public List<FlowInfo> findAllFlow(){
        return flowService.findAllFlow();
    }

    /**
     * 部署流程
     * @param request
     * @return 0-部署失败  1- 部署成功  2- 已经部署过
     */
    @PutMapping("/flow/deployment/{id}")
    public Integer deployment(HttpServletRequest request, @PathVariable(name = "id")Long id){
//        1.由id查询流程信息
        FlowInfo flowInfo = flowService.findOneFlow(id);
//        2.校验状态
        if(flowInfo.getState() == 0){
            return 2;
        }
//        3.流程部署
        actFlowCommService.saveNewDeploy(flowInfo);
//        4.修改部署状态
        return flowService.updateDeployState(id);
    }

    /**
     * 查询用户任务
     * @param request
     * @return
     */
    @GetMapping("/flow/findUserTask")
    public List<Map<String,Object>> findUserTask(HttpServletRequest request){
        Long userId = (Long)request.getSession().getAttribute("userid");
        return flowService.findUserTask(userId);
    }

    /**
     * 查询任务详细信息
     * @param request
     * @return
     */
    @GetMapping("/flow/findTaskInfo")
    public List<Map<String,Object>> findTaskInfo(HttpServletRequest request){
        Long userId = (Long)request.getSession().getAttribute("userid");
        return flowService.findTaskInfo(userId);
    }

    /**
     * 查询
     * @return
     */
    @GetMapping("/flow/findFlowTask/{id}")
    public Map<String,Object> findFlowTask(@PathVariable(name = "id")Long id){
        String businessKey = "evection:"+id;
        actFlowCommService.searchHistory(businessKey);
        return null;
    }

    /**
     * 完成任务
     * @param request
     */
    @PutMapping("/flow/completeTask/{taskId}")
    public void completeTask(HttpServletRequest request,@PathVariable("taskId")String taskId){
        Long userId = (Long)request.getSession().getAttribute("userid");
        String formKey = "evection";
        String prefix = formKey+":";
        String beanName = formKey + "Service";

        flowService.completeTask(taskId,userId,beanName,prefix);
    }
}
