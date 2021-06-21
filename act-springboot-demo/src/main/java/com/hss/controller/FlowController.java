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
}
