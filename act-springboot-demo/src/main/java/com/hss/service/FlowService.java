package com.hss.service;

import com.hss.entity.FlowInfo;
import com.hss.mapper.FlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FlowService {

    @Autowired
    private ActFlowCommService actFlowCommService;

    @Autowired
    private FlowMapper flowMapper;

    /**
     * 查询用户任务
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findUserTask(Long userId) {
        List<Map<String, Object>> list = actFlowCommService.myTaskList(userId.toString());
        return list;
    }

    /**
     * 查询所有流程
     * @return
     */
    public List<FlowInfo> findAllFlow() {
        return flowMapper.selectFlowList();
    }

    /**
     * 查询单个流程
     * @param id
     * @return
     */
    public FlowInfo findOneFlow(Long id){
        return flowMapper.selectOneFlow(id);
    }

    /**
     * 更新部署状态
     * @param id
     * @return
     */
    public int updateDeployState(Long id){
        return flowMapper.updateFlowDeployState(id);
    }
}
