package com.hss.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;


/**
 * 流程监听器
 */
public class ExampleExecutionListenerOne implements ExecutionListener {

    private Expression message;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("---------触发监听器--------");

        delegateExecution.setVariable("variableSetInExecutionListener", "firstValue");
        delegateExecution.setVariable("eventReceived", delegateExecution.getEventName());

        System.out.println("参数接收：" + message.getValue(delegateExecution).toString());
    }

}
