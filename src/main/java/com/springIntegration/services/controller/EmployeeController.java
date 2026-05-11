package com.springIntegration.services.controller;

import com.springIntegration.services.exception.GenericException;
import com.springIntegration.services.gateway.EmployeeGateway;
import com.springIntegration.services.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrate")
public class EmployeeController {
    @Autowired
    public EmployeeGateway employeeGateway;

    /**
     * Here the below controller method called the gateway method and the message goes to the request channel of the
     * gateway to the input channel of the service and then finally return the message as the output.
     *
     * @param name
     * @return name
     */
    @GetMapping(value = "{name}")
    public String getEmployeeNameFromService(@PathVariable("name") String name) {
        return employeeGateway.getEmployeeName(name);
    }

    @PostMapping("/hireEmployee")
    public Employee HireEmployee(@RequestBody Employee employee) {
        Message<Employee> replyMessage = employeeGateway.hireEmployee(employee);
        return replyMessage.getPayload();
    }

    /**
     * A transformer takes a message from a channel and created a new message containing converted payload to message
     * structure.
     */

    @GetMapping(value = "/processEmployeeStatus/{status}")
    public String processEmployeeStatus(@PathVariable("status") String status) {
        return employeeGateway.processEmployeeStatus(status);
    }

    //splitter

    /**
     * The splitter is a SI component whose role is to partition a message into several parts and send the resulting
     * messages to be processed independently
     */
    @GetMapping(value = "/getManagerList/{managers}")
    public String getManagerList(@PathVariable("managers") String managers) {
        return employeeGateway.getManagerList(managers);
    }

    //Filter

    /**
     * Message filters are used to decide whether a message should be passed along or dropped based on some criteria.
     */
    @ResponseStatus()
    @ExceptionHandler(GenericException.class)
    @GetMapping(value = "/getEmployeeIfADeveloper/{empDesignation}")
    public String getEmployeeIfADeveloper(@PathVariable("empDesignation") String empDesignation) {
        return employeeGateway.getEmployeeIfADeveloper(empDesignation);
    }

    //Router

    /**
     * A code component responsible for directing a message to a specific message channels based on defined conditions
     * or the message's content. It acts as a dynamic traffic controller between an input channel and multiple potential
     * output channels.
     * <p/>
     * Routers consume messages from a channel and forward each consumed message to one or more different message
     * channel depending on a set of conditions.
     */

    @GetMapping(value = "/getEmployeeDepartment")
    public Employee getEmployeeDepartment(@RequestBody Employee employee) {
        return employeeGateway.getEmployeeDepartment(employee);
    }
}
