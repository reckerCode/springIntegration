package com.springIntegration.services.service;

import com.springIntegration.services.models.Employee;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * The service activator is the endpoint type for connecting any Spring-managed
 * object to an input channel so that it may play the role of a service.
 * <p>
 * The request goes from Gateway(request channel) to Service(input channel)
 */
@Service
public class EmployeeService {

    @ServiceActivator(inputChannel = "request-emp-name-channel")
    public void getEmployeeName(Message<String> name) {
        MessageChannel replyChannel = (MessageChannel) name.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(name);
        } else {
            System.out.println("No channel found");
        }
    }


    /**
     * For the below three methods, the message flow from hireEmployee() to processEmployee() as the output channel
     * "process-emp-channel" of the hireEmployee becomes the input channel for processEmployee. Similarly, the message
     * from processEmployee() to getEmployeeStatus() as the output channel "get-emp-status-channel" of the
     * processEmployee() becomes the input channel for getEmployeeStatus()
     *
     * @param employee
     * @return
     */
    @ServiceActivator(inputChannel = "request-hire-emp-channel",
            outputChannel = "process-emp-channel")
    public Message<Employee> hireEmployee(Message<Employee> employee) {
        return employee;
    }

    @ServiceActivator(inputChannel = "process-emp-channel",
            outputChannel = "get-emp-status-channel")
    public Message<Employee> processEmployee(Message<Employee> employee) {
        employee.getPayload().setEmployeeStatus("Permanent");
        return employee;
    }

    @ServiceActivator(inputChannel = "get-emp-status-channel")
    public void getEmployeeStatus(Message<Employee> employee) {
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(employee);
        } else {
            System.out.println("No channel found");
        }
    }

    //Transformer
    @ServiceActivator(inputChannel = "emp-status-channel",
            outputChannel = "output-channel")
    public Message<String> convertToUpperCase(Message<String> message) {
        String payload = message.getPayload().toUpperCase();
        return MessageBuilder.withPayload(payload)
                .copyHeaders(message.getHeaders())
                .build();
    }
    // Common output channel

    @ServiceActivator(inputChannel = "output-channel")
    public void consumeStringMessage(Message<String> message) {
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(message);
        }
    }
}
