package com.hss.listener.eventGateWay;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class TimerListenerImpl implements TaskListener,ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("Timer Event Task Is Running");
    }

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("Timer Event Execution Is Running");
    }
}
