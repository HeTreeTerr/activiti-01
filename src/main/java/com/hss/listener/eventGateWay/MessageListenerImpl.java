package com.hss.listener.eventGateWay;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class MessageListenerImpl implements TaskListener,ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("Message Event Task Is Running");
    }

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("Message Event Execution Is Running");
    }
}
