package com.hss.mapper;

import com.hss.entity.FlowInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowMapper {

    @Select("select * from tb_flow order by createtime desc")
    List<FlowInfo> selectFlowList();

    @Update("update tb_flow set state = 0 where id=#{id}")
    int updateFlowDeployState(Long id);

    @Select("select * from tb_flow where id=#{id}")
    FlowInfo selectOneFlow(Long id);
}
