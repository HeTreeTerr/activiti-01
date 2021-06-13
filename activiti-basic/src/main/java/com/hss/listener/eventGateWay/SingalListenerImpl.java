package com.hss.listener.eventGateWay;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class SingalListenerImpl implements TaskListener,ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("Singal Event Task Is Running");
    }

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("Singal Event Execution Is Running");
    }
}
