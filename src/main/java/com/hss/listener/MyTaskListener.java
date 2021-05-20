package com.hss.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("--------->myTaskListener.notify触发<----------");
//        触发事件名称
        String eventName = delegateTask.getEventName();
//        当前任务名
        String name = delegateTask.getName();
        if("创建申请".equals(name) && "create".equals(eventName)){
            delegateTask.setAssignee("zhao_si");
        }
        if("任务审核".equals(name) && "create".equals(eventName)){
            delegateTask.setAssignee("qian_boss");
        }
    }
}
