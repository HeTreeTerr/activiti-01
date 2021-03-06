package com.hss.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请中的流程变量对象
 */
public class Evection implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 出差单的名字
     */
    private String ecectionName;

    /**
     * 出差天数
     */
    private Double num;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出差原因
     */
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEcectionName() {
        return ecectionName;
    }

    public void setEcectionName(String ecectionName) {
        this.ecectionName = ecectionName;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
