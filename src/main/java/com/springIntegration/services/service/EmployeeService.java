package com.springIntegration.services.service;

import com.springIntegration.services.enums.Department;
import com.springIntegration.services.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The service activator is the endpoint type for connecting any Spring-managed
 * object to an input channel so that it may play the role of a service.
 * <p>
 * The request goes from Gateway(request channel) to Service(input channel)
 */
@Slf4j
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

    //Splitter
    @Splitter(inputChannel = "emp-managers-channel",
            outputChannel = "output-channel")
    List<Message<String>> splitMessageString(Message<?> message) {
        List<Message<String>> messages = new ArrayList<>();
        String[] msg = message.getPayload().toString().split(",");
        for (String s : msg) {
            Message<String> splitMessage = MessageBuilder.withPayload(s)
                    .copyHeaders(message.getHeaders())
                    .build();
            messages.add(splitMessage);
        }
        return messages;
    }

    //SI Filter sample code
    @Filter(inputChannel = "dev-emp-channel",
            outputChannel = "filter-output-channel",
            discardChannel = "discard-message-output-channel",
            discardWithinAdvice = "true")
    boolean filter(Message<?> message) {
        String msg = message.getPayload().toString();
        return msg.contains("Dev");
    }

    // Router
    @Router(inputChannel = "emp-department-channel")
    String getEmployeeDepartment(Message<Employee> message) {
        String deptRouter = null;
        switch (Department.valueOf(Objects.requireNonNull(message.getPayload().getEmployeeDepartment()).toString())) {
            case SALES:
                deptRouter = "sales-channel";
                break;
            case MARKETING:
                deptRouter = "marketing-channel";
                break;
            case GENERAL:
                deptRouter = "general-channel";
                break;
            default:
                deptRouter = "output-channel";
                break;
        }
        return deptRouter;
    }


    // Common output channel
    @ServiceActivator(inputChannel = "output-channel")
    public void consumeStringMessage(Message<String> message) {
        log.info("Common output channel: {}", message.getPayload());
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(message);
        }
    }

    @ServiceActivator(inputChannel = "filter-output-channel")
    public void consumeFilterMessage(Message<String> message) {
        log.info("Filter output channel: {}", message.getPayload());
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(message);
        }
    }

    @ServiceActivator(inputChannel = "discard-message-output-channel")
    public void discardedMessageConsumer(Message<String> message) {
        log.info("This message has been discarded: {}", message.getPayload());
    }

    // Router Channels
    @ServiceActivator(inputChannel = "sales-channel")
    public void salesChannelRouterServiceActivator(Message<Employee> employee) {
        log.info("Sales channel router service activator: {}", employee.getPayload());
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(employee);
        } else {
            System.out.println("No channel found");
        }
    }
    @ServiceActivator(inputChannel = "marketing-channel")
    public void marketingChannelServiceActivator(Message<Employee> employee) {
        log.info("Marketing channel service activator: {}", employee.getPayload());
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(employee);
        } else {
            System.out.println("No channel found");
        }
    }
    @ServiceActivator(inputChannel = "general-channel")
    public void generalChannelServiceActivator(Message<Employee> employee) {
        log.info("General channel service activator: {}", employee.getPayload());
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(employee);
        } else {
            System.out.println("No channel found");
        }
    }
}
